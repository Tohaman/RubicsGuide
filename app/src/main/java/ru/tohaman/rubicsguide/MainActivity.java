package ru.tohaman.rubicsguide;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import ru.tohaman.rubicsguide.plltestgame.PLLTestActivity;
import ru.tohaman.rubicsguide.about.AboutActivity;
import ru.tohaman.rubicsguide.about.AboutFragment;
import ru.tohaman.rubicsguide.blind.BlindMenuActivity;
import ru.tohaman.rubicsguide.g2f.G2FActivity;
import ru.tohaman.rubicsguide.listpager.ListActivity;
import ru.tohaman.rubicsguide.listpager.ListFragment;
import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.timer.TimerActivity;
import ru.tohaman.rubicsguide.util.IabBroadcastReceiver;
import ru.tohaman.rubicsguide.util.IabBroadcastReceiver.IabBroadcastListener;
import ru.tohaman.rubicsguide.util.IabHelper;
import ru.tohaman.rubicsguide.util.IabHelper.IabAsyncInProgressException;;
import ru.tohaman.rubicsguide.util.IabResult;
import ru.tohaman.rubicsguide.util.Inventory;
import ru.tohaman.rubicsguide.util.Purchase;

public class MainActivity extends SingleFragmentActivity implements IabBroadcastListener,
        DialogInterface.OnClickListener, MainFragment.Callbacks, ListFragment.Callbacks {
    private String phase;
    private static long back_pressed;
    private SharedPreferences sp;
    private int count;
    private FiveStarFragment FSF;
    private Boolean neverAskRate;

    // Пробуем добавить платежи внутри программы https://xakep.ru/2017/05/23/android-in-apps/
    // Debug tag, для записи в лог
    static final String TAG = "RubicsGuide";
    // Пользователь уже платил?
    boolean mIsPremium = false;
    // SKUs для продуктов: the premium upgrade (non-consumable) and gas (consumable)
    static final String SKU_PREMIUM = "medium_donation";
    static final String SKU_GAS = "small_donation";
    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;
    // The helper object
    IabHelper mHelper;
    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;



    @Override
    protected Fragment createFragment () {
        return new MainFragment();
    }

    @Override
    protected int getLayoutResId () {
        return R.layout.activity_autoframe;
    }

    @Override
    // действие для большого (правого) фрагмента двухфрагментной активности
    public void onItemSelected(ListPager listPager) {
        Toast.makeText(this,listPager.getDescription(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            phase = savedInstanceState.getString("phase");
        } else {
            phase = "ABOUT";
        }

        // load game data from PlayMarket
        loadData();
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiFFr98uzF/2DXWP6fa+2gjHV4VoXdj8KUb5Yoeg46Uj31nkDemyhksAWuNyfVpbyYBo5B7hHxeQj4ygMRlQa9GBbgh7VnwI5QTMVO+JVWaogzC1v14MwoOrynKs82TVqxXJLFykQb1fpc5KEmfj8CuafYv9UCWElH/zV/GKU6eyaBz6Fvyv3xCbjfuk9S6jdJG6UTrGxgUpIAuchkbM2dhlyAqNsk3lDX1G0Xd+1AxD4+EGCyucWsgIMzz7PfTVUFysBU7pbdwjs3M5GaMPlfZYz+Zrg4Nj5s0AnLvuFhSHyIevT2BeNpfmzjMyRNA6rXn1zsHkezVyAQOhYIF8tQwIDAQAB";
        // Создаем helper, передаем context и public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        //TODO enable debug logging (Для полноценной версии надо поставить в false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // О нет, у нас проблемы.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Важно: Динамически сгенерированный слушатель броадкастовых сообщений о покупках.
                // Создаем его динамически, а не через <receiver> in the Manifest
                // потому что мы всегда вызываем getPurchases() при старте программы, этим мы можем
                // игнорировать любые броадкасты пока приложение не запущено.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(MainActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });

    }

    // Слушатель, который вызывается, когда мы законичили запрос к серверу о купленных товарах
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Хелпер был ликвидирован? If so, выходим.
            if (mHelper == null) return;

            // Не получилось?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Проверяем купленные товары. Обратите внимание, что проверяем для каждой покупки
             * to see if it's correct! See verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            // Проверяем, платил ли пользователь уже 100руб.
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

            // First find out which subscription is auto renewing
            // У нас подписок нет, так что пока это отключаем
//            Purchase gasMonthly = inventory.getPurchase(SKU_INFINITE_GAS_MONTHLY);
//            Purchase gasYearly = inventory.getPurchase(SKU_INFINITE_GAS_YEARLY);
//            if (gasMonthly != null && gasMonthly.isAutoRenewing()) {
//                mInfiniteGasSku = SKU_INFINITE_GAS_MONTHLY;
//                mAutoRenewEnabled = true;
//            } else if (gasYearly != null && gasYearly.isAutoRenewing()) {
//                mInfiniteGasSku = SKU_INFINITE_GAS_YEARLY;
//                mAutoRenewEnabled = true;
//            } else {
//                mInfiniteGasSku = "";
//                mAutoRenewEnabled = false;
//            }

            // The user is subscribed if either subscription exists, even if neither is auto
            // renewing
//            mSubscribedToInfiniteGas = (gasMonthly != null && verifyDeveloperPayload(gasMonthly))
//                    || (gasYearly != null && verifyDeveloperPayload(gasYearly));
//            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
//                    + " infinite gas subscription.");
//            if (mSubscribedToInfiniteGas) mTank = TANK_MAX;

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            // Проверяем платил ли 50 руб
            Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
//            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
//                Log.d(TAG, "We have gas. Consuming it.");
//                try {
//                    mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
//                } catch (IabAsyncInProgressException e) {
//                    complain("Error consuming gas. Another async operation in progress.");
//                }
//                return;
//            }

            updateUi();
            setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    @Override
    public boolean onMyOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_settings:
                Intent mIntent = new Intent(this, SettingsActivity.class);
                startActivity(mIntent);
                return true;
            case R.id.main_donate50r:
                Intent mIntent2 = new Intent(this, PLLTestActivity.class);
                startActivity(mIntent2);
                return true;
            case R.id.main_donate100r:
//                Intent mIntent2 = new Intent(this, PLLTestActivity.class);
//                startActivity(mIntent2);
                return true;

            default:
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    // действие для левого фрагмента двухфрагментной активности или основного фрагмента однофрагментной
    public void onMainItemSelected (int id) {
        phase = getResources().getStringArray(R.array.main_phase)[id];
        switch (phase) {
            case "BEGIN":
                Intent mIntent = ListActivity.newIntenet(this, phase);
                startActivity(mIntent);
                break;

            case "G2F":
                mIntent = new Intent(this, G2FActivity.class);
                startActivity(mIntent);
                break;

            case "BLIND":
                mIntent = new Intent(this,BlindMenuActivity.class);
                startActivity(mIntent);
                break;

            case "BASIC":
                if (findViewById(R.id.detail_fragment_container) == null) {
                    // для телефона вызываем новую активность
                    mIntent = ListActivity.newIntenet(this, phase);
                    startActivity(mIntent);
                } else {
                    // для планшета открываем во фрагменте
                    Fragment newDetail = ListFragment.newInstance(phase);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detail_fragment_container, newDetail)
                            .commit();
                }
                break;

            case "TIMER":
                mIntent = new Intent(this, TimerActivity.class);
                startActivity(mIntent);
                break;

            case "ABOUT":
                if (findViewById(R.id.detail_fragment_container) == null) {
                    // для телефона вызываем новую активность
                    mIntent = new Intent(this, AboutActivity.class);
                    startActivity(mIntent);
                    break;
                } else {
                    // для планшета открываем в правом фрагменте
                    Fragment newDetail = new AboutFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detail_fragment_container, newDetail)
                            .commit();
                }
        }
    }

    @Override
    public void onBackPressed() {
        //Если в течении секунды нажали второй раз, то выходим
        if (back_pressed + 1000 > System.currentTimeMillis()){
            super.onBackPressed();
        } else {
            // если программа запускалась больше 50 раз и флаг "больше не спрашивать" не установлен - выводим окно
            if ((count > 50) && (!neverAskRate)) {
                // обновим данные, на случай если были нажаты "Больше не спрашивать" или "Спросить позже"
                count = sp.getInt("startcount", 1);
                neverAskRate = sp.getBoolean("neverAskRate", false);
                if ((count > 50) && (!neverAskRate)) {
                    FSF = new FiveStarFragment();
                    // отменяем закрытие окна по кнопке back
                    FSF.setCancelable(false);
                    FSF.show(getSupportFragmentManager(), "RateUsPlease");
                }
            }
            Toast.makeText(getBaseContext(), "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("phase",phase);
    }

    @Override
    public void onResume() {
        super.onResume();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        count = sp.getInt("startcount", 1);
        neverAskRate = sp.getBoolean("neverAskRate", false);
    }

    @Override
    protected void onStart () {
        super.onStart();
        if (!phase.equals("BASIC")) { phase="ABOUT"; }
        if (findViewById(R.id.detail_fragment_container) != null) {
            if (phase.equals("ABOUT")) {
                Fragment newDetail = new AboutFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, newDetail)
                        .commit();
            } else { // Если не About, значит открываем окно
                Fragment newDetail = ListFragment.newInstance(phase);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, newDetail)
                        .commit();
            }
        }
    }

}

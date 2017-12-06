package ru.tohaman.rubicsguide;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import ru.tohaman.rubicsguide.util.IabHelper.IabAsyncInProgressException;
import ru.tohaman.rubicsguide.util.IabResult;
import ru.tohaman.rubicsguide.util.Inventory;
import ru.tohaman.rubicsguide.util.Purchase;

import static ru.tohaman.rubicsguide.DeveloperKey.base64EncodedPublicKey;

public class MainActivity extends SingleFragmentActivity implements IabBroadcastListener,
        MainFragment.Callbacks, ListFragment.Callbacks {
    private String phase;
    private static long back_pressed;
    private SharedPreferences sp;
    private int count;
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
    // Current amount of rub was paid
    int mTank;
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
        //Если повернули экран или вернулись в активность, то открываем ту фазу, которая была, иначе - О Программе
        if (savedInstanceState != null) {
            phase = savedInstanceState.getString("phase");
        } else {
            phase = "ABOUT";
        }

        // load game data from PlayMarket
        loadData();
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

//            updateUi();
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
                Log.d(TAG, "50rub button clicked; launching purchase flow for pay 50rub.");
                setWaitScreen(true);

                /* TODO: for security, generate your payload here for verification. See the comments on
                 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
                 *        an empty string, but on a production app you should carefully generate this. */
                String payload = "";

                try {
                    mHelper.launchPurchaseFlow(this, SKU_GAS, RC_REQUEST,
                            mPurchaseFinishedListener, payload);
                } catch (IabAsyncInProgressException e) {
                    complain("Ошибка запуска потока оплаты. Другая асинхронная операция запущена.");
                    setWaitScreen(false);
                }
                return true;

            case R.id.main_donate100r:
                Log.d(TAG, "100rub button clicked; launching purchase flow for pay 100rub.");
                setWaitScreen(true);

                /* TODO: for security, generate your payload here for verification. See the comments on
                 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
                 *        an empty string, but on a production app you should carefully generate this. */
                String payload2 = "";

                try {
                    mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
                            mPurchaseFinishedListener, payload2);
                } catch (IabAsyncInProgressException e) {
                    complain("Ошибка запуска потока оплаты. Другая асинхронная операция запущена.");
                    setWaitScreen(false);
                }
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
                    FiveStarFragment FSF = new FiveStarFragment();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Покупка завершена: " + result + ", куплено: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Ошибка покупки: " + result);
                setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Ошибка покупки. Ошибка авторизации.");
                setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Покупка прошла успешно.");

            if (purchase.getSku().equals(SKU_GAS)) {
                // Пользователь задонатил 50 руб.
                Log.d(TAG, "Покупка = донат 50 руб.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabAsyncInProgressException e) {
                    complain("Ошибка оплаты. Другая асинхронная операция запущена.");
                    setWaitScreen(false);
                    return;
                }
            }
            else if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium upgrade!
                Log.d(TAG, "Покупка = донат 100 руб.");
                alert("Большое спаибо за поддержку");
                mIsPremium = true;
                updateUi();
                setWaitScreen(false);
            }
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
//            if (result.isSuccess()) {
//                // successfully consumed, so we apply the effects of the item in our
//                // game world's logic, which in our case means filling the gas tank a bit
//                Log.d(TAG, "Consumption successful. Provisioning.");
//                mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
//                saveData();
//                alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
//            }
//            else {
//                complain("Error while consuming: " + result);
//            }
            updateUi();
            setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    // Тут что-то делаем с интерфейсом в зависимости от сделанных покупок
    public void updateUi() {
        // update the car color to reflect premium status or lack thereof
//        ((ImageView)findViewById(R.id.free_or_premium)).setImageResource(mIsPremium ? R.drawable.premium : R.drawable.free);
//
//        // "Upgrade" button is only visible if the user is not premium
//        findViewById(R.id.upgrade_button).setVisibility(mIsPremium ? View.GONE : View.VISIBLE);
//
//        ImageView infiniteGasButton = (ImageView) findViewById(R.id.infinite_gas_button);
//        if (mSubscribedToInfiniteGas) {
//            // If subscription is active, show "Manage Infinite Gas"
//            infiniteGasButton.setImageResource(R.drawable.manage_infinite_gas);
//        } else {
//            // The user does not have infinite gas, show "Get Infinite Gas"
//            infiniteGasButton.setImageResource(R.drawable.get_infinite_gas);
//        }
//
//        // update gas gauge to reflect tank status
//        if (mSubscribedToInfiniteGas) {
//            ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(R.drawable.gas_inf);
//        }
//        else {
//            int index = mTank >= TANK_RES_IDS.length ? TANK_RES_IDS.length - 1 : mTank;
//            ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(TANK_RES_IDS[index]);
//        }
    }

    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {
        findViewById(R.id.fragment_container).setVisibility(set ? View.GONE : View.VISIBLE);
        findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    void saveData() {

        /*
         * WARNING: on a real application, we recommend you save data in a secure way to
         * prevent tampering. For simplicity in this sample, we simply store the data using a
         * SharedPreferences.
         */

        SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
        spe.putInt("tank", mTank);
        spe.apply();
        Log.d(TAG, "Saved data: tank = " + String.valueOf(mTank));
    }

    void loadData() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        mTank = sp.getInt("tank", 0);
        Log.d(TAG, "Loaded data: tank = " + String.valueOf(mTank));
    }

}

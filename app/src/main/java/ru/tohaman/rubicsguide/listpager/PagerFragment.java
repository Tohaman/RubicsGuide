package ru.tohaman.rubicsguide.listpager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import ru.tohaman.rubicsguide.CommentFragment;
import ru.tohaman.rubicsguide.R;

import java.util.List;

import static ru.tohaman.rubicsguide.DeveloperKey.DEVELOPER_KEY;
import static ru.tohaman.rubicsguide.g2f.G2FFragment.RubicPhase;


public class PagerFragment extends Fragment implements YouTubeThumbnailView.OnInitializedListener {

    private static final String ARG_ID = "ru.tohaman.rubicsguide.phase_id";   //передается номер страницы, которую надо открыть в пейджере
    private static final String DIALOG_COMMENT = "DialogComment";  //в этой "паре", передаем значение комментария для редактирования

    // YouTube
    private static String VIDEO_ID = "0TvO_rpG_aM";
    public static final int REQ_START_STANDALONE_PLAYER = 101;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private boolean canHideStatusBar = false;
    private Dialog errorDialog;

    //стр.249
    private static final int REQUEST_COMMENT = 0;

    private ListPager mListPager;
    private ImageView mImageView;
    private TextView mTitleField;
    private TextView mDescriptionField;
    private TextView mCommentField;
//    private WebView mWebView; //Альтернатива mDescriptionField (textview)
    private YouTubeThumbnailView thumbnailView;

    public PagerFragment() {
    }

    public static PagerFragment newInstance(String Id, String phase) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID, Id);    //Передали номер страницы
        args.putSerializable(RubicPhase, phase);    //Передали номер страницы
        PagerFragment fragment = new PagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String stringId = (String) getArguments().getSerializable(ARG_ID);   //Приняли номер страницы
        String phase = (String) getArguments().getSerializable(RubicPhase);  //Приняли название фазы (PLL,OLL,...)
        setHasOptionsMenu(true);
        int id = Integer.parseInt(stringId);
        mListPager = ListPagerLab.get(getActivity()).getPhaseItem(id, phase);

    }

    @Override
    public void onPause() {
        super.onPause();
//        PLLLab.get(getActivity()).updatePLL(mPLL); обновление комментариев
    }


    public void onResume(){
        super.onResume();
        boolean isLandscape = false;
        int currentOrientation = getActivity().getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandscape = true;
        } else {
            isLandscape = false;
        }
        if (canHideStatusBar && this.isVisible() && isLandscape) {
//            Utils.hideStatusBar(getActivity());
            canHideStatusBar = false;
        }
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit_comment:
                //вызов окна редактирования комментария
                FragmentManager manager = getFragmentManager();
                CommentFragment dialog = CommentFragment.newInstance(mListPager.getComment(),"Комментарий:");
                dialog.setTargetFragment(PagerFragment.this, REQUEST_COMMENT);
                dialog.show (manager, DIALOG_COMMENT);
                return true;
            default:
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager, container, false);

        mImageView = (ImageView) v.findViewById(R.id.pager_imageView);
        mImageView.setImageResource (mListPager.getIcon());

        mTitleField = (TextView) v.findViewById(R.id.pager_title_text);
        mTitleField.setText(mListPager.getTitle());

        if (!VIDEO_ID.equals("")) {
            thumbnailView = (YouTubeThumbnailView) v.findViewById(R.id.pager_youtube);
            thumbnailView.initialize(DEVELOPER_KEY,this);
            thumbnailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    //String youtubeURL = mPLL.getUrl();

                    intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(),DEVELOPER_KEY,mListPager.getUrl(),1000,true,true);
                    if (intent !=null) {
                        if (canResolveIntent(intent)) {
                            canHideStatusBar = true;
                            startActivityForResult(intent,REQ_START_STANDALONE_PLAYER);
                        } else {
                            YouTubeInitializationResult.SERVICE_MISSING.getErrorDialog(getActivity(),REQ_RESOLVE_SERVICE_MISSING).show();
                        }
                    }
                }
            });
        }

        // Немного преобразуем текст для корректного отображения.
        String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
        String description = String.format(text,getString(mListPager.getDescription()));
//        description = description.replace("#", "%23");
        description = description.replace("%%", "%25");
//        description = description.replace("\\", "%27");
//        description = description.replace("?", "%3f");

        // Android 7.0 ака N (Nougat) = API 24, начиная с версии Андроид 7.0 вместо HTML.fromHtml (String)
        // лучше использовать HTML.fromHtml (String, int), где int различные флаги, влияющие на отображение html
        // аналогично для метода HTML.fromHtml (String, ImageGetter, TagHandler) -> HTML.fromHtml (String, int, ImageGetter, TagHandler)
        // поэтому используем @SuppressWarnings("deprecation") перед объявлением метода и вот такую конструкцию
        // для преобразования String в Spanned. В принципе использование старой конструкции равноценно использованию
        // новой с флагом Html.FROM_HTML_MODE_LEGACY... подробнее о флагах-модификаторах на developer.android.com
        // В методе Html.fromHtml(String, imgGetter, tagHandler) - tagHandler - это метод, который вызывется, если
        // в строке встречается тэг, который не распознан, т.е. тут можно обрабатывать свои тэги
        // пока не используется (null), но все воозможно :)

        Spanned spanresult;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                spanresult = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY, imgGetter, null);
          } else {
                spanresult = Html.fromHtml(description, imgGetter, null);
        }

        mDescriptionField = (TextView) v.findViewById(R.id.description_text);
        mDescriptionField.setText(spanresult);
        mDescriptionField.setMovementMethod(LinkMovementMethod.getInstance());

        // Вместо TextView можно использовать WebView, но он медленее чем TextView, но
        // поддерживает выравнивание по ширине и изменение выравнивания (по центру/левому краю/ширине)
        // TextView поддерживает изменение выравнивания только начиная с Android 7.0
//        mWebView = (WebView) v.findViewById(R.id.pager_webview_text);
//        mWebView.setWebViewClient(new MyWebViewClient());
//        String baseUrl = "file:///android_res/drawable/";
//        mWebView.loadDataWithBaseURL(baseUrl, description , "text/html", "UTF-8", null);
//        mWebView.loadData(String.format(text,getString(mPLL.getDescription2())), "text/html; charset=UTF-8", null);

        mCommentField = (TextView) v.findViewById(R.id.pll_comment_text);
        mCommentField.setText(mListPager.getComment());
        mCommentField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                FragmentManager manager = getFragmentManager();
                CommentFragment dialog = CommentFragment.newInstance(mListPager.getComment(), "Комментарий:");
                dialog.setTargetFragment(PagerFragment.this, REQUEST_COMMENT);
                dialog.show (manager, DIALOG_COMMENT);
            }
        });

        return v;
    }


    //251
    // Обрабатываем результат вызова редактирования комментария
    // была ли нажата кнопка ОК? и изменился ли коммент
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        //Если все таки была нажата кнопка ОК
        if (requestCode == REQUEST_COMMENT) {
            // Получаем значение из EXTRA_Comment
            String string = (String) data.getSerializableExtra(CommentFragment.EXTRA_Comment);
            mListPager.setComment(string);
            ListPagerLab.get(getActivity()).updateListPager(mListPager);
            // Обновляем текст в пэйджере
            mCommentField.setText(mListPager.getComment());
        }
    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView thumbnailView, YouTubeThumbnailLoader thumbnailLoader) {
        thumbnailLoader.setVideo(mListPager.getUrl());
    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView thumbnailView, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            if (errorDialog == null || !errorDialog.isShowing()) {
                errorDialog = errorReason.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST);
                errorDialog.show();
            }
        } else {
            String errorMessage = "Ошибка инициализации YouTubePlayer";
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
    // ***** Private methods *************************************************************************************

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    // не знаю для чего, взято со Стэка из обработки какого-то Ютуба, можно попробовать удалить
    public boolean isCanHideStatusBar() {
        return canHideStatusBar;
    }

    public void setCanHideStatusBar(boolean canHideStatusBar) {
        this.canHideStatusBar = canHideStatusBar;
    }

    // Переназначаем WebViewClient на свой для обработки ссылкок
    // тут тоже есть deprecated метод, надо с ним разобраться
    // и еще сделать обработку только ссылок вида
    // <a href="rubic-activity://ytactivity?time=5:18&link=B3iSPvlr7PA"
    // а остальные обрабатывать стандартно, пока просто метод со СтэкОверФлоу, но работает :)
    // Если используется TextView, то он вообще не нужен
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("www.example.com")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    // метод ImageGetter для Html.fromHtml (String, ImageGetter, TagHandler)
    // получаем ссылку на ресурс из drawable по имени. Если имя с расширением, то расширение убираем (.png)
    // т.к. в дравабле-ресурсах оно не нужно. Используем конструкцию с @SuppressWarnings("deprecation")
    // для использования в разных версиях разных методов.
    // начиная с лолипопа, для получения ссылки на ресурс можно еще указывать тему (Theme).

    @SuppressWarnings("deprecation")
    private Html.ImageGetter imgGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            source = source.replace(".png", "");
            int resID = getResources().getIdentifier(source , "drawable", getActivity().getPackageName());
            //если картинка в drawable не найдена, то подсовываем заведомо существующую картинку
            if (resID == 0 ) {
                resID = getResources().getIdentifier("noscr" , "drawable", getActivity().getPackageName());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                drawable = getResources().getDrawable(resID, null);
            }else {
                drawable = getResources().getDrawable(resID);}

            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight());

            return drawable;
        }
    };


}

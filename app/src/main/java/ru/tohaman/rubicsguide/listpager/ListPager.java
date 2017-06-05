package ru.tohaman.rubicsguide.listpager;

/**
 * Created by Toha on 20.05.2017.
 */

public class ListPager {
    private String mPhase;  // фаза - PLL,OLL,Beginer,Blind etc
    private int mId;     // порядковый номер этапа в фазе
    private String mTitle;  // Название этапа
    private int mIcon;      // иконка этапа
    private int mDescription;  // описание этапа
    private String mUrl;        // ссылка на ютубвидео
    private String mComment;    // свой коммент к этапу

    public ListPager(String mPhase, int mId, String mTitle, int mIcon, int mDescription, String mUrl, String mComment){
        this.mPhase = mPhase;
        this.mId = mId;
        this.mTitle = mTitle;
        this.mIcon = mIcon;
        this.mDescription = mDescription;
        this.mUrl = mUrl;
        this.mComment = mComment;
    }

    public ListPager(String mPhase, int mId, String mTitle, int mIcon, String s) {
        this.mPhase = mPhase;
        this.mId = mId;
        this.mTitle = mTitle;
        this.mIcon = mIcon;
        this.mDescription = 0;
        this.mUrl = "";
        this.mComment = "";
    }

    //конструктор для объекта содержащего только фазу, номер в фазе и комментарий
     public ListPager(int Id, String Phase, String comment) {
         this.mPhase = Phase;
         this.mId = Id;
         this.mTitle = "";
         this.mIcon = 0;
         this.mDescription = 0;
         this.mUrl = "";
         this.mComment = comment;
     }


    public String getPhase() {
        return mPhase;
    }

    public void setPhase(String phase) {
        mPhase = phase;
    }

    public int getId() {
        return mId;
    }

    public String getStringId() { return String.valueOf(mId);}

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public int getDescription() {
        return mDescription;
    }

    public void setDescription(int description) {
        mDescription = description;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }
}

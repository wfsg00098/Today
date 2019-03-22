package com.eat.today;

public class ItemModel {
    private int headImg;// 保存说说头像，暂时未实现头像上传，初步估计是保存头像在服务器的地址。
    private int shuoId;// 说说的Id
    private String userName;// 发说说的用户名字
    private String shuoDate;// 发说说的日期
    private String shuoContent;// 说说的内容
    private String shuoPhoneModel;// 发说说的手机型号
    private int shuoPhraseNum;// 说说的点赞数目
    private Boolean isPhrase;//说说点赞的图标，主要判断当前用户是否点赞。

    public int getHeadImg() {
        return headImg;
    }

    public void setHeadImg(int headImg) {
        this.headImg = headImg;
    }

    public int getShuoId() {
        return shuoId;
    }

    public void setShuoId(int shuoId) {
        this.shuoId = shuoId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShuoDate() {
        return shuoDate;
    }

    public void setShuoDate(String shuoDate) {
        this.shuoDate = shuoDate;
    }

    public String getShuoContent() {
        return shuoContent;
    }

    public void setShuoContent(String shuoContent) {
        this.shuoContent = shuoContent;
    }

    public String getShuoPhoneModel() {
        return shuoPhoneModel;
    }

    public void setShuoPhoneModel(String shuoPhoneModel) {
        this.shuoPhoneModel = shuoPhoneModel;
    }

    public int getShuoPhraseNum() {
        return shuoPhraseNum;
    }
    public void setShuoPhraseNum(int shuoPhraseNum) {
        this.shuoPhraseNum = shuoPhraseNum;
    }

    public Boolean getIsPhrase() {
        return isPhrase;
    }

    public void setIsPhrase(Boolean isPhrase) {
        this.isPhrase = isPhrase;
    }
}

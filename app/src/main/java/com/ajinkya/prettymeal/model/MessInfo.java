package com.ajinkya.prettymeal.model;

public class MessInfo {
    private String messName;
    private String messOwnerName;
    private String PhoneNo;
    private String messAddress;
    private String MessLat;
    private String MessLong;
    private String MessDescription;
    private String SupportMail;
    private String SupportPhoneNo;
    private String MessType;
    private String todayVegMenu;
    private String todayNonVegMenu;
    private String price;
    private String bannerImgURL;

    public String getMessUID() {
        return MessUID;
    }

    public void setMessUID(String messUID) {
        MessUID = messUID;
    }

    private String MessUID;

    public String getMessLat() {
        return MessLat;
    }

    public void setMessLat(String messLat) {
        MessLat = messLat;
    }

    public String getMessLong() {
        return MessLong;
    }

    public void setMessLong(String messLong) {
        MessLong = messLong;
    }

    public String getMessDescription() {
        return MessDescription;
    }

    public void setMessDescription(String messDescription) {
        MessDescription = messDescription;
    }

    public String getSupportMail() {
        return SupportMail;
    }

    public void setSupportMail(String supportMail) {
        SupportMail = supportMail;
    }

    public String getSupportPhoneNo() {
        return SupportPhoneNo;
    }

    public void setSupportPhoneNo(String supportPhoneNo) {
        SupportPhoneNo = supportPhoneNo;
    }


    public String getMessName() {
        return messName;
    }

    public void setMessName(String messName) {
        this.messName = messName;
    }

    public String getMessOwnerName() {
        return messOwnerName;
    }

    public void setMessOwnerName(String messOwnerName) {
        this.messOwnerName = messOwnerName;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getMessAddress() {
        return messAddress;
    }

    public void setMessAddress(String messAddress) {
        this.messAddress = messAddress;
    }

    public String getMessType() {
        return MessType;
    }

    public void setMessType(String messType) {
        this.MessType = messType;
    }

    public String getTodayVegMenu() {
        return todayVegMenu;
    }

    public void setTodayVegMenu(String todayVegMenu) {
        this.todayVegMenu = todayVegMenu;
    }

    public String getTodayNonVegMenu() {
        return todayNonVegMenu;
    }

    public void setTodayNonVegMenu(String todayNonVegMenu) {
        this.todayNonVegMenu = todayNonVegMenu;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBannerImgURL() {
        return bannerImgURL;
    }

    public void setBannerImgURL(String bannerImgURL) {
        this.bannerImgURL = bannerImgURL;
    }

}

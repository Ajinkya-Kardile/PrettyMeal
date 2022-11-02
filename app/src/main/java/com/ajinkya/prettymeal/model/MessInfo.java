package com.ajinkya.prettymeal.model;

public class MessInfo {
    private String messName;
    private String messOwnerName;
    private String PhoneNo;
    private String messLocation;
    private String veg_nonVeg;
    private String todayVegMenu;
    private String todayNonVegMenu;
    private String price;
    private String bannerImgURL;


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

    public String getMessLocation() {
        return messLocation;
    }

    public void setMessLocation(String messLocation) {
        this.messLocation = messLocation;
    }

    public String getVeg_nonVeg() {
        return veg_nonVeg;
    }

    public void setVeg_nonVeg(String veg_nonVeg) {
        this.veg_nonVeg = veg_nonVeg;
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

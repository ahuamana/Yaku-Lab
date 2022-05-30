package edu.aha.agualimpiafinal.helper;

public class MySharedPreferences {

    public static MySharedPreferences instance = new MySharedPreferences();

    boolean isMobile;
    boolean isWifi;

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    public boolean isWifi() {
        return isWifi;
    }

    public void setWifi(boolean wifi) {
        isWifi = wifi;
    }
}

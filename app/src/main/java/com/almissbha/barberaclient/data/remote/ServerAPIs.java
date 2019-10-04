package com.almissbha.barberaclient.data.remote;

/**
 * Created by mohamed on 7/7/2017.
 */

public class ServerAPIs {
    private static String AppVersion="0";
    private static String ServerDomin="http://hakeem-sd.com/barbera/client";
   // private static String ServerDomin="http://192.168.137.1/";
    private static String Login_url=ServerDomin+"/login.php";
    private static String accept_order_url=ServerDomin+"/accept_order.php";

    public static String getAppVersion() {
        return AppVersion;
    }

    public static String getServerDomin() {
        return ServerDomin;
    }

    public static String getLogin_url() {
        return Login_url;
    }

    public static String getAccept_order_url() {
        return accept_order_url;
    }
}

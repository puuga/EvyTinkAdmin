package com.appspace.evytinkadmin.util;

/**
 * Created by siwaweswongcharoen on 6/6/2016 AD.
 */
public class Helper {
    public static String webUrl(String id, String barcode) {
        return "http://evbt.azurewebsites.net/docs/page/theme/evytinkbarcodeadmin.aspx"
                + "?evid=" + id + "&evbarcodeid=" + barcode;
    }

    public static String webUrl() {
        return "http://evbt.azurewebsites.net/docs/page/theme/evytinkbarcodeadmin.aspx";
    }
}

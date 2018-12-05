package com.gwokhou.deadline.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.net.URLEncoder;

public class DonateUtils {

    private static final String ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone";

    private static final String ALIPAY_PRE = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=";

    private static final String MY_QRCODE = "HTTPS://QR.ALIPAY.COM/FKX03656AL6MG8DA5TAT88?t=1534851150636";


    public static boolean startAlipayClient(Activity activity) {
        return StartIntentUtils.startIntentUrl(activity, doFormUri(MY_QRCODE));
    }

    private static String doFormUri(String urlCode) {
        try {
            urlCode = URLEncoder.encode(urlCode, "utf-8");
        } catch (Exception e) {

        }
        final String alipayQr = ALIPAY_PRE + urlCode;
        return alipayQr + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis();
    }


    public static boolean isInstalledAlipayClient(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(ALIPAY_PACKAGE_NAME, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


}

package com.mai.oaid.helper.system.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.mai.oaid.helper.utils.StringUtil;

import java.lang.reflect.Method;

public class RomUtil {

    private static final String TAG = "RomUtil";

    public static String sysProperty(String key, String defValue) {
        String res = null;
        try {
            @SuppressLint("PrivateApi") Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("get", String.class, String.class);
            res = (String) method.invoke(clazz, new Object[]{key, defValue});
        } catch (Exception e) {
            Log.w(TAG, "System property invoke error: " + e);
        }
        if (res == null) {
            res = "";
        }
        return res;
    }

    /**
     * 华硕
     */
    public static boolean isASUS(String manufacturer, String brand) {
        return manufacturer.equals("ASUS") || brand.equals("ASUS");
    }

    /**
     * 酷派
     */
    public static boolean isCoolpad(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.coolpad.deviceidsupport", 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 酷赛
     */
    public static boolean isCoosea() {
        return sysProperty("ro.odm.manufacturer", "").equalsIgnoreCase("PRIZE");
    }

    /**
     * 卓易
     */
    public static boolean isFreeme() {
        return StringUtil.isNotEmpty(sysProperty("ro.build.freeme.label", ""));
    }

    /**
     * 华为、EMUI
     */
    public static boolean isHW(String manufacturer, String brand) {
        return manufacturer.equals("HUAWEI") || brand.equals("HUAWEI") || brand.equals("HONOR")
                // 其他设备刷了EMUI
                || StringUtil.isNotEmpty(sysProperty("ro.build.version.emui", ""));
    }

    /**
     * 联想、乐檬、ZUI、摩托罗拉
     */
    public static boolean isLenovo(String manufacturer, String brand) {
        return manufacturer.equals("LENOVO") || brand.equals("LENOVO") || brand.equals("ZUK")
                || manufacturer.equals("MOTOLORA") || brand.equals("MOTOLORA");
    }

    /**
     * 魅族
     */
    public static boolean isMeizu(String manufacturer, String brand) {
        return manufacturer.equals("MEIZU") || brand.equals("MEIZU") || Build.DISPLAY.equalsIgnoreCase("FLYME");
    }

    /**
     * 小米、红米、黑鲨、MIUI
     */
    public static boolean isMi(String manufacturer, String brand) {
        return manufacturer.equals("XIAOMI") || brand.equals("XIAOMI") || brand.equals("REDMI")
                // 其他设备刷了MIUI
                || StringUtil.isNotEmpty(sysProperty("ro.miui.ui.version.name", ""))
                // 黑鲨
                || manufacturer.equals("BLACKSHARK") || brand.equals("BLACKSHARK");
    }

    /**
     * 努比亚
     */
    public static boolean isNubia(String manufacturer, String brand) {
        return manufacturer.equals("NUBIA") || brand.equals("NUBIA");
    }

    /**
     * oppo、realme、一加
     */
    public static boolean isOppo(String manufacturer, String brand) {
        return manufacturer.equals("OPPO") || brand.equals("OPPO")
                || brand.equals("REALME")
                || StringUtil.isNotEmpty(sysProperty("ro.build.version.opporom", ""))
                || manufacturer.equals("ONEPLUS") || brand.equals("ONEPLUS");
    }

    /**
     * 星星星
     */
    public static boolean isSamsung(String manufacturer, String brand) {
        return manufacturer.equals("SAMSUNG") || brand.equals("SAMSUNG");
    }

    /**
     * Vivo、IQOO
     */
    public static boolean isVivo(String manufacturer, String brand) {
        return manufacturer.equals("VIVO") || brand.equals("VIVO")
                || StringUtil.isNotEmpty(sysProperty("ro.vivo.version", ""));
    }

    /**
     * 中兴
     * <p>
     * Deprecated ZTE同MSA
     */
    @Deprecated
    public static boolean isZTE(String manufacturer, String brand) {
        return manufacturer.equals("ZTE") || brand.equals("ZTE");
    }

    /**
     * SSUI
     * 这是啥玩意的手机或系统？百度及谷歌都搜不到相关资料
     */
    @SuppressWarnings("unused")
    public static boolean isSSUI() {
        return StringUtil.isNotEmpty(sysProperty("ro.ssui.product", ""));
    }

}

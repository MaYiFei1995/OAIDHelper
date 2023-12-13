package com.mai.oaid.helper.system.device;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Pair;

import com.huawei.hms.ads.identifier.AdvertisingIdClient;
import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseDevice;

/**
 * 华为 (Huawei)
 */
public class HWDevice implements BaseDevice {

    private final Context context;

    public HWDevice(Context paramContext) {
        this.context = paramContext;
    }

    @Override
    public boolean isSupport() {
        if (context == null) {
            return false;
        }
        try {
            if (AdvertisingIdClient.isAdvertisingIdAvailable(context)) {
                return true;
            }
            PackageManager pm = context.getPackageManager();
            if (pm.getPackageInfo("com.huawei.hwid", 0) != null) {
                return true;
            } else if (pm.getPackageInfo("com.huawei.hwid.tv", 0) != null) {
                return true;
            } else if (pm.getPackageInfo("com.huawei.hms", 0) != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws Exception {
        try {
            AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(context);
            if (info == null) {
                return new Pair<>(null, OAIDError.STATUS_ERROR);
            }
            if (info.isLimitAdTrackingEnabled()) {
                return new Pair<>(null, OAIDError.LIMITED);
            }
            return new Pair<>(info.getId(), null);
        } catch (Throwable tr) {
            return new Pair<>(null, OAIDError.UNKNOWN_ERROR);
        }
    }

}

package com.mai.oaid.helper.system.device;

import android.content.Context;
import android.util.Pair;

import com.hihonor.ads.identifier.AdvertisingIdClient;
import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseDevice;

/**
 * 荣耀(Honor)
 */
public class HonorDevice implements BaseDevice {

    private final Context context;

    public HonorDevice(Context context) {
        this.context = context;
    }

    @Override
    public boolean isSupport() {
        try {
            return AdvertisingIdClient.isAdvertisingIdAvailable(context);
        } catch (Throwable tr) {
            return false;
        }
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws Exception {
        try {
            final AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(context);
            if (info == null) {
                return new Pair<>(null, OAIDError.STATUS_ERROR);
            }
            if (info.isLimit) {
                return new Pair<>(null, OAIDError.LIMITED);
            }
            return new Pair<>(info.id, null);
        } catch (Throwable tr) {
            return new Pair<>(null, OAIDError.UNKNOWN_ERROR);
        }
    }

}

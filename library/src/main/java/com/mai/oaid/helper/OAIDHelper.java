package com.mai.oaid.helper;

import android.content.Context;
import android.text.TextUtils;

import com.mai.oaid.helper.sdk.OAIDSdkHelper;
import com.mai.oaid.helper.sdk.OAIDSdkHelper25;
import com.mai.oaid.helper.system.OAIDSystemHelper;
import com.mai.oaid.helper.utils.StringUtil;

public class OAIDHelper implements OAIDCallback {

    private static class InstanceHolder {
        private static final OAIDHelper holder = new OAIDHelper();
    }

    public static OAIDHelper get() {
        return InstanceHolder.holder;
    }

    private String oaid;

    public void init(Context context) {
        if (context == null) {
            return;
        }
        try {
            if (OAIDSdkHelper.isSupport()) {
                OAIDSdkHelper.getOAID(context, this);
            } else if (OAIDSdkHelper25.isSupport()) {
                OAIDSdkHelper25.getOAID(context, this);
            }
        } catch (Throwable ignore) {

        }
        OAIDSystemHelper.tryGetOaid(context, this);
    }

    @Override
    public synchronized void onResult(String oaid) {
        if (StringUtil.isNotEmpty(oaid) && TextUtils.isEmpty(this.oaid)) {
            this.oaid = oaid;
        }
    }

}

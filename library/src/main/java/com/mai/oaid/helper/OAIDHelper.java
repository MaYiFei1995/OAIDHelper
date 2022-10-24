package com.mai.oaid.helper;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mai.oaid.helper.sdk.OAIDSdkHelper;
import com.mai.oaid.helper.sdk.OAIDSdkHelper25;
import com.mai.oaid.helper.system.OAIDSystemHelper;
import com.mai.oaid.helper.utils.StringUtil;

public class OAIDHelper {

    private static class InstanceHolder {
        private static final OAIDHelper holder = new OAIDHelper();
    }

    public static OAIDHelper get() {
        return InstanceHolder.holder;
    }

    private boolean isUseSdk = true;
    private Application context;
    private InitListener initListener;
    private boolean isInit;

    private String oaid = null;

    /**
     * 是否优先使用OAID-SDK获取OAID
     * 需要调用 {@link #init(Application, InitListener)} 方法前配置
     *
     * isUseSdk == true 时，会尝试通过SDK接口获取，返回失败后再尝试调用系统方法获取
     * 且当SDK版本大于1.0.25时，要确保调用初始化前已按照文档配置证书文件
     * isUseSdk == false 时，直接尝试调用系统方法获取
     *
     * @param isUseSdk 默认使用
     */
    public OAIDHelper useSdk(boolean isUseSdk) {
        this.isUseSdk = isUseSdk;
        return this;
    }

    /**
     * 初始化
     */
    public void init(@NonNull Application application, @Nullable InitListener initListener) {
        if (isInit) {
            return;
        }
        this.context = application;
        this.initListener = initListener;
        synchronized (OAIDHelper.class) {
            if (!isInit) {
                try {
                    if (isUseSdk) {
                        RetListener sdkListener = oaid -> {
                            // SDK获取优先
                            if (StringUtil.isNotEmpty(oaid)) {
                                OAIDHelper.this.oaid = oaid;
                                if (initListener != null) {
                                    initListener.onSuccess(OAIDHelper.this.oaid);
                                    clear();
                                }
                            } else {
                                // SDK获取失败，尝试通过系统获取
                                tryGetOAID();
                            }
                        };
                        if (OAIDSdkHelper.isSupport()) {
                            // SDK 1.0.26-2.0.0
                            OAIDSdkHelper.getOAID(context.getApplicationContext(), sdkListener);
                            return;
                        } else if (OAIDSdkHelper25.isSupport()) {
                            // SDK 1.0.25
                            OAIDSdkHelper25.getOAID(context.getApplicationContext(), sdkListener);
                            return;
                        }
                    }
                    // SDK环境错误或不开始SDK时，尝试通过系统获取
                    tryGetOAID();
                } catch (Throwable tr) {
                    Log.e("OAIDHelper", "init error", tr);
                    if (initListener != null) {
                        initListener.onFailure(OAIDError.UNKNOWN_ERROR);
                    }
                }
            }
            isInit = true;
        }
    }

    /**
     * 获取OAID，可能为空
     */
    @Nullable
    public String getOaid() {
        return this.oaid;
    }

    private void tryGetOAID() {
        // 通过系统获取OAID
        Pair<String, OAIDError> ret = OAIDSystemHelper.tryGetOaid(context.getApplicationContext());
        String oaid = ret.first;
        if (StringUtil.isNotEmpty(oaid)) {
            if (oaid.equals("00000000-0000-0000-0000-000000000000")) {
                if (initListener != null) {
                    initListener.onFailure(OAIDError.LIMITED);
                    return;
                }
            }
            this.oaid = oaid;
            if (initListener != null) {
                initListener.onSuccess(this.oaid);
            }
        } else {
            if (initListener != null) {
                initListener.onFailure(ret.second != null ? ret.second : OAIDError.RETURN_EMPTY);
            }
        }
        clear();
    }

    /**
     * 初始化成功后，清空缓存
     */
    private void clear() {
        initListener = null;
        context = null;
    }

    /**
     * 获取回调
     */
    public interface RetListener {

        /**
         * 返回
         *
         * @param oaid OAID
         */
        void onResult(String oaid);

    }

    /**
     * 初始化回调
     */
    public interface InitListener {

        /**
         * 初始化成功，已获取到OAID
         */
        void onSuccess(@Nullable String oaid);

        /**
         * 初始化失败，无法获取OAID
         *
         * @param error 错误信息
         */
        void onFailure(@NonNull OAIDError error);

    }

}

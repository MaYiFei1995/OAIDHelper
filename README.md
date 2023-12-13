# OAIDHelper

OAID 作为广告标识符，在`OAID_SDK_1.0.25`版本及以前支持直接使用，但`1.0.25`版本支持的设备覆盖不够全面，`1.0.26`版本及以后需要根据应用包名申请证书文件，且需要企业认证才可以申请账号。
企业用户需要通过[MSA官网](http://www.msa-alliance.cn/)申请账号和证书，使用官方SDK接入。
个人开发者可以尝试使用本工程获取设备的`OAID`
OAID-SDK 各版本文档与 AAR 见 [OAIDViewer](https://github.com/MaYiFei1995/OAIDViewer)

---

## 引入

### 增加仓库
```groovy
repositories {
    // ...
    maven { url 'https://jitpack.io' }
}
```

### 增加依赖
```groovy
rependencies {
    implementation 'com.github.MaYiFei1995:OAIDHelper:1.5.0'
}
```

### 注意

AGP4.0 以下版本会无法识别 AndroidManifest 中的`<queries>`标签，建议升级版本。

也可以参照[Android_CN_OAID_ISSUES_26](https://github.com/gzu-liyujiang/Android_CN_OAID/issues/26#issuecomment-854419703)，在 AndroidManifest 中增加 `<queries tools:node="remove" />`

## 使用

### 说明

```java
public class OAIDHelper {
    
    /**
     * 返回单例
     */
    public static OAIDHelper get();

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
    public OAIDHelper useSdk(boolean isUseSdk);

    /**
     * 初始化
     */
    public void init(@NonNull Application application, @Nullable InitListener initListener);

    /**
     * 获取OAID，可能为空，不会根据初始化结果重复获取
     */
    @Nullable
    public String getOaid();
    
    /**
     * 是否已初始化
     */
    public boolean isInit() {
        return this.isInit;
    }
    
}
```

```java
/**
 * 初始化回调
 */
public interface InitListener {

    /**
     * 初始化成功，已获取到OAID，可能为空
     * 返回为空时，可以根据需求使用其他信息替代
     */
    void onSuccess(@Nullable String oaid);

    /**
     * 初始化失败，无法获取OAID
     *
     * @param error 错误信息，见 errMsg
     */
    void onFailure(@NonNull OAIDError error);

}
```

### Demo
`AndroidId`等获取见[app/MainActivity](./app/src/main/java/com/mai/oaid/demo/MainActivity.kt)
```kotlin    
    OAIDHelper.get().useSdk(false).init(application, object : OAIDHelper.InitListener {

        override fun onSuccess(oaid: String?) {
            Log.e("Mai", "on init oaid success: $oaid")
        }

        override fun onFailure(error: OAIDError?) {
            Log.e("Mai", "on init oaid error: $error")
        }

    })
```

## 感谢

+ [gzu-liyujiang/Android_CN_OAID](https://github.com/gzu-liyujiang/Android_CN_OAID)
+ [OpenMico/MicoLauncher](https://github.com/OpenMico/MicoLauncher)

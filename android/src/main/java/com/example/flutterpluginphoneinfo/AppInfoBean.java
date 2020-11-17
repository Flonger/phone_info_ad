package com.example.flutterpluginphoneinfo;

public class AppInfoBean {
    private String userId;
    public String appName; // 应用名
    public String appId; // 包名
    public String versionName; // 版本名
    public int versionCode = 0; // 版本号
    public String systemApp;//app类型 0:第三方应用,1.系统内置内应用
    public String lastUpdateTime;
    public String firstInstallTime;
    private String systemType;

    public String getSystemType() {
        return systemType == null ? "" : systemType;
    }

    public void setSystemApp(String systemApp) {
        this.systemApp = systemApp;
    }

    public String getSystemApp() {
        return systemApp == null ? "" : systemApp;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFirstInstallTime(String firstInstallTime) {
        this.firstInstallTime = firstInstallTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getFirstInstallTime() {
        return firstInstallTime == null ? "" : firstInstallTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime == null ? "" : lastUpdateTime;
    }



    public String getAppName() {
        return appName == null ? "" : appName;
    }

    public String getAppId() {
        return appId == null ? "" : appId;
    }

    public String getVersionName() {
        return versionName == null ? "" : versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}

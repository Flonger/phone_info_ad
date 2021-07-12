package com.example.flutterpluginphoneinfo;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.wifi.ScanResult;
import android.provider.MediaStore;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import static android.content.Context.WIFI_SERVICE;

/**
 * FlutterpluginphoneinfoPlugin
 */
public class FlutterpluginphoneinfoPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Context applicationContext;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutterpluginphoneinfo");
        channel.setMethodCallHandler(this);
        applicationContext = flutterPluginBinding.getApplicationContext();
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutterpluginphoneinfo");
        channel.setMethodCallHandler(new FlutterpluginphoneinfoPlugin());
    }

    void getFaceBookKey() {
        try {
            int i = 0;
            PackageInfo info = applicationContext.getPackageManager().getPackageInfo( applicationContext.getPackageName(),  PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                i++;
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                //KeyHash 就是你要的，不用改任何代码  复制粘贴 ;
                Log.e("tyl","KeyHash="+KeyHash);
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPhoneData")) {
            List<ContactsBean> mailList = SystemUtils.getMailList(applicationContext);
            String base64Contact = SystemUtils.getBase64Contact(mailList);
//            List<AppInfoBean> applicationName = SystemUtils.getApplicationName(applicationContext);
//            String json = getJson(applicationName);
            result.success(base64Contact);
        } else if(call.method.equals("getApplicationInfo")){
            List<AppInfoBean> applicationName = SystemUtils.getApplicationName(applicationContext);
            String json = getJson(applicationName);
            result.success(json);
        } else if (call.method.equals("getWifiSSID")){
            getFaceBookKey();
            result.success(getWifiSSID(applicationContext));
        }else if (call.method.equals("getWifiList")){
            result.success(getWifiList(applicationContext));
        }else if (call.method.equals("getMemUnused")){
            result.success(getMemUnused(applicationContext));
        }else if (call.method.equals("getMemTotal")){
            result.success(getMemTotal());
        }else if (call.method.equals("isRoot")){
            result.success(isRoot());
        }else if (call.method.equals("getMacAddress")){
            result.success(getMacAddress());
        }else if (call.method.equals("getPicsInfo")){
            List<ImageInfo> imageList = SystemUtils.getImageList(applicationContext);
            String json = getJsonImage(imageList);
            result.success(json);
        }
        else {
            result.notImplemented();
        }
    }



    ///
    public static String getWifiSSID(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        int wifiState = wifiMgr.getWifiState();
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        return wifiId;
    }

    public static String getWifiList(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        List<ScanResult> scanWifiList = wifiManager.getScanResults();

        List<String> wifiList = new ArrayList<>();
        if (scanWifiList != null && scanWifiList.size() > 0) {
            for (int i = 0; i < scanWifiList.size(); i++) {
                ScanResult scanResult = scanWifiList.get(i);
                if (!scanResult.SSID.isEmpty()) {
                    wifiList.add(scanResult.SSID);
                }
            }
        }else {

        }
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        String json = gson.toJson(wifiList);

        return json;
    }

    // 获得可用的内存
    public static String getMemUnused(Context mContext) {
        long memUnused;
        // 得到ActivityManager
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 创建ActivityManager.MemoryInfo对象
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memInfo);

        // 取得剩余的内存空间
        memUnused = memInfo.availMem / 1024;
        return memUnused+"kB";
    }
    // 获得总内存
    public static String getMemTotal() {
        long memTotal;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // beginIndex
        int begin = content.indexOf(':');
        // endIndex
        int end = content.indexOf('k');
        // 截取字符串信息
        content = content.substring(begin + 1, end).trim();
        memTotal = Integer.parseInt(content);
        return memTotal+"kB";
    }

    //判断手机是否root
    public static String isRoot() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";

        if (new File(binPath).exists() && isCanExecute(binPath)) {
            return "1";
        }
        if (new File(xBinPath).exists() && isCanExecute(xBinPath)) {
            return "1";
        }
        return "0";
    }

    private static boolean isCanExecute(String filePath) {
        java.lang.Process process = null;
        try {
            process = Runtime.getRuntime().exec("ls -l " + filePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 获取mac地址（适配所有Android版本）
     * @return
     */
    public static String getMacAddress(){
        /*获取mac地址有一点需要注意的就是android 6.0版本后，以下注释方法不再适用，不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，这是googel官方为了加强权限管理而禁用了getSYstemService(Context.WIFI_SERVICE)方法来获得mac地址。*/
        //    String macAddress= "";
//    WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
//    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//    macAddress = wifiInfo.getMacAddress();
//    return macAddress;
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            if (addr == null) {
                return "02:00:00:00:00:02";
            }
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    private String getJson(List<AppInfoBean> appInfoBeans) {
        if (appInfoBeans == null || appInfoBeans.size() < 1) {
            return "";
        }
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        String json = gson.toJson(appInfoBeans).replace("\n", "").replace("\r", "").replace("\t", "").replace(" ", "");
        // return Base64.encodeToString(json.getBytes(), Base64.DEFAULT).replace("\n", "");
        return json;
    }
    private String getJsonImage(List<ImageInfo> appInfoBeans) {
        if (appInfoBeans == null || appInfoBeans.size() < 1) {
            return "";
        }
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        String json = gson.toJson(appInfoBeans).replace("\n", "").replace("\r", "").replace("\t", "").replace(" ", "");
        // return Base64.encodeToString(json.getBytes(), Base64.DEFAULT).replace("\n", "");
        return json;
    }
}

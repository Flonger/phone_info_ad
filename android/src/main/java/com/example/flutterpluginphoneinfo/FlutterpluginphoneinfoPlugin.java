package com.example.flutterpluginphoneinfo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

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
        }
        else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    private String getJson(List<AppInfoBean> appInfoBeans) {
        if (appInfoBeans == null || appInfoBeans.size() < 1)
            return "";
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        String json = gson.toJson(appInfoBeans).replace("\n", "").replace("\r", "").replace("\t", "").replace(" ", "");
        // return Base64.encodeToString(json.getBytes(), Base64.DEFAULT).replace("\n", "");
        return json;
    }
}

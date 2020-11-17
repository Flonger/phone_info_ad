package com.example.flutterpluginphoneinfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SystemUtils {
    /**
     * 跳转到应用详情界面
     */
    public static void toSetting(Context context) {
        try {
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            //startActivityForResult(localIntent, 10001);
           context.startActivity(localIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public synchronized static List<ContactsBean> getMailList(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                List<ContactsBean> contactsBeans = new ArrayList<>();
                String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.PhoneLookup.TIMES_CONTACTED, ContactsContract.PhoneLookup.CONTACT_LAST_UPDATED_TIMESTAMP};
                Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, cols, null, null, null);
                if (cursor != null) {
                    try {
                        while (cursor.moveToNext()) {
                            ContactsBean contactsBean = new ContactsBean();
                            // cursor.moveToPosition(i);
                            // 取得联系人名字
                            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                            int timesContactedIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.TIMES_CONTACTED);//总共联系了多少次
                            int timesIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.CONTACT_LAST_UPDATED_TIMESTAMP);//最后一次的添加时间
                            int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            // int emailFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email);
                            String name = cursor.getString(nameFieldColumnIndex);
                            String number = cursor.getString(numberFieldColumnIndex);
                            String timesContact = cursor.getString(timesContactedIndex);
                            String times = cursor.getString(timesIndex);
                            contactsBean.setName(name);
                            contactsBean.setPhone(number);
                            contactsBean.setContactTimes(timesContact);
                            contactsBean.setTimes(times);
                            contactsBeans.add(contactsBean);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ContactsBean contactsBean = new ContactsBean();
                        contactsBean.setName("Data Exception");
                        contactsBeans.add(contactsBean);
                    } finally {
                        cursor.close();
                    }

                }
                return contactsBeans;
            } else {
//                new AlertDialog.Builder(context)
//                        .setMessage("The permission is not open, you need to open the permission to use")
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //引导用户到设置中去进行设置
//                                SystemUtils.toSetting(context);
//                            }
//                        })
//                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //finish();
//                            }
//                        })
//                        .setCancelable(false)
//                        .create()
//                        .show();
                return null;
            }
        } else {
            List<ContactsBean> contactsBeans = new ArrayList<>();
            String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.PhoneLookup.TIMES_CONTACTED, ContactsContract.PhoneLookup.CONTACT_LAST_UPDATED_TIMESTAMP};
            Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, cols, null, null, null);
            if (cursor != null) {
                try {
                    while (cursor.moveToNext()) {
                        ContactsBean contactsBean = new ContactsBean();
                        // cursor.moveToPosition(i);
                        // 取得联系人名字
                        int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                        int timesContactedIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.TIMES_CONTACTED);//总共联系了多少次
                        int timesIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.CONTACT_LAST_UPDATED_TIMESTAMP);//最后一次的添加时间
                        int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        // int emailFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email);
                        String name = cursor.getString(nameFieldColumnIndex);
                        String number = cursor.getString(numberFieldColumnIndex);
                        String timesContact = cursor.getString(timesContactedIndex);
                        String times = cursor.getString(timesIndex);
                        contactsBean.setName(name);
                        contactsBean.setPhone(number);
                        contactsBean.setContactTimes(timesContact);
                        contactsBean.setTimes(times);
                        contactsBeans.add(contactsBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ContactsBean contactsBean = new ContactsBean();
                    contactsBean.setName("Data Exception");
                    contactsBeans.add(contactsBean);
                } finally {
                    cursor.close();
                }

            }
            return contactsBeans;
        }
    }

    /**
     * 获取手机里面所有应用的名称
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public synchronized static List<AppInfoBean> getApplicationName(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                ArrayList<AppInfoBean> appList = new ArrayList<>(); // 用来存储获取的应用信息数据
                try {
                    PackageManager pm = context.getPackageManager();
                    List<PackageInfo> list2 = pm.getInstalledPackages(0);
                    for (PackageInfo packageInfo : list2) {
                        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                            //第三方应用，否则系统应用
                            AppInfoBean appInfoBean = new AppInfoBean();
                            appInfoBean.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
                            appInfoBean.setAppId(packageInfo.packageName);
                            appInfoBean.setVersionName(packageInfo.versionName);
                            appInfoBean.setVersionCode(packageInfo.versionCode);
                            appInfoBean.setLastUpdateTime(packageInfo.lastUpdateTime + "");
                            appInfoBean.setFirstInstallTime(packageInfo.firstInstallTime + "");
                            appInfoBean.setSystemApp("0");
                            appList.add(appInfoBean);
                        }
//                    else {
//                        AppInfoBean appInfoBean = new AppInfoBean();
//                        appInfoBean.setUserId(DataManagerPanShi.getInstance().getLoginInfo().getUserId() + "");
//                        appInfoBean.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
//                        appInfoBean.setPackageName(packageInfo.packageName);
//                        appInfoBean.setVersionName(packageInfo.versionName);
//                        appInfoBean.setVersionCode(packageInfo.versionCode);
//                        appInfoBean.setLastUpdateTime(packageInfo.lastUpdateTime + "");
//                        appInfoBean.setFirstInstallTime(packageInfo.firstInstallTime + "");
////                        String time = DateUtilPanShi.formatTimehms(packageInfo.firstInstallTime, DateUtilPanShi.FULL_DATE_TIME_FORMAT_1);
////                        Log.e("xkff", appInfoBean.getAppName() + "=========" + time);
//                        appInfoBean.setIexpress("1");
//                        appList.add(appInfoBean);
//                    }
                    }
                    return appList;
                } catch (Exception e) {
                    e.printStackTrace();
                    AppInfoBean appInfoBean = new AppInfoBean();
                    appInfoBean.setAppName("Data Exception");
                    appList.add(appInfoBean);
                    return appList;
                }
            } else {
//                new AlertDialog.Builder(context)
//                        .setMessage("The permission is not open, you need to open the permission to use")
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //引导用户到设置中去进行设置
//                                SystemUtils.toSetting(context);
//                            }
//                        })
//                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //finish();
//                            }
//                        })
//                        .setCancelable(false)
//                        .create()
//                        .show();
                return null;
            }
        } else {
            ArrayList<AppInfoBean> appList = new ArrayList<>(); // 用来存储获取的应用信息数据
            try {
                PackageManager pm = context.getPackageManager();
                @SuppressLint("WrongConstant") List<PackageInfo> list2 = pm.getInstalledPackages(0);
                for (PackageInfo packageInfo : list2) {
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                        //第三方应用，否则系统应用
                        AppInfoBean appInfoBean = new AppInfoBean();
                        appInfoBean.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
                        appInfoBean.setAppId(packageInfo.packageName);
                        appInfoBean.setVersionName(packageInfo.versionName);
                        appInfoBean.setVersionCode(packageInfo.versionCode);
                        appInfoBean.setLastUpdateTime(packageInfo.lastUpdateTime + "");
                        appInfoBean.setFirstInstallTime(packageInfo.firstInstallTime + "");
                        appInfoBean.setSystemApp("0");
                        appList.add(appInfoBean);
                    }
                }
                return appList;
            } catch (Exception e) {
                e.printStackTrace();
                AppInfoBean appInfoBean = new AppInfoBean();
                appInfoBean.setAppName("Data Exception");
                appList.add(appInfoBean);
                return appList;
            }
        }
    }

    /**
     * 对通讯录进行64位加密
     */
    public static String getBase64Contact(List<ContactsBean> contactsBeans) {
        if (contactsBeans != null && contactsBeans.size() > 0) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            String json = gson.toJson(contactsBeans).replace("\n", "").replace(" ", "");
            return Base64.encodeToString(json.getBytes(), Base64.DEFAULT).replace("\n", "");
        } else {
            return "";
        }
    }


}
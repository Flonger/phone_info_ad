package com.example.flutterpluginphoneinfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
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
import android.provider.MediaStore;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


    public synchronized static List<ImageInfo> getImageList(final Context context) {
        ContentResolver cr = context.getContentResolver();

        String columns[] = new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.ORIENTATION, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.SIZE, MediaStore.Images.Media.IS_PRIVATE};

        String selection = MediaStore.Images.Media.DATA + " LIKE ? ";
        String selectionArgs[] = new String[]{"%/DCIM/%"}; //100LGDSC;
        String sortOrder = MediaStore.Images.Media.DATA;

        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs, sortOrder);

        ArrayList<ImageInfo> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            String display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));

            int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
            float lantitude = cursor.getFloat(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
            float longitude = cursor.getFloat(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
            String date_taken = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
            String date_added = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
            String date_modified = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
            int is_private = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.IS_PRIVATE));
            int width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
            int height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));

            ImageInfo info = new ImageInfo();
            info.setName(display_name);
            info.setWidth(String.valueOf(width));
            info.setHeight(String.valueOf(height));
            info.setLatitude(String.valueOf(lantitude));
            info.setLongitude(String.valueOf(longitude));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Long addTime = Long.valueOf(date_added);
            Date date1 = new Date(addTime * 1000L);
            info.setAddTime(simpleDateFormat.format(date1));
            Long updateTime = Long.valueOf(date_modified);
            Date date2 = new Date(updateTime * 1000L);
            info.setUpdateTime(simpleDateFormat.format(date2));
            info.setModel(Build.MODEL);
            list.add(info);
        }
        return list;

    }

}
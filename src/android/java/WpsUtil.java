package com.yls.tech.plugin;

import com.yls.tech.plugin.Define;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;

import java.io.File;
import java.util.List;

public class WpsUtil {
    public WpsInterface wpsInterface;
    private WpsCloseListener wpsCloseListener = null;
    private WpsSave wpsSave;
    private Boolean canWrite;
    private Activity mActivity;
    private String fileUrl;
    private String userName = "yls-tech";

    public interface WpsSave {
        void saveinfo(String url);
    }

    public void setGetUrl(WpsSave wpsSave) {
        this.wpsSave = wpsSave;
    }

    public WpsUtil(WpsInterface wpsInterface, String fileUrl, Boolean canWrite, Activity activity) {
        this.wpsInterface = wpsInterface;
        this.fileUrl = fileUrl;
        this.canWrite = canWrite;
        this.mActivity = activity;
    }

    public void open() {
        try {
            wpsCloseListener = new WpsCloseListener();
            IntentFilter filter = new IntentFilter(Define.OFFICE_SERVICE_ACTION);
            filter.addAction("com.kingsoft.writer.back.key.down");//按下返回键
            filter.addAction("com.kingsoft.writer.home.key.down");//按下home键
            filter.addAction("cn.wps.moffice.file.save");//保存
            filter.addAction("cn.wps.moffice.file.close");//关闭
            mActivity.registerReceiver(wpsCloseListener,filter);//注册广播
            openDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 打开本地文件
    private void openDocument() {
        try {
            Intent intent = mActivity.getPackageManager().getLaunchIntentForPackage("cn.wps.moffice_eng");

            Bundle bundle = new Bundle();
            //打开模式
            bundle.putString(Define.OPEN_MODE, Define.EDIT_MODE);
            bundle.putBoolean(Define.ENTER_REVISE_MODE, true);//以修订模式打开
            //bundle.putString(Define.OPEN_MODE, Define.READ_ONLY);
            bundle.putBoolean(Define.SEND_SAVE_BROAD, true);
            bundle.putBoolean(Define.SEND_CLOSE_BROAD, true);
            bundle.putBoolean(Define.HOME_KEY_DOWN, true);
            bundle.putBoolean(Define.BACK_KEY_DOWN, true);
            bundle.putBoolean(Define.ENTER_REVISE_MODE, true);
            bundle.putBoolean(Define.IS_SHOW_VIEW, false);
            bundle.putString(Define.USER_NAME, userName);
            bundle.putBoolean(Define.AUTO_JUMP, true);
            bundle.putBoolean(Define.CLEAR_TRACE,true);
            bundle.putBoolean(Define.CLEAR_BUFFER,true);
            //bundle.putBoolean(Define.CLEAR_FILE,true);
            //设置广播
            bundle.putString(Define.THIRD_PACKAGE, mActivity.getPackageName());
            //华为参数
            bundle.putBoolean("huawei_print_enable",true);
            intent.putExtras(bundle);

            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("cn.wps.moffice_eng", Define.CLASSNAME);
            //intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Log.d("------>>>File Url:", fileUrl);

            Uri uri = Uri.parse(fileUrl);
            Log.d("------>>>Url Path:", uri.getPath());

            File file = new File(uri.getPath());
            if (file == null || !file.exists()) {
                Log.e("------>>>File:", "打开失败，文件不存在！");
            }
            intent.setData(uri);
            intent.putExtras(bundle);
            String type = this.getMIMEType();
            intent.setDataAndType(Uri.fromFile(file), type);

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri contentUri = FileProvider.getUriForFile(mActivity, mActivity.getPackageName()+".fileProvider", file);
                intent.setDataAndType(contentUri, type);
            } else {
                intent.setDataAndType(Uri.fromFile(file), type);
            }*/
            mActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMIMEType() {
        String end = fileUrl.substring(fileUrl.lastIndexOf(".") + 1, fileUrl.length()).toLowerCase();
        String type = "";
        if (end.equals("mp3") || end.equals("aac") || end.equals("aac") || end.equals("amr") || end.equals("mpeg") || end.equals("mp4")) {
            type = "audio";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")) {
            type = "image";
        } else if (end.equals("doc") || end.equals("docx") || end.equals("pdf") || end.equals("txt")) {
            type = "application/msword";
            return type;
        } else {
            type = "*";
        }
        type += "/*";
        return type;
    }

    public interface WpsInterface {
        void doRequest(String filePath);
        void doFinish();
    }

    // 广播接收器
    private class WpsCloseListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals("cn.wps.moffice.file.save")) {
                    String fileSavePath = intent.getExtras().getString(Define.SAVE_PATH);
                    wpsSave.saveinfo(fileSavePath);
                    //Toast.makeText(context,fileSavePath,Toast.LENGTH_SHORT).show();
                    //callbackContext.success(fileSavePath);
                    if(canWrite) {
                        wpsInterface.doRequest(fileSavePath);
                    }
                } else if (intent.getAction().equals("cn.wps.moffice.file.close") || intent.getAction().equals("com.kingsoft.writer.back.key.down")) {
                    wpsInterface.doFinish();
                    mActivity.unregisterReceiver(wpsCloseListener);//注销广播
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void appBack() {
        //获取ActivityManager
        ActivityManager mAm = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
        for (ActivityManager.RunningTaskInfo rti : taskList) {
            if (rti.topActivity.getPackageName().equals(mActivity.getPackageName())) {
                Intent LaunchIntent = new Intent(Intent.ACTION_VIEW);
                LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ComponentName cn = new ComponentName(mActivity.getPackageName(), rti.topActivity.getClassName());
                LaunchIntent.setComponent(cn);
                mActivity.startActivity(LaunchIntent);
                break;
            }
        }
    }
}

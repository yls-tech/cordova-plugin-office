package com.yls.tech.plugin;

import android.util.Log;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import com.yls.tech.plugin.WpsUtil;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * This class echoes a string called from JavaScript.
 */
public class Office extends CordovaPlugin implements WpsUtil.WpsInterface {
	private WpsUtil wpsUtil;
    private String backUrl = "";
    private CallbackContext McallbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.McallbackContext = callbackContext;

        if (action.equals("open")) {
            String path = args.getString(0);
            this.open(path, callbackContext);
            return true;
        }
        return false;
    }

    private void open(String path, CallbackContext callbackContext) {
        if (path != null && path.length() > 0) {
			wpsUtil = new WpsUtil(this, path, true, cordova.getActivity());
            wpsUtil.open();

            wpsUtil.setGetUrl(new WpsUtil.WpsSave() {
                @Override
                public void saveinfo(String url) {
                    backUrl = url;
                    //Toast.makeText(cordova.getContext(),url,Toast.LENGTH_SHORT).show();
                }
            });
            callbackContext.success(path);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    @Override
    public void doRequest(String filePath) {
        Log.d("MainActivity", "这里处理你的文件保存事件");
    }

    @Override
    public void doFinish() {
        Log.d("MainActivity", "in doFinish");
        wpsUtil.appBack();
        McallbackContext.success(backUrl);
        closeWPS("cn.wps.moffice_eng");
    }

    private void closeWPS(String packageName) {
        ActivityManager am = (ActivityManager) cordova.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mRunningProcess = am.getRunningAppProcesses();
        int pid = -1;
        //int i = 1;
        for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess) {
            if (amProcess.processName.equals(packageName)) {
                pid = amProcess.pid;
                break;
            }
            Log.i("zhuming", "PID: " + amProcess.pid + "(processName=" + amProcess.processName + "UID=" + amProcess.uid + ")");
        }
        ActivityManager mAm = (ActivityManager) cordova.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        //mAm.forceStopPackage("com.bbk.audiofx");
        //mAm.killBackgroundProcesses(packageName);
        //android.os.Process.killProcess(pid);

    }
}

package com.yls.tech.plugin;

import android.util.Log;

import com.yls.tech.plugin.WpsUtil;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * This class echoes a string called from JavaScript.
 */
public class Office extends CordovaPlugin implements WpsUtil.WpsInterface {
	private WpsUtil wpsUtil;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("open")) {
            String path = args.getString(0);
            this.open(path, callbackContext);
            return true;
        }
        return false;
    }

    private void open(String path, CallbackContext callbackContext) {
        if (path != null && path.length() > 0) {
			wpsUtil = new WpsUtil(this, "", "", true, cordova.getActivity());
            wpsUtil.openDocument(new File(path.trim()));
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
    }
}

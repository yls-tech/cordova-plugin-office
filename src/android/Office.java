package cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * This class echoes a string called from JavaScript.
 */
public class Office extends CordovaPlugin {
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
}

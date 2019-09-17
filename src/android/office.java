package cordova.plugin.office;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class office extends CordovaPlugin {
    private WpsUtil wpsUtil;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("open")) {
            String message = args.getString(0);
            this.open(message, callbackContext);
            return true;
        }
        return false;
    }

    private void open(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            wpsUtil = new WpsUtil(this, "", "", true, cordova.getActivity());
            wpsUtil.openDocument(new File(message.trim()));
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}

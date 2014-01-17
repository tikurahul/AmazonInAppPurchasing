package com.rahulrav.cordova.amazon.plugin;

import android.app.Activity;
import android.util.Log;

import com.amazon.inapp.purchasing.Offset;
import com.amazon.inapp.purchasing.PurchasingManager;
import com.rahulrav.cordova.amazon.util.Logger;
import com.rahulrav.cordova.amazon.util.Macros;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the main In-App Purchasing Plugin definition.
 *
 * @author Rahul Ravikumar
 *
 */
public class AmazonInAppPurchasing extends CordovaPlugin {
    CallbackContext callbackContext;
    @Override
    public boolean execute(final String request, JSONArray args, final CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
        Logger.d(String.format("Executing (%s, %s, %s)", request, args, callbackContext.getCallbackId()));

        try {
            // register purchase observer
            final Activity activityContext = cordova.getActivity();
            final AmazonPurchasingObserver purchaseObserver = AmazonPurchasingObserver.getPurchasingObserver(this, activityContext);

            if (Macros.isEmpty(request) || !request.matches("(?i)(initialize|itemData|purchase|purchaseUpdates|userId)")) {
                Logger.e(String.format("Invalid Request %s", request));
                callbackContext.sendPluginResult(new PluginResult(Status.INVALID_ACTION));
            }

            // initialize request
            if (request.matches("(?i)initialize")) {

                // prevent repeated initialization
                if (purchaseObserver.isAlreadyInitialized()) {
                    // send the old response back
                    callbackContext.sendPluginResult(new PluginResult(Status.ERROR, purchaseObserver.sdkAvailableResponse()));
                }

                // sdk initialization request
                purchaseObserver.registerInitialization(callbackContext.getCallbackId());
                PurchasingManager.registerObserver(purchaseObserver);
            }

            // purchase request
            else if (request.matches("(?i)purchase")) {
                if (Macros.isEmptyJSONArray(args)) {
                    Logger.e("Invalid purchase request");
                    callbackContext.sendPluginResult(new PluginResult(Status.ERROR));
                }
                final String sku = args.optString(0);
                final String requestId = PurchasingManager.initiatePurchaseRequest(sku);
                PluginResult result = completeRequest(purchaseObserver, requestId, callbackContext.getCallbackId());
                callbackContext.sendPluginResult(result);
            }

            // purchase updates request
            else if (request.matches("(?i)purchaseUpdates")) {
                final String strOffset = !Macros.isEmptyJSONArray(args) ? args.optString(0, null) : null;
                final Offset offset = Macros.isEmpty(strOffset) ? Offset.BEGINNING : Offset.fromString(strOffset);
                final String requestId = PurchasingManager.initiatePurchaseUpdatesRequest(offset);
                PluginResult result = completeRequest(purchaseObserver, requestId, callbackContext.getCallbackId());
                callbackContext.sendPluginResult(result);
            }

            // item data request
            else if (request.matches("(?i)itemData")) {
                if (Macros.isEmptyJSONArray(args)) {
                    Logger.e("Invalid item data request");
                    callbackContext.sendPluginResult(new PluginResult(Status.ERROR));
                }

                final JSONArray skus = args.optJSONArray(0);

                final Set<String> skuSet = new HashSet<String>();
                for (int i = 0; i < skus.length(); i++) {
                    skuSet.add(skus.optString(i));
                }

                final String requestId = PurchasingManager.initiateItemDataRequest(skuSet);
                callbackContext.sendPluginResult(completeRequest(purchaseObserver, requestId, callbackContext.getCallbackId()));
            }

            // user id request
            else if (request.matches("(?i)userId")) {
                final String requestId = PurchasingManager.initiateGetUserIdRequest();
                PluginResult result = completeRequest(purchaseObserver, requestId, callbackContext.getCallbackId());
                callbackContext.sendPluginResult(result);
            }
            else {
                return false;
            }

        } catch (final Exception e) {
            Logger.e(String.format("Unable to execute request (%s. %s. %s)", request, args != null ? args.toString() : "", callbackContext.getCallbackId()), e);
        }
//        callbackContext.sendPluginResult(new PluginResult(Status.ERROR));
        return true;

    }
    /*
     * a helper method that completes a request
     */
    private PluginResult completeRequest(final AmazonPurchasingObserver purchaseObserver, final String requestId, final String callbackId) {
        purchaseObserver.addRequest(requestId, callbackId);
        final PluginResult result = new PluginResult(Status.NO_RESULT);
        result.setKeepCallback(true);
        return result;
    }
    public void echo(boolean success, JSONObject msg) {
        if (success){
            callbackContext.success(msg);
        }
        else {
            callbackContext.error(msg);
        }
    }
}
package mx.bnext.safe_validation;


import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.view.View;

import androidx.annotation.NonNull;

import mx.bnext.safe_validation.DevelopmentMode.DevelopmentModeCheck;
import mx.bnext.safe_validation.Emulator.EmulatorCheck;
import mx.bnext.safe_validation.ExternalStorage.ExternalStorageCheck;
import mx.bnext.safe_validation.Rooted.RootedCheck;
import mx.bnext.safe_validation.Location.LocationAssistant;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;

public class SafeValidationPlugin implements FlutterPlugin, MethodCallHandler {
    private Context context;
    private static LocationAssistantListener locationAssistantListener;
    private static final String CHANNEL = "safe_device";

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        this.context = binding.getApplicationContext();
        final MethodChannel channel = new MethodChannel(
                binding.getBinaryMessenger(),
                CHANNEL
        );
        channel.setMethodCallHandler(this);
        //locationAssistantListener = new LocationAssistantListener(context);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        context = null;
    }

    @Override
    public void onMethodCall(MethodCall call, final Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("isJailBroken")) {
            result.success(RootedCheck.isJailBroken(context));
        }else if (call.method.equals("isMockLocation")) {
            /*if (locationAssistantListener.isMockLocationsDetected()) {
                result.success(true);
            } else if (locationAssistantListener.getLatitude() != null && locationAssistantListener.getLongitude() != null) {
                result.success(false);
            } else {
                locationAssistantListener = new LocationAssistantListener(context);
                result.success(true);
            }*/
            result.success(false);
        }else if (call.method.equals("isRealDevice")) {
            result.success(!EmulatorCheck.isEmulator());
        }else if (call.method.equals("isOnExternalStorage")) {
            result.success(ExternalStorageCheck.isOnExternalStorage(context));
        }else if(call.method.equals("isDevelopmentModeEnable"))  {
            result.success(DevelopmentModeCheck.developmentModeCheck(context));
        }else if(call.method.equals("usbDebuggingCheck"))  {
            result.success(DevelopmentModeCheck.usbDebuggingCheck(context));
        }else {
            result.notImplemented();
        }
    }
}

class LocationAssistantListener implements LocationAssistant.Listener {
    private final LocationAssistant assistant;
    private boolean isMockLocationsDetected = false;
    private String latitude;
    private String longitude;

    public LocationAssistantListener(Context context) {
        assistant = new LocationAssistant(context, this, LocationAssistant.Accuracy.HIGH, 5000, false);
        assistant.setVerbose(true);
        assistant.start();
    }

    @Override
    public void onNeedLocationPermission() {
        assistant.requestLocationPermission();
        assistant.requestAndPossiblyExplainLocationPermission();
    }

    @Override
    public void onExplainLocationPermission() {
        io.flutter.Log.i("i", "onExplainLocationPermission: ");
    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        io.flutter.Log.i("i", "onLocationPermissionPermanentlyDeclined: ");
    }

    @Override
    public void onNeedLocationSettingsChange() {
        io.flutter.Log.i("i", "LocationSettingsStatusCodes.RESOLUTION_REQUIRED: Please Turn on GPS location service.");
    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        io.flutter.Log.i("i", "onFallBackToSystemSettings: ");
    }

    @Override
    public void onNewLocationAvailable(Location location) {
        if (location == null) return;
        latitude = location.getLatitude() + "";
        longitude = location.getLongitude() + "";
        isMockLocationsDetected = false;
    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        isMockLocationsDetected = true;
    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {
        io.flutter.Log.i("i", "Error: " + message);
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public boolean isMockLocationsDetected() {
        return isMockLocationsDetected;
    }

    public LocationAssistant getAssistant() {
        return assistant;
    }
}

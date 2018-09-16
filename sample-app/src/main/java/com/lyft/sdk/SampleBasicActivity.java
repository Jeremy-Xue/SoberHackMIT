package com.lyft.sdk;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.gm.android.vehicle.settings.widget.GmAlertDialog;
import com.gm.android.vehicle.signals.ConnectionResult;
import com.gm.android.vehicle.signals.IConnectionListener;
import com.gm.android.vehicle.signals.IVehicleDataListener;
import com.gm.android.vehicle.signals.Permissions;
import com.gm.android.vehicle.signals.VehicleDataRequest;
import com.gm.android.vehicle.signals.VehicleDataResult;
import com.gm.android.vehicle.signals.VehicleManager;
import com.gm.android.vehicle.signals.motion.Motion;
import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.lyftbutton.RideTypeEnum;
import com.lyft.networking.ApiConfig;
import com.lyft.networking.apiObjects.Vehicle;

import android.content.Intent;
import android.support.annotation.NonNull;

public class SampleBasicActivity extends Activity implements IConnectionListener, IVehicleDataListener {

    private VehicleManager mVehicleManager;
    private GmAlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample_basic);
        mVehicleManager = VehicleManager.getInstance(this);
        mVehicleManager.registerConnectionListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Permissions.getPermission(Motion.SPEED)}, 0);
        } else {
            subscribeSpeed();
        }

        //Function that reads the alcohol sensor data
        //If the sensor returns a 2 at any point,
        if(false) {
            Intent menuIntent = new Intent(this, SampleLocationAwareActivity.class);
            startActivity(menuIntent);
        }

    }

    private void subscribeSpeed() {
        VehicleDataRequest request = new VehicleDataRequest.Builder().addDataElement(Motion.SPEED).build();
        mVehicleManager.subscribe(request, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                new GmAlertDialog.Builder(this)
                        .setTitle("Permission Required")
                        .setMessage("We require permission to monitor your speed to use this app")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finishAffinity();
                            }
                        })
                        .show();
            }   else {
                subscribeSpeed();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mVehicleManager.connect();
    }

    @Override
    protected void onStop() {
        mVehicleManager.disconnect();
        super.onStop();
    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended() {

    }

    @Override
    public void onDataReceived(VehicleDataResult... vehicleDataResults) {
        for (VehicleDataResult result : vehicleDataResults) {
            if (result.getId() == Motion.SPEED) {
                //
                if ((float)result.getData() > 1) {
                    if(mDialog == null) {
                        mDialog = new GmAlertDialog.Builder(this)
                                .setTitle("You are driving under the influence")
                                .setMessage("Please pull over, we will help you to call a lyft")
                                .setPositiveButton("Call Lyft", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent lyftIntent = new Intent(SampleBasicActivity.this, SampleLocationAwareActivity.class);
                                        startActivity(lyftIntent);
                                    }
                                }).create();
                    }
                    if(!mDialog.isShowing()){
                        mDialog.show();
                    }
                }

            }

        }
    }
}

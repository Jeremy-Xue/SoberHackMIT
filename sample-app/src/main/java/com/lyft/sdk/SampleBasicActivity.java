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
import com.gm.android.vehicle.signals.brakes.Brakes;
import com.gm.android.vehicle.signals.motion.Motion;
import com.gm.android.vehicle.signals.safety.Safety;


import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

public class SampleBasicActivity extends Activity implements IConnectionListener, IVehicleDataListener {

    private VehicleManager mVehicleManager;
    private GmAlertDialog mDialog;
    private float mSpeed;
    private float mBrake;
    private boolean mSeatbelt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample_basic);
        mVehicleManager = VehicleManager.getInstance(this);
        mVehicleManager.registerConnectionListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Permissions.getPermission(Motion.SPEED)}, 0);
            requestPermissions(new String[]{Permissions.getPermission(Safety.DRIVER_SEATBELT_FASTENED)}, 1);
            requestPermissions(new String[]{Permissions.getPermission(Brakes.BRAKE_PEDAL_POS)}, 2);
        } else {
            subscribeSpeed();
            subscribeSeatbelt();
            subscribeBrake();
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
    private void subscribeSeatbelt() {
        VehicleDataRequest request = new VehicleDataRequest.Builder().addDataElement(Safety.DRIVER_SEATBELT_FASTENED).build();
        mVehicleManager.subscribe(request, this);
    }
    private void subscribeBrake() {
        VehicleDataRequest request = new VehicleDataRequest.Builder().addDataElement(Brakes.BRAKE_PEDAL_POS).build();
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
            if (result.getId() == Safety.DRIVER_SEATBELT_FASTENED) {
                mSeatbelt = (boolean)result.getData();
            }
            else if (result.getId() == Brakes.BRAKE_PEDAL_POS){
                mBrake = (float)result.getData();
            }
            else if (result.getId() == Motion.SPEED) {
                mSpeed = (float)result.getData();
            }
            if (mSpeed > 30 && (mBrake >= 0.75)){
                if(mDialog == null) {
                    mDialog = new GmAlertDialog.Builder(this)
                            .setTitle("Erratic Driving Detected")
                            .setMessage("You are under the influence and we have detected Hard Braking, we recommend that you call a rideshare service")
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
            else if(mSeatbelt && mSpeed > 1 ){

                if(mDialog == null) {
                    mDialog = new GmAlertDialog.Builder(this)
                            .setTitle("You are driving without a seatbelt")
                            .setMessage("We have not detected that you are above the legal BAC, but please buckle up")
                            .setPositiveButton("OK", null).create();
                }
                if(!mDialog.isShowing()){
                    mDialog.show();
                }
            }
            else if (mSpeed > 0) {
                if(mDialog == null) {
                    mDialog = new GmAlertDialog.Builder(this)
                            .setTitle("You may be driving under the influence")
                            .setMessage("Please pull over - we will help you to call a ride")
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

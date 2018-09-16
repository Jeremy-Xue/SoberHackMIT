package com.lyft.sdk;

import android.app.Activity;
import android.os.Bundle;
import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.lyftbutton.RideTypeEnum;
import com.lyft.networking.ApiConfig;

public class SampleBasicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample_basic);

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("z3Q_D3hYmpmw")
                .setClientToken("CzJYudFVOtGCSC8Qz++cQ9iF04VMkcuEf5g1sngFXIhQu2yjKWVvQaTGd+77GJ8jXruzrDMKwjMBFksDg84yMo319Xd9hLa1YOp2Fx8ZXGvVU3L0hKnHAIQ=")
                .build();

        LyftButton lyftButton = (LyftButton) findViewById(R.id.lyft_button);
        lyftButton.setApiConfig(apiConfig);

        RideParams.Builder rideParamsBuilder = new RideParams.Builder()
                .setPickupLocation(37.7766048, -122.3943629)
                .setDropoffLocation(37.759234, -122.4135125);
        rideParamsBuilder.setRideTypeEnum(RideTypeEnum.CLASSIC);

        lyftButton.setRideParams(rideParamsBuilder.build());
        lyftButton.load();
    }
}

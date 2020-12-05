package com.yleg.testvictimbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.yleg.poplib.PoPChallenge;

public class MainActivity extends AppCompatActivity {

    int LAUNCH_SECOND_ACTIVITY = 1;
    boolean pop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
//    }

    public void onClick(View v){
        Intent myint = new Intent(this, PoPChallenge.class);
        startActivityForResult(myint, LAUNCH_SECOND_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                Boolean result=data.getBooleanExtra("PoP",false);
                pop = result;
                if(result){

                    //enter code here
                    TextView tv = (TextView) findViewById(R.id.tv);
                    if(tv!=null){
                        tv.setText("Presence has been verified");
                    }

                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                TextView tv = (TextView) findViewById(R.id.tv);
                if(tv!=null){
                    tv.setText("Prove your presence!");
                }

            }
        }
    }//onActivityResult
}

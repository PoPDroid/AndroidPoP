package com.yleg.testvictimbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yleg.poplib.PoPChallenge;
import com.yleg.poplib.PoPPuzzleChallenge;

import org.w3c.dom.Text;

import facedetection.LivePreviewActivity;

public class MainActivity extends AppCompatActivity {

    private Button startButton, startButtonFace, sendButton, resetButton;
    private  TextView hinttext, textnum;
    private int level,depth;
    MyDatabaseHelper db;
    int LAUNCH_SECOND_ACTIVITY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MyDatabaseHelper(this);
        level = db.getCurrentLevel();
        depth = db.GetCurrentDepth();
        updateLogs();

        startButton = (Button) findViewById(R.id.button2);
        startButtonFace = (Button) findViewById(R.id.buttonFace);
        hinttext = (TextView) findViewById(R.id.textViewHint);
        textnum = findViewById(R.id.textViewNumber);
        sendButton = (Button) findViewById(R.id.buttonclip);
        resetButton = (Button) findViewById(R.id.buttonreset);

        if(depth==0){
            startButton.setVisibility(View.VISIBLE);
            startButton.setText("Start Trial Run - PoPLar");
            startButtonFace.setVisibility(View.VISIBLE);
            startButtonFace.setText("Start Trial Run - PoPL-Face");
            hinttext.setText("Start Trial Run. This will help you familiarize yourself with PoPL");
            sendButton.setVisibility(View.GONE);
            resetButton.setVisibility(View.GONE);
            EditText etm = findViewById(R.id.editTextTextMultiLine);
            etm.setText("");
            db.resetPuzzleLogs();
            depth=3;
        }else{

            hinttext.setText("Start PoPL: " + (24-level)+" runs left." );
        }
        textnum.setText(String.valueOf(depth));


//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        if (preferences.getBoolean("confirmed",false)){
//        }else {
//            Intent myint = new Intent(this, Confirm.class);
//            startActivity(myint);
//        }

    }

    public void onClick(View v){

        Intent myint = new Intent(this, PoPPuzzleChallenge.class);
        textnum = findViewById(R.id.textViewNumber);
        int num = Integer.parseInt(textnum.getText().toString());
        // EditText etp = findViewById(R.id.editTextPoP);

        //String txt = (etp.getText().toString());
        String txt = ("test");
        myint.putExtra("PoPDepth",num);
        myint.putExtra("PoPText",txt);
        startActivityForResult(myint, LAUNCH_SECOND_ACTIVITY);

    }
    public void onClickFace(View v){

        Intent myint = new Intent(this, LivePreviewActivity.class);
        textnum = findViewById(R.id.textViewNumber);
        int num = Integer.parseInt(textnum.getText().toString());
        // EditText etp = findViewById(R.id.editTextPoP);

        //String txt = (etp.getText().toString());
        String txt = ("test");
        myint.putExtra("PoPDepth",num);
        myint.putExtra("PoPText",txt);
        startActivityForResult(myint, LAUNCH_SECOND_ACTIVITY);

    }

    public void onClickProximity(View v){
        Intent myint = new Intent(this, PoPChallenge.class);
        startActivityForResult(myint, LAUNCH_SECOND_ACTIVITY);

    }


    public void onClipboard(View v){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", getLogs());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this,"Copied",Toast.LENGTH_LONG).show();

    }
    public void onReset(View v){
        //do counter that shows only start at first, then start and reset, and finally only send resutls
        new AlertDialog.Builder(this)
                .setTitle("Reset")
                .setMessage("Do you really want to reset?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.resetPuzzleLogs();

                        startButton.setVisibility(View.VISIBLE);
                        startButton.setText("Start Trial Run PoPLar");
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setText("Start Trial Run PoPL-Face");
                        hinttext.setText("Start Trial Run. This will help you familiarize yourself with PoPL");
                        sendButton.setVisibility(View.GONE);
                        resetButton.setVisibility(View.GONE);
                        EditText etm = findViewById(R.id.editTextTextMultiLine);
                        etm.setText("");
                        level = 0;
                        depth = 3;
                        textnum.setText(String.valueOf(depth));
                    }})
                .setNegativeButton(android.R.string.no, null).show();


    }

    private String getLogs(){
        Cursor cursor = db.GetAllPuzzleLogs();
        String myres = "";
        while (cursor.moveToNext()) {
            myres+= String.valueOf(cursor.getInt(1));
            myres += " - " + String.valueOf(cursor.getInt(2)) +"\n";

        }
        if (cursor != null) {
            cursor.close();
        }
        return  myres;
    }
    private void updateLogs(){
        Cursor cursor = db.GetAllPuzzleLogs();
        EditText etm = findViewById(R.id.editTextTextMultiLine);
        String myres = getLogs();
        etm.setText("");
        etm.setText(myres);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                Boolean result=data.getBooleanExtra("PoPPuzzle",false);
                long duration = data.getLongExtra("PoPPuzzleTime",0);


                if(result){
                    db.InsertPuzzleDetails(depth, duration);
                    updateLogs();
                    level+=1;
                    if(level>0 && level<4){
                        depth = 3;
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setText("Start PoPLar");
                        startButtonFace.setVisibility(View.VISIBLE);
                        startButtonFace.setText("Start PoPL-Face");
                        hinttext.setText("Start PoPL: " + (25-level)+" runs left." );
                        sendButton.setVisibility(View.GONE);
                        resetButton.setVisibility(View.VISIBLE);
                    }else if(level>3 && level<7){
                        depth = 4;
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setText("Start PoPLar");
                        startButtonFace.setVisibility(View.VISIBLE);
                        startButtonFace.setText("Start PoPL-Face");
                        hinttext.setText("Start PoPL: " + (25-level)+" runs left." );
                        sendButton.setVisibility(View.GONE);
                        resetButton.setVisibility(View.VISIBLE);
                    }else if(level>6 && level<10){
                        depth = 5;
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setText("Start PoPLar");
                        startButtonFace.setVisibility(View.VISIBLE);
                        startButtonFace.setText("Start PoPL-Face");
                        hinttext.setText("Start PoPL: " + (25-level)+" runs left." );
                        sendButton.setVisibility(View.GONE);
                        resetButton.setVisibility(View.VISIBLE);
                    }else if(level>9 && level<13){
                        depth = 6;
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setText("Start PoPLar");
                        startButtonFace.setVisibility(View.VISIBLE);
                        startButtonFace.setText("Start PoPL-Face");
                        hinttext.setText("Start PoPL: " + (25-level)+" runs left." );
                        sendButton.setVisibility(View.GONE);
                        resetButton.setVisibility(View.VISIBLE);
                    }else if(level>12 && level<16){
                        depth = 7;
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setText("Start PoPLar");
                        startButtonFace.setVisibility(View.VISIBLE);
                        startButtonFace.setText("Start PoPL-Face");
                        hinttext.setText("Start PoPL: " + (25-level)+" runs left." );
                        sendButton.setVisibility(View.GONE);
                        resetButton.setVisibility(View.VISIBLE);
                    }else if(level>15 && level<19){
                        depth = 8;
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setText("Start PoPLar");
                        startButtonFace.setVisibility(View.VISIBLE);
                        startButtonFace.setText("Start PoPL-Face");
                        hinttext.setText("Start PoPL: " + (25-level)+" runs left." );
                        sendButton.setVisibility(View.GONE);
                        resetButton.setVisibility(View.VISIBLE);
                    }else if(level>18 && level<22){
                        depth = 9;
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setText("Start PoPLar");
                        startButtonFace.setVisibility(View.VISIBLE);
                        startButtonFace.setText("Start PoPL-Face");
                        hinttext.setText("Start PoPL: " + (25-level)+" runs left." );
                        sendButton.setVisibility(View.GONE);
                        resetButton.setVisibility(View.VISIBLE);
                    }else if(level>21 && level<25){
                        depth = 10;
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setText("Start PoPLar");
                        startButtonFace.setVisibility(View.VISIBLE);
                        startButtonFace.setText("Start PoPL-Face");
                        hinttext.setText("Start PoPL: " + (25-level)+" runs left." );
                        sendButton.setVisibility(View.GONE);
                        resetButton.setVisibility(View.VISIBLE);
                    }else if(level==25){
                        startButton.setVisibility(View.GONE);
                        startButtonFace.setVisibility(View.GONE);
                        sendButton.setVisibility(View.VISIBLE);
                        sendButton.setText("Copy Results");
                        hinttext.setText("Click Copy Results and paste them in the PoPL Timing survey" );
                        resetButton.setVisibility(View.VISIBLE);
                    }

                    textnum.setText(String.valueOf(depth));
                    String time = "Presence verified!! (" + duration/1000 + " seconds)";
                    Toast.makeText(getApplicationContext(),time, Toast.LENGTH_LONG).show();
                    //EditText et = findViewById(R.id.editTextNumber);
                    //int depth = Integer.parseInt(et.getText().toString());
                    //enter code here
//                    TextView tv = (TextView) findViewById(R.id.tv);
//                    if(tv!=null){
//                        tv.setText("Presence has been verified");
//                    }
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
//                TextView tv = (TextView) findViewById(R.id.tv);
//                if(tv!=null){
//                    tv.setText("Prove your presence!");
//                }

            }
        }
    }//onActivityResult
}

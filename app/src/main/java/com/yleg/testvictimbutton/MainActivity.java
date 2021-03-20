package com.yleg.testvictimbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yleg.poplib.PoPChallenge;
import com.yleg.poplib.PoPPuzzleChallenge;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    MyDatabaseHelper db;
    int LAUNCH_SECOND_ACTIVITY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.getBoolean("confirmed",false)){
            db = new MyDatabaseHelper(this);
            updateLogs();
        }else {
            Intent myint = new Intent(this, Confirm.class);
            startActivity(myint);
        }

    }

    public void onClick(View v){
        Intent myint = new Intent(this, PoPPuzzleChallenge.class);
        EditText et = findViewById(R.id.editTextNumber);
        int num = Integer.parseInt(et.getText().toString());
        EditText etp = findViewById(R.id.editTextPoP);

        String txt = (etp.getText().toString());
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
        EditText et = findViewById(R.id.editTextTextMultiLine);
        String myres = getLogs();
        et.setText("");
        et.setText(myres);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                Boolean result=data.getBooleanExtra("PoPPuzzle",false);
                long duration = data.getLongExtra("PoPPuzzleTime",0);


                if(result){
                    String time = "Presence verified!! (" + duration/1000 + " seconds)";
                    Toast.makeText(getApplicationContext(),time, Toast.LENGTH_LONG).show();
                    EditText et = findViewById(R.id.editTextNumber);
                    int depth = Integer.parseInt(et.getText().toString());
                    db.InsertPuzzleDetails(depth, duration);
                    updateLogs();
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

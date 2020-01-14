package com.example.hawatm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CAMERA = 5;
    Button btn1, btn2;
    EditText et1, et2;
    CheckBox cbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //dangerous permission
        //        camera();

        //        myGetSaredPreferences();
        btn1 = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        et1 = findViewById(R.id.userid);
        et2 = findViewById(R.id.passwd);
        cbRemember = findViewById(R.id.cb_rem_userid);
        cbRemember.setChecked(getSharedPreferences("atm",MODE_PRIVATE).getBoolean("REMEMBER_USERID", false));
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences("atm", MODE_PRIVATE)
                        .edit()
                        .putBoolean("REMEMBER_USERID", isChecked)
                        .apply();
            }
        });

        String userid = getSharedPreferences("atm", MODE_PRIVATE)
                .getString("USERID", "");
        et1.setText(userid);

        View.OnClickListener OCL = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = ((Button)v).getId();
                switch (id){
                    case R.id.button:

                        final String userid = et1.getText().toString();
                        final String passwd = et2.getText().toString();
                        FirebaseDatabase.getInstance().getReference("users").child(userid).child("password")
                                 .addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String firebasepw = (String) dataSnapshot.getValue();
                                        if(firebasepw.equals(passwd)){
                                            boolean remember = getSharedPreferences("atm", MODE_PRIVATE)
                                                    .getBoolean("REMEMBER_USERID",false);
                                            //save userid
                                            if(remember){

                                                getSharedPreferences("atm", MODE_PRIVATE)
                                                        .edit()
                                                        .putString("USERID", userid)
                                                        .commit();//apply()
                                            }
                                        setResult(RESULT_OK);
                                        finish();
                                        }else{
                                            new AlertDialog.Builder(LoginActivity.this)
                                                    .setTitle("登入結果")
                                                    .setMessage("登入失敗")
                                                    .setPositiveButton("OK", null)
                                                    .show();
                                        }
                                     }

                                     @Override
                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                     }
                                 });
            //                        if("jack".equals(userid) && "1234".equals(passwd)){}
                        break;

                    case R.id.button2:
                        break;
                }
            }
        };
        btn1.setOnClickListener(OCL);
        btn2.setOnClickListener(OCL);
    }

    private void camera() {
        int permission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA);
        if(permission == PackageManager.PERMISSION_GRANTED){
            takePhoto();
        }else{
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_CAMERA){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePhoto();
            }
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    private void myGetSaredPreferences() {
        getSharedPreferences("atm", MODE_PRIVATE)
                .edit()
                .putInt("LEVEL", 3)
                .putString("NAME", "Angus")
                .commit();
        int level = getSharedPreferences("atm", MODE_PRIVATE)
                .getInt("LEVEL", 0);
        Log.d(TAG, "onCreate:" + level);
    }

}

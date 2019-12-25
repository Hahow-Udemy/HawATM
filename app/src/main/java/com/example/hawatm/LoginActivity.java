package com.example.hawatm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button btn1, btn2;
    EditText et1, et2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn1 = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        et1 = findViewById(R.id.userid);
        et2 = findViewById(R.id.passwd);

        View.OnClickListener OCL = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = ((Button)v).getId();
                switch (id){
                    case R.id.button:

                        String userid = et1.getText().toString();
                        String passwd = et2.getText().toString();
                        if("jack".equals(userid) && "1234".equals(passwd)){
                            setResult(RESULT_OK);
                            finish();
                        }
                        break;

                    case R.id.button2:
                        break;
                }
            }
        };
        btn1.setOnClickListener(OCL);
        btn2.setOnClickListener(OCL);
    }

}

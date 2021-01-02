package com.example.electiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordChangeActivity extends AppCompatActivity {
    String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        Token = sp.getString("Token","null");
    }

    public void onClick_submit(View view){
        EditText editText = findViewById(R.id.oldPassword);
        String old_pw = editText.getText().toString();
        editText = findViewById(R.id.newPassword);
        String new_pw = editText.getText().toString();
        editText = findViewById(R.id.newPassword2);

        String new_pw2 = editText.getText().toString();
        if (!new_pw.equals(new_pw2)) {
            Toast.makeText(this, "两次密码不一致",
                    Toast.LENGTH_SHORT).show();
            return ;
        }

        HttpThread thread = new HttpThread(){
            @Override
            public void run(){
                String res = doAlterPassword(Token, MD5Utils.md5(new_pw));
                Log.d("Passwordchange", res);
            }
        };
        thread.start();
        try {
            Thread.sleep( 1000 );
        } catch (Exception e){
            System.exit( 0 ); //退出程序
        }
    }
}
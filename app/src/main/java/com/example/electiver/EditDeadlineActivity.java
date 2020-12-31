package com.example.electiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditDeadlineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deadline);

        init();
    }
    //--------参数
    Intent intent;
    String state;

    //根据edit_add的不同初始化界面
    private void init(){
        state = "false";
        Button btn_state = (Button)findViewById(R.id.state);
        btn_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equals("true")) {
                    btn_state.setBackgroundColor(0xFF939393);
                    btn_state.setText("未完成");
                    state = "false";
                }
                else if (state.equals("false")) {
                    btn_state.setBackgroundColor(0xFF8B0012);
                    btn_state.setText("已完成");
                    state = "true";
                }
            }
        });

        intent = getIntent();
        String edit_add = intent.getStringExtra("edit_add");
        if (edit_add.equals("add")){
            Button btn = (Button)findViewById(R.id.deleteDDL);
            btn.setVisibility(View.GONE);//添加则隐藏删除按钮
            btn = (Button)findViewById(R.id.submit);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText textView;
                    String ddl_time = "";
                    String ddl_content = "";
                    textView = (EditText)findViewById((R.id.Year));
                    ddl_time = ddl_time + textView.getText().toString() + "-";
                    textView = (EditText)findViewById((R.id.Month));
                    ddl_time = ddl_time + textView.getText().toString() + "-";
                    textView = (EditText)findViewById((R.id.Day));
                    ddl_time = ddl_time + textView.getText().toString() + "-";
                    textView = (EditText)findViewById((R.id.Hour));
                    ddl_time = ddl_time + textView.getText().toString() + "-";
                    textView = (EditText)findViewById((R.id.Minute));
                    ddl_time = ddl_time + textView.getText().toString();
                    textView = (EditText)findViewById((R.id.contentInput));
                    ddl_content = textView.getText().toString();
                    add_ddl_to_back(ddl_time, ddl_content, state);

                    TextView temp = (TextView)findViewById(R.id.titleContent);
                    temp.setText(intent.getStringExtra("cid"));

                    /*
                    Test
                    */
                    String Token;
                    SharedPreferences getToken = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                    Token = getToken.getString("Token","null");
                    HttpThread thread = new HttpThread(){
                        @Override
                        public void run(){
                            String res=doQueryDDL(Token, intent.getStringExtra("cid"));
                            TextView temp = (TextView)findViewById(R.id.titleEdit);
                            temp.setText(res);
                        }
                    };
                    thread.start();
                    ///////

                    //EditDeadlineActivity.this.finish();
                }
            });
        }
        else if (edit_add.equals("edit")){
            TextView textView;
            textView = (TextView)findViewById((R.id.Year));
            textView.setText(intent.getStringExtra("year"));
            textView = (TextView)findViewById((R.id.Month));
            textView.setText(intent.getStringExtra("month"));
            textView = (TextView)findViewById((R.id.Day));
            textView.setText(intent.getStringExtra("day"));
            textView = (TextView)findViewById((R.id.Hour));
            textView.setText(intent.getStringExtra("hour"));
            textView = (TextView)findViewById((R.id.Minute));
            textView.setText(intent.getStringExtra("minute"));
            textView = (TextView)findViewById((R.id.contentInput));
            textView.setText(intent.getStringExtra("content"));
        }
    }

    private void add_ddl_to_back(String ddl_time, String ddl_content, String ddl_state){
        String Token;
        SharedPreferences getToken = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        Token = getToken.getString("Token","null");
        HttpThread thread = new HttpThread(){
            @Override
            public void run(){
                doInsertDDL(Token, intent.getStringExtra("cid"), ddl_content, ddl_time, ddl_state);
            }
        };
        thread.start();
    }
}
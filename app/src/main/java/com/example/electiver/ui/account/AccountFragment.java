package com.example.electiver.ui.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.electiver.DeadlineActivity;
import com.example.electiver.HttpThread;
import com.example.electiver.LoginActivity;
import com.example.electiver.MainActivity;
import com.example.electiver.MyCommentActivity;
import com.example.electiver.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class AccountFragment extends Fragment {
    View view;
    String Token;
    TextView test;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_account, container, false);

        //保险起见，每次createView时从SP中重新读取数据
        getValueRefreshed();

        init();
        /*
         * Test Only
         */
        //ddl的入口
        Button to_my_comment = (Button)view.findViewById(R.id.enter);
        to_my_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DeadlineActivity.class);
                intent.putExtra("course_id", "04832191");
                intent.putExtra("course_name", "软件工程");
                startActivity(intent);
            }
        });
        /*
         * Test Only
         */
        return view;
    }

    private void init(){
        //查看评论界面的入口
        Button to_my_comment = (Button)view.findViewById(R.id.toMyComment);
        to_my_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyCommentActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);

        //退出登陆需要先清空SP文件再跳转到登录界面，重新登录完成后refresh数据
        Button btn_logout = (Button) getActivity().findViewById(R.id.account_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "退出登录", Toast.LENGTH_SHORT).show();
                reLogin();
                getValueRefreshed();
            }
        });
    }

    public void reLogin(){
        //清空loginInfo
        SharedPreferences getUserInfo = getActivity().getSharedPreferences("loginInfo",getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor=getUserInfo.edit();
        editor.clear();
        editor.commit();
        //清空courseInfo
        SharedPreferences getCourseInfo = getActivity().getSharedPreferences("courseInfo",getActivity().MODE_PRIVATE);
        editor=getCourseInfo.edit();
        editor.clear();
        editor.commit();
        //清空timeAvail
        SharedPreferences occupyTime=getActivity().getSharedPreferences("timeAvail",getActivity().MODE_PRIVATE);
        editor = occupyTime.edit();
        String possibledays[] ={"mon","tue","wed","thu","fri","sat","sun"};
        String possibletime[] ={"1-2","3-4","5-6","7-8","10-11"};
        for(int i=0;i<possibledays.length;i++){
            for(int j=0;j<possibletime.length;j++){
                String timetag = possibledays[i]+possibletime[j];
                editor.putString(timetag,"false");
            }
        }
        editor.commit();
        //跳转到登录界面
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivityForResult(intent, 1);

    }

    public void getValueRefreshed(){

        TextView username = (TextView)view.findViewById(R.id.account_username);
        TextView grade = (TextView)view.findViewById(R.id.account_grade);
        TextView department = (TextView)view.findViewById(R.id.account_department);
        TextView major = (TextView)view.findViewById(R.id.account_major);

        SharedPreferences saveinfo = getActivity().getSharedPreferences("loginInfo", getActivity().MODE_PRIVATE);

        username.setText(saveinfo.getString("UserName", "none"));
        grade.setText(saveinfo.getString("Grade","none"));
        department.setText(saveinfo.getString("Department","none"));
        major.setText(saveinfo.getString("Major", "none"));
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            SharedPreferences saveinfo = getActivity().getSharedPreferences("loginInfo", getActivity().MODE_PRIVATE);
            String roughInfo = saveinfo.getString("roughInfo","null");
            if(!roughInfo.equals("null")){
                try{
                    JSONObject json = new JSONObject(roughInfo);
                    Token = json.getString("token");
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
            SharedPreferences.Editor editor = saveinfo.edit();
            editor.putString("Token",Token);
            editor.commit();

        }else{
            Log.d("bundle","nodatatomain");
        }
    }
}
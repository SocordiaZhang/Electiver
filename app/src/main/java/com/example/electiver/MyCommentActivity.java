package com.example.electiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Vector;

public class MyCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);

        get_comment_info();

        find_View();
        dynamic_create_View();
    }

    //--------全局变量
    //--参数
    String Token;
    Vector<String> comment_key;
    Vector<String> my_comment;
    private String[] class_comment;

    //--控件
    private ScrollView scrollView_scroll_list;
    private ConstraintLayout constraintLayout_list;
    private ConstraintSet constraintSet_list_set;
    private TextView[] textViews_comment;
    private Button[] buttons_delete;
    private Button[] buttons_edit;

    //--------该界面与后端的接口
    private void get_comment_info() {
        /*
        String Token;
        SharedPreferences getToken = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        Token = getToken.getString("Token","null");
        HttpThread thread = new HttpThread(){
            @Override
            public void run(){
                String res=doQueryMyComment(Token);
                TextView temp = (TextView)findViewById(R.id.titleComment);
                temp.setText(res);
            }
        };
        thread.start();*///测试http thread

        SharedPreferences getToken = this.getSharedPreferences("loginInfo", this.MODE_PRIVATE);
        Token = getToken.getString("Token","null");

        my_comment = new Vector<String>();
        comment_key = new Vector<String>();
        HttpThread thread = new HttpThread(){
            @Override
            public void start(){
                String res_query_comment = doQueryMyComment(Token);
                // System.out.println(res_query_comment);
                try {
                    JSONObject comments = new JSONObject(res_query_comment);
                    // int n = comments.toString().split(";").length;
                    // System.out.println(n);
                    Iterator iterator = comments.keys();
                    while(iterator.hasNext()){
                        String key = (String) iterator.next();
                        comment_key.add(key);
                        my_comment.add(comments.getString(key));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        class_comment = new String[my_comment.size()];

        for (int i = 0; i < my_comment.size(); i ++) {
            class_comment[i] = comment_key.get(i) + ":" + my_comment.get(i);
        }
    }

    //--------具体的逻辑实现
    private void find_View() {
        scrollView_scroll_list = (ScrollView)findViewById(R.id.scrollList);
        constraintLayout_list = (ConstraintLayout)scrollView_scroll_list.findViewById(R.id.commentList);

        constraintSet_list_set = new ConstraintSet();
        textViews_comment = new TextView[class_comment.length];
        buttons_delete = new Button[class_comment.length];
        buttons_edit = new Button[class_comment.length];
    }

    private void dynamic_create_View() {
        //设置按钮格式
        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.RECTANGLE);
        draw.setColor(0xFF8B0012);
        draw.setCornerRadius(20);
        for (int i = 0; i < class_comment.length; i ++) {
            textViews_comment[i] = new TextView(this);
            textViews_comment[i].setText(class_comment[i]);
            textViews_comment[i].setMinLines(5);
            textViews_comment[i].setId(IDUtils.generateViewId());
            constraintLayout_list.addView(textViews_comment[i]);
            constraintSet_list_set.constrainHeight(textViews_comment[i].getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet_list_set.constrainWidth(textViews_comment[i].getId(), ConstraintSet.MATCH_CONSTRAINT);
            constraintSet_list_set.connect(textViews_comment[i].getId(), ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID, ConstraintSet.LEFT,0);
            constraintSet_list_set.connect(textViews_comment[i].getId(), ConstraintSet.RIGHT,
                    ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,0);
            if (i == 0) {
                constraintSet_list_set.connect(textViews_comment[i].getId(), ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID, ConstraintSet.TOP,0); }
            else {
                constraintSet_list_set.connect(textViews_comment[i].getId(), ConstraintSet.TOP,
                        buttons_delete[i-1].getId(), ConstraintSet.BOTTOM,0); }

            buttons_delete[i] = new Button(this);
            buttons_delete[i].setText("删除评论");

            int finalI = i;
            buttons_delete[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HttpThread thread = new HttpThread(){
                        @Override
                        public void start(){
                            int res = doDeleteComment(Token, comment_key.get(finalI));
                            TextView test = (TextView)findViewById(R.id.titleComment);
                            test.setText(String.valueOf(res));
                        }
                    };
                    thread.start();
                    //MyCommentActivity.this.finish();
                }
            });
            buttons_delete[i].setBackground(draw);
            buttons_delete[i].setTextColor(Color.WHITE);
            buttons_delete[i].setId(IDUtils.generateViewId());
            constraintLayout_list.addView(buttons_delete[i]);
            constraintSet_list_set.constrainHeight(buttons_delete[i].getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet_list_set.constrainWidth(buttons_delete[i].getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet_list_set.connect(buttons_delete[i].getId(), ConstraintSet.RIGHT,
                    ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,0);
            constraintSet_list_set.connect(buttons_delete[i].getId(), ConstraintSet.TOP,
                    textViews_comment[i].getId(), ConstraintSet.BOTTOM,0);
        }
        constraintSet_list_set.applyTo(constraintLayout_list);
    }
}
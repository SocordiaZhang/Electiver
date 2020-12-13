package com.example.electiver;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Vector;


public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }
}


class ClassInf{

}

class Comments {
    class Comment {
        int uid;
        int cid;
        String text;
        public Comment(int uid, int cid, String text) {
            this.uid = uid;
            this.cid = cid;
            this.text = new String(text);
        }
    }

    public int course_id;
    int num_cnt;
    Vector<Comment> comments;

    public Comments() {
        num_cnt = 5;
        for (int i = 0; i < num_cnt; i++) {
            comments.add(new Comment(1700000000 + i, i, "this is a comment.\n这是一条评论。"));
        }
    }

}
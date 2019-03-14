package com.example.test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Posts_commentActivity extends AppCompatActivity {
    private DatabaseReference mPost,mLike ;
    final ArrayList<Posts> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_comment);
        mPost = FirebaseDatabase.getInstance().getReference().child("Post");
        final EditText postCont = findViewById(R.id.postContent);
        final EditText postUser = findViewById(R.id.postName);
        Button btn_send = findViewById(R.id.send);
        final CustomeAdpater customeAdpater = new CustomeAdpater();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mPost.push().getKey();
                Posts post = new Posts(postCont.getText().toString(),postUser.getText().toString());
                mPost.push().setValue(post);
                postCont.setText("");
                postUser.setText("");
            }
        });
        ListView listView = findViewById(R.id.main_list);
        listView.setAdapter(customeAdpater);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView p_conent = view.findViewById(R.id.post_content);
                Bundle bundle = new Bundle();
                Toast.makeText(getApplicationContext(), "sdsd", Toast.LENGTH_SHORT).show();
                bundle.putString("post",p_conent.getText().toString());
//                bundle.put("arrayposts", (ArrayList<? extends Parcelable>) list);
                Intent comment_intent = new Intent(getApplication(),CommentActivity.class);
                ArrayList<String> arr = new ArrayList<>();
                arr.add("qqq");
                arr.add("qqq");
                arr.add("qqq");
                arr.add("qqq");

                comment_intent.putExtras(bundle);
                startActivity(comment_intent);
            }
        });

        // read the post from firebase
        mPost.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Posts postData = dataSnapshot.getValue(Posts.class);
                list.add(0,postData);
                customeAdpater.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    // Custome list view
    class CustomeAdpater extends BaseAdapter{
        boolean checklike = false;

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.post_content,null);
            TextView p_name = convertView.findViewById(R.id.post_name);
            TextView p_conent = convertView.findViewById(R.id.post_content);
            final ImageButton like = convertView.findViewById(R.id.like_btn);
            final ImageButton dislike = convertView.findViewById(R.id.dislike_btn);

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dislike.setColorFilter(getResources().getColor(R.color.colorLikeFaceDefault));
                    like.setColorFilter(getResources().getColor(R.color.colorLikeFace));
                    checklike = true;
//                    Likes likes = new Likes(list.get(position).fullname,checklike);
//                    mLike.push().setValue(likes);
                }
            });
            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    like.setColorFilter(getResources().getColor(R.color.colorLikeFaceDefault));
                    dislike.setColorFilter(getResources().getColor(R.color.colorLikeFace));
                    checklike = false;
                }
            });
            p_name.setText(list.get(position).fullname);
            p_conent.setText(list.get(position).description);
            return convertView;
        }
    }
}

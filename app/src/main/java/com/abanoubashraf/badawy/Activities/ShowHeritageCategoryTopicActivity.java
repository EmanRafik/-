package com.abanoubashraf.badawy.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.Community.CommentNew;
import com.abanoubashraf.badawy.Community.PostNew;
import com.abanoubashraf.badawy.Community.PostsAdapterNew;
import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowHeritageCategoryTopicActivity extends AppCompatActivity {

    private TextView heritage_category_headline_activity, textView_no_info;
    private ProgressBar progress_loading;
    private RecyclerView recycler_view_heritage_category;
    private PostsAdapterNew postsAdapter;
    private List<PostNew> mData;
    private DatabaseReference reff;
    private Query query;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_heritage_category_topic);

        heritage_category_headline_activity = findViewById(R.id.heritage_category_headline_activity);
        recycler_view_heritage_category = findViewById(R.id.recycler_view_heritage_category);
        textView_no_info = findViewById(R.id.textView_no_info);
        progress_loading = findViewById(R.id.progress_loading);

        tag = getIntent().getStringExtra("tag_index");

        heritage_category_headline_activity.setText(getApplicationContext().getResources().getStringArray(R.array.heritage_categories)
                [Integer.parseInt(tag)]);

        User user = SharedHelper.getSharedHelper(getApplicationContext()).getCurrentUser();
        mData = new ArrayList<>();
        query = FirebaseDatabase.getInstance().getReference()
                .child("Posts")
                .orderByChild("verified")
                .equalTo(true);

        progress_loading.setVisibility(View.VISIBLE);
        textView_no_info.setVisibility(View.GONE);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    PostNew post = new PostNew();
                    if(snapshot.child("post_id").getValue() != null){
                        post.setPost_id(snapshot.child("post_id").getValue().toString());
                    }
                    if(snapshot.child("post").getValue()!= null){
                        post.setPost(snapshot.child("post").getValue().toString());
                    }
                    if(snapshot.child("tag").getValue()!= null){
                        post.setTag(snapshot.child("tag").getValue().toString());
                    }
                    if(snapshot.child("user_id").getValue() != null){
                        post.setUser_id(snapshot.child("user_id").getValue().toString());
                    }
                    if(snapshot.child("user_name").getValue() != null){
                        post.setUser_name(snapshot.child("user_name").getValue().toString());
                    }
                    if(snapshot.child("post_img").getValue() != null){
                        post.setPost_img(snapshot.child("post_img").getValue().toString());
                    }
                    if(snapshot.child("profile_pic").getValue() != null){
                        post.setProfile_pic(snapshot.child("profile_pic").getValue().toString());
                    }
                    if(snapshot.child("post_audio").getValue() != null){
                        post.setPost_audio(snapshot.child("post_audio").getValue().toString());
                    }
                    if(snapshot.child("num_of_votes").getValue() != null){
                        post.setNum_of_votes(snapshot.child("num_of_votes").getValue().toString());
                    }
                    if(snapshot.child("num_of_comments").getValue() != null){
                        post.setNum_of_comments(snapshot.child("num_of_comments").getValue().toString());
                    }
                    if(snapshot.child("verified").getValue() != null){
                        post.setVerified((boolean) snapshot.child("verified").getValue());
                    }
                    if(snapshot.child("upvotes").getValue() != null){
                        post.setUpvotes((ArrayList<String>) snapshot.child("upvotes").getValue());
                    }
                    if(snapshot.child("downvotes").getValue() != null){
                        post.setDownvotes((ArrayList<String>) snapshot.child("downvotes").getValue());
                    }


                    // read answers list
                    if(!snapshot.hasChild("comments")){
                        post.setComments(new ArrayList<CommentNew>());
                    }
                    else{
                        ArrayList<CommentNew> commentsList=new ArrayList<CommentNew>();
                        DataSnapshot ss=snapshot.child("comments");
                        for(DataSnapshot shot : ss.getChildren()){
                            CommentNew comment = shot.getValue(CommentNew.class);
                            commentsList.add(comment);
                        }
                        post.setComments(commentsList);
                    }
//                    Toast.makeText(getApplicationContext(), post.getTag(), Toast.LENGTH_SHORT).show();
                    if (post.getTag().equals(tag)) {
                        mData.add(post);
                    }
                    if (mData.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "empty", Toast.LENGTH_SHORT).show();
                        textView_no_info.setVisibility(View.VISIBLE);
                    } else {
                        postsAdapter.notifyDataSetChanged();
                        textView_no_info.setVisibility(View.GONE);
                    }
                    progress_loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                textView_no_info.setVisibility(View.VISIBLE);
                progress_loading.setVisibility(View.GONE);
            }
        });
        postsAdapter = new PostsAdapterNew(getApplicationContext(),mData, true);
        recycler_view_heritage_category.setAdapter(postsAdapter);
        recycler_view_heritage_category.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}

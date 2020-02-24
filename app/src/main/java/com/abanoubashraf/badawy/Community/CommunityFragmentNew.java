package com.abanoubashraf.badawy.Community;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragmentNew extends Fragment {
    private TextView textView_no_info;
    private ProgressBar progress_loading;
    private RecyclerView recyclerView;
    private PostsAdapterNew postsAdapter;
    private List<PostNew> mData;
    private FloatingActionButton fab;
    private DatabaseReference reff;
    private ValueEventListener listener;
    private Query query;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinner_adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_community, container, false);
        reff = FirebaseDatabase.getInstance().getReference().child("Posts");
        recyclerView=v.findViewById(R.id.recycler_view_community);
        fab=v.findViewById(R.id.fab_community);
        spinner = v.findViewById(R.id.spinner_community);
        textView_no_info = v.findViewById(R.id.textView_no_info);
        progress_loading = v.findViewById(R.id.progress_loading);

        User user = SharedHelper.getSharedHelper(getContext()).getCurrentUser();
        mData = new ArrayList<>();
        query = reff;

        //fill spinner
        spinner_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.heritage_categories, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                progress_loading.setVisibility(View.VISIBLE);
                textView_no_info.setVisibility(View.GONE);
                // set query according to filter (to be added)
                listener = query.addValueEventListener(new ValueEventListener() {
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
//                                post.setTag(getContext().getResources().getStringArray(R.array.heritage_categories)
//                                        [Integer.parseInt(snapshot.child("tag").getValue().toString())]);
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
                            if (spinner.getSelectedItemPosition() != 0) {
                                if (post.getTag().equals(String.valueOf(spinner.getSelectedItemPosition()))) {
                                    mData.add(post);
                                }
                            } else {
                                mData.add(post);
                            }
                            postsAdapter.notifyDataSetChanged();
                        }
                        progress_loading.setVisibility(View.GONE);
                        if (mData.isEmpty()) {
                            textView_no_info.setVisibility(View.VISIBLE);
                        } else {
                            textView_no_info.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progress_loading.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                progress_loading.setVisibility(View.GONE);
            }
        });

        progress_loading.setVisibility(View.VISIBLE);
        textView_no_info.setVisibility(View.GONE);
        // set query according to filter (to be added)
        listener = query.addValueEventListener(new ValueEventListener() {
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
                    mData.add(post);
                    postsAdapter.notifyDataSetChanged();
                }
                progress_loading.setVisibility(View.GONE);
                if (mData.isEmpty()) {
                    textView_no_info.setVisibility(View.VISIBLE);
                } else {
                    textView_no_info.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progress_loading.setVisibility(View.GONE);
            }
        });
        postsAdapter = new PostsAdapterNew(getContext(),mData, false);
        recyclerView.setAdapter(postsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddPostActivityNew.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return v;
    }
}

package com.abanoubashraf.badawy.Questions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ViewQuestionActivity extends AppCompatActivity {
    private ProgressBar progress_loading;
    private TextView question, num_of_answers, tag;
    private EditText answer_txt;
    private RecyclerView recyclerView;
    private AnswersAdapter answersAdapter;
    private List<Answer> mData;
    private ImageView add_answer_pic;
    private ImageView answer_pic;
    private ImageView delete_answer_pic;
    private ImageView submitButton;
    DatabaseReference reff;
    ValueEventListener listener;
    String question_id;
    Uri imageUri;
    String imageName;
    String downloadUri;
    LinearLayout add_answer_layout;
    RelativeLayout answer_pic_layout;
    boolean imageSelected;
    public static final int PICK_IMAGE = 1;
    StorageReference storageReference;
    Answer answer;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init
        setContentView(R.layout.activity_view_question);

        progress_loading = findViewById(R.id.progress_loading);
        question = findViewById(R.id.view_question_text);
        num_of_answers = findViewById(R.id.view_question_num_of_answers);
        tag = findViewById(R.id.view_question_tag);
        recyclerView = findViewById(R.id.view_question_recycler_view);
        add_answer_pic = findViewById(R.id.add_answer_pic);
        answer_pic_layout = findViewById(R.id.answer_pic_layout);
        answer_pic = findViewById(R.id.answer_pic);
        answer_txt = findViewById(R.id.answer_txt);
        submitButton = findViewById(R.id.submit2);
        add_answer_layout = findViewById(R.id.add_answer_layout);
        storageReference= FirebaseStorage.getInstance().getReference();
        delete_answer_pic = findViewById(R.id.delete_answer_pic_btn);
        imageSelected = false;
        imageName = UUID.randomUUID().toString()+".jpg";
        answer = new Answer();
        mData = new ArrayList<Answer>();
        question_id = getIntent().getStringExtra("question_id");
        reff = FirebaseDatabase.getInstance().getReference().child("Questions").child(question_id);


        add_answer_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, getText(R.string.select_picture)), PICK_IMAGE);
            }
        });

        delete_answer_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_answer_pic();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitClicked();
            }
        });

        // hiding add answer btn for nb
        User user = SharedHelper.getSharedHelper(this).getCurrentUser();
        if(user.getType().equals("nb")){
            add_answer_layout.setVisibility(View.GONE);
        }

        // fill answer recycler view
        listener = reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progress_loading.setVisibility(View.VISIBLE);

                mData.clear();
                if(dataSnapshot.child("question").getValue() != null){
                    question.setText(dataSnapshot.child("question").getValue().toString());
                }
                if(dataSnapshot.child("tag").getValue() != null){
                    tag.setText(getApplicationContext().getResources().getStringArray(R.array.heritage_categories)
                            [Integer.parseInt(dataSnapshot.child("tag").getValue().toString())]);
                }
                if(dataSnapshot.hasChild("answers")){
                    dataSnapshot = dataSnapshot.child("answers");
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Answer answer = snapshot.getValue(Answer.class);
                        mData.add(answer);
                        answersAdapter.notifyDataSetChanged();
                    }
                }
                num_of_answers.setText(String.valueOf(mData.size()));
                progress_loading.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progress_loading.setVisibility(View.GONE);
            }
        });

        //set adapter

        answersAdapter = new AnswersAdapter(this,mData);
        recyclerView.setAdapter(answersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

    public void submitClicked(){
        User user = SharedHelper.getSharedHelper(this).getCurrentUser();
        answer.setComment_text(answer_txt.getText().toString());
        answer.setUser_name(user.getUsername());
        answer.setQuestion_id(question_id);
        answer.setNum_of_votes("0");
        answer.setProfile_pic(user.getImage_URL());
        answer.setVerified(user.is_verified());
        if(!imageSelected){
            answer.setComment_pic("default");
        }
        else{
            answer.setComment_pic(downloadUri);
        }
        String answer_id = reff.push().getKey();
        reff.child("answers").child(answer_id).setValue(answer);
        reff.child("answers").child(answer_id).child("answer_id").setValue(answer_id);
        answer_txt.getText().clear();
        answer_pic_layout.setVisibility(View.GONE);
        imageSelected=false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                answer_pic_layout.setVisibility(View.VISIBLE);
                answer_pic.setImageBitmap(bitmap);
                uploadImage(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();;
            }
        }
    }
    private void uploadImage(Bitmap bitmap) {

        if(imageUri != null)
        {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Uploading image");
            pd.show();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            final StorageReference ref = storageReference.child("answer_pictures").child(imageName);
            UploadTask uploadTask = ref.putBytes(data);
            submitButton.setEnabled(false);
            delete_answer_pic.setEnabled(false);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        submitButton.setEnabled(true);
                        delete_answer_pic.setEnabled(true);
                        Toast.makeText(ViewQuestionActivity.this,getText(R.string.failed_to_upload_image),Toast.LENGTH_SHORT).show();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        downloadUri = downloadUrl.toString();
                        imageSelected = true;
//                        Toast.makeText(ViewQuestionActivity.this,getText(R.string.uploaded_successfully),Toast.LENGTH_SHORT).show();
                        submitButton.setEnabled(true);
                        delete_answer_pic.setEnabled(true);
                        pd.dismiss();
                    }
                }
            });
        }
    }
    //delete post pic

    public void delete_answer_pic(){
        answer_pic_layout.setVisibility(View.GONE);
        final StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(downloadUri);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                imageSelected = false;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }
}

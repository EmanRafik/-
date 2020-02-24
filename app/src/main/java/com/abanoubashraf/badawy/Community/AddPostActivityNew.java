package com.abanoubashraf.badawy.Community;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abanoubashraf.badawy.Activities.HomeActivity;
import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AddPostActivityNew extends AppCompatActivity{

    private FloatingActionButton add_picture;
    private EditText post_txt;
    private ImageView post_pic,delete_post_pic;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinner_adapter;
    private Button submitButton;
    private PostNew post;
    private RelativeLayout post_pic_layout;

    private DatabaseReference reff;
    private User user;
    private Uri imageUri;
    private String imageName;
    private String imageDownloadUri;
    public static final int PICK_IMAGE = 1;
    private StorageReference storageReference;
    private Float translationY = 100f;
    private boolean isMenuOpen ;
    private boolean imageSelected ;
    private OvershootInterpolator interpolator = new OvershootInterpolator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init
        isMenuOpen = false;
        imageSelected = false;
        setContentView(R.layout.activity_add_post_new);
        add_picture= findViewById(R.id.add_picture_textview);
        post_txt = findViewById(R.id.post_txt);
        post_pic = findViewById(R.id.post_pic);
        submitButton = findViewById(R.id.submit_post_btn);
        delete_post_pic = findViewById(R.id.delete_post_pic_button);
        spinner = findViewById(R.id.tag_spinner);
        post_pic_layout = findViewById(R.id.post_pic_layout);

        user = SharedHelper.getSharedHelper(getApplicationContext()).getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference();
        imageName = UUID.randomUUID().toString()+".jpg";

        //fill spinner
        spinner_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.heritage_categories, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        //firebase reference
        reff= FirebaseDatabase.getInstance().getReference().child("Posts");
        post = new PostNew();

        //listeners
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitClicked();
            }
        });

        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPictureClicked();
            }
        });


        delete_post_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_post_pic();
            }
        });


    }


    //after successfully choosing files

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                post_pic_layout.setVisibility(View.VISIBLE);
                post_pic.setImageBitmap(bitmap);
                uploadImage(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();;
            }
        }
    }

    //upload files to firebase storage
    private void uploadImage(Bitmap bitmap) {

        if(imageUri != null)
        {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Uploading image");
            pd.show();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            final StorageReference ref = storageReference.child("post_pictures").child(imageName);
            UploadTask uploadTask = ref.putBytes(data);
            submitButton.setEnabled(false);
            delete_post_pic.setEnabled(false);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        submitButton.setEnabled(true);
                        Toast.makeText(AddPostActivityNew.this, getText(R.string.failed_to_upload_image), Toast.LENGTH_SHORT).show();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        imageDownloadUri = downloadUrl.toString();
                        imageSelected = true;
//                        Toast.makeText(AddPostActivityNew.this,getText(R.string.uploaded_successfully), Toast.LENGTH_SHORT).show();
                        submitButton.setEnabled(true);
                        delete_post_pic.setEnabled(true);
                        pd.dismiss();
                    }
                }
            });
        }

    }

    // submit btn click action
    public void submitClicked(){
        if (validate_data()) {
            post.setUser_id(user.getId());
            post.setPost(post_txt.getText().toString());
            post.setComments(new ArrayList<CommentNew>());
            post.setTag(String.valueOf(spinner.getSelectedItemPosition()));
            post.setUser_name(user.getUsername());
            post.setNum_of_votes("0");
            post.setNum_of_comments("0");
            post.setProfile_pic(user.getImage_URL());
            post.setVerified(user.is_verified());
            if(!imageSelected){
                post.setPost_img("default");
            } else{
                post.setPost_img(imageDownloadUri);
            }
            String post_id = reff.push().getKey();
            reff.child(post_id).setValue(post);
            reff.child(post_id).child("post_id").setValue(post_id);
            Intent intent = new Intent(AddPostActivityNew.this, HomeActivity.class);
            intent.putExtra("type", "b");
            startActivity(intent);
            finish();
        }
    }

    private boolean validate_data () {
        if (TextUtils.isEmpty(post_txt.getText().toString().trim())){
            Toast.makeText(this, getText(R.string.enter_a_post),Toast.LENGTH_SHORT).show();
            return false;
        } else if (spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this,getText(R.string.select_a_tag),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //add picture click action
    public void addPictureClicked(){
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
    }

    //delete post pic

    public void delete_post_pic(){
        post_pic_layout.setVisibility(View.GONE);
        final StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageDownloadUri);
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

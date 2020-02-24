package com.abanoubashraf.badawy.Settings;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment {

    private ImageView profile_image;
    private ImageButton change_profile_image;
    private TextView textView_tribe;
    private EditText edittext_change_username, edittext_change_tribe;
    private Button button_confirm_changing_data;
    private RelativeLayout mActivityIndicator;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private StorageReference storageReference;
    private final static int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        profile_image = v.findViewById(R.id.profile_image);
        change_profile_image = v.findViewById(R.id.change_profile_image);
        textView_tribe = v.findViewById(R.id.tribe);
        edittext_change_username = v.findViewById(R.id.edittext_change_username);
        edittext_change_tribe = v.findViewById(R.id.edittext_change_tribe);
        button_confirm_changing_data = v.findViewById(R.id.button_confirm_changing_data);
        mActivityIndicator = v.findViewById(R.id.activityIndicatorLayout);
        mActivityIndicator.setOnClickListener(null);

        edittext_change_username.setText(SharedHelper.getSharedHelper(getContext()).getCurrentUser().getUsername());

        if (SharedHelper.getSharedHelper(getContext()).getCurrentUser().getType().equals("nb")) {
            textView_tribe.setVisibility(View.GONE);
            edittext_change_tribe.setVisibility(View.GONE);
        } else {
            edittext_change_tribe.setText(SharedHelper.getSharedHelper(getContext()).getCurrentUser().getTribe());
        }

        if (SharedHelper.getSharedHelper(getContext()).getCurrentUser().getImage_URL().equals("default")) {
            profile_image.setImageResource(R.drawable.default_pp);
        } else {
            Glide.with(getContext()).load(SharedHelper.getSharedHelper(getContext()).getCurrentUser().getImage_URL()).into(profile_image);
        }

        change_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        button_confirm_changing_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityIndicator.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(edittext_change_username.getText()) ||
                        (SharedHelper.getSharedHelper(getContext()).getCurrentUser().getType().equals("b")
                                && TextUtils.isEmpty(edittext_change_tribe.getText()))) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setTitle(R.string.error_title_failed);
                    alertDialogBuilder.setMessage(R.string.fill_all_fields);
                    alertDialogBuilder.setPositiveButton(R.string.ok, null);
                    mActivityIndicator.setVisibility(View.GONE);
                    alertDialogBuilder.create().show();
                } else {
                    HashMap<String, Object> hashMap = new HashMap();
                    hashMap.put("username", edittext_change_username.getText().toString().trim());
                    hashMap.put("tribe", edittext_change_tribe.getText().toString().trim().toLowerCase());

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(SharedHelper.getSharedHelper(getContext()).getCurrentUser().getId());
                    databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            SharedHelper.getSharedHelper(getContext()).setUsername(edittext_change_username.getText().toString().trim());
                            SharedHelper.getSharedHelper(getContext()).setTribe(edittext_change_tribe.getText().toString().trim());
                            Toast.makeText(getContext(), R.string.profile_updated_successfully, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                mActivityIndicator.setVisibility(View.GONE);
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(SharedHelper.getSharedHelper(getContext()).getCurrentUser().getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getImage_URL().equals("default")) {
                    profile_image.setImageResource(R.drawable.default_pp);
                } else {
                    Glide.with(getContext()).load(user.getImage_URL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        return v;
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getText(R.string.uploading_image));
        pd.show();

        if (imageUri != null) {
            storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                                .child(SharedHelper.getSharedHelper(getContext()).getCurrentUser().getId());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("image_URL", mUri);
                        SharedHelper.getSharedHelper(getContext()).setImage_URL(mUri);
                        databaseReference.updateChildren(hashMap);

                        pd.dismiss();
                    } else {
                        Toast.makeText(getContext(), getText(R.string.failed_to_upload_image), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), getText(R.string.no_image_selected), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), getText(R.string.uploading_image), Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }

        }
    }
}

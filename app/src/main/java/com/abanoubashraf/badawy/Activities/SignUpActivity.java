package com.abanoubashraf.badawy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.R;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    private TextView toolbar_text;
    private CountryCodePicker ccp;
    private EditText editText_username, editText_tribe, editText_mobile_number;
    private Button button_register;
    private String type;
    private RelativeLayout mActivityIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar_text = findViewById(R.id.toolbar_text);
        editText_username = findViewById(R.id.edittext_username);
        editText_tribe = findViewById(R.id.edittext_tribe);
        editText_mobile_number = findViewById(R.id.edittext_mobile_number);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(editText_mobile_number);
        button_register = findViewById(R.id.button_register);
        mActivityIndicator = findViewById(R.id.activityIndicatorLayout);
        mActivityIndicator.setOnClickListener(null);

        toolbar_text.setText(R.string.registration);

        type = getIntent().getStringExtra("type");
        if (type.equals("nb")) {
            editText_tribe.setVisibility(View.GONE);
        }

        ccp.resetToDefaultCountry();
        ccp.isValidFullNumber();

        if (Locale.getDefault().getDisplayLanguage().equals("العربية")) {
            ccp.changeDefaultLanguage(CountryCodePicker.Language.valueOf("ARABIC"));
        }

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean vaild_data = validate_data(editText_username.getText().toString().trim(),
                        editText_tribe.getText().toString().trim());

                if (vaild_data) {
                    register(editText_username.getText().toString().trim(),
                            editText_tribe.getText().toString().trim());
                }
            }
        });
    }

    private boolean validate_data (String username, String tribe) {
        mActivityIndicator.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(username) || (type.equals("b") && TextUtils.isEmpty(tribe))) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
            alertDialogBuilder.setTitle(R.string.error_title_failed);
            alertDialogBuilder.setMessage(R.string.fill_all_fields);
            alertDialogBuilder.setPositiveButton(R.string.ok, null);
            mActivityIndicator.setVisibility(View.GONE);
            alertDialogBuilder.create().show();
            return false;
        } else if (!ccp.isValidFullNumber()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
            alertDialogBuilder.setTitle(R.string.error_title_failed);
            alertDialogBuilder.setMessage(R.string.error_mobile_number);
            alertDialogBuilder.setPositiveButton(R.string.ok, null);
            alertDialogBuilder.create().show();
            mActivityIndicator.setVisibility(View.GONE);
            return false;
        }
        mActivityIndicator.setVisibility(View.GONE);
        return true;
    }

    private void register(String username, String tribe){
        if (type.equals("nb")) {
            tribe = "default";
        }
        Intent intent = new Intent(SignUpActivity.this, VerificationCodeActivity.class);
        User user = new User(username, ccp.getFullNumberWithPlus(), type, tribe);
        Gson gson = new Gson();
        String myjson = gson.toJson(user);
        intent.putExtra("user", myjson);
        startActivity(intent);
    }

}

package com.abanoubashraf.badawy.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class SharedHelper {

    private static SharedHelper sharedHelper;
    private SharedPreferences.Editor editor;
    private Context context;

    private SharedHelper(Context context) {
        this.context = context;
        editor = context.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
    }

    public static SharedHelper getSharedHelper(Context context) {
        sharedHelper = new SharedHelper(context);
        return sharedHelper;
    }


    public void setCurrentUser(User user) {
        Gson gson = new Gson();
        String myjson = gson.toJson(user);
        editor.putString("user", myjson);
        editor.apply();
    }

    public User getCurrentUser () {
        Gson gson = new Gson();
        User user = gson.fromJson(context.getSharedPreferences("PREFS", MODE_PRIVATE)
                .getString("user", "null"), User.class);
        return user;
    }

    public void setUsername (String username) {
        User user = getCurrentUser();
        user.setUsername(username);
        setCurrentUser(user);
    }

    public void setTribe (String tribe) {
        User user = getCurrentUser();
        user.setTribe(tribe);
        setCurrentUser(user);
    }

    public void setImage_URL (String image_url) {
        User user = getCurrentUser();
        user.setImage_URL(image_url);
        setCurrentUser(user);
    }
}

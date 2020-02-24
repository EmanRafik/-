package com.abanoubashraf.badawy.BedouinHeritage;

import com.abanoubashraf.badawy.R;

import java.util.List;

public class HeritageCategory {
    private String headline;
    private int image_ID = R.drawable.post_image;

    public HeritageCategory(String headline) {
        this.headline = headline;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public int getImage_ID() {
        return image_ID;
    }

    public void setImage_ID(int image_ID) {
        this.image_ID = image_ID;
    }

}

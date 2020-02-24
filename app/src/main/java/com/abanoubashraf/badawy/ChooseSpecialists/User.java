package com.abanoubashraf.badawy.ChooseSpecialists;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id, username, image_URL = "default", mobile_number, type, tribe = "default";
    private boolean is_verified = false;
    private List<String> voters_ids = new ArrayList<>();

    public User() {
    }

    public User(String id, String username, String mobile_number, String type, String tribe) {
        this.id = id;
        this.username = username;
        this.mobile_number = mobile_number;
        this.type = type;
        this.tribe = tribe;
    }

    public User(String username, String mobile_number, String type, String tribe) {
        this.username = username;
        this.mobile_number = mobile_number;
        this.type = type;
        this.tribe = tribe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTribe() {
        return tribe;
    }

    public void setTribe(String tribe) {
        this.tribe = tribe;
    }

    public boolean is_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public List<String> getVoters_ids() {
        return voters_ids;
    }

    public void setVoters_ids(List<String> voters_ids) {
        this.voters_ids = voters_ids;
    }

}

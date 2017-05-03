package com.example.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iem on 03/05/2017.
 */

@IgnoreExtraProperties
public class User {
    public String nickname;
    public String description;
    public int age;
    public int gender;
    public List<String> genderPref;

    //Firebase field contnte
    public static final String NICKNAME = "nickname";
    public static final String DESCRIPTION = "description";
    public static final String AGE = "age";
    public static final String GENDER = "gender";
    public static final String GENDER_PREF = "genderPreferences";
    public static final String GENDER_PREF1 = "0";
    public static final String GENDER_PREF2 = "1";
    public static final String URL_PICTURE = "picture";

    public User() {
    }

    public User(String nickname, String description, int age, int gender, List<String> genderPref) {
        this.nickname = nickname;
        this.description = description;
        this.age = age;
        this.gender = gender;
        this.genderPref = genderPref;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put(NICKNAME,nickname);
        result.put(AGE,age);
        result.put(GENDER,gender);
        result.put(DESCRIPTION,description);
        return result;
    }
}

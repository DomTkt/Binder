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
    private String id;
    private String nickname;
    private String description;
    private int age;
    private int gender;
    private List<String> genderPref;

    //Firebase field contnte
    public static final String NICKNAME = "nickname";
    public static final String DESCRIPTION = "description";
    public static final String AGE = "age";
    public static final String GENDER = "gender";
    public static final String GENDER_PREF = "genderPreferences";
    public static final String GENDER_PREF1 = "0";
    public static final String GENDER_PREF2 = "1";
    public static final String URL_PICTURE = "picture";
    public static final String CONVERSATIONS = "conversations";

    public  User(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setGenderPref(List<String> genderPref) {
        this.genderPref = genderPref;
    }

    public User(String name) {
        this.nickname = name;
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

    public String getNickname() {
        return nickname;
    }

    public String getDescription() {
        return description;
    }

    public int getAge() {
        return age;
    }

    public int getGender() {
        return gender;
    }

    public List<String> getGenderPref() {
        return genderPref;
    }
}

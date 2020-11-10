package com.randomone.androidmonsterc3;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

public class Module implements Parcelable {
    private String Code;
    private String Title;
    private String Description;
    private String Time;
    private int Credits;
    private int Level;
    private List<String> Pathway;
    private List<String> Prerequisites;
    private List<String> Corequisite;
    private boolean isFinished;
    private boolean isActive;

    public Module(){

    }

    public Module(String code, String title, String description, String time, int credits, int level, List<String> pathway, List<String> prerequisites, List<String> corequisite, boolean isFinished, boolean isActive) {
        this.Code = code;
        this.Title = title;
        this.Description = description;
        this.Time = time;
        this.Credits = credits;
        this.Level = level;
        this.Pathway = pathway;
        this.Prerequisites = prerequisites;
        this.Corequisite = corequisite;
        this.isFinished = isFinished;
        this.isActive = isActive;

    }

    protected Module(Parcel in) {
        Code = in.readString();
        Title = in.readString();
        Description = in.readString();
        Time = in.readString();
        Credits = in.readInt();
        Level = in.readInt();
        Pathway = in.createStringArrayList();
        Prerequisites = in.createStringArrayList();
        Corequisite = in.createStringArrayList();
    }

    public static final Creator<Module> CREATOR = new Creator<Module>() {
        @Override
        public Module createFromParcel(Parcel in) {
            return new Module(in);
        }

        @Override
        public Module[] newArray(int size) {
            return new Module[size];
        }
    };

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public int getCredits() {
        return Credits;
    }

    public void setCredits(int credits) {
        this.Credits = credits;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        this.Level = level;
    }

    public List<String> getPathway() {
        return Pathway;
    }

    public void setPathway(List<String> pathway) {
        this.Pathway = pathway;
    }

    public List<String> getPrerequisites() {
        return Prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.Prerequisites = prerequisites;
    }

    public List<String> getCorequisite() {
        return Corequisite;
    }

    public void setCorequisite(List<String> corequisite) {
        this.Corequisite = corequisite;
    }

    public boolean getisFinished() {
        return isFinished;
    }

    public void setisFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean getisActive() {
        return isActive;
    }


    public void setisActive(boolean active) {
        isActive = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Code);
        parcel.writeString(Title);
        parcel.writeString(Description);
        parcel.writeString(Time);
        parcel.writeInt(Credits);
        parcel.writeInt(Level);
        parcel.writeStringList(Pathway);
        parcel.writeStringList(Prerequisites);
        parcel.writeStringList(Corequisite);
    }
}

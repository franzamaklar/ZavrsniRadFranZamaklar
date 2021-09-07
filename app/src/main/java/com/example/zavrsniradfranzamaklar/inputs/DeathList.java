package com.example.zavrsniradfranzamaklar.inputs;

import android.os.Parcel;
import android.os.Parcelable;

public class DeathList{
    private String name;
    private String surname;
    private String gender;
    private String birthdate;
    private String deathdate;
    private String deathtime;
    private String deathplace;
    private String deathcause;

    public DeathList(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }


    public String getDeathdate() {
        return deathdate;
    }

    public void setDeathdate(String deathdate) {
        this.deathdate = deathdate;
    }

    public String getDeathtime() {
        return deathtime;
    }

    public void setDeathtime(String deathtime) {
        this.deathtime = deathtime;
    }

    public String getDeathplace() {
        return deathplace;
    }

    public void setDeathplace(String deathplace) {
        this.deathplace = deathplace;
    }

   public String getDeathcause() {
        return deathcause;
    }

    public void setDeathcause(String deathcause) {
        this.deathcause = deathcause;
    }

}

package com.example.rfarm.constructors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class mating_records {
    String doe_nm;
    String buck_nm;
    String date_now;
    int number_of_bunnies;
    int n_bunnies_died;
    int n_bunnies_alive;
    int n_bunnies_sold;
    boolean time_birth;
    String birth_prep_date;
    String birth_date;
    String image;

    public mating_records() {
    }

    public mating_records(int n_bunnies_sold,String doe_nm, String buck_nm, String date_now, int number_of_bunnies, int n_bunnies_died, int n_bunnies_alive,  boolean time_birth,String birth_prep_date,String birth_date,String image) {
        this.doe_nm = doe_nm;
        this.buck_nm = buck_nm;
        this.date_now = date_now;
        this.number_of_bunnies = number_of_bunnies;
        this.n_bunnies_died = n_bunnies_died;
        this.n_bunnies_alive = n_bunnies_alive;
        this.time_birth = time_birth;
        this.birth_prep_date=birth_prep_date;
        this.birth_date=birth_date;
        this.image=image;
        this.n_bunnies_sold=n_bunnies_sold;
    }

    public int getN_bunnies_sold() {
        return n_bunnies_sold;
    }

    public void setN_bunnies_sold(int n_bunnies_sold) {
        this.n_bunnies_sold = n_bunnies_sold;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getBirth_prep_date() {
        return birth_prep_date;
    }

    public void setBirth_prep_date(String birth_prep_date) {
        this.birth_prep_date = birth_prep_date;
    }

    public String getDoe_nm() {
        return doe_nm;
    }

    public void setDoe_nm(String doe_nm) {
        this.doe_nm = doe_nm;
    }

    public String getBuck_nm() {
        return buck_nm;
    }

    public void setBuck_nm(String buck_nm) {
        this.buck_nm = buck_nm;
    }

    public String getDate_now() {
        return date_now;
    }

    public void setDate_now(String date_now) {
        this.date_now = date_now;
    }

    public int getNumber_of_bunnies() {
        return number_of_bunnies;
    }

    public void setNumber_of_bunnies(int number_of_bunnies) {
        this.number_of_bunnies = number_of_bunnies;
    }

    public int getN_bunnies_died() {
        return n_bunnies_died;
    }

    public void setN_bunnies_died(int n_bunnies_died) {
        this.n_bunnies_died = n_bunnies_died;
    }

    public int getN_bunnies_alive() {
        return n_bunnies_alive;
    }

    public void setN_bunnies_alive(int n_bunnies_alive) {
        this.n_bunnies_alive = n_bunnies_alive;
    }


    public boolean isTime_birth() {
        return time_birth;
    }

    public void setTime_birth(boolean time_birth) {
        this.time_birth = time_birth;
    }
}

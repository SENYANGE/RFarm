package com.example.rfarm.constructors;

public class ExpenseInfo {
    private String exp_category,exp_date,exp_amount,exp_type;

    public ExpenseInfo() {
    }

    public ExpenseInfo(String exp_category, String exp_date, String exp_amount, String exp_type) {
        this.exp_category = exp_category;
        this.exp_date = exp_date;
        this.exp_amount = exp_amount;
        this.exp_type = exp_type;
    }

    public String getExp_category() {
        return exp_category;
    }

    public void setExp_category(String exp_category) {
        this.exp_category = exp_category;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getExp_amount() {
        return exp_amount;
    }

    public void setExp_amount(String exp_amount) {
        this.exp_amount = exp_amount;
    }

    public String getExp_type() {
        return exp_type;
    }

    public void setExp_type(String exp_type) {
        this.exp_type = exp_type;
    }
}

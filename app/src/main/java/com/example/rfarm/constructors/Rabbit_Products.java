package com.example.rfarm.constructors;

public class Rabbit_Products {
   private String rabbit_tag;
   private int amount;
   private String date_sold;
   int qty;
   private boolean sold;
   private String img_url;
   private String prdct_type;

    public Rabbit_Products() {
    }

    public Rabbit_Products(String rabbit_tag, int amount, int qty,String date_sold, boolean sold,String prdct_type, String img_url) {
        this.rabbit_tag = rabbit_tag;
        this.amount = amount;
        this.date_sold = date_sold;
        this.sold = sold;
        this.prdct_type=prdct_type;
        this.img_url = img_url;
        this.qty=qty;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getPrdct_type() {
        return prdct_type;
    }

    public void setPrdct_type(String prdct_type) {
        this.prdct_type = prdct_type;
    }

    public String getRabbit_tag() {
        return rabbit_tag;
    }

    public void setRabbit_tag(String rabbit_tag) {
        this.rabbit_tag = rabbit_tag;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate_sold() {
        return date_sold;
    }

    public void setDate_sold(String date_sold) {
        this.date_sold = date_sold;
    }



    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}

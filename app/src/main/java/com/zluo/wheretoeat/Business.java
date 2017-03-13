package com.zluo.wheretoeat;

import java.util.List;

/**
 * Created by Jeff on 3/12/2017.
 */

class Business {
    //emitted unimportant ones
    private String id;
    private List<String> transactions;
    private double rating;
    private String name;
    //private YelpLocation location;
    private String phone;
    private double distance;
    private String image_url;
    private String url;
    private boolean is_closes;
    private String display_phone;
    private String price;
    private int review_count;
    public Business() {

    }

    public String getId() {
        return id;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public double getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }


    public String getPhone() {
        return phone;
    }

    public double getDistance() {
        return distance;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getUrl() {
        return url;
    }

    public boolean is_closes() {
        return is_closes;
    }

    public String getDisplay_phone() {
        return display_phone;
    }

    public String getPrice() {
        return price;
    }

    public int getReview_count() {
        return review_count;
    }
}

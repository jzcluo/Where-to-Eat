package com.zluo.wheretoeat;

import java.util.List;

/**
 * Created by Jeff on 3/12/2017.
 */

class Business {
    //emitted unimportant ones
    private String id;
    private List<String> transactions;
    private String rating;
    private String name;
    //private YelpLocation location;
    private String phone;
    private String distance;
    private String image_url;
    private String url;
    private Coordinates coordinates;
    private String is_closed;
    private List<Categories> categories;
    private String price;
    private String review_count;
    public Business() {

    }

    public String getId() {
        return id;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public String getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }


    public String getPhone() {
        return phone;
    }

    public String getDistance() {
        return distance;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getUrl() {
        return url;
    }

    public String getLatitude() {
        return coordinates.getLatitude();
    }

    public String getLongitude() {
        return coordinates.getLongitude();
    }

    public String getIs_closed() {
        return is_closed;
    }

    public String getCategories() {
        StringBuilder builder = new StringBuilder();
        for (Categories category : categories) {
            builder.append(category.getTitle()).append(", ");
        }
        builder.setLength(builder.length() - 2);
        return builder.toString();
    }

    public String getPrice() {
        return price;
    }

    public String getReview_count() {
        return review_count;
    }

    private class Categories {
        private String alias;
        private String title;

        public Categories() {

        }

        public String getAlias() {
            return alias;
        }
        public String getTitle() {
            return title;
        }
    }
    private class Coordinates {
        private String latitude;
        private String longitude;

        public Coordinates() {

        }

        public String getLatitude() {
            return latitude;
        }
        public String getLongitude() {
            return longitude;
        }
    }
}

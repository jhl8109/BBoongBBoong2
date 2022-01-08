package com.example.toilet;

import com.google.gson.annotations.SerializedName;

public class RoadAddress {
    @SerializedName("address_name")
    String address_name;

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public RoadAddress(String address_name) {
        this.address_name = address_name;
    }
}

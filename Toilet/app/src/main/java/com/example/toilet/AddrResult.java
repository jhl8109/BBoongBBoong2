package com.example.toilet;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddrResult {
    @SerializedName("documents")
    private List<Place> documents;
    public List<Place> getDocuments() {
        return documents;
    }
    public void setDocuments(List<Place> documents) {
        this.documents = documents;
    }


}

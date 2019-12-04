package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsPendingChekinResponse {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
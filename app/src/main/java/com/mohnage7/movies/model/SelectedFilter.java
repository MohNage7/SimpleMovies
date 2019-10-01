package com.mohnage7.movies.model;

import com.mohnage7.movies.utils.Constants;

public class SelectedFilter {
    int itemId;
    String value;

    public SelectedFilter(int itemId,@Constants.FilterBy String value) {
        this.itemId = itemId;
        this.value = value;
    }

    public int getItemId() {
        return itemId;
    }

    public String getValue() {
        return value;
    }
}

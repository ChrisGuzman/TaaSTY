package com.chris_guzman.taasty;

import android.net.Uri;

import java.net.URL;
import java.util.List;

/**
 * Created by chrisguzman on 5/28/17.
 */

public class AzureResponse {
    private List<Value> value;

    public List<Value> getValue() {
        return value;
    }

    public class Value {
        private String contentUrl;

        public String getContentUrl() {
            return contentUrl;
        }
    }
}

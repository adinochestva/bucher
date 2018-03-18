package com.plazza.app.main.service;

import com.plazza.app.main.model.Feed;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by sadegh on 9/16/2016.
 */

public interface RssService {


    @GET
    Call<Feed> getItems(@Url String url);


}

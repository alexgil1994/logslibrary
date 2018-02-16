package com.amarkovits.logslibrary.service;

import com.amarkovits.logslibrary.Model.Event;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import org.json.simple.JSONObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// Needed to be able to send a @RequestBody (Json log object)
public interface PostService {
    @POST("http://localhost:8080/v1/event/addMessage")
//    Call<ResponseBody> sendingLog(@Body RequestBody log);
    Call<Event> sendingLog(@Body JSONObject log);
}

package com.amarkovits.logslibrary.service;

import com.amarkovits.logslibrary.Model.Event;
import org.json.simple.JSONObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// Needed to be able to send a @RequestBody (Json log object)
public interface PostService {
    @POST("http://localhost:8080/v1/event/addMessage")
    Call<Event> sendingLog(@Body JSONObject log);
}

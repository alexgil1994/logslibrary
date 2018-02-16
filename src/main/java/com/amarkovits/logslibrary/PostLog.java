package com.amarkovits.logslibrary;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.amarkovits.logslibrary.Model.Event;
import com.amarkovits.logslibrary.service.PostService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.sun.xml.internal.bind.v2.TODO;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.json.simple.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class PostLog extends AppenderBase<ILoggingEvent> implements Callback<Event> {
//public class PostLog extends AppenderBase<ILoggingEvent>{

    // Created from the logback.xml configuration
    private String baseurl;
    private String applicationname;

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public String getApplicationname() {
        return applicationname;
    }

    public void setApplicationname(String applicationname) {
        this.applicationname = applicationname;
    }

    protected void append(ILoggingEvent iLoggingEvent) {

        // Setting the Event that will be returned
        Event event = new Event();

        // TODO Ta upoloipa applicationname mallon prepei na ginoun me configuration.
        event.setMessage(iLoggingEvent.getMessage());
        event.setTime(iLoggingEvent.getTimeStamp());
        event.setApplicationname(getApplicationname());
        event.setLevel(String.valueOf(iLoggingEvent.getLevel()));
        // Getting the host
        try {
            InetAddress host = InetAddress.getLocalHost();
            event.setHostname(String.valueOf(host));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // Getting the ip of the server that creates the log
        try {
            InetAddress ip = InetAddress.getLocalHost();
            event.setIp(ip.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        event.setCargo("Not specified");


//        iLoggingEvent.getCallerData();
//        iLoggingEvent.getLoggerName();
//        iLoggingEvent.getMDCPropertyMap();
//        iLoggingEvent.getThreadName();

        // Making an okHttpClient
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();

         // Todo Interceptor mallon den xreiazetai, auto einai gia na fainetai to logging, den einai kati pou thelw mallon
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();


        // Xrhsh tou Callback<Event> interface
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseurl())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build())
                .build();

        PostService postService = retrofit.create(PostService.class);

        try {
            JSONObject obj = new JSONObject();
            obj.put("message", event.getMessage());
            obj.put("time", event.getTime());
            obj.put("hostname", event.getHostname());
            obj.put("applicationname", event.getApplicationname());
            obj.put("level", event.getLevel());
            obj.put("ip", event.getLevel());
            obj.put("cargo", event.getCargo());

            Call<Event> call = postService.sendingLog(obj);
            call.enqueue(this);
        }catch (Exception e){
            e.printStackTrace();
        }

//        try {
//            JSONObject object2 = new JSONObject();
//            object2.put("message", event.getMessage());
//
//            Call<ResponseBody> call = postService.sendingLog(object.toString());
////            call.enqueue(this);
//            call.enqueue(new Callback<ResponseBody>() {
//                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//
//                }
//
//                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
//
//                }
//            });
//        }catch (Exception e){
//            e.printStackTrace();
//        }



        // TRY
        // Creating the JsonArray json.simple
//        JsonArray array = new JsonArray();
//        JSONObject object = new JSONObject();
//
//        object.put("message", event.getMessage());
//        object.put("time", event.getTime());
//        object.put("hostname", event.getHostname());
//        object.put("applicationname", event.getApplicationname());
//        object.put("level", event.getLevel());
//        object.put("cargo", event.getCargo());
//
//        array.add(String.valueOf(object));


        // Creating the JsonArray Gson
//        JsonArray array = new JsonArray();
//        JsonObject object = new JsonObject();
//
//        object.addProperty("message", event.getMessage());
//        object.addProperty("time", event.getTime());
//        object.addProperty("hostname", event.getHostname());
//        object.addProperty("applicationname", event.getApplicationname());
//        object.addProperty("level", event.getLevel());
//        object.addProperty("cargo", event.getCargo());

//        array.add(object);


//        JsonObject params = new JsonObject();
//        params.add("message", iLoggingEvent.getMessage());


//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new JsonObject(object).toString());
//        RequestBody body2 = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JsonObject(jsonParams)).toString());

//        Call<ResponseBody> response =


        // Retrofit
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://localhost:8080/v1/event/addMessage")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okhttpClientBuilder.build())
//                .build();


//        event = retrofit.create(Event.class);
//        Call<Event> call = event.

    }

    public void onResponse(Call<Event> call, retrofit2.Response<Event> response) {

    }

    public void onFailure(Call<Event> call, Throwable throwable) {

    }
}

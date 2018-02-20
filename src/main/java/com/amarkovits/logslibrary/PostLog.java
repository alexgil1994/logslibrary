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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class PostLog extends AppenderBase<ILoggingEvent> implements Callback<Event> {

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
            obj.put("ip", event.getIp());
            obj.put("cargo", event.getCargo());

            Call<Event> call = postService.sendingLog(obj);
            call.enqueue(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onResponse(Call<Event> call, retrofit2.Response<Event> response) {

    }

    public void onFailure(Call<Event> call, Throwable throwable) {

    }
}

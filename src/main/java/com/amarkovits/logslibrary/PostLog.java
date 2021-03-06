package com.amarkovits.logslibrary;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.amarkovits.logslibrary.Model.Event;
import com.amarkovits.logslibrary.service.PostService;
import okhttp3.OkHttpClient;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.net.InetAddress;
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

        Logger logger = LoggerFactory.getLogger(PostLog.class);

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
            logger.error("An error occurred when trying to get the host's name", e);
            e.printStackTrace();
        }

        // Getting the ip of the server that creates the log
        try {
            InetAddress ip = InetAddress.getLocalHost();
            event.setIp(ip.getHostAddress());
        } catch (UnknownHostException e) {
            logger.error("An error occurred when trying to get the host's ip", e);
            e.printStackTrace();
        }
        event.setCargo("Not specified");

        // Making an okHttpClient
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();

        // Using Callback<Event> interface ( onResponse, onFailure )
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
            logger.debug("Json Object was created with the desired informations and is ready to be posted");

            Call<Event> call = postService.sendingLog(obj);
            call.enqueue(this);
            logger.debug("Log was successfully posted to the db.");
        }catch (Exception e){
            logger.warn("When creating the JsonObject and sending the Post Request", e);
            e.printStackTrace();
        }
    }

    public void onResponse(Call<Event> call, retrofit2.Response<Event> response) {
//        TODO : LOG SUCCESSFUL ?
    }

    public void onFailure(Call<Event> call, Throwable throwable) {
//        TODO : LOG FAILED ?
    }
}

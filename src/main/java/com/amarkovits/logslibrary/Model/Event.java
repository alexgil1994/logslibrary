package com.amarkovits.logslibrary.Model;

import ch.qos.logback.classic.Level;

public class Event {

    private String message;

    private long time;

    private String ip;

    private String hostname;

    private String applicationname;

    private String level;

    private String cargo;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getApplicationname() {
        return applicationname;
    }

    public void setApplicationname(String applicationname) {
        this.applicationname = applicationname;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Event(String message, long time, String ip, String hostname, String applicationname, String level, String cargo) {
        this.message = message;
        this.time = time;
        this.ip = ip;
        this.hostname = hostname;
        this.applicationname = applicationname;
        this.level = level;
        this.cargo = cargo;
    }

    public Event() {
    }
}

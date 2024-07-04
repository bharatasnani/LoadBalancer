package com.demo.balancer;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * This class is used to store destination server information.
 */
public class Server {

    private String url;

    /**
     * Currently each server is assumed to handle infinite requests.
     * This can be used to set threshold of the server capacity.
     */
    private int threshold;
    private AtomicInteger requestCounter;

    private boolean healthy;

    public Server(String url) {
        this.url = url;
        requestCounter = new AtomicInteger(0);
        this.healthy = true;
    }

    public Server(String url, int threshold) {
        this.url = url;
        this.threshold = threshold;
        requestCounter = new AtomicInteger(0);
        this.healthy = true;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public AtomicInteger getRequestCounter() {
        return requestCounter;
    }

    public void setRequestCounter(AtomicInteger requestCounter) {
        this.requestCounter = requestCounter;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public void increaseLoad() {
        this.requestCounter.incrementAndGet();
    }

    public void decreaseLoad() {
        this.requestCounter.decrementAndGet();
    }

}

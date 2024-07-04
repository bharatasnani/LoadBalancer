package com.demo.balancer;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//@Component("roundRobin")
public class RoundRobinAlgorithm implements LoadBalancingAlgorithm{

    private AtomicInteger currentServerIndex;

    public RoundRobinAlgorithm() {
        currentServerIndex = new AtomicInteger(0);
    }

    /**
     * This function determines the destination server to fulfill the incoming request using round robin algorithm.
     * @param servers contains the list of destination servers
     * @return the destination server
     */
    public Server getServer(List<Server> servers) {
        if (servers == null || servers.isEmpty()) {
            throw new IllegalStateException("No servers available in the load balancer.");
        }

        int totalServers = servers.size();
        Server selected = servers.get(currentServerIndex.getAndIncrement()%totalServers);

        selected.increaseLoad();
        return selected;
    }
}

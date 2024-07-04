package com.demo.balancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component("leastConnections")
public class LeastConnectionsAlgorithm implements LoadBalancingAlgorithm{

    private final static Logger log = LoggerFactory.getLogger(LeastConnectionsAlgorithm.class);

    /**
     * This function determines the destination server to fulfill the incoming request using least connections algorithm.
     * @param servers contains the list of destination servers
     * @return the destination server
     */
    public Server getServer(List<Server> servers) {
        if (servers == null || servers.isEmpty()) {
            log.error("No destination servers available in the load balancer.");
            throw new IllegalStateException("No destination servers available in the load balancer.");
        }

        int minConnection = Integer.MAX_VALUE;
        Server leastConnectionServer = null;
        for (Server server : servers) {
            if (server.isHealthy() && server.getRequestCounter().get() < minConnection) {
                leastConnectionServer = server;
                minConnection = server.getRequestCounter().get();
            }
        }

        // Increment the load of the selected server
        leastConnectionServer.increaseLoad();

        return leastConnectionServer;
    }
}

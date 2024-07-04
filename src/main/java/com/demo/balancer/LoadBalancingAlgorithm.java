package com.demo.balancer;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This interface provides methods to get server based on the algorithm used for load balancing.
 */
@Component
public interface LoadBalancingAlgorithm {

    /**
     * This function determines the destination server to fulfill the incoming request.
     * @param servers contains the list of destination servers
     * @return the destination server
     */
    Server getServer(List<Server> servers);
}

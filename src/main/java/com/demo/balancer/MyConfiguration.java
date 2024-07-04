package com.demo.balancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MyConfiguration {

    @Autowired
    Environment env;

    @Bean
    public LoadBalancingAlgorithm createBean() {
        LoadBalancingAlgorithm bean;
        String algorithm = env.getProperty("load.balancer.algorithm", "roundRobin");
        switch(algorithm) {
            case "leastConnections":
                bean = new LeastConnectionsAlgorithm();
                break;
            default: bean = new RoundRobinAlgorithm();
        }
        return bean;
    }

}

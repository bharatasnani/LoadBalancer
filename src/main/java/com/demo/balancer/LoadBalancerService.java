package com.demo.balancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * This class provides services to handle request and add or remove destination servers.
 */
@Service
public class LoadBalancerService {

    private static final Logger log = LoggerFactory.getLogger(LoadBalancerService.class);

    private List<Server> servers;
    @Autowired
    private LoadBalancingAlgorithm loadBalancingAlgorithm;

    @Autowired
    private Environment env;

    public LoadBalancerService() {
        servers = new ArrayList<Server>();
    }

    /**
     * This functions load destination servers from properties file on server startup.
     */
    @PostConstruct
    public void loadServers() {
        String serversList = env.getProperty("destination.servers");
        if (serversList != null) {
            String[] list = serversList.split(",");
            for (String data : list) {
                servers.add(new Server(data));
            }
        }
    }

    /**
     * This function handles all the incoming request and gets the response from destination servers.
     * @param request
     * @return the response from destination server
     */
    public String handleRequest(String request) {
        if (request == null || request.isEmpty()) {
            log.error("request not found");
            throw new IllegalArgumentException("request not found");
        }
        Server server = loadBalancingAlgorithm.getServer(servers);
        log.debug("Server : " + server.getUrl() + " selected with current request counter : " + server.getRequestCounter().get());
        String response = forwardRequest(server, request);
        server.decreaseLoad();
        return response;
    }

    /**
     * This function forwards the request to destination server and returns the response.
     * @param server the destination server to send the request
     * @param request
     * @return the response
     */
    private String forwardRequest(Server server, String request) {
        String response = request + " from " + server.getUrl() + " and load is " + server.getRequestCounter().get();
        log.debug("Response generated for request : " + request + " is " + response);
        /*
        Uncomment to get some delay for response
        try {
            Thread.sleep(10000);
        } catch (Exception e) {

        }*/
        return response;
    }

    /**
     * This function adds the destination server
     * @param server
     */
    public void addServer(Server server) {
        Optional<Server> serverOptional = servers.stream().filter(s -> s.getUrl().equals(server.getUrl())).findFirst();
        if (serverOptional.isPresent()) {
            log.debug("Server already present for adding. Server url : " + server.getUrl());
            serverOptional.get().setHealthy(true);
        } else {
            server.setHealthy(true);
            servers.add(server);
        }
    }

    /**
     * This function removes the destination server
     * @param server
     */
    public void removeServer(Server server) {
        Optional<Server> serverOptional = servers.stream().filter(s -> s.getUrl().equals(server.getUrl())).findFirst();
        if (serverOptional.isPresent()) {
            serverOptional.get().setHealthy(false);
        } else {
            log.error("Server not found for removing. Server url : " + server.getUrl());
            throw new IllegalArgumentException("Server not found");
        }
    }
}

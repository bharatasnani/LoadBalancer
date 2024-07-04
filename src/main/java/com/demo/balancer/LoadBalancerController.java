package com.demo.balancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class provides the rest endpoints to forward request and other services.
 */
@RestController
public class LoadBalancerController {

    private final static Logger log = LoggerFactory.getLogger(LoadBalancerController.class);

    @Autowired
    private LoadBalancerService loadBalancerService;

    /**
     * This api endpoint forwards request to destination server.
     * @param path
     * @return the response with code
     */
    @GetMapping("/forward/{path}")
    public ResponseEntity<String> handleRequest(@PathVariable String path) {
        String response;
        try {
            response = loadBalancerService.handleRequest(path);
        } catch (Exception e) {
            log.error("Internal error occurred while forwarding request. Error trace:", e);
            return new ResponseEntity<>("{\"error\": \"internal error occurred\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Response from server : " + response, HttpStatus.OK);
    }

    /**
     * This api endpoint is used to add server dynamically.
     * @param server
     * @return the response with code
     */
    @PostMapping("/server")
    public ResponseEntity<String> addServer(@RequestBody Server server) {
        if (server.getUrl() == null || server.getUrl().isEmpty()) {
            log.error("Data not found for adding server.");
            return new ResponseEntity("Data not found for adding server", HttpStatus.NO_CONTENT);
        }

        try {
            loadBalancerService.addServer(server);
        } catch (Exception e) {
            return new ResponseEntity<>("{\"error\": \"internal error occurred\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.debug("Server added successfully. Server url : " + server.getUrl());
        return new ResponseEntity("Server added successfully", HttpStatus.OK);
    }

    /**
     * This api endpoint is used to remove server dynamically.
     * @param server
     * @return the response with code
     */
    @DeleteMapping("/server")
    public ResponseEntity<String> removeServer(@RequestBody Server server) {
        if (server.getUrl() == null || server.getUrl().isEmpty()) {
            log.error("Data not found for adding server.");
            return new ResponseEntity("Data not found for remove server", HttpStatus.NO_CONTENT);
        }

        try {
            loadBalancerService.removeServer(server);
        } catch (Exception e) {
            return new ResponseEntity<>("{\"error\": \"internal error occurred\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.debug("Server removed successfully. Server url : " + server.getUrl());
        return new ResponseEntity("Server removed successfully", HttpStatus.OK);
    }
}

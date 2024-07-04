package com.demo.balancer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TestLeastConnectionsAlgorithm {

    LoadBalancingAlgorithm leastConnectionsAlgorithm = new LeastConnectionsAlgorithm();

    ArrayList<Server> servers = new ArrayList<>(Arrays.asList(new Server("http://server1:8080"),
            new Server("http://server2:8080"), new Server("http://server3:8080")));

    @Test
    public void testAlgoWithNoServers() {
        assertThrows(IllegalStateException.class,
                ()->{
                    leastConnectionsAlgorithm.getServer(Collections.emptyList());
                });
    }

    @Test
    public void testLeastConnectionRequests() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(11);
        for (int i=0;i<11;i++) {
            executorService.submit(()->{
                leastConnectionsAlgorithm.getServer(servers);
            });
        }
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        assertEquals(4, servers.get(0).getRequestCounter().get());
        assertEquals(4, servers.get(1).getRequestCounter().get());
        assertEquals(3, servers.get(2).getRequestCounter().get());
    }

}

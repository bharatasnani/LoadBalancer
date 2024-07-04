package com.demo.balancer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TestLoadBalancerService {

	@Spy
	LoadBalancingAlgorithm loadBalancingAlgorithm = new RoundRobinAlgorithm();

	@Spy
	ArrayList<Server> servers = new ArrayList<>(Arrays.asList(new Server("http://server1:8080"),
			new Server("http://server2:8080"), new Server("http://server3:8080")));

	@InjectMocks
	LoadBalancerService loadBalancerService;

	@Test
	public void testWithNoRequest() {
		assertThrows(IllegalArgumentException.class,
				()->{
					loadBalancerService.handleRequest(null);
				});
	}

	@Test
	public void testHandleMultipleRequests() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		for (int i=0;i<5;i++) {
			executorService.submit(()->{
				String response = loadBalancerService.handleRequest("random");
				assertNotNull(response);
			});
		}

		executorService.awaitTermination(2, TimeUnit.SECONDS);

	}

	@Test
	public void testAddServerWithServerAlreadyPresent() {
		servers.get(0).setHealthy(false);
		Server server = new Server(servers.get(0).getUrl());
		loadBalancerService.addServer(server);
		assertEquals(servers.get(0).isHealthy(), true);
	}

	@Test
	public void testAddServer() {
		int initialServers = servers.size();
		Server server = new Server("test:8080");
		loadBalancerService.addServer(server);
		assertEquals(initialServers+1, servers.size());
	}

	@Test
	public void testRemoveServerWhenServerNotPresent() {
		Server server = new Server("test:8080");
		assertThrows(IllegalArgumentException.class,
				()->{
					loadBalancerService.removeServer(server);
				});
	}

	@Test
	public void testRemoveServer() {
		String url = servers.get(0).getUrl();
		Server server = new Server(url);
		loadBalancerService.removeServer(server);
		assertEquals(servers.stream().filter(s-> s.getUrl().equals(url)).findFirst().get().isHealthy(), false);
	}

}

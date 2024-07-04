# Load Balancer Spring Boot Application

This spring boot application serves as a simple load balancer and can be used to dynamically add or delete the server.

## Prerequisites
- Java 8
- Maven build tool
- Basic understanding of Spring Boot

## Setup Instructions
Follow these steps to setup and run the load balancer:

1) Download and extract the zip.
2) Open the project in IntelliJ or any other IDE.
3) Specify the port in application.properties. Default is 8080 in server.port.
4) Run the class - LoadBalancerApplication

## Steps to test
1) Hit the endpoint - http://localhost:8080/<port-used>/forward/{path} to send request to loadbalancer that has to be forwarded.
2) You can initiate the request from multiple browsers as well.
3) This can be tested by running the test cases.

## Call Flow
1) High-level: Client sends request -> Load balancer selects server -> Load balancer forwards request to server -> Server processes request and sends response -> Load balancer receives request and sends it back to client.
2) Low-Level: LoadBalancerController -> LoadBalancerService -> LoadBalancingAlgorithm -> Destination Server(returns the response)
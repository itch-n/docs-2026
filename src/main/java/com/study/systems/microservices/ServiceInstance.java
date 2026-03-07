package com.study.systems.microservices;

import java.util.*;
import java.util.List;
import java.util.Optional;

public class ServiceInstance {

    private final String id;          // Unique instance identifier (e.g. pod name)
    private final String serviceName; // Logical service name (e.g. "order-service")
    private final String host;
    private final int port;

    public ServiceInstance(String id, String serviceName, String host, int port) {
        this.id = id;
        this.serviceName = serviceName;
        this.host = host;
        this.port = port;
    }

    public String getId() { return id; }
    public String getServiceName() { return serviceName; }
    public String getHost() { return host; }
    public int getPort() { return port; }

    public String getAddress() {
        return host + ":" + port;
    }
}

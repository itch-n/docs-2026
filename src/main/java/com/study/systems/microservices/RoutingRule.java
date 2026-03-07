package com.study.systems.microservices;

import java.util.*;
import java.util.List;
import java.util.Optional;

public class RoutingRule {

    private final String pathPrefix;
    private final String serviceName;

    public RoutingRule(String pathPrefix, String serviceName) {
        this.pathPrefix = pathPrefix;
        this.serviceName = serviceName;
    }

    public String getPathPrefix() { return pathPrefix; }
    public String getServiceName() { return serviceName; }
}

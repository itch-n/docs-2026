package com.study.systems.microservices;

import java.util.*;
import java.util.List;
import java.util.Optional;

public class ApiGateway {

    private final ServiceRegistry registry;
    private final List<RoutingRule> routingRules;

    public ApiGateway(ServiceRegistry registry, List<RoutingRule> routingRules) {
        this.registry = registry;
        this.routingRules = routingRules;
    }

    /**
     * Route an inbound HTTP request to a backend service instance.
     *
     * @param request the inbound request from an external client
     * @return the address of the backend instance to forward to,
     *         or empty if no matching rule or no healthy instance exists
     *
     * TODO: Step 1 — Authenticate / authorise the request.
     *   Check a JWT or API key before routing. Reject with 401/403 if invalid.
     *   (In production this would call an auth service or validate locally.)
     *
     * TODO: Step 2 — Apply rate limiting.
     *   Check a per-client counter before forwarding.
     *   Return 429 Too Many Requests if the client is over quota.
     *
     * TODO: Step 3 — Find a matching routing rule.
     *   Iterate routingRules and find the first rule whose pathPrefix
     *   is a prefix of request.getPath().
     *
     * TODO: Step 4 — Look up healthy instances via the registry.
     *   Call registry.lookup(matchedRule.getServiceName()).
     *   If the list is empty, return empty (no healthy instances).
     *
     * TODO: Step 5 — Select an instance (load balancing).
     *   Implement round-robin or random selection over the instance list.
     *
     * TODO: Step 6 — Return the selected instance address.
     *   Return Optional.of(selectedInstance.getAddress()).
     */
    public Optional<String> route(HttpRequest request) {
        // TODO: implement steps above
        return Optional.empty(); // Replace
    }
}

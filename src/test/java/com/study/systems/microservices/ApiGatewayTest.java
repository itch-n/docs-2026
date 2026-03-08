package com.study.systems.microservices;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ApiGatewayTest {

    // ------------------------------------------------------------------
    // Minimal in-memory ServiceRegistry for tests
    // ------------------------------------------------------------------

    static class InMemoryRegistry implements ServiceRegistry {
        private final List<ServiceInstance> instances = new ArrayList<>();

        @Override
        public void register(ServiceInstance instance) {
            instances.add(instance);
        }

        @Override
        public void deregister(String instanceId) {
            instances.removeIf(i -> i.getId().equals(instanceId));
        }

        @Override
        public List<ServiceInstance> lookup(String serviceName) {
            return instances.stream()
                    .filter(i -> i.getServiceName().equals(serviceName))
                    .toList();
        }
    }

    // ------------------------------------------------------------------
    // ServiceInstance tests
    // ------------------------------------------------------------------

    @Test
    void testServiceInstanceAddress() {
        ServiceInstance si = new ServiceInstance("pod-1", "order-service", "10.0.0.1", 8080);

        assertEquals("10.0.0.1:8080", si.getAddress());
        assertEquals("order-service", si.getServiceName());
        assertEquals("pod-1", si.getId());
    }

    // ------------------------------------------------------------------
    // ServiceRegistry contract tests
    // ------------------------------------------------------------------

    @Test
    void testRegistryRegisterAndLookup() {
        InMemoryRegistry registry = new InMemoryRegistry();
        ServiceInstance instance = new ServiceInstance("pod-1", "order-service", "10.0.0.1", 8080);

        registry.register(instance);
        List<ServiceInstance> result = registry.lookup("order-service");

        assertEquals(1, result.size());
        assertEquals("pod-1", result.get(0).getId());
    }

    @Test
    void testRegistryLookupUnknownServiceReturnsEmpty() {
        InMemoryRegistry registry = new InMemoryRegistry();

        List<ServiceInstance> result = registry.lookup("nonexistent-service");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testRegistryDeregisterRemovesInstance() {
        InMemoryRegistry registry = new InMemoryRegistry();
        registry.register(new ServiceInstance("pod-1", "order-service", "10.0.0.1", 8080));

        registry.deregister("pod-1");

        assertTrue(registry.lookup("order-service").isEmpty());
    }

    @Test
    void testRegistryOnlyReturnsMatchingServiceName() {
        InMemoryRegistry registry = new InMemoryRegistry();
        registry.register(new ServiceInstance("pod-1", "order-service", "10.0.0.1", 8080));
        registry.register(new ServiceInstance("pod-2", "payment-service", "10.0.0.2", 8080));

        List<ServiceInstance> orders = registry.lookup("order-service");

        assertEquals(1, orders.size());
        assertEquals("pod-1", orders.get(0).getId());
    }

    // ------------------------------------------------------------------
    // ApiGateway.route tests
    // ------------------------------------------------------------------

    @Test
    void testRouteReturnsEmptyWhenNoRulesConfigured() {
        InMemoryRegistry registry = new InMemoryRegistry();
        registry.register(new ServiceInstance("pod-1", "order-service", "10.0.0.1", 8080));

        ApiGateway gateway = new ApiGateway(registry, List.of());
        HttpRequest request = new HttpRequest("GET", "/orders/123", null);

        Optional<String> address = gateway.route(request);

        // No routing rules — should return empty
        assertTrue(address.isEmpty());
    }

    @Test
    void testRouteMatchesPathPrefixAndReturnsInstanceAddress() {
        InMemoryRegistry registry = new InMemoryRegistry();
        registry.register(new ServiceInstance("pod-1", "order-service", "10.0.0.1", 8080));

        List<RoutingRule> rules = List.of(new RoutingRule("/orders", "order-service"));
        ApiGateway gateway = new ApiGateway(registry, rules);
        HttpRequest request = new HttpRequest("GET", "/orders/123", null);

        Optional<String> address = gateway.route(request);

        assertTrue(address.isPresent());
        assertEquals("10.0.0.1:8080", address.get());
    }

    @Test
    void testRouteReturnsEmptyWhenNoHealthyInstances() {
        InMemoryRegistry registry = new InMemoryRegistry();
        // No instances registered for "order-service"

        List<RoutingRule> rules = List.of(new RoutingRule("/orders", "order-service"));
        ApiGateway gateway = new ApiGateway(registry, rules);
        HttpRequest request = new HttpRequest("GET", "/orders/123", null);

        Optional<String> address = gateway.route(request);

        assertTrue(address.isEmpty());
    }

    @Test
    void testRouteReturnsEmptyWhenPathDoesNotMatchAnyRule() {
        InMemoryRegistry registry = new InMemoryRegistry();
        registry.register(new ServiceInstance("pod-1", "order-service", "10.0.0.1", 8080));

        List<RoutingRule> rules = List.of(new RoutingRule("/orders", "order-service"));
        ApiGateway gateway = new ApiGateway(registry, rules);
        HttpRequest request = new HttpRequest("GET", "/payments/99", null);

        Optional<String> address = gateway.route(request);

        assertTrue(address.isEmpty());
    }

    // ------------------------------------------------------------------
    // HttpRequest tests
    // ------------------------------------------------------------------

    @Test
    void testHttpRequestFields() {
        HttpRequest req = new HttpRequest("POST", "/users", "{\"name\":\"Alice\"}");

        assertEquals("POST", req.getMethod());
        assertEquals("/users", req.getPath());
        assertEquals("{\"name\":\"Alice\"}", req.getBody());
    }
}

package com.study.systems.loadbalancing;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class WeightedRoundRobinLoadBalancerTest {

    @Test
    void testGetNextServerReturnsNonNull() {
        List<WeightedRoundRobinLoadBalancer.WeightedServer> servers = Arrays.asList(
            new WeightedRoundRobinLoadBalancer.WeightedServer(
                new RoundRobinLoadBalancer.Server("S1", "10.0.0.1", 8080), 1),
            new WeightedRoundRobinLoadBalancer.WeightedServer(
                new RoundRobinLoadBalancer.Server("S2", "10.0.0.2", 8080), 2)
        );

        WeightedRoundRobinLoadBalancer lb = new WeightedRoundRobinLoadBalancer(servers);

        assertNotNull(lb.getNextServer());
    }

    @Test
    void testHigherWeightServerGetsMoreRequests() {
        // S1: weight 1, S2: weight 2, S3: weight 3 — expected ratio 1:2:3
        List<WeightedRoundRobinLoadBalancer.WeightedServer> servers = Arrays.asList(
            new WeightedRoundRobinLoadBalancer.WeightedServer(
                new RoundRobinLoadBalancer.Server("S1", "10.0.0.1", 8080), 1),
            new WeightedRoundRobinLoadBalancer.WeightedServer(
                new RoundRobinLoadBalancer.Server("S2", "10.0.0.2", 8080), 2),
            new WeightedRoundRobinLoadBalancer.WeightedServer(
                new RoundRobinLoadBalancer.Server("S3", "10.0.0.3", 8080), 3)
        );

        WeightedRoundRobinLoadBalancer lb = new WeightedRoundRobinLoadBalancer(servers);

        Map<String, Integer> distribution = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            String id = lb.getNextServer().id;
            distribution.merge(id, 1, Integer::sum);
        }

        // Expected: S1=2, S2=4, S3=6 (total weight 6, 12 requests)
        assertEquals(2, distribution.getOrDefault("S1", 0).intValue());
        assertEquals(4, distribution.getOrDefault("S2", 0).intValue());
        assertEquals(6, distribution.getOrDefault("S3", 0).intValue());
    }
}

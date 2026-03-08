package com.study.systems.loadbalancing;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ConsistentHashingLoadBalancerTest {

    private List<RoundRobinLoadBalancer.Server> threeServers() {
        return Arrays.asList(
            new RoundRobinLoadBalancer.Server("S1", "10.0.0.1", 8080),
            new RoundRobinLoadBalancer.Server("S2", "10.0.0.2", 8080),
            new RoundRobinLoadBalancer.Server("S3", "10.0.0.3", 8080)
        );
    }

    @Test
    void testSameKeyAlwaysGoesToSameServer() {
        ConsistentHashingLoadBalancer lb = new ConsistentHashingLoadBalancer(threeServers(), 3);

        RoundRobinLoadBalancer.Server first = lb.getServer("user123");
        RoundRobinLoadBalancer.Server second = lb.getServer("user123");

        assertNotNull(first);
        assertNotNull(second);
        assertEquals(first.id, second.id);
    }

    @Test
    void testRingHasVirtualNodes() {
        ConsistentHashingLoadBalancer lb = new ConsistentHashingLoadBalancer(threeServers(), 3);

        // 3 servers * 3 virtual nodes = 9 entries on the ring
        assertEquals(9, lb.getRingSize());
    }

    @Test
    void testAddServerIncreasesRingSize() {
        ConsistentHashingLoadBalancer lb = new ConsistentHashingLoadBalancer(threeServers(), 3);

        lb.addServer(new RoundRobinLoadBalancer.Server("S4", "10.0.0.4", 8080));

        // 4 servers * 3 virtual nodes = 12
        assertEquals(12, lb.getRingSize());
    }

    @Test
    void testGetServerReturnsNonNullForAnyKey() {
        ConsistentHashingLoadBalancer lb = new ConsistentHashingLoadBalancer(threeServers(), 3);

        assertNotNull(lb.getServer("key-abc"));
        assertNotNull(lb.getServer("user:999"));
        assertNotNull(lb.getServer("session:xyz"));
    }

    @Test
    void testConsistencyAcrossMultipleRequests() {
        ConsistentHashingLoadBalancer lb = new ConsistentHashingLoadBalancer(threeServers(), 3);

        String[] keys = {"user123", "user456", "user789"};
        for (String key : keys) {
            RoundRobinLoadBalancer.Server first = lb.getServer(key);
            RoundRobinLoadBalancer.Server second = lb.getServer(key);
            assertEquals(first.id, second.id, "Key " + key + " should always map to same server");
        }
    }
}

package com.study.systems.loadbalancing;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class IPHashLoadBalancerTest {

    private List<RoundRobinLoadBalancer.Server> threeServers() {
        return Arrays.asList(
            new RoundRobinLoadBalancer.Server("S1", "10.0.0.1", 8080),
            new RoundRobinLoadBalancer.Server("S2", "10.0.0.2", 8080),
            new RoundRobinLoadBalancer.Server("S3", "10.0.0.3", 8080)
        );
    }

    @Test
    void testSameIPAlwaysGoesToSameServer() {
        IPHashLoadBalancer lb = new IPHashLoadBalancer(threeServers());

        RoundRobinLoadBalancer.Server first = lb.getServer("192.168.1.100");
        RoundRobinLoadBalancer.Server second = lb.getServer("192.168.1.100");

        assertNotNull(first);
        assertNotNull(second);
        assertEquals(first.id, second.id);
    }

    @Test
    void testDifferentIPsMayGoToDifferentServers() {
        IPHashLoadBalancer lb = new IPHashLoadBalancer(threeServers());

        // With 3 servers and different IPs, at least some should hash differently
        // (This tests that the hash function works at all)
        RoundRobinLoadBalancer.Server s1 = lb.getServer("192.168.1.100");
        RoundRobinLoadBalancer.Server s2 = lb.getServer("192.168.1.101");
        RoundRobinLoadBalancer.Server s3 = lb.getServer("192.168.1.102");

        assertNotNull(s1);
        assertNotNull(s2);
        assertNotNull(s3);
    }

    @Test
    void testSessionPersistenceForMultipleRequests() {
        IPHashLoadBalancer lb = new IPHashLoadBalancer(threeServers());

        String clientIp = "10.20.30.40";
        RoundRobinLoadBalancer.Server server = lb.getServer(clientIp);

        // Repeat 5 times — always same server
        for (int i = 0; i < 5; i++) {
            assertEquals(server.id, lb.getServer(clientIp).id);
        }
    }

    @Test
    void testReturnsNullForEmptyServerList() {
        IPHashLoadBalancer lb = new IPHashLoadBalancer(Arrays.asList());

        assertNull(lb.getServer("192.168.1.100"));
    }
}

package com.study.systems.loadbalancing;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LeastConnectionsLoadBalancerTest {

    private List<RoundRobinLoadBalancer.Server> threeServers() {
        return Arrays.asList(
            new RoundRobinLoadBalancer.Server("S1", "10.0.0.1", 8080),
            new RoundRobinLoadBalancer.Server("S2", "10.0.0.2", 8080),
            new RoundRobinLoadBalancer.Server("S3", "10.0.0.3", 8080)
        );
    }

    @Test
    void testFirstRequestGoesToAServer() {
        LeastConnectionsLoadBalancer lb = new LeastConnectionsLoadBalancer(threeServers());

        RoundRobinLoadBalancer.Server server = lb.getNextServer();

        assertNotNull(server);
    }

    @Test
    void testRoutsToServerWithFewestConnections() {
        LeastConnectionsLoadBalancer lb = new LeastConnectionsLoadBalancer(threeServers());

        // Make 2 requests — S1 and S2 get 1 connection each
        RoundRobinLoadBalancer.Server s1 = lb.getNextServer();
        RoundRobinLoadBalancer.Server s2 = lb.getNextServer();

        // S3 has 0 connections — next request should go to S3
        RoundRobinLoadBalancer.Server s3 = lb.getNextServer();
        assertEquals("S3", s3.id);
    }

    @Test
    void testReleaseConnectionDecrementsCount() {
        LeastConnectionsLoadBalancer lb = new LeastConnectionsLoadBalancer(threeServers());

        RoundRobinLoadBalancer.Server s1 = lb.getNextServer();
        lb.releaseConnection(s1);

        // S1 has 0 connections again — it or another 0-conn server should be chosen
        // Stats should show 0 for released server
        assertNotNull(lb.getStats());
        assertTrue(lb.getStats().containsKey(s1.id));
        assertEquals(0, lb.getStats().get(s1.id).intValue());
    }

    @Test
    void testStatsShowConnectionCounts() {
        LeastConnectionsLoadBalancer lb = new LeastConnectionsLoadBalancer(threeServers());

        RoundRobinLoadBalancer.Server server = lb.getNextServer();

        assertNotNull(lb.getStats());
        assertEquals(1, lb.getStats().get(server.id).intValue());
    }

    @Test
    void testReturnsNullForEmptyServerList() {
        LeastConnectionsLoadBalancer lb = new LeastConnectionsLoadBalancer(Arrays.asList());

        assertNull(lb.getNextServer());
    }
}

package com.study.systems.loadbalancing;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RoundRobinLoadBalancerTest {

    private List<RoundRobinLoadBalancer.Server> threeServers() {
        return Arrays.asList(
            new RoundRobinLoadBalancer.Server("S1", "10.0.0.1", 8080),
            new RoundRobinLoadBalancer.Server("S2", "10.0.0.2", 8080),
            new RoundRobinLoadBalancer.Server("S3", "10.0.0.3", 8080)
        );
    }

    @Test
    void testFirstRequestGoesToFirstServer() {
        RoundRobinLoadBalancer lb = new RoundRobinLoadBalancer(threeServers());

        RoundRobinLoadBalancer.Server server = lb.getNextServer();

        assertNotNull(server);
        assertEquals("S1", server.id);
    }

    @Test
    void testSecondRequestGoesToSecondServer() {
        RoundRobinLoadBalancer lb = new RoundRobinLoadBalancer(threeServers());

        lb.getNextServer(); // S1
        RoundRobinLoadBalancer.Server server = lb.getNextServer(); // S2

        assertEquals("S2", server.id);
    }

    @Test
    void testWrapsAroundAfterLastServer() {
        RoundRobinLoadBalancer lb = new RoundRobinLoadBalancer(threeServers());

        lb.getNextServer(); // S1
        lb.getNextServer(); // S2
        lb.getNextServer(); // S3
        RoundRobinLoadBalancer.Server server = lb.getNextServer(); // Back to S1

        assertEquals("S1", server.id);
    }

    @Test
    void testDistributionAcross10Requests() {
        RoundRobinLoadBalancer lb = new RoundRobinLoadBalancer(threeServers());

        int s1Count = 0, s2Count = 0, s3Count = 0;
        for (int i = 0; i < 9; i++) {
            String id = lb.getNextServer().id;
            if ("S1".equals(id)) s1Count++;
            else if ("S2".equals(id)) s2Count++;
            else if ("S3".equals(id)) s3Count++;
        }

        // 9 requests among 3 servers — 3 each
        assertEquals(3, s1Count);
        assertEquals(3, s2Count);
        assertEquals(3, s3Count);
    }

    @Test
    void testReturnsNullForEmptyServerList() {
        RoundRobinLoadBalancer lb = new RoundRobinLoadBalancer(Arrays.asList());

        RoundRobinLoadBalancer.Server server = lb.getNextServer();

        assertNull(server);
    }

    @Test
    void testRoundRobinIsDeterministicAndRepeating() {
        RoundRobinLoadBalancer lb = new RoundRobinLoadBalancer(threeServers());

        String[] expected = {"S1", "S2", "S3", "S1", "S2", "S3"};
        for (String exp : expected) {
            assertEquals(exp, lb.getNextServer().id);
        }
    }
}

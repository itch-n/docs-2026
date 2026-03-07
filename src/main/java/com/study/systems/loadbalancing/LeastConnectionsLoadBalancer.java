package com.study.systems.loadbalancing;

import java.util.*;
/**
 * Least Connections: Route to server with fewest active connections
 *
 * Key principles:
 * - Track active connections per server
 * - Select server with minimum load
 * - Better for varying request durations
 * - More overhead than round robin
 */

public class LeastConnectionsLoadBalancer {

    private final List<ServerWithStats> servers;

    /**
     * Initialize least connections load balancer
     *
     * @param servers List of backend servers
     *
     * TODO: Initialize balancer
     * - Create ServerWithStats for each server
     * - Initialize connection counters to 0
     */
    public LeastConnectionsLoadBalancer(List<RoundRobinLoadBalancer.Server> servers) {
        // TODO: Wrap each server with connection counter

        this.servers = null; // Replace
    }

    /**
     * Select server with fewest connections
     *
     * @return Server with minimum active connections
     *
     * TODO: Implement least connections selection
     * 1. Find server with minimum connections
     * 2. Increment its connection count
     * 3. Return server
     *
     * Hint: Break ties by choosing first found
     */
    public synchronized RoundRobinLoadBalancer.Server getNextServer() {
        // TODO: Check if servers list is empty

        // TODO: Find server with minimum connections

        // TODO: Increment connection count for selected server

        // TODO: Return selected server

        return null; // Replace
    }

    /**
     * Release connection when request completes
     *
     * @param server Server to release connection from
     *
     * TODO: Decrement connection count
     * - Find server in list
     * - Decrement activeConnections
     * - Ensure doesn't go below 0
     */
    public synchronized void releaseConnection(RoundRobinLoadBalancer.Server server) {
        // TODO: Find ServerWithStats for given server

        // TODO: Decrement activeConnections (min 0)
    }

    /**
     * Get server statistics
     */
    public synchronized Map<String, Integer> getStats() {
        Map<String, Integer> stats = new HashMap<>();
        for (ServerWithStats s : servers) {
            stats.put(s.server.id, s.activeConnections);
        }
        return stats;
    }

    static class ServerWithStats {
        RoundRobinLoadBalancer.Server server;
        int activeConnections;

        public ServerWithStats(RoundRobinLoadBalancer.Server server) {
            this.server = server;
            this.activeConnections = 0;
        }
    }
}

package com.study.systems.loadbalancing;

import java.util.*;
/**
 * IP Hash: Route client to same server based on IP
 *
 * Key principles:
 * - Hash client IP to select server
 * - Ensures session persistence
 * - Client always hits same server
 * - Issues when server pool changes
 */

public class IPHashLoadBalancer {

    private final List<RoundRobinLoadBalancer.Server> servers;

    /**
     * Initialize IP hash load balancer
     *
     * @param servers List of backend servers
     */
    public IPHashLoadBalancer(List<RoundRobinLoadBalancer.Server> servers) {
        // TODO: Store servers
        this.servers = null; // Replace
    }

    /**
     * Select server based on client IP
     *
     * @param clientIP Client IP address
     * @return Server for this client
     *
     * TODO: Implement IP hash selection
     * 1. Hash the client IP
     * 2. Modulo by server count
     * 3. Return server at that index
     */
    public synchronized RoundRobinLoadBalancer.Server getServer(String clientIP) {
        // TODO: Check if servers list is empty

        // TODO: Hash clientIP to integer

        // TODO: Get index using modulo
        // index = abs(hash) % servers.size()

        // TODO: Return server at index

        return null; // Replace
    }

    /**
     * Hash IP address
     */
    private int hash(String ip) {
        // TODO: Hash IP string
        // Hint: ip.hashCode()
        return 0; // Replace
    }

    /**
     * Add server (warning: disrupts session persistence)
     */
    public synchronized void addServer(RoundRobinLoadBalancer.Server server) {
        // TODO: Add server
        // Note: This will change hash distribution
    }

    /**
     * Remove server (warning: disrupts session persistence)
     */
    public synchronized void removeServer(RoundRobinLoadBalancer.Server server) {
        // TODO: Remove server
        // Note: This will change hash distribution
    }
}

package com.study.systems.loadbalancing;

import java.util.*;
/**
 * Weighted Round Robin: Distribute based on server capacity
 *
 * Key principles:
 * - Servers have different weights (capacity)
 * - Higher weight = more requests
 * - Smooth distribution using GCD algorithm
 * - Good for heterogeneous servers
 */

public class WeightedRoundRobinLoadBalancer {

    private final List<WeightedServer> servers;
    private int currentIndex;
    private int currentWeight;
    private int maxWeight;
    private int gcd; // Greatest common divisor of weights

    /**
     * Initialize weighted round robin
     *
     * @param servers List of servers with weights
     *
     * TODO: Initialize balancer
     * - Store servers
     * - Calculate max weight and GCD
     * - Initialize currentWeight to max
     */
    public WeightedRoundRobinLoadBalancer(List<WeightedServer> servers) {
        // TODO: Store servers

        // TODO: Calculate maxWeight (max of all weights)

        // TODO: Calculate GCD of all weights

        // TODO: Initialize currentIndex to -1

        // TODO: Initialize currentWeight to 0

        this.servers = null; // Replace
    }

    /**
     * Select next server using weighted round robin
     *
     * @return Next server based on weight
     *
     * TODO: Implement weighted selection
     * 1. Loop until finding server with weight >= currentWeight
     * 2. When loop completes, decrease currentWeight by GCD
     * 3. Return selected server
     *
     * Hint: This ensures smooth distribution
     */
    public synchronized RoundRobinLoadBalancer.Server getNextServer() {
        // TODO: Loop through servers
        while (true) {
            // TODO: Move to next index (wraparound)
            // currentIndex = (currentIndex + 1) % servers.size()

            // TODO: Implement iteration/conditional logic

            // TODO: Implement iteration/conditional logic

        }
    }

    /**
     * Calculate GCD of all weights
     */
    private int calculateGCD() {
        // TODO: Calculate GCD of all server weights
        // Hint: Use Euclidean algorithm
        return 1; // Replace
    }

    /**
     * Calculate GCD of two numbers
     */
    private int gcd(int a, int b) {
        // TODO: Implement Euclidean algorithm
        return 0; // Replace
    }

    static class WeightedServer {
        RoundRobinLoadBalancer.Server server;
        int weight; // Capacity/power of server

        public WeightedServer(RoundRobinLoadBalancer.Server server, int weight) {
            this.server = server;
            this.weight = weight;
        }
    }
}

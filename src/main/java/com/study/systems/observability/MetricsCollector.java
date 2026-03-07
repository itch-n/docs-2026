package com.study.systems.observability;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Metrics Collection: Counter, Gauge, Histogram
 *
 * Metric types:
 * - Counter: monotonically increasing (requests, errors)
 * - Gauge: point-in-time value (CPU, memory, queue size)
 * - Histogram: distribution of values (latencies, sizes)
 *
 * Methods:
 * - RED: Rate, Errors, Duration
 * - USE: Utilization, Saturation, Errors
 */
public class MetricsCollector {

    /**
     * Counter: Monotonically increasing value
     * Time: O(1), Space: O(1)
     *
     * Use for: request counts, error counts, bytes processed
     */
    static class Counter {
        private final AtomicLong value = new AtomicLong(0);
        private final String name;
        private final Map<String, String> labels;

        Counter(String name, Map<String, String> labels) {
            this.name = name;
            this.labels = labels;
        }

        /**
         * Increment counter by 1
         * Time: O(1)
         *
         * TODO: Implement increment
         */
        public void inc() {
            // TODO: Increment value atomically
        }

        /**
         * Increment counter by delta
         * Time: O(1)
         *
         * TODO: Implement increment by delta
         */
        public void inc(long delta) {
            // TODO: Add delta to value atomically
        }

        public long get() {
            return value.get();
        }
    }

    /**
     * Gauge: Point-in-time value that can increase or decrease
     * Time: O(1), Space: O(1)
     *
     * Use for: CPU usage, memory usage, active connections
     */
    static class Gauge {
        private final java.util.concurrent.atomic.AtomicLong value = new java.util.concurrent.atomic.AtomicLong(0L); // scaled by 1000 (java.util.concurrent.atomic.AtomicLong requires Guava)
        private final String name;
        private final Map<String, String> labels;

        Gauge(String name, Map<String, String> labels) {
            this.name = name;
            this.labels = labels;
        }

        /**
         * Set gauge to specific value
         * Time: O(1)
         *
         * TODO: Implement gauge set
         */
        public void set(double val) {
            // TODO: Set value atomically
        }

        /**
         * Increment gauge
         * Time: O(1)
         *
         * TODO: Implement increment
         */
        public void inc(double delta) {
            // TODO: Add delta atomically
        }

        public void dec(double delta) {
            inc(-delta);
        }

        public double get() {
            return value.get();
        }
    }

    /**
     * Histogram: Distribution of observed values
     * Time: O(1) per observation, Space: O(B) where B = buckets
     *
     * Use for: request latencies, response sizes, batch sizes
     */
    static class Histogram {
        private final String name;
        private final double[] buckets; // Upper bounds
        private final AtomicLongArray counts; // Count per bucket
        private final AtomicLong sum = new AtomicLong(0);
        private final AtomicLong count = new AtomicLong(0);

        /**
         * Create histogram with specific buckets
         * Example: [0.01, 0.05, 0.1, 0.5, 1.0, 5.0]
         *
         * TODO: Initialize histogram
         */
        Histogram(String name, double[] buckets) {
            this.name = name;
            this.buckets = buckets;
            this.counts = new AtomicLongArray(buckets.length + 1); // +1 for +Inf
        }

        /**
         * Observe a value
         * Time: O(log B) with binary search
         *
         * TODO: Implement observation
         * 1. Find which bucket this value falls into
         * 2. Increment that bucket's count
         * 3. Update sum and count
         */
        public void observe(double value) {
            // TODO: Find bucket using binary search

            // TODO: Update sum and count
        }

        /**
         * Helper: Find bucket index for value
         *
         * TODO: Implement binary search
         */
        private int findBucket(double value) {
            // TODO: Binary search in buckets array
            return 0; // Replace
        }

        /**
         * Calculate percentile from histogram
         * Time: O(B)
         *
         * TODO: Implement percentile calculation
         */
        public double getPercentile(double percentile) {
            long totalCount = count.get();
            if (totalCount == 0) return 0.0;

            // TODO: Find bucket containing percentile

            return 0.0; // Replace
        }

        public double getAverage() {
            long c = count.get();
            return c > 0 ? (sum.get() / 1000.0) / c : 0.0;
        }
    }

    /**
     * RED Method: Rate, Errors, Duration
     * Time: O(1) per metric update
     *
     * TODO: Implement RED method tracking
     */
    static class REDMetrics {
        private final Counter requestCount;
        private final Counter errorCount;
        private final Histogram duration;

        REDMetrics(String service) {
            Map<String, String> labels = Map.of("service", service);
            this.requestCount = new Counter("requests_total", labels);
            this.errorCount = new Counter("errors_total", labels);
            this.duration = new Histogram("request_duration_seconds",
                new double[]{0.01, 0.05, 0.1, 0.5, 1.0, 5.0});
        }

        /**
         * Record successful request
         * Time: O(log B)
         *
         * TODO: Update RED metrics
         */
        public void recordRequest(double durationSeconds) {
            // TODO: Increment request count

            // TODO: Record duration
        }

        /**
         * Record failed request
         *
         * TODO: Update error metrics
         */
        public void recordError(double durationSeconds) {
            // TODO: Increment request and error counts

            // TODO: Record duration
        }

        public double getErrorRate() {
            long requests = requestCount.get();
            return requests > 0 ? (double)errorCount.get() / requests : 0.0;
        }

        public double getP50Latency() {
            return duration.getPercentile(0.50);
        }

        public double getP99Latency() {
            return duration.getPercentile(0.99);
        }
    }

    /**
     * USE Method: Utilization, Saturation, Errors
     * Time: O(1) per metric update
     *
     * TODO: Implement USE method tracking
     */
    static class USEMetrics {
        private final Gauge utilization;  // % of resource used
        private final Gauge saturation;   // Amount of queued work
        private final Counter errors;     // Error events

        USEMetrics(String resource) {
            Map<String, String> labels = Map.of("resource", resource);
            this.utilization = new Gauge("resource_utilization", labels);
            this.saturation = new Gauge("resource_saturation", labels);
            this.errors = new Counter("resource_errors", labels);
        }

        /**
         * Update resource utilization (0.0 to 1.0)
         *
         * TODO: Set utilization gauge
         */
        public void setUtilization(double percent) {
            // TODO: Set gauge value
        }

        /**
         * Update saturation (queue depth)
         *
         * TODO: Set saturation gauge
         */
        public void setSaturation(double queueDepth) {
            // TODO: Set gauge value
        }

        /**
         * Record resource error
         *
         * TODO: Increment error counter
         */
        public void recordError() {
            // TODO: Increment errors
        }
    }

}

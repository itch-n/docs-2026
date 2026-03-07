package com.study.systems.messagequeues;

import java.util.concurrent.*;
import java.util.*;
/**
 * Publish-Subscribe: Broadcast messages to multiple subscribers
 *
 * Key principles:
 * - One message delivered to all subscribers
 * - Topic-based routing
 * - Decoupled publishers and subscribers
 * - Each subscriber has own queue
 */

public class PubSubMessageQueue {

    private final Map<String, List<Subscriber>> topicSubscribers;
    private final Object lock = new Object();

    /**
     * Initialize pub-sub system
     *
     * TODO: Initialize topic mapping
     */
    public PubSubMessageQueue() {
        // TODO: Initialize topicSubscribers map (ConcurrentHashMap)
        this.topicSubscribers = null; // Replace
    }

    /**
     * Subscribe to topic
     *
     * @param topic Topic name
     * @param subscriber Subscriber to register
     *
     * TODO: Register subscriber
     * - Create topic if doesn't exist
     * - Add subscriber to topic list
     */
    public void subscribe(String topic, Subscriber subscriber) {
        synchronized (lock) {
            // TODO: Get or create subscriber list for topic

            // TODO: Add subscriber to list

            System.out.println(subscriber.name + " subscribed to " + topic);
        }
    }

    /**
     * Unsubscribe from topic
     *
     * TODO: Remove subscriber from topic
     */
    public void unsubscribe(String topic, Subscriber subscriber) {
        synchronized (lock) {
            // TODO: Get subscriber list for topic

            // TODO: Remove subscriber
        }
    }

    /**
     * Publish message to topic
     *
     * @param topic Topic to publish to
     * @param message Message to publish
     *
     * TODO: Deliver to all subscribers
     * - Get all subscribers for topic
     * - Send message to each subscriber's queue
     */
    public void publish(String topic, SimpleMessageQueue.Message message) {
        synchronized (lock) {
            // TODO: Get subscribers for topic

            // TODO: Implement iteration/conditional logic

            System.out.println("Published to " + topic + ": " + message);
        }
    }

    /**
     * Get topic statistics
     */
    public Map<String, Integer> getTopicStats() {
        Map<String, Integer> stats = new HashMap<>();
        synchronized (lock) {
            for (Map.Entry<String, List<Subscriber>> entry : topicSubscribers.entrySet()) {
                stats.put(entry.getKey(), entry.getValue().size());
            }
        }
        return stats;
    }

    static class Subscriber {
        String name;
        Queue<SimpleMessageQueue.Message> queue;

        public Subscriber(String name) {
            this.name = name;
            this.queue = new LinkedList<>();
        }

        public void deliver(SimpleMessageQueue.Message message) {
            queue.offer(message);
        }

        public SimpleMessageQueue.Message receive() {
            return queue.poll();
        }

        public int getQueueSize() {
            return queue.size();
        }
    }
}

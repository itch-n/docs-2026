package com.study.systems.messagequeues;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PubSubMessageQueueTest {

    @Test
    void testSubscriberReceivesPublishedMessage() {
        PubSubMessageQueue pubsub = new PubSubMessageQueue();
        PubSubMessageQueue.Subscriber sub = new PubSubMessageQueue.Subscriber("User1");

        pubsub.subscribe("news", sub);
        pubsub.publish("news", new SimpleMessageQueue.Message("n1", "Breaking news!"));

        assertEquals(1, sub.getQueueSize());
        SimpleMessageQueue.Message msg = sub.receive();
        assertNotNull(msg);
        assertEquals("n1", msg.id);
    }

    @Test
    void testMultipleSubscribersReceiveMessage() {
        PubSubMessageQueue pubsub = new PubSubMessageQueue();
        PubSubMessageQueue.Subscriber sub1 = new PubSubMessageQueue.Subscriber("User1");
        PubSubMessageQueue.Subscriber sub2 = new PubSubMessageQueue.Subscriber("User2");

        pubsub.subscribe("news", sub1);
        pubsub.subscribe("news", sub2);
        pubsub.publish("news", new SimpleMessageQueue.Message("n1", "Breaking news!"));

        assertEquals(1, sub1.getQueueSize());
        assertEquals(1, sub2.getQueueSize());
    }

    @Test
    void testSubscriberOnlyReceivesSubscribedTopics() {
        PubSubMessageQueue pubsub = new PubSubMessageQueue();
        PubSubMessageQueue.Subscriber sub1 = new PubSubMessageQueue.Subscriber("User1");
        PubSubMessageQueue.Subscriber sub2 = new PubSubMessageQueue.Subscriber("User2");

        pubsub.subscribe("news", sub1);
        pubsub.subscribe("sports", sub2);

        pubsub.publish("news", new SimpleMessageQueue.Message("n1", "News item"));
        pubsub.publish("sports", new SimpleMessageQueue.Message("s1", "Sports update"));

        assertEquals(1, sub1.getQueueSize()); // Only news
        assertEquals(1, sub2.getQueueSize()); // Only sports
    }

    @Test
    void testSubscriberSubscribedToMultipleTopicsReceivesAll() {
        PubSubMessageQueue pubsub = new PubSubMessageQueue();
        PubSubMessageQueue.Subscriber sub = new PubSubMessageQueue.Subscriber("User2");

        pubsub.subscribe("news", sub);
        pubsub.subscribe("sports", sub);

        pubsub.publish("news", new SimpleMessageQueue.Message("n1", "News"));
        pubsub.publish("sports", new SimpleMessageQueue.Message("s1", "Sports"));

        assertEquals(2, sub.getQueueSize());
    }

    @Test
    void testTopicStatsReflectSubscriberCounts() {
        PubSubMessageQueue pubsub = new PubSubMessageQueue();
        PubSubMessageQueue.Subscriber sub1 = new PubSubMessageQueue.Subscriber("User1");
        PubSubMessageQueue.Subscriber sub2 = new PubSubMessageQueue.Subscriber("User2");

        pubsub.subscribe("news", sub1);
        pubsub.subscribe("news", sub2);
        pubsub.subscribe("sports", sub2);

        assertEquals(2, pubsub.getTopicStats().get("news").intValue());
        assertEquals(1, pubsub.getTopicStats().get("sports").intValue());
    }

    @Test
    void testNoMessageDeliveredToUnsubscribedTopic() {
        PubSubMessageQueue pubsub = new PubSubMessageQueue();
        PubSubMessageQueue.Subscriber sub = new PubSubMessageQueue.Subscriber("User1");

        pubsub.subscribe("news", sub);
        pubsub.publish("sports", new SimpleMessageQueue.Message("s1", "Sports"));

        assertEquals(0, sub.getQueueSize());
    }
}

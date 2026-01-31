# 08. Message Queues

> Asynchronous communication patterns for decoupled, scalable systems

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing different message queue patterns, explain them simply.

**Prompts to guide you:**

1. **What is a message queue in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why do we need message queues?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy for simple queue:**
   - Example: "A simple queue is like a line at a store where..."
   - Your analogy: _[Fill in]_

4. **What is the producer-consumer pattern in one sentence?**
   - Your answer: _[Fill in after implementation]_

5. **How is pub-sub different from producer-consumer?**
   - Your answer: _[Fill in after implementation]_

6. **Real-world analogy for pub-sub:**
   - Example: "Pub-sub is like a newsletter subscription where..."
   - Your analogy: _[Fill in]_

7. **What is a priority queue in one sentence?**
   - Your answer: _[Fill in after implementation]_

8. **When would you use a dead letter queue?**
   - Your answer: _[Fill in after implementation]_

---

## Core Implementation

### Part 1: Simple Message Queue

**Your task:** Implement a basic FIFO message queue.

```java
import java.util.*;
import java.util.concurrent.*;

/**
 * Simple Message Queue: FIFO with blocking operations
 *
 * Key principles:
 * - First In First Out ordering
 * - Blocking when empty (wait for messages)
 * - Thread-safe operations
 * - Decouples producers and consumers
 */

public class SimpleMessageQueue {

    private final Queue<Message> queue;
    private final int capacity;
    private final Object lock = new Object();

    /**
     * Initialize simple message queue
     *
     * @param capacity Maximum queue size
     *
     * TODO: Initialize queue
     * - Create LinkedList for messages
     * - Set capacity limit
     */
    public SimpleMessageQueue(int capacity) {
        // TODO: Initialize queue (LinkedList)

        // TODO: Store capacity

        this.queue = null; // Replace
        this.capacity = 0;
    }

    /**
     * Send message to queue (producer)
     *
     * @param message Message to send
     * @throws InterruptedException if interrupted while waiting
     *
     * TODO: Implement send
     * 1. Wait if queue is full
     * 2. Add message to queue
     * 3. Notify waiting consumers
     *
     * Hint: Use wait() and notifyAll() with synchronized block
     */
    public void send(Message message) throws InterruptedException {
        synchronized (lock) {
            // TODO: While queue is full, wait
            // while (queue.size() >= capacity):
            //   lock.wait()

            // TODO: Add message to queue

            // TODO: Notify all waiting consumers
            // lock.notifyAll()
        }
    }

    /**
     * Receive message from queue (consumer)
     *
     * @return Next message from queue
     * @throws InterruptedException if interrupted while waiting
     *
     * TODO: Implement receive
     * 1. Wait if queue is empty
     * 2. Remove and return message
     * 3. Notify waiting producers
     */
    public Message receive() throws InterruptedException {
        synchronized (lock) {
            // TODO: While queue is empty, wait
            // while (queue.isEmpty()):
            //   lock.wait()

            // TODO: Remove message from queue

            // TODO: Notify all waiting producers
            // lock.notifyAll()

            // TODO: Return message

            return null; // Replace
        }
    }

    /**
     * Try to receive with timeout
     *
     * @param timeoutMs Timeout in milliseconds
     * @return Message or null if timeout
     */
    public Message receive(long timeoutMs) throws InterruptedException {
        synchronized (lock) {
            long deadline = System.currentTimeMillis() + timeoutMs;

            // TODO: Wait until message available or timeout
            // while (queue.isEmpty() && timeRemaining > 0):
            //   lock.wait(timeRemaining)
            //   timeRemaining = deadline - currentTime

            // TODO: If queue not empty, remove and return message

            return null; // Replace (or message)
        }
    }

    /**
     * Get queue size
     */
    public synchronized int size() {
        return queue.size();
    }

    /**
     * Check if queue is empty
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    static class Message {
        String id;
        String content;
        long timestamp;

        public Message(String id, String content) {
            this.id = id;
            this.content = content;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return "Message{id='" + id + "', content='" + content + "'}";
        }
    }
}
```

### Part 2: Producer-Consumer Pattern

**Your task:** Implement producer-consumer with multiple workers.

```java
/**
 * Producer-Consumer: Multiple producers and consumers processing work
 *
 * Key principles:
 * - Work distribution across consumers
 * - Load balancing
 * - Backpressure handling
 * - Graceful shutdown
 */

public class ProducerConsumer {

    private final SimpleMessageQueue queue;
    private final List<Thread> consumerThreads;
    private volatile boolean running;

    /**
     * Initialize producer-consumer system
     *
     * @param queueCapacity Queue size
     * @param numConsumers Number of consumer threads
     *
     * TODO: Initialize system
     * - Create message queue
     * - Create consumer threads
     * - Set running flag
     */
    public ProducerConsumer(int queueCapacity, int numConsumers) {
        // TODO: Create SimpleMessageQueue

        // TODO: Initialize consumer threads list

        // TODO: Set running to true

        this.queue = null; // Replace
        this.consumerThreads = null; // Replace
    }

    /**
     * Start all consumers
     *
     * TODO: Start consumer threads
     * - Each consumer polls queue and processes messages
     * - Handle InterruptedException
     * - Check running flag
     */
    public void start() {
        // TODO: For each consumer:
        //   Create thread that:
        //     - Runs while 'running' is true
        //     - Receives message from queue
        //     - Processes message
        //     - Handles exceptions
        //   Start thread
        //   Add to consumerThreads list
    }

    /**
     * Produce message (called by producers)
     *
     * TODO: Send message to queue
     */
    public void produce(String messageId, String content) throws InterruptedException {
        // TODO: Create Message and send to queue
    }

    /**
     * Process message (override in subclass for custom logic)
     *
     * TODO: Implement message processing
     * - Extract message content
     * - Perform work
     * - Handle errors
     */
    protected void processMessage(SimpleMessageQueue.Message message) {
        // TODO: Process message (simulated work)
        System.out.println(Thread.currentThread().getName() +
                          " processing: " + message);

        // TODO: Simulate work
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Shutdown system
     *
     * TODO: Graceful shutdown
     * - Set running to false
     * - Wait for consumers to finish
     */
    public void shutdown() throws InterruptedException {
        // TODO: Set running to false

        // TODO: Interrupt all consumer threads

        // TODO: Wait for all threads to finish (join)
    }

    /**
     * Get queue statistics
     */
    public QueueStats getStats() {
        return new QueueStats(queue.size(), consumerThreads.size());
    }

    static class QueueStats {
        int queueSize;
        int activeConsumers;

        public QueueStats(int queueSize, int activeConsumers) {
            this.queueSize = queueSize;
            this.activeConsumers = activeConsumers;
        }
    }
}
```

### Part 3: Publish-Subscribe Pattern

**Your task:** Implement pub-sub for multiple subscribers.

```java
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

            // TODO: For each subscriber:
            //   Add message to subscriber's queue

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
```

### Part 4: Priority Message Queue

**Your task:** Implement priority queue for urgent messages.

```java
/**
 * Priority Message Queue: Process high-priority messages first
 *
 * Key principles:
 * - Priority levels (HIGH, MEDIUM, LOW)
 * - Higher priority processed first
 * - FIFO within same priority
 * - Prevents starvation of low priority
 */

public class PriorityMessageQueue {

    private final PriorityQueue<PriorityMessage> queue;
    private final Object lock = new Object();
    private final int capacity;

    /**
     * Initialize priority queue
     *
     * @param capacity Maximum queue size
     *
     * TODO: Initialize priority queue
     * - Create PriorityQueue with comparator
     * - Sort by priority then timestamp
     */
    public PriorityMessageQueue(int capacity) {
        // TODO: Create PriorityQueue with comparator
        // Comparator: First by priority (descending), then timestamp (ascending)

        // TODO: Store capacity

        this.queue = null; // Replace
        this.capacity = 0;
    }

    /**
     * Send message with priority
     *
     * TODO: Add message to priority queue
     * - Wait if queue is full
     * - Add message
     * - Notify consumers
     */
    public void send(PriorityMessage message) throws InterruptedException {
        synchronized (lock) {
            // TODO: Wait while queue is full

            // TODO: Add message to queue

            // TODO: Notify waiting consumers
        }
    }

    /**
     * Receive highest priority message
     *
     * TODO: Get message with highest priority
     * - Wait if queue is empty
     * - Remove highest priority message
     * - Notify producers
     */
    public PriorityMessage receive() throws InterruptedException {
        synchronized (lock) {
            // TODO: Wait while queue is empty

            // TODO: Poll highest priority message

            // TODO: Notify waiting producers

            return null; // Replace
        }
    }

    /**
     * Get queue size
     */
    public synchronized int size() {
        return queue.size();
    }

    static class PriorityMessage implements Comparable<PriorityMessage> {
        String id;
        String content;
        Priority priority;
        long timestamp;

        public PriorityMessage(String id, String content, Priority priority) {
            this.id = id;
            this.content = content;
            this.priority = priority;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public int compareTo(PriorityMessage other) {
            // TODO: Compare by priority first (higher priority first)
            // Then by timestamp (earlier first)

            // Hint:
            // int priorityCompare = other.priority.value - this.priority.value;
            // if (priorityCompare != 0) return priorityCompare;
            // return Long.compare(this.timestamp, other.timestamp);

            return 0; // Replace
        }

        @Override
        public String toString() {
            return "PriorityMessage{id='" + id + "', priority=" + priority + "}";
        }
    }

    enum Priority {
        LOW(1), MEDIUM(2), HIGH(3);

        final int value;

        Priority(int value) {
            this.value = value;
        }
    }
}
```

### Part 5: Dead Letter Queue

**Your task:** Implement dead letter queue for failed messages.

```java
/**
 * Dead Letter Queue: Handle messages that fail processing
 *
 * Key principles:
 * - Retry failed messages
 * - Max retry limit
 * - Move to DLQ after max retries
 * - Allows manual inspection/reprocessing
 */

public class DeadLetterQueue {

    private final SimpleMessageQueue mainQueue;
    private final SimpleMessageQueue dlq;
    private final int maxRetries;
    private final Map<String, Integer> retryCount;

    /**
     * Initialize dead letter queue system
     *
     * @param capacity Queue capacity
     * @param maxRetries Maximum retry attempts
     *
     * TODO: Initialize queues
     * - Create main queue
     * - Create DLQ
     * - Initialize retry counter
     */
    public DeadLetterQueue(int capacity, int maxRetries) {
        // TODO: Create main queue

        // TODO: Create DLQ

        // TODO: Store maxRetries

        // TODO: Initialize retry counter map

        this.mainQueue = null; // Replace
        this.dlq = null; // Replace
        this.maxRetries = 0;
        this.retryCount = null; // Replace
    }

    /**
     * Send message to main queue
     */
    public void send(SimpleMessageQueue.Message message) throws InterruptedException {
        // TODO: Send to main queue
        // Initialize retry count to 0
    }

    /**
     * Process message with retry logic
     *
     * @param processor Message processor
     * @return true if processed successfully
     *
     * TODO: Process with retries
     * 1. Receive message from main queue
     * 2. Try to process
     * 3. If fails, check retry count
     * 4. If under limit, requeue with incremented count
     * 5. If over limit, move to DLQ
     */
    public boolean processWithRetry(MessageProcessor processor) throws InterruptedException {
        // TODO: Receive message from main queue

        // TODO: Try to process message
        try {
            // processor.process(message)
            // return true if successful
        } catch (Exception e) {
            // TODO: Get current retry count

            // TODO: If under max retries:
            //   Increment retry count
            //   Send back to main queue
            //   Return false

            // TODO: If at max retries:
            //   Send to DLQ
            //   Remove from retry count
            //   Return false
        }

        return false; // Replace
    }

    /**
     * Get message from DLQ for manual processing
     */
    public SimpleMessageQueue.Message receiveDLQ() throws InterruptedException {
        return dlq.receive();
    }

    /**
     * Reprocess message from DLQ (manual retry)
     */
    public void reprocessFromDLQ(SimpleMessageQueue.Message message) throws InterruptedException {
        // TODO: Reset retry count and send to main queue
    }

    /**
     * Get statistics
     */
    public DLQStats getStats() {
        return new DLQStats(
            mainQueue.size(),
            dlq.size(),
            retryCount.size()
        );
    }

    interface MessageProcessor {
        void process(SimpleMessageQueue.Message message) throws Exception;
    }

    static class DLQStats {
        int mainQueueSize;
        int dlqSize;
        int messagesWithRetries;

        public DLQStats(int mainQueueSize, int dlqSize, int messagesWithRetries) {
            this.mainQueueSize = mainQueueSize;
            this.dlqSize = dlqSize;
            this.messagesWithRetries = messagesWithRetries;
        }

        @Override
        public String toString() {
            return "DLQStats{main=" + mainQueueSize +
                   ", dlq=" + dlqSize +
                   ", retrying=" + messagesWithRetries + "}";
        }
    }
}
```

---

## Client Code

```java
import java.util.*;

public class MessageQueuesClient {

    public static void main(String[] args) throws Exception {
        testSimpleQueue();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testProducerConsumer();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testPubSub();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testPriorityQueue();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testDeadLetterQueue();
    }

    static void testSimpleQueue() throws InterruptedException {
        System.out.println("=== Simple Message Queue Test ===\n");

        SimpleMessageQueue queue = new SimpleMessageQueue(5);

        // Test: Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    SimpleMessageQueue.Message msg =
                        new SimpleMessageQueue.Message("msg" + i, "Content " + i);
                    queue.send(msg);
                    System.out.println("Sent: " + msg);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Test: Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    SimpleMessageQueue.Message msg = queue.receive();
                    System.out.println("Received: " + msg);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        System.out.println("\nFinal queue size: " + queue.size());
    }

    static void testProducerConsumer() throws InterruptedException {
        System.out.println("=== Producer-Consumer Test ===\n");

        ProducerConsumer pc = new ProducerConsumer(10, 3);
        pc.start();

        // Produce messages
        System.out.println("Producing 10 messages...");
        for (int i = 1; i <= 10; i++) {
            pc.produce("msg" + i, "Task " + i);
            Thread.sleep(50);
        }

        // Let consumers process
        Thread.sleep(2000);

        System.out.println("\nStats: " + pc.getStats());
        pc.shutdown();
    }

    static void testPubSub() throws InterruptedException {
        System.out.println("=== Pub-Sub Test ===\n");

        PubSubMessageQueue pubsub = new PubSubMessageQueue();

        // Create subscribers
        PubSubMessageQueue.Subscriber sub1 = new PubSubMessageQueue.Subscriber("User1");
        PubSubMessageQueue.Subscriber sub2 = new PubSubMessageQueue.Subscriber("User2");
        PubSubMessageQueue.Subscriber sub3 = new PubSubMessageQueue.Subscriber("User3");

        // Subscribe to topics
        pubsub.subscribe("news", sub1);
        pubsub.subscribe("news", sub2);
        pubsub.subscribe("sports", sub2);
        pubsub.subscribe("sports", sub3);

        System.out.println("\nTopic stats: " + pubsub.getTopicStats());

        // Publish messages
        System.out.println("\nPublishing messages:");
        pubsub.publish("news", new SimpleMessageQueue.Message("n1", "Breaking news!"));
        pubsub.publish("sports", new SimpleMessageQueue.Message("s1", "Game update!"));

        // Check subscriber queues
        System.out.println("\nSubscriber queues:");
        System.out.println("User1 queue size: " + sub1.getQueueSize());
        System.out.println("User2 queue size: " + sub2.getQueueSize());
        System.out.println("User3 queue size: " + sub3.getQueueSize());

        // Receive messages
        System.out.println("\nUser1 receives: " + sub1.receive());
        System.out.println("User2 receives: " + sub2.receive());
        System.out.println("User2 receives: " + sub2.receive());
    }

    static void testPriorityQueue() throws InterruptedException {
        System.out.println("=== Priority Queue Test ===\n");

        PriorityMessageQueue queue = new PriorityMessageQueue(10);

        // Send messages with different priorities
        System.out.println("Sending messages:");
        queue.send(new PriorityMessageQueue.PriorityMessage(
            "m1", "Low priority", PriorityMessageQueue.Priority.LOW));
        queue.send(new PriorityMessageQueue.PriorityMessage(
            "m2", "High priority", PriorityMessageQueue.Priority.HIGH));
        queue.send(new PriorityMessageQueue.PriorityMessage(
            "m3", "Medium priority", PriorityMessageQueue.Priority.MEDIUM));
        queue.send(new PriorityMessageQueue.PriorityMessage(
            "m4", "High priority 2", PriorityMessageQueue.Priority.HIGH));

        System.out.println("Queue size: " + queue.size());

        // Receive in priority order
        System.out.println("\nReceiving messages (priority order):");
        while (queue.size() > 0) {
            PriorityMessageQueue.PriorityMessage msg = queue.receive();
            System.out.println("Received: " + msg);
        }
    }

    static void testDeadLetterQueue() throws InterruptedException {
        System.out.println("=== Dead Letter Queue Test ===\n");

        DeadLetterQueue dlq = new DeadLetterQueue(10, 3);

        // Create failing processor
        DeadLetterQueue.MessageProcessor failingProcessor = message -> {
            System.out.println("Processing: " + message.id);
            if (message.id.equals("msg2")) {
                throw new Exception("Simulated failure");
            }
        };

        // Send messages
        System.out.println("Sending messages:");
        dlq.send(new SimpleMessageQueue.Message("msg1", "Good message"));
        dlq.send(new SimpleMessageQueue.Message("msg2", "Bad message"));
        dlq.send(new SimpleMessageQueue.Message("msg3", "Good message"));

        // Process messages
        System.out.println("\nProcessing messages:");
        for (int i = 0; i < 3; i++) {
            boolean success = dlq.processWithRetry(failingProcessor);
            System.out.println("Process attempt " + (i+1) + ": " +
                             (success ? "SUCCESS" : "FAILED"));
            System.out.println("Stats: " + dlq.getStats());
        }

        // Try more times to move bad message to DLQ
        System.out.println("\nRetrying failed message:");
        for (int i = 0; i < 3; i++) {
            dlq.processWithRetry(failingProcessor);
            System.out.println("Stats: " + dlq.getStats());
        }
    }
}
```

---

## Decision Framework

**Questions to answer after implementation:**

### 1. Pattern Selection

**When to use Simple Queue?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Producer-Consumer?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Pub-Sub?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Priority Queue?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Dead Letter Queue?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

### 2. Trade-offs

**Simple Queue:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Producer-Consumer:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Pub-Sub:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Priority Queue:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

### 3. Your Decision Tree

Build your decision tree after practicing:

```
What is your communication pattern?
├─ One-to-one async processing → ?
├─ Multiple workers needed → ?
├─ Broadcast to multiple consumers → ?
├─ Urgent messages need priority → ?
└─ Need retry and failure handling → ?
```

### 4. Kill Switch - Don't use when:

**Message Queues:**
1. _[When do message queues fail? Fill in]_
2. _[Another failure case]_

**Pub-Sub:**
1. _[When does pub-sub fail? Fill in]_
2. _[Another failure case]_

**Priority Queue:**
1. _[When does priority queue fail? Fill in]_
2. _[Another failure case]_

### 5. Rule of Three - Alternatives

For each scenario, identify alternatives and compare:

**Scenario: Async task processing**
1. Option A: _[Fill in]_
2. Option B: _[Fill in]_
3. Option C: _[Fill in]_

---

## Practice

### Scenario 1: Process uploaded images

**Requirements:**
- Users upload images
- Need to resize, compress, generate thumbnails
- Processing takes 5-10 seconds
- Want fast upload response
- Handle processing failures

**Your design:**
- Which pattern would you choose? _[Fill in]_
- Why? _[Fill in]_
- How many workers? _[Fill in]_
- Failure handling strategy? _[Fill in]_

### Scenario 2: Notification system

**Requirements:**
- Send notifications via email, SMS, push
- Users subscribe to notification types
- Some notifications are urgent
- Must deliver to all channels
- Track delivery failures

**Your design:**
- Which pattern would you choose? _[Fill in]_
- Why? _[Fill in]_
- How to handle different channels? _[Fill in]_
- Priority strategy? _[Fill in]_

### Scenario 3: Order processing system

**Requirements:**
- Process orders from multiple sources
- Some orders need priority (VIP customers)
- Payment processing might fail
- Need retry logic
- Monitor failed orders

**Your design:**
- Which pattern would you choose? _[Fill in]_
- Why? _[Fill in]_
- How to handle priorities? _[Fill in]_
- Retry strategy? _[Fill in]_

---

## Review Checklist

- [ ] Simple message queue implemented with blocking operations
- [ ] Producer-consumer implemented with multiple workers
- [ ] Pub-sub implemented with topic-based routing
- [ ] Priority queue implemented with priority ordering
- [ ] Dead letter queue implemented with retry logic
- [ ] Understand when to use each pattern
- [ ] Can explain trade-offs between patterns
- [ ] Built decision tree for pattern selection
- [ ] Completed practice scenarios

---

**Next:** [11. Stream Processing →](11-stream-processing.md)

**Back:** [09. Database Scaling ←](09-database-scaling.md)

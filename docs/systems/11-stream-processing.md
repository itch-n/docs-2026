# 11. Stream Processing

> Real-time data processing with windowing, watermarks, and stateful operations

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing stream processing patterns, explain them simply.

**Prompts to guide you:**

1. **What is stream processing in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **What is a window in stream processing?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy for tumbling window:**
   - Example: "A tumbling window is like counting cars that pass every 5 minutes..."
   - Your analogy: _[Fill in]_

4. **What are watermarks in one sentence?**
   - Your answer: _[Fill in after implementation]_

5. **What is the difference between event time and processing time?**
   - Your answer: _[Fill in after implementation]_

6. **Real-world analogy for late data handling:**
   - Example: "Late data is like receiving a postcard that was sent last week..."
   - Your analogy: _[Fill in]_

---

## Core Implementation

### Pattern 1: Windowing (Tumbling, Sliding, Session)

**Concept:** Group streaming data into finite chunks for aggregation.

**Use case:** Real-time analytics, metrics aggregation, event counting.

```java
import java.util.*;

/**
 * Stream Windowing: Group events into time-based windows
 *
 * Window Types:
 * - Tumbling: Fixed-size, non-overlapping (e.g., every 5 minutes)
 * - Sliding: Fixed-size, overlapping (e.g., last 5 minutes, updated every 1 minute)
 * - Session: Dynamic size based on inactivity gaps
 */
public class StreamWindow<K, V> {

    static class Event<K, V> {
        K key;
        V value;
        long timestamp; // Event time

        Event(K key, V value, long timestamp) {
            this.key = key;
            this.value = value;
            this.timestamp = timestamp;
        }
    }

    static class WindowResult<K> {
        K key;
        long windowStart;
        long windowEnd;
        long count;

        WindowResult(K key, long windowStart, long windowEnd, long count) {
            this.key = key;
            this.windowStart = windowStart;
            this.windowEnd = windowEnd;
            this.count = count;
        }

        @Override
        public String toString() {
            return String.format("Window[%d-%d] key=%s count=%d",
                windowStart, windowEnd, key, count);
        }
    }

    /**
     * Tumbling Window: Fixed, non-overlapping time buckets
     * Time: O(1) per event, Space: O(W*K) where W=windows, K=keys
     *
     * TODO: Implement tumbling window
     * 1. Determine which window the event belongs to
     * 2. windowStart = (timestamp / windowSize) * windowSize
     * 3. Aggregate events in the same window
     * 4. Emit results when window closes
     */
    public static <K, V> Map<Long, Map<K, Long>> tumblingWindow(
            List<Event<K, V>> events,
            long windowSizeMs) {

        Map<Long, Map<K, Long>> windows = new TreeMap<>();

        // TODO: Process each event
        //   for (Event<K, V> event : events) {
        //     // Calculate window start
        //     long windowStart = (event.timestamp / windowSizeMs) * windowSizeMs;
        //
        //     // Get or create window
        //     Map<K, Long> window = windows.computeIfAbsent(windowStart, k -> new HashMap<>());
        //
        //     // Aggregate (count in this case)
        //     window.merge(event.key, 1L, Long::sum);
        //   }

        return windows; // Replace
    }

    /**
     * Sliding Window: Overlapping windows
     * Time: O(N) per event where N=num overlapping windows, Space: O(W*K)
     *
     * TODO: Implement sliding window
     * 1. For each event, determine ALL windows it belongs to
     * 2. An event at time T belongs to windows:
     *    [T-windowSize+slide, T-windowSize+2*slide, ..., T]
     * 3. Update all overlapping windows
     */
    public static <K, V> Map<Long, Map<K, Long>> slidingWindow(
            List<Event<K, V>> events,
            long windowSizeMs,
            long slideMs) {

        Map<Long, Map<K, Long>> windows = new TreeMap<>();

        // TODO: Process each event
        //   for (Event<K, V> event : events) {
        //     // Calculate all window starts this event belongs to
        //     long firstWindowStart = ((event.timestamp - windowSizeMs + slideMs) / slideMs) * slideMs;
        //     long lastWindowStart = (event.timestamp / slideMs) * slideMs;
        //
        //     // Add event to all overlapping windows
        //     for (long windowStart = firstWindowStart;
        //          windowStart <= lastWindowStart;
        //          windowStart += slideMs) {
        //       Map<K, Long> window = windows.computeIfAbsent(windowStart, k -> new HashMap<>());
        //       window.merge(event.key, 1L, Long::sum);
        //     }
        //   }

        return windows; // Replace
    }

    /**
     * Session Window: Group events with inactivity gap
     * Time: O(log N) per event, Space: O(S*K) where S=sessions
     *
     * TODO: Implement session window
     * 1. Sort events by key and timestamp
     * 2. For each key, group events within gap threshold
     * 3. Start new session if gap > threshold
     * 4. Merge sessions if events arrive late
     */
    public static <K, V> List<WindowResult<K>> sessionWindow(
            List<Event<K, V>> events,
            long gapMs) {

        List<WindowResult<K>> results = new ArrayList<>();

        // TODO: Group events by key
        //   Map<K, List<Event<K, V>>> eventsByKey = new HashMap<>();
        //   for (Event<K, V> event : events) {
        //     eventsByKey.computeIfAbsent(event.key, k -> new ArrayList<>())
        //                .add(event);
        //   }

        // TODO: For each key, create sessions based on gap
        //   for (Map.Entry<K, List<Event<K, V>>> entry : eventsByKey.entrySet()) {
        //     K key = entry.getKey();
        //     List<Event<K, V>> keyEvents = entry.getValue();
        //
        //     // Sort by timestamp
        //     keyEvents.sort(Comparator.comparingLong(e -> e.timestamp));
        //
        //     // Create sessions
        //     long sessionStart = keyEvents.get(0).timestamp;
        //     long lastTimestamp = sessionStart;
        //     long count = 0;
        //
        //     for (Event<K, V> event : keyEvents) {
        //       if (event.timestamp - lastTimestamp > gapMs) {
        //         // Close current session
        //         results.add(new WindowResult<>(key, sessionStart, lastTimestamp, count));
        //
        //         // Start new session
        //         sessionStart = event.timestamp;
        //         count = 0;
        //       }
        //
        //       lastTimestamp = event.timestamp;
        //       count++;
        //     }
        //
        //     // Close final session
        //     results.add(new WindowResult<>(key, sessionStart, lastTimestamp, count));
        //   }

        return results; // Replace
    }

    /**
     * Helper: Print window results
     */
    public static <K> void printWindows(Map<Long, Map<K, Long>> windows) {
        for (Map.Entry<Long, Map<K, Long>> entry : windows.entrySet()) {
            long windowStart = entry.getKey();
            System.out.println("Window [" + windowStart + "]:");
            for (Map.Entry<K, Long> keyCount : entry.getValue().entrySet()) {
                System.out.println("  " + keyCount.getKey() + ": " + keyCount.getValue());
            }
        }
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class StreamWindowClient {

    public static void main(String[] args) {
        System.out.println("=== Stream Windowing ===\n");

        // Create sample events (userId, action, timestamp)
        List<StreamWindow.Event<String, String>> events = Arrays.asList(
            new StreamWindow.Event<>("user1", "click", 1000L),
            new StreamWindow.Event<>("user2", "click", 2000L),
            new StreamWindow.Event<>("user1", "click", 3000L),
            new StreamWindow.Event<>("user1", "click", 6000L),
            new StreamWindow.Event<>("user2", "click", 7000L),
            new StreamWindow.Event<>("user1", "click", 11000L),
            new StreamWindow.Event<>("user2", "click", 12000L),
            new StreamWindow.Event<>("user1", "click", 15000L),
            new StreamWindow.Event<>("user2", "click", 16000L)
        );

        // Test 1: Tumbling Window (5 second windows)
        System.out.println("--- Test 1: Tumbling Window (5s) ---");
        Map<Long, Map<String, Long>> tumbling =
            StreamWindow.tumblingWindow(events, 5000L);
        StreamWindow.printWindows(tumbling);

        // Test 2: Sliding Window (5s window, 2s slide)
        System.out.println("\n--- Test 2: Sliding Window (5s window, 2s slide) ---");
        Map<Long, Map<String, Long>> sliding =
            StreamWindow.slidingWindow(events, 5000L, 2000L);
        StreamWindow.printWindows(sliding);

        // Test 3: Session Window (3s gap)
        System.out.println("\n--- Test 3: Session Window (3s gap) ---");
        List<StreamWindow.WindowResult<String>> sessions =
            StreamWindow.sessionWindow(events, 3000L);
        for (StreamWindow.WindowResult<String> result : sessions) {
            System.out.println(result);
        }

        // Test 4: Different gap threshold
        System.out.println("\n--- Test 4: Session Window (5s gap) ---");
        List<StreamWindow.WindowResult<String>> sessions2 =
            StreamWindow.sessionWindow(events, 5000L);
        for (StreamWindow.WindowResult<String> result : sessions2) {
            System.out.println(result);
        }
    }
}
```

---

### Pattern 2: Watermarks and Late Data

**Concept:** Handle out-of-order events and determine when to close windows.

**Use case:** Distributed systems, network delays, mobile data sync.

```java
import java.util.*;

/**
 * Watermarks: Track event time progress in the stream
 *
 * Watermark Properties:
 * - Monotonically increasing timestamp
 * - Indicates "all events before this time have been seen"
 * - Allows system to close windows and emit results
 * - Late data: events arriving after watermark
 */
public class WatermarkProcessor<K, V> {

    static class Event<K, V> {
        K key;
        V value;
        long eventTime;      // When event actually occurred
        long processingTime; // When event was processed

        Event(K key, V value, long eventTime, long processingTime) {
            this.key = key;
            this.value = value;
            this.eventTime = eventTime;
            this.processingTime = processingTime;
        }
    }

    static class WindowState<K> {
        K key;
        long windowStart;
        long windowEnd;
        long count;
        boolean closed;

        WindowState(K key, long windowStart, long windowEnd) {
            this.key = key;
            this.windowStart = windowStart;
            this.windowEnd = windowEnd;
            this.count = 0;
            this.closed = false;
        }
    }

    private final long windowSize;
    private final long allowedLateness;
    private long currentWatermark;

    // Active windows: windowStart -> key -> state
    private Map<Long, Map<K, WindowState<K>>> windows;

    // Late data count
    private long lateEventCount;

    public WatermarkProcessor(long windowSize, long allowedLateness) {
        this.windowSize = windowSize;
        this.allowedLateness = allowedLateness;
        this.currentWatermark = 0;
        this.windows = new TreeMap<>();
        this.lateEventCount = 0;
    }

    /**
     * Process event with watermark tracking
     * Time: O(log W) where W=windows, Space: O(W*K)
     *
     * TODO: Implement event processing with watermarks
     * 1. Update watermark based on event time
     * 2. Assign event to window
     * 3. Check if event is late (eventTime < watermark - allowedLateness)
     * 4. Close windows when watermark passes windowEnd + allowedLateness
     */
    public void processEvent(Event<K, V> event) {
        // TODO: Update watermark (typically: eventTime - maxDelay)
        //   // Simple watermark: use event time directly
        //   if (event.eventTime > currentWatermark) {
        //     currentWatermark = event.eventTime;
        //   }

        // TODO: Calculate window for this event
        //   long windowStart = (event.eventTime / windowSize) * windowSize;
        //   long windowEnd = windowStart + windowSize;

        // TODO: Check if event is too late
        //   if (event.eventTime < currentWatermark - allowedLateness) {
        //     System.out.println("LATE DATA: " + event.key + " at " + event.eventTime);
        //     lateEventCount++;
        //     return; // Drop or send to dead letter queue
        //   }

        // TODO: Get or create window state
        //   Map<K, WindowState<K>> window = windows.computeIfAbsent(windowStart, k -> new HashMap<>());
        //   WindowState<K> state = window.computeIfAbsent(event.key,
        //     k -> new WindowState<>(event.key, windowStart, windowEnd));

        // TODO: Update state if window not closed
        //   if (!state.closed) {
        //     state.count++;
        //   }

        // TODO: Close windows that are ready
        //   closeCompletedWindows();
    }

    /**
     * Close windows that have passed watermark + allowedLateness
     * Time: O(W*K), Space: O(1)
     *
     * TODO: Implement window closing logic
     */
    private void closeCompletedWindows() {
        List<Long> toRemove = new ArrayList<>();

        // TODO: Check each window
        //   for (Map.Entry<Long, Map<K, WindowState<K>>> entry : windows.entrySet()) {
        //     long windowStart = entry.getKey();
        //     long windowEnd = windowStart + windowSize;
        //
        //     // Close if watermark passed windowEnd + allowedLateness
        //     if (currentWatermark >= windowEnd + allowedLateness) {
        //       Map<K, WindowState<K>> window = entry.getValue();
        //
        //       // Emit results for each key in window
        //       for (WindowState<K> state : window.values()) {
        //         if (!state.closed) {
        //           emitResult(state);
        //           state.closed = true;
        //         }
        //       }
        //
        //       toRemove.add(windowStart);
        //     }
        //   }

        // TODO: Remove closed windows
        //   for (Long windowStart : toRemove) {
        //     windows.remove(windowStart);
        //   }
    }

    /**
     * Emit window result (in production: send to output stream)
     */
    private void emitResult(WindowState<K> state) {
        System.out.printf("EMIT: Window[%d-%d] key=%s count=%d watermark=%d%n",
            state.windowStart, state.windowEnd, state.key, state.count, currentWatermark);
    }

    /**
     * Generate periodic watermark (for idle streams)
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement periodic watermark generation
     */
    public void generatePeriodicWatermark(long timestamp) {
        // TODO: Advance watermark
        //   if (timestamp > currentWatermark) {
        //     currentWatermark = timestamp;
        //     closeCompletedWindows();
        //   }
    }

    /**
     * Get current watermark
     */
    public long getWatermark() {
        return currentWatermark;
    }

    /**
     * Get late event count
     */
    public long getLateEventCount() {
        return lateEventCount;
    }

    /**
     * Get active window count
     */
    public int getActiveWindowCount() {
        return windows.size();
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class WatermarkClient {

    public static void main(String[] args) {
        System.out.println("=== Watermarks and Late Data ===\n");

        WatermarkProcessor<String, String> processor =
            new WatermarkProcessor<>(5000L, 2000L); // 5s window, 2s late

        // Test 1: In-order events
        System.out.println("--- Test 1: In-Order Events ---");
        List<WatermarkProcessor.Event<String, String>> events1 = Arrays.asList(
            new WatermarkProcessor.Event<>("user1", "click", 1000L, 1000L),
            new WatermarkProcessor.Event<>("user1", "click", 2000L, 2000L),
            new WatermarkProcessor.Event<>("user2", "click", 3000L, 3000L)
        );

        for (WatermarkProcessor.Event<String, String> event : events1) {
            processor.processEvent(event);
        }
        System.out.println("Watermark: " + processor.getWatermark());

        // Test 2: Advance watermark to close window
        System.out.println("\n--- Test 2: Close Window ---");
        processor.generatePeriodicWatermark(8000L);
        System.out.println("Active windows: " + processor.getActiveWindowCount());

        // Test 3: Out-of-order events (within allowed lateness)
        System.out.println("\n--- Test 3: Out-of-Order (Within Lateness) ---");
        WatermarkProcessor.Event<String, String> lateEvent =
            new WatermarkProcessor.Event<>("user2", "click", 4000L, 9000L);
        processor.processEvent(lateEvent);

        // Test 4: Very late event (outside allowed lateness)
        System.out.println("\n--- Test 4: Very Late Event (Dropped) ---");
        WatermarkProcessor.Event<String, String> veryLateEvent =
            new WatermarkProcessor.Event<>("user1", "click", 500L, 10000L);
        processor.processEvent(veryLateEvent);
        System.out.println("Late event count: " + processor.getLateEventCount());

        // Test 5: Multiple windows with different keys
        System.out.println("\n--- Test 5: Multiple Windows ---");
        WatermarkProcessor<String, String> processor2 =
            new WatermarkProcessor<>(5000L, 1000L);

        List<WatermarkProcessor.Event<String, String>> events2 = Arrays.asList(
            new WatermarkProcessor.Event<>("A", "x", 1000L, 1000L),
            new WatermarkProcessor.Event<>("B", "y", 2000L, 2000L),
            new WatermarkProcessor.Event<>("A", "x", 3000L, 3000L),
            new WatermarkProcessor.Event<>("A", "x", 7000L, 7000L),
            new WatermarkProcessor.Event<>("B", "y", 8000L, 8000L)
        );

        for (WatermarkProcessor.Event<String, String> event : events2) {
            processor2.processEvent(event);
        }

        // Close all windows
        processor2.generatePeriodicWatermark(15000L);
    }
}
```

---

### Pattern 3: Stateful Stream Processing

**Concept:** Maintain state across events for aggregations, joins, and enrichment.

**Use case:** Running totals, user sessions, stream joins, enrichment.

```java
import java.util.*;

/**
 * Stateful Stream Processing
 *
 * State Types:
 * - Value State: Single value per key
 * - List State: List of values per key
 * - Map State: Nested key-value map per key
 *
 * State Backends:
 * - In-memory (fast, not fault-tolerant)
 * - RocksDB (persistent, fault-tolerant)
 */
public class StatefulProcessor<K, V> {

    static class Event<K, V> {
        K key;
        V value;
        long timestamp;

        Event(K key, V value, long timestamp) {
            this.key = key;
            this.value = value;
            this.timestamp = timestamp;
        }
    }

    static class StateDescriptor<S> {
        String name;
        Class<S> stateType;
        long ttlMs; // Time-to-live for state cleanup

        StateDescriptor(String name, Class<S> stateType, long ttlMs) {
            this.name = name;
            this.stateType = stateType;
            this.ttlMs = ttlMs;
        }
    }

    /**
     * Value State: Single value per key
     * Time: O(1), Space: O(K)
     */
    static class ValueState<K, S> {
        private Map<K, S> state;
        private Map<K, Long> lastAccess; // For TTL
        private long ttlMs;

        ValueState(long ttlMs) {
            this.state = new HashMap<>();
            this.lastAccess = new HashMap<>();
            this.ttlMs = ttlMs;
        }

        /**
         * Get state for key
         *
         * TODO: Implement get with TTL check
         * 1. Check if key exists
         * 2. Check if state expired (current time - lastAccess > ttl)
         * 3. If expired, remove and return null
         * 4. Otherwise return value
         */
        public S get(K key, long currentTime) {
            // TODO: Check expiration
            //   if (state.containsKey(key)) {
            //     Long lastTime = lastAccess.get(key);
            //     if (currentTime - lastTime > ttlMs) {
            //       // State expired
            //       state.remove(key);
            //       lastAccess.remove(key);
            //       return null;
            //     }
            //     return state.get(key);
            //   }
            return null; // Replace
        }

        /**
         * Update state for key
         *
         * TODO: Implement update with TTL tracking
         */
        public void update(K key, S value, long currentTime) {
            // TODO: Update state and last access time
            //   state.put(key, value);
            //   lastAccess.put(key, currentTime);
        }

        /**
         * Clear state for key
         */
        public void clear(K key) {
            state.remove(key);
            lastAccess.remove(key);
        }
    }

    /**
     * List State: Append-only list per key
     * Time: O(1) append, O(N) iterate, Space: O(K*N)
     */
    static class ListState<K, S> {
        private Map<K, List<S>> state;
        private Map<K, Long> lastAccess;
        private long ttlMs;

        ListState(long ttlMs) {
            this.state = new HashMap<>();
            this.lastAccess = new HashMap<>();
            this.ttlMs = ttlMs;
        }

        /**
         * Append value to list
         *
         * TODO: Implement append
         */
        public void append(K key, S value, long currentTime) {
            // TODO: Get or create list and append
            //   state.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            //   lastAccess.put(key, currentTime);
        }

        /**
         * Get all values for key
         *
         * TODO: Implement get with TTL check
         */
        public List<S> get(K key, long currentTime) {
            // TODO: Check expiration similar to ValueState
            return new ArrayList<>(); // Replace
        }
    }

    /**
     * Example: Running sum aggregation
     * Time: O(1) per event, Space: O(K)
     *
     * TODO: Implement stateful aggregation
     */
    public static class RunningSumProcessor {
        private ValueState<String, Long> sumState;

        public RunningSumProcessor(long ttlMs) {
            this.sumState = new ValueState<>(ttlMs);
        }

        /**
         * Process event and update running sum
         *
         * TODO: Implement running sum
         * 1. Get current sum for key
         * 2. Add new value
         * 3. Update state
         * 4. Return new sum
         */
        public long process(Event<String, Long> event) {
            // TODO: Get current sum
            //   Long currentSum = sumState.get(event.key, event.timestamp);
            //   if (currentSum == null) {
            //     currentSum = 0L;
            //   }

            // TODO: Add new value
            //   long newSum = currentSum + event.value;

            // TODO: Update state
            //   sumState.update(event.key, newSum, event.timestamp);

            // TODO: Return result
            //   return newSum;

            return 0L; // Replace
        }

        public Long getCurrentSum(String key, long timestamp) {
            return sumState.get(key, timestamp);
        }
    }

    /**
     * Example: Stream-Stream Join
     * Time: O(1) per event, Space: O(K*W) where W=window size
     *
     * TODO: Implement stream join
     */
    public static class StreamJoinProcessor {
        private ListState<String, Event<String, String>> leftState;
        private ListState<String, Event<String, String>> rightState;
        private long joinWindowMs;

        public StreamJoinProcessor(long joinWindowMs, long stateTtl) {
            this.leftState = new ListState<>(stateTtl);
            this.rightState = new ListState<>(stateTtl);
            this.joinWindowMs = joinWindowMs;
        }

        /**
         * Process left stream event
         *
         * TODO: Implement left stream processing
         * 1. Store event in left state
         * 2. Look for matching events in right state within join window
         * 3. Emit joined results
         */
        public List<String> processLeft(Event<String, String> event) {
            List<String> results = new ArrayList<>();

            // TODO: Store in left state
            //   leftState.append(event.key, event, event.timestamp);

            // TODO: Find matches in right state
            //   List<Event<String, String>> rightEvents = rightState.get(event.key, event.timestamp);
            //   if (rightEvents != null) {
            //     for (Event<String, String> right : rightEvents) {
            //       // Check if within join window
            //       if (Math.abs(event.timestamp - right.timestamp) <= joinWindowMs) {
            //         results.add(event.value + "+" + right.value);
            //       }
            //     }
            //   }

            return results; // Replace
        }

        /**
         * Process right stream event
         *
         * TODO: Implement right stream processing (symmetric to left)
         */
        public List<String> processRight(Event<String, String> event) {
            List<String> results = new ArrayList<>();

            // TODO: Similar to processLeft but reversed

            return results; // Replace
        }
    }

    /**
     * State cleanup: Remove expired state
     * Time: O(K), Space: O(1)
     *
     * TODO: Implement periodic state cleanup
     */
    public static void cleanupExpiredState(ValueState<?, ?> state, long currentTime) {
        // TODO: Iterate through all keys and remove expired entries
        // In production: RocksDB handles this with compaction
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class StatefulProcessorClient {

    public static void main(String[] args) {
        System.out.println("=== Stateful Stream Processing ===\n");

        // Test 1: Running sum
        System.out.println("--- Test 1: Running Sum ---");
        StatefulProcessor.RunningSumProcessor sumProcessor =
            new StatefulProcessor.RunningSumProcessor(10000L);

        List<StatefulProcessor.Event<String, Long>> sumEvents = Arrays.asList(
            new StatefulProcessor.Event<>("user1", 10L, 1000L),
            new StatefulProcessor.Event<>("user1", 20L, 2000L),
            new StatefulProcessor.Event<>("user2", 5L, 2500L),
            new StatefulProcessor.Event<>("user1", 15L, 3000L),
            new StatefulProcessor.Event<>("user2", 10L, 3500L)
        );

        for (StatefulProcessor.Event<String, Long> event : sumEvents) {
            long sum = sumProcessor.process(event);
            System.out.printf("Key=%s Value=%d RunningSum=%d%n",
                event.key, event.value, sum);
        }

        // Test 2: Stream join
        System.out.println("\n--- Test 2: Stream Join ---");
        StatefulProcessor.StreamJoinProcessor joinProcessor =
            new StatefulProcessor.StreamJoinProcessor(2000L, 10000L);

        // Left stream events
        System.out.println("Processing left stream:");
        StatefulProcessor.Event<String, String> left1 =
            new StatefulProcessor.Event<>("order1", "LeftA", 1000L);
        List<String> joined1 = joinProcessor.processLeft(left1);
        System.out.println("  " + left1.key + ": " + joined1);

        // Right stream events
        System.out.println("Processing right stream:");
        StatefulProcessor.Event<String, String> right1 =
            new StatefulProcessor.Event<>("order1", "RightX", 1500L);
        List<String> joined2 = joinProcessor.processRight(right1);
        System.out.println("  " + right1.key + ": " + joined2);

        // More events
        StatefulProcessor.Event<String, String> left2 =
            new StatefulProcessor.Event<>("order2", "LeftB", 2000L);
        List<String> joined3 = joinProcessor.processLeft(left2);
        System.out.println("  " + left2.key + ": " + joined3);

        // Test 3: State TTL
        System.out.println("\n--- Test 3: State TTL ---");
        StatefulProcessor.ValueState<String, String> ttlState =
            new StatefulProcessor.ValueState<>(2000L); // 2s TTL

        ttlState.update("key1", "value1", 1000L);
        System.out.println("Stored at t=1000");

        String val1 = ttlState.get("key1", 2000L);
        System.out.println("Get at t=2000: " + val1); // Should exist

        String val2 = ttlState.get("key1", 4000L);
        System.out.println("Get at t=4000: " + val2); // Should be expired

        // Test 4: List state
        System.out.println("\n--- Test 4: List State ---");
        StatefulProcessor.ListState<String, String> listState =
            new StatefulProcessor.ListState<>(10000L);

        listState.append("user1", "event1", 1000L);
        listState.append("user1", "event2", 2000L);
        listState.append("user1", "event3", 3000L);

        List<String> events = listState.get("user1", 3500L);
        System.out.println("Events for user1: " + events);
    }
}
```

---

### Pattern 4: Exactly-Once Semantics

**Concept:** Ensure each event is processed exactly once, even with failures.

**Use case:** Financial transactions, billing, critical business logic.

```java
import java.util.*;

/**
 * Exactly-Once Processing
 *
 * Techniques:
 * - Idempotent operations (safe to retry)
 * - Two-phase commit for sinks
 * - Transaction markers in stream
 * - Deduplication with state
 */
public class ExactlyOnceProcessor<K, V> {

    static class Event<K, V> {
        String eventId; // Unique ID for deduplication
        K key;
        V value;
        long timestamp;

        Event(String eventId, K key, V value, long timestamp) {
            this.eventId = eventId;
            this.key = key;
            this.value = value;
            this.timestamp = timestamp;
        }
    }

    static class Transaction {
        String transactionId;
        long timestamp;
        List<String> processedEventIds;
        boolean committed;

        Transaction(String transactionId, long timestamp) {
            this.transactionId = transactionId;
            this.timestamp = timestamp;
            this.processedEventIds = new ArrayList<>();
            this.committed = false;
        }
    }

    /**
     * Deduplication State: Track processed events
     * Time: O(1) per check, Space: O(N) where N=events in window
     */
    static class DeduplicationState {
        private Set<String> processedEventIds;
        private long oldestEventTime;
        private long retentionMs;

        DeduplicationState(long retentionMs) {
            this.processedEventIds = new HashSet<>();
            this.retentionMs = retentionMs;
            this.oldestEventTime = Long.MAX_VALUE;
        }

        /**
         * Check if event already processed
         *
         * TODO: Implement deduplication check
         * 1. Check if eventId exists in set
         * 2. If yes, return true (duplicate)
         * 3. If no, add to set and return false
         */
        public boolean isDuplicate(String eventId) {
            // TODO: Check and add
            //   if (processedEventIds.contains(eventId)) {
            //     return true; // Duplicate
            //   }
            //   processedEventIds.add(eventId);
            //   return false; // First time seeing this

            return false; // Replace
        }

        /**
         * Cleanup old event IDs
         *
         * TODO: Implement periodic cleanup
         * In production: use time-based eviction or Bloom filter
         */
        public void cleanup(long currentTime) {
            // TODO: Remove event IDs older than retention period
            // Simplified: in reality need timestamps per event
            if (currentTime - oldestEventTime > retentionMs) {
                processedEventIds.clear();
                oldestEventTime = currentTime;
            }
        }

        public int size() {
            return processedEventIds.size();
        }
    }

    /**
     * Idempotent Aggregator: Safe to process same event multiple times
     * Time: O(1) per event, Space: O(K)
     *
     * TODO: Implement idempotent aggregation
     */
    static class IdempotentAggregator<K> {
        // Store: key -> (value, eventId)
        private Map<K, Long> values;
        private Map<K, String> lastEventIds;

        IdempotentAggregator() {
            this.values = new HashMap<>();
            this.lastEventIds = new HashMap<>();
        }

        /**
         * Process event idempotently
         *
         * TODO: Implement idempotent update
         * 1. Check if this exact event was already processed
         * 2. If same eventId, skip (idempotent)
         * 3. If new eventId, update value
         */
        public void process(Event<K, Long> event) {
            // TODO: Check if already processed
            //   String lastEventId = lastEventIds.get(event.key);
            //   if (event.eventId.equals(lastEventId)) {
            //     // Already processed this exact event
            //     return;
            //   }

            // TODO: Update value (idempotent SET operation)
            //   values.put(event.key, event.value);
            //   lastEventIds.put(event.key, event.eventId);
        }

        public Long getValue(K key) {
            return values.get(key);
        }
    }

    /**
     * Two-Phase Commit Sink: Transactional output
     * Time: O(1) per event, O(N) per commit
     *
     * TODO: Implement 2PC for sink
     */
    static class TransactionalSink<T> {
        private Transaction currentTransaction;
        private List<T> pendingWrites;
        private Set<String> committedTransactions;

        TransactionalSink() {
            this.pendingWrites = new ArrayList<>();
            this.committedTransactions = new HashSet<>();
        }

        /**
         * Begin new transaction
         *
         * TODO: Implement transaction begin
         */
        public void beginTransaction(String txnId) {
            // TODO: Create new transaction
            //   currentTransaction = new Transaction(txnId, System.currentTimeMillis());
            //   pendingWrites.clear();
        }

        /**
         * Write to transaction (not committed yet)
         *
         * TODO: Implement transactional write
         */
        public void write(T value, String eventId) {
            // TODO: Add to pending writes
            //   if (currentTransaction == null) {
            //     throw new IllegalStateException("No active transaction");
            //   }
            //   pendingWrites.add(value);
            //   currentTransaction.processedEventIds.add(eventId);
        }

        /**
         * Commit transaction (make writes visible)
         *
         * TODO: Implement commit
         * 1. Check if transaction already committed (idempotent)
         * 2. Flush all pending writes
         * 3. Mark transaction as committed
         */
        public void commit() {
            // TODO: Commit if not already done
            //   if (currentTransaction == null) {
            //     throw new IllegalStateException("No active transaction");
            //   }
            //
            //   if (committedTransactions.contains(currentTransaction.transactionId)) {
            //     // Already committed (idempotent)
            //     return;
            //   }
            //
            //   // Flush writes (in production: write to external system)
            //   System.out.println("COMMIT: " + pendingWrites.size() + " writes");
            //
            //   // Mark as committed
            //   committedTransactions.add(currentTransaction.transactionId);
            //   currentTransaction.committed = true;
        }

        /**
         * Abort transaction (discard writes)
         *
         * TODO: Implement abort
         */
        public void abort() {
            // TODO: Clear pending writes
            //   pendingWrites.clear();
            //   currentTransaction = null;
        }

        public int getPendingCount() {
            return pendingWrites.size();
        }
    }

    /**
     * Checkpoint coordinator: Manage checkpoints for fault tolerance
     * Time: O(S) where S=state size, Space: O(S)
     *
     * TODO: Implement checkpointing
     */
    static class CheckpointCoordinator {
        private long lastCheckpointId;
        private Map<Long, Map<String, Object>> checkpoints;

        CheckpointCoordinator() {
            this.lastCheckpointId = 0;
            this.checkpoints = new HashMap<>();
        }

        /**
         * Trigger checkpoint
         *
         * TODO: Implement checkpoint trigger
         * 1. Generate checkpoint ID
         * 2. Snapshot all state
         * 3. Store checkpoint
         * 4. Return checkpoint ID
         */
        public long triggerCheckpoint(Map<String, Object> state) {
            // TODO: Create checkpoint
            //   long checkpointId = ++lastCheckpointId;
            //   Map<String, Object> snapshot = new HashMap<>(state);
            //   checkpoints.put(checkpointId, snapshot);
            //   System.out.println("Checkpoint " + checkpointId + " completed");
            //   return checkpointId;

            return 0; // Replace
        }

        /**
         * Restore from checkpoint
         *
         * TODO: Implement restore
         */
        public Map<String, Object> restore(long checkpointId) {
            // TODO: Restore state
            //   return checkpoints.get(checkpointId);
            return null; // Replace
        }
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class ExactlyOnceClient {

    public static void main(String[] args) {
        System.out.println("=== Exactly-Once Processing ===\n");

        // Test 1: Deduplication
        System.out.println("--- Test 1: Deduplication ---");
        ExactlyOnceProcessor.DeduplicationState dedup =
            new ExactlyOnceProcessor.DeduplicationState(10000L);

        String[] eventIds = {"evt1", "evt2", "evt1", "evt3", "evt2"};
        for (String id : eventIds) {
            boolean isDup = dedup.isDuplicate(id);
            System.out.println("Event " + id + ": " +
                (isDup ? "DUPLICATE" : "NEW"));
        }
        System.out.println("Unique events tracked: " + dedup.size());

        // Test 2: Idempotent aggregation
        System.out.println("\n--- Test 2: Idempotent Operations ---");
        ExactlyOnceProcessor.IdempotentAggregator<String> aggregator =
            new ExactlyOnceProcessor.IdempotentAggregator<>();

        List<ExactlyOnceProcessor.Event<String, Long>> events = Arrays.asList(
            new ExactlyOnceProcessor.Event<>("e1", "user1", 100L, 1000L),
            new ExactlyOnceProcessor.Event<>("e2", "user1", 200L, 2000L),
            new ExactlyOnceProcessor.Event<>("e1", "user1", 100L, 2500L), // Duplicate
            new ExactlyOnceProcessor.Event<>("e3", "user2", 50L, 3000L)
        );

        for (ExactlyOnceProcessor.Event<String, Long> event : events) {
            System.out.println("Processing: " + event.eventId);
            aggregator.process(event);
            System.out.println("  user1: " + aggregator.getValue("user1"));
            System.out.println("  user2: " + aggregator.getValue("user2"));
        }

        // Test 3: Transactional sink
        System.out.println("\n--- Test 3: Transactional Sink ---");
        ExactlyOnceProcessor.TransactionalSink<String> sink =
            new ExactlyOnceProcessor.TransactionalSink<>();

        // Transaction 1
        sink.beginTransaction("txn1");
        sink.write("value1", "e1");
        sink.write("value2", "e2");
        System.out.println("Pending writes: " + sink.getPendingCount());
        sink.commit();

        // Transaction 2 (with abort)
        sink.beginTransaction("txn2");
        sink.write("value3", "e3");
        System.out.println("Pending writes: " + sink.getPendingCount());
        sink.abort();
        System.out.println("After abort: " + sink.getPendingCount());

        // Test 4: Checkpointing
        System.out.println("\n--- Test 4: Checkpointing ---");
        ExactlyOnceProcessor.CheckpointCoordinator coordinator =
            new ExactlyOnceProcessor.CheckpointCoordinator();

        Map<String, Object> state1 = new HashMap<>();
        state1.put("counter", 100);
        state1.put("sum", 500L);

        long cp1 = coordinator.triggerCheckpoint(state1);
        System.out.println("Created checkpoint: " + cp1);

        // Modify state
        state1.put("counter", 200);
        long cp2 = coordinator.triggerCheckpoint(state1);
        System.out.println("Created checkpoint: " + cp2);

        // Restore
        Map<String, Object> restored = coordinator.restore(cp1);
        System.out.println("Restored state: " + restored);
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for when to use each stream processing pattern.

### Question 1: What type of windowing do you need?

Answer after implementation:

**Use Tumbling Window when:**
- Fixed time boundaries: _[Every hour, every day]_
- Non-overlapping: _[Each event in exactly one window]_
- Simple aggregation: _[Count, sum per time period]_
- Example: _[Hourly sales reports, daily active users]_

**Use Sliding Window when:**
- Moving average: _[Last N minutes]_
- Overlapping periods: _[Need smooth transitions]_
- Real-time dashboards: _[Updated frequently]_
- Example: _[5-minute average updated every 30 seconds]_

**Use Session Window when:**
- User activity: _[Group by engagement sessions]_
- Variable length: _[Based on inactivity]_
- Burst detection: _[Cluster related events]_
- Example: _[User browsing sessions, click streams]_

### Question 2: How do you handle late data?

**Use Watermarks when:**
- Bounded lateness: _[Most events arrive within X seconds]_
- Completeness needed: _[Want accurate results]_
- Can tolerate delay: _[Results can wait for late data]_

**Allow Lateness when:**
- Some late arrivals: _[Network delays, mobile sync]_
- Update results: _[Can emit corrections]_
- Balance accuracy/latency: _[Wait a bit, not forever]_

**Drop Late Data when:**
- Strict latency: _[Need real-time results]_
- Rare late arrivals: _[< 1% of events]_
- Approximate OK: _[Metrics, dashboards]_

### Question 3: Do you need state?

**Stateless processing when:**
- Pure transformations: _[map, filter]_
- No aggregation: _[Just routing events]_
- No joins: _[Single stream]_
- Maximum throughput: _[No state overhead]_

**Stateful processing when:**
- Aggregations: _[count, sum, average]_
- Joins: _[Combine multiple streams]_
- Enrichment: _[Add reference data]_
- Session tracking: _[User state across events]_

### Question 4: What consistency level?

**At-most-once when:**
- Monitoring/Metrics: _[Losing some data OK]_
- Maximum throughput: _[No overhead]_
- Non-critical: _[Dashboards, alerts]_

**At-least-once when:**
- Idempotent operations: _[Safe to retry]_
- Can deduplicate: _[Downstream handles duplicates]_
- Good balance: _[Performance + reliability]_

**Exactly-once when:**
- Financial: _[Money, billing, payments]_
- Critical business logic: _[Inventory, orders]_
- Compliance: _[Audit trails]_

### Your Decision Tree

Build this after solving practice scenarios:

```
Stream Processing Pattern Selection
│
├─ What's the data arrival pattern?
│   ├─ In-order, low latency → Simple windowing
│   ├─ Out-of-order, bounded → Watermarks + allowed lateness
│   └─ Out-of-order, unbounded → Session windows or approximation
│
├─ What operations do you need?
│   ├─ Simple aggregation → Windowing only
│   ├─ Cross-event logic → Stateful processing
│   ├─ Multiple streams → Stream joins
│   └─ Enrichment → Stateful with reference data
│
├─ What's the consistency requirement?
│   ├─ Best effort → At-most-once
│   ├─ No duplicates OK → At-least-once + dedup
│   └─ Exactly-once → Full transactional processing
│
└─ What's the latency requirement?
    ├─ Sub-second → Drop late data, smaller windows
    ├─ Seconds → Watermarks with small lateness
    └─ Minutes → Large lateness window, accurate results
```

### The "Kill Switch" - Stream Processing Anti-Patterns

**Don't do this:**
1. **Large state without cleanup** - _[Set TTL, use cleanup triggers]_
2. **No backpressure handling** - _[Rate limit, buffer with spillover]_
3. **Ignoring late data** - _[Understand arrival patterns first]_
4. **Synchronous external calls** - _[Use async I/O or batching]_
5. **No monitoring of watermarks** - _[Track lag, late events]_
6. **Infinite session windows** - _[Set max session duration]_
7. **Stateful operations without checkpoints** - _[Enable fault tolerance]_

### The Rule of Three: Alternatives

**Option 1: Batch Processing (e.g., Spark)**
- Pros: _[Simple, high throughput, SQL support]_
- Cons: _[Higher latency, no real-time]_
- Use when: _[Hourly/daily jobs, historical data]_

**Option 2: Stream Processing (e.g., Flink, Kafka Streams)**
- Pros: _[Low latency, exactly-once, stateful]_
- Cons: _[Complex, operational overhead]_
- Use when: _[Real-time, event-driven, sub-second]_

**Option 3: Micro-Batch (e.g., Spark Streaming)**
- Pros: _[Balance latency/throughput, Spark ecosystem]_
- Cons: _[Not true streaming, higher latency than pure streaming]_
- Use when: _[Second-level latency OK, existing Spark]_

---

## Practice

### Scenario 1: Real-Time Analytics Dashboard

**Requirements:**
- Track page views per minute (updated every 10 seconds)
- Show top pages in last 5 minutes
- Handle 100K events/second
- Mobile apps may sync late data (up to 30s delay)
- Display updated immediately

**Your design:**

Windowing strategy: _[Tumbling, Sliding, or Session?]_

Reasoning:
- Window type: _[Fill in]_
- Window size: _[Fill in]_
- Slide interval: _[Fill in]_
- Why this choice: _[Fill in]_

Late data handling: _[How to handle 30s delayed mobile events?]_
- Watermark strategy: _[Fill in]_
- Allowed lateness: _[Fill in]_
- Trade-offs: _[Fill in]_

State requirements: _[What state do you need?]_
- Per-key state: _[Fill in]_
- State backend: _[In-memory or RocksDB?]_
- TTL: _[Fill in]_

### Scenario 2: Fraud Detection System

**Requirements:**
- Detect suspicious patterns in real-time
- Multiple failed logins within 1 minute
- Transactions from different countries < 10 minutes apart
- Must detect within 2 seconds of last event
- No false negatives (can't miss fraud)

**Your design:**

Pattern detection: _[How to detect patterns across events?]_
- Windowing: _[Fill in]_
- State needed: _[Fill in]_
- Join strategy: _[Fill in]_

Consistency: _[At-most-once, at-least-once, or exactly-once?]_
- Choice: _[Fill in]_
- Why: _[Fill in]_
- Implementation: _[Deduplication? Transactions?]_

Latency: _[How to meet 2-second requirement?]_
- Watermark strategy: _[Fill in]_
- Trade-offs: _[Accuracy vs speed]_

### Scenario 3: IoT Sensor Aggregation

**Requirements:**
- 10K sensors sending readings every 10 seconds
- Compute average, min, max per sensor per minute
- Sensors have unreliable networks (late data common)
- Some sensors offline for hours, then send batch
- Store aggregates in database (no duplicates)

**Your design:**

Windowing: _[Which type and why?]_
- Window type: _[Fill in]_
- Size: _[Fill in]_
- Reasoning: _[Fill in]_

Late data: _[How to handle hours-late data?]_
- Watermark strategy: _[Fill in]_
- Allowed lateness: _[Fill in]_
- Very late data: _[Drop or reprocess?]_

State management: _[How to manage state for 10K sensors?]_
- State size: _[Estimate per sensor]_
- TTL: _[How long to keep state?]_
- Cleanup: _[When to purge old state?]_

Output: _[How to avoid duplicate writes to database?]_
- Strategy: _[Idempotent writes? Deduplication? Transactions?]_
- Implementation: _[Fill in]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
  - [ ] Tumbling window implementation works
  - [ ] Sliding window implementation works
  - [ ] Session window implementation works
  - [ ] Watermark processing works
  - [ ] Late data handling works
  - [ ] Stateful processing works
  - [ ] Deduplication works
  - [ ] All client code runs successfully

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Understand difference between window types
  - [ ] Know event time vs processing time
  - [ ] Understand watermarks and their purpose
  - [ ] Know how state works and TTL
  - [ ] Understand exactly-once semantics
  - [ ] Can explain checkpointing

- [ ] **Decision Making**
  - [ ] Know when to use each window type
  - [ ] Can design watermark strategy
  - [ ] Know when to use state
  - [ ] Can choose consistency level
  - [ ] Completed all practice scenarios
  - [ ] Can justify design choices

- [ ] **Performance**
  - [ ] Understand time/space complexity
  - [ ] Know state size implications
  - [ ] Can estimate resource needs
  - [ ] Know throughput vs latency trade-offs

- [ ] **Mastery Check**
  - [ ] Could implement basic stream processor
  - [ ] Could design windowing strategy
  - [ ] Could handle late data appropriately
  - [ ] Could implement exactly-once processing
  - [ ] Know common streaming pitfalls
  - [ ] Can debug watermark issues

---

**Next:** [12. Observability →](12-observability.md)

**Back:** [10. Message Queues ←](10-message-queues.md)

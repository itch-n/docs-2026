# Stream Processing

> Real-time data processing with windowing

---

## Learning Objectives

By the end of this section you should be able to:

- Implement tumbling, sliding, and session windows and explain when each is appropriate
- Debug common stream processing bugs including window boundary errors and duplicate event counting

---

!!! warning "Operational reality"
    Stateful stream processing is significantly harder to operate than it appears. Watermarks require careful tuning and still produce late-data surprises; stateful operators accumulate state that must be snapshotted, replayed on restart, and pruned to prevent OOM failures; exactly-once semantics requires both source and sink to support it — one weak link breaks the guarantee. Many teams that adopted Flink or Kafka Streams for complex stateful pipelines quietly replaced specific use cases with scheduled batch jobs when the operational cost became clear.

    This topic covers the concepts needed to reason about real-time data architectures in interviews. The implementation details are for the teams whose job it is to run Flink at scale.

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing stream processing patterns, explain them simply.

**Prompts to guide you:**

1. **What is stream processing in one sentence?**
    - Your answer: <span class="fill-in">Stream processing is a ___ that handles events ___ as they arrive, instead of ___</span>

2. **What is a window in stream processing?**
    - Your answer: <span class="fill-in">A window is a ___ that groups events by ___ so you can ___</span>

3. **Real-world analogy for tumbling window:**
    - Example: "A tumbling window is like counting cars that pass every 5 minutes..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

</div>

---

## Quick Quiz (Do BEFORE implementing)

!!! tip "How to use this section"
    Fill in your best guesses **before** reading any code. After implementing each pattern, return here and check your predictions. The goal is to build intuition, not to get everything right on the first pass.

<div class="learner-section" markdown>

**Your task:** Test your intuition about stream processing. Answer these, then verify after implementation.

### Complexity Predictions

1. **Tumbling window processing:**
    - Time complexity per event: <span class="fill-in">[Your guess: O(?)]</span>
    - Space complexity for K keys over W windows: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified after learning: <span class="fill-in">[Actual]</span>

2. **Sliding window vs tumbling window:**
    - If sliding window size = 10s, slide = 2s, how many windows per event? <span class="fill-in">[Guess]</span>
    - Space overhead compared to tumbling: <span class="fill-in">[Guess: X times larger]</span>
    - Verified: <span class="fill-in">[Actual]</span>

### Scenario Predictions

**Scenario 1:** Events arriving: timestamps [100, 200, 150, 300] (out of order)

- **Tumbling window (size=100ms):** Which windows do they belong to?
    - Event@100ms → Window <span class="fill-in">[0-100? 100-200?]</span>
    - Event@200ms → Window <span class="fill-in">[Fill in]</span>
    - Event@150ms → Window <span class="fill-in">[Fill in]</span>
    - Event@300ms → Window <span class="fill-in">[Fill in]</span>

**Scenario 2:** Session window with 3-second gap

Events for user1: [1000ms, 2000ms, 3000ms, 7000ms, 8000ms]

- **How many sessions?** <span class="fill-in">[Guess]</span>
- **Session boundaries:** <span class="fill-in">[Fill in]</span>
- **If event@4500ms arrives late, what happens?** <span class="fill-in">[New session or merge?]</span>

### Trade-off Quiz

**Question:** When would batch processing be BETTER than stream processing?

- Your answer: <span class="fill-in">[Fill in]</span>
- Verified: <span class="fill-in">[Fill in after implementation]</span>

</div>

---

## Case Studies: Stream Processing in the Wild

### Netflix: Real-time Streaming Analytics with Flink

- **Pattern:** Windowed Aggregations on event streams.
- **How it works:** Netflix's infrastructure generates a massive stream of events: every "play," "pause," "buffer,"
  and "finish" action from millions of viewers is published to Apache Kafka. They use Apache Flink to process this
  stream in real-time. For example, a Flink job might use a **10-second tumbling window** to count the number of
  playback errors per region, allowing engineers to spot and react to regional outages instantly.
- **Key Takeaway:** Stream processing is essential for real-time operational monitoring at scale. By using windowed
  aggregations, raw event streams can be transformed into meaningful, actionable metrics for dashboards and alerting
  systems.

### LinkedIn Feed Updates: Real-time Content Delivery

- **Pattern:** Stream-Table Joins.
- **How it works:** LinkedIn's feed is a combination of real-time activity and user profile data. When a user you follow
  shares an article, that's a real-time event on a stream. To render the feed, their stream processing system (Apache
  Samza) must join this event stream with a table stream containing user profile data (like the user's name and
  headline). The result is a fully enriched feed item, delivered in near real-time.
- **Key Takeaway:** Stream processing isn't just about counting events. It's often about enriching real-time events with
  static or slow-moving data from tables to provide context and create a complete picture for the end-user.

### Cloudflare: DDoS Detection with Sliding Windows

- **Pattern:** Sliding Window analysis for anomaly detection.
- **How it works:** Cloudflare protects websites from DDoS attacks by analyzing vast streams of network request data. A
  stream processor might use a **1-minute sliding window**, evaluated every 5 seconds, to track the request count per IP
  address. If the count for any IP suddenly spikes and crosses a predefined threshold within that window, the system
  automatically identifies it as a potential attack and can block the IP at the edge.
- **Key Takeaway:** Sliding windows are perfect for detecting anomalies in real-time data. By continuously analyzing
  recent activity, systems can identify and react to security threats or performance issues much faster than traditional
  batch-based analysis would allow.

---

## Core Implementation

### Pattern 1: Windowing (Tumbling, Sliding, Session)

**Concept:** Group streaming data into finite chunks for aggregation.

**Use case:** Real-time analytics, metrics aggregation, event counting.

```java
--8<-- "com/study/systems/streamprocessing/StreamWindow.java"
```

!!! warning "Debugging Challenge — Window Boundary Off-by-One"
    The following `tumblingWindow_Buggy` method has **2 bugs**. It is supposed to count events per key per window.

    ```java
    public static Map<Long, Map<String, Long>> tumblingWindow_Buggy(
            List<Event<String, String>> events,
            long windowSize) {

        Map<Long, Map<String, Long>> windows = new TreeMap<>();

        for (Event<String, String> event : events) {
            long windowStart = event.timestamp / windowSize;

            Map<String, Long> window = windows.computeIfAbsent(windowStart, k -> new HashMap<>());

            window.put(event.key, 1L);
        }

        return windows;
    }
    ```

    - Bug 1: What is wrong with the `windowStart` calculation?
    - Bug 2: What is wrong with how each event is counted?

    ??? success "Answer"
        **Bug 1:** `windowStart` should be `(event.timestamp / windowSize) * windowSize` to align to proper window boundaries. Without the multiply, keys are window indices, not actual timestamps — so `windowStart = 1500/1000 = 1` instead of the correct `1000`.

        **Bug 2:** `window.put(event.key, 1L)` always **overwrites** the count with 1. It should use `window.merge(event.key, 1L, Long::sum)` to accumulate counts instead.

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
## Before/After: Why Stream Processing Matters

!!! note "Key insight"
    The critical difference between batch and stream processing is not throughput — it is **when results are available**. Stream processing produces output continuously as windows close, while batch processing waits for the entire dataset to accumulate before computing anything.

**Your task:** Compare batch processing vs stream processing to understand the impact.

### Example: Real-Time Analytics

**Problem:** Calculate page views per minute for a website getting 10K events/second.

#### Approach 1: Batch Processing (Traditional)

```java
// Batch processing - Process accumulated data every minute
public class BatchAnalytics {

    private List<Event> eventBuffer = new ArrayList<>();

    public void collectEvent(Event event) {
        eventBuffer.add(event);
    }

    // Runs every 60 seconds
    public Map<String, Long> computePageViews() {
        Map<String, Long> counts = new HashMap<>();

        // Process all accumulated events
        for (Event event : eventBuffer) {
            counts.merge(event.page, 1L, Long::sum);
        }

        // Clear buffer for next batch
        eventBuffer.clear();

        return counts;
    }
}
```

**Analysis:**

- **Latency:** 30-60 seconds average (must wait for batch to complete)
- **Memory:** All events in 1 minute = 10K/sec × 60 = 600K events in memory
- **Throughput:** High (process all at once)
- **Real-time:** No - results delayed by up to 60 seconds
- **Use case:** Reports, ETL jobs, historical analysis

**Timeline visualization:**

```
Events:     |-------- 60 seconds of collection --------|
Processing:                                              [Compute] → Results at T+60s
User sees:                                               ↑
                                                    Results 60s old
```

#### Approach 2: Stream Processing (Real-Time)

```java
// Stream processing - Continuous windowing
public class StreamAnalytics {

    private Map<Long, Map<String, Long>> windows = new TreeMap<>();
    private long windowSize = 60_000; // 60 seconds

    public void processEvent(Event event) {
        // Immediate assignment to window
        long windowStart = (event.timestamp / windowSize) * windowSize;

        // Update count immediately
        windows.computeIfAbsent(windowStart, k -> new HashMap<>())
               .merge(event.page, 1L, Long::sum);

        // Emit results when window closes
        long currentTime = System.currentTimeMillis();
        closeCompletedWindows(currentTime);
    }

    private void closeCompletedWindows(long currentTime) {
        // Windows that ended more than watermark delay ago
        long watermark = currentTime - 5000; // 5s delay tolerance

        windows.entrySet().removeIf(entry -> {
            long windowEnd = entry.getKey() + windowSize;
            if (windowEnd < watermark) {
                emitResults(entry.getKey(), entry.getValue());
                return true; // Remove closed window
            }
            return false;
        });
    }

    private void emitResults(long windowStart, Map<String, Long> counts) {
        // Results available immediately when window closes
        System.out.println("Window [" + windowStart + "]: " + counts);
    }
}
```

**Analysis:**

- **Latency:** 5-10 seconds (watermark delay + processing)
- **Memory:** Only active windows = ~2 windows × events = ~100K events in memory
- **Throughput:** Same (10K events/second)
- **Real-time:** Yes - results within seconds
- **Use case:** Dashboards, alerting, fraud detection

**Timeline visualization:**

```
Events:     |--10s--|--10s--|--10s--|--10s--|--10s--|--10s--|
Windows:    [-------- Window 0-60s --------]
Processing:                                  ↑
Results:                                     Results at T+5s
User sees:                                   ↑
                                        Results ~5s old
```

#### Performance Comparison

| Metric                     | Batch (60s)    | Stream (Real-Time) | Improvement      |
|----------------------------|----------------|--------------------|------------------|
| **Latency to see results** | 30-60 seconds  | 5-10 seconds       | **6-10x faster** |
| **Memory (peak)**          | 600K events    | 100K events        | **6x less**      |
| **Staleness of data**      | Up to 60s old  | Up to 5s old       | **12x fresher**  |
| **Throughput**             | 10K events/sec | 10K events/sec     | Same             |

#### Real-World Impact: Fraud Detection Example

**Batch approach:**

```

10:00:00 - Fraudulent transaction occurs
10:00:05 - 3 more suspicious transactions

10:00:45 - 5 more transactions (pattern clear)
10:01:00 - Batch job runs, detects fraud

10:01:05 - Alert sent, account frozen

Total: 9 fraudulent transactions, $4,500 loss
Detection delay: 65 seconds
```

**Stream approach:**

```

10:00:00 - Fraudulent transaction occurs
10:00:05 - 3 more suspicious transactions

10:00:10 - Pattern detected (window closed at watermark)
10:00:11 - Alert sent, account frozen

Total: 4 fraudulent transactions, $2,000 loss
Detection delay: 11 seconds
```

**Impact:** Stream processing caught fraud **54 seconds faster**, preventing **$2,500 in losses**.

#### Why Does Stream Processing Win?

**Key insight to understand:**

Batch processing treats time in discrete chunks:

```
Batch 1: [0s ────────────────── 60s] → Process → Wait
Batch 2: [60s ──────────────── 120s] → Process → Wait
```

Stream processing treats time continuously:

```
Events: ─•──•─•───•──•─•──•─•──•─→ (continuous)
Windows: [─────────] [─────────]
Results:     ↑            ↑
         (immediate)  (immediate)
```

**Your calculation:**

- For 1M events/second, batch processing with 5-minute windows needs _____ GB memory
- Stream processing with 1-minute windows needs _____ GB memory
- Memory savings: <span class="fill-in">_____</span> times less

#### When Batch Processing Is Still Better

**Batch wins when:**

1. **Historical analysis:** Processing months of data
2. **Complex joins:** Multiple large datasets
3. **Cost-sensitive:** Pay per compute hour (batch cheaper)
4. **No urgency:** Daily reports, weekly summaries

**After implementing, explain in your own words:**

<div class="learner-section" markdown>

- Why does stream processing use less memory? <span class="fill-in">[Your answer]</span>
- What's the trade-off between latency and accuracy with watermarks? <span class="fill-in">[Your answer]</span>
- When would you still choose batch processing? <span class="fill-in">[Your answer]</span>

</div>

---

## Common Misconceptions

!!! warning "Misconception 1: A larger watermark delay always gives more accurate results"
    A watermark delay controls how long the system waits for late-arriving events before closing a window. A very large delay does reduce data loss from late events, but it also increases the **end-to-end latency** for every window result — including results that had no late data. The right delay is the smallest value that covers the typical skew in your source data, not the maximum possible skew.

!!! warning "Misconception 2: Exactly-once processing means events are never duplicated in the network"
    Exactly-once semantics refers to the **effect** on state and output, not to how many times messages physically travel over the wire. The implementation typically sends some messages multiple times (at-least-once delivery) and uses idempotency or deduplication to ensure the state machine transitions exactly once per logical event. If your sink is not idempotent, exactly-once at the processor level does not protect against duplicate output.

!!! warning "Misconception 3: Stateless stream processing has no memory growth"
    Stateless operators (map, filter) do not accumulate per-key state, so they have constant memory usage regardless of throughput. However, windowing itself is stateful — each open window stores partial aggregates. A pipeline with many windows, large window sizes, or high key cardinality can still grow memory substantially even if you consider the individual operators "stateless."

---

## Decision Framework

<div class="learner-section" markdown>

**Your task:** Build decision trees for when to use each stream processing pattern.

### Question 1: What type of windowing do you need?

Answer after implementation:

**Use Tumbling Window when:**

- Fixed time boundaries: <span class="fill-in">[Every hour, every day]</span>
- Non-overlapping: <span class="fill-in">[Each event in exactly one window]</span>
- Simple aggregation: <span class="fill-in">[Count, sum per time period]</span>
- Example: <span class="fill-in">[Hourly sales reports, daily active users]</span>

**Use Sliding Window when:**

- Moving average: <span class="fill-in">[Last N minutes]</span>
- Overlapping periods: <span class="fill-in">[Need smooth transitions]</span>
- Real-time dashboards: <span class="fill-in">[Updated frequently]</span>
- Example: <span class="fill-in">[5-minute average updated every 30 seconds]</span>

**Use Session Window when:**

- User activity: <span class="fill-in">[Group by engagement sessions]</span>
- Variable length: <span class="fill-in">[Based on inactivity]</span>
- Burst detection: <span class="fill-in">[Cluster related events]</span>
- Example: <span class="fill-in">[User browsing sessions, click streams]</span>


</div>

---

## Practice

<div class="learner-section" markdown>

### Scenario 1: Real-Time Analytics Dashboard

**Requirements:**

- Track page views per minute (updated every 10 seconds)
- Show top pages in last 5 minutes
- Handle 100K events/second
- Mobile apps may sync late data (up to 30s delay)
- Display updated immediately

**Your design:**

Windowing strategy: <span class="fill-in">[Tumbling, Sliding, or Session?]</span>

Reasoning:

- Window type: <span class="fill-in">[Fill in]</span>
- Window size: <span class="fill-in">[Fill in]</span>
- Slide interval: <span class="fill-in">[Fill in]</span>
- Why this choice: <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if the Kafka topic that feeds page-view events becomes unavailable for 2 minutes? <span class="fill-in">[Fill in]</span>
- How does your design behave when a mobile app delivers a backlog of 30-second-late events after the relevant sliding windows have already closed? <span class="fill-in">[Fill in]</span>

### Scenario 2: Fraud Detection System

**Requirements:**

- Detect suspicious patterns in real-time
- Multiple failed logins within 1 minute
- Transactions from different countries < 10 minutes apart
- Must detect within 2 seconds of last event
- No false negatives (can't miss fraud)

**Your design:**

Pattern detection: <span class="fill-in">[How to detect patterns across events?]</span>

- Windowing: <span class="fill-in">[Fill in]</span>
- State needed: <span class="fill-in">[Fill in]</span>
- Join strategy: <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if the stream processor crashes mid-window while accumulating per-user login attempt state? <span class="fill-in">[Fill in]</span>
- How does your design behave when the downstream fraud alert sink (e.g. the block-account API) is temporarily unavailable and events continue arriving? <span class="fill-in">[Fill in]</span>

### Scenario 3: IoT Sensor Aggregation

**Requirements:**

- 10K sensors sending readings every 10 seconds
- Compute average, min, max per sensor per minute
- Sensors have unreliable networks (late data common)
- Some sensors offline for hours, then send batch
- Store aggregates in database (no duplicates)

**Your design:**

Windowing: <span class="fill-in">[Which type and why?]</span>

- Window type: <span class="fill-in">[Fill in]</span>
- Size: <span class="fill-in">[Fill in]</span>
- Reasoning: <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if a sensor goes offline for 3 hours and then re-connects, sending all buffered readings at once as a burst? <span class="fill-in">[Fill in]</span>
- How does your design behave when the database sink rejects a write due to a constraint violation, risking duplicate aggregates if the window is retried? <span class="fill-in">[Fill in]</span>


</div>

---

## Test Your Understanding

Answer these questions without looking at your implementation. They are designed to probe understanding, not recall.

1. **A session window with a 5-second gap creates 10 sessions for a given user over 1 hour. If you reduce the gap to 2 seconds, will there be more or fewer sessions? Explain why.**

    ??? success "Rubric"
        A complete answer addresses: (1) a smaller gap threshold causes more pairs of consecutive events to exceed the threshold, so previously merged sessions get split into separate sessions — the count increases; (2) the exact increase depends on the inter-event time distribution: events with inter-arrival times between 2s and 5s that were in the same session now start a new session; (3) reducing the gap to the limit of zero would make every event its own session, while increasing it to infinity would collapse all events into one session.

---

## Connected Topics

!!! info "Where this topic connects"

    - **12. Message Queues** — stream processors consume from queues; understanding delivery guarantees is prerequisite to windowing correctness → [12. Message Queues](12-message-queues.md)
    - **15. Observability** — windowed aggregations are a primary source of real-time metrics; observability pipelines often use stream processing internally → [15. Observability](15-observability.md)

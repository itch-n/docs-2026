# Observability

> Metrics, Logging, Tracing - Understanding what your distributed system is doing

---

## Learning Objectives

By the end of this section you should be able to:

- Explain the three pillars of observability (metrics, logs, traces) and when to use each
- Implement the RED (Rate, Errors, Duration) and USE (Utilization, Saturation, Errors) metric frameworks
- Design structured logs with consistent context fields and explain why context propagation is essential
- Implement distributed tracing with parent-child span relationships and describe how trace IDs enable cross-service correlation
- Define SLOs, calculate error budgets, and configure alert rules with appropriate severity and duration thresholds
- Identify and fix high-cardinality metric label issues before they cause performance problems

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing observability patterns, explain them simply.

**Prompts to guide you:**

1. **What is observability in one sentence?**
    - Your answer: <span class="fill-in">Observability is the ability to ___ what is happening inside your system by ___</span>

2. **What are the three pillars of observability?**
    - Your answer: <span class="fill-in">The three pillars are ___, ___, and ___; they differ because ___</span>

3. **Real-world analogy for metrics:**
    - Example: "Metrics are like a car's dashboard showing speed, fuel, temperature..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **Real-world analogy for logs:**
    - Example: "Logs are like a detailed diary of everything that happened..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

5. **Real-world analogy for traces:**
    - Example: "Traces are like following a package through the postal system..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

6. **When should you add metrics vs logs vs traces?**
    - Your answer: <span class="fill-in">Use metrics when ___, logs when ___, traces when ___</span>

</div>

---

## Quick Quiz (Do BEFORE implementing)

!!! tip "How to use this section"
    Fill in your best guesses **before** reading any code. After implementing each pattern, return here and check your predictions. Pay special attention to the cardinality scenario — it reveals a cost that surprises many engineers.

<div class="learner-section" markdown>

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **Storing all request details in memory:**
    - Space complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified after learning: <span class="fill-in">[Actual: O(?)]</span>

2. **Recording a metric counter increment:**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Space complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified: <span class="fill-in">[Actual]</span>

3. **Cost calculation:**
    - If you log every request at 10K req/sec = <span class="fill-in">_____</span> logs/day
    - If you sample traces at 1% = <span class="fill-in">_____</span> traces/day
    - Storage reduction factor: <span class="fill-in">_____</span> times less

### Scenario Predictions

**Scenario 1:** Your API has 99.5% success rate with a 99.9% SLO

- **Is this within SLO?** <span class="fill-in">[Yes/No - Why?]</span>
- **Error budget remaining:** <span class="fill-in">[Calculate]</span>
- **Should you alert?** <span class="fill-in">[Yes/No - Why?]</span>
- **How many more failures can you have?** <span class="fill-in">[Fill in]</span>

**Scenario 2:** Users report "slow checkout" but avg latency looks fine

- **Which observability tool helps most?** <span class="fill-in">[Metrics/Logs/Traces - Why?]</span>
- **What metric might you be missing?** <span class="fill-in">[Fill in]</span>
- **What percentile should you check?** <span class="fill-in">[P50/P95/P99 - Why?]</span>

**Scenario 3:** Metric label has user_id with 1M unique values

- **Is this a good metric label?** <span class="fill-in">[Yes/No - Why?]</span>
- **What problem does this cause?** <span class="fill-in">[Fill in]</span>
- **What should you do instead?** <span class="fill-in">[Fill in]</span>

### Trade-off Quiz

**Question:** When would structured logs be BETTER than traces for debugging?

- Your answer: <span class="fill-in">[Fill in before implementation]</span>
- Verified answer: <span class="fill-in">[Fill in after learning]</span>

**Question:** What's the MAIN difference between metrics and logs?

- [ ] Metrics are numbers, logs are text
- [ ] Metrics are aggregated, logs are individual events
- [ ] Metrics are faster, logs are slower
- [ ] Metrics are free, logs cost money

Verify after implementation: <span class="fill-in">[Which one(s)?]</span>

**Question:** Why sample traces instead of capturing 100%?

- Your answer: <span class="fill-in">[Fill in reasoning]</span>
- Verified: <span class="fill-in">[Fill in after learning about performance impact]</span>

</div>

---

## Case Studies: Observability in the Wild

### Datadog: The Three Pillars in One Platform

- **Pattern:** Unified collection of Metrics, Traces, and Logs.
- **How it works:** A company using Datadog instruments its applications to send all three types of telemetry data. When
  a user reports a slow API endpoint, an engineer can start with a dashboard showing a spike in latency for that
  endpoint (**metric**). From there, they can drill down to a specific slow **trace** for that endpoint. The trace will
  show that the database query took 3 seconds. The engineer can then pivot directly to the **logs** from the database
  server at that exact time, which reveal a "slow query" log line, identifying the exact SQL query that needs
  optimization.
- **Key Takeaway:** The power of observability comes from correlating the three pillars. By seamlessly moving between
  metrics, traces, and logs, engineers can diagnose problems orders of magnitude faster than by looking at each data
  source in isolation.

### Netflix's Distributed Tracing: Debugging Microservices

- **Pattern:** Distributed Tracing with a unique Request ID.
- **How it works:** When a user's request enters the Netflix ecosystem, it's assigned a unique ID (e.g.,
  `Netflix-Request-Id`). This ID is passed in the header of every subsequent internal network call as the request
  travels through dozens of microservices. If any service encounters an error, it logs the error along with this Request
  ID. Engineers can then use this single ID to search their centralized logging platform (like ELK) and instantly
  retrieve all logs and traces related to that specific request from every service it touched.
- **Key Takeaway:** In a complex microservices architecture, simple log messages are not enough. Distributed tracing is
  essential for understanding the full lifecycle of a request and quickly pinpointing which service in a long chain is
  the source of an error or latency.

### Uber's M3: High-Cardinality Metrics at Scale

- **Pattern:** High-cardinality time-series metrics for business and system monitoring.
- **How it works:** Uber needed to monitor millions of unique entities in real-time (drivers, riders, trips).
  Traditional metrics systems struggle with this "high cardinality." They built M3, a metrics platform designed to
  handle this scale. It allows them to ask questions like "What is the average wait time for riders in downtown San
  Francisco right now?" (`metrics.riders.wait_time.avg{region=sf, district=downtown}`).
- **Key Takeaway:** Metrics are not just for CPU and memory. They are a powerful tool for real-time business
  intelligence. However, monitoring high-cardinality dimensions (like individual users or orders) requires a specialized
  time-series database built to handle the metric explosion.

---

## Core Implementation

### Pattern 1: Metrics Collection

**Concept:** Time-series measurements for monitoring system health and performance.

**Use case:** API monitoring, resource utilization, business metrics.

```java
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
        private final AtomicDouble value = new AtomicDouble(0.0);
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
```

!!! warning "Debugging Challenge — High-Cardinality Metric Label"
    The following metrics class has a **scalability bug**. It is supposed to track cache hits per user, but it will crash Prometheus (or any time-series metrics system) if you have more than a few thousand users.

    ```java
    public class CacheMetrics {
        private Map<String, Counter> cacheHitsByUser = new ConcurrentHashMap<>();

        public void recordCacheHit(String userId) {
            cacheHitsByUser
                .computeIfAbsent(userId,
                    k -> new Counter("cache_hits", Map.of("user_id", userId)))
                .inc();
        }
    }
    ```

    - What is the problem with using `user_id` as a metric label?
    - If you have 1 million users, how many unique time-series does this create?
    - What should you track instead?

    ??? success "Answer"
        **Bug:** `user_id` is a high-cardinality label. Each unique label combination creates a new time-series in the metrics system. With 1 million users, this creates 1 million time-series — far beyond what Prometheus or Datadog can handle efficiently without specialized infrastructure.

        **Memory impact:** Each series uses ~10 KB of memory → 10 GB total. Query time degrades from milliseconds to seconds. The metrics system can crash.

        **Fix:** Track aggregated metrics. Use logs or traces for per-user detail:

        ```java
        private Counter cacheHits = new Counter("cache_hits_total", Map.of());
        private Counter cacheMisses = new Counter("cache_misses_total", Map.of());

        public void recordCacheHit(boolean hit, String userId) {
            if (hit) cacheHits.inc();
            else cacheMisses.inc();
            // For per-user detail, use a log line instead
            logger.info("Cache result", Map.of("user_id", userId, "hit", hit));
        }
        ```

        **Rule:** Metric labels must have **bounded cardinality** (fewer than ~100 unique values per label).

---

### Pattern 2: Structured Logging

**Concept:** JSON-formatted logs with consistent fields for parsing and aggregation.

**Use case:** Application logs, audit trails, debugging distributed systems.

```java
import java.util.*;
import java.time.*;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Structured Logging: JSON logs with context
 *
 * Log levels: TRACE, DEBUG, INFO, WARN, ERROR, FATAL
 * Context: Trace ID, User ID, Request ID
 * Fields: timestamp, level, message, context, fields
 */
public class StructuredLogger {

    enum LogLevel {
        TRACE(0), DEBUG(1), INFO(2), WARN(3), ERROR(4), FATAL(5);

        final int priority;
        LogLevel(int priority) {
            this.priority = priority;
        }
    }

    private final String service;
    private final LogLevel minLevel;
    private final ObjectMapper mapper;
    private final ThreadLocal<Map<String, Object>> context;

    public StructuredLogger(String service, LogLevel minLevel) {
        this.service = service;
        this.minLevel = minLevel;
        this.mapper = new ObjectMapper();
        this.context = ThreadLocal.withInitial(HashMap::new);
    }

    /**
     * Log entry structure
     *
     * TODO: Define log entry format
     */
    static class LogEntry {
        public String timestamp;
        public String level;
        public String service;
        public String message;
        public Map<String, Object> context;
        public Map<String, Object> fields;

        LogEntry(String service, LogLevel level, String message,
                 Map<String, Object> context, Map<String, Object> fields) {
            this.timestamp = Instant.now().toString();
            this.level = level.name();
            this.service = service;
            this.message = message;
            this.context = new HashMap<>(context);
            this.fields = fields;
        }
    }

    /**
     * Add context to current thread (trace ID, user ID, etc.)
     * Time: O(1)
     *
     * TODO: Implement context addition
     */
    public void addContext(String key, Object value) {
        // TODO: Add to thread-local context
    }

    /**
     * Clear context for current thread
     * Time: O(1)
     *
     * TODO: Implement context clearing
     */
    public void clearContext() {
        // TODO: Clear thread-local context
    }

    /**
     * Log at INFO level
     * Time: O(1)
     *
     * TODO: Implement info logging
     */
    public void info(String message) {
        log(LogLevel.INFO, message, Map.of());
    }

    public void info(String message, Map<String, Object> fields) {
        log(LogLevel.INFO, message, fields);
    }

    /**
     * Log at WARN level
     *
     * TODO: Implement warn logging
     */
    public void warn(String message) {
        log(LogLevel.WARN, message, Map.of());
    }

    public void warn(String message, Map<String, Object> fields) {
        log(LogLevel.WARN, message, fields);
    }

    /**
     * Log at ERROR level
     *
     * TODO: Implement error logging
     */
    public void error(String message) {
        log(LogLevel.ERROR, message, Map.of());
    }

    public void error(String message, Throwable t) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("error", t.getClass().getName());
        fields.put("error_message", t.getMessage());
        fields.put("stack_trace", getStackTrace(t));
        log(LogLevel.ERROR, message, fields);
    }

    public void error(String message, Map<String, Object> fields) {
        log(LogLevel.ERROR, message, fields);
    }

    /**
     * Core logging method
     * Time: O(1) + O(JSON serialization)
     *
     * TODO: Implement core logging
     * 1. Check if level is enabled
     * 2. Create log entry with context
     * 3. Serialize to JSON
     * 4. Write to output
     */
    private void log(LogLevel level, String message, Map<String, Object> fields) {
        // TODO: Check if level should be logged

        // TODO: Create log entry

        // TODO: Serialize to JSON and output
    }

    /**
     * Helper: Get stack trace as string
     *
     * TODO: Implement stack trace extraction
     */
    private String getStackTrace(Throwable t) {
        // TODO: Convert stack trace to string
        return null; // Replace
    }

}
```

---

### Pattern 3: Distributed Tracing

**Concept:** Track requests across multiple services with parent-child relationships.

**Use case:** Debugging microservices, understanding request flow, finding bottlenecks.

```java
import java.util.*;
import java.time.*;
import java.util.concurrent.*;

/**
 * Distributed Tracing: Track requests across services
 *
 * Concepts:
 * - Trace: End-to-end request flow
 * - Span: Single operation within a trace
 * - Context: Trace ID + Span ID propagated across services
 */
public class DistributedTracer {

    /**
     * Trace context: Propagated across service boundaries
     *
     * TODO: Define trace context structure
     */
    static class TraceContext {
        String traceId;      // Unique ID for entire trace
        String spanId;       // Current span ID
        String parentSpanId; // Parent span ID (null for root)

        TraceContext(String traceId, String spanId, String parentSpanId) {
            this.traceId = traceId;
            this.spanId = spanId;
            this.parentSpanId = parentSpanId;
        }

        /**
         * Create root trace context
         *
         * TODO: Generate new trace ID
         */
        public static TraceContext createRoot() {
            // TODO: Generate unique trace ID
            return null; // Replace
        }

        /**
         * Create child context for new span
         *
         * TODO: Generate child span ID
         */
        public TraceContext createChild() {
            // TODO: Keep same trace ID, new span ID
            return null; // Replace
        }
    }

    /**
     * Span: Represents single operation
     * Time: O(1) for all operations
     *
     * TODO: Define span structure
     */
    static class Span {
        String traceId;
        String spanId;
        String parentSpanId;
        String operationName;
        long startTimeMicros;
        long endTimeMicros;
        Map<String, String> tags;
        List<String> logs;

        Span(TraceContext context, String operationName) {
            this.traceId = context.traceId;
            this.spanId = context.spanId;
            this.parentSpanId = context.parentSpanId;
            this.operationName = operationName;
            this.startTimeMicros = System.nanoTime() / 1000;
            this.tags = new HashMap<>();
            this.logs = new ArrayList<>();
        }

        /**
         * Add tag to span (metadata)
         *
         * TODO: Add span tag
         */
        public void setTag(String key, String value) {
            // TODO: Add to tags map
        }

        /**
         * Log event within span
         *
         * TODO: Add log entry
         */
        public void log(String message) {
            // TODO: Add timestamped log
        }

        /**
         * Finish span (record end time)
         *
         * TODO: Record end time
         */
        public void finish() {
            // TODO: Set end time
        }

        public long getDurationMicros() {
            return endTimeMicros - startTimeMicros;
        }
    }

    /**
     * Tracer: Creates and manages spans
     */
    private final String serviceName;
    private final ThreadLocal<Deque<Span>> activeSpans;
    private final List<Span> completedSpans;

    public DistributedTracer(String serviceName) {
        this.serviceName = serviceName;
        this.activeSpans = ThreadLocal.withInitial(ArrayDeque::new);
        this.completedSpans = new CopyOnWriteArrayList<>();
    }

    /**
     * Start new root span
     * Time: O(1)
     *
     * TODO: Create root span
     */
    public Span startSpan(String operationName) {
        // TODO: Create root context and span
        return null; // Replace
    }

    /**
     * Start child span of current active span
     * Time: O(1)
     *
     * TODO: Create child span
     */
    public Span startChildSpan(String operationName) {
        // TODO: Get current span, create child context
        //
        //   Span parent = stack.peek();
        //   TraceContext parentContext = new TraceContext(
        //     parent.traceId, parent.spanId, parent.parentSpanId
        //   );
        //   TraceContext childContext = parentContext.createChild();
        //   Span span = new Span(childContext, operationName);
        //   span.setTag("service", serviceName);
        //   stack.push(span);
        //   return span;
        return null; // Replace
    }

    /**
     * Finish current span
     * Time: O(1)
     *
     * TODO: Finish and record span
     */
    public void finishSpan() {
        // TODO: Pop span from stack, finish it, store it
    }

    /**
     * Get current active span
     * Time: O(1)
     *
     * TODO: Return current span
     */
    public Span getCurrentSpan() {
        // TODO: Peek at top of stack
        return null; // Replace
    }

    /**
     * Find all spans for a trace
     * Time: O(N) where N = total spans
     *
     * TODO: Filter spans by trace ID
     */
    public List<Span> getTrace(String traceId) {
        // TODO: Filter completed spans by trace ID
        return null; // Replace
    }

    /**
     * Build trace tree for visualization
     * Time: O(N) where N = spans in trace
     *
     * TODO: Build parent-child tree
     */
    public String visualizeTrace(String traceId) {
        // TODO: Build tree structure from parent-child relationships
        //
        //   // Group by parent
        //   for (Span span : spans) {
        //     String parentId = span.parentSpanId != null ? span.parentSpanId : "root";
        //     childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>())
        //                .add(span);
        //   }
        //
        //   // Find root and build tree
        //   Span root = spans.stream()
        //     .filter(s -> s.parentSpanId == null)
        //     .findFirst()
        //     .orElse(null);
        //
        //   StringBuilder sb = new StringBuilder();
        //   buildTraceString(root, childrenMap, sb, 0);
        //   return sb.toString();

        return null; // Replace
    }

    private void buildTraceString(Span span, Map<String, List<Span>> childrenMap,
                                   StringBuilder sb, int depth) {
        // TODO: Recursively build tree string
        //
        //   List<Span> children = childrenMap.get(span.spanId);
        //   if (children != null) {
        //     for (Span child : children) {
        //       buildTraceString(child, childrenMap, sb, depth + 1);
        //     }
        //   }
    }

    /**
     * Calculate critical path (longest path in trace)
     * Time: O(N) where N = spans
     *
     * TODO: Find bottleneck in trace
     */
    public List<Span> getCriticalPath(String traceId) {
        // TODO: DFS to find longest path
        return null; // Replace
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class DistributedTracerClient {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Distributed Tracing ===\n");

        DistributedTracer tracer = new DistributedTracer("order-service");

        // Test 1: Simple trace
        System.out.println("--- Test 1: Simple Trace ---");
        DistributedTracer.Span rootSpan = tracer.startSpan("process_order");
        rootSpan.setTag("order_id", "12345");
        rootSpan.log("Order validation started");

        Thread.sleep(10);
        rootSpan.log("Order validated");
        tracer.finishSpan();

        System.out.println("Root span duration: " +
            rootSpan.getDurationMicros() / 1000.0 + "ms");

        // Test 2: Parent-child spans
        System.out.println("\n--- Test 2: Parent-Child Spans ---");
        DistributedTracer.Span apiSpan = tracer.startSpan("api_request");
        apiSpan.setTag("endpoint", "/api/checkout");
        apiSpan.log("Request received");

        Thread.sleep(5);

        DistributedTracer.Span dbSpan = tracer.startChildSpan("database_query");
        dbSpan.setTag("query", "SELECT * FROM orders");
        dbSpan.log("Query started");
        Thread.sleep(15);
        dbSpan.log("Query completed");
        tracer.finishSpan(); // Finish DB span

        DistributedTracer.Span cacheSpan = tracer.startChildSpan("cache_lookup");
        cacheSpan.setTag("key", "user:123");
        Thread.sleep(2);
        tracer.finishSpan(); // Finish cache span

        apiSpan.log("Request completed");
        tracer.finishSpan(); // Finish API span

        String traceId = apiSpan.traceId;
        System.out.println("Trace ID: " + traceId);
        System.out.println("API span duration: " + apiSpan.getDurationMicros() / 1000.0 + "ms");
        System.out.println("DB span duration: " + dbSpan.getDurationMicros() / 1000.0 + "ms");
        System.out.println("Cache span duration: " + cacheSpan.getDurationMicros() / 1000.0 + "ms");

        // Test 3: Trace visualization
        System.out.println("\n--- Test 3: Trace Visualization ---");
        String visualization = tracer.visualizeTrace(traceId);
        if (visualization != null) {
            System.out.println(visualization);
        } else {
            System.out.println("(Trace visualization not yet implemented)");
        }

        // Test 4: Multiple traces
        System.out.println("\n--- Test 4: Multiple Traces ---");
        DistributedTracer.Span trace1 = tracer.startSpan("operation_1");
        Thread.sleep(5);
        tracer.finishSpan();

        DistributedTracer.Span trace2 = tracer.startSpan("operation_2");
        Thread.sleep(8);
        tracer.finishSpan();

        System.out.println("Trace 1 ID: " + trace1.traceId);
        System.out.println("Trace 2 ID: " + trace2.traceId);
        System.out.println("Different traces: " + !trace1.traceId.equals(trace2.traceId));
    }
}
```

---

### Pattern 4: SLOs and Alerting

**Concept:** Define Service Level Objectives and alert on violations.

**Use case:** Production monitoring, on-call alerting, capacity planning.

```java
import java.util.*;
import java.time.*;

/**
 * SLO Management and Alerting
 *
 * Definitions:
 * - SLI (Service Level Indicator): Actual measurement (e.g., 99.5% uptime)
 * - SLO (Service Level Objective): Target value (e.g., 99.9% uptime)
 * - SLA (Service Level Agreement): Contract with penalties (e.g., 99.95% uptime)
 * - Error Budget: Allowed failure amount (e.g., 0.1% = 43 minutes/month)
 */
public class SLOManager {

    /**
     * SLI: Service Level Indicator
     */
    enum SLIType {
        AVAILABILITY,  // % of successful requests
        LATENCY,       // % of requests below threshold
        ERROR_RATE     // % of failed requests
    }

    /**
     * SLO Definition
     *
     * TODO: Define SLO structure
     */
    static class SLO {
        String name;
        SLIType type;
        double target;         // Target value (e.g., 0.999 for 99.9%)
        Duration window;       // Time window (e.g., 30 days)
        double alertThreshold; // When to alert (e.g., 0.5 = 50% error budget consumed)

        SLO(String name, SLIType type, double target, Duration window, double alertThreshold) {
            this.name = name;
            this.type = type;
            this.target = target;
            this.window = window;
            this.alertThreshold = alertThreshold;
        }
    }

    /**
     * SLI Measurement
     *
     * TODO: Track actual measurements
     */
    static class SLIMeasurement {
        SLO slo;
        List<DataPoint> measurements;

        static class DataPoint {
            Instant timestamp;
            double value;

            DataPoint(Instant timestamp, double value) {
                this.timestamp = timestamp;
                this.value = value;
            }
        }

        SLIMeasurement(SLO slo) {
            this.slo = slo;
            this.measurements = new ArrayList<>();
        }

        /**
         * Record measurement
         * Time: O(1)
         *
         * TODO: Add data point
         */
        public void record(double value) {
            // TODO: Add measurement with timestamp
        }

        /**
         * Calculate current SLI value
         * Time: O(N) where N = measurements in window
         *
         * TODO: Calculate SLI for time window
         */
        public double calculate() {
            // TODO: Filter measurements within window
            //
            //   if (recent.isEmpty()) return 1.0;
            //
            //   // Calculate based on SLI type
            //   switch (slo.type) {
            //     case AVAILABILITY:
            //       // % of successful requests
            //       return recent.stream()
            //         .mapToDouble(dp -> dp.value)
            //         .average()
            //         .orElse(1.0);
            //     // ... other types
            //   }

            return 1.0; // Replace
        }

        /**
         * Calculate error budget remaining
         * Time: O(1) after calculate()
         *
         * TODO: Compute error budget
         * Error budget = (target - actual) / (1 - target)
         * Example: target=0.999, actual=0.998
         *   budget = (0.999 - 0.998) / (1 - 0.999) = 0.001 / 0.001 = 1.0 (100% remaining)
         */
        public double getErrorBudget() {
            // TODO: Calculate error budget
            //
            //   double allowed = 1.0 - slo.target;  // Allowed failure rate
            //   double actual = 1.0 - current;       // Actual failure rate
            //   double consumed = actual / allowed;  // Fraction consumed
            //   return Math.max(0.0, 1.0 - consumed); // Remaining budget

            return 1.0; // Replace
        }

        /**
         * Check if alert should fire
         * Time: O(1)
         *
         * TODO: Determine if alert needed
         */
        public boolean shouldAlert() {
            // TODO: Check if error budget below threshold
            return false; // Replace
        }
    }

    /**
     * Alert Rule
     *
     * TODO: Define alert conditions
     */
    static class AlertRule {
        String name;
        String query;          // Metric query (e.g., "error_rate > 0.01")
        Duration duration;     // Must be true for this long
        String severity;       // CRITICAL, WARNING, INFO
        String message;
        List<String> notifyChannels; // slack, pagerduty, email

        AlertRule(String name, String query, Duration duration,
                  String severity, String message, List<String> channels) {
            this.name = name;
            this.query = query;
            this.duration = duration;
            this.severity = severity;
            this.message = message;
            this.notifyChannels = channels;
        }
    }

    /**
     * Alert Manager
     *
     * TODO: Evaluate rules and fire alerts
     */
    static class AlertManager {
        private List<AlertRule> rules;
        private Map<String, Instant> firingAlerts; // Alert name -> first fired time

        AlertManager() {
            this.rules = new ArrayList<>();
            this.firingAlerts = new HashMap<>();
        }

        /**
         * Add alert rule
         *
         * TODO: Register alert rule
         */
        public void addRule(AlertRule rule) {
            // TODO: Add to rules list
        }

        /**
         * Evaluate all rules
         * Time: O(R) where R = rules
         *
         * TODO: Check all alert conditions
         */
        public List<Alert> evaluate(Map<String, Double> metrics) {
            List<Alert> alerts = new ArrayList<>();

            // TODO: Implement iteration/conditional logic
            //
            //     if (condition) {
            //       // Check if been firing long enough
            //       Instant firstFired = firingAlerts.computeIfAbsent(
            //         rule.name, k -> Instant.now()
            //       );
            //       Duration firingFor = Duration.between(firstFired, Instant.now());
            //
            //       if (firingFor.compareTo(rule.duration) >= 0) {
            //         alerts.add(new Alert(rule));
            //       }
            //     } else {
            //       // Clear if no longer firing
            //       firingAlerts.remove(rule.name);
            //     }
            //   }

            return alerts; // Replace
        }

        /**
         * Helper: Evaluate query condition
         *
         * TODO: Parse and evaluate simple queries
         */
        private boolean evaluateQuery(String query, Map<String, Double> metrics) {
            // TODO: Parse query like "error_rate > 0.01"
            //
            //   String metric = parts[0];
            //   String operator = parts[1];
            //   double threshold = Double.parseDouble(parts[2]);
            //
            //   Double value = metrics.get(metric);
            //   if (value == null) return false;
            //
            //   switch (operator) {
            //     case ">": return value > threshold;
            //     case "<": return value < threshold;
            //     case ">=": return value >= threshold;
            //     case "<=": return value <= threshold;
            //     default: return false;
            //   }

            return false; // Replace
        }
    }

    /**
     * Fired Alert
     *
     * TODO: Alert notification structure
     */
    static class Alert {
        AlertRule rule;
        Instant firedAt;

        Alert(AlertRule rule) {
            this.rule = rule;
            this.firedAt = Instant.now();
        }

        @Override
        public String toString() {
            return String.format("[%s] %s: %s",
                rule.severity, rule.name, rule.message);
        }
    }

    /**
     * Runbook: Steps to resolve alert
     *
     * TODO: Define runbook structure
     */
    static class Runbook {
        String alertName;
        String description;
        List<String> investigationSteps;
        List<String> resolutionSteps;
        Map<String, String> relatedDashboards;

        Runbook(String alertName) {
            this.alertName = alertName;
            this.investigationSteps = new ArrayList<>();
            this.resolutionSteps = new ArrayList<>();
            this.relatedDashboards = new HashMap<>();
        }

        public void addInvestigationStep(String step) {
            investigationSteps.add(step);
        }

        public void addResolutionStep(String step) {
            resolutionSteps.add(step);
        }

        public void addDashboard(String name, String url) {
            relatedDashboards.put(name, url);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Runbook: ").append(alertName).append(" ===\n\n");
            sb.append(description).append("\n\n");

            sb.append("Investigation Steps:\n");
            for (int i = 0; i < investigationSteps.size(); i++) {
                sb.append((i + 1)).append(". ").append(investigationSteps.get(i)).append("\n");
            }

            sb.append("\nResolution Steps:\n");
            for (int i = 0; i < resolutionSteps.size(); i++) {
                sb.append((i + 1)).append(". ").append(resolutionSteps.get(i)).append("\n");
            }

            if (!relatedDashboards.isEmpty()) {
                sb.append("\nRelated Dashboards:\n");
                relatedDashboards.forEach((name, url) ->
                    sb.append("- ").append(name).append(": ").append(url).append("\n")
                );
            }

            return sb.toString();
        }
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;
import java.time.*;

public class SLOManagerClient {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== SLO Management and Alerting ===\n");

        // Test 1: SLO Tracking
        System.out.println("--- Test 1: SLO Tracking ---");
        SLOManager.SLO availabilitySLO = new SLOManager.SLO(
            "API Availability",
            SLOManager.SLIType.AVAILABILITY,
            0.999,  // 99.9% target
            Duration.ofDays(30),
            0.5     // Alert at 50% error budget
        );

        SLOManager.SLIMeasurement measurement = new SLOManager.SLIMeasurement(availabilitySLO);

        // Simulate measurements (1 = success, 0 = failure)
        for (int i = 0; i < 1000; i++) {
            measurement.record(1.0); // Success
        }
        for (int i = 0; i < 2; i++) {
            measurement.record(0.0); // Failure
        }

        double currentSLI = measurement.calculate();
        double errorBudget = measurement.getErrorBudget();
        boolean shouldAlert = measurement.shouldAlert();

        System.out.println("Target SLO: 99.9%");
        System.out.println("Current SLI: " + (currentSLI * 100) + "%");
        System.out.println("Error budget remaining: " + (errorBudget * 100) + "%");
        System.out.println("Should alert: " + shouldAlert);

        // Test 2: Alert Rules
        System.out.println("\n--- Test 2: Alert Rules ---");
        SLOManager.AlertManager alertManager = new SLOManager.AlertManager();

        SLOManager.AlertRule highErrorRate = new SLOManager.AlertRule(
            "HighErrorRate",
            "error_rate > 0.01",
            Duration.ofMinutes(5),
            "CRITICAL",
            "Error rate above 1% for 5+ minutes",
            Arrays.asList("pagerduty", "slack")
        );

        SLOManager.AlertRule highLatency = new SLOManager.AlertRule(
            "HighLatency",
            "p99_latency > 1.0",
            Duration.ofMinutes(10),
            "WARNING",
            "P99 latency above 1s for 10+ minutes",
            Arrays.asList("slack")
        );

        alertManager.addRule(highErrorRate);
        alertManager.addRule(highLatency);

        // Simulate metrics
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("error_rate", 0.015);  // 1.5% errors
        metrics.put("p99_latency", 0.8);   // 800ms

        List<SLOManager.Alert> alerts = alertManager.evaluate(metrics);
        System.out.println("Alerts fired: " + alerts.size());
        for (SLOManager.Alert alert : alerts) {
            System.out.println(alert);
        }

        // Test 3: Error Budget Calculation
        System.out.println("\n--- Test 3: Error Budget Calculation ---");
        double[] targets = {0.99, 0.999, 0.9999};
        String[] names = {"99%", "99.9%", "99.99%"};

        for (int i = 0; i < targets.length; i++) {
            double target = targets[i];
            double allowedDowntime = (1.0 - target) * 30 * 24 * 60; // minutes per month
            System.out.println("\nSLO: " + names[i]);
            System.out.println("Allowed downtime: " + allowedDowntime + " minutes/month");
            System.out.println("That's " + (allowedDowntime / 60) + " hours/month");
        }

        // Test 4: Runbook
        System.out.println("\n--- Test 4: Runbook ---");
        SLOManager.Runbook runbook = new SLOManager.Runbook("HighErrorRate");
        runbook.description = "Error rate has exceeded 1% threshold";

        runbook.addInvestigationStep("Check error logs for patterns");
        runbook.addInvestigationStep("Review recent deployments");
        runbook.addInvestigationStep("Check downstream service health");
        runbook.addInvestigationStep("Verify database connection pool");

        runbook.addResolutionStep("Rollback recent deployment if needed");
        runbook.addResolutionStep("Scale up affected services");
        runbook.addResolutionStep("Enable circuit breakers");
        runbook.addResolutionStep("Update on-call ticket with findings");

        runbook.addDashboard("Error Dashboard", "https://grafana.example.com/errors");
        runbook.addDashboard("Service Health", "https://grafana.example.com/health");

        System.out.println(runbook);
    }
}
```

!!! info "Loop back"
    Now that you have implemented all four patterns, return to the **Quick Quiz** at the top of this page. Fill in the "Verified after learning" fields. Did your estimate of P99 latency's importance match what you saw in the SLO section? Return to the **ELI5** section and complete your one-sentence explanations.

---

## Before/After: Why This Pattern Matters

!!! note "Key insight"
    Observability doesn't just speed up debugging — it changes the **nature** of debugging. Without it, engineers must form hypotheses and deploy code to test them. With it, engineers query data that already exists. The difference is not linear (faster) but categorical (different kind of work).

**Your task:** Compare blind systems vs observable systems to understand the impact.

### Example: Debugging a Slow API

**Problem:** Users report checkout API is slow, but you don't know why.

#### Approach 1: No Observability (Flying Blind)

```java
// No instrumentation - just the business logic
public class CheckoutService {
    public Order checkout(Cart cart) {
        validateCart(cart);
        chargePayment(cart);
        createOrder(cart);
        sendEmail(cart);
        return order;
    }
}
```

**What you can see:**

- Nothing! You have to guess what's slow
- Add println statements and redeploy
- Wait for complaints to narrow down the issue
- Time to debug: Hours to days

**Analysis:**

- Debugging time: Multiple deploy cycles
- Mean time to resolution: 4-8 hours
- Customer impact: High (prolonged issues)

#### Approach 2: With Full Observability

```java
// Instrumented with metrics, logs, and traces
public class CheckoutService {
    private final MetricsCollector metrics;
    private final StructuredLogger logger;
    private final DistributedTracer tracer;

    public Order checkout(Cart cart) {
        // Start trace
        Span span = tracer.startSpan("checkout");
        span.setTag("cart_id", cart.getId());
        span.setTag("items_count", cart.getItems().size());

        // Add context for logs
        logger.addContext("trace_id", span.traceId);
        logger.addContext("cart_id", cart.getId());

        long startTime = System.nanoTime();

        try {
            // Validate cart
            Span validateSpan = tracer.startChildSpan("validate_cart");
            validateCart(cart);
            tracer.finishSpan();

            // Charge payment
            Span paymentSpan = tracer.startChildSpan("charge_payment");
            chargePayment(cart);
            tracer.finishSpan();
            logger.info("Payment charged", Map.of("amount", cart.getTotal()));

            // Create order
            Span orderSpan = tracer.startChildSpan("create_order");
            Order order = createOrder(cart);
            tracer.finishSpan();

            // Send email
            Span emailSpan = tracer.startChildSpan("send_email");
            sendEmail(cart);
            tracer.finishSpan();

            // Record success metrics
            long duration = System.nanoTime() - startTime;
            metrics.recordRequest(duration / 1_000_000_000.0);
            logger.info("Checkout completed", Map.of("order_id", order.getId()));

            return order;

        } catch (Exception e) {
            metrics.recordError((System.nanoTime() - startTime) / 1_000_000_000.0);
            logger.error("Checkout failed", e);
            throw e;
        } finally {
            tracer.finishSpan();
            logger.clearContext();
        }
    }
}
```

**What you can see:**

1. **Metrics:** P99 latency is 3s (P50 is 100ms) - it's a tail latency issue!
2. **Logs:** Search by trace_id shows payment gateway timeouts
3. **Traces:** Visualization shows 95% of time spent in charge_payment span

**Analysis:**

- Debugging time: 5 minutes (query dashboards)
- Root cause: Payment gateway timeout for 1% of requests
- Solution: Add timeout + retry logic
- Time to resolution: 30 minutes

#### Performance Comparison

| Scenario                    | No Observability           | With Observability       | Improvement |
|-----------------------------|----------------------------|--------------------------|-------------|
| Time to detect issue        | 30+ minutes (user reports) | 30 seconds (alert fired) | 60x faster  |
| Time to identify root cause | 2-4 hours (trial/error)    | 5 minutes (query traces) | 24x faster  |
| Deploy cycles needed        | 3-5 deploys                | 1 deploy                 | 3-5x fewer  |
| Customer impact             | High (hours)               | Low (minutes)            | 10x better  |

**Your calculation:** If you have 10 incidents per month, observability saves approximately _____ engineering hours.

#### Why Does Observability Work?

**Key insight to understand:**

Without observability, debugging is like:

- Finding a needle in a haystack... blindfolded... in the dark

With observability, you can:

1. **Metrics:** Quickly identify that there IS a problem (P99 spike)
2. **Logs:** Find specific failing requests (search by error, user_id, trace_id)
3. **Traces:** See exactly where time is spent (payment span = 2.9s of 3s total)

```
No observability:
"Users say checkout is slow" → Try things → Deploy → Wait → Repeat

With observability:
"Users say checkout is slow"
  → Check metrics (P99 = 3s)
  → Check traces (payment = 2.9s)
  → Check logs (gateway timeout)
  → Fix + deploy → Done
```

**Why can you skip trial-and-error?**

- Traces show the exact bottleneck (no guessing)
- Logs provide context (what failed and why)
- Metrics prove the fix worked (P99 drops to 200ms)

**After implementing, explain in your own words:**

<div class="learner-section" markdown>

- How do the three pillars work together? <span class="fill-in">[Your answer]</span>
- What questions can you answer with each type? <span class="fill-in">[Your answer]</span>
- Why is context propagation (trace_id) important? <span class="fill-in">[Your answer]</span>

</div>

---

## Common Misconceptions

!!! warning "Misconception 1: Average latency is a good SLO metric"
    Average (P50) latency hides tail behavior. If your checkout API takes 100ms for 99% of requests and 5 seconds for 1%, the average might look fine at ~150ms. The affected users (the 1% with 5-second waits) are exactly the ones most likely to abandon their cart. Always track **P99 or P99.9** for user-facing latency SLOs, not averages.

!!! warning "Misconception 2: More logs always means better observability"
    Excessive logging degrades performance (I/O cost), increases storage costs, and — most critically — buries signal in noise. When every line of code emits a DEBUG log, finding the relevant ERROR in an incident becomes harder, not easier. The goal is structured logs at appropriate levels: ERROR and WARN always on, INFO for significant business events, DEBUG only on demand (via dynamic log-level configuration).

!!! warning "Misconception 3: Trace sampling breaks observability"
    Sampling 1–10% of traces does not mean you miss 90–99% of problems. Most errors and latency issues affect a category of requests, not a single one. Sampling captures representative examples of every type of behavior. The cases sampling genuinely misses are extremely rare one-off events — for those, **error logs** (which are never sampled) provide the context you need.

---

## Decision Framework

<div class="learner-section" markdown>

**Your task:** Build decision trees for observability patterns.

### Question 1: Metrics vs Logs vs Traces?

Answer after implementation:

**Use Metrics when:**

- Aggregated data: <span class="fill-in">[Count of requests, average latency]</span>
- Alerting: <span class="fill-in">[Need to trigger alerts on thresholds]</span>
- Dashboards: <span class="fill-in">[Time-series graphs and trends]</span>
- Low overhead: <span class="fill-in">[Constant memory usage]</span>

**Use Logs when:**

- Debugging: <span class="fill-in">[Need full context of what happened]</span>
- Audit trail: <span class="fill-in">[Who did what and when]</span>
- Irregular events: <span class="fill-in">[Errors, exceptions, business events]</span>
- Flexible queries: <span class="fill-in">[Search by any field]</span>

**Use Traces when:**

- Distributed systems: <span class="fill-in">[Request flows across services]</span>
- Performance analysis: <span class="fill-in">[Find bottlenecks in request path]</span>
- Dependencies: <span class="fill-in">[Understand service relationships]</span>
- Latency debugging: <span class="fill-in">[Which service is slow]</span>

### Question 2: When to add observability?

**During development:**

- Add metrics: <span class="fill-in">[Core business operations, API endpoints]</span>
- Add logs: <span class="fill-in">[Error paths, state changes, important decisions]</span>
- Add traces: <span class="fill-in">[Service boundaries, external calls]</span>

**During incidents:**

- Add metrics: <span class="fill-in">[Missing visibility into problem area]</span>
- Add logs: <span class="fill-in">[Need more context for debugging]</span>
- Add traces: <span class="fill-in">[Don't understand request flow]</span>

### Question 3: How much is too much?

**Metrics:**

- Too few: <span class="fill-in">[Can't understand system health]</span>
- Too many: <span class="fill-in">[Storage costs, query performance]</span>
- Sweet spot: <span class="fill-in">[RED/USE for each service, key business metrics]</span>

**Logs:**

- Too few: <span class="fill-in">[Can't debug issues]</span>
- Too many: <span class="fill-in">[Storage costs, signal-to-noise ratio]</span>
- Sweet spot: <span class="fill-in">[WARN+ always, INFO for business events, DEBUG on-demand]</span>

**Traces:**

- Too few: <span class="fill-in">[Can't understand distributed requests]</span>
- Too many: <span class="fill-in">[Storage costs, performance impact]</span>
- Sweet spot: <span class="fill-in">[Sample based on traffic volume (1-10%)]</span>

### Your Decision Tree

Build this after solving practice scenarios:
```mermaid
flowchart LR
    Start["Observability Pattern Selection"]

    Q1{"What are you trying to understand?"}
    Start --> Q1
    N2["Metrics<br/>(RED/USE)"]
    Q1 -->|"System health"| N2
    N3["Logs + Traces"]
    Q1 -->|"Why something failed"| N3
    N4["Traces + Metrics"]
    Q1 -->|"Performance bottleneck"| N4
    N5["Metrics + Logs"]
    Q1 -->|"Business analytics"| N5
    Q6{"What's the cardinality?"}
    Start --> Q6
    N7["Metric labels"]
    Q6 -->|"Low (< 100 unique values)"| N7
    N8["Logs with indexing"]
    Q6 -->|"Medium (100-10K)"| N8
    N9["Sampling + traces"]
    Q6 -->|"High (> 10K)"| N9
    Q10{"What's the query pattern?"}
    Start --> Q10
    N11["Metrics"]
    Q10 -->|"Time-series aggregation"| N11
    N12["Logs"]
    Q10 -->|"Full-text search"| N12
    N13["Traces"]
    Q10 -->|"Causality tracking"| N13
    N14["Logs + Traces"]
    Q10 -->|"Ad-hoc exploration"| N14
    Q15{"What's the retention need?"}
    Start --> Q15
    N16["Metrics<br/>(short retention)"]
    Q15 -->|"Real-time only"| N16
    N17["Logs + Traces"]
    Q15 -->|"Debugging (days)"| N17
    N18["Logs<br/>(archive)"]
    Q15 -->|"Compliance (years)"| N18
    N19["Metrics<br/>(downsampled)"]
    Q15 -->|"Trending (months)"| N19
```

</div>

---

## Practice

<div class="learner-section" markdown>

### Scenario 1: Monitor E-commerce API

**Requirements:**

- REST API: /checkout, /orders, /products
- Traffic: 10K requests/sec peak
- SLO: 99.9% availability, P99 < 500ms
- Team of 5 engineers on-call rotation

**Your observability design:**

Metrics to collect:

1. <span class="fill-in">[What RED metrics for each endpoint?]</span>
2. <span class="fill-in">[What USE metrics for infrastructure?]</span>
3. <span class="fill-in">[What business metrics (orders, revenue)?]</span>

Logs to capture:

1. <span class="fill-in">[What should be logged at each level?]</span>
2. <span class="fill-in">[How to add trace IDs to logs?]</span>
3. <span class="fill-in">[What fields in structured logs?]</span>

Traces to implement:

1. <span class="fill-in">[Where to start/end spans?]</span>
2. <span class="fill-in">[What sampling rate?]</span>
3. <span class="fill-in">[What tags on spans?]</span>

Alerts to configure:

1. <span class="fill-in">[SLO violation alerts?]</span>
2. <span class="fill-in">[Error budget alerts?]</span>
3. <span class="fill-in">[Runbook for each alert?]</span>

**Failure modes:**

- What happens if the metrics collection agent on a host crashes and stops shipping data — how does your alerting distinguish a genuine outage from a silent monitoring gap? <span class="fill-in">[Fill in]</span>
- How does your design behave when the centralised log aggregation pipeline falls behind under peak traffic and log records are dropped or delayed by several minutes? <span class="fill-in">[Fill in]</span>

### Scenario 2: Debug Distributed Payment System

**Context:**

- Payment service calls: auth-service, fraud-service, payment-gateway
- Users reporting "payment hangs" (no error, just slow)
- Happens for 1% of requests
- Can't reproduce in staging

**Your debugging approach:**

Using traces:

1. <span class="fill-in">[What would you look for first?]</span>
2. <span class="fill-in">[How to find the slow requests?]</span>
3. <span class="fill-in">[How to identify which service is slow?]</span>

Using logs:

1. <span class="fill-in">[What log queries would you run?]</span>
2. <span class="fill-in">[How to correlate logs across services?]</span>
3. <span class="fill-in">[What might you be missing?]</span>

Using metrics:

1. <span class="fill-in">[What metrics would show the problem?]</span>
2. <span class="fill-in">[How to narrow down the time window?]</span>
3. <span class="fill-in">[What percentiles to examine?]</span>

Root cause:

- How would you prove your hypothesis? <span class="fill-in">[Your answer]</span>
- What would you change to fix it? <span class="fill-in">[Your answer]</span>
- What observability would you add? <span class="fill-in">[Your answer]</span>

**Failure modes:**

- What happens if the distributed tracing pipeline is overloaded and begins dropping spans for the 1% of slow requests you are trying to investigate? <span class="fill-in">[Fill in]</span>
- How does your debugging approach change when the fraud-service does not propagate the trace context header, breaking the trace chain mid-flight? <span class="fill-in">[Fill in]</span>

### Scenario 3: Capacity Planning for Growth

**Situation:**

- Current: 1K requests/sec
- Growth: Expected 10K requests/sec in 6 months
- Need to plan infrastructure scaling

**Your analysis approach:**

Metrics analysis:

1. <span class="fill-in">[What metrics show current capacity?]</span>
2. <span class="fill-in">[How to extrapolate to 10x load?]</span>
3. <span class="fill-in">[What are the bottlenecks?]</span>

Load testing:

1. <span class="fill-in">[What metrics to collect during load test?]</span>
2. <span class="fill-in">[How to identify breaking points?]</span>
3. <span class="fill-in">[What percentiles matter most?]</span>

Planning:

1. <span class="fill-in">[When will current capacity be exceeded?]</span>
2. <span class="fill-in">[What needs to be scaled (compute, db, cache)?]</span>
3. <span class="fill-in">[What are the cost implications?]</span>

**Failure modes:**

- What happens if a key capacity metric (e.g. database connection pool utilisation) has been incorrectly instrumented and is reporting lower than actual values, causing your projections to underestimate the scaling deadline? <span class="fill-in">[Fill in]</span>
- How does your capacity planning model behave when traffic growth is non-linear — for example, a sudden viral event causes 10x load in one week rather than six months? <span class="fill-in">[Fill in]</span>

</div>

---

## Test Your Understanding

Answer these questions without looking at your implementation. They are designed to probe understanding, not recall.

1. **Your P50 latency is 80ms and your P99 latency is 4 seconds. Users are not complaining. Should you be concerned? Why or why not?**

    ??? success "Rubric"
        A complete answer addresses: (1) yes, be concerned — P99 at 4 seconds means roughly 1% of requests are extremely slow, which at 10K req/sec is 100 slow requests every second affecting real users; (2) absence of user complaints is a lagging indicator, not evidence of no problem — users often abandon rather than complain; (3) the 50x gap between P50 and P99 indicates the tail is pathological, pointing to a specific cause such as GC pauses, lock contention, or slow database queries on particular code paths worth investigating immediately.

2. **An engineer proposes adding a `user_id` label to your HTTP request counter metric so the team can track per-user request rates. What is the likely consequence in a system with 500K monthly active users, and what alternative approach achieves the same monitoring goal?**

    ??? success "Rubric"
        A complete answer addresses: (1) adding a high-cardinality label like `user_id` creates 500K unique time series for a single metric — this causes metric store memory explosion, slow queries, and can crash Prometheus or equivalent systems; (2) the alternative is to keep the counter without `user_id` for aggregate monitoring, and instead use logs (with user_id as a structured field, queried on demand) or a separate per-user analytics store for the per-user tracking use case; (3) a good answer also mentions that cardinality should be bounded at label design time, not discovered reactively.

3. **A distributed trace shows the total request took 2 seconds, but the sum of all child span durations adds up to only 800ms. What does this gap represent, and what is the most likely cause?**

    ??? success "Rubric"
        A complete answer addresses: (1) the 1.2-second gap represents time not accounted for by any instrumented span — this is uninstrumented work; (2) the most common causes are: network transit time between services that is not wrapped in a span, serialisation/deserialisation overhead, connection pool wait time, or middleware/framework code that runs outside the application spans; (3) the fix is to add spans around the suspected uninstrumented regions — connection acquisition, request queuing, and serialisation are the first places to look.

4. **Your SLO is 99.9% availability over 30 days. You have consumed 80% of your error budget in the first 10 days. Should you stop all feature work immediately? Describe how you would use the error budget to make a rational engineering decision.**

    ??? success "Rubric"
        A complete answer addresses: (1) stopping all feature work immediately is an overreaction if the incident causing the burn is already resolved — the budget is consumed but the rate may now be nominal; (2) the rational response is to calculate the current burn rate: if you consumed 80% in 10 days you had a roughly 8x normal burn rate; assess whether that rate is ongoing or was a one-time event; (3) if the burn rate remains elevated, halt risky deployments and prioritise reliability work; if it has returned to baseline, proceed with feature work but with heightened caution and smaller deployment batches, accepting you have little remaining budget for the month.

5. **Design decision: You are adding observability to a new microservice that processes financial transactions. For each of the three pillars (metrics, logs, traces), name one specific piece of data you would capture and explain why that specific data item is more valuable than alternatives you considered.**

    ??? success "Rubric"
        A complete answer addresses: (1) for metrics — transaction success/failure rate by payment provider (not just overall error rate) because provider-specific failure rates reveal integration issues that aggregate counts hide; (2) for logs — a structured log entry at transaction completion containing amount, currency, provider, outcome, and correlation ID (not a raw string log) because structured fields enable forensic queries like "all failed USD transactions over $1000 in the last hour"; (3) for traces — a span wrapping each external payment gateway call with the gateway's response code as a tag, because this makes gateway latency and error rates visible without requiring log parsing, and lets you correlate slow transactions to a specific provider call.

---

## Connected Topics

!!! info "Where this topic connects"

    - **08. Rate Limiting** — rate limiting decisions depend on real-time counters that are part of the observability surface; SLO alerting fires when rate-limited traffic exceeds thresholds → [08. Rate Limiting](08-rate-limiting.md)
    - **09. Load Balancing** — load balancer health checks and request-rate metrics are core observability signals; SLO dashboards typically visualise per-backend latency → [09. Load Balancing](09-load-balancing.md)
    - **15. Distributed Transactions** — distributed tracing is essential for diagnosing saga failures; correlation IDs span both observability and transaction coordination → [15. Distributed Transactions](15-distributed-transactions.md)

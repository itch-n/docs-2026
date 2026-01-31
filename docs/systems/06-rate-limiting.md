# 05. Rate Limiting

> Protecting APIs from abuse and ensuring fair resource usage

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing different rate limiting algorithms, explain them simply.

**Prompts to guide you:**

1. **What is rate limiting in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why do we need rate limiting?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy for token bucket:**
   - Example: "Token bucket is like a piggy bank where..."
   - Your analogy: _[Fill in]_

4. **What is the token bucket algorithm in one sentence?**
   - Your answer: _[Fill in after implementation]_

5. **How is leaky bucket different from token bucket?**
   - Your answer: _[Fill in after implementation]_

6. **Real-world analogy for leaky bucket:**
   - Example: "Leaky bucket is like a water tower where..."
   - Your analogy: _[Fill in]_

7. **What is sliding window algorithm in one sentence?**
   - Your answer: _[Fill in after implementation]_

8. **When would you use fixed window vs sliding window?**
   - Your answer: _[Fill in after implementation]_

---

## Core Implementation

### Part 1: Token Bucket Algorithm

**Your task:** Implement token bucket rate limiter with refill mechanism.

```java
import java.util.*;
import java.util.concurrent.*;

/**
 * Token Bucket: Tokens refill at constant rate, burst traffic allowed
 *
 * Key principles:
 * - Bucket holds tokens (capacity)
 * - Tokens refill at fixed rate
 * - Request consumes token(s)
 * - Allows burst traffic up to capacity
 */

public class TokenBucketRateLimiter {

    private final int capacity;          // Max tokens in bucket
    private final double refillRate;     // Tokens per second
    private double tokens;               // Current tokens
    private long lastRefillTime;         // Last refill timestamp

    /**
     * Initialize token bucket
     *
     * @param capacity Maximum tokens (burst size)
     * @param refillRate Tokens added per second
     *
     * TODO: Initialize bucket
     * - Set capacity and refill rate
     * - Start with full bucket
     * - Record current time
     */
    public TokenBucketRateLimiter(int capacity, double refillRate) {
        // TODO: Store capacity and refill rate

        // TODO: Initialize tokens to capacity (bucket starts full)

        // TODO: Record current time in milliseconds

        this.capacity = 0; // Replace
        this.refillRate = 0; // Replace
    }

    /**
     * Attempt to acquire a token
     *
     * @return true if token acquired, false if rate limited
     *
     * TODO: Implement token acquisition
     * 1. Refill tokens based on time elapsed
     * 2. Check if token available
     * 3. Consume token if available
     *
     * Hint: tokens_to_add = time_elapsed * refill_rate
     */
    public synchronized boolean tryAcquire() {
        // TODO: Calculate time elapsed since last refill

        // TODO: Calculate new tokens to add
        // tokens_to_add = elapsed_seconds * refillRate

        // TODO: Add tokens but cap at capacity
        // tokens = Math.min(tokens + tokens_to_add, capacity)

        // TODO: Update lastRefillTime to now

        // TODO: If tokens >= 1, consume one and return true

        // TODO: Otherwise return false (rate limited)

        return false; // Replace
    }

    /**
     * Try to acquire multiple tokens (for weighted rate limiting)
     *
     * @param tokensNeeded Number of tokens to acquire
     * @return true if acquired, false if insufficient tokens
     *
     * TODO: Implement multi-token acquisition
     * - Refill tokens first
     * - Check if enough tokens available
     * - Consume requested tokens
     */
    public synchronized boolean tryAcquire(int tokensNeeded) {
        // TODO: Refill tokens (same as tryAcquire())

        // TODO: Check if tokens >= tokensNeeded

        // TODO: If yes, consume tokensNeeded and return true

        // TODO: Otherwise return false

        return false; // Replace
    }

    /**
     * Get current token count (for monitoring)
     */
    public synchronized double getTokens() {
        refill();
        return tokens;
    }

    /**
     * Refill tokens based on elapsed time
     *
     * TODO: Extract refill logic
     * - Calculate elapsed time
     * - Add tokens
     * - Cap at capacity
     */
    private void refill() {
        // TODO: Implement refill logic
    }
}
```

### Part 2: Leaky Bucket Algorithm

**Your task:** Implement leaky bucket rate limiter with constant outflow.

```java
/**
 * Leaky Bucket: Requests leak out at constant rate
 *
 * Key principles:
 * - Bucket holds pending requests (queue)
 * - Requests leak out at fixed rate
 * - Smooths burst traffic
 * - Rejects requests if bucket full
 */

public class LeakyBucketRateLimiter {

    private final int capacity;           // Max queue size
    private final double leakRate;        // Requests per second
    private final Queue<Long> bucket;     // Request timestamps
    private long lastLeakTime;

    /**
     * Initialize leaky bucket
     *
     * @param capacity Maximum pending requests
     * @param leakRate Requests processed per second
     *
     * TODO: Initialize bucket
     * - Create queue with capacity
     * - Set leak rate
     * - Record current time
     */
    public LeakyBucketRateLimiter(int capacity, double leakRate) {
        // TODO: Store capacity and leak rate

        // TODO: Initialize queue (LinkedList)

        // TODO: Record current time

        this.capacity = 0; // Replace
        this.leakRate = 0; // Replace
        this.bucket = null; // Replace
    }

    /**
     * Try to add request to bucket
     *
     * @return true if accepted, false if bucket full
     *
     * TODO: Implement request acceptance
     * 1. Leak out old requests
     * 2. Check if space available
     * 3. Add current request
     *
     * Hint: Remove requests older than (current_time - 1/leak_rate)
     */
    public synchronized boolean tryAcquire() {
        // TODO: Leak out processed requests
        // Calculate how many requests should have leaked
        // requests_to_leak = elapsed_seconds * leakRate

        // TODO: Remove that many requests from queue

        // TODO: If bucket.size() < capacity, add current time and return true

        // TODO: Otherwise return false (bucket full)

        return false; // Replace
    }

    /**
     * Leak out processed requests
     *
     * TODO: Remove requests based on elapsed time
     * - Calculate requests that should leak
     * - Remove from queue
     * - Update lastLeakTime
     */
    private void leak() {
        // TODO: Calculate elapsed time

        // TODO: Calculate requests to leak
        // requestsToLeak = elapsed_seconds * leakRate

        // TODO: Poll requestsToLeak items from queue

        // TODO: Update lastLeakTime
    }

    /**
     * Get current bucket size (for monitoring)
     */
    public synchronized int getQueueSize() {
        leak();
        return bucket.size();
    }
}
```

### Part 3: Fixed Window Algorithm

**Your task:** Implement simple fixed window counter.

```java
/**
 * Fixed Window: Count requests in fixed time windows
 *
 * Key principles:
 * - Reset counter at window boundaries
 * - Simple and memory efficient
 * - Can allow 2x rate at window boundaries
 */

public class FixedWindowRateLimiter {

    private final int maxRequests;        // Max requests per window
    private final long windowSizeMs;      // Window size in milliseconds
    private int counter;                  // Requests in current window
    private long windowStart;             // Current window start time

    /**
     * Initialize fixed window rate limiter
     *
     * @param maxRequests Maximum requests per window
     * @param windowSizeMs Window size in milliseconds
     *
     * TODO: Initialize window
     * - Set max requests and window size
     * - Start counter at 0
     * - Record window start time
     */
    public FixedWindowRateLimiter(int maxRequests, long windowSizeMs) {
        // TODO: Store maxRequests and windowSizeMs

        // TODO: Initialize counter to 0

        // TODO: Set windowStart to current time

        this.maxRequests = 0; // Replace
        this.windowSizeMs = 0; // Replace
    }

    /**
     * Try to acquire permission
     *
     * @return true if allowed, false if rate limited
     *
     * TODO: Implement fixed window logic
     * 1. Check if window expired (reset if needed)
     * 2. Check if under limit
     * 3. Increment counter
     */
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();

        // TODO: If (now - windowStart) >= windowSizeMs:
        //   Reset counter to 0
        //   Set windowStart to now

        // TODO: If counter < maxRequests:
        //   Increment counter
        //   Return true

        // TODO: Otherwise return false

        return false; // Replace
    }

    /**
     * Get current window stats (for monitoring)
     */
    public synchronized WindowStats getStats() {
        return new WindowStats(counter, maxRequests, windowStart);
    }

    static class WindowStats {
        int current;
        int max;
        long windowStart;

        WindowStats(int current, int max, long windowStart) {
            this.current = current;
            this.max = max;
            this.windowStart = windowStart;
        }
    }
}
```

### Part 4: Sliding Window Log Algorithm

**Your task:** Implement sliding window with request log.

```java
/**
 * Sliding Window Log: Track individual request timestamps
 *
 * Key principles:
 * - Store timestamp of each request
 * - Count requests in sliding window
 * - More accurate than fixed window
 * - Higher memory usage
 */

public class SlidingWindowLogRateLimiter {

    private final int maxRequests;        // Max requests per window
    private final long windowSizeMs;      // Window size in milliseconds
    private final Queue<Long> requestLog; // Request timestamps

    /**
     * Initialize sliding window log
     *
     * @param maxRequests Maximum requests per window
     * @param windowSizeMs Window size in milliseconds
     *
     * TODO: Initialize log
     * - Set max requests and window size
     * - Create queue for timestamps
     */
    public SlidingWindowLogRateLimiter(int maxRequests, long windowSizeMs) {
        // TODO: Store maxRequests and windowSizeMs

        // TODO: Initialize LinkedList for request log

        this.maxRequests = 0; // Replace
        this.windowSizeMs = 0; // Replace
        this.requestLog = null; // Replace
    }

    /**
     * Try to acquire permission
     *
     * @return true if allowed, false if rate limited
     *
     * TODO: Implement sliding window logic
     * 1. Remove old requests outside window
     * 2. Check if under limit
     * 3. Add current timestamp
     *
     * Hint: Remove requests older than (now - windowSizeMs)
     */
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();

        // TODO: Remove timestamps older than (now - windowSizeMs)
        // while (!requestLog.isEmpty() && requestLog.peek() <= now - windowSizeMs)

        // TODO: If requestLog.size() < maxRequests:
        //   Add current timestamp
        //   Return true

        // TODO: Otherwise return false

        return false; // Replace
    }

    /**
     * Get current request count (for monitoring)
     */
    public synchronized int getCurrentCount() {
        long now = System.currentTimeMillis();
        while (!requestLog.isEmpty() && requestLog.peek() <= now - windowSizeMs) {
            requestLog.poll();
        }
        return requestLog.size();
    }
}
```

### Part 5: Sliding Window Counter (Hybrid)

**Your task:** Implement memory-efficient sliding window counter.

```java
/**
 * Sliding Window Counter: Hybrid of fixed window and sliding window
 *
 * Key principles:
 * - Two counters: current and previous window
 * - Weighted average based on time in window
 * - More accurate than fixed window
 * - Less memory than sliding log
 */

public class SlidingWindowCounterRateLimiter {

    private final int maxRequests;
    private final long windowSizeMs;
    private int currentWindowCount;
    private int previousWindowCount;
    private long currentWindowStart;

    /**
     * Initialize sliding window counter
     *
     * @param maxRequests Maximum requests per window
     * @param windowSizeMs Window size in milliseconds
     *
     * TODO: Initialize counters
     * - Set max requests and window size
     * - Initialize both counters
     * - Record window start
     */
    public SlidingWindowCounterRateLimiter(int maxRequests, long windowSizeMs) {
        // TODO: Store maxRequests and windowSizeMs

        // TODO: Initialize currentWindowCount to 0

        // TODO: Initialize previousWindowCount to 0

        // TODO: Set currentWindowStart to now

        this.maxRequests = 0; // Replace
        this.windowSizeMs = 0; // Replace
    }

    /**
     * Try to acquire permission
     *
     * @return true if allowed, false if rate limited
     *
     * TODO: Implement sliding window counter
     * 1. Rotate windows if needed
     * 2. Calculate weighted count
     * 3. Check against limit
     *
     * Formula:
     * weighted_count = previous_count * (1 - elapsed_ratio) + current_count
     * where elapsed_ratio = time_in_window / window_size
     */
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();

        // TODO: Check if window expired
        // If (now - currentWindowStart) >= windowSizeMs:
        //   previousWindowCount = currentWindowCount
        //   currentWindowCount = 0
        //   currentWindowStart = now

        // TODO: Calculate time elapsed in current window
        // elapsedRatio = (now - currentWindowStart) / windowSizeMs

        // TODO: Calculate weighted count
        // weightedCount = previousWindowCount * (1 - elapsedRatio) + currentWindowCount

        // TODO: If weightedCount < maxRequests:
        //   currentWindowCount++
        //   Return true

        // TODO: Otherwise return false

        return false; // Replace
    }

    /**
     * Get estimated current count (for monitoring)
     */
    public synchronized double getEstimatedCount() {
        long now = System.currentTimeMillis();
        double elapsedRatio = (double)(now - currentWindowStart) / windowSizeMs;
        return previousWindowCount * (1 - elapsedRatio) + currentWindowCount;
    }
}
```

---

## Client Code

```java
import java.util.concurrent.*;

public class RateLimitingClient {

    public static void main(String[] args) throws Exception {
        testTokenBucket();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testLeakyBucket();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testFixedWindow();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testSlidingWindowLog();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testSlidingWindowCounter();
        System.out.println("\n" + "=".repeat(50) + "\n");
        compareBurstTraffic();
    }

    static void testTokenBucket() {
        System.out.println("=== Token Bucket Test ===\n");

        // 10 tokens capacity, 2 tokens/second refill
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 2.0);

        // Test: Burst traffic
        System.out.println("Burst: 15 rapid requests");
        int allowed = 0;
        for (int i = 0; i < 15; i++) {
            if (limiter.tryAcquire()) {
                allowed++;
            }
        }
        System.out.println("Allowed: " + allowed + "/15");
        System.out.println("Remaining tokens: " + limiter.getTokens());

        // Test: Wait and retry
        System.out.println("\nWait 2 seconds for refill...");
        sleep(2000);
        System.out.println("Tokens after refill: " + limiter.getTokens());

        // Test: Weighted request (costs 3 tokens)
        System.out.println("\nWeighted request (3 tokens):");
        boolean acquired = limiter.tryAcquire(3);
        System.out.println("Acquired: " + acquired);
        System.out.println("Remaining tokens: " + limiter.getTokens());
    }

    static void testLeakyBucket() {
        System.out.println("=== Leaky Bucket Test ===\n");

        // 5 capacity, 1 request/second leak rate
        LeakyBucketRateLimiter limiter = new LeakyBucketRateLimiter(5, 1.0);

        // Test: Fill bucket
        System.out.println("Fill bucket with 5 requests");
        int allowed = 0;
        for (int i = 0; i < 5; i++) {
            if (limiter.tryAcquire()) {
                allowed++;
            }
        }
        System.out.println("Allowed: " + allowed + "/5");
        System.out.println("Queue size: " + limiter.getQueueSize());

        // Test: Overflow
        System.out.println("\nTry 3 more requests (should overflow)");
        int overflow = 0;
        for (int i = 0; i < 3; i++) {
            if (limiter.tryAcquire()) {
                overflow++;
            }
        }
        System.out.println("Allowed: " + overflow + "/3");

        // Test: Wait and retry
        System.out.println("\nWait 2 seconds for leak...");
        sleep(2000);
        System.out.println("Queue size after leak: " + limiter.getQueueSize());
    }

    static void testFixedWindow() {
        System.out.println("=== Fixed Window Test ===\n");

        // 5 requests per 2 second window
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter(5, 2000);

        // Test: Fill window
        System.out.println("Make 5 requests (should all succeed)");
        testRequests(limiter, 5);

        // Test: Overflow
        System.out.println("\nMake 3 more requests (should fail)");
        testRequests(limiter, 3);

        // Test: Window boundary
        System.out.println("\nWait for window reset...");
        sleep(2100);
        System.out.println("Make 5 requests in new window");
        testRequests(limiter, 5);
    }

    static void testSlidingWindowLog() {
        System.out.println("=== Sliding Window Log Test ===\n");

        // 5 requests per 2 second window
        SlidingWindowLogRateLimiter limiter = new SlidingWindowLogRateLimiter(5, 2000);

        // Test: Fill window
        System.out.println("Make 5 requests");
        testRequests(limiter, 5);
        System.out.println("Current count: " + limiter.getCurrentCount());

        // Test: Wait partial window
        System.out.println("\nWait 1 second (half window)...");
        sleep(1000);
        System.out.println("Current count: " + limiter.getCurrentCount());

        // Test: Make more requests
        System.out.println("\nMake 3 more requests");
        testRequests(limiter, 3);
    }

    static void testSlidingWindowCounter() {
        System.out.println("=== Sliding Window Counter Test ===\n");

        // 5 requests per 2 second window
        SlidingWindowCounterRateLimiter limiter = new SlidingWindowCounterRateLimiter(5, 2000);

        // Test: Fill window
        System.out.println("Make 5 requests");
        testRequests(limiter, 5);
        System.out.println("Estimated count: " + limiter.getEstimatedCount());

        // Test: Wait partial window
        System.out.println("\nWait 1 second...");
        sleep(1000);
        System.out.println("Estimated count: " + limiter.getEstimatedCount());

        // Test: Make more requests
        System.out.println("\nMake 3 more requests");
        testRequests(limiter, 3);
        System.out.println("Estimated count: " + limiter.getEstimatedCount());
    }

    static void compareBurstTraffic() {
        System.out.println("=== Burst Traffic Comparison ===\n");

        TokenBucketRateLimiter tokenBucket = new TokenBucketRateLimiter(10, 2.0);
        LeakyBucketRateLimiter leakyBucket = new LeakyBucketRateLimiter(10, 2.0);
        FixedWindowRateLimiter fixedWindow = new FixedWindowRateLimiter(10, 5000);

        // Simulate burst of 20 requests
        System.out.println("Sending 20 rapid requests...");

        int tokenAllowed = 0, leakyAllowed = 0, fixedAllowed = 0;
        for (int i = 0; i < 20; i++) {
            if (tokenBucket.tryAcquire()) tokenAllowed++;
            if (leakyBucket.tryAcquire()) leakyAllowed++;
            if (fixedWindow.tryAcquire()) fixedAllowed++;
        }

        System.out.println("Token Bucket allowed: " + tokenAllowed + "/20");
        System.out.println("Leaky Bucket allowed: " + leakyAllowed + "/20");
        System.out.println("Fixed Window allowed: " + fixedAllowed + "/20");
    }

    static void testRequests(FixedWindowRateLimiter limiter, int count) {
        int allowed = 0;
        for (int i = 0; i < count; i++) {
            if (limiter.tryAcquire()) allowed++;
        }
        System.out.println("Allowed: " + allowed + "/" + count);
    }

    static void testRequests(SlidingWindowLogRateLimiter limiter, int count) {
        int allowed = 0;
        for (int i = 0; i < count; i++) {
            if (limiter.tryAcquire()) allowed++;
        }
        System.out.println("Allowed: " + allowed + "/" + count);
    }

    static void testRequests(SlidingWindowCounterRateLimiter limiter, int count) {
        int allowed = 0;
        for (int i = 0; i < count; i++) {
            if (limiter.tryAcquire()) allowed++;
        }
        System.out.println("Allowed: " + allowed + "/" + count);
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

---

## Decision Framework

**Questions to answer after implementation:**

### 1. Algorithm Selection

**When to use Token Bucket?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Leaky Bucket?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Fixed Window?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Sliding Window?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

### 2. Trade-offs

**Token Bucket:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Leaky Bucket:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Fixed Window:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Sliding Window:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

### 3. Your Decision Tree

Build your decision tree after practicing:

```
What is your priority?
├─ Allow burst traffic → ?
├─ Smooth traffic flow → ?
├─ Simple and memory efficient → ?
└─ Accurate rate limiting → ?
```

### 4. Kill Switch - Don't use when:

**Token Bucket:**
1. _[When does token bucket fail? Fill in]_
2. _[Another failure case]_

**Leaky Bucket:**
1. _[When does leaky bucket fail? Fill in]_
2. _[Another failure case]_

**Sliding Window:**
1. _[When does sliding window fail? Fill in]_
2. _[Another failure case]_

### 5. Rule of Three - Alternatives

For each scenario, identify alternatives and compare:

**Scenario: Public API with burst traffic**
1. Option A: _[Fill in]_
2. Option B: _[Fill in]_
3. Option C: _[Fill in]_

---

## Practice

### Scenario 1: Rate limit public API

**Requirements:**
- Public REST API
- Need to allow burst traffic
- 100 requests per minute per user
- Premium users get 1000 requests per minute

**Your design:**
- Which algorithm would you choose? _[Fill in]_
- Why? _[Fill in]_
- How to handle different user tiers? _[Fill in]_
- How to handle distributed servers? _[Fill in]_

### Scenario 2: Rate limit login attempts

**Requirements:**
- Prevent brute force attacks
- 5 login attempts per minute
- Smooth out retry attempts
- Block for 15 minutes after limit

**Your design:**
- Which algorithm would you choose? _[Fill in]_
- Why? _[Fill in]_
- How to implement blocking? _[Fill in]_
- How to handle false positives? _[Fill in]_

### Scenario 3: Rate limit microservice calls

**Requirements:**
- Service A calls Service B
- Protect Service B from overload
- Service B can handle 1000 req/sec
- Need graceful degradation

**Your design:**
- Which algorithm would you choose? _[Fill in]_
- Why? _[Fill in]_
- How to handle backpressure? _[Fill in]_
- Circuit breaker integration? _[Fill in]_

---

## Review Checklist

- [ ] Token bucket implemented with refill mechanism
- [ ] Leaky bucket implemented with constant outflow
- [ ] Fixed window implemented with reset logic
- [ ] Sliding window log implemented with timestamp tracking
- [ ] Sliding window counter implemented with weighted average
- [ ] Understand when to use each algorithm
- [ ] Can explain trade-offs between algorithms
- [ ] Built decision tree for algorithm selection
- [ ] Completed practice scenarios

---

**Next:** [07. Load Balancing →](07-load-balancing.md)

**Back:** [05. Security Patterns ←](05-security-patterns.md)

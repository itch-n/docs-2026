# Rate Limiting

> Protecting APIs from abuse and ensuring fair resource usage

---

## Learning Objectives

By the end of this topic you will be able to:

- Implement the token bucket algorithm including refill logic, burst capacity enforcement, and multi-token acquisition
- Implement the leaky bucket algorithm with a constant-drain queue and explain why it produces smoother traffic than token bucket
- Compare fixed window, sliding window log, and sliding window counter algorithms across accuracy, memory usage, and boundary-case behaviour
- Identify the fixed-window double-rate boundary attack and explain how sliding or token-bucket approaches mitigate it
- Choose the appropriate rate-limiting algorithm given constraints on memory, burst tolerance, and accuracy
- Explain common implementation bugs such as elapsed-time unit mismatches and fractional-time loss in leak calculations

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing different rate limiting algorithms, explain them simply.

**Prompts to guide you:**

1. **What is rate limiting in one sentence?**
    - Your answer: <span class="fill-in">Rate limiting is a ___ that works by ___</span>

2. **Why do we need rate limiting?**
    - Your answer: <span class="fill-in">Without rate limiting, a single client could ___, which would cause ___</span>

3. **Real-world analogy for token bucket:**
    - Example: "Token bucket is like a piggy bank where..."
    - Your analogy: <span class="fill-in">Think about how you'd ration coins for a vending machine — you earn coins over time, and you can save up a handful before spending them in a burst...</span>

4. **What is the token bucket algorithm in one sentence?**
    - Your answer: <span class="fill-in">Token bucket is a ___ that allows ___ by ___</span>

5. **How is leaky bucket different from token bucket?**
    - Your answer: <span class="fill-in">Leaky bucket ___ while token bucket ___; the key difference in output is ___</span>

6. **Real-world analogy for leaky bucket:**
    - Example: "Leaky bucket is like a water tower where..."
    - Your analogy: <span class="fill-in">Think about how a dripping faucet smooths out a gush of water — no matter how fast water arrives, it only leaves at one constant rate...</span>

7. **What is sliding window algorithm in one sentence?**
    - Your answer: <span class="fill-in">Sliding window is a ___ that fixes the boundary problem by ___</span>

8. **When would you use fixed window vs sliding window?**
    - Your answer: <span class="fill-in">Fixed window is better when ___ because ___; sliding window is better when ___</span>

</div>

---

## Quick Quiz (Do BEFORE implementing)

!!! tip "How to use this section"
    Complete your predictions now, before reading further. You will revisit and verify each answer after running the benchmark (or completing the implementation).

<div class="learner-section" markdown>

**Your task:** Test your intuition about rate limiting algorithms. Answer these, then verify after implementation.

### Algorithm Understanding Predictions

1. **Token bucket with 10 tokens, 2 tokens/sec refill:**
    - How many requests can burst immediately? <span class="fill-in">[Your guess]</span>
    - After waiting 5 seconds, how many tokens? <span class="fill-in">[Your guess]</span>
    - Verified after implementation: <span class="fill-in">[Actual]</span>

2. **Leaky bucket vs token bucket for 100 req/sec:**
    - Which allows bursts? <span class="fill-in">[Token/Leaky]</span>
    - Which smooths traffic? <span class="fill-in">[Token/Leaky]</span>
    - Verified: <span class="fill-in">[Fill in]</span>

3. **Fixed window: 10 req/min starting at 12:00:00:**
    - 9 requests at 12:00:58
    - 9 requests at 12:01:01
    - Total allowed in 3 seconds: <span class="fill-in">[Your guess: 9? 10? 18?]</span>
    - Why is this a problem? <span class="fill-in">[Fill in]</span>
    - Verified: <span class="fill-in">[Actual behavior]</span>

### Scenario Predictions

**Scenario 1:** API needs to handle traffic spikes but prevent abuse

- **Best algorithm?** <span class="fill-in">[Token bucket/Leaky bucket/Fixed window/Sliding window]</span>
- **Why?** <span class="fill-in">[Your reasoning]</span>
- **Verified after implementation:** <span class="fill-in">[Fill in]</span>

**Scenario 2:** Login system needs steady rate limiting (no bursts)

- **Best algorithm?** <span class="fill-in">[Token bucket/Leaky bucket/Fixed window/Sliding window]</span>
- **Why?** <span class="fill-in">[Your reasoning]</span>
- **Verified after implementation:** <span class="fill-in">[Fill in]</span>

**Scenario 3:** Rate limit 100 requests per minute with 1 million users

- **Fixed window memory:** <span class="fill-in">[Estimate: bytes per user]</span>
- **Sliding window log memory:** <span class="fill-in">[Estimate: bytes per user]</span>
- **Which is more memory efficient?** <span class="fill-in">[Your guess]</span>
- **Verified:** <span class="fill-in">[Fill in calculations]</span>

### Refill Logic Quiz

**Token bucket refills 5 tokens/second, last refill at T=0:**

1. **At T=2 seconds, how many tokens added?** <span class="fill-in">[Your calculation]</span>
2. **At T=2.5 seconds, how many tokens added?** <span class="fill-in">[Your calculation]</span>
3. **If capacity is 10 and current tokens = 8:**
    - After 1 second: <span class="fill-in">[tokens available?]</span>
    - After 5 seconds: <span class="fill-in">[tokens available? can exceed capacity?]</span>

**Verify these calculations after implementation!**

### Trade-off Quiz

**Question:** Why would you choose fixed window over sliding window?

- Your answer: <span class="fill-in">[Fill in before implementation]</span>
- Verified answer: <span class="fill-in">[Fill in after learning]</span>

**Question:** Token bucket allows 100 burst requests. Is this ALWAYS good?

- [ ] Yes, bursts are always beneficial
- [ ] No, depends on backend capacity
- [ ] No, depends on abuse prevention needs
- [ ] It depends on the use case

**Verify after implementation:** <span class="fill-in">[Which one(s) and why?]</span>


</div>

---

## Case Studies: Rate Limiting in the Wild

### Stripe API: The Leaky Bucket for Smooth Traffic

- **Pattern:** Leaky Bucket algorithm.
- **How it works:** Stripe's API processes requests at a fixed, steady rate, smoothing out bursts. Imagine a bucket with
  a small hole in the bottom. Incoming requests fill the bucket, and they are processed at the rate water "leaks" out.
  If requests arrive too quickly, the bucket overflows, and requests are rejected with a `429 Too Many Requests` status
  code.
- **Key Takeaway:** The Leaky Bucket algorithm is excellent for services that require a predictable, stable load and
  want to prevent being overwhelmed by sudden bursts of traffic. It enforces a very smooth processing rate.

### GitHub API: The Token Bucket for Flexibility

- **Pattern:** Token Bucket algorithm.
- **How it works:** GitHub provides each API client with a "bucket" of tokens (e.g., 5,000) that refills over time (
  e.g., per hour). Each API request consumes one token. This allows clients to make short, intense bursts of requests as
  long as they have tokens remaining. The API's HTTP response headers (`X-RateLimit-Limit`, `X-RateLimit-Remaining`,
  `X-RateLimit-Reset`) clearly communicate the client's current status, allowing applications to gracefully back off.
- **Key Takeaway:** The Token Bucket algorithm provides more flexibility than the Leaky Bucket, as it permits bursty
  traffic. This is user-friendly for clients who may have legitimate reasons to make many requests in a short period, as
  long as their average rate remains within the limit.

### Cloudflare: Fixed Windows for DDoS Protection

- **Pattern:** Fixed Window Counters for security.
- **How it works:** As a security company, Cloudflare's priority is blocking malicious traffic. They use simple,
  high-performance fixed window counters at their edge locations. They might have a rule like: "Block any IP that makes
  more than 100 requests in any 10-second window." While this can be cheated by a sophisticated attacker who distributes
  requests across windows, it is extremely effective at stopping basic brute-force attacks and application-layer DDoS
  attempts with very little overhead.
- **Key Takeaway:** For security and DDoS mitigation, the raw performance and simplicity of a fixed window counter can
  be the most effective choice. The goal isn't perfect fairness but rather the rapid identification and blocking of
  abusive behavior.

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
        // TODO: Track state

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

        // TODO: Implement iteration/conditional logic

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

        // TODO: Implement iteration/conditional logic

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

!!! warning "Debugging Challenge — Time Unit Mismatch in Token Refill"

    The refill calculation below produces wildly incorrect token counts. Find and fix the bug.

    ```java
    private void refill() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;

        double tokensToAdd = elapsed * refillRate;  // refillRate is tokens/sec

        tokens = Math.min(capacity, tokens + tokensToAdd);
        lastRefillTime = now;
    }
    ```

    Manual trace: `elapsed = 2000` (2 seconds), `refillRate = 2.0` tokens/sec.
    What value does `tokensToAdd` compute to? What should it be?

    ??? success "Answer"

        **Bug:** `elapsed` is in milliseconds but `refillRate` is in tokens/second. Multiplying them gives `2000 × 2.0 = 4000` tokens — 2000x too many. A 2-second wait would overflow any reasonable capacity limit.

        **Fix:** Divide elapsed by 1000.0 before multiplying:

        ```java
        double elapsedSeconds = elapsed / 1000.0;
        double tokensToAdd = elapsedSeconds * refillRate;
        ```

        This is one of the most common rate-limiting bugs in production code. Always be explicit about the time unit of every variable that holds a duration.

---

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
        // TODO: Track state

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
        // requests_to_leak = elapsed_seconds * leakRate

        // TODO: Remove that many requests from queue

        // TODO: Implement iteration/conditional logic

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

---

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
        // TODO: Track state

        // TODO: Initialize counter to 0

        // TODO: Track state

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

        // TODO: Implement iteration/conditional logic

        // TODO: Implement iteration/conditional logic

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

---

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
        // TODO: Track state

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

        // TODO: Implement iteration/conditional logic

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

---

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
        // TODO: Track state

        // TODO: Initialize currentWindowCount to 0

        // TODO: Initialize previousWindowCount to 0

        // TODO: Track state

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

        // TODO: Calculate time elapsed in current window
        // elapsedRatio = (now - currentWindowStart) / windowSizeMs

        // TODO: Calculate weighted count
        // weightedCount = previousWindowCount * (1 - elapsedRatio) + currentWindowCount

        // TODO: Implement iteration/conditional logic

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

## Leaky Bucket Debugging Exercises

**Your task:** Find and fix bugs in broken leaky bucket implementation. This tests your understanding of the algorithm.

### Challenge: Broken Leaky Bucket (Time Unit Bug + Fractional Time Loss)

```java
public class BrokenLeakyBucket {
    private final int capacity;
    private final double leakRate;  // requests per second
    private final Queue<Long> bucket;
    private long lastLeakTime;

    public boolean tryAcquire() {
        leak();

        if (bucket.size() < capacity) {
            bucket.offer(System.currentTimeMillis());
            return true;
        }

        return false;
    }

    private void leak() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastLeakTime;

        int requestsToLeak = (int)(elapsed * leakRate);

        for (int i = 0; i < requestsToLeak && !bucket.isEmpty(); i++) {
            bucket.poll();
        }

        lastLeakTime = now;    }
}
```

**Your debugging:**

- **Bug 1:** Calculation `elapsed * leakRate`
    - elapsed = 2000ms, leakRate = 2 req/sec
    - Current: 2000 × 2 = <span class="fill-in">[What?]</span>
    - Expected: <span class="fill-in">[How many requests should leak in 2 seconds?]</span>
    - Fix: <span class="fill-in">[Correct formula]</span>
- **Bug 2:** `lastLeakTime = now` happens every call
    - Scenario: elapsed = 500ms, leakRate = 2 req/sec
    - requests to leak = 1 request
    - We update lastLeakTime, "losing" the remaining 0.5 requests worth of time
    - Fix: <span class="fill-in">[Should we account for fractional leak time?]</span>

??? success "Answer"

    **Bug 1:** Same as token bucket - multiplying milliseconds by rate per second.

    **Fix:**

    ```java
    double elapsedSeconds = elapsed / 1000.0;
    int requestsToLeak = (int)(elapsedSeconds * leakRate);
    ```

    **Bug 2:** Updating `lastLeakTime` every call loses fractional seconds.

    **Better approach:**

    ```java
    private void leak() {
        long now = System.currentTimeMillis();
        double elapsedSeconds = (now - lastLeakTime) / 1000.0;
        int requestsToLeak = (int)(elapsedSeconds * leakRate);

        for (int i = 0; i < requestsToLeak && !bucket.isEmpty(); i++) {
            bucket.poll();
        }

        // Only update time for the requests we actually leaked
        if (requestsToLeak > 0) {
            lastLeakTime += (long)((requestsToLeak / leakRate) * 1000);
        }
    }
    ```

    This preserves fractional time for more accurate leaking.

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found time unit conversion errors (ms vs seconds)
- [ ] Identified race conditions in concurrent access
- [ ] Caught off-by-one errors in counters
- [ ] Understood memory implications of different approaches
- [ ] Fixed incorrect weight formulas in sliding window
- [ ] Recognized fractional time loss in leak calculations

**Common rate limiting bugs you discovered:**

1. <span class="fill-in">[List the patterns you noticed]</span>
2. <span class="fill-in">[Fill in]</span>
3. <span class="fill-in">[Fill in]</span>

**Testing strategies you learned:**

<div class="learner-section" markdown>

- How would you test for race conditions? <span class="fill-in">[Your answer]</span>
- How would you test boundary conditions? <span class="fill-in">[Your answer]</span>
- How would you test time-based calculations? <span class="fill-in">[Your answer]</span>

</div>

!!! info "Loop back"
    Return to the Quick Quiz now and fill in your verified answers.

---

## Before/After: Why This Pattern Matters

**Your task:** Compare naive vs optimized rate limiting approaches.

### Example: Protecting an API Endpoint

**Problem:** Prevent API abuse while allowing legitimate traffic.

#### Approach 1: No Rate Limiting

```java
// Naive approach - No protection
public class NoRateLimiting {
    public Response handleRequest(Request req) {
        return processRequest(req); // Process every request
    }
}
```

**Analysis:**

- Time: O(1) per request
- Space: O(1)
- Protection: NONE - vulnerable to abuse
- Result: Server overload, DDoS attacks succeed
- For 1 million malicious requests: Server crashes

#### Approach 2: Fixed Window Rate Limiter

```java
// Simple fixed window - Memory efficient
public class FixedWindowRateLimiter {
    private int counter = 0;
    private long windowStart = System.currentTimeMillis();
    private final int maxRequests = 100;
    private final long windowMs = 60000; // 1 minute

    public boolean tryAcquire() {
        long now = System.currentTimeMillis();

        // Reset window if expired
        if (now - windowStart >= windowMs) {
            counter = 0;
            windowStart = now;
        }

        // Check limit
        if (counter < maxRequests) {
            counter++;
            return true;
        }
        return false;
    }
}
```

**Analysis:**

- Time: O(1) per request
- Space: O(1) - only 3 variables per user
- Protection: Good - limits to 100 req/min
- Issue: Allows 200 requests in 2 seconds at window boundary
- For 1 million users: ~12 bytes × 1M = ~12 MB

#### Approach 3: Token Bucket (Better for Bursts)

```java
// Token bucket - Allows controlled bursts
public class TokenBucketRateLimiter {
    private double tokens;
    private long lastRefillTime;
    private final int capacity = 100;
    private final double refillRate = 10.0; // tokens/sec

    public boolean tryAcquire() {
        refill();

        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        double elapsed = (now - lastRefillTime) / 1000.0;
        tokens = Math.min(capacity, tokens + elapsed * refillRate);
        lastRefillTime = now;
    }
}
```

**Analysis:**

- Time: O(1) per request
- Space: O(1) - 3 variables per user
- Protection: Excellent - smooth rate limiting + burst capacity
- Flexibility: Can handle legitimate traffic spikes
- For 1 million users: ~16 bytes × 1M = ~16 MB

#### Performance Comparison

| Scenario        | No Limiting | Fixed Window   | Token Bucket | Sliding Window Log          |
|-----------------|-------------|----------------|--------------|-----------------------------|
| Memory per user | 0 bytes     | 12 bytes       | 16 bytes     | ~800 bytes (100 timestamps) |
| 1M users memory | 0 MB        | 12 MB          | 16 MB        | 800 MB                      |
| Prevents abuse  | No          | Yes            | Yes          | Yes                         |
| Allows bursts   | N/A         | No             | Yes          | No                          |
| Accurate rate   | N/A         | Boundary issue | Accurate     | Most accurate               |
| CPU per request | Low         | Low            | Low          | Medium (cleanup)            |

**Your calculation:** For 10 million users with sliding window log tracking 1000 requests each:

- Memory needed: <span class="fill-in">[Calculate after implementation]</span>
- Why this might be impractical: <span class="fill-in">[Fill in]</span>

#### Boundary Attack Demonstration

**Fixed Window Problem:**

```
Window 1: 12:00:00 - 12:01:00 (limit: 100 req)
Window 2: 12:01:00 - 12:02:00 (limit: 100 req)

Attack pattern:

- 12:00:59 → 100 requests (allowed, fills window 1)
- 12:01:01 → 100 requests (allowed, new window 2)
- Total: 200 requests in 2 seconds!
```

**Token Bucket Solution:**

```
Capacity: 100 tokens, Refill: 10 tokens/sec

Attack pattern:

- 12:00:59 → 100 requests (bucket empties)
- 12:01:01 → 20 requests (only ~20 tokens refilled)
- Total: 120 requests max (controlled burst)
```

**After implementing, explain in your own words:**

- Why does fixed window allow double rate at boundaries? <span class="fill-in">[Fill in]</span>
- How does token bucket prevent this? <span class="fill-in">[Fill in]</span>
- When is fixed window "good enough"? <span class="fill-in">[Fill in]</span>

---

## Common Misconceptions

!!! warning "Token bucket and leaky bucket are interchangeable"
    They enforce the same average rate but behave very differently under burst traffic. Token bucket allows a client to spend accumulated tokens instantly — a full bucket of 100 means 100 requests right now. Leaky bucket drains at a constant rate regardless of arrival pattern; burst traffic simply overflows. Use token bucket when legitimate bursty clients exist; use leaky bucket when downstream services need steady load.

!!! warning "Sliding window log is always better than fixed window"
    Sliding window log is more accurate but stores one timestamp per request. For 1 million users each allowed 1,000 req/min, that is potentially 1 billion timestamps (~8 GB) just for the rate-limiter state. Fixed window uses only a counter and a timestamp per user — orders of magnitude less memory. Accuracy must be weighed against memory cost for your specific scale.

!!! warning "Rate limiting only needs to run once per user per minute"
    Rate limiting must run on every incoming request, not periodically. Checking "did this user exceed their limit in the last minute?" is different from "does this specific request arrive within quota?" The check must happen synchronously at request time, before work is done, and it must be atomic to avoid race conditions in distributed deployments.

---

## Decision Framework

<div class="learner-section" markdown>

**Questions to answer after implementation:**

### 1. Algorithm Selection

**When to use Token Bucket?**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key factors: <span class="fill-in">[Fill in]</span>

**When to use Leaky Bucket?**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key factors: <span class="fill-in">[Fill in]</span>

**When to use Fixed Window?**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key factors: <span class="fill-in">[Fill in]</span>

**When to use Sliding Window?**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key factors: <span class="fill-in">[Fill in]</span>

### 2. Trade-offs

**Token Bucket:**

- Pros: <span class="fill-in">[Fill in after understanding]</span>
- Cons: <span class="fill-in">[Fill in after understanding]</span>

**Leaky Bucket:**

- Pros: <span class="fill-in">[Fill in after understanding]</span>
- Cons: <span class="fill-in">[Fill in after understanding]</span>

**Fixed Window:**

- Pros: <span class="fill-in">[Fill in after understanding]</span>
- Cons: <span class="fill-in">[Fill in after understanding]</span>

**Sliding Window:**

- Pros: <span class="fill-in">[Fill in after understanding]</span>
- Cons: <span class="fill-in">[Fill in after understanding]</span>

### 3. Your Decision Tree

Build your decision tree after practicing:
```mermaid
flowchart LR
    Start["What is your priority?"]

    N1["?"]
    Start -->|"Allow burst traffic"| N1
    N2["?"]
    Start -->|"Smooth traffic flow"| N2
    N3["?"]
    Start -->|"Simple and memory efficient"| N3
    N4["?"]
    Start -->|"Accurate rate limiting"| N4
```

</div>

---

## Practice

<div class="learner-section" markdown>

### Scenario 1: Rate limit public API

**Requirements:**

- Public REST API
- Need to allow burst traffic
- 100 requests per minute per user
- Premium users get 1000 requests per minute

**Your design:**

- Which algorithm would you choose? <span class="fill-in">[Fill in]</span>
- Why? <span class="fill-in">[Fill in]</span>
- How to handle different user tiers? <span class="fill-in">[Fill in]</span>
- How to handle distributed servers? <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if the shared Redis store used for rate limit counters becomes unavailable across all API servers? <span class="fill-in">[Fill in]</span>
- How does your design behave when a premium user's quota misconfiguration allows unlimited requests and floods the backend? <span class="fill-in">[Fill in]</span>

### Scenario 2: Rate limit login attempts

**Requirements:**

- Prevent brute force attacks
- 5 login attempts per minute
- Smooth out retry attempts
- Block for 15 minutes after limit

**Your design:**

- Which algorithm would you choose? <span class="fill-in">[Fill in]</span>
- Why? <span class="fill-in">[Fill in]</span>
- How to implement blocking? <span class="fill-in">[Fill in]</span>
- How to handle false positives? <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if the rate limiter state is stored only in memory and the server restarts, resetting all block counters mid-attack? <span class="fill-in">[Fill in]</span>
- How does your design behave when a legitimate user is behind a shared NAT and their IP is blocked due to another user's brute-force attempts? <span class="fill-in">[Fill in]</span>

### Scenario 3: Rate limit microservice calls

**Requirements:**

- Service A calls Service B
- Protect Service B from overload
- Service B can handle 1000 req/sec
- Need graceful degradation

**Your design:**

- Which algorithm would you choose? <span class="fill-in">[Fill in]</span>
- Why? <span class="fill-in">[Fill in]</span>
- How to handle backpressure? <span class="fill-in">[Fill in]</span>
- Circuit breaker integration? <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if Service B's rate limiter is bypassed because Service A retries aggressively after 429 responses without exponential backoff? <span class="fill-in">[Fill in]</span>
- How does your design behave when a thundering herd of retries from multiple service instances overwhelms Service B after a brief outage? <span class="fill-in">[Fill in]</span>

</div>

---

## Test Your Understanding

Answer these without referring to your notes or implementation.

1. A token bucket has capacity 100 and refill rate 10 tokens/sec. At T=0 the bucket is full. A client sends 100 requests instantly, then sends 1 more request 500ms later. Is that 101st request allowed? Show your calculation.

    ??? success "Rubric"
        A complete answer addresses: (1) at T=500ms, elapsed time is 0.5 seconds, so tokens refilled = 0.5 × 10 = 5 tokens, (2) the bucket therefore has 5 tokens, so the 101st request (which costs 1 token) is allowed — 4 tokens remain, (3) the key insight is that the bucket refills continuously from the last refill timestamp, not only at discrete intervals.

2. Explain the fixed-window boundary attack in concrete terms. If a limit is 10 requests/minute, what is the maximum number of requests an attacker can send in any 2-second window using this attack?

    ??? success "Rubric"
        A complete answer addresses: (1) the attacker sends 10 requests at 11:59:59 (fills window 1) and 10 more at 12:00:01 (starts fresh window 2), achieving 20 requests in 2 seconds — double the stated rate, (2) this works because each window resets independently with no memory of the previous window's traffic, (3) token bucket or sliding window log prevents this because they track actual elapsed time rather than resetting on a fixed schedule.

3. You need to rate-limit 50 million users at 100 requests/minute each. Compare the memory footprint of fixed window, sliding window log, and token bucket approaches for this scale. Which would you choose and why?

    ??? success "Rubric"
        A complete answer addresses: (1) fixed window: ~12 bytes per user × 50M = ~600 MB total; token bucket: ~16 bytes per user × 50M = ~800 MB total; sliding window log: up to 100 timestamps × 8 bytes × 50M = ~40 GB — impractical, (2) token bucket is the best choice: it is memory-efficient, handles bursts fairly, and avoids the boundary attack that affects fixed window, (3) sliding window log is eliminated at this scale because storing per-request timestamps for 50 million users exhausts available memory.

4. Your leaky bucket implementation passes unit tests but behaves incorrectly under load in production. The bucket appears to allow far more requests than configured. What time-unit bug should you look for first?

    ??? success "Rubric"
        A complete answer addresses: (1) the most common bug is a units mismatch: computing elapsed time in milliseconds but using a drain rate configured in seconds (or vice versa), causing the bucket to drain 1000x slower or faster than intended, (2) a secondary bug is integer truncation when computing fractional drain amounts — if elapsed time is stored as a long in milliseconds and the rate is tokens/second, the division must be floating-point, (3) the fix is to enforce consistent time units throughout (use nanoseconds or milliseconds everywhere) and use double arithmetic for the drain calculation.

5. A colleague proposes using a single in-memory counter for rate limiting on a horizontally-scaled API with 10 servers. What is the problem with this approach, and what storage backend would you recommend instead?

    ??? success "Rubric"
        A complete answer addresses: (1) each server maintains its own in-memory counter, so a client sending 10 requests distributed across 10 servers consumes only 1 request per counter — effectively bypassing the limit by a factor of N servers, (2) the correct approach is a shared atomic counter in a centralised store such as Redis, using INCR and EXPIRE commands which are atomic on a single-threaded Redis server, (3) Redis also enables sliding window counter via sorted sets and supports Lua scripts for atomic multi-step operations, making it the standard backend for distributed rate limiting.

---

## Connected Topics

!!! info "Where this topic connects"

    - **06. API Design** — rate limiting is applied at the API layer to protect endpoints; the rate limit unit (per user, per IP, per API key) aligns with the API authentication scheme → [06. API Design](06-api-design.md)
    - **09. Load Balancing** — rate limiting is commonly implemented at the load balancer or API gateway level, before requests reach backend services → [09. Load Balancing](09-load-balancing.md)
    - **15. Observability** — rate limit hit rate and remaining quota are key metrics; tracking them with counters reveals abuse patterns and helps tune thresholds → [15. Observability](15-observability.md)

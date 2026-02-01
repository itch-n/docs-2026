# 10. Full System Designs

> End-to-end system design examples with architecture, implementation, and trade-offs

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing full system designs, explain them simply.

**Prompts to guide you:**

1. **What is a URL shortener in one sentence?**
    - Your answer: _[Fill in after implementation]_

2. **Why do we need URL shorteners?**
    - Your answer: _[Fill in after implementation]_

3. **Real-world analogy for URL shortening:**
    - Example: "URL shortening is like giving your house a nickname instead of..."
    - Your analogy: _[Fill in]_

4. **What is a Twitter feed in one sentence?**
    - Your answer: _[Fill in after implementation]_

5. **How does Twitter handle millions of tweets?**
    - Your answer: _[Fill in after implementation]_

6. **Real-world analogy for news feed:**
    - Example: "A news feed is like a personalized newspaper where..."
    - Your analogy: _[Fill in]_

7. **What is Pastebin in one sentence?**
    - Your answer: _[Fill in after implementation]_

8. **When would you use Pastebin instead of email?**
    - Your answer: _[Fill in after implementation]_

---

## Quick Quiz (Do BEFORE implementing)

**Your task:** Test your system design intuition without looking at implementation details. Answer these, then verify after building the systems.

### Capacity Planning Predictions

1. **URL Shortener with 100M URLs:**
    - Storage per URL (approx): _[Your guess: __ bytes]_
    - Total storage needed: _[Your guess: __ GB]_
    - Verified after implementation: _[Actual: __]_

2. **Twitter with 100M daily active users:**
    - Tweets per second (avg): _[Your guess: __]_
    - Peak load multiplier: _[Your guess: __x average]_
    - Read/Write ratio: _[Your guess: __ reads per write]_
    - Verified: _[Actual]_

3. **Cache hit ratio calculation:**
    - If 80% cache hit rate, how many database queries per 1000 requests?
    - Your answer: _[Fill in]_
    - Verified: _[Fill in after implementation]_

### Architecture Predictions

**Scenario 1:** URL Shortener design

- **Database: SQL or NoSQL?** _[Your choice and why]_
- **Sharding key:** _[What would you shard by?]_
- **Cache strategy:** _[Write-through/Write-back/Cache-aside - which one?]_
- **Bottleneck prediction:** _[Where will the bottleneck be?]_

**Scenario 2:** Twitter Feed generation

- **Fan-out approach for regular user (100 followers):** _[Push/Pull/Hybrid?]_
- **Fan-out approach for celebrity (10M followers):** _[Push/Pull/Hybrid?]_
- **Why different?** _[Fill in your reasoning]_

**Scenario 3:** Pastebin storage

- **Store in database or object storage?** _[Your choice]_
- **Why?** _[Fill in reasoning]_
- **How to handle 10MB paste vs 100 byte paste?** _[Different strategies?]_

### Trade-off Quiz

**Question:** Which is more important for URL shortener: availability or consistency?

- Your answer: _[Fill in before implementation]_
- Reasoning: _[Why?]_
- Verified answer: _[Fill in after learning]_

**Question:** For Twitter feed, what happens if you choose fan-out on write (push)?

- Advantage: _[Fill in]_
- Disadvantage: _[Fill in]_
- When it breaks down: _[Fill in]_

**Question:** What's the main bottleneck in each system?

| System | Bottleneck (CPU/Memory/Disk/Network) | Why? |
|--------|--------------------------------------|------|
| URL Shortener | _[Your guess]_ | _[Explain]_ |
| Twitter Feed | _[Your guess]_ | _[Explain]_ |
| Pastebin | _[Your guess]_ | _[Explain]_ |

Verify after implementation: _[Were you correct?]_

---

## Before/After: Monolithic vs Distributed Design

**Your task:** Compare monolithic and well-designed distributed approaches to understand system design principles.

### Example: Twitter Feed System

#### Approach 1: Monolithic Design (Doesn't Scale)

```
┌─────────────────────────────┐
│   Single Application Server │
│                              │
│  ┌────────────────────┐     │
│  │ Web Server         │     │
│  │ Tweet Handler      │     │
│  │ Timeline Generator │     │
│  │ User Management    │     │
│  └────────────────────┘     │
└──────────┬──────────────────┘
           │
           v
   ┌───────────────┐
   │ Single MySQL  │
   │   Database    │
   └───────────────┘
```

**Problems:**

- Single point of failure - Server crashes → entire system down
- No scalability - Can't handle 100M users
- Blocking operations - Timeline generation blocks tweet posting
- Database bottleneck - All operations hit single database
- No caching - Every request queries database
- No geographic distribution - High latency for global users

**Performance:**

- Max concurrent users: ~10,000
- Timeline generation: 2-3 seconds (queries all followed users)
- Downtime risk: High (single failure point)
- Deployment: Requires full system restart

#### Approach 2: Distributed Design (Production-Ready)

```
┌──────────────┐
│ Load Balancer│
└──────┬───────┘
       │
       ├───────────────┬────────────────┐
       │               │                │
       v               v                v
┌──────────┐    ┌──────────┐    ┌──────────┐
│  Write   │    │   Read   │    │ Timeline │
│ Service  │    │  Service │    │ Service  │
└────┬─────┘    └─────┬────┘    └────┬─────┘
     │                │              │
     v                v              v
┌────────────────────────────────────────┐
│        Message Queue (Kafka)            │
└────────────────────────────────────────┘
     │                │              │
     v                v              v
┌──────────┐    ┌──────────┐    ┌──────────┐
│ Tweet DB │    │ User DB  │    │ Timeline │
│(sharded) │    │(sharded) │    │  Cache   │
└──────────┘    └──────────┘    └──────────┘
```

**Solutions:**

- Separation of concerns - Write/Read services scaled independently
- Database sharding - Distributes data across multiple servers
- Caching layer - Timeline cache reduces database queries by 80%
- Async processing - Message queue decouples operations
- Geographic distribution - CDN and regional data centers
- Multiple replicas - High availability and fault tolerance

**Performance:**

- Max concurrent users: Millions (horizontal scaling)
- Timeline generation: 50-100ms (pre-computed cache)
- Downtime risk: Very low (redundancy at every layer)
- Deployment: Rolling updates, zero downtime

#### Performance Comparison

| Metric | Monolithic | Distributed | Improvement |
|--------|-----------|-------------|-------------|
| Concurrent Users | 10K | 10M+ | 1000x |
| Timeline Load Time | 2-3 sec | 50-100ms | 20-30x |
| Database Load | 100% | 20% (cache) | 5x reduction |
| Availability | 95% (1 failure point) | 99.99% | 52x less downtime |
| Deployment Risk | Full outage | Zero downtime | Risk eliminated |
| Cost per User | High (over-provision) | Low (scale on demand) | 10x reduction |

### Key System Design Principles Illustrated

**1. Separation of Concerns**

- Monolithic: All logic in one place
- Distributed: Write/Read/Timeline services independent
- **Why it matters:** Scale bottlenecks independently

**2. Asynchronous Processing**

- Monolithic: Blocking operations (wait for fanout)
- Distributed: Message queue for async fanout
- **Why it matters:** User doesn't wait for expensive operations

**3. Caching Strategy**

- Monolithic: No cache (query DB every time)
- Distributed: Multi-layer cache (Redis for timelines)
- **Why it matters:** 80% reduction in database load

**4. Database Sharding**

- Monolithic: Single database (vertical scaling limit)
- Distributed: Sharded by user ID (horizontal scaling)
- **Why it matters:** Distribute load across many servers

**5. Load Balancing**

- Monolithic: Single server (traffic limit)
- Distributed: Load balancer + multiple API servers
- **Why it matters:** Handle traffic spikes and failures

**After implementing, answer:**

- _[Which principle had the biggest impact on performance?]_
- _[Which principle was most complex to implement?]_
- _[What trade-offs did distributed design introduce?]_

---

## Core Implementation

### Part 1: URL Shortener System

**Your task:** Design and implement a URL shortening service.

#### Architecture Diagram

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ HTTP
       v
┌──────────────────────┐
│   Load Balancer      │
└──────┬───────────────┘
       │
       v
┌──────────────────────┐
│   API Servers        │
│  (Create/Redirect)   │
└──────┬───────────────┘
       │
       ├──────────────┐
       │              │
       v              v
┌──────────┐   ┌──────────────┐
│  Cache   │   │   Database   │
│ (Redis)  │   │ (sharded)    │
└──────────┘   └──────────────┘
       │              │
       └──────┬───────┘
              │
              v
        ┌──────────┐
        │Analytics │
        │ Service  │
        └──────────┘
```

#### Requirements
- Shorten long URLs to 7-character codes
- Redirect short URL to original
- Track click analytics
- Handle 100M URLs
- Low latency (<100ms)

#### Implementation

```java
import java.util.*;
import java.util.concurrent.*;

/**
 * URL Shortener: Complete system implementation
 *
 * Components:
 * - Base62 encoding for short URLs
 * - Database sharding by hash
 * - Cache for hot URLs
 * - Analytics tracking
 */

public class URLShortener {

    private final DatabaseShards database;
    private final Cache cache;
    private final IDGenerator idGenerator;
    private final Analytics analytics;

    /**
     * Initialize URL shortener system
     *
     * TODO: Initialize all components
     * - Database with sharding
     * - Cache layer
     * - ID generator
     * - Analytics tracker
     */
    public URLShortener(int numShards) {
        // TODO: Initialize database shards

        // TODO: Initialize cache

        // TODO: Initialize ID generator

        // TODO: Initialize analytics

        this.database = null; // Replace
        this.cache = null; // Replace
        this.idGenerator = null; // Replace
        this.analytics = null; // Replace
    }

    /**
     * Shorten URL
     *
     * @param longURL Original URL to shorten
     * @return Short code (7 characters)
     *
     * TODO: Implement URL shortening
     * 1. Generate unique ID
     * 2. Encode ID to base62 (7 chars)
     * 3. Store mapping in database
     * 4. Warm cache
     * 5. Return short code
     */
    public String shortenURL(String longURL) {
        // TODO: Check if URL already exists (optional)
        // String existing = database.findByLongURL(longURL)

        // TODO: Generate unique ID
        // long id = idGenerator.next()

        // TODO: Encode to base62
        // String shortCode = encodeBase62(id)

        // TODO: Store in database (shard by hash)
        // database.save(shortCode, longURL)

        // TODO: Warm cache
        // cache.put(shortCode, longURL)

        // TODO: Return short code
        return null; // Replace
    }

    /**
     * Get original URL
     *
     * @param shortCode Short URL code
     * @return Original long URL
     *
     * TODO: Implement URL resolution
     * 1. Check cache first
     * 2. If miss, query database
     * 3. Update cache
     * 4. Track analytics
     * 5. Return URL
     */
    public String getOriginalURL(String shortCode) {
        // TODO: Check cache
        // String url = cache.get(shortCode)
        // if (url != null) return url

        // TODO: Query database (shard by hash)
        // url = database.get(shortCode)

        // TODO: Update cache
        // if (url != null) cache.put(shortCode, url)

        // TODO: Track click analytics (async)
        // analytics.trackClick(shortCode)

        return null; // Replace
    }

    /**
     * Get click analytics
     *
     * TODO: Get analytics for short URL
     */
    public URLAnalytics getAnalytics(String shortCode) {
        return analytics.getStats(shortCode);
    }

    /**
     * Base62 encoding (0-9, a-z, A-Z)
     *
     * TODO: Encode long ID to 7-character string
     * - Use base62 alphabet
     * - Convert number to base62
     * - Pad to 7 characters
     */
    private String encodeBase62(long id) {
        // TODO: Implement base62 encoding
        String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();

        // TODO: Convert id to base62
        // while (id > 0):
        //   sb.append(alphabet.charAt(id % 62))
        //   id /= 62

        // TODO: Pad to 7 characters if needed

        return null; // Replace
    }

    /**
     * Database with sharding
     */
    static class DatabaseShards {
        List<Map<String, URLMapping>> shards;

        public DatabaseShards(int numShards) {
            // TODO: Initialize shards
            this.shards = new ArrayList<>();
            for (int i = 0; i < numShards; i++) {
                shards.add(new ConcurrentHashMap<>());
            }
        }

        /**
         * Save URL mapping
         *
         * TODO: Shard by hash of short code
         */
        public void save(String shortCode, String longURL) {
            // TODO: Get shard index
            int shard = Math.abs(shortCode.hashCode()) % shards.size();

            // TODO: Save mapping
            URLMapping mapping = new URLMapping(shortCode, longURL);
            shards.get(shard).put(shortCode, mapping);
        }

        /**
         * Get URL mapping
         *
         * TODO: Retrieve from correct shard
         */
        public String get(String shortCode) {
            // TODO: Get shard index
            int shard = Math.abs(shortCode.hashCode()) % shards.size();

            // TODO: Get mapping
            URLMapping mapping = shards.get(shard).get(shortCode);
            return mapping != null ? mapping.longURL : null;
        }
    }

    /**
     * LRU Cache for hot URLs
     */
    static class Cache {
        private final Map<String, String> cache;
        private final int capacity;

        public Cache(int capacity) {
            // TODO: Initialize LRU cache
            this.cache = new LinkedHashMap<>(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        public String get(String key) {
            return cache.get(key);
        }

        public void put(String key, String value) {
            // TODO: Implement LRU eviction
            if (cache.size() >= capacity && !cache.containsKey(key)) {
                String firstKey = cache.keySet().iterator().next();
                cache.remove(firstKey);
            }
            cache.put(key, value);
        }
    }

    /**
     * Distributed ID generator (similar to Twitter Snowflake)
     */
    static class IDGenerator {
        private long sequence = 0L;
        private long lastTimestamp = -1L;

        /**
         * Generate unique ID
         *
         * TODO: Generate unique 64-bit ID
         * - Timestamp (41 bits)
         * - Machine ID (10 bits)
         * - Sequence (12 bits)
         */
        public synchronized long next() {
            long timestamp = System.currentTimeMillis();

            // TODO: If same millisecond, increment sequence
            if (timestamp == lastTimestamp) {
                sequence = (sequence + 1) & 4095; // 12 bits
                if (sequence == 0) {
                    // Wait for next millisecond
                    timestamp = waitNextMillis(timestamp);
                }
            } else {
                sequence = 0L;
            }

            lastTimestamp = timestamp;

            // TODO: Combine timestamp, machine ID, and sequence
            // return (timestamp << 22) | (machineId << 12) | sequence;

            return timestamp * 10000 + sequence; // Simplified
        }

        private long waitNextMillis(long current) {
            long timestamp = System.currentTimeMillis();
            while (timestamp <= current) {
                timestamp = System.currentTimeMillis();
            }
            return timestamp;
        }
    }

    /**
     * Analytics tracker
     */
    static class Analytics {
        private final Map<String, URLAnalytics> stats;

        public Analytics() {
            this.stats = new ConcurrentHashMap<>();
        }

        /**
         * Track click
         *
         * TODO: Increment click count (async in production)
         */
        public void trackClick(String shortCode) {
            stats.computeIfAbsent(shortCode, k -> new URLAnalytics())
                 .incrementClicks();
        }

        public URLAnalytics getStats(String shortCode) {
            return stats.getOrDefault(shortCode, new URLAnalytics());
        }
    }

    static class URLMapping {
        String shortCode;
        String longURL;
        long createdAt;

        public URLMapping(String shortCode, String longURL) {
            this.shortCode = shortCode;
            this.longURL = longURL;
            this.createdAt = System.currentTimeMillis();
        }
    }

    static class URLAnalytics {
        private long clicks;
        private long lastAccessed;

        public URLAnalytics() {
            this.clicks = 0;
            this.lastAccessed = 0;
        }

        public synchronized void incrementClicks() {
            this.clicks++;
            this.lastAccessed = System.currentTimeMillis();
        }

        public long getClicks() {
            return clicks;
        }
    }
}
```

### Part 2: Twitter Feed System

**Your task:** Design and implement Twitter's timeline feed.

#### Architecture Diagram

```
┌──────────────┐
│    Users     │
└──────┬───────┘
       │
       v
┌──────────────────────┐
│   API Gateway        │
└──────┬───────────────┘
       │
       ├─────────────┬──────────────┐
       │             │              │
       v             v              v
┌──────────┐  ┌───────────┐  ┌──────────┐
│  Write   │  │   Read    │  │Timeline  │
│ Service  │  │  Service  │  │Generator │
└────┬─────┘  └─────┬─────┘  └────┬─────┘
     │              │              │
     v              v              v
┌─────────────────────────────────────┐
│         Message Queue (Kafka)        │
└─────────────────────────────────────┘
     │              │              │
     v              v              v
┌──────────┐  ┌──────────┐  ┌──────────┐
│ Tweet DB │  │ User DB  │  │Timeline  │
│(sharded) │  │(sharded) │  │  Cache   │
└──────────┘  └──────────┘  └──────────┘
```

#### Requirements
- Post tweets (140 chars)
- Follow/unfollow users
- View home timeline (tweets from followed users)
- Handle 300M users, 100M DAU
- Fan-out tweets to followers

#### Implementation

```java
/**
 * Twitter Feed: Timeline generation system
 *
 * Approaches:
 * 1. Fan-out on write (push): Pre-compute timelines
 * 2. Fan-out on read (pull): Compute timeline on request
 * 3. Hybrid: Push for most, pull for celebrities
 */

public class TwitterFeed {

    private final TweetStore tweetStore;
    private final UserGraph userGraph;
    private final TimelineCache timelineCache;
    private final FanoutService fanoutService;

    /**
     * Initialize Twitter feed system
     *
     * TODO: Initialize components
     * - Tweet storage (sharded)
     * - User follow graph
     * - Timeline cache
     * - Fanout service
     */
    public TwitterFeed() {
        // TODO: Initialize components
        this.tweetStore = null; // Replace
        this.userGraph = null; // Replace
        this.timelineCache = null; // Replace
        this.fanoutService = null; // Replace
    }

    /**
     * Post tweet
     *
     * @param userId User posting tweet
     * @param content Tweet content
     * @return Tweet ID
     *
     * TODO: Implement tweet posting
     * 1. Store tweet
     * 2. Fan out to followers (async)
     * 3. Add to user's timeline
     * 4. Return tweet ID
     */
    public long postTweet(long userId, String content) {
        // TODO: Create tweet
        // Tweet tweet = new Tweet(generateId(), userId, content)

        // TODO: Store tweet in database
        // tweetStore.save(tweet)

        // TODO: Fan out to followers (async)
        // fanoutService.fanoutTweet(tweet)

        // TODO: Return tweet ID
        return 0L; // Replace
    }

    /**
     * Get home timeline
     *
     * @param userId User requesting timeline
     * @param limit Number of tweets
     * @return List of tweets from followed users
     *
     * TODO: Implement timeline retrieval
     * 1. Check cache first
     * 2. If miss, compute timeline
     * 3. Merge from multiple sources
     * 4. Sort by timestamp
     * 5. Return top N
     */
    public List<Tweet> getHomeTimeline(long userId, int limit) {
        // TODO: Check timeline cache
        // List<Tweet> cachedTimeline = timelineCache.get(userId)
        // if (cachedTimeline != null) return cachedTimeline.subList(0, limit)

        // TODO: Get followed users
        // List<Long> following = userGraph.getFollowing(userId)

        // TODO: Fetch recent tweets from each
        // List<Tweet> tweets = new ArrayList<>()
        // for (Long followedUser : following):
        //   tweets.addAll(tweetStore.getUserTweets(followedUser, limit))

        // TODO: Merge and sort by timestamp
        // Collections.sort(tweets, (a, b) -> Long.compare(b.timestamp, a.timestamp))

        // TODO: Cache result

        // TODO: Return top N
        return null; // Replace
    }

    /**
     * Follow user
     *
     * TODO: Add follow relationship
     * - Update user graph
     * - Backfill timeline with followed user's tweets
     */
    public void followUser(long followerId, long followeeId) {
        // TODO: Add edge in user graph
        // userGraph.addFollow(followerId, followeeId)

        // TODO: Backfill timeline (optional)
        // List<Tweet> recentTweets = tweetStore.getUserTweets(followeeId, 100)
        // timelineCache.addTweets(followerId, recentTweets)
    }

    /**
     * Unfollow user
     *
     * TODO: Remove follow relationship
     * - Update user graph
     * - Remove tweets from timeline
     */
    public void unfollowUser(long followerId, long followeeId) {
        // TODO: Remove edge from user graph
        // TODO: Optionally clean timeline cache
    }

    /**
     * Tweet storage (sharded by user ID)
     */
    static class TweetStore {
        Map<Long, List<Tweet>> userTweets; // userId -> tweets

        public TweetStore() {
            this.userTweets = new ConcurrentHashMap<>();
        }

        public void save(Tweet tweet) {
            userTweets.computeIfAbsent(tweet.userId, k -> new ArrayList<>())
                     .add(tweet);
        }

        public List<Tweet> getUserTweets(long userId, int limit) {
            List<Tweet> tweets = userTweets.getOrDefault(userId, new ArrayList<>());
            return tweets.subList(0, Math.min(limit, tweets.size()));
        }
    }

    /**
     * User follow graph
     */
    static class UserGraph {
        Map<Long, Set<Long>> following; // userId -> set of followed users
        Map<Long, Set<Long>> followers; // userId -> set of followers

        public UserGraph() {
            this.following = new ConcurrentHashMap<>();
            this.followers = new ConcurrentHashMap<>();
        }

        public void addFollow(long followerId, long followeeId) {
            following.computeIfAbsent(followerId, k -> new HashSet<>())
                    .add(followeeId);
            followers.computeIfAbsent(followeeId, k -> new HashSet<>())
                    .add(followerId);
        }

        public List<Long> getFollowing(long userId) {
            return new ArrayList<>(following.getOrDefault(userId, new HashSet<>()));
        }

        public List<Long> getFollowers(long userId) {
            return new ArrayList<>(followers.getOrDefault(userId, new HashSet<>()));
        }

        public int getFollowerCount(long userId) {
            return followers.getOrDefault(userId, new HashSet<>()).size();
        }
    }

    /**
     * Timeline cache (pre-computed feeds)
     */
    static class TimelineCache {
        Map<Long, List<Tweet>> timelines; // userId -> timeline
        static final int MAX_TIMELINE_SIZE = 1000;

        public TimelineCache() {
            this.timelines = new ConcurrentHashMap<>();
        }

        public List<Tweet> get(long userId) {
            return timelines.get(userId);
        }

        public void addTweet(long userId, Tweet tweet) {
            timelines.computeIfAbsent(userId, k -> new ArrayList<>())
                    .add(0, tweet); // Add to beginning

            // Trim if too large
            List<Tweet> timeline = timelines.get(userId);
            if (timeline.size() > MAX_TIMELINE_SIZE) {
                timeline.remove(timeline.size() - 1);
            }
        }

        public void addTweets(long userId, List<Tweet> tweets) {
            for (Tweet tweet : tweets) {
                addTweet(userId, tweet);
            }
        }
    }

    /**
     * Fan-out service (push tweets to followers)
     */
    static class FanoutService {
        private final UserGraph userGraph;
        private final TimelineCache timelineCache;

        public FanoutService(UserGraph userGraph, TimelineCache timelineCache) {
            this.userGraph = userGraph;
            this.timelineCache = timelineCache;
        }

        /**
         * Fan out tweet to all followers
         *
         * TODO: Implement fan-out
         * 1. Get all followers
         * 2. Add tweet to each follower's timeline
         * 3. Handle celebrities (skip if too many followers)
         */
        public void fanoutTweet(Tweet tweet) {
            List<Long> followers = userGraph.getFollowers(tweet.userId);

            // TODO: Check if user is celebrity (too many followers)
            if (followers.size() > 10000) {
                // Skip fan-out, use pull-based approach
                System.out.println("Celebrity user, skipping fan-out");
                return;
            }

            // TODO: Push to each follower's timeline
            for (Long followerId : followers) {
                timelineCache.addTweet(followerId, tweet);
            }
        }
    }

    static class Tweet {
        long id;
        long userId;
        String content;
        long timestamp;

        public Tweet(long id, long userId, String content) {
            this.id = id;
            this.userId = userId;
            this.content = content;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return "Tweet{id=" + id + ", user=" + userId + ", content='" + content + "'}";
        }
    }
}
```

### Part 3: Pastebin System

**Your task:** Design and implement a text sharing service.

#### Architecture Diagram

```
┌──────────────┐
│    Users     │
└──────┬───────┘
       │
       v
┌──────────────────────┐
│   CDN / Cache        │
└──────┬───────────────┘
       │
       v
┌──────────────────────┐
│   API Servers        │
└──────┬───────────────┘
       │
       ├──────────────┬──────────────┐
       │              │              │
       v              v              v
┌──────────┐   ┌──────────┐  ┌──────────┐
│Metadata  │   │ Object   │  │  Cache   │
│   DB     │   │ Storage  │  │ (Redis)  │
│(SQL)     │   │  (S3)    │  │          │
└──────────┘   └──────────┘  └──────────┘
```

#### Requirements
- Store text snippets (up to 10MB)
- Generate unique URLs
- Optional expiration
- Support custom URLs
- Syntax highlighting metadata

#### Implementation

```java
/**
 * Pastebin: Text sharing service
 *
 * Features:
 * - Store text with unique URL
 * - Optional expiration
 * - Custom URLs
 * - Access control (public/private)
 */

public class Pastebin {

    private final MetadataStore metadataStore;
    private final ObjectStorage objectStorage;
    private final Cache cache;
    private final IDGenerator idGenerator;

    /**
     * Initialize Pastebin system
     *
     * TODO: Initialize components
     * - Metadata store (SQL)
     * - Object storage (for large pastes)
     * - Cache layer
     * - ID generator
     */
    public Pastebin() {
        // TODO: Initialize components
        this.metadataStore = null; // Replace
        this.objectStorage = null; // Replace
        this.cache = null; // Replace
        this.idGenerator = null; // Replace
    }

    /**
     * Create paste
     *
     * @param content Paste content
     * @param options Paste options (expiration, privacy, etc.)
     * @return Paste URL
     *
     * TODO: Implement paste creation
     * 1. Generate unique key
     * 2. Store content in object storage
     * 3. Store metadata in database
     * 4. Cache if public
     * 5. Return URL
     */
    public String createPaste(String content, PasteOptions options) {
        // TODO: Generate unique key
        // String key = options.customURL != null ?
        //   options.customURL : generateKey()

        // TODO: Store content
        // objectStorage.store(key, content)

        // TODO: Create metadata
        // PasteMetadata metadata = new PasteMetadata(key, content.length(),
        //   options.expirationTime, options.isPrivate)

        // TODO: Store metadata
        // metadataStore.save(metadata)

        // TODO: Cache if public
        // if (!options.isPrivate):
        //   cache.put(key, content)

        // TODO: Return URL
        return null; // Replace
    }

    /**
     * Get paste
     *
     * @param key Paste key
     * @return Paste content
     *
     * TODO: Implement paste retrieval
     * 1. Check if expired
     * 2. Check cache
     * 3. If miss, load from storage
     * 4. Increment view count
     * 5. Return content
     */
    public Paste getPaste(String key) {
        // TODO: Get metadata
        // PasteMetadata metadata = metadataStore.get(key)
        // if (metadata == null) return null

        // TODO: Check expiration
        // if (metadata.isExpired()):
        //   deletePaste(key)
        //   return null

        // TODO: Check cache
        // String content = cache.get(key)

        // TODO: If miss, load from storage
        // if (content == null):
        //   content = objectStorage.load(key)
        //   if (!metadata.isPrivate):
        //     cache.put(key, content)

        // TODO: Increment views (async)
        // metadata.incrementViews()

        return null; // Replace
    }

    /**
     * Delete paste
     *
     * TODO: Delete from all stores
     */
    public void deletePaste(String key) {
        // TODO: Delete from metadata store
        // TODO: Delete from object storage
        // TODO: Invalidate cache
    }

    /**
     * Get paste stats
     *
     * TODO: Get metadata with view count, etc.
     */
    public PasteStats getStats(String key) {
        return null; // Replace
    }

    /**
     * Generate unique key (base62)
     *
     * TODO: Generate 7-character key
     */
    private String generateKey() {
        long id = idGenerator.next();
        return encodeBase62(id);
    }

    private String encodeBase62(long id) {
        String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.append(alphabet.charAt((int)(id % 62)));
            id /= 62;
        }
        return sb.reverse().toString();
    }

    /**
     * Metadata store (SQL database)
     */
    static class MetadataStore {
        Map<String, PasteMetadata> store;

        public MetadataStore() {
            this.store = new ConcurrentHashMap<>();
        }

        public void save(PasteMetadata metadata) {
            store.put(metadata.key, metadata);
        }

        public PasteMetadata get(String key) {
            return store.get(key);
        }

        public void delete(String key) {
            store.remove(key);
        }
    }

    /**
     * Object storage (like S3)
     */
    static class ObjectStorage {
        Map<String, String> storage;

        public ObjectStorage() {
            this.storage = new ConcurrentHashMap<>();
        }

        public void store(String key, String content) {
            storage.put(key, content);
        }

        public String load(String key) {
            return storage.get(key);
        }

        public void delete(String key) {
            storage.remove(key);
        }
    }

    /**
     * Cache (Redis)
     */
    static class Cache {
        Map<String, String> cache;
        int maxSize = 10000;

        public Cache() {
            this.cache = new LinkedHashMap<>(maxSize, 0.75f, true);
        }

        public String get(String key) {
            return cache.get(key);
        }

        public void put(String key, String value) {
            if (cache.size() >= maxSize && !cache.containsKey(key)) {
                String firstKey = cache.keySet().iterator().next();
                cache.remove(firstKey);
            }
            cache.put(key, value);
        }

        public void invalidate(String key) {
            cache.remove(key);
        }
    }

    static class IDGenerator {
        private long id = 0L;

        public synchronized long next() {
            return ++id;
        }
    }

    static class PasteMetadata {
        String key;
        int size;
        long createdAt;
        long expiresAt;
        boolean isPrivate;
        long views;

        public PasteMetadata(String key, int size, long expiresAt, boolean isPrivate) {
            this.key = key;
            this.size = size;
            this.createdAt = System.currentTimeMillis();
            this.expiresAt = expiresAt;
            this.isPrivate = isPrivate;
            this.views = 0;
        }

        public boolean isExpired() {
            return expiresAt > 0 && System.currentTimeMillis() > expiresAt;
        }

        public void incrementViews() {
            this.views++;
        }
    }

    static class PasteOptions {
        String customURL;
        long expirationTime; // 0 = never expire
        boolean isPrivate;
        String language; // For syntax highlighting

        public PasteOptions() {
            this.expirationTime = 0;
            this.isPrivate = false;
        }
    }

    static class Paste {
        PasteMetadata metadata;
        String content;

        public Paste(PasteMetadata metadata, String content) {
            this.metadata = metadata;
            this.content = content;
        }
    }

    static class PasteStats {
        long views;
        long createdAt;
        int size;
        boolean isPrivate;

        public PasteStats(PasteMetadata metadata) {
            this.views = metadata.views;
            this.createdAt = metadata.createdAt;
            this.size = metadata.size;
            this.isPrivate = metadata.isPrivate;
        }
    }
}
```

---

## Debugging Challenges

**Your task:** Find and fix system-wide bugs that span multiple components. These are the hardest bugs to debug because they involve interactions between services.

### Challenge 1: Cascading Failure in URL Shortener

```java
/**
 * BUG: System crashes under load
 *
 * Symptoms:
 * - Works fine with 100 concurrent users
 * - Crashes with 10,000 concurrent users
 * - Cache memory grows until OutOfMemoryError
 * - Database connections exhausted
 *
 * Current architecture:
 * - No connection pooling
 * - Unbounded cache size
 * - No rate limiting
 */

// Buggy cache implementation
static class Cache {
    private final Map<String, String> cache = new ConcurrentHashMap<>();
    // BUG: No size limit!

    public String get(String key) {
        return cache.get(key);
    }

    public void put(String key, String value) {
        cache.put(key, value);  // BUG: Grows forever
    }
}

// Buggy database access
static class DatabaseShards {
    public String get(String shortCode) {
        // BUG: Creates new connection every time
        Connection conn = DriverManager.getConnection(url);
        // ... query database
        conn.close();
        return result;
    }
}
```

**Your debugging:**

1. **What causes the cascade?**
    - Root cause: _[Fill in]_
    - Why it works at low load: _[Fill in]_
    - Why it fails at high load: _[Fill in]_

2. **Identify all failure points:**
    - Failure point 1: _[Fill in]_
    - Failure point 2: _[Fill in]_
    - Failure point 3: _[Fill in]_

3. **Fix the cascade:**
   ```java
   // TODO: Add connection pooling
   // TODO: Add cache size limit with LRU eviction
   // TODO: Add rate limiting
   // TODO: Add circuit breaker pattern
   ```

4. **Verification:**
    - How to test the fix: _[Fill in]_
    - Load test scenario: _[Fill in]_
    - Expected behavior under load: _[Fill in]_

<details markdown>
<summary>Click to see the solution</summary>

**Root cause:** No resource limits cause cascading failure:
1. Unbounded cache → OutOfMemoryError
2. No connection pooling → connection exhaustion
3. No rate limiting → traffic overwhelms system

**Fixes:**
```java
// Fix 1: Bounded LRU cache
static class Cache {
    private final LinkedHashMap<String, String> cache;
    private final int capacity;

    public Cache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > capacity;
            }
        };
    }
}

// Fix 2: Connection pool
static class DatabaseShards {
    private final HikariDataSource connectionPool;

    public DatabaseShards() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(20);
        this.connectionPool = new HikariDataSource(config);
    }
}

// Fix 3: Rate limiter
static class RateLimiter {
    private final int maxRequestsPerSecond = 1000;
    // Use token bucket or sliding window
}
```
</details>

---

### Challenge 2: Data Inconsistency in Twitter Feed

```java
/**
 * BUG: Users see tweets out of order and miss tweets
 *
 * Symptoms:
 * - Alice follows Bob
 * - Bob posts tweet at T1
 * - Alice sees empty timeline
 * - 5 seconds later, Bob's tweet appears
 * - Sometimes Bob's tweets never appear
 *
 * Root cause: Race condition in fan-out
 */

public class TwitterFeed {

    // Buggy follow implementation
    public void followUser(long followerId, long followeeId) {
        userGraph.addFollow(followerId, followeeId);

        // BUG: Backfill happens AFTER follow is recorded
        // If followee posts tweet between addFollow and backfill,
        // tweet is missed!
        List<Tweet> recentTweets = tweetStore.getUserTweets(followeeId, 100);
        timelineCache.addTweets(followerId, recentTweets);
    }

    // Buggy fan-out implementation
    public void fanoutTweet(Tweet tweet) {
        List<Long> followers = userGraph.getFollowers(tweet.userId);

        // BUG: Fan-out is slow and synchronous
        // If user posts 2 tweets quickly, second tweet might
        // fan out before first one completes
        for (Long followerId : followers) {
            timelineCache.addTweet(followerId, tweet);
            // What if this fails halfway through?
        }
    }
}
```

**Your debugging:**

1. **Identify the race conditions:**
    - Race 1: _[Between follow and backfill]_
    - Race 2: _[Between fanout operations]_
    - Race 3: _[Fill in if you find more]_

2. **Timeline consistency issues:**
    - What order should tweets appear: _[Fill in]_
    - Why are tweets missing: _[Fill in]_
    - Why are tweets out of order: _[Fill in]_

3. **Design fixes:**
   ```java
   // TODO: Use transactional follow with backfill
   // TODO: Use message queue for ordered fanout
   // TODO: Add timestamps for ordering
   // TODO: Handle partial fanout failures
   ```

4. **Trade-off analysis:**
    - Strong consistency cost: _[Fill in]_
    - Eventual consistency risk: _[Fill in]_
    - Your choice: _[Strong/Eventual - why?]_

<details markdown>
<summary>Click to see the solution</summary>

**Root causes:**

1. **Follow race:** Tweet posted between addFollow and backfill is missed
2. **Fanout race:** No ordering guarantee for concurrent fanouts
3. **Partial failure:** Fanout fails halfway, some followers see tweet, others don't

**Fixes:**

```java
// Fix 1: Transactional follow with timestamp
public void followUser(long followerId, long followeeId) {
    long followTimestamp = System.currentTimeMillis();

    // Record follow with timestamp
    userGraph.addFollow(followerId, followeeId, followTimestamp);

    // Backfill tweets posted BEFORE follow time
    List<Tweet> recentTweets = tweetStore.getUserTweets(
        followeeId, 100, beforeTimestamp: followTimestamp
    );
    timelineCache.addTweets(followerId, recentTweets);
}

// Fix 2: Message queue for ordered fanout
public void fanoutTweet(Tweet tweet) {
    // Publish to message queue with ordering key (userId)
    messageQueue.publish(
        topic: "tweet-fanout",
        key: tweet.userId,  // Guarantees order per user
        value: tweet
    );
}

// Fix 3: Idempotent consumer with retry
class FanoutConsumer {
    public void processTweet(Tweet tweet) {
        List<Long> followers = userGraph.getFollowers(tweet.userId);

        for (Long followerId : followers) {
            try {
                // Idempotent: won't duplicate if retry
                timelineCache.addTweetIfNotExists(followerId, tweet);
            } catch (Exception e) {
                // Retry with exponential backoff
                retryQueue.add(followerId, tweet);
            }
        }
    }
}
```

**Trade-off chosen:** Eventual consistency with ordering guarantees
- Accept slight delay (100-200ms)
- Guarantee eventual delivery
- Guarantee ordering per user
</details>

---

### Challenge 3: Bottleneck in Pastebin

```java
/**
 * BUG: System slows down under load
 *
 * Symptoms:
 * - Small pastes (< 1KB): Fast
 * - Large pastes (> 1MB): Very slow
 * - Database grows to 500GB
 * - Most queries timeout
 *
 * Observation: Only 5% of pastes are > 100KB
 * But they consume 90% of database storage
 */

public class Pastebin {

    public String createPaste(String content, PasteOptions options) {
        String key = generateKey();

        // BUG: Storing large content in SQL database
        // SQL is optimized for small rows, not BLOBs
        metadataStore.save(new PasteMetadata(key, content, options));

        return key;
    }

    public Paste getPaste(String key) {
        // BUG: Every request loads full content from DB
        // Even for 10MB pastes
        PasteMetadata metadata = metadataStore.get(key);

        // BUG: Cache stores large pastes in memory
        // 100 * 10MB pastes = 1GB memory
        if (!metadata.isPrivate) {
            cache.put(key, metadata.content);
        }

        return new Paste(metadata, metadata.content);
    }
}
```

**Your debugging:**

1. **Identify the bottleneck:**
    - Primary bottleneck: _[Database/Memory/Network?]_
    - Why small pastes are fast: _[Fill in]_
    - Why large pastes are slow: _[Fill in]_

2. **Calculate the impact:**
    - If 1M pastes, avg size 100KB, total storage: _[__ GB]_
    - If 5% are 5MB, storage for large pastes: _[__ GB]_
    - Database query time for 5MB row: _[Estimate]_

3. **Design the fix:**
    - Where should large pastes go: _[Object storage/CDN/Other?]_
    - Threshold for "large": _[Fill in]_
    - Architecture change needed: _[Draw/describe]_

4. **Implementation:**
   ```java
   // TODO: Store metadata in SQL, content in object storage
   // TODO: Different strategies for small vs large pastes
   // TODO: Cache only metadata, not content
   // TODO: Stream large pastes instead of loading fully
   ```

<details markdown>
<summary>Click to see the solution</summary>

**Bottleneck analysis:**

- **Primary:** SQL database storing large BLOBs (wrong tool)
- **Secondary:** Memory exhaustion from caching large content
- **Tertiary:** Network bandwidth loading full content every time

**Solution: Hybrid storage architecture**

```java
public class Pastebin {
    private static final int LARGE_PASTE_THRESHOLD = 100_000; // 100KB

    public String createPaste(String content, PasteOptions options) {
        String key = generateKey();
        int size = content.length();

        PasteMetadata metadata;

        if (size > LARGE_PASTE_THRESHOLD) {
            // Store large content in object storage (S3)
            String s3Key = objectStorage.store(key, content);
            metadata = new PasteMetadata(key, size, s3Key, options);
        } else {
            // Store small content in database
            metadata = new PasteMetadata(key, size, content, options);
        }

        metadataStore.save(metadata);

        // Cache only metadata, not content
        if (!options.isPrivate) {
            cache.putMetadata(key, metadata);
        }

        return key;
    }

    public Paste getPaste(String key) {
        // Get metadata from cache or DB
        PasteMetadata metadata = cache.getMetadata(key);
        if (metadata == null) {
            metadata = metadataStore.get(key);
            cache.putMetadata(key, metadata);
        }

        // Load content based on size
        String content;
        if (metadata.isLarge()) {
            // Stream from S3, don't cache
            content = objectStorage.load(metadata.s3Key);
        } else {
            // Load from DB, can cache
            content = metadata.content;
        }

        return new Paste(metadata, content);
    }
}
```

**Results:**

- Database size: 500GB → 50GB (10x reduction)
- Query time for large pastes: 2s → 100ms (20x faster)
- Memory usage: 1GB → 50MB (20x reduction)
- Cost: 1x (SQL) → 0.2x (hybrid SQL + S3)
</details>

---

### Challenge 4: Cross-Service Data Inconsistency

```java
/**
 * BUG: Analytics show wrong click counts
 *
 * Symptoms:
 * - URL shortener shows 100 clicks
 * - Analytics service shows 95 clicks
 * - Numbers diverge over time
 * - After system restart, analytics reset to 0
 *
 * Architecture:
 * - URL shortener increments local counter
 * - Analytics service polls for updates
 * - No transaction coordination
 */

// URL Shortener Service
public String getOriginalURL(String shortCode) {
    String url = cache.get(shortCode);
    if (url == null) {
        url = database.get(shortCode);
    }

    // BUG: Async analytics call can fail silently
    analytics.trackClick(shortCode);  // Fire and forget

    return url;
}

// Analytics Service
public void trackClick(String shortCode) {
    // BUG: In-memory counter lost on restart
    stats.computeIfAbsent(shortCode, k -> new URLAnalytics())
         .incrementClicks();

    // BUG: Persists to DB every 5 minutes
    // Crashes between saves lose data
    if (shouldFlush()) {
        persistToDB();
    }
}
```

**Your debugging:**

1. **Identify data loss scenarios:**
    - Loss scenario 1: _[Fill in]_
    - Loss scenario 2: _[Fill in]_
    - Loss scenario 3: _[Fill in]_

2. **Why counts diverge:**
    - Root cause: _[Fill in]_
    - Race condition: _[Fill in]_
    - Network failure impact: _[Fill in]_

3. **Design a reliable solution:**
    - Guarantee delivery: _[How?]_
    - Exactly-once semantics: _[How?]_
    - Durability: _[How?]_

**Implementation choices:**

-   [ ] Message queue with acknowledgments
-   [ ] Synchronous DB writes (slow but safe)
-   [ ] Write-ahead log
-   [ ] Distributed transactions
-   Your choice: _[Fill in and explain why]_

<details markdown>
<summary>Click to see the solution</summary>

**Data loss scenarios:**

1. Analytics service crash → in-memory counters lost
2. Network failure → trackClick() call drops
3. Async fire-and-forget → no retry on failure

**Solution: Event-driven architecture with durability**

```java
// URL Shortener: Publish event instead of direct call
public String getOriginalURL(String shortCode) {
    String url = cache.get(shortCode);
    if (url == null) {
        url = database.get(shortCode);
    }

    // Publish to durable message queue
    ClickEvent event = new ClickEvent(shortCode, System.currentTimeMillis());
    messageQueue.publish("click-events", event);

    return url;
}

// Analytics: Consume events with acknowledgment
class AnalyticsConsumer {
    public void processClickEvent(ClickEvent event) {
        try {
            // Write directly to database (no in-memory only)
            analyticsDB.incrementClick(event.shortCode, event.timestamp);

            // Acknowledge only after successful write
            messageQueue.ack(event);

        } catch (Exception e) {
            // Don't ack - message will be retried
            log.error("Failed to process click event", e);
            throw e;
        }
    }
}

// Message Queue guarantees:
// 1. At-least-once delivery (with retry)
// 2. Durability (persisted to disk)
// 3. Ordering per partition

// Database handles idempotency:
// UPDATE analytics SET clicks = clicks + 1, last_seen = ?
// WHERE short_code = ? AND (last_seen IS NULL OR last_seen < ?)
```

**Trade-offs:**

- **Latency:** Slight increase (10-20ms for message publish)
- **Complexity:** Higher (need message queue infrastructure)
- **Consistency:** Eventual (analytics lag by 100-200ms)
- **Reliability:** Much higher (no data loss)

**Alternative:** Synchronous writes (simpler but slower)
```java
public String getOriginalURL(String shortCode) {
    String url = database.get(shortCode);

    // Synchronous write to analytics DB
    analyticsDB.incrementClick(shortCode);  // Blocks 10-20ms

    return url;
}
```
</details>

---

### Your Debugging Scorecard

After working through these challenges:

-   [ ] Identified cascading failure patterns
-   [ ] Fixed race conditions in distributed systems
-   [ ] Resolved performance bottlenecks
-   [ ] Designed for data consistency across services
-   [ ] Understood trade-offs in each solution

**System design lessons learned:**

1. _[Most important lesson from Challenge 1]_
2. _[Most important lesson from Challenge 2]_
3. _[Most important lesson from Challenge 3]_
4. _[Most important lesson from Challenge 4]_

---

## Client Code

```java
import java.util.*;

public class FullSystemDesignsClient {

    public static void main(String[] args) {
        testURLShortener();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testTwitterFeed();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testPastebin();
    }

    static void testURLShortener() {
        System.out.println("=== URL Shortener Test ===\n");

        URLShortener shortener = new URLShortener(3);

        // Test: Shorten URLs
        System.out.println("Shortening URLs:");
        String short1 = shortener.shortenURL("https://www.example.com/very/long/url/page1");
        String short2 = shortener.shortenURL("https://www.example.com/another/long/url/page2");
        String short3 = shortener.shortenURL("https://www.example.com/yet/another/url/page3");

        System.out.println("Short code 1: " + short1);
        System.out.println("Short code 2: " + short2);
        System.out.println("Short code 3: " + short3);

        // Test: Resolve URLs
        System.out.println("\nResolving short URLs:");
        String original1 = shortener.getOriginalURL(short1);
        System.out.println(short1 + " -> " + original1);

        // Test: Multiple accesses (analytics)
        for (int i = 0; i < 5; i++) {
            shortener.getOriginalURL(short1);
        }

        // Test: Analytics
        System.out.println("\nAnalytics:");
        URLShortener.URLAnalytics analytics = shortener.getAnalytics(short1);
        System.out.println("Total clicks: " + analytics.getClicks());
    }

    static void testTwitterFeed() {
        System.out.println("=== Twitter Feed Test ===\n");

        TwitterFeed twitter = new TwitterFeed();

        // Create users
        long alice = 1L;
        long bob = 2L;
        long charlie = 3L;

        // Alice follows Bob and Charlie
        System.out.println("Alice follows Bob and Charlie");
        twitter.followUser(alice, bob);
        twitter.followUser(alice, charlie);

        // Bob and Charlie post tweets
        System.out.println("\nPosting tweets:");
        twitter.postTweet(bob, "Hello from Bob!");
        twitter.postTweet(charlie, "Charlie here!");
        twitter.postTweet(bob, "Bob's second tweet");

        // Small delay for fan-out
        try { Thread.sleep(100); } catch (InterruptedException e) {}

        // Alice views timeline
        System.out.println("\nAlice's timeline:");
        List<TwitterFeed.Tweet> timeline = twitter.getHomeTimeline(alice, 10);
        if (timeline != null) {
            for (TwitterFeed.Tweet tweet : timeline) {
                System.out.println("  " + tweet);
            }
        }

        // Bob posts after Alice unfollows
        System.out.println("\nAlice unfollows Bob");
        twitter.unfollowUser(alice, bob);
        twitter.postTweet(bob, "Bob's third tweet (Alice won't see this)");
    }

    static void testPastebin() {
        System.out.println("=== Pastebin Test ===\n");

        Pastebin pastebin = new Pastebin();

        // Test: Create public paste
        System.out.println("Creating public paste:");
        Pastebin.PasteOptions options1 = new Pastebin.PasteOptions();
        options1.isPrivate = false;
        options1.expirationTime = 0; // Never expire

        String url1 = pastebin.createPaste(
            "public class HelloWorld {\n  public static void main(String[] args) {\n    System.out.println(\"Hello!\");\n  }\n}",
            options1
        );
        System.out.println("Paste URL: " + url1);

        // Test: Create private paste with expiration
        System.out.println("\nCreating private paste (expires in 1 hour):");
        Pastebin.PasteOptions options2 = new Pastebin.PasteOptions();
        options2.isPrivate = true;
        options2.expirationTime = System.currentTimeMillis() + 3600000; // 1 hour

        String url2 = pastebin.createPaste("This is a private paste", options2);
        System.out.println("Paste URL: " + url2);

        // Test: Create paste with custom URL
        System.out.println("\nCreating paste with custom URL:");
        Pastebin.PasteOptions options3 = new Pastebin.PasteOptions();
        options3.customURL = "my-code";

        String url3 = pastebin.createPaste("Custom URL paste content", options3);
        System.out.println("Paste URL: " + url3);

        // Test: Retrieve paste
        System.out.println("\nRetrieving paste:");
        Pastebin.Paste paste = pastebin.getPaste(url1);
        if (paste != null) {
            System.out.println("Content: " + paste.content);
            System.out.println("Size: " + paste.metadata.size + " bytes");
        }

        // Test: Multiple views (analytics)
        for (int i = 0; i < 3; i++) {
            pastebin.getPaste(url1);
        }

        System.out.println("\nPaste stats:");
        Pastebin.PasteStats stats = pastebin.getStats(url1);
        if (stats != null) {
            System.out.println("Views: " + stats.views);
        }
    }
}
```

---

## Decision Framework

**Questions to answer after implementation:**

### 1. System Requirements Analysis

**URL Shortener:**

- Scale: _[How many URLs? Reads vs writes?]_
- Latency: _[What's acceptable?]_
- Consistency: _[Strong or eventual?]_

**Twitter Feed:**

- Scale: _[Users, tweets per day?]_
- Fan-out strategy: _[Push, pull, or hybrid?]_
- Consistency: _[Can timeline have slight delay?]_

**Pastebin:**

- Storage: _[How to store large pastes?]_
- Expiration: _[How to clean up?]_
- Access patterns: _[Read-heavy or write-heavy?]_

### 2. Architecture Decisions

**When to use each approach:**

**Sharding:**

- Your scenario: _[Fill in]_
- Shard key: _[Fill in]_

**Caching:**

- Your scenario: _[Fill in]_
- Cache strategy: _[Fill in]_

**Message Queue:**

- Your scenario: _[Fill in]_
- Queue type: _[Fill in]_

### 3. Your Decision Tree

```
What are you building?
├─ URL shortener-like → Key generation, sharding, caching
├─ Social feed-like → Fan-out, timeline generation
├─ File storage-like → Object storage, metadata DB
└─ Real-time updates → WebSockets, message queues
```

### 4. Scaling Considerations

**Database:**

- When to shard? _[Fill in]_
- Shard key selection: _[Fill in]_

**Cache:**

- What to cache? _[Fill in]_
- Eviction policy: _[Fill in]_

**Load Balancing:**

- Algorithm: _[Fill in]_
- Session handling: _[Fill in]_

---

## Practice

### Scenario 1: Design Instagram

**Requirements:**

- Upload photos
- Follow users
- View photo feed
- Like and comment
- Handle 500M users

**Your design:**

- Architecture: _[Draw components]_
- Storage strategy: _[Fill in]_
- Feed generation: _[Fill in]_
- Scalability: _[Fill in]_

### Scenario 2: Design Dropbox

**Requirements:**

- Upload/download files
- File synchronization
- Sharing and permissions
- Handle TBs of data
- Offline support

**Your design:**

- Architecture: _[Draw components]_
- File storage: _[Fill in]_
- Sync strategy: _[Fill in]_
- Conflict resolution: _[Fill in]_

### Scenario 3: Design Netflix

**Requirements:**

- Stream videos
- Recommendations
- Handle millions of concurrent users
- Global distribution
- Different video qualities

**Your design:**

- Architecture: _[Draw components]_
- CDN strategy: _[Fill in]_
- Video encoding: _[Fill in]_
- Recommendations: _[Fill in]_

---

## Review Checklist

-   [ ] URL shortener implemented with ID generation and sharding
-   [ ] Twitter feed implemented with fan-out strategy
-   [ ] Pastebin implemented with object storage
-   [ ] Understand architectural patterns used
-   [ ] Can explain scaling strategies for each system
-   [ ] Can identify bottlenecks and solutions
-   [ ] Completed practice system designs

---

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery of full system design by demonstrating integration of all concepts. You cannot move forward until you can confidently complete this section.

### Gate 1: Explain System Trade-offs

**Scenario:** A product manager asks you to design a system similar to Twitter but optimized for 1 billion users.

**Your explanation (write it out):**

> "For a system at this scale, the key trade-offs are..."
>
> _[Fill in - discuss consistency vs availability, latency vs throughput, cost vs performance - 4-5 sentences]_

**Self-assessment:**

- Trade-off clarity score (1-10): ___
- Did you mention specific technologies (Redis, Kafka, etc.)? _[Yes/No]_
- Did you quantify the trade-offs (e.g., "100ms latency increase for 99.99% availability")? _[Yes/No]_

If you scored below 8 or answered "No" to either question, revise your explanation.

---

### Gate 2: Capacity Planning Exercise

**Task:** Calculate infrastructure requirements for a URL shortener without looking at notes.

**Given:**

- 100 million URLs per month
- Average URL length: 2000 characters
- Read/write ratio: 100:1
- 7-day retention in cache
- Peak traffic: 3x average

**Calculate:**

1. **Writes per second:**
    - Average: _[Fill in calculation]_
    - Peak: _[Fill in]_

2. **Reads per second:**
    - Average: _[Fill in calculation]_
    - Peak: _[Fill in]_

3. **Storage requirements:**
    - Total per year: _[Fill in GB/TB]_
    - With metadata (2x): _[Fill in]_

4. **Cache memory:**
    - URLs to cache: _[Fill in calculation]_
    - Memory needed: _[Fill in GB]_

5. **Database shards:**
    - Assuming 10M URLs per shard: _[How many shards?]_

**Verification checklist:**

-   [ ] Converted months to seconds correctly
-   [ ] Applied read/write ratio correctly
-   [ ] Accounted for peak traffic
-   [ ] Calculated storage with overhead
-   [ ] Sized cache appropriately

---

### Gate 3: Architecture Design Test

**Without looking at your notes, design a complete system for Instagram:**

**Requirements:**

- 500M users
- 100M photos uploaded per day
- Average photo size: 2MB
- Feed shows last 100 photos from followed users
- 300 follows per user on average

**Draw your architecture (on paper or in ASCII):**

```
[Your architecture diagram here]
- Include all major components
- Show data flow
- Label databases, caches, message queues
- Indicate what's sharded and how



```

**Answer these about your design:**

1. **Photo storage strategy:**
    - Where: _[S3/CDN/Database?]_
    - Why: _[Fill in]_
    - Cost estimate: _[Rough calculation]_

2. **Feed generation approach:**
    - Strategy: _[Push/Pull/Hybrid?]_
    - Why: _[Explain your choice]_
    - Bottleneck: _[What will fail first?]_

3. **Sharding strategy:**
    - What to shard: _[Users/Photos/Both?]_
    - Shard key: _[Fill in]_
    - Number of shards: _[Estimate]_

4. **Caching layers:**
    - Layer 1: _[What and why]_
    - Layer 2: _[What and why]_
    - Eviction policy: _[Fill in]_

---

### Gate 4: Bottleneck Identification

**Scenario:** Your URL shortener is deployed and working. After 6 months:
- Database is at 80% CPU usage
- Cache hit rate dropped from 90% to 40%
- P99 latency increased from 50ms to 800ms
- Users complain about slow redirects

**Diagnose the problem:**

1. **What's the root cause?**
    - Your diagnosis: _[Fill in]_
    - Why cache hit rate dropped: _[Fill in]_
    - Why database is overloaded: _[Fill in]_

2. **Order of operations to fix (prioritize):**
    - Step 1: _[What to fix first?]_
    - Step 2: _[What to fix second?]_
    - Step 3: _[What to fix third?]_

3. **Long-term solutions:**
    - Architecture change 1: _[Fill in]_
    - Architecture change 2: _[Fill in]_
    - Monitoring to add: _[Fill in]_

4. **Validation plan:**
    - Metric to track: _[Fill in]_
    - Success criteria: _[Fill in]_
    - Rollback plan: _[Fill in]_

---

### Gate 5: Pattern Integration Test

**Classify these system design challenges (without looking at notes):**

| Challenge | Primary Pattern(s) | Why? |
|-----------|-------------------|------|
| News feed with 1M posts/day | _[Fill in]_ | _[Explain]_ |
| Distributed rate limiter | _[Fill in]_ | _[Explain]_ |
| Global leaderboard (real-time) | _[Fill in]_ | _[Explain]_ |
| File sync across devices | _[Fill in]_ | _[Explain]_ |
| Chat system with 100M users | _[Fill in]_ | _[Explain]_ |
| Search autocomplete | _[Fill in]_ | _[Explain]_ |

**Score:** ___/6 correct

**For each, also identify:**

- Main bottleneck: _[Fill in]_
- Database choice: _[SQL/NoSQL/Both]_
- Caching strategy: _[Yes/No, what to cache?]_

---

### Gate 6: Live Design Interview Simulation

**Set a 45-minute timer. Design this system as if in a real interview:**

**Problem:** Design a ride-sharing service like Uber

**Requirements:**

- 100M riders, 10M drivers
- Real-time location tracking
- Match riders with nearby drivers
- Calculate ETAs and routes
- Handle payments
- Track ride history

**Your design process (document each step):**

**Step 1: Requirements Clarification (5 min)**

- Questions you would ask: _[List 5-7 questions]_

**Step 2: Capacity Estimation (5 min)**

- Requests per second: _[Calculate]_
- Storage requirements: _[Calculate]_
- Bandwidth: _[Calculate]_

**Step 3: High-Level Design (10 min)**
```
[Draw your architecture]




```

**Step 4: Deep Dive - Location Tracking (10 min)**

- How to store driver locations: _[Data structure?]_
- How to find nearby drivers: _[Algorithm?]_
- Update frequency: _[How often?]_
- Database choice: _[What and why?]_

**Step 5: Deep Dive - Matching Algorithm (10 min)**

- Matching criteria: _[List factors]_
- Real-time updates: _[How?]_
- Fairness: _[How to ensure?]_
- Failure handling: _[What if driver rejects?]_

**Step 6: Bottlenecks and Scaling (5 min)**

- Bottleneck 1: _[What and how to fix]_
- Bottleneck 2: _[What and how to fix]_
- Bottleneck 3: _[What and how to fix]_

**Self-grading rubric:**

-   [ ] Clarified requirements before designing
-   [ ] Calculated capacity accurately
-   [ ] Designed complete architecture (not just pieces)
-   [ ] Identified and explained trade-offs
-   [ ] Discussed failure scenarios
-   [ ] Proposed monitoring and metrics
-   [ ] Stayed within time limits
-   [ ] Design would actually work at scale

**Score:** ___/8 passed

If score < 6, review weak areas and retry.

---

### Gate 7: Teaching Check

**The ultimate test of understanding is teaching.**

**Task:** Explain to an imaginary junior engineer when to use push vs pull for feed generation.

Your explanation:

> "Push vs pull is a fundamental trade-off in system design..."
>
> _[Fill in - explain both approaches, when to use each, with concrete examples - 6-8 sentences]_

**Include in your explanation:**

- What push means: _[Fill in]_
- What pull means: _[Fill in]_
- Push advantage: _[Fill in]_
- Push disadvantage: _[Fill in]_
- Pull advantage: _[Fill in]_
- Pull disadvantage: _[Fill in]_
- When to use hybrid: _[Fill in]_

**Real-world examples:**

- Twitter uses _[Push/Pull/Hybrid]_ because _[Fill in]_
- Instagram uses _[Push/Pull/Hybrid]_ because _[Fill in]_
- LinkedIn uses _[Push/Pull/Hybrid]_ because _[Fill in]_

---

### Gate 8: Trade-off Decision Matrix

**Complete this matrix from memory:**

| System Requirement | Consistency | Availability | Partition Tolerance | Your Choice | Why? |
|-------------------|-------------|--------------|---------------------|-------------|------|
| Bank transactions | _[Priority 1-3]_ | _[Priority 1-3]_ | _[Priority 1-3]_ | _[Fill in]_ | _[Explain]_ |
| Social media feed | _[Priority 1-3]_ | _[Priority 1-3]_ | _[Priority 1-3]_ | _[Fill in]_ | _[Explain]_ |
| URL shortener | _[Priority 1-3]_ | _[Priority 1-3]_ | _[Priority 1-3]_ | _[Fill in]_ | _[Explain]_ |
| Chat messages | _[Priority 1-3]_ | _[Priority 1-3]_ | _[Priority 1-3]_ | _[Fill in]_ | _[Explain]_ |
| E-commerce inventory | _[Priority 1-3]_ | _[Priority 1-3]_ | _[Priority 1-3]_ | _[Fill in]_ | _[Explain]_ |

**Deep question:** Given the CAP theorem, explain why you can't have perfect C, A, and P simultaneously.

Your answer: _[Fill in - explain in plain English with an example]_

---

### Gate 9: Failure Scenario Planning

**For each scenario, describe what happens and how to mitigate:**

**Scenario 1:** Your primary database in the URL shortener crashes.
- Immediate impact: _[Fill in]_
- User experience: _[Fill in]_
- Detection method: _[Fill in]_
- Mitigation: _[Fill in]_
- Prevention: _[Fill in]_

**Scenario 2:** Cache servers lose all data (Redis cluster restart).
- Immediate impact: _[Fill in]_
- Database load spike: _[Estimate: __x normal]_
- Mitigation: _[Fill in]_
- Cache warming strategy: _[Fill in]_

**Scenario 3:** Message queue (Kafka) has 2-hour delay due to traffic spike.
- Affected systems: _[Fill in]_
- Data consistency issues: _[Fill in]_
- User-visible impact: _[Fill in]_
- Mitigation: _[Fill in]_

**Scenario 4:** CDN goes down in US region.
- Affected users: _[Fill in]_
- Fallback mechanism: _[Fill in]_
- Performance degradation: _[Fill in]_
- Mitigation: _[Fill in]_

---

### Gate 10: Code to Architecture (Final Integration Test)

**Given this code, identify all the architectural problems:**

```java
public class TwitterFeed {
    // Single global cache
    private static Map<Long, List<Tweet>> cache = new HashMap<>();

    public List<Tweet> getTimeline(long userId) {
        // Problem 1: What's wrong here?
        List<Tweet> timeline = cache.get(userId);
        if (timeline != null) {
            return timeline;
        }

        // Problem 2: What's wrong here?
        List<Long> following = database.getFollowing(userId);
        timeline = new ArrayList<>();
        for (Long followedUser : following) {
            timeline.addAll(database.getUserTweets(followedUser));
        }

        // Problem 3: What's wrong here?
        Collections.sort(timeline, (a, b) ->
            Long.compare(b.timestamp, a.timestamp)
        );

        // Problem 4: What's wrong here?
        cache.put(userId, timeline);

        return timeline;
    }
}
```

**Identify problems:**

1. **Problem 1 (Cache):**
    - Issue: _[Fill in]_
    - Impact at scale: _[Fill in]_
    - Fix: _[Fill in]_

2. **Problem 2 (Database queries):**
    - Issue: _[Fill in]_
    - Impact at scale: _[Fill in]_
    - Fix: _[Fill in]_

3. **Problem 3 (Sorting):**
    - Issue: _[Fill in]_
    - Impact at scale: _[Fill in]_
    - Fix: _[Fill in]_

4. **Problem 4 (Cache update):**
    - Issue: _[Fill in]_
    - Impact at scale: _[Fill in]_
    - Fix: _[Fill in]_

**Additional problems you spotted:**
5. _[Fill in if you found more]_
6. _[Fill in if you found more]_

**Your production-ready rewrite:**
```java
// TODO: Rewrite this class to be production-ready
// - Use distributed cache
// - Use pre-computed timelines
// - Handle failures gracefully
// - Add monitoring
```

---

### Mastery Certification

**I certify that I can:**

-   [ ] Design complete systems integrating all patterns learned
-   [ ] Perform accurate capacity planning calculations
-   [ ] Identify and resolve bottlenecks
-   [ ] Make and justify architecture trade-offs
-   [ ] Handle failure scenarios gracefully
-   [ ] Explain designs clearly to technical and non-technical audiences
-   [ ] Debug distributed system issues
-   [ ] Choose appropriate technologies for requirements
-   [ ] Scale systems from prototype to production
-   [ ] Complete a live system design interview

**Self-assessment score:** ___/10

**Time to complete all gates:** ___ hours

**If score < 8:** Review the sections where you struggled, revisit the implementations, then retry the gates.

**If score ≥ 8:** Congratulations! You've mastered full system design. You're ready for system design interviews and real-world distributed systems work.

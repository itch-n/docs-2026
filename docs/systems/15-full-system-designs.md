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

- [ ] URL shortener implemented with ID generation and sharding
- [ ] Twitter feed implemented with fan-out strategy
- [ ] Pastebin implemented with object storage
- [ ] Understand architectural patterns used
- [ ] Can explain scaling strategies for each system
- [ ] Can identify bottlenecks and solutions
- [ ] Completed practice system designs

---

**Back:** [14. Consensus Patterns ←](14-consensus-patterns.md)

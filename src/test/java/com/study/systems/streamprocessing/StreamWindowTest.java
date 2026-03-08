package com.study.systems.streamprocessing;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class StreamWindowTest {

    // Shared event list used across all window tests.
    // Events: user1 at 1000, 3000, 6000, 11000, 15000
    //         user2 at 2000, 7000, 12000, 16000
    private List<StreamWindow.Event<String, String>> sampleEvents() {
        return Arrays.asList(
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
    }

    // --- Tumbling Window (5000ms) ---

    @Test
    void testTumblingWindowZeroHasUser1Count2() {
        // Events at 1000 (user1) and 3000 (user1) fall in window [0, 4999]
        Map<Long, Map<String, Long>> result =
            StreamWindow.tumblingWindow(sampleEvents(), 5000L);

        Map<String, Long> window0 = result.get(0L);
        assertNotNull(window0, "Window starting at 0 should exist");
        assertEquals(2L, window0.get("user1"), "user1 count in window 0 should be 2");
    }

    @Test
    void testTumblingWindowZeroHasUser2Count1() {
        // Event at 2000 (user2) falls in window [0, 4999]
        Map<Long, Map<String, Long>> result =
            StreamWindow.tumblingWindow(sampleEvents(), 5000L);

        Map<String, Long> window0 = result.get(0L);
        assertNotNull(window0, "Window starting at 0 should exist");
        assertEquals(1L, window0.get("user2"), "user2 count in window 0 should be 1");
    }

    @Test
    void testTumblingWindow5000HasUser1Count1() {
        // Event at 6000 (user1) falls in window [5000, 9999]
        Map<Long, Map<String, Long>> result =
            StreamWindow.tumblingWindow(sampleEvents(), 5000L);

        Map<String, Long> window5000 = result.get(5000L);
        assertNotNull(window5000, "Window starting at 5000 should exist");
        assertEquals(1L, window5000.get("user1"), "user1 count in window 5000 should be 1");
    }

    @Test
    void testTumblingWindow5000HasUser2Count1() {
        // Event at 7000 (user2) falls in window [5000, 9999]
        Map<Long, Map<String, Long>> result =
            StreamWindow.tumblingWindow(sampleEvents(), 5000L);

        Map<String, Long> window5000 = result.get(5000L);
        assertNotNull(window5000, "Window starting at 5000 should exist");
        assertEquals(1L, window5000.get("user2"), "user2 count in window 5000 should be 1");
    }

    @Test
    void testTumblingWindow10000HasUser1Count1() {
        // Event at 11000 (user1) falls in window [10000, 14999]
        Map<Long, Map<String, Long>> result =
            StreamWindow.tumblingWindow(sampleEvents(), 5000L);

        Map<String, Long> window10000 = result.get(10000L);
        assertNotNull(window10000, "Window starting at 10000 should exist");
        assertEquals(1L, window10000.get("user1"), "user1 count in window 10000 should be 1");
    }

    @Test
    void testTumblingWindow10000HasUser2Count1() {
        // Event at 12000 (user2) falls in window [10000, 14999]
        Map<Long, Map<String, Long>> result =
            StreamWindow.tumblingWindow(sampleEvents(), 5000L);

        Map<String, Long> window10000 = result.get(10000L);
        assertNotNull(window10000, "Window starting at 10000 should exist");
        assertEquals(1L, window10000.get("user2"), "user2 count in window 10000 should be 1");
    }

    @Test
    void testTumblingWindow15000HasUser1Count1() {
        // Event at 15000 (user1) falls in window [15000, 19999]
        Map<Long, Map<String, Long>> result =
            StreamWindow.tumblingWindow(sampleEvents(), 5000L);

        Map<String, Long> window15000 = result.get(15000L);
        assertNotNull(window15000, "Window starting at 15000 should exist");
        assertEquals(1L, window15000.get("user1"), "user1 count in window 15000 should be 1");
    }

    @Test
    void testTumblingWindow15000HasUser2Count1() {
        // Event at 16000 (user2) falls in window [15000, 19999]
        Map<Long, Map<String, Long>> result =
            StreamWindow.tumblingWindow(sampleEvents(), 5000L);

        Map<String, Long> window15000 = result.get(15000L);
        assertNotNull(window15000, "Window starting at 15000 should exist");
        assertEquals(1L, window15000.get("user2"), "user2 count in window 15000 should be 1");
    }

    @Test
    void testTumblingWindowHasExactlyFourWindows() {
        Map<Long, Map<String, Long>> result =
            StreamWindow.tumblingWindow(sampleEvents(), 5000L);

        assertEquals(4, result.size(), "Should produce exactly 4 tumbling windows");
    }

    @Test
    void testTumblingWindowEmptyInputReturnsEmptyMap() {
        Map<Long, Map<String, Long>> result =
            StreamWindow.tumblingWindow(Collections.emptyList(), 5000L);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Empty input should produce empty tumbling window map");
    }

    // --- Sliding Window (5000ms window, 2000ms slide) ---

    @Test
    void testSlidingWindowReturnsNonEmptyResult() {
        Map<Long, Map<String, Long>> result =
            StreamWindow.slidingWindow(sampleEvents(), 5000L, 2000L);

        assertNotNull(result);
        assertFalse(result.isEmpty(), "Sliding window result should not be empty");
    }

    @Test
    void testSlidingWindowAllCountsArePositive() {
        Map<Long, Map<String, Long>> result =
            StreamWindow.slidingWindow(sampleEvents(), 5000L, 2000L);

        for (Map.Entry<Long, Map<String, Long>> windowEntry : result.entrySet()) {
            for (Map.Entry<String, Long> keyCount : windowEntry.getValue().entrySet()) {
                assertTrue(keyCount.getValue() > 0,
                    "All counts should be positive in window " + windowEntry.getKey());
            }
        }
    }

    @Test
    void testSlidingWindowZeroContainsUser1() {
        // Window [0, 5000): events at 1000(user1), 2000(user2), 3000(user1) → user1=2, user2=1
        Map<Long, Map<String, Long>> result =
            StreamWindow.slidingWindow(sampleEvents(), 5000L, 2000L);

        Map<String, Long> window0 = result.get(0L);
        assertNotNull(window0, "Window starting at 0 should exist");
        assertTrue(window0.containsKey("user1"), "Window 0 should contain user1");
    }

    @Test
    void testSlidingWindowZeroUser1CountIsTwo() {
        // Window [0, 5000): events at 1000(user1) and 3000(user1) → user1=2
        Map<Long, Map<String, Long>> result =
            StreamWindow.slidingWindow(sampleEvents(), 5000L, 2000L);

        Map<String, Long> window0 = result.get(0L);
        assertNotNull(window0, "Window starting at 0 should exist");
        assertEquals(2L, window0.get("user1"), "user1 count in window 0 should be 2");
    }

    @Test
    void testSlidingWindowEmptyInputReturnsEmptyMap() {
        Map<Long, Map<String, Long>> result =
            StreamWindow.slidingWindow(Collections.emptyList(), 5000L, 2000L);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Empty input should produce empty sliding window map");
    }

    // --- Session Window (3000ms gap) ---

    @Test
    void testSessionWindowTotalSessionCountIsSeven() {
        // Condition to open a new session: gap > gapMs (strictly greater).
        // user1 events: 1000, 3000, 6000, 11000, 15000
        //   gap 1000→3000 = 2000 — NOT > 3000 → same session
        //   gap 3000→6000 = 3000 — NOT > 3000 → same session
        //   gap 6000→11000 = 5000 > 3000 → new session
        //   gap 11000→15000 = 4000 > 3000 → new session
        //   user1: 3 sessions
        // user2 events: 2000, 7000, 12000, 16000
        //   gap 2000→7000 = 5000 > 3000 → new session
        //   gap 7000→12000 = 5000 > 3000 → new session
        //   gap 12000→16000 = 4000 > 3000 → new session
        //   user2: 4 sessions (each with count=1)
        // Total: 7
        List<StreamWindow.WindowResult<String>> results =
            StreamWindow.sessionWindow(sampleEvents(), 3000L);

        assertEquals(7, results.size(), "Should produce exactly 7 sessions total");
    }

    @Test
    void testSessionWindowUser1HasThreeSessions() {
        List<StreamWindow.WindowResult<String>> results =
            StreamWindow.sessionWindow(sampleEvents(), 3000L);

        long user1Sessions = results.stream()
            .filter(r -> "user1".equals(r.key))
            .count();
        assertEquals(3, user1Sessions, "user1 should have 3 sessions");
    }

    @Test
    void testSessionWindowUser2HasFourSessions() {
        // All user2 gaps (5000, 5000, 4000) are > 3000ms → each event is its own session
        List<StreamWindow.WindowResult<String>> results =
            StreamWindow.sessionWindow(sampleEvents(), 3000L);

        long user2Sessions = results.stream()
            .filter(r -> "user2".equals(r.key))
            .count();
        assertEquals(4, user2Sessions, "user2 should have 4 sessions (each gap > 3000ms)");
    }

    @Test
    void testSessionWindowUser1FirstSessionCountIsThree() {
        // user1 events at 1000, 3000, 6000 — gaps 2000 and 3000 are NOT > 3000 → same session
        List<StreamWindow.WindowResult<String>> results =
            StreamWindow.sessionWindow(sampleEvents(), 3000L);

        StreamWindow.WindowResult<String> firstUser1Session = results.stream()
            .filter(r -> "user1".equals(r.key))
            .min(java.util.Comparator.comparingLong(r -> r.windowStart))
            .orElse(null);

        assertNotNull(firstUser1Session, "user1 must have at least one session");
        assertEquals(3L, firstUser1Session.count,
            "user1's first session should contain 3 events (1000, 3000, 6000)");
    }

    @Test
    void testSessionWindowUser2AllSessionsHaveCountOne() {
        // Every user2 gap is > 3000ms, so each event becomes its own session of count=1
        List<StreamWindow.WindowResult<String>> results =
            StreamWindow.sessionWindow(sampleEvents(), 3000L);

        results.stream()
            .filter(r -> "user2".equals(r.key))
            .forEach(r -> assertEquals(1L, r.count,
                "Each user2 session should have count=1 (all gaps > 3000ms)"));
    }

    @Test
    void testSessionWindowUser1LaterSessionsHaveCountOne() {
        // After the first session [1000,6000], user1's remaining events are isolated:
        // 11000 (gap from 6000 = 5000 > 3000) and 15000 (gap from 11000 = 4000 > 3000)
        List<StreamWindow.WindowResult<String>> results =
            StreamWindow.sessionWindow(sampleEvents(), 3000L);

        List<StreamWindow.WindowResult<String>> user1Sessions = results.stream()
            .filter(r -> "user1".equals(r.key))
            .sorted(java.util.Comparator.comparingLong(r -> r.windowStart))
            .collect(java.util.stream.Collectors.toList());

        // Sessions after the first should each contain exactly 1 event
        for (int i = 1; i < user1Sessions.size(); i++) {
            assertEquals(1L, user1Sessions.get(i).count,
                "user1 session " + i + " should contain 1 event");
        }
    }

    @Test
    void testSessionWindowAllSessionWindowEndGeqStart() {
        List<StreamWindow.WindowResult<String>> results =
            StreamWindow.sessionWindow(sampleEvents(), 3000L);

        for (StreamWindow.WindowResult<String> r : results) {
            assertTrue(r.windowEnd >= r.windowStart,
                "windowEnd must be >= windowStart for all sessions");
        }
    }

    @Test
    void testSessionWindowEmptyInputReturnsEmptyList() {
        List<StreamWindow.WindowResult<String>> results =
            StreamWindow.sessionWindow(Collections.emptyList(), 3000L);

        assertNotNull(results);
        assertTrue(results.isEmpty(), "Empty input should produce empty session list");
    }
}

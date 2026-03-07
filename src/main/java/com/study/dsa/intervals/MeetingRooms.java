package com.study.dsa.intervals;

import java.util.*;

public class MeetingRooms {

    /**
     * Problem: Meeting Rooms I — can one person attend all?
     * Time: O(n log n), Space: O(1)
     *
     * TODO: Sort by start time, check if any consecutive pair overlaps
     * Overlap condition: intervals[i][0] < intervals[i-1][1]
     */
    public static boolean canAttendMeetings(int[][] intervals) {
        // TODO: Sort and check consecutive pairs

        return true; // Replace with implementation
    }

    /**
     * Problem: Meeting Rooms II — minimum rooms needed
     * Time: O(n log n), Space: O(n)
     *
     * TODO: Method 1 (min-heap): sort by start time; use heap of end times
     * If heap.peek() <= current start, reuse that room (poll + offer)
     * Otherwise add a new room (just offer)
     * Answer: heap.size() at the end
     *
     * TODO: Method 2 (two sorted arrays): sort starts and ends separately
     * Two pointers: if starts[i] < ends[j], need a new room; else free one
     */
    public static int minMeetingRooms(int[][] intervals) {
        // TODO: Implement either method

        return 0; // Replace with implementation
    }
}

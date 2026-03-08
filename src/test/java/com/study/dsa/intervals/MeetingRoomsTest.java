package com.study.dsa.intervals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MeetingRoomsTest {

    // ---- canAttendMeetings ----------------------------------------------

    @Test
    void testCanAttendMeetingsOverlapping() {
        // [[0,30],[5,10],[15,20]]: 0-30 overlaps with 5-10 -> false
        assertFalse(MeetingRooms.canAttendMeetings(new int[][]{{0,30},{5,10},{15,20}}));
    }

    @Test
    void testCanAttendMeetingsNoOverlap() {
        // [[7,10],[2,4]]: sorted -> [2,4],[7,10] -> no overlap -> true
        assertTrue(MeetingRooms.canAttendMeetings(new int[][]{{7,10},{2,4}}));
    }

    @Test
    void testCanAttendMeetingsContainedOverlap() {
        // [[9,10],[4,9],[4,17]]: [4,9] and [4,17] share start -> overlap -> false
        assertFalse(MeetingRooms.canAttendMeetings(new int[][]{{9,10},{4,9},{4,17}}));
    }

    @Test
    void testCanAttendMeetingsSingleMeeting() {
        // One meeting -> always true
        assertTrue(MeetingRooms.canAttendMeetings(new int[][]{{1,5}}));
    }

    @Test
    void testCanAttendMeetingsAdjacentNoOverlap() {
        // [[1,5],[5,10]]: end of first == start of second -> no overlap (strict <) -> true
        assertTrue(MeetingRooms.canAttendMeetings(new int[][]{{1,5},{5,10}}));
    }

    // ---- minMeetingRooms ------------------------------------------------

    @Test
    void testMinMeetingRoomsThree() {
        // [[0,30],[5,10],[15,20]]: at time 5, two meetings overlap -> at time 15,
        // second ended but third starts while first ongoing -> max concurrent = 2
        assertEquals(2, MeetingRooms.minMeetingRooms(new int[][]{{0,30},{5,10},{15,20}}));
    }

    @Test
    void testMinMeetingRoomsOne() {
        // [[7,10],[2,4]]: no overlap -> 1 room
        assertEquals(1, MeetingRooms.minMeetingRooms(new int[][]{{7,10},{2,4}}));
    }

    @Test
    void testMinMeetingRoomsAllOverlap() {
        // [[9,10],[4,9],[4,17]]: [4,9],[4,17] overlap; [9,10] is within [4,17]
        // At time 4: two start simultaneously -> need 2 rooms; at time 9: third starts before 17 ends
        // Actually: [4,9] ends at 9, [9,10] starts at 9 -> can reuse? depends on strictness
        // Using heap method: [4,9],[4,17],[9,10] sorted by start
        //   add [4,9]: heap=[9] size=1
        //   add [4,17]: heap.peek()=9 > 4 -> new room, heap=[9,17] size=2
        //   add [9,10]: heap.peek()=9 <= 9 -> reuse, poll 9, add 10 -> heap=[10,17] size=2
        // Result: 2
        assertEquals(2, MeetingRooms.minMeetingRooms(new int[][]{{9,10},{4,9},{4,17}}));
    }

    @Test
    void testMinMeetingRoomsSingleMeeting() {
        assertEquals(1, MeetingRooms.minMeetingRooms(new int[][]{{1,5}}));
    }

    @Test
    void testMinMeetingRoomsNonOverlapping() {
        // [[1,4],[5,8],[9,12]]: all sequential -> 1 room
        assertEquals(1, MeetingRooms.minMeetingRooms(new int[][]{{1,4},{5,8},{9,12}}));
    }

    @Test
    void testMinMeetingRoomsAllSimultaneous() {
        // [[1,5],[1,5],[1,5]]: all start and end together -> 3 rooms
        assertEquals(3, MeetingRooms.minMeetingRooms(new int[][]{{1,5},{1,5},{1,5}}));
    }
}

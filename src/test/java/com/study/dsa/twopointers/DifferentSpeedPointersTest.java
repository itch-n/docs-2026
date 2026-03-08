package com.study.dsa.twopointers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DifferentSpeedPointersTest {

    // ---- hasCycle --------------------------------------------------------

    @Test
    void testHasCycleNoCycle() {
        // 1 -> 2 -> 3 -> 4 -> 5
        DifferentSpeedPointers.ListNode head = DifferentSpeedPointers.createList(new int[]{1, 2, 3, 4, 5});
        assertFalse(DifferentSpeedPointers.hasCycle(head));
    }

    @Test
    void testHasCycleWithCycle() {
        // 1 -> 2 -> 3 -> 4 -> 5 -> (back to 3)
        DifferentSpeedPointers.ListNode head = DifferentSpeedPointers.createList(new int[]{1, 2, 3, 4, 5});
        DifferentSpeedPointers.ListNode node3 = head.next.next;       // value 3
        DifferentSpeedPointers.ListNode tail  = head.next.next.next.next; // value 5
        tail.next = node3; // create cycle
        assertTrue(DifferentSpeedPointers.hasCycle(head));
    }

    @Test
    void testHasCycleSingleNode() {
        DifferentSpeedPointers.ListNode head = new DifferentSpeedPointers.ListNode(1);
        assertFalse(DifferentSpeedPointers.hasCycle(head));
    }

    // ---- findMiddle ------------------------------------------------------

    @Test
    void testFindMiddleOddLength() {
        // 1 -> 2 -> 3 -> 4 -> 5  →  middle = 3
        DifferentSpeedPointers.ListNode head = DifferentSpeedPointers.createList(new int[]{1, 2, 3, 4, 5});
        DifferentSpeedPointers.ListNode mid = DifferentSpeedPointers.findMiddle(head);
        assertNotNull(mid);
        assertEquals(3, mid.val);
    }

    @Test
    void testFindMiddleEvenLength() {
        // 1 -> 2 -> 3 -> 4 -> 5 -> 6  →  second middle = 4
        DifferentSpeedPointers.ListNode head = DifferentSpeedPointers.createList(new int[]{1, 2, 3, 4, 5, 6});
        DifferentSpeedPointers.ListNode mid = DifferentSpeedPointers.findMiddle(head);
        assertNotNull(mid);
        assertEquals(4, mid.val);
    }

    @Test
    void testFindMiddleSingleNode() {
        DifferentSpeedPointers.ListNode head = DifferentSpeedPointers.createList(new int[]{42});
        DifferentSpeedPointers.ListNode mid = DifferentSpeedPointers.findMiddle(head);
        assertNotNull(mid);
        assertEquals(42, mid.val);
    }

    // ---- findKthFromEnd --------------------------------------------------

    @Test
    void testFindKthFromEnd1() {
        // 1 -> 2 -> 3 -> 4 -> 5  →  1st from end = 5
        DifferentSpeedPointers.ListNode head = DifferentSpeedPointers.createList(new int[]{1, 2, 3, 4, 5});
        DifferentSpeedPointers.ListNode node = DifferentSpeedPointers.findKthFromEnd(head, 1);
        assertNotNull(node);
        assertEquals(5, node.val);
    }

    @Test
    void testFindKthFromEnd2() {
        // 1 -> 2 -> 3 -> 4 -> 5  →  2nd from end = 4
        DifferentSpeedPointers.ListNode head = DifferentSpeedPointers.createList(new int[]{1, 2, 3, 4, 5});
        DifferentSpeedPointers.ListNode node = DifferentSpeedPointers.findKthFromEnd(head, 2);
        assertNotNull(node);
        assertEquals(4, node.val);
    }

    @Test
    void testFindKthFromEnd3() {
        // 1 -> 2 -> 3 -> 4 -> 5  →  3rd from end = 3
        DifferentSpeedPointers.ListNode head = DifferentSpeedPointers.createList(new int[]{1, 2, 3, 4, 5});
        DifferentSpeedPointers.ListNode node = DifferentSpeedPointers.findKthFromEnd(head, 3);
        assertNotNull(node);
        assertEquals(3, node.val);
    }
}

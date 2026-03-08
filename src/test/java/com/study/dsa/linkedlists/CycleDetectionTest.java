package com.study.dsa.linkedlists;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CycleDetectionTest {

    // ---- hasCycle -------------------------------------------------------

    @Test
    void testHasCycleNoCycle() {
        // 1 -> 2 -> 3 -> 4 -> 5  (no cycle)
        CycleDetection.ListNode head = CycleDetection.createList(new int[]{1, 2, 3, 4, 5});
        assertFalse(CycleDetection.hasCycle(head));
    }

    @Test
    void testHasCycleWithCycle() {
        // 1 -> 2 -> 3 -> 4 -> 5 -> (back to 3)
        CycleDetection.ListNode head = CycleDetection.createList(new int[]{1, 2, 3, 4, 5});
        CycleDetection.ListNode node3 = head.next.next;           // value 3
        CycleDetection.ListNode tail  = head.next.next.next.next; // value 5
        tail.next = node3;
        assertTrue(CycleDetection.hasCycle(head));
    }

    @Test
    void testHasCycleSingleNoSelf() {
        CycleDetection.ListNode head = new CycleDetection.ListNode(1);
        assertFalse(CycleDetection.hasCycle(head));
    }

    // ---- detectCycle ----------------------------------------------------

    @Test
    void testDetectCycleNoCycle() {
        CycleDetection.ListNode head = CycleDetection.createList(new int[]{1, 2, 3, 4, 5});
        assertNull(CycleDetection.detectCycle(head));
    }

    @Test
    void testDetectCycleStartsAt3() {
        // cycle: 5 -> 3, so cycle start = node with value 3
        CycleDetection.ListNode head = CycleDetection.createList(new int[]{1, 2, 3, 4, 5});
        CycleDetection.ListNode node3 = head.next.next;
        CycleDetection.ListNode tail  = head.next.next.next.next;
        tail.next = node3;
        CycleDetection.ListNode start = CycleDetection.detectCycle(head);
        assertNotNull(start);
        assertEquals(3, start.val);
    }

    // ---- removeCycle ----------------------------------------------------

    @Test
    void testRemoveCycle() {
        // After removing cycle, hasCycle should return false
        CycleDetection.ListNode head = CycleDetection.createList(new int[]{1, 2, 3, 4, 5});
        CycleDetection.ListNode node3 = head.next.next;
        CycleDetection.ListNode tail  = head.next.next.next.next;
        tail.next = node3; // create cycle

        assertTrue(CycleDetection.hasCycle(head)); // sanity check cycle exists
        CycleDetection.removeCycle(head);
        assertFalse(CycleDetection.hasCycle(head));
    }
}

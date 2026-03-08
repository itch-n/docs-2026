package com.study.dsa.linkedlists;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReverseLinkedListTest {

    // Helper: collect list values into an array for easy assertion
    private static int[] toArray(ReverseLinkedList.ListNode head) {
        java.util.List<Integer> vals = new java.util.ArrayList<>();
        ReverseLinkedList.ListNode cur = head;
        while (cur != null) {
            vals.add(cur.val);
            cur = cur.next;
        }
        return vals.stream().mapToInt(Integer::intValue).toArray();
    }

    // ---- reverseList ----------------------------------------------------

    @Test
    void testReverseListTypical() {
        // 1 -> 2 -> 3 -> 4 -> 5  →  5 -> 4 -> 3 -> 2 -> 1
        ReverseLinkedList.ListNode head = ReverseLinkedList.createList(new int[]{1, 2, 3, 4, 5});
        ReverseLinkedList.ListNode result = ReverseLinkedList.reverseList(head);
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, toArray(result));
    }

    @Test
    void testReverseListSingleNode() {
        ReverseLinkedList.ListNode head = ReverseLinkedList.createList(new int[]{42});
        ReverseLinkedList.ListNode result = ReverseLinkedList.reverseList(head);
        assertArrayEquals(new int[]{42}, toArray(result));
    }

    // ---- reverseListRecursive -------------------------------------------

    @Test
    void testReverseListRecursiveTypical() {
        // 1 -> 2 -> 3 -> 4 -> 5  →  5 -> 4 -> 3 -> 2 -> 1
        ReverseLinkedList.ListNode head = ReverseLinkedList.createList(new int[]{1, 2, 3, 4, 5});
        ReverseLinkedList.ListNode result = ReverseLinkedList.reverseListRecursive(head);
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, toArray(result));
    }

    @Test
    void testReverseListRecursiveTwoNodes() {
        ReverseLinkedList.ListNode head = ReverseLinkedList.createList(new int[]{1, 2});
        ReverseLinkedList.ListNode result = ReverseLinkedList.reverseListRecursive(head);
        assertArrayEquals(new int[]{2, 1}, toArray(result));
    }

    // ---- reverseFirstK --------------------------------------------------

    @Test
    void testReverseFirstKTypical() {
        // 1->2->3->4->5->6->7, k=3  →  3->2->1->4->5->6->7
        ReverseLinkedList.ListNode head = ReverseLinkedList.createList(new int[]{1, 2, 3, 4, 5, 6, 7});
        ReverseLinkedList.ListNode result = ReverseLinkedList.reverseFirstK(head, 3);
        assertArrayEquals(new int[]{3, 2, 1, 4, 5, 6, 7}, toArray(result));
    }

    @Test
    void testReverseFirstK1() {
        // k=1  →  no change
        ReverseLinkedList.ListNode head = ReverseLinkedList.createList(new int[]{1, 2, 3});
        ReverseLinkedList.ListNode result = ReverseLinkedList.reverseFirstK(head, 1);
        assertArrayEquals(new int[]{1, 2, 3}, toArray(result));
    }

    @Test
    void testReverseFirstKAll() {
        // k = entire list length  →  fully reversed
        ReverseLinkedList.ListNode head = ReverseLinkedList.createList(new int[]{1, 2, 3});
        ReverseLinkedList.ListNode result = ReverseLinkedList.reverseFirstK(head, 3);
        assertArrayEquals(new int[]{3, 2, 1}, toArray(result));
    }
}

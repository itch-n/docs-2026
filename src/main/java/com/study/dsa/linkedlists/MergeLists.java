package com.study.dsa.linkedlists;

import java.util.*;

public class MergeLists {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Merge two sorted linked lists
     * Time: O(n + m), Space: O(1)
     *
     * TODO: Implement iterative merge
     */
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        // TODO: Create dummy node to simplify edge cases

        // TODO: Implement iteration/conditional logic

        // TODO: Attach remaining nodes from non-empty list

        // TODO: Return dummy.next

        return null; // Replace with implementation
    }

    /**
     * Problem: Merge two sorted lists recursively
     * Time: O(n + m), Space: O(n + m) due to recursion
     *
     * TODO: Implement recursive merge
     */
    public static ListNode mergeTwoListsRecursive(ListNode l1, ListNode l2) {
        // TODO: Base cases: if l1 null return l2, if l2 null return l1

        // TODO: Compare values:

        return null; // Replace with implementation
    }

    /**
     * Problem: Merge K sorted linked lists
     * Time: O(N log k) where N = total nodes, k = number of lists
     * Space: O(k) for priority queue
     *
     * TODO: Implement using min heap (PriorityQueue)
     */
    public static ListNode mergeKLists(ListNode[] lists) {
        // TODO: Create PriorityQueue with custom comparator

        // TODO: Add all list heads to queue

        // TODO: Create dummy node

        // TODO: Implement iteration/conditional logic

        // TODO: Return dummy.next

        return null; // Replace with implementation
    }

    // Helper: Create list
    static ListNode createList(int[] values) {
        if (values.length == 0) return null;
        ListNode head = new ListNode(values[0]);
        ListNode current = head;
        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
        }
        return head;
    }

    // Helper: Print list
    static void printList(ListNode head) {
        ListNode current = head;
        while (current != null) {
            System.out.print(current.val);
            if (current.next != null) System.out.print(" -> ");
            current = current.next;
        }
        System.out.println();
    }
}

package com.study.dsa.twopointers;

public class DifferentSpeedPointers {

    // Simple ListNode definition
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Detect cycle in linked list
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using slow/fast pointers
     */
    public static boolean hasCycle(ListNode head) {
        // TODO: Move pointers at different speeds
        // What happens when they meet?

        return false; // Replace with implementation
    }

    /**
     * Problem: Find middle of linked list
     * If even length, return second middle node
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using slow/fast pointers
     */
    public static ListNode findMiddle(ListNode head) {
        // TODO: Use different pointer speeds
        // Where is the slow pointer when fast reaches the end?

        return null; // Replace with implementation
    }

    /**
     * Problem: Find kth node from end
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using two pointers with gap
     */
    public static ListNode findKthFromEnd(ListNode head, int k) {
        // TODO: Create a fixed gap between pointers
        // When one reaches the end, where is the other?

        return null; // Replace with implementation
    }

    // Helper: Create linked list from array
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

    // Helper: Print linked list
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

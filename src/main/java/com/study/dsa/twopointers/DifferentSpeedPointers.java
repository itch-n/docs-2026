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
     */
    public static boolean hasCycle(ListNode head) {
        if (head == null) return false;

        ListNode slow = head;
        ListNode fast = head;

        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
            if (fast != null) {
                fast = fast.next;
            }
            if (slow == fast) return true;
        }

        return false;
    }

    /**
     * Problem: Find middle of linked list
     * If even length, return second middle node
     * Time: O(n), Space: O(1)
     */
    public static ListNode findMiddle(ListNode head) {
        // this handles the "rounding up" behavior
        ListNode sentinel = new ListNode(-1);
        sentinel.next = head;

        ListNode slow = sentinel;
        ListNode fast = sentinel;

        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
            if (fast != null) {
                fast = fast.next;
            }
        }
        return slow ;
    }

    /**
     * Problem: Find kth node from end
     * Time: O(n), Space: O(1)
     */
    public static ListNode findKthFromEnd(ListNode head, int k) {
        if (head == null) throw new IllegalArgumentException();
        if (k <= 0) throw new IllegalArgumentException();

        ListNode boat = head;
        for (int i = 0; i <= k; i++) {
            boat = boat.next;
        }

        ListNode waterskiier = head;
        while (boat != null) {
            boat = boat.next;
            waterskiier = waterskiier.next;
        }
        return waterskiier;
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

package com.study.dsa.heaps;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class MergeKSortedTest {

    // Helper: collect linked list values into a List for easy assertion
    private List<Integer> toList(MergeKSorted.ListNode head) {
        List<Integer> result = new java.util.ArrayList<>();
        MergeKSorted.ListNode cur = head;
        while (cur != null) {
            result.add(cur.val);
            cur = cur.next;
        }
        return result;
    }

    @Test
    void testMergeKLists() {
        // Lists: [1,4,5], [1,3,4], [2,6]
        // Merged: [1,1,2,3,4,4,5,6]
        MergeKSorted.ListNode[] lists = new MergeKSorted.ListNode[3];
        lists[0] = MergeKSorted.createList(new int[]{1, 4, 5});
        lists[1] = MergeKSorted.createList(new int[]{1, 3, 4});
        lists[2] = MergeKSorted.createList(new int[]{2, 6});
        MergeKSorted.ListNode merged = MergeKSorted.mergeKLists(lists);
        assertEquals(List.of(1, 1, 2, 3, 4, 4, 5, 6), toList(merged));
    }

    @Test
    void testMergeKListsEmpty() {
        MergeKSorted.ListNode[] lists = new MergeKSorted.ListNode[0];
        assertNull(MergeKSorted.mergeKLists(lists));
    }

    @Test
    void testMergeKListsSingleList() {
        MergeKSorted.ListNode[] lists = new MergeKSorted.ListNode[]{
            MergeKSorted.createList(new int[]{1, 2, 3})
        };
        assertEquals(List.of(1, 2, 3), toList(MergeKSorted.mergeKLists(lists)));
    }

    @Test
    void testMergeKArrays() {
        // Arrays: [1,3,5,7], [2,4,6,8], [0,9,10,11]
        // Merged: [0,1,2,3,4,5,6,7,8,9,10,11]
        int[][] arrays = {
            {1, 3, 5, 7},
            {2, 4, 6, 8},
            {0, 9, 10, 11}
        };
        List<Integer> merged = MergeKSorted.mergeKArrays(arrays);
        assertEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), merged);
    }

    @Test
    void testSmallestRange() {
        // Lists: [4,10,15,24,26], [0,9,12,20], [5,18,22,30]
        // Smallest range: [20, 24]
        List<List<Integer>> nums = Arrays.asList(
            Arrays.asList(4, 10, 15, 24, 26),
            Arrays.asList(0, 9, 12, 20),
            Arrays.asList(5, 18, 22, 30)
        );
        int[] range = MergeKSorted.smallestRange(nums);
        assertArrayEquals(new int[]{20, 24}, range);
    }
}

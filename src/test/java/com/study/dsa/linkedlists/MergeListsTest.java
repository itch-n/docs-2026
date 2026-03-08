package com.study.dsa.linkedlists;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MergeListsTest {

    private static int[] toArray(MergeLists.ListNode head) {
        java.util.List<Integer> vals = new java.util.ArrayList<>();
        MergeLists.ListNode cur = head;
        while (cur != null) {
            vals.add(cur.val);
            cur = cur.next;
        }
        return vals.stream().mapToInt(Integer::intValue).toArray();
    }

    // ---- mergeTwoLists --------------------------------------------------

    @Test
    void testMergeTwoListsTypical() {
        // {1,3,5,7} + {2,4,6,8}  →  {1,2,3,4,5,6,7,8}
        MergeLists.ListNode l1 = MergeLists.createList(new int[]{1, 3, 5, 7});
        MergeLists.ListNode l2 = MergeLists.createList(new int[]{2, 4, 6, 8});
        MergeLists.ListNode result = MergeLists.mergeTwoLists(l1, l2);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, toArray(result));
    }

    @Test
    void testMergeTwoListsOneEmpty() {
        MergeLists.ListNode l1 = MergeLists.createList(new int[]{1, 2, 3});
        MergeLists.ListNode l2 = null;
        MergeLists.ListNode result = MergeLists.mergeTwoLists(l1, l2);
        assertArrayEquals(new int[]{1, 2, 3}, toArray(result));
    }

    @Test
    void testMergeTwoListsBothEmpty() {
        MergeLists.ListNode result = MergeLists.mergeTwoLists(null, null);
        assertNull(result);
    }

    // ---- mergeTwoListsRecursive -----------------------------------------

    @Test
    void testMergeTwoListsRecursiveTypical() {
        // {1,2,4} + {1,3,4}  →  {1,1,2,3,4,4}
        MergeLists.ListNode l3 = MergeLists.createList(new int[]{1, 2, 4});
        MergeLists.ListNode l4 = MergeLists.createList(new int[]{1, 3, 4});
        MergeLists.ListNode result = MergeLists.mergeTwoListsRecursive(l3, l4);
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4}, toArray(result));
    }

    @Test
    void testMergeTwoListsRecursiveOneEmpty() {
        MergeLists.ListNode result = MergeLists.mergeTwoListsRecursive(
            null, MergeLists.createList(new int[]{5}));
        assertArrayEquals(new int[]{5}, toArray(result));
    }

    // ---- mergeKLists ----------------------------------------------------

    @Test
    void testMergeKListsTypical() {
        // {1,4,5} + {1,3,4} + {2,6}  →  {1,1,2,3,4,4,5,6}
        MergeLists.ListNode[] lists = {
            MergeLists.createList(new int[]{1, 4, 5}),
            MergeLists.createList(new int[]{1, 3, 4}),
            MergeLists.createList(new int[]{2, 6})
        };
        MergeLists.ListNode result = MergeLists.mergeKLists(lists);
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4, 5, 6}, toArray(result));
    }

    @Test
    void testMergeKListsEmpty() {
        MergeLists.ListNode result = MergeLists.mergeKLists(new MergeLists.ListNode[]{});
        assertNull(result);
    }

    @Test
    void testMergeKListsSingleList() {
        MergeLists.ListNode[] lists = {MergeLists.createList(new int[]{1, 2, 3})};
        MergeLists.ListNode result = MergeLists.mergeKLists(lists);
        assertArrayEquals(new int[]{1, 2, 3}, toArray(result));
    }
}

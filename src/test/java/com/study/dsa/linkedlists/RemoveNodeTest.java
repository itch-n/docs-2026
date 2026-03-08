package com.study.dsa.linkedlists;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RemoveNodeTest {

    private static int[] toArray(RemoveNode.ListNode head) {
        java.util.List<Integer> vals = new java.util.ArrayList<>();
        RemoveNode.ListNode cur = head;
        while (cur != null) {
            vals.add(cur.val);
            cur = cur.next;
        }
        return vals.stream().mapToInt(Integer::intValue).toArray();
    }

    // ---- removeNthFromEnd -----------------------------------------------

    @Test
    void testRemoveNthFromEndMiddle() {
        // {1,2,3,4,5}, n=2  →  remove node with value 4  →  {1,2,3,5}
        RemoveNode.ListNode list = RemoveNode.createList(new int[]{1, 2, 3, 4, 5});
        RemoveNode.ListNode result = RemoveNode.removeNthFromEnd(list, 2);
        assertArrayEquals(new int[]{1, 2, 3, 5}, toArray(result));
    }

    @Test
    void testRemoveNthFromEndFirst() {
        // {1,2,3,4,5}, n=5  →  remove first node  →  {2,3,4,5}
        RemoveNode.ListNode list = RemoveNode.createList(new int[]{1, 2, 3, 4, 5});
        RemoveNode.ListNode result = RemoveNode.removeNthFromEnd(list, 5);
        assertArrayEquals(new int[]{2, 3, 4, 5}, toArray(result));
    }

    @Test
    void testRemoveNthFromEndLast() {
        // {1,2,3,4,5}, n=1  →  remove last node  →  {1,2,3,4}
        RemoveNode.ListNode list = RemoveNode.createList(new int[]{1, 2, 3, 4, 5});
        RemoveNode.ListNode result = RemoveNode.removeNthFromEnd(list, 1);
        assertArrayEquals(new int[]{1, 2, 3, 4}, toArray(result));
    }

    @Test
    void testRemoveNthFromEndSingleNode() {
        // {1}, n=1  →  empty list (null)
        RemoveNode.ListNode list = RemoveNode.createList(new int[]{1});
        RemoveNode.ListNode result = RemoveNode.removeNthFromEnd(list, 1);
        assertNull(result);
    }

    // ---- deleteDuplicates -----------------------------------------------

    @Test
    void testDeleteDuplicatesTypical() {
        // {1,1,2,3,3,3,4,5,5}  →  {1,2,3,4,5}
        RemoveNode.ListNode list = RemoveNode.createList(new int[]{1, 1, 2, 3, 3, 3, 4, 5, 5});
        RemoveNode.ListNode result = RemoveNode.deleteDuplicates(list);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, toArray(result));
    }

    @Test
    void testDeleteDuplicatesNoDuplicates() {
        RemoveNode.ListNode list = RemoveNode.createList(new int[]{1, 2, 3});
        RemoveNode.ListNode result = RemoveNode.deleteDuplicates(list);
        assertArrayEquals(new int[]{1, 2, 3}, toArray(result));
    }

    @Test
    void testDeleteDuplicatesAllSame() {
        RemoveNode.ListNode list = RemoveNode.createList(new int[]{5, 5, 5});
        RemoveNode.ListNode result = RemoveNode.deleteDuplicates(list);
        assertArrayEquals(new int[]{5}, toArray(result));
    }

}

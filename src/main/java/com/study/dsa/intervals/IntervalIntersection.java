package com.study.dsa.intervals;

import java.util.*;

public class IntervalIntersection {

    /**
     * Problem: Interval list intersection
     * Time: O(m + n), Space: O(min(m,n))
     *
     * TODO: Implement two-pointer intersection
     * An intersection exists when max(start1,start2) <= min(end1,end2)
     * Advance the pointer whose interval ends first
     */
    public static int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        List<int[]> result = new ArrayList<>();
        int i = 0, j = 0;

        // TODO: Advance both pointers, check for intersection each step
        // TODO: Move pointer of interval that ends first

        return result.toArray(new int[0][]);
    }
}

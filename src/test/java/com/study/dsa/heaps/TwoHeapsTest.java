package com.study.dsa.heaps;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TwoHeapsTest {

    @Test
    void testMedianFinderOdd() {
        // After adding 1, 2, 3: median = 2.0
        TwoHeaps.MedianFinder mf = new TwoHeaps.MedianFinder();
        mf.addNum(1);
        assertEquals(1.0, mf.findMedian(), 0.001);
        mf.addNum(2);
        assertEquals(1.5, mf.findMedian(), 0.001);
        mf.addNum(3);
        assertEquals(2.0, mf.findMedian(), 0.001);
    }

    @Test
    void testMedianFinderEven() {
        // After adding 1,2,3,4,5: medians are 1.0, 1.5, 2.0, 2.5, 3.0
        TwoHeaps.MedianFinder mf = new TwoHeaps.MedianFinder();
        mf.addNum(1);
        assertEquals(1.0, mf.findMedian(), 0.001);
        mf.addNum(2);
        assertEquals(1.5, mf.findMedian(), 0.001);
        mf.addNum(3);
        assertEquals(2.0, mf.findMedian(), 0.001);
        mf.addNum(4);
        assertEquals(2.5, mf.findMedian(), 0.001);
        mf.addNum(5);
        assertEquals(3.0, mf.findMedian(), 0.001);
    }

    @Test
    void testMedianFinderStream2() {
        // Stream: 5, 15, 1, 3
        // After 5:      median = 5.0
        // After 5,15:   median = 10.0
        // After 5,15,1: sorted=[1,5,15] median = 5.0
        // After 5,15,1,3: sorted=[1,3,5,15] median = (3+5)/2 = 4.0
        TwoHeaps.MedianFinder mf = new TwoHeaps.MedianFinder();
        mf.addNum(5);
        assertEquals(5.0, mf.findMedian(), 0.001);
        mf.addNum(15);
        assertEquals(10.0, mf.findMedian(), 0.001);
        mf.addNum(1);
        assertEquals(5.0, mf.findMedian(), 0.001);
        mf.addNum(3);
        assertEquals(4.0, mf.findMedian(), 0.001);
    }

    @Test
    void testMedianSlidingWindow() {
        // nums = [1, 3, -1, -3, 5, 3, 6, 7], k = 3
        // Window medians:
        //   [1,3,-1]  sorted=[-1,1,3]   median=1.0
        //   [3,-1,-3] sorted=[-3,-1,3]  median=-1.0
        //   [-1,-3,5] sorted=[-3,-1,5]  median=-1.0
        //   [-3,5,3]  sorted=[-3,3,5]   median=3.0
        //   [5,3,6]   sorted=[3,5,6]    median=5.0
        //   [3,6,7]   sorted=[3,6,7]    median=6.0
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        double[] medians = TwoHeaps.medianSlidingWindow(nums, 3);
        assertEquals(6, medians.length);
        assertEquals(1.0,  medians[0], 0.001);
        assertEquals(-1.0, medians[1], 0.001);
        assertEquals(-1.0, medians[2], 0.001);
        assertEquals(3.0,  medians[3], 0.001);
        assertEquals(5.0,  medians[4], 0.001);
        assertEquals(6.0,  medians[5], 0.001);
    }
}

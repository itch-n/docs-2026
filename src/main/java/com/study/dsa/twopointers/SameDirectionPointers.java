package com.study.dsa.twopointers;

import java.util.Arrays;

public class SameDirectionPointers {

    /**
     * Problem: Remove duplicates from sorted array in-place
     * Return new length
     * Time: O(n), Space: O(1)
     * <p>
     */
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;

        int write = 1;
        for (int read = 1; read < nums.length; read++) {
            // duplicate, skip this write
            if (nums[read] == nums[write - 1]) {
                continue;
            }
            nums[write] = nums[read];
            write++;
        }
        return write; // new length
    }

    /**
     * Problem: Move all zeros to end, maintain order of non-zeros
     * Time: O(n), Space: O(1)
     * <p>
     */
    public static void moveZeroes(int[] nums) {
        int write = 0;
        for (int read = 0; read < nums.length; read++) {
            if (nums[read] == 0) {
                continue;
            }
            nums[write] = nums[read];
            write++;
        }

        while (write < nums.length) {
            nums[write] = 0;
            write++;
        }
    }

    /**
     * Problem: Partition array - all elements < pivot go left
     * Time: O(n), Space: O(1)
     * <p>
     * <pre>
     *        [  Condition Met  |  Unmet/Mixed  |  Unprocessed  ]
     *           0           slow-1 slow          fast          n-1
     *           ↓             ↓     ↓             ↓             ↓
     * Array: [  2  1  4  3  0  |  9  8  7  6  5  |  ?  ?  ?  ?  ]
     *           ↑             ↑                 ↑             ↑
     *           └─────┬───────┘                 └──────┬──────┘
     *          Elements ≤ Pivot                 Current Element
     *          (The "Good" Zone)               Being Evaluated
     * </pre>
     */
    public static int partition(int[] arr, int pivot) {
        int wall = 0; // "good zone indices < wall"
        for (int fast = 0; fast < arr.length; fast++) {
            if (arr[fast] >= pivot) {
                continue;
            }

            int tmp = arr[fast];
            arr[fast] = arr[wall];
            arr[wall] = tmp;
            wall++;
        }

        return wall; // Replace with implementation
    }
}

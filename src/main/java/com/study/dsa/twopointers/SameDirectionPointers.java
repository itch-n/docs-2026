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


    public static void main(String[] args) {
        System.out.println("=== Same Direction Two Pointers ===\n");

        // Test 1: Remove duplicates
        System.out.println("--- Test 1: Remove Duplicates ---");
        int[] arr1 = {1, 1, 2, 2, 2, 3, 4, 4, 5};
        System.out.println("Before: " + Arrays.toString(arr1));

        int newLength = SameDirectionPointers.removeDuplicates(arr1);
        System.out.println("After:  " + Arrays.toString(Arrays.copyOf(arr1, newLength)));
        System.out.println("New length: " + newLength);

        // Test 2: Move zeros
        System.out.println("\n--- Test 2: Move Zeros ---");
        int[] arr2 = {0, 1, 0, 3, 12, 0, 5};
        System.out.println("Before: " + Arrays.toString(arr2));
        SameDirectionPointers.moveZeroes(arr2);
        System.out.println("After:  " + Arrays.toString(arr2));

        // Test 3: Partition
        System.out.println("\n--- Test 3: Partition ---");
        int[] arr3 = {7, 2, 9, 1, 5, 3, 8};
        int pivot = 5;
        System.out.println("Before: " + Arrays.toString(arr3));
        System.out.println("Pivot:  " + pivot);

        int partitionIdx = SameDirectionPointers.partition(arr3, pivot);
        System.out.println("After:  " + Arrays.toString(arr3));
        System.out.println("Partition index: " + partitionIdx);
        System.out.println("(All elements before index " + partitionIdx + " are < " + pivot + ")");
    }
}
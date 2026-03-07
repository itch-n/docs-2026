package com.study.dsa.twopointers;

public class OppositeDirectionPointers {

    /**
     * Problem: Check if string is a palindrome
     * Time: O(n), Space: O(1)
     */
    public static boolean isPalindrome(String s) {
        int l = 0;
        int r = s.length() - 1;
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true; // Replace with implementation
    }

    /**
     * Problem: Find pair in sorted array that sums to target
     * Time: O(n), Space: O(1)
     * <p>
     */
    public static int[] twoSum(int[] nums, int target) {
        int l = 0;
        int r = nums.length - 1;
        while (l < r) {
            int sum = nums[l] + nums[r];
            if (sum == target) {
                return new int[]{l, r};
            } else if (sum < target) {
                l++;
            } else {
                r--;
            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    /**
     * Problem: Reverse array in-place
     * Time: O(n), Space: O(1)
     * <p>
     */
    public static void reverseArray(int[] arr) {
        int l = 0;
        int r = arr.length - 1;
        while (l < r) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
            l++;
            r--;
        }
    }
}

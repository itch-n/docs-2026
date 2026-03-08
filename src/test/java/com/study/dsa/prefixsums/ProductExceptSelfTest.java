package com.study.dsa.prefixsums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductExceptSelfTest {

    // ---- productExceptSelf ----------------------------------------------

    @Test
    void testProductExceptSelf1234() {
        // [1,2,3,4] -> [2*3*4, 1*3*4, 1*2*4, 1*2*3] = [24,12,8,6]
        assertArrayEquals(
            new int[]{24, 12, 8, 6},
            ProductExceptSelf.productExceptSelf(new int[]{1, 2, 3, 4})
        );
    }

    @Test
    void testProductExceptSelfWithZero() {
        // [-1,1,0,-3,3]
        // Product of all = 0
        // Only the zero-position element is non-zero (it gets the product of everything else)
        // result[2] = (-1)*1*(-3)*3 = 9
        // all others get 0 (since one zero in remaining product)
        assertArrayEquals(
            new int[]{0, 0, 9, 0, 0},
            ProductExceptSelf.productExceptSelf(new int[]{-1, 1, 0, -3, 3})
        );
    }

    @Test
    void testProductExceptSelfTwoElements() {
        // [2,3] -> [3,2]
        assertArrayEquals(
            new int[]{3, 2},
            ProductExceptSelf.productExceptSelf(new int[]{2, 3})
        );
    }

    @Test
    void testProductExceptSelfWithOnes() {
        // [1,1,1,1] -> [1,1,1,1]
        assertArrayEquals(
            new int[]{1, 1, 1, 1},
            ProductExceptSelf.productExceptSelf(new int[]{1, 1, 1, 1})
        );
    }

    @Test
    void testProductExceptSelfNegatives() {
        // [-1,-2,-3,-4] -> [(-2)*(-3)*(-4), (-1)*(-3)*(-4), (-1)*(-2)*(-4), (-1)*(-2)*(-3)]
        //                = [-24, -12, -8, -6]
        assertArrayEquals(
            new int[]{-24, -12, -8, -6},
            ProductExceptSelf.productExceptSelf(new int[]{-1, -2, -3, -4})
        );
    }

    @Test
    void testProductExceptSelfIncludesLarger() {
        // [2,3,4] -> [12,8,6]
        assertArrayEquals(
            new int[]{12, 8, 6},
            ProductExceptSelf.productExceptSelf(new int[]{2, 3, 4})
        );
    }
}

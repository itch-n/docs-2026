package com.study.dsa.stacksqueues;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BasicStackTest {

    // ---- isValid --------------------------------------------------------

    @Test
    void testIsValidEmpty() {
        assertTrue(BasicStack.isValid("()"));
    }

    @Test
    void testIsValidMixed() {
        assertTrue(BasicStack.isValid("()[]{}"));
    }

    @Test
    void testIsValidNested() {
        assertTrue(BasicStack.isValid("{[]}"));
    }

    @Test
    void testIsValidMismatch() {
        assertFalse(BasicStack.isValid("(]"));
    }

    @Test
    void testIsValidInterleaved() {
        assertFalse(BasicStack.isValid("([)]"));
    }

    @Test
    void testIsValidEmptyString() {
        assertTrue(BasicStack.isValid(""));
    }

    // ---- evalRPN --------------------------------------------------------

    @Test
    void testEvalRPNAddThenMultiply() {
        // ["2","1","+","3","*"]  →  (2+1)*3 = 9
        assertEquals(9, BasicStack.evalRPN(new String[]{"2", "1", "+", "3", "*"}));
    }

    @Test
    void testEvalRPNDivideAndAdd() {
        // ["4","13","5","/","+"]  →  4 + (13/5) = 4 + 2 = 6
        assertEquals(6, BasicStack.evalRPN(new String[]{"4", "13", "5", "/", "+"}));
    }

    @Test
    void testEvalRPNSingleNumber() {
        assertEquals(42, BasicStack.evalRPN(new String[]{"42"}));
    }

    @Test
    void testEvalRPNSubtraction() {
        // ["5","3","-"]  →  5-3 = 2
        assertEquals(2, BasicStack.evalRPN(new String[]{"5", "3", "-"}));
    }

    // ---- MinStack -------------------------------------------------------

    @Test
    void testMinStackOperations() {
        // push(-2), push(0), push(-3)  →  getMin() = -3
        BasicStack.MinStack minStack = new BasicStack.MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        assertEquals(-3, minStack.getMin());

        // pop()  →  top() = 0, getMin() = -2
        minStack.pop();
        assertEquals(0, minStack.top());
        assertEquals(-2, minStack.getMin());
    }

    @Test
    void testMinStackSingleElement() {
        BasicStack.MinStack minStack = new BasicStack.MinStack();
        minStack.push(5);
        assertEquals(5, minStack.top());
        assertEquals(5, minStack.getMin());
    }

    @Test
    void testMinStackMinAfterPop() {
        BasicStack.MinStack minStack = new BasicStack.MinStack();
        minStack.push(3);
        minStack.push(1);
        minStack.push(2);
        assertEquals(1, minStack.getMin());
        minStack.pop(); // remove 2
        assertEquals(1, minStack.getMin());
        minStack.pop(); // remove 1
        assertEquals(3, minStack.getMin());
    }
}

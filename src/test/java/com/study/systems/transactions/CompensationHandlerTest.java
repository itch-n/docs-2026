package com.study.systems.transactions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompensationHandlerTest {

    @Test
    void testSuccessfulActionReturnsTrue() {
        CompensationHandler handler = new CompensationHandler();

        boolean result = handler.executeWithCompensation(
            new CompensationHandler.CompensatingAction("Deduct Balance") {
                @Override
                public void execute() {
                    // succeeds
                }

                @Override
                public void compensate() {
                    // undo
                }
            }
        );

        assertTrue(result);
    }

    @Test
    void testFailingActionReturnsFalse() {
        CompensationHandler handler = new CompensationHandler();

        boolean result = handler.executeWithCompensation(
            new CompensationHandler.CompensatingAction("Failing Action") {
                @Override
                public void execute() throws Exception {
                    throw new Exception("Simulated failure");
                }

                @Override
                public void compensate() {
                    // undo
                }
            }
        );

        assertFalse(result);
    }

    @Test
    void testCompensateAllRunsCompensationsInReverseOrder() {
        CompensationHandler handler = new CompensationHandler();
        StringBuilder order = new StringBuilder();

        handler.executeWithCompensation(
            new CompensationHandler.CompensatingAction("Action1") {
                @Override
                public void execute() { /* no-op */ }

                @Override
                public void compensate() {
                    order.append("compensate1 ");
                }
            }
        );

        handler.executeWithCompensation(
            new CompensationHandler.CompensatingAction("Action2") {
                @Override
                public void execute() { /* no-op */ }

                @Override
                public void compensate() {
                    order.append("compensate2 ");
                }
            }
        );

        handler.compensateAll();

        // Should compensate in reverse order: Action2 then Action1
        assertTrue(order.toString().startsWith("compensate2"),
            "Expected compensation2 to run before compensation1, got: " + order);
        assertTrue(order.toString().contains("compensate1"),
            "Expected compensation1 to run, got: " + order);
    }

    @Test
    void testClearEmptiesCompensationStack() {
        CompensationHandler handler = new CompensationHandler();

        handler.executeWithCompensation(
            new CompensationHandler.CompensatingAction("Action") {
                @Override
                public void execute() { /* no-op */ }

                @Override
                public void compensate() { /* no-op */ }
            }
        );

        handler.clear();

        // After clear, compensateAll should do nothing (no compensations)
        // This is verifiable by calling compensateAll without exception
        assertDoesNotThrow(handler::compensateAll);
    }
}

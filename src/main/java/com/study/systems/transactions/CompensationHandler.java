package com.study.systems.transactions;

import java.util.*;
/**
 * Compensation: Undo completed operations on failure
 *
 * Key principles:
 * - Each operation has compensating action
 * - Compensation executed in reverse order
 * - Semantic rollback (not physical)
 * - Eventually consistent
 */

public class CompensationHandler {

    private final Stack<CompensatingAction> completedActions;

    /**
     * Initialize compensation handler
     *
     * TODO: Initialize action stack
     */
    public CompensationHandler() {
        // TODO: Create stack for completed actions
        this.completedActions = null; // Replace
    }

    /**
     * Execute action and record for compensation
     *
     * @param action Action to execute
     * @return true if successful
     *
     * TODO: Execute and record action
     * - Try to execute action
     * - If success, push to stack
     * - If failure, return false
     */
    public boolean executeWithCompensation(CompensatingAction action) {
        try {
            System.out.println("Executing: " + action.getName());
            // TODO: Execute action
            // action.execute()

            // TODO: Push to stack for potential compensation

            return true;

        } catch (Exception e) {
            System.out.println("Action failed: " + action.getName());
            return false;
        }
    }

    /**
     * Compensate all completed actions
     *
     * TODO: Execute compensating actions in reverse order
     * - Pop actions from stack
     * - Execute compensation for each
     * - Handle compensation failures
     */
    public void compensateAll() {
        System.out.println("Starting compensation");

        // TODO: Implement iteration/conditional logic

        while (!completedActions.isEmpty()) {
            CompensatingAction action = completedActions.pop();
            try {
                System.out.println("Compensating: " + action.getName());
                // TODO: Execute compensation
                // action.compensate()
            } catch (Exception e) {
                System.out.println("Compensation failed: " + action.getName());
                // TODO: Log failure but continue compensating
            }
        }
    }

    /**
     * Clear compensation stack (after successful completion)
     */
    public void clear() {
        // TODO: Clear the stack
    }

    /**
     * Action with compensating logic
     */
    static abstract class CompensatingAction {
        String name;

        public CompensatingAction(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        /**
         * Execute forward action
         */
        public abstract void execute() throws Exception;

        /**
         * Execute compensating action
         */
        public abstract void compensate() throws Exception;
    }
}

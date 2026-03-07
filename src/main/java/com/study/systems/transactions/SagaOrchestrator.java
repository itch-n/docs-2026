package com.study.systems.transactions;

import java.util.*;
/**
 * Saga Orchestration: Centralized coordinator manages workflow
 *
 * Key principles:
 * - Orchestrator controls flow
 * - Sequential steps with compensation
 * - Rollback on failure
 * - Each step has compensating action
 */

public class SagaOrchestrator {

    private final List<SagaStep> steps;

    /**
     * Initialize Saga orchestrator
     *
     * TODO: Initialize step list
     */
    public SagaOrchestrator() {
        // TODO: Initialize steps list
        this.steps = null; // Replace
    }

    /**
     * Add step to saga
     *
     * @param step Saga step with transaction and compensation
     */
    public void addStep(SagaStep step) {
        // TODO: Add step to list
    }

    /**
     * Execute saga
     *
     * TODO: Execute all steps sequentially
     * 1. Execute each step's transaction
     * 2. If any step fails:
     *    - Execute compensation for completed steps
     *    - Return failure
     * 3. If all succeed, return success
     */
    public SagaResult execute(SagaContext context) {
        List<SagaStep> completedSteps = new ArrayList<>();

        System.out.println("Starting Saga execution");

        // TODO: Execute each step
        for (SagaStep step : steps) {
            try {
                System.out.println("Executing: " + step.getName());
                // TODO: Execute step transaction
                // step.execute(context)

                // TODO: Add to completed steps

            } catch (Exception e) {
                System.out.println("Step failed: " + step.getName());

                // TODO: Compensate completed steps in reverse order
                System.out.println("Starting compensation");
                // for (int i = completedSteps.size() - 1; i >= 0; i--):
                //   completedSteps.get(i).compensate(context)

                // TODO: Return failure
                return new SagaResult(false, "Failed at: " + step.getName());
            }
        }

        // TODO: All steps succeeded
        System.out.println("Saga completed successfully");
        return new SagaResult(true, "Success");
    }

    /**
     * Saga step with transaction and compensation
     */
    static abstract class SagaStep {
        String name;

        public SagaStep(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        /**
         * Execute forward transaction
         */
        public abstract void execute(SagaContext context) throws Exception;

        /**
         * Execute compensating transaction
         */
        public abstract void compensate(SagaContext context);
    }

    /**
     * Saga execution context (shared state)
     */
    static class SagaContext {
        Map<String, Object> data;

        public SagaContext() {
            this.data = new HashMap<>();
        }

        public void put(String key, Object value) {
            data.put(key, value);
        }

        public Object get(String key) {
            return data.get(key);
        }
    }

    static class SagaResult {
        boolean success;
        String message;

        public SagaResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}

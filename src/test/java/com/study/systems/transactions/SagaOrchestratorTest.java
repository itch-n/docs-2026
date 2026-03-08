package com.study.systems.transactions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SagaOrchestratorTest {

    @Test
    void testExecuteWithAllSuccessfulStepsReturnsSuccess() {
        SagaOrchestrator saga = new SagaOrchestrator();

        saga.addStep(new SagaOrchestrator.SagaStep("Step 1") {
            @Override
            public void execute(SagaOrchestrator.SagaContext ctx) {
                ctx.put("step1", "done");
            }

            @Override
            public void compensate(SagaOrchestrator.SagaContext ctx) {
                ctx.put("step1", "compensated");
            }
        });

        saga.addStep(new SagaOrchestrator.SagaStep("Step 2") {
            @Override
            public void execute(SagaOrchestrator.SagaContext ctx) {
                ctx.put("step2", "done");
            }

            @Override
            public void compensate(SagaOrchestrator.SagaContext ctx) {
                ctx.put("step2", "compensated");
            }
        });

        SagaOrchestrator.SagaContext context = new SagaOrchestrator.SagaContext();
        SagaOrchestrator.SagaResult result = saga.execute(context);

        assertNotNull(result);
        assertTrue(result.success);
        assertEquals("done", context.get("step1"));
        assertEquals("done", context.get("step2"));
    }

    @Test
    void testExecuteWithFailingStepReturnsFailure() {
        SagaOrchestrator saga = new SagaOrchestrator();

        saga.addStep(new SagaOrchestrator.SagaStep("Step 1") {
            @Override
            public void execute(SagaOrchestrator.SagaContext ctx) {
                ctx.put("step1", "done");
            }

            @Override
            public void compensate(SagaOrchestrator.SagaContext ctx) {
                ctx.put("step1", "compensated");
            }
        });

        saga.addStep(new SagaOrchestrator.SagaStep("Step 2 - Fails") {
            @Override
            public void execute(SagaOrchestrator.SagaContext ctx) throws Exception {
                throw new Exception("Step 2 failed");
            }

            @Override
            public void compensate(SagaOrchestrator.SagaContext ctx) {
                ctx.put("step2", "compensated");
            }
        });

        SagaOrchestrator.SagaContext context = new SagaOrchestrator.SagaContext();
        SagaOrchestrator.SagaResult result = saga.execute(context);

        assertNotNull(result);
        assertFalse(result.success);
    }

    @Test
    void testCompensationRunsForCompletedStepsOnFailure() {
        SagaOrchestrator saga = new SagaOrchestrator();

        saga.addStep(new SagaOrchestrator.SagaStep("Step 1") {
            @Override
            public void execute(SagaOrchestrator.SagaContext ctx) {
                ctx.put("step1", "done");
            }

            @Override
            public void compensate(SagaOrchestrator.SagaContext ctx) {
                ctx.put("step1", "compensated");
            }
        });

        saga.addStep(new SagaOrchestrator.SagaStep("Step 2 - Fails") {
            @Override
            public void execute(SagaOrchestrator.SagaContext ctx) throws Exception {
                throw new Exception("Failure");
            }

            @Override
            public void compensate(SagaOrchestrator.SagaContext ctx) {
                ctx.put("step2", "compensated");
            }
        });

        SagaOrchestrator.SagaContext context = new SagaOrchestrator.SagaContext();
        saga.execute(context);

        // Step 1 completed before failure, so its compensation should run
        assertEquals("compensated", context.get("step1"));
    }

    @Test
    void testEmptySagaReturnsSuccess() {
        SagaOrchestrator saga = new SagaOrchestrator();

        SagaOrchestrator.SagaContext context = new SagaOrchestrator.SagaContext();
        SagaOrchestrator.SagaResult result = saga.execute(context);

        assertNotNull(result);
        assertTrue(result.success);
    }
}

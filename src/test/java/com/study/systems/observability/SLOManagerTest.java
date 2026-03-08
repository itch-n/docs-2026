package com.study.systems.observability;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class SLOManagerTest {

    // Convenience builder for a standard availability SLO with a 365-day window
    // so that all recorded measurements are always within the window.
    private SLOManager.SLO availabilitySlo(double target, double alertThreshold) {
        return new SLOManager.SLO(
            "test-availability",
            SLOManager.SLIType.AVAILABILITY,
            target,
            Duration.ofDays(365),
            alertThreshold
        );
    }

    // --- SLIMeasurement.calculate() ---

    @Test
    void testCalculateReturnsOneWithNoMeasurements() {
        SLOManager.SLO slo = availabilitySlo(0.999, 0.5);
        SLOManager.SLIMeasurement measurement = new SLOManager.SLIMeasurement(slo);

        assertEquals(1.0, measurement.calculate(), 1e-9,
            "calculate() with no measurements should return 1.0");
    }

    @Test
    void testCalculateAveragesAvailabilityMeasurements() {
        // 8 successes (1.0) + 2 failures (0.0) → average = 0.8
        SLOManager.SLO slo = availabilitySlo(0.999, 0.5);
        SLOManager.SLIMeasurement measurement = new SLOManager.SLIMeasurement(slo);

        for (int i = 0; i < 8; i++) measurement.record(1.0);
        for (int i = 0; i < 2; i++) measurement.record(0.0);

        assertEquals(0.8, measurement.calculate(), 1e-9,
            "calculate() should return the average of all recorded values");
    }

    @Test
    void testCalculateAllSuccessReturnsOne() {
        SLOManager.SLO slo = availabilitySlo(0.999, 0.5);
        SLOManager.SLIMeasurement measurement = new SLOManager.SLIMeasurement(slo);

        for (int i = 0; i < 100; i++) measurement.record(1.0);

        assertEquals(1.0, measurement.calculate(), 1e-9,
            "calculate() should return 1.0 when all measurements are successes");
    }

    // --- SLIMeasurement.getErrorBudget() ---

    @Test
    void testErrorBudgetIsFullWhenMeetingTarget() {
        // All successes → SLI = 1.0.
        // allowed_failure = 1 - 0.999 = 0.001
        // actual_failure  = 1 - 1.0   = 0.0
        // consumed = 0.0 / 0.001 = 0.0 → remaining = 1.0
        SLOManager.SLO slo = availabilitySlo(0.999, 0.5);
        SLOManager.SLIMeasurement measurement = new SLOManager.SLIMeasurement(slo);

        for (int i = 0; i < 1000; i++) measurement.record(1.0);

        assertEquals(1.0, measurement.getErrorBudget(), 1e-9,
            "Error budget should be 1.0 (fully remaining) when all requests succeed");
    }

    @Test
    void testErrorBudgetConsumedWhenBelowTarget() {
        // target=0.999, record 998 successes + 2 failures → SLI = 998/1000 = 0.998
        // allowed_failure  = 1 - 0.999 = 0.001
        // actual_failure   = 1 - 0.998 = 0.002
        // consumed = 0.002 / 0.001 = 2.0
        // remaining = max(0, 1 - 2.0) = 0.0  (fully consumed / overrun)
        SLOManager.SLO slo = availabilitySlo(0.999, 0.5);
        SLOManager.SLIMeasurement measurement = new SLOManager.SLIMeasurement(slo);

        for (int i = 0; i < 998; i++) measurement.record(1.0);
        for (int i = 0; i < 2; i++)   measurement.record(0.0);

        assertEquals(0.0, measurement.getErrorBudget(), 1e-9,
            "Error budget should be 0.0 when SLI falls below target (budget fully consumed)");
    }

    // --- SLIMeasurement.shouldAlert() ---

    @Test
    void testShouldNotAlertWhenBudgetFine() {
        // All successes → budget is full → should not alert regardless of threshold
        SLOManager.SLO slo = availabilitySlo(0.999, 0.5);
        SLOManager.SLIMeasurement measurement = new SLOManager.SLIMeasurement(slo);

        for (int i = 0; i < 1000; i++) measurement.record(1.0);

        assertFalse(measurement.shouldAlert(),
            "shouldAlert() should return false when error budget is fully intact");
    }

    @Test
    void testShouldAlertWhenBudgetExceeded() {
        // target=0.999, alertThreshold=0.5 (alert when >50% budget consumed).
        // Record 998 successes + 2 failures → budget consumed = 200% → shouldAlert() = true
        SLOManager.SLO slo = availabilitySlo(0.999, 0.5);
        SLOManager.SLIMeasurement measurement = new SLOManager.SLIMeasurement(slo);

        for (int i = 0; i < 998; i++) measurement.record(1.0);
        for (int i = 0; i < 2; i++)   measurement.record(0.0);

        assertTrue(measurement.shouldAlert(),
            "shouldAlert() should return true when error budget consumption exceeds alertThreshold");
    }

    // --- AlertManager ---

    @Test
    void testAlertManagerFiresWhenConditionMet() {
        // Use Duration.ZERO so the rule fires immediately on the first evaluation.
        SLOManager.AlertManager alertManager = new SLOManager.AlertManager();
        SLOManager.AlertRule rule = new SLOManager.AlertRule(
            "HighErrorRate",
            "error_rate > 0.01",
            Duration.ZERO,
            "CRITICAL",
            "Error rate above 1%",
            List.of("slack")
        );
        alertManager.addRule(rule);

        Map<String, Double> metrics = Map.of("error_rate", 0.05);
        List<SLOManager.Alert> alerts = alertManager.evaluate(metrics);

        assertNotNull(alerts, "evaluate() should not return null");
        assertEquals(1, alerts.size(),
            "One alert should fire when error_rate (0.05) > 0.01");
        assertEquals("HighErrorRate", alerts.get(0).rule.name,
            "Fired alert should reference the matching rule");
    }

    @Test
    void testAlertManagerDoesNotFireWhenConditionNotMet() {
        SLOManager.AlertManager alertManager = new SLOManager.AlertManager();
        SLOManager.AlertRule rule = new SLOManager.AlertRule(
            "HighErrorRate",
            "error_rate > 0.01",
            Duration.ZERO,
            "CRITICAL",
            "Error rate above 1%",
            List.of("slack")
        );
        alertManager.addRule(rule);

        Map<String, Double> metrics = Map.of("error_rate", 0.005);
        List<SLOManager.Alert> alerts = alertManager.evaluate(metrics);

        assertNotNull(alerts, "evaluate() should not return null");
        assertTrue(alerts.isEmpty(),
            "No alert should fire when error_rate (0.005) <= 0.01");
    }

    @Test
    void testAlertManagerClearsAfterConditionGone() {
        // Fire the alert once, then evaluate with a metric that no longer meets the condition.
        // The second evaluation should return an empty alert list.
        SLOManager.AlertManager alertManager = new SLOManager.AlertManager();
        SLOManager.AlertRule rule = new SLOManager.AlertRule(
            "HighErrorRate",
            "error_rate > 0.01",
            Duration.ZERO,
            "CRITICAL",
            "Error rate above 1%",
            List.of("slack")
        );
        alertManager.addRule(rule);

        // First call: condition met → alert fires
        alertManager.evaluate(Map.of("error_rate", 0.05));

        // Second call: condition no longer met → firing state should be cleared, no alerts
        List<SLOManager.Alert> alerts = alertManager.evaluate(Map.of("error_rate", 0.005));
        assertTrue(alerts.isEmpty(),
            "No alert should fire after the condition clears");
    }
}

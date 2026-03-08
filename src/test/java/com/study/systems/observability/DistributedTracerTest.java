package com.study.systems.observability;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DistributedTracerTest {

    private DistributedTracer tracer;

    @BeforeEach
    void setUp() {
        tracer = new DistributedTracer("test-service");
    }

    // --- startSpan / basic span properties ---

    @Test
    void testStartSpanReturnsNonNull() {
        DistributedTracer.Span span = tracer.startSpan("op");
        assertNotNull(span, "startSpan should return a non-null Span");
        tracer.finishSpan();
    }

    @Test
    void testStartSpanSetsOperationName() {
        DistributedTracer.Span span = tracer.startSpan("process_order");
        assertEquals("process_order", span.operationName,
            "operationName should match the argument passed to startSpan");
        tracer.finishSpan();
    }

    @Test
    void testFinishSpanRecordsDuration() {
        DistributedTracer.Span span = tracer.startSpan("quick_op");
        tracer.finishSpan();
        // After finish, endTimeMicros is set; getDurationMicros() = end - start >= 0
        assertTrue(span.getDurationMicros() >= 0,
            "getDurationMicros() should be >= 0 after span is finished");
    }

    @Test
    void testStartSpanHasNonNullNonEmptyTraceId() {
        DistributedTracer.Span span = tracer.startSpan("op");
        assertNotNull(span.traceId, "traceId must not be null");
        assertFalse(span.traceId.isEmpty(), "traceId must not be empty");
        tracer.finishSpan();
    }

    @Test
    void testTwoRootSpansHaveDifferentTraceIds() {
        DistributedTracer.Span span1 = tracer.startSpan("op1");
        tracer.finishSpan();

        DistributedTracer.Span span2 = tracer.startSpan("op2");
        tracer.finishSpan();

        assertNotEquals(span1.traceId, span2.traceId,
            "Two separate root spans should have different traceIds");
    }

    // --- child spans ---

    @Test
    void testChildSpanSharesTraceIdWithParent() {
        DistributedTracer.Span parent = tracer.startSpan("parent_op");
        DistributedTracer.Span child = tracer.startChildSpan("child_op");

        assertEquals(parent.traceId, child.traceId,
            "Child span must share the same traceId as its parent");

        tracer.finishSpan(); // finish child
        tracer.finishSpan(); // finish parent
    }

    @Test
    void testChildSpanHasParentSpanId() {
        DistributedTracer.Span parent = tracer.startSpan("parent_op");
        DistributedTracer.Span child = tracer.startChildSpan("child_op");

        assertEquals(parent.spanId, child.parentSpanId,
            "Child span's parentSpanId must equal the parent span's spanId");

        tracer.finishSpan(); // finish child
        tracer.finishSpan(); // finish parent
    }

    // --- tags and logs ---

    @Test
    void testSetTagStoresValue() {
        DistributedTracer.Span span = tracer.startSpan("tagged_op");
        span.setTag("env", "production");

        assertTrue(span.tags.containsKey("env"),
            "tags map should contain the key after setTag");
        assertEquals("production", span.tags.get("env"),
            "tags map should contain the correct value after setTag");

        tracer.finishSpan();
    }

    @Test
    void testLogAddsEntryToLogs() {
        DistributedTracer.Span span = tracer.startSpan("logged_op");
        span.log("request started");

        assertTrue(span.logs.contains("request started"),
            "logs list should contain the message after log()");

        tracer.finishSpan();
    }

    // --- getTrace ---

    @Test
    void testGetTraceReturnsCompletedSpans() {
        // Start root span, add one child, finish both, then check getTrace returns 2 spans
        DistributedTracer.Span root = tracer.startSpan("api_request");
        DistributedTracer.Span child = tracer.startChildSpan("db_query");
        tracer.finishSpan(); // finish child
        tracer.finishSpan(); // finish root

        List<DistributedTracer.Span> trace = tracer.getTrace(root.traceId);
        assertNotNull(trace, "getTrace should return a non-null list");
        assertEquals(2, trace.size(),
            "getTrace should return both the root and child span");
    }

    @Test
    void testGetTraceFiltersToCorrectTrace() {
        // Build two independent traces; verify each only appears in its own getTrace result
        DistributedTracer.Span trace1Root = tracer.startSpan("trace1_root");
        tracer.finishSpan();

        DistributedTracer.Span trace2Root = tracer.startSpan("trace2_root");
        tracer.finishSpan();

        List<DistributedTracer.Span> trace1Spans = tracer.getTrace(trace1Root.traceId);
        List<DistributedTracer.Span> trace2Spans = tracer.getTrace(trace2Root.traceId);

        assertNotNull(trace1Spans);
        assertNotNull(trace2Spans);

        assertEquals(1, trace1Spans.size(),
            "trace1 should contain exactly 1 span");
        assertEquals(1, trace2Spans.size(),
            "trace2 should contain exactly 1 span");

        assertEquals(trace1Root.traceId, trace1Spans.get(0).traceId,
            "trace1 span should have trace1's traceId");
        assertEquals(trace2Root.traceId, trace2Spans.get(0).traceId,
            "trace2 span should have trace2's traceId");
    }

    // --- TraceContext ---

    @Test
    void testCreateRootContextReturnsNonNull() {
        DistributedTracer.TraceContext context = DistributedTracer.TraceContext.createRoot();
        assertNotNull(context, "createRoot() should return a non-null TraceContext");
    }

    @Test
    void testCreateRootContextHasNonNullTraceId() {
        DistributedTracer.TraceContext context = DistributedTracer.TraceContext.createRoot();
        assertNotNull(context.traceId,
            "Root TraceContext must have a non-null traceId");
        assertFalse(context.traceId.isEmpty(),
            "Root TraceContext must have a non-empty traceId");
    }

    @Test
    void testCreateChildPreservesTraceId() {
        DistributedTracer.TraceContext parent = DistributedTracer.TraceContext.createRoot();
        DistributedTracer.TraceContext child = parent.createChild();

        assertNotNull(child, "createChild() should return a non-null TraceContext");
        assertEquals(parent.traceId, child.traceId,
            "Child context must preserve the parent's traceId");
    }
}

package com.study.systems.transactions;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SagaChoreographyTest {

    @Test
    void testRegisterAndPublishEventInvokesHandler() {
        SagaChoreography choreography = new SagaChoreography();
        List<String> handled = new ArrayList<>();

        choreography.registerHandler("OrderCreated", (event, bus) -> {
            handled.add("OrderCreated");
        });

        SagaChoreography.Event event = new SagaChoreography.Event("OrderCreated");
        event.put("orderId", "order123");
        choreography.startSaga(event);

        assertEquals(1, handled.size());
        assertEquals("OrderCreated", handled.get(0));
    }

    @Test
    void testEventChainFiresSequentially() {
        SagaChoreography choreography = new SagaChoreography();
        List<String> steps = new ArrayList<>();

        choreography.registerHandler("OrderCreated", (event, bus) -> {
            steps.add("OrderCreated");
            SagaChoreography.Event next = new SagaChoreography.Event("InventoryReserved");
            next.put("orderId", event.get("orderId"));
            bus.publish(next);
        });

        choreography.registerHandler("InventoryReserved", (event, bus) -> {
            steps.add("InventoryReserved");
            SagaChoreography.Event next = new SagaChoreography.Event("PaymentProcessed");
            next.put("orderId", event.get("orderId"));
            bus.publish(next);
        });

        choreography.registerHandler("PaymentProcessed", (event, bus) -> {
            steps.add("PaymentProcessed");
        });

        SagaChoreography.Event orderCreated = new SagaChoreography.Event("OrderCreated");
        orderCreated.put("orderId", "order123");
        choreography.startSaga(orderCreated);

        assertEquals(3, steps.size());
        assertEquals("OrderCreated", steps.get(0));
        assertEquals("InventoryReserved", steps.get(1));
        assertEquals("PaymentProcessed", steps.get(2));
    }

    @Test
    void testEventWithNoHandlerDoesNotThrow() {
        SagaChoreography choreography = new SagaChoreography();

        SagaChoreography.Event event = new SagaChoreography.Event("UnknownEvent");
        assertDoesNotThrow(() -> choreography.startSaga(event));
    }

    @Test
    void testEventDataIsPassedToHandler() {
        SagaChoreography choreography = new SagaChoreography();
        List<Object> receivedOrderIds = new ArrayList<>();

        choreography.registerHandler("OrderCreated", (event, bus) -> {
            receivedOrderIds.add(event.get("orderId"));
        });

        SagaChoreography.Event event = new SagaChoreography.Event("OrderCreated");
        event.put("orderId", "order456");
        choreography.startSaga(event);

        assertEquals(1, receivedOrderIds.size());
        assertEquals("order456", receivedOrderIds.get(0));
    }
}

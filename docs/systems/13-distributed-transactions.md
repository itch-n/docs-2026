# 09. Distributed Transactions

> Maintaining consistency across multiple services and databases

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing distributed transaction patterns, explain them simply.

**Prompts to guide you:**

1. **What is a distributed transaction in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why are distributed transactions hard?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy for 2PC:**
   - Example: "Two-phase commit is like a wedding ceremony where..."
   - Your analogy: _[Fill in]_

4. **What is the Saga pattern in one sentence?**
   - Your answer: _[Fill in after implementation]_

5. **How is orchestration different from choreography?**
   - Your answer: _[Fill in after implementation]_

6. **Real-world analogy for Saga choreography:**
   - Example: "Saga choreography is like a relay race where..."
   - Your analogy: _[Fill in]_

7. **What is compensation in one sentence?**
   - Your answer: _[Fill in after implementation]_

8. **When would you use event sourcing?**
   - Your answer: _[Fill in after implementation]_

---

## Core Implementation

### Part 1: Two-Phase Commit (2PC)

**Your task:** Implement basic 2PC protocol.

```java
import java.util.*;

/**
 * Two-Phase Commit: Atomic commit across multiple participants
 *
 * Key principles:
 * - Phase 1: Prepare (voting)
 * - Phase 2: Commit/Abort (decision)
 * - Coordinator manages protocol
 * - All or nothing semantics
 */

public class TwoPhaseCommit {

    private final List<Participant> participants;
    private final TransactionLog log;

    /**
     * Initialize 2PC coordinator
     *
     * @param participants List of transaction participants
     *
     * TODO: Initialize coordinator
     * - Store participants
     * - Create transaction log
     */
    public TwoPhaseCommit(List<Participant> participants) {
        // TODO: Store participants list

        // TODO: Create transaction log

        this.participants = null; // Replace
        this.log = null; // Replace
    }

    /**
     * Execute distributed transaction
     *
     * @param transactionId Transaction identifier
     * @param operations Operations to execute
     * @return Transaction result
     *
     * TODO: Implement 2PC protocol
     * Phase 1: Prepare
     *   - Send prepare to all participants
     *   - Collect votes (YES/NO)
     *   - If any NO, abort
     * Phase 2: Commit/Abort
     *   - If all YES, send commit to all
     *   - If any NO, send abort to all
     *   - Wait for acknowledgments
     */
    public TransactionResult executeTransaction(String transactionId,
                                                Map<Participant, String> operations) {
        // TODO: Log transaction start
        log.write("START " + transactionId);

        // PHASE 1: PREPARE
        System.out.println("Phase 1: Prepare");

        // TODO: Send prepare to all participants
        Map<Participant, Vote> votes = new HashMap<>();
        for (Map.Entry<Participant, String> entry : participants) {
            // TODO: Send prepare request
            // Vote vote = participant.prepare(transactionId, operation)
            // Store vote
        }

        // TODO: Check if all voted YES
        boolean allYes = true; // Calculate this

        // PHASE 2: COMMIT or ABORT
        if (allYes) {
            System.out.println("Phase 2: Commit");
            // TODO: Send commit to all participants
            // for participant:
            //   participant.commit(transactionId)
            // TODO: Log commit
            // TODO: Return success

        } else {
            System.out.println("Phase 2: Abort");
            // TODO: Send abort to all participants
            // for participant:
            //   participant.abort(transactionId)
            // TODO: Log abort
            // TODO: Return failure
        }

        return null; // Replace
    }

    /**
     * Participant in distributed transaction
     */
    static class Participant {
        String id;
        Map<String, String> preparedTransactions; // transactionId -> data

        public Participant(String id) {
            this.id = id;
            this.preparedTransactions = new HashMap<>();
        }

        /**
         * Prepare phase: Can you commit?
         *
         * TODO: Prepare transaction
         * - Check if can commit (resources available, no conflicts)
         * - If yes, lock resources and save state
         * - Return YES or NO vote
         */
        public Vote prepare(String transactionId, String operation) {
            System.out.println(id + " preparing: " + operation);

            // TODO: Check if can commit (simulate)
            // Save prepared state
            // Return vote

            // Simulate: random failure 20% of time
            if (Math.random() < 0.2) {
                System.out.println(id + " votes NO");
                return Vote.NO;
            }

            preparedTransactions.put(transactionId, operation);
            System.out.println(id + " votes YES");
            return Vote.YES;
        }

        /**
         * Commit phase: Execute the transaction
         *
         * TODO: Commit transaction
         * - Apply prepared changes
         * - Release locks
         * - Clean up prepared state
         */
        public void commit(String transactionId) {
            System.out.println(id + " committing");
            // TODO: Apply changes
            // TODO: Clean up prepared state
            preparedTransactions.remove(transactionId);
        }

        /**
         * Abort phase: Rollback the transaction
         *
         * TODO: Abort transaction
         * - Discard prepared changes
         * - Release locks
         * - Clean up prepared state
         */
        public void abort(String transactionId) {
            System.out.println(id + " aborting");
            // TODO: Rollback changes
            // TODO: Clean up prepared state
            preparedTransactions.remove(transactionId);
        }
    }

    enum Vote {
        YES, NO
    }

    static class TransactionLog {
        List<String> entries;

        public TransactionLog() {
            this.entries = new ArrayList<>();
        }

        public void write(String entry) {
            entries.add(System.currentTimeMillis() + ": " + entry);
        }
    }

    static class TransactionResult {
        boolean success;
        String message;

        public TransactionResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
```

### Part 2: Saga Pattern - Orchestration

**Your task:** Implement Saga with centralized orchestrator.

```java
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
```

### Part 3: Saga Pattern - Choreography

**Your task:** Implement Saga with event-based choreography.

```java
/**
 * Saga Choreography: Event-driven with no central coordinator
 *
 * Key principles:
 * - Services listen for events
 * - Each service knows next step
 * - Decentralized control
 * - Event-driven compensation
 */

public class SagaChoreography {

    private final Map<String, List<EventHandler>> eventHandlers;
    private final EventBus eventBus;

    /**
     * Initialize choreography
     *
     * TODO: Initialize event system
     * - Create event bus
     * - Create handler registry
     */
    public SagaChoreography() {
        // TODO: Initialize eventHandlers map

        // TODO: Create event bus

        this.eventHandlers = null; // Replace
        this.eventBus = null; // Replace
    }

    /**
     * Register event handler
     *
     * @param eventType Event type to listen for
     * @param handler Handler to execute
     *
     * TODO: Register handler for event type
     */
    public void registerHandler(String eventType, EventHandler handler) {
        // TODO: Get or create handler list for event type

        // TODO: Add handler to list
    }

    /**
     * Publish event
     *
     * TODO: Publish event to all registered handlers
     * - Get handlers for event type
     * - Execute each handler
     * - Handlers may publish new events
     */
    public void publishEvent(Event event) {
        System.out.println("Event published: " + event.type);

        // TODO: Get handlers for event type

        // TODO: Execute each handler
        // for handler in handlers:
        //   handler.handle(event, eventBus)
    }

    /**
     * Start saga by publishing initial event
     */
    public void startSaga(Event initialEvent) {
        // TODO: Publish initial event
        publishEvent(initialEvent);
    }

    /**
     * Event handler interface
     */
    interface EventHandler {
        void handle(Event event, EventBus eventBus);
    }

    /**
     * Event bus for publishing events
     */
    static class EventBus {
        SagaChoreography choreography;

        public EventBus(SagaChoreography choreography) {
            this.choreography = choreography;
        }

        public void publish(Event event) {
            choreography.publishEvent(event);
        }
    }

    /**
     * Event in the saga
     */
    static class Event {
        String type;
        Map<String, Object> data;

        public Event(String type) {
            this.type = type;
            this.data = new HashMap<>();
        }

        public void put(String key, Object value) {
            data.put(key, value);
        }

        public Object get(String key) {
            return data.get(key);
        }
    }
}
```

### Part 4: Compensation Pattern

**Your task:** Implement compensating transactions.

```java
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

        // TODO: While stack not empty:
        //   Pop action
        //   Execute compensation
        //   Handle errors (log but continue)

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
```

### Part 5: Event Sourcing

**Your task:** Implement event sourcing for transaction history.

```java
/**
 * Event Sourcing: Store events instead of current state
 *
 * Key principles:
 * - All changes stored as events
 * - Current state derived from events
 * - Complete audit trail
 * - Time travel (replay to any point)
 */

public class EventSourcedAggregate {

    private final String aggregateId;
    private final List<DomainEvent> events;
    private int version;

    /**
     * Initialize event sourced aggregate
     *
     * @param aggregateId Unique identifier
     *
     * TODO: Initialize aggregate
     * - Store aggregate ID
     * - Create events list
     * - Set version to 0
     */
    public EventSourcedAggregate(String aggregateId) {
        // TODO: Store aggregateId

        // TODO: Initialize events list

        // TODO: Set version to 0

        this.aggregateId = null; // Replace
        this.events = null; // Replace
        this.version = 0;
    }

    /**
     * Apply and record event
     *
     * @param event Event to apply
     *
     * TODO: Apply event
     * - Add event to list
     * - Increment version
     * - Apply state change
     */
    public void applyEvent(DomainEvent event) {
        // TODO: Set event version

        // TODO: Add to events list

        // TODO: Increment version

        // TODO: Apply state change (handled by subclass)

        System.out.println("Event applied: " + event);
    }

    /**
     * Replay events to reconstruct state
     *
     * @param events Historical events
     *
     * TODO: Replay all events
     * - Clear current state
     * - Apply each event in order
     * - Reconstruct current state
     */
    public void replayEvents(List<DomainEvent> events) {
        System.out.println("Replaying " + events.size() + " events");

        // TODO: For each event:
        //   Apply event
        //   Update version
    }

    /**
     * Get events after specific version
     *
     * TODO: Filter events by version
     */
    public List<DomainEvent> getEventsSince(int version) {
        // TODO: Filter events where event.version > version
        return null; // Replace
    }

    /**
     * Get all events
     */
    public List<DomainEvent> getAllEvents() {
        return new ArrayList<>(events);
    }

    /**
     * Get current version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Domain event
     */
    static class DomainEvent {
        String aggregateId;
        String eventType;
        int version;
        long timestamp;
        Map<String, Object> data;

        public DomainEvent(String aggregateId, String eventType) {
            this.aggregateId = aggregateId;
            this.eventType = eventType;
            this.timestamp = System.currentTimeMillis();
            this.data = new HashMap<>();
        }

        public void put(String key, Object value) {
            data.put(key, value);
        }

        public Object get(String key) {
            return data.get(key);
        }

        @Override
        public String toString() {
            return "Event{type='" + eventType + "', version=" + version + "}";
        }
    }
}
```

---

## Client Code

```java
import java.util.*;

public class DistributedTransactionsClient {

    public static void main(String[] args) {
        testTwoPhaseCommit();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testSagaOrchestration();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testSagaChoreography();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testCompensation();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testEventSourcing();
    }

    static void testTwoPhaseCommit() {
        System.out.println("=== Two-Phase Commit Test ===\n");

        // Create participants
        List<TwoPhaseCommit.Participant> participants = Arrays.asList(
            new TwoPhaseCommit.Participant("Database-A"),
            new TwoPhaseCommit.Participant("Database-B"),
            new TwoPhaseCommit.Participant("Database-C")
        );

        TwoPhaseCommit coordinator = new TwoPhaseCommit(participants);

        // Execute transaction
        Map<TwoPhaseCommit.Participant, String> operations = new HashMap<>();
        for (TwoPhaseCommit.Participant p : participants) {
            operations.put(p, "UPDATE balance SET amount = amount - 100");
        }

        TwoPhaseCommit.TransactionResult result =
            coordinator.executeTransaction("tx123", operations);

        System.out.println("\nResult: " + result.message);
    }

    static void testSagaOrchestration() {
        System.out.println("=== Saga Orchestration Test ===\n");

        SagaOrchestrator saga = new SagaOrchestrator();

        // Define saga steps
        saga.addStep(new SagaOrchestrator.SagaStep("Reserve Inventory") {
            @Override
            public void execute(SagaOrchestrator.SagaContext context) throws Exception {
                System.out.println("  -> Reserving inventory");
                context.put("inventoryReserved", true);
                // Simulate occasional failure
                if (Math.random() < 0.3) {
                    throw new Exception("Out of stock");
                }
            }

            @Override
            public void compensate(SagaOrchestrator.SagaContext context) {
                System.out.println("  -> Releasing inventory");
                context.put("inventoryReserved", false);
            }
        });

        saga.addStep(new SagaOrchestrator.SagaStep("Process Payment") {
            @Override
            public void execute(SagaOrchestrator.SagaContext context) throws Exception {
                System.out.println("  -> Processing payment");
                context.put("paymentProcessed", true);
            }

            @Override
            public void compensate(SagaOrchestrator.SagaContext context) {
                System.out.println("  -> Refunding payment");
                context.put("paymentProcessed", false);
            }
        });

        saga.addStep(new SagaOrchestrator.SagaStep("Ship Order") {
            @Override
            public void execute(SagaOrchestrator.SagaContext context) throws Exception {
                System.out.println("  -> Shipping order");
                context.put("orderShipped", true);
            }

            @Override
            public void compensate(SagaOrchestrator.SagaContext context) {
                System.out.println("  -> Canceling shipment");
                context.put("orderShipped", false);
            }
        });

        // Execute saga
        SagaOrchestrator.SagaContext context = new SagaOrchestrator.SagaContext();
        SagaOrchestrator.SagaResult result = saga.execute(context);

        System.out.println("\nResult: " + result.message);
    }

    static void testSagaChoreography() {
        System.out.println("=== Saga Choreography Test ===\n");

        SagaChoreography choreography = new SagaChoreography();

        // Register event handlers
        choreography.registerHandler("OrderCreated", (event, bus) -> {
            System.out.println("  -> Handling OrderCreated");
            System.out.println("  -> Reserving inventory");

            // Publish next event
            SagaChoreography.Event inventoryReserved =
                new SagaChoreography.Event("InventoryReserved");
            inventoryReserved.put("orderId", event.get("orderId"));
            bus.publish(inventoryReserved);
        });

        choreography.registerHandler("InventoryReserved", (event, bus) -> {
            System.out.println("  -> Handling InventoryReserved");
            System.out.println("  -> Processing payment");

            // Publish next event
            SagaChoreography.Event paymentProcessed =
                new SagaChoreography.Event("PaymentProcessed");
            paymentProcessed.put("orderId", event.get("orderId"));
            bus.publish(paymentProcessed);
        });

        choreography.registerHandler("PaymentProcessed", (event, bus) -> {
            System.out.println("  -> Handling PaymentProcessed");
            System.out.println("  -> Shipping order");

            SagaChoreography.Event orderShipped =
                new SagaChoreography.Event("OrderShipped");
            orderShipped.put("orderId", event.get("orderId"));
            System.out.println("  -> Saga complete!");
        });

        // Start saga
        SagaChoreography.Event orderCreated =
            new SagaChoreography.Event("OrderCreated");
        orderCreated.put("orderId", "order123");
        choreography.startSaga(orderCreated);
    }

    static void testCompensation() {
        System.out.println("=== Compensation Test ===\n");

        CompensationHandler handler = new CompensationHandler();

        // Define compensating actions
        boolean success = true;

        success = handler.executeWithCompensation(
            new CompensationHandler.CompensatingAction("Deduct Balance") {
                @Override
                public void execute() throws Exception {
                    System.out.println("  -> Balance deducted");
                }

                @Override
                public void compensate() throws Exception {
                    System.out.println("  -> Balance restored");
                }
            }
        );

        if (!success) return;

        success = handler.executeWithCompensation(
            new CompensationHandler.CompensatingAction("Send Email") {
                @Override
                public void execute() throws Exception {
                    System.out.println("  -> Email sent");
                    // Simulate failure
                    if (Math.random() < 0.5) {
                        throw new Exception("Email service down");
                    }
                }

                @Override
                public void compensate() throws Exception {
                    System.out.println("  -> Cancellation email sent");
                }
            }
        );

        if (!success) {
            System.out.println("\nOperation failed, compensating...");
            handler.compensateAll();
        } else {
            System.out.println("\nAll operations successful");
            handler.clear();
        }
    }

    static void testEventSourcing() {
        System.out.println("=== Event Sourcing Test ===\n");

        EventSourcedAggregate account = new EventSourcedAggregate("account123");

        // Apply events
        System.out.println("Applying events:");

        EventSourcedAggregate.DomainEvent created =
            new EventSourcedAggregate.DomainEvent("account123", "AccountCreated");
        created.put("initialBalance", 1000);
        account.applyEvent(created);

        EventSourcedAggregate.DomainEvent deposited =
            new EventSourcedAggregate.DomainEvent("account123", "MoneyDeposited");
        deposited.put("amount", 500);
        account.applyEvent(deposited);

        EventSourcedAggregate.DomainEvent withdrawn =
            new EventSourcedAggregate.DomainEvent("account123", "MoneyWithdrawn");
        withdrawn.put("amount", 200);
        account.applyEvent(withdrawn);

        System.out.println("\nCurrent version: " + account.getVersion());
        System.out.println("Total events: " + account.getAllEvents().size());

        // Replay events
        System.out.println("\nReplaying events:");
        EventSourcedAggregate newAccount = new EventSourcedAggregate("account123");
        newAccount.replayEvents(account.getAllEvents());

        System.out.println("Reconstructed version: " + newAccount.getVersion());
    }
}
```

---

## Decision Framework

**Questions to answer after implementation:**

### 1. Pattern Selection

**When to use Two-Phase Commit?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Saga (Orchestration)?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Saga (Choreography)?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Event Sourcing?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

### 2. Trade-offs

**Two-Phase Commit:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Saga (Orchestration):**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Saga (Choreography):**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Event Sourcing:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

### 3. Your Decision Tree

Build your decision tree after practicing:

```
What consistency do you need?
├─ Strong consistency (ACID) → ?
├─ Eventual consistency acceptable → ?
├─ Long-running transactions → ?
├─ Need audit trail → ?
└─ Highly distributed services → ?
```

### 4. Kill Switch - Don't use when:

**Two-Phase Commit:**
1. _[When does 2PC fail? Fill in]_
2. _[Another failure case]_

**Saga Pattern:**
1. _[When does Saga fail? Fill in]_
2. _[Another failure case]_

**Event Sourcing:**
1. _[When does event sourcing fail? Fill in]_
2. _[Another failure case]_

### 5. Rule of Three - Alternatives

For each scenario, identify alternatives and compare:

**Scenario: E-commerce order processing**
1. Option A: _[Fill in]_
2. Option B: _[Fill in]_
3. Option C: _[Fill in]_

---

## Practice

### Scenario 1: Banking transfer

**Requirements:**
- Transfer money between accounts
- Accounts in different databases
- Must be atomic (all or nothing)
- Low latency required
- Rare failures acceptable

**Your design:**
- Which pattern would you choose? _[Fill in]_
- Why? _[Fill in]_
- How to handle failures? _[Fill in]_
- Consistency guarantees? _[Fill in]_

### Scenario 2: E-commerce order

**Requirements:**
- Order involves: inventory, payment, shipping
- Each service is independent
- Long-running process (minutes)
- Need to handle partial failures
- Must be eventually consistent

**Your design:**
- Which pattern would you choose? _[Fill in]_
- Why? _[Fill in]_
- Compensation strategy? _[Fill in]_
- How to monitor progress? _[Fill in]_

### Scenario 3: Account audit system

**Requirements:**
- Need complete history of all changes
- Regulatory compliance
- Ability to replay transactions
- Time-based queries
- High write volume

**Your design:**
- Which pattern would you choose? _[Fill in]_
- Why? _[Fill in]_
- Storage strategy? _[Fill in]_
- Query optimization? _[Fill in]_

---

## Review Checklist

- [ ] Two-phase commit implemented with prepare and commit phases
- [ ] Saga orchestration implemented with central coordinator
- [ ] Saga choreography implemented with event-driven flow
- [ ] Compensation handler implemented for rollback
- [ ] Event sourcing implemented with event replay
- [ ] Understand when to use each pattern
- [ ] Can explain trade-offs between patterns
- [ ] Built decision tree for pattern selection
- [ ] Completed practice scenarios

---

**Next:** [14. Consensus Patterns →](14-consensus-patterns.md)

**Back:** [12. Observability ←](12-observability.md)

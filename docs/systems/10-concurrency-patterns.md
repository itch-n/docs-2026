# Concurrency Patterns

> Locks, thread pools, synchronization, and async patterns - From threads to coroutines to reactive streams

---

## Learning Objectives

By the end of this topic you will be able to:

- Implement ReentrantLock, ReadWriteLock, and lock-ordering in the bank-transfer pattern to prevent deadlock
- Implement the producer-consumer pattern using a bounded BlockingQueue and explain what happens when the queue is full or empty
- Compare synchronized, lock-free (CAS/Atomic), and ReadWriteLock approaches for counter and cache workloads
- Identify concurrency bugs including race conditions, deadlocks, visibility issues, and thread pool starvation
- Choose the correct concurrency tool (locks, lock-free, thread pools, blocking queues) for a given workload

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing concurrency patterns, explain them simply.

**Prompts to guide you:**

1. **What is a lock in one sentence?**
    - Your answer: <span class="fill-in">A lock is a ___ that works by ___</span>

2. **Why do we need locks in concurrent programs?**
    - Your answer: <span class="fill-in">Without locks, two threads can ___ the same variable simultaneously, causing ___ because the operation is not ___</span>

3. **Real-world analogy for ReentrantLock:**
    - Example: "A ReentrantLock is like a bathroom key that you can use multiple times..."
    - Your analogy: <span class="fill-in">Think about how a single hotel room key prevents two guests from entering simultaneously — and how the same guest can re-enter their own room without returning the key first...</span>

4. **What is a thread pool in one sentence?**
    - Your answer: <span class="fill-in">A thread pool is a ___ that avoids the cost of ___ by ___</span>

5. **Why use a thread pool instead of creating threads directly?**
    - Your answer: <span class="fill-in">Creating a new thread for every task is expensive because ___; a pool is preferred when ___ because it ___</span>

6. **Real-world analogy for BlockingQueue:**
    - Example: "A BlockingQueue is like a conveyor belt in a factory..."
    - Your analogy: <span class="fill-in">Think about a ticket window where customers wait when it's full and the clerk waits when it's empty — the queue naturally keeps producer and consumer in sync...</span>

</div>

---

## Case Studies: Concurrency in the Wild

### Relational Databases: Pessimistic Locking for Consistency

- **Pattern:** Pessimistic Locking (`SELECT ... FOR UPDATE`).
- **How it works:** In a traditional banking application, when a user initiates a money transfer, the database
  transaction can place an exclusive lock on the user's account balance row. The transaction reads the balance, updates
  it, and then releases the lock upon commit. If another transaction tries to access the same account balance row in the
  meantime, it is blocked and forced to wait.
- **Key Takeaway:** For high-contention, mission-critical operations where data integrity is paramount (e.g., financial
  transactions, inventory management), pessimistic locking is a safe and robust strategy. It prevents conflicts from
  happening in the first place, at the cost of reduced concurrency.

### Web Frameworks (Ruby on Rails, Django): Optimistic Locking for High Concurrency

- **Pattern:** Optimistic Concurrency Control (OCC) with a version column.
- **How it works:** Imagine two admins editing the same product in an e-commerce backend. Admin A loads the product
  page (version 42). Admin B loads the same page. Admin A saves her changes; the application updates the product and
  increments its version to 43. When Admin B tries to save his changes, the application sees that he is trying to update
  version 42, but the current version is 43. The update is rejected, and Admin B is shown an error: "This record was
  modified by someone else. Please reload and try again."
- **Key Takeaway:** Optimistic locking is ideal for web applications where conflicts are rare and high concurrency is
  desirable. It "hopes" for the best and only deals with conflicts when they actually occur, avoiding the overhead of
  database locks for the majority of non-conflicting operations.

### PostgreSQL & Oracle: MVCC for Read/Write Isolation

- **Pattern:** Multi-Version Concurrency Control (MVCC).
- **How it works:** When you run a long analytical query (`SELECT AVG(price) FROM products`) in PostgreSQL, you get a
  consistent "snapshot" of the database at the time your query began. If another user updates a product price while your
  query is running, PostgreSQL creates a *new version* of that product row instead of overwriting it. Your long-running
  query continues to see the old version, while new transactions will see the updated version.
- **Key Takeaway:** MVCC is a powerful mechanism that allows readers and writers not to block each other. This is a
  massive performance benefit for mixed workloads, where long-running reports or analytics can execute alongside fast
  OLTP transactions without contention.

---

## Core Implementation

### Pattern 1: Lock-Based Synchronization

**Concept:** Using explicit locks to control access to shared resources and prevent race conditions.

**Use case:** Thread-safe counters, banking transactions, resource pooling.

```java
--8<-- "com/study/systems/concurrency/LockBasedSync.java"
```


!!! warning "Debugging Challenge — Lock Not Released on Exception"

    The counter below correctly acquires the lock but has a critical bug that causes threads to hang permanently after any exception.

    ```java
    public void increment_Buggy() {
        lock.lock();
        count++;
        if (count < 0) {
            throw new IllegalStateException("Counter went negative");
        }
        lock.unlock();
    }
    ```

    Trace what happens if `count++` overflows and becomes negative. Which thread hangs, and why does the whole system stop making progress?

    ??? success "Answer"

        **Bug:** `lock.unlock()` is only called on the happy path. If the `IllegalStateException` is thrown, `unlock()` is skipped entirely. The lock is held forever by the thread that threw the exception, and every other thread trying to call `increment_Buggy()` blocks indefinitely.

        **Fix:** Always unlock in a `finally` block:

        ```java
        public void increment_Fixed() {
            lock.lock();
            try {
                count++;
                if (count < 0) {
                    throw new IllegalStateException("Counter went negative");
                }
            } finally {
                lock.unlock();  // Always runs, even if exception thrown
            }
        }
        ```

        This is the single most important rule for ReentrantLock: the `unlock()` call must always be inside a `finally` block.

---

### Pattern 2: Producer-Consumer with BlockingQueue

**Concept:** Decoupling producers and consumers using a thread-safe bounded queue.

**Use case:** Task queues, message processing, batch processing pipelines.

```java
--8<-- "com/study/systems/concurrency/ProducerConsumer.java"
```


---

### Pattern 3: Thread-Safe Data Structures

**Concept:** Using concurrent collections and lock-free algorithms for high-performance concurrent access.

**Use case:** Caches, shared state, counters in high-throughput systems.

```java
--8<-- "com/study/systems/concurrency/ThreadSafeDataStructures.java"
```


---

### Pattern 4: Thread Pools

**Concept:** Reusing threads from a pool instead of creating new threads for each task.

**Use case:** Web servers, background job processing, parallel computations.

```java
--8<-- "com/study/systems/concurrency/ThreadPoolPatterns.java"
```


---

## Before/After: Why These Patterns Matter

**Your task:** Compare unsafe vs synchronized vs lock-free approaches to understand the impact.

### Example: Thread-Safe Counter

**Problem:** Multiple threads incrementing a shared counter.

#### Approach 1: Unsafe (No Synchronization)

```java
// BROKEN - Race condition!
public class UnsafeCounter {
    private int count = 0;

    public void increment() {
        count++;  // NOT atomic! Actually: read, add, write
    }

    public int getCount() {
        return count;
    }
}
```

**Analysis:**

- Time: O(1) per operation
- Space: O(1)
- **BUG:** Race condition - lost updates
- For 10 threads × 100,000 increments = 1,000,000 expected
- Actual result: ~650,000 (varies each run!)
- **Lost updates:** ~350,000 increments lost

**Why it fails:**

```
Thread 1: read count=5
Thread 2: read count=5
Thread 1: add 1, write count=6
Thread 2: add 1, write count=6  ← Should be 7!
```

#### Approach 2: Synchronized (Lock-Based)

```java
// CORRECT - Using synchronized
public class SynchronizedCounter {
    private int count = 0;

    public synchronized void increment() {
        count++;  // Protected by lock
    }

    public synchronized int getCount() {
        return count;
    }
}
```

**Analysis:**

- Time: O(1) per operation (with lock overhead ~50-100ns)
- Space: O(1)
- **CORRECT:** Mutual exclusion guarantees atomicity
- For 10 threads × 100,000 increments = 1,000,000 expected
- Actual result: 1,000,000 ✓

**Lock overhead:**

- Low contention: ~50ns per lock
- High contention: ~500-1000ns (threads wait in queue)

#### Approach 3: Lock-Free (Atomic with CAS)

```java
// CORRECT - Using Compare-And-Swap
import java.util.concurrent.atomic.AtomicInteger;

public class LockFreeCounter {
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.getAndIncrement();  // Uses CAS internally
    }

    public int getCount() {
        return count.get();
    }
}
```

**Analysis:**

- Time: O(1) expected per operation (CAS ~5-10ns when successful)
- Space: O(1)
- **CORRECT:** CAS ensures atomicity without locks
- For 10 threads × 100,000 increments = 1,000,000 expected
- Actual result: 1,000,000 ✓

**CAS behavior:**

```
do {
    current = read count
    next = current + 1
} while (!compareAndSet(current, next))  // Retry if changed
```

#### Performance Comparison

| Threads | Unsafe (BROKEN) | Synchronized  | Lock-Free (Atomic) | Winner         |
|---------|-----------------|---------------|--------------------|----------------|
| 1       | 100% (5ms)      | 100% (5ms)    | 100% (5ms)         | Tie            |
| 2       | 75% lost        | 100% (12ms)   | 100% (8ms)         | Lock-free 1.5x |
| 10      | 35% lost        | 100% (150ms)  | 100% (50ms)        | Lock-free 3x   |
| 100     | 5% lost         | 100% (2000ms) | 100% (500ms)       | Lock-free 4x   |

**Your calculation:** For 50 threads, synchronized takes 800ms. Lock-free should take approximately _____ ms.

#### Why Does Lock-Free Win Under Contention?

**Synchronized (Lock):**

- Thread 1 acquires lock → others BLOCK and wait in queue
- Context switches, scheduler overhead
- Serialized execution under high contention

**Lock-Free (CAS):**

- All threads attempt CAS simultaneously
- Failed CAS retries immediately (no blocking)
- Better CPU utilization, no context switches
- Scales better with more threads

**When locks win:**

- Complex operations (need to hold lock for multiple steps)
- Low contention (lock overhead minimal)
- Need fairness guarantees (lock provides FIFO)

**After implementing, explain in your own words:**

<div class="learner-section" markdown>

- Why does unsynchronized code lose updates? <span class="fill-in">[Your answer]</span>
- When would you choose synchronized over lock-free? <span class="fill-in">[Your answer]</span>
- What is the "ABA problem" in lock-free algorithms? <span class="fill-in">[Your answer]</span>

</div>

---

## Debugging Challenges

**Your task:** Find and fix concurrency bugs in broken implementations. This tests your understanding of thread safety.

### Challenge 1: Lost Updates (Race Condition)

```java
/**
 * This bank account has a race condition.
 * Two threads transferring money simultaneously can cause lost updates.
 */
public class BrokenBankAccount {
    private int balance = 1000;

    public boolean withdraw(int amount) {
        if (balance >= amount) {
            // What if another thread withdraws here?
            balance -= amount;
            return true;
        }
        return false;
    }

    public int getBalance() {
        return balance;
    }
}
```

**Your debugging:**

- **Bug location:** <span class="fill-in">[Which lines?]</span>
- **Bug explanation:** <span class="fill-in">[What can go wrong?]</span>
- **Scenario:** Thread A and B both call withdraw(600) when balance=1000
    - Expected: One succeeds, one fails, final balance = 400
    - Actual: <span class="fill-in">[What happens?]</span>
    - Final balance could be: <span class="fill-in">[Fill in]</span>
- **Bug fix:** <span class="fill-in">[How to make it thread-safe?]</span>

??? success "Answer"

    **Bug:** Check-then-act race condition. The check `balance >= amount` and the action `balance -= amount` are not atomic.

    **Scenario trace:**

    ```
    Thread A: check balance (1000) >= 600 ✓
    Thread B: check balance (1000) >= 600 ✓
    Thread A: balance = 1000 - 600 = 400
    Thread B: balance = 400 - 600 = -200  ← OVERDRAFT!
    ```

    **Fix Option 1 - Synchronized:**

    ```java
    public synchronized boolean withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
    ```

    **Fix Option 2 - ReentrantLock:**

    ```java
    private final ReentrantLock lock = new ReentrantLock();

    public boolean withdraw(int amount) {
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
    ```


---

### Challenge 2: Deadlock (Lock Ordering)

```java
/**
 * This code can deadlock!
 * Thread 1: transfer(acc1, acc2, 100)
 * Thread 2: transfer(acc2, acc1, 50)
 */
public class DeadlockTransfer {
    static class Account {
        int balance;
        final ReentrantLock lock = new ReentrantLock();
    }

    public static boolean transfer(Account from, Account to, int amount) {
        from.lock.lock();  // Thread 1 locks acc1
        try {
            to.lock.lock();  // Thread 1 tries to lock acc2
                            // BUT Thread 2 already locked acc2!
            try {
                if (from.balance >= amount) {
                    from.balance -= amount;
                    to.balance += amount;
                    return true;
                }
                return false;
            } finally {
                to.lock.unlock();
            }
        } finally {
            from.lock.unlock();
        }
    }
}
```

**Your debugging:**

- **Bug explanation:** <span class="fill-in">[Why does deadlock occur?]</span>
- **Deadlock scenario:**
    - Thread 1: Holds lock on ___, waiting for lock on ___
    - Thread 2: Holds lock on ___, waiting for lock on ___
    - Result: <span class="fill-in">[Both threads stuck forever]</span>
- **Bug fix:** <span class="fill-in">[What's the solution?]</span>

??? success "Answer"

    **Bug:** Circular lock dependency causes deadlock.

    **Deadlock scenario:**

    ```
    Thread 1: transfer(acc1, acc2, 100)
        - Locks acc1 ✓
        - Tries to lock acc2... WAITS

    Thread 2: transfer(acc2, acc1, 50)
        - Locks acc2 ✓
        - Tries to lock acc1... WAITS

    DEADLOCK! Both threads waiting for each other.
    ```

    **Fix - Consistent lock ordering:**

    ```java
    public static boolean transfer(Account from, Account to, int amount) {
        // Always lock in consistent order (e.g., by account ID)
        Account first = from.id < to.id ? from : to;
        Account second = from.id < to.id ? to : from;

        first.lock.lock();
        try {
            second.lock.lock();
            try {
                if (from.balance >= amount) {
                    from.balance -= amount;
                    to.balance += amount;
                    return true;
                }
                return false;
            } finally {
                second.lock.unlock();
            }
        } finally {
            first.lock.unlock();
        }
    }
    ```

    **Why it works:** All threads acquire locks in the same order (by ID), preventing circular wait.

---

### Challenge 3: Visibility Problem (Missing volatile)

```java
/**
 * This shutdown mechanism might not work!
 * Worker thread may never see the updated flag.
 */
public class BrokenShutdown {
    private boolean stopRequested = false;
    // Background worker thread
    public void backgroundWork() {
        long count = 0;
        while (!stopRequested) {  // May never see true!
            count++;
            // CPU might cache stopRequested = false forever
        }
        System.out.println("Stopped after " + count + " iterations");
    }

    // Main thread
    public void requestStop() {
        stopRequested = true;  // Write might not be visible!
    }
}
```

**Your debugging:**

- **Bug location:** <span class="fill-in">[Which line?]</span>
- **Bug explanation:** <span class="fill-in">[What's the visibility problem?]</span>
- **Why does it fail?** <span class="fill-in">[CPU caching? Compiler optimization?]</span>
- **Symptoms:**
    - Expected: Worker stops immediately when requestStop() is called
    - Actual: <span class="fill-in">[What happens?]</span>
- **Bug fix:** <span class="fill-in">[How to ensure visibility?]</span>

??? success "Answer"

    **Bug:** Missing `volatile` keyword causes visibility problem.

    **Why it fails:**

    - Each CPU core has its own cache
    - Worker thread caches `stopRequested = false`
    - Main thread writes `stopRequested = true` to its cache
    - Worker thread never sees the update (reads from its stale cache)
    - Loop runs forever!

    **Fix Option 1 - volatile:**

    ```java
    private volatile boolean stopRequested = false;
    ```

    - `volatile` ensures writes are visible to all threads
    - Prevents CPU caching
    - Adds memory barrier (flush to main memory)

    **Fix Option 2 - synchronized:**

    ```java
    private boolean stopRequested = false;

    public synchronized void requestStop() {
        stopRequested = true;
    }

    public synchronized boolean isStopRequested() {
        return stopRequested;
    }

    public void backgroundWork() {
        long count = 0;
        while (!isStopRequested()) {
            count++;
        }
    }
    ```

    **Fix Option 3 - AtomicBoolean:**

    ```java
    private final AtomicBoolean stopRequested = new AtomicBoolean(false);

    public void backgroundWork() {
        long count = 0;
        while (!stopRequested.get()) {
            count++;
        }
    }

    public void requestStop() {
        stopRequested.set(true);
    }
    ```


---

### Challenge 4: Thread Pool Starvation

```java
/**
 * This code can cause thread pool starvation!
 * All threads block waiting for tasks that can't execute.
 */
public class StarvationExample {
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    public void processWithSubtasks() throws Exception {
        executor.submit(() -> {
            System.out.println("Task 1: Starting");

            // This task submits subtasks and waits for them
            Future<String> subtask1 = executor.submit(() -> "Subtask 1");
            Future<String> subtask2 = executor.submit(() -> "Subtask 2");

            try {
                subtask1.get();  // Waits forever if pool is full
                subtask2.get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Task 1: Done");
        });

        executor.submit(() -> {
            System.out.println("Task 2: Starting");

            Future<String> subtask3 = executor.submit(() -> "Subtask 3");
            Future<String> subtask4 = executor.submit(() -> "Subtask 4");

            try {
                subtask3.get();
                subtask4.get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Task 2: Done");
        });
    }
}
```

**Your debugging:**

- **Bug explanation:** <span class="fill-in">[Why does it deadlock?]</span>
- **Thread pool state:**
    - Pool size: 2 threads
    - Thread 1 executing: <span class="fill-in">___</span>
    - Thread 2 executing: <span class="fill-in">___</span>
    - Queued subtasks: <span class="fill-in">___</span>
    - Why can't subtasks run? <span class="fill-in">[Fill in]</span>
- **Bug fix:** <span class="fill-in">[How to prevent starvation?]</span>

??? success "Answer"

    **Bug:** Thread pool starvation - workers block waiting for work that can't execute.

    **Deadlock scenario:**

    ```
    Pool size: 2 threads
    Thread 1: Executing Task 1, blocked on subtask1.get()
    Thread 2: Executing Task 2, blocked on subtask3.get()
    Queue: [subtask1, subtask2, subtask3, subtask4]

    All workers blocked, no thread available to run queued subtasks!
    DEADLOCK!
    ```

    **Fix Option 1 - Separate thread pools:**

    ```java
    private final ExecutorService mainExecutor = Executors.newFixedThreadPool(2);
    private final ExecutorService subtaskExecutor = Executors.newFixedThreadPool(4);

    // Submit main tasks to mainExecutor
    // Submit subtasks to subtaskExecutor
    ```

    **Fix Option 2 - Larger pool:**

    ```java
    // Pool must be large enough for all parallel tasks + their subtasks
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    ```

    **Fix Option 3 - Don't block on subtasks in worker thread:**

    ```java
    // Use callbacks/CompletableFuture instead of blocking get()
    CompletableFuture.supplyAsync(() -> "Subtask 1", executor)
        .thenCombine(
            CompletableFuture.supplyAsync(() -> "Subtask 2", executor),
            (s1, s2) -> s1 + s2
        )
        .thenAccept(result -> System.out.println(result));
    ```


---

### Challenge 5: ABA Problem (Lock-Free Bug)

```java
/**
 * This lock-free stack has the ABA problem!
 * CAS can succeed incorrectly when value cycles back.
 */
public class ABAStack<T> {
    private static class Node<T> {
        final T value;
        Node<T> next;
        Node(T value) { this.value = value; }
    }

    private final AtomicReference<Node<T>> head = new AtomicReference<>();

    public void push(T value) {
        Node<T> newNode = new Node<>(value);
        while (true) {
            Node<T> current = head.get();
            newNode.next = current;
            if (head.compareAndSet(current, newNode)) {
                return;
            }
        }
    }

    public T pop() {
        while (true) {
            Node<T> current = head.get();  // Read A
            if (current == null) return null;

            Node<T> next = current.next;

            // Between here and CAS, another thread could:
            // 1. Pop A (head = B)
            // 2. Pop B (head = C)
            // 3. Push A back (head = A again!)
            // Now CAS succeeds but we're pointing to recycled node!

            if (head.compareAndSet(current, next)) {  // CAS sees A == A ✓
                return current.value;
            }
        }
    }
}
```

**Your debugging:**

- **Bug explanation:** <span class="fill-in">[What is the ABA problem?]</span>
- **ABA scenario:**
    1. Thread 1 reads head = A
    2. Thread 2 pops A, pops B, pushes A back
    3. Thread 1's CAS succeeds (A == A) but...
    4. Problem: <span class="fill-in">[What's wrong?]</span>
- **When does this cause issues?** <span class="fill-in">[What if nodes are reused?]</span>
- **Bug fix:** <span class="fill-in">[How to prevent ABA?]</span>

??? success "Answer"

    **Bug:** ABA problem - CAS succeeds when it shouldn't because value cycled back.

    **ABA scenario:**

    ```
    Initial: head -> A -> B -> C

    Thread 1: Read head = A, next = B
      (Gets preempted before CAS)

    Thread 2:
        - Pop A (head = B)
        - Pop B (head = C)
        - Push A back (head = A, but A.next = C now!)

    Thread 1 resumes:
        - CAS(head, A, B) succeeds! (head was A)
        - But now head points to B, which was already popped!
        - B might be reused/freed → CORRUPTION!
    ```

    **Fix Option 1 - AtomicStampedReference:**

    ```java
    // Add version number to prevent ABA
    private final AtomicStampedReference<Node<T>> head =
        new AtomicStampedReference<>(null, 0);

    public void push(T value) {
        Node<T> newNode = new Node<>(value);
        int[] stampHolder = new int[1];
        while (true) {
            Node<T> current = head.get(stampHolder);
            int stamp = stampHolder[0];
            newNode.next = current;
            if (head.compareAndSet(current, newNode, stamp, stamp + 1)) {
                return;
            }
        }
    }
    ```

    **Fix Option 2 - Hazard pointers (prevent reuse while in use)**

    **Fix Option 3 - Use Java's ConcurrentLinkedStack (already handles this)**

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found race condition causing lost updates
- [ ] Found deadlock due to lock ordering
- [ ] Found visibility problem (missing volatile)
- [ ] Found thread pool starvation
- [ ] Understood ABA problem in lock-free algorithms
- [ ] Could explain each bug's root cause
- [ ] Learned how to prevent each type of bug

**Common concurrency bugs you discovered:**

1. <span class="fill-in">[Race conditions: read-modify-write, check-then-act]</span>
2. <span class="fill-in">[Deadlocks: circular lock dependencies]</span>
3. <span class="fill-in">[Visibility: CPU caching, missing volatile/synchronized]</span>
4. <span class="fill-in">[Starvation: blocking on tasks in same pool]</span>
5. <span class="fill-in">[ABA problem: CAS with recycled values]</span>

**How to prevent these bugs:**

1. <span class="fill-in">[Use locks for compound operations]</span>
2. <span class="fill-in">[Always acquire locks in consistent order]</span>
3. <span class="fill-in">[Use volatile for flags, Atomic for counters]</span>
4. <span class="fill-in">[Separate thread pools for dependent tasks]</span>
5. <span class="fill-in">[Use stamped references or hazard pointers]</span>

---

## Common Misconceptions

!!! warning "synchronized and volatile are interchangeable"
    `synchronized` establishes mutual exclusion (only one thread at a time) AND memory visibility (changes become visible). `volatile` provides only memory visibility — two threads can still read-modify-write a `volatile` variable concurrently, producing lost updates. Use `volatile` for simple flags and `synchronized` or `AtomicXxx` for any compound operations.

!!! warning "More threads always means faster execution"
    For CPU-bound work, creating more threads than available cores causes context-switching overhead that can make performance worse, not better. The optimal pool size for CPU-bound tasks is typically `nCores + 1`. For I/O-bound tasks, more threads help — until thread-creation overhead and contention dominate. Measure before assuming more threads help.

!!! warning "ReentrantLock is always faster than synchronized"
    On modern JVMs, the JIT compiler applies biased locking, lock elision, and lock coarsening to `synchronized` blocks, making them competitive with or faster than `ReentrantLock` in low-contention cases. `ReentrantLock` wins when you need features `synchronized` lacks: try-lock, timed lock, interruptible lock, or fair ordering. Default to `synchronized` for simplicity; switch to `ReentrantLock` when you need those features or when profiling shows benefit.

!!! warning "When it breaks"
    `synchronized` breaks under high contention: when many threads compete for the same lock, threads queue and CPU time is spent on context switching rather than work — throughput declines as you add threads, the opposite of expected scaling. Lock-free algorithms (compare-and-swap) break under the ABA problem: a value changes from A to B and back to A between your read and CAS, making the CAS succeed when it should fail. Thread pools break when tasks block on I/O — all threads are stuck waiting and new tasks queue indefinitely. The fix is either async I/O, separate thread pools for I/O and CPU work, or virtual threads, which block without consuming a carrier thread.

---

## Decision Framework

<div class="learner-section" markdown>

**Your task:** Build decision trees for when to use each concurrency pattern.

### Question 1: When to use locks vs lock-free algorithms?

Answer after implementation:

**Use Locks when:**

- Complex operations: <span class="fill-in">[Multiple steps that must be atomic]</span>
- Simple reasoning: <span class="fill-in">[Lock-based code is easier to understand]</span>
- Fairness needed: <span class="fill-in">[Locks can guarantee FIFO ordering]</span>
- Most use cases: <span class="fill-in">[Locks are sufficient for 90% of scenarios]</span>

**Use Lock-Free when:**

- High contention: <span class="fill-in">[Many threads competing for same resource]</span>
- Low-latency critical: <span class="fill-in">[Cannot afford lock overhead]</span>
- Progress guarantees: <span class="fill-in">[At least one thread makes progress]</span>
- Simple operations: <span class="fill-in">[Increment, swap, stack push/pop]</span>

### Question 2: Which thread pool to use?

**FixedThreadPool when:**

- Known workload: <span class="fill-in">[Consistent number of tasks]</span>
- Resource limiting: <span class="fill-in">[Don't want unbounded thread creation]</span>
- CPU-bound tasks: <span class="fill-in">[Pool size = CPU cores]</span>

**CachedThreadPool when:**

- Unpredictable load: <span class="fill-in">[Varying number of tasks]</span>
- I/O-bound tasks: <span class="fill-in">[Threads spend time waiting]</span>
- Short-lived tasks: <span class="fill-in">[Quick execution, many tasks]</span>

**ScheduledThreadPool when:**

- Delayed execution: <span class="fill-in">[Run after delay]</span>
- Periodic tasks: <span class="fill-in">[Cron-like scheduling]</span>
- Background jobs: <span class="fill-in">[Cleanup, monitoring, etc.]</span>

**ForkJoinPool when:**

- Recursive tasks: <span class="fill-in">[Divide-and-conquer algorithms]</span>
- Parallel algorithms: <span class="fill-in">[Parallel sort, sum, map-reduce]</span>
- Work stealing: <span class="fill-in">[Balance load across threads]</span>

### Question 3: Synchronized vs ReentrantLock?

**Synchronized when:**

- Simple use case: <span class="fill-in">[Just need mutual exclusion]</span>
- Less code: <span class="fill-in">[synchronized(this) { ... }]</span>
- No advanced features needed: <span class="fill-in">[Try-lock, interruptible, timeouts not needed]</span>

**ReentrantLock when:**

- Try-lock needed: <span class="fill-in">[Attempt lock without blocking]</span>
- Timeouts: <span class="fill-in">[Give up after waiting]</span>
- Interruptible: <span class="fill-in">[Can interrupt waiting thread]</span>
- Fairness: <span class="fill-in">[FIFO lock acquisition]</span>
- Condition variables: <span class="fill-in">[Complex waiting conditions]</span>

### Your Decision Tree

Build this after solving practice scenarios:
```mermaid
flowchart LR
    Start["Concurrency Pattern Selection"]

    Q1{"What's the workload?"}
    Start --> Q1
    N2["Use thread pool"]
    Q1 -->|"Many independent tasks"| N2
    N3["Need synchronization"]
    Q1 -->|"Shared mutable state"| N3
    N4["Use BlockingQueue"]
    Q1 -->|"Message passing"| N4
    Q5{"Need synchronization?"}
    Start --> Q5
    N6["synchronized"]
    Q5 -->|"Simple critical section"| N6
    N7["ReentrantLock"]
    Q5 -->|"Advanced features"| N7
    N8["ReadWriteLock"]
    Q5 -->|"Read-heavy"| N8
    N9["Lock-free<br/>(Atomic)"]
    Q5 -->|"High contention"| N9
    Q10{"Thread pool sizing?"}
    Start --> Q10
    N11["cores + 1"]
    Q10 -->|"CPU-bound"| N11
    N12["cores * 2 or more"]
    Q10 -->|"I/O-bound"| N12
    N13["Test and measure"]
    Q10 -->|"Mixed"| N13
    Q14{"Queue sizing?"}
    Start --> Q14
    N15["Prevent memory exhaustion"]
    Q14 -->|"Bounded"| N15
    N16["Risk OOM, but never blocks producers"]
    Q14 -->|"Unbounded"| N16
    N17["Direct handoff, no buffering"]
    Q14 -->|"SynchronousQueue"| N17
```

</div>

---

## Practice

<div class="learner-section" markdown>

### Scenario 1: Web Server Request Handler

**Requirements:**

- Handle 10,000 concurrent HTTP requests
- Each request: parse, validate, database query, response
- Database pool: 20 connections max
- CPU cores: 8
- Must handle bursts (spike to 50K requests)

**Your design:**

Thread pool configuration:

- Pool type: <span class="fill-in">[Fixed, Cached, or Custom?]</span>
- Core threads: <span class="fill-in">[How many?]</span>
- Max threads: <span class="fill-in">[How many?]</span>
- Queue size: <span class="fill-in">[Bounded or unbounded?]</span>
- Rejection policy: <span class="fill-in">[What happens when queue full?]</span>

Reasoning:

1. <span class="fill-in">[Why this pool type?]</span>
2. <span class="fill-in">[How did you calculate thread counts?]</span>
3. <span class="fill-in">[How does it handle bursts?]</span>
4. <span class="fill-in">[What about database connection pool coordination?]</span>

**Failure modes:**

- What happens if the database connection pool is exhausted and all 20 connections are in use when a new request arrives? <span class="fill-in">[Fill in]</span>
- How does your design behave when the thread pool queue fills up during a traffic spike and the rejection policy triggers? <span class="fill-in">[Fill in]</span>

### Scenario 2: Real-Time Analytics Pipeline

**Requirements:**

- Ingest 1M events/second
- Processing stages: validate → enrich → aggregate → store
- Each stage is CPU-intensive (10ms per event)
- Must maintain order within same user_id
- Latency target: p99 < 100ms

**Your design:**

Pipeline architecture:

- Number of stages: <span class="fill-in">[How many?]</span>
- Queue between stages: <span class="fill-in">[Bounded or unbounded?]</span>
- Threads per stage: <span class="fill-in">[How many?]</span>
- Backpressure handling: <span class="fill-in">[What to do when backed up?]</span>

Ordering guarantee:

- How to maintain order per user_id: <span class="fill-in">[Your approach]</span>
- Trade-off: <span class="fill-in">[Ordering vs throughput]</span>

**Failure modes:**

- What happens if one processing stage (e.g., enrich) becomes slow and its bounded queue fills up? <span class="fill-in">[Fill in]</span>
- How does your design behave when a single user_id generates a burst of events that overwhelms the thread assigned to that partition? <span class="fill-in">[Fill in]</span>

### Scenario 3: Distributed Cache

**Requirements:**

- In-memory cache with 1M entries
- Operations: get (90%), put (9%), delete (1%)
- Concurrent access: 1000 threads
- Must track hit rate, eviction stats
- LRU eviction policy

**Your design:**

Data structure:

- Base structure: <span class="fill-in">[ConcurrentHashMap? Why?]</span>
- Synchronization: <span class="fill-in">[Where do you need locks?]</span>
- Statistics tracking: <span class="fill-in">[Atomic variables?]</span>

LRU implementation:

- How to track access order: <span class="fill-in">[Your approach]</span>
- Thread-safe eviction: <span class="fill-in">[How to handle concurrent evictions?]</span>
- Read-write coordination: <span class="fill-in">[ReadWriteLock? Why or why not?]</span>

Trade-offs:

1. <span class="fill-in">[Accuracy vs performance]</span>
2. <span class="fill-in">[Lock-free vs locked]</span>
3. <span class="fill-in">[Memory overhead]</span>

**Failure modes:**

- What happens if a thread holding the write lock during an eviction is interrupted or throws an exception before releasing the lock? <span class="fill-in">[Fill in]</span>
- How does your design behave under a cache stampede where many threads simultaneously find the same key missing and attempt to populate it? <span class="fill-in">[Fill in]</span>

</div>

---

## Test Your Understanding

Answer these without referring to your notes or implementation.

1. A bank account has `balance = 1000`. Thread A and Thread B both call `withdraw(600)` at the same time. Neither method is synchronized. Trace the exact sequence of operations that produces a final balance of `-200`, and explain why this is a check-then-act race condition.

    ??? success "Rubric"
        A complete answer addresses: (1) the interleaving: Thread A reads balance=1000, Thread B reads balance=1000, Thread A checks 1000>=600 (true) and sets balance=400, Thread B checks 1000>=600 (true, stale read) and sets balance=400-600=-200, (2) the race is specifically check-then-act: the guard condition and the mutation are two separate non-atomic operations, so another thread can change state between them, (3) the fix is to make the check and act atomic under a lock or `synchronized` block.

2. You have a fixed thread pool of size 2. Thread 1 is running a task that submits a subtask to the same pool and blocks on `future.get()`. Thread 2 is doing the same. Explain why this causes deadlock, and give two ways to fix it.

    ??? success "Rubric"
        A complete answer addresses: (1) both worker threads are blocked waiting for subtasks that are queued but cannot run because there are no free threads — a classic thread pool deadlock, (2) fix 1: use a separate executor for subtasks so that the submitting threads are not blocking threads needed to execute the submitted work, (3) fix 2: use `CompletableFuture`/async chaining so the worker thread is not held while waiting for subtask completion, freeing it to execute other work.

3. You change a `boolean stopFlag` field to `volatile boolean stopFlag`. A colleague says this is now fully thread-safe. Under what specific circumstances is `volatile` sufficient, and when is it not enough even with `volatile`?

    ??? success "Rubric"
        A complete answer addresses: (1) `volatile` is sufficient for a simple single-writer flag where one thread writes and others only read — it guarantees visibility with no mutual exclusion needed, (2) `volatile` is not sufficient for read-modify-write operations such as `counter++` because that is three steps (read, increment, write) and two threads can interleave them, producing lost updates despite volatile, (3) for compound operations, use `synchronized`, `AtomicXxx`, or `ReentrantLock` to guarantee both visibility and atomicity.

4. You need to implement a read-heavy cache: 95% reads, 5% writes, 100 threads. Compare using `synchronized`, `ConcurrentHashMap`, and `ReadWriteLock`. Which do you choose and why, considering both correctness and performance?

    ??? success "Rubric"
        A complete answer addresses: (1) `synchronized` on the whole map serialises all reads and writes — correct but reduces concurrency to 1 thread at a time regardless of operation type, (2) `ConcurrentHashMap` uses lock striping so reads are mostly non-blocking and writes lock only the relevant segment — good for simple key/value operations but does not support compound conditional updates atomically without extra coordination, (3) `ReadWriteLock` allows all 95% reads to proceed concurrently (multiple readers simultaneously) while writes block all readers — the best fit for this workload's access pattern, at the cost of slightly more complex code.

5. A colleague says "virtual threads are just like coroutines — they avoid blocking by being non-blocking." Identify what is wrong with this statement. What actually happens when a virtual thread calls a blocking I/O operation?

    ??? success "Rubric"
        A complete answer addresses: (1) virtual threads do not avoid blocking calls — they make the same blocking API calls as platform threads, (2) what actually happens: the JVM detects a blocking operation on a virtual thread, unmounts the virtual thread from its carrier (OS) thread, parks it, and the carrier thread is freed to run other virtual threads — the virtual thread itself is blocked but the OS thread is not, (3) the difference from coroutines is that virtual threads require no async/await syntax changes; existing blocking code works unchanged, but the scaling benefit comes from the JVM scheduler, not from making code non-blocking.

---

## Connected Topics

!!! info "Where this topic connects"

    - **12. Message Queues** — the producer-consumer pattern implemented here with threads and blocking queues is the same pattern message queues implement across distributed services → [12. Message Queues](12-message-queues.md)
    - **17. Distributed Transactions** — distributed locking extends per-process lock patterns to span multiple machines; the same race conditions occur but are harder to detect and diagnose → [17. Distributed Transactions](17-distributed-transactions.md)
    - **18. Consensus Patterns** — consensus state machines require the same thread-safe design as the monitor pattern here, applied across multiple nodes → [18. Consensus Patterns](18-consensus-patterns.md)

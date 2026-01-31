# 14. Consensus Patterns

> Leader election, Raft consensus, distributed locks, and quorum-based systems

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing consensus patterns, explain them simply.

**Prompts to guide you:**

1. **What is consensus in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why do distributed systems need consensus?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy for leader election:**
   - Example: "Leader election is like choosing a class president where..."
   - Your analogy: _[Fill in]_

4. **What is the split-brain problem in one sentence?**
   - Your answer: _[Fill in after implementation]_

5. **Real-world analogy for distributed locks:**
   - Example: "A distributed lock is like a bathroom key that..."
   - Your analogy: _[Fill in]_

6. **Why do we need quorums?**
   - Your answer: _[Fill in after practice]_

---

## Core Implementation

### Pattern 1: Leader Election

**Concept:** Distributed algorithm to elect a single leader node from a cluster of nodes, ensuring only one leader exists at any time.

**Use case:** Distributed databases, coordination services, master-worker systems.

```java
import java.util.*;
import java.util.concurrent.*;

/**
 * Leader Election: Bully Algorithm
 *
 * Properties:
 * - Highest ID becomes leader
 * - Nodes send election messages to higher IDs
 * - If no response, node becomes leader
 * - Heartbeats detect leader failure
 */
public class LeaderElection {

    enum NodeState {
        FOLLOWER, CANDIDATE, LEADER
    }

    static class Node {
        int id;
        NodeState state;
        int leaderId;
        long lastHeartbeat;
        Set<Integer> higherNodes;

        Node(int id, Set<Integer> allNodes) {
            this.id = id;
            this.state = NodeState.FOLLOWER;
            this.leaderId = -1;
            this.lastHeartbeat = System.currentTimeMillis();
            this.higherNodes = new HashSet<>();

            // Track nodes with higher IDs
            for (int nodeId : allNodes) {
                if (nodeId > id) {
                    higherNodes.add(nodeId);
                }
            }
        }
    }

    private final Map<Integer, Node> nodes;
    private final Map<Integer, Boolean> nodeActive; // Simulate node failures
    private final long heartbeatTimeout;
    private final ScheduledExecutorService scheduler;

    public LeaderElection(Set<Integer> nodeIds, long heartbeatTimeout) {
        this.nodes = new ConcurrentHashMap<>();
        this.nodeActive = new ConcurrentHashMap<>();
        this.heartbeatTimeout = heartbeatTimeout;
        this.scheduler = Executors.newScheduledThreadPool(2);

        // Initialize nodes
        for (int id : nodeIds) {
            nodes.put(id, new Node(id, nodeIds));
            nodeActive.put(id, true);
        }
    }

    /**
     * Start leader election from a node
     * Time: O(N) where N = number of higher-priority nodes
     *
     * TODO: Implement Bully algorithm election
     * 1. Send election messages to all higher ID nodes
     * 2. Wait for responses (timeout)
     * 3. If no response, declare self as leader
     * 4. If response received, wait for victory message
     */
    public void startElection(int nodeId) {
        // TODO: Check if node is active
        //   if (!nodeActive.get(nodeId)) return;

        // TODO: Set node to CANDIDATE state
        //   Node node = nodes.get(nodeId);
        //   node.state = NodeState.CANDIDATE;

        // TODO: Send election messages to higher nodes
        //   Set<Integer> respondingNodes = new HashSet<>();
        //   for (int higherId : node.higherNodes) {
        //     if (nodeActive.get(higherId)) {
        //       respondingNodes.add(higherId);
        //       // In real system: send network message
        //       // Recursively trigger election in higher nodes
        //       startElection(higherId);
        //     }
        //   }

        // TODO: If no higher nodes active, become leader
        //   if (respondingNodes.isEmpty()) {
        //     becomeLeader(nodeId);
        //   }
    }

    /**
     * Node becomes leader
     * Time: O(N)
     *
     * TODO: Implement leader declaration
     * 1. Set state to LEADER
     * 2. Broadcast victory message to all nodes
     * 3. Start sending heartbeats
     */
    private void becomeLeader(int nodeId) {
        // TODO: Update node state
        //   Node node = nodes.get(nodeId);
        //   node.state = NodeState.LEADER;
        //   node.leaderId = nodeId;

        // TODO: Broadcast victory to all nodes
        //   for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
        //     if (entry.getKey() != nodeId && nodeActive.get(entry.getKey())) {
        //       Node follower = entry.getValue();
        //       follower.state = NodeState.FOLLOWER;
        //       follower.leaderId = nodeId;
        //       follower.lastHeartbeat = System.currentTimeMillis();
        //     }
        //   }

        // TODO: Start heartbeat thread
        //   startHeartbeat(nodeId);

        System.out.println("Node " + nodeId + " became LEADER");
    }

    /**
     * Send heartbeats from leader
     * Time: O(N) per heartbeat
     *
     * TODO: Implement heartbeat mechanism
     * 1. Periodically send heartbeat to all followers
     * 2. Update lastHeartbeat timestamp
     */
    private void startHeartbeat(int leaderId) {
        // TODO: Schedule periodic heartbeat
        //   scheduler.scheduleAtFixedRate(() -> {
        //     if (!nodeActive.get(leaderId)) {
        //       return; // Leader died, stop heartbeats
        //     }
        //
        //     Node leader = nodes.get(leaderId);
        //     if (leader.state != NodeState.LEADER) {
        //       return; // No longer leader
        //     }
        //
        //     // Send heartbeat to all followers
        //     long now = System.currentTimeMillis();
        //     for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
        //       if (entry.getKey() != leaderId && nodeActive.get(entry.getKey())) {
        //         entry.getValue().lastHeartbeat = now;
        //       }
        //     }
        //   }, 0, heartbeatTimeout / 2, TimeUnit.MILLISECONDS);
    }

    /**
     * Check for leader failure and trigger election
     * Time: O(1) for check, O(N) for election
     *
     * TODO: Implement failure detection
     * 1. Check if heartbeat timed out
     * 2. If leader dead, start new election
     */
    public void checkLeaderHealth(int nodeId) {
        // TODO: Check heartbeat timeout
        //   Node node = nodes.get(nodeId);
        //   if (node.state == NodeState.LEADER) {
        //     return; // Leader doesn't check itself
        //   }
        //
        //   long now = System.currentTimeMillis();
        //   if (now - node.lastHeartbeat > heartbeatTimeout) {
        //     System.out.println("Node " + nodeId + " detected leader failure");
        //     startElection(nodeId);
        //   }
    }

    /**
     * Simulate node failure
     */
    public void simulateFailure(int nodeId) {
        nodeActive.put(nodeId, false);
        System.out.println("Node " + nodeId + " FAILED");

        // If leader failed, trigger elections
        Node node = nodes.get(nodeId);
        if (node.state == NodeState.LEADER) {
            // Followers will detect via heartbeat timeout
            for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
                if (entry.getKey() != nodeId && nodeActive.get(entry.getKey())) {
                    checkLeaderHealth(entry.getKey());
                }
            }
        }
    }

    /**
     * Get current leader ID
     */
    public int getLeader() {
        for (Node node : nodes.values()) {
            if (node.state == NodeState.LEADER && nodeActive.get(node.id)) {
                return node.id;
            }
        }
        return -1; // No leader
    }

    /**
     * Get node state
     */
    public NodeState getNodeState(int nodeId) {
        return nodes.get(nodeId).state;
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class LeaderElectionClient {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Leader Election Demo ===\n");

        Set<Integer> nodeIds = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        LeaderElection election = new LeaderElection(nodeIds, 1000);

        // Test 1: Initial election
        System.out.println("--- Test 1: Initial Election ---");
        election.startElection(1); // Lowest node starts
        Thread.sleep(100);
        System.out.println("Current leader: " + election.getLeader());
        System.out.println("Node 1 state: " + election.getNodeState(1));
        System.out.println("Node 5 state: " + election.getNodeState(5));

        // Test 2: Leader failure
        System.out.println("\n--- Test 2: Leader Failure ---");
        int currentLeader = election.getLeader();
        System.out.println("Simulating failure of leader: " + currentLeader);
        election.simulateFailure(currentLeader);
        Thread.sleep(100);
        System.out.println("New leader: " + election.getLeader());

        // Test 3: Multiple failures
        System.out.println("\n--- Test 3: Multiple Failures ---");
        election.simulateFailure(4);
        election.simulateFailure(3);
        Thread.sleep(100);
        System.out.println("Leader after multiple failures: " + election.getLeader());

        // Test 4: Check all node states
        System.out.println("\n--- Test 4: Node States ---");
        for (int id : nodeIds) {
            System.out.println("Node " + id + ": " + election.getNodeState(id));
        }

        election.shutdown();
    }
}
```

---

### Pattern 2: Raft Consensus Algorithm

**Concept:** Consensus algorithm that ensures replicated log consistency across distributed nodes through leader election and log replication.

**Use case:** Distributed databases (etcd, Consul), replicated state machines, configuration management.

```java
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Raft Consensus: Leader-based log replication
 *
 * Key concepts:
 * - Leader election with terms
 * - Log replication via AppendEntries
 * - Commit only when majority replicated
 * - Safety: committed entries never lost
 */
public class RaftConsensus {

    enum NodeRole {
        FOLLOWER, CANDIDATE, LEADER
    }

    static class LogEntry {
        int term;
        String command;
        int index;

        LogEntry(int term, String command, int index) {
            this.term = term;
            this.command = command;
            this.index = index;
        }

        @Override
        public String toString() {
            return String.format("[idx=%d, term=%d, cmd=%s]", index, term, command);
        }
    }

    static class RaftNode {
        int id;
        NodeRole role;
        int currentTerm;
        int votedFor; // candidateId that received vote in current term
        List<LogEntry> log;
        int commitIndex; // highest log entry known to be committed
        int lastApplied; // highest log entry applied to state machine

        // Leader-specific state
        Map<Integer, Integer> nextIndex; // for each server, index of next log entry to send
        Map<Integer, Integer> matchIndex; // for each server, index of highest log entry known to be replicated

        long lastHeartbeat;

        RaftNode(int id) {
            this.id = id;
            this.role = NodeRole.FOLLOWER;
            this.currentTerm = 0;
            this.votedFor = -1;
            this.log = new ArrayList<>();
            this.commitIndex = 0;
            this.lastApplied = 0;
            this.nextIndex = new HashMap<>();
            this.matchIndex = new HashMap<>();
            this.lastHeartbeat = System.currentTimeMillis();
        }

        int getLastLogIndex() {
            return log.isEmpty() ? 0 : log.get(log.size() - 1).index;
        }

        int getLastLogTerm() {
            return log.isEmpty() ? 0 : log.get(log.size() - 1).term;
        }
    }

    private final Map<Integer, RaftNode> nodes;
    private final Map<Integer, Boolean> nodeActive;
    private final int majoritySize;

    public RaftConsensus(Set<Integer> nodeIds) {
        this.nodes = new ConcurrentHashMap<>();
        this.nodeActive = new ConcurrentHashMap<>();
        this.majoritySize = (nodeIds.size() / 2) + 1;

        for (int id : nodeIds) {
            nodes.put(id, new RaftNode(id));
            nodeActive.put(id, true);
        }
    }

    /**
     * Start leader election (RequestVote RPC)
     * Time: O(N) where N = number of nodes
     *
     * TODO: Implement Raft leader election
     * 1. Increment current term
     * 2. Vote for self
     * 3. Send RequestVote RPCs to all nodes
     * 4. If majority votes received, become leader
     */
    public void startElection(int candidateId) {
        // TODO: Check if node is active
        //   if (!nodeActive.get(candidateId)) return;

        // TODO: Transition to CANDIDATE
        //   RaftNode candidate = nodes.get(candidateId);
        //   candidate.role = NodeRole.CANDIDATE;
        //   candidate.currentTerm++;
        //   candidate.votedFor = candidateId;

        // TODO: Request votes from all other nodes
        //   int votesReceived = 1; // Vote for self
        //
        //   for (Map.Entry<Integer, RaftNode> entry : nodes.entrySet()) {
        //     int nodeId = entry.getKey();
        //     if (nodeId != candidateId && nodeActive.get(nodeId)) {
        //       boolean voteGranted = requestVote(nodeId, candidateId,
        //           candidate.currentTerm,
        //           candidate.getLastLogIndex(),
        //           candidate.getLastLogTerm());
        //       if (voteGranted) votesReceived++;
        //     }
        //   }

        // TODO: Check if majority reached
        //   if (votesReceived >= majoritySize) {
        //     becomeLeader(candidateId);
        //   }

        System.out.println("Node " + candidateId + " started election for term " +
            nodes.get(candidateId).currentTerm);
    }

    /**
     * RequestVote RPC handler
     * Time: O(1)
     *
     * TODO: Implement vote granting logic
     * 1. Check if candidate's term >= current term
     * 2. Check if haven't voted in this term
     * 3. Check if candidate's log is at least as up-to-date
     * 4. Grant vote if all conditions met
     */
    private boolean requestVote(int voterId, int candidateId, int candidateTerm,
                                 int lastLogIndex, int lastLogTerm) {
        // TODO: Get voter node
        //   RaftNode voter = nodes.get(voterId);

        // TODO: Check term
        //   if (candidateTerm < voter.currentTerm) {
        //     return false; // Reject if candidate term is outdated
        //   }

        // TODO: Update term if candidate has higher term
        //   if (candidateTerm > voter.currentTerm) {
        //     voter.currentTerm = candidateTerm;
        //     voter.votedFor = -1;
        //     voter.role = NodeRole.FOLLOWER;
        //   }

        // TODO: Check if can vote
        //   if (voter.votedFor == -1 || voter.votedFor == candidateId) {
        //     // Check if candidate's log is at least as up-to-date
        //     boolean logUpToDate = lastLogTerm > voter.getLastLogTerm() ||
        //         (lastLogTerm == voter.getLastLogTerm() && lastLogIndex >= voter.getLastLogIndex());
        //
        //     if (logUpToDate) {
        //       voter.votedFor = candidateId;
        //       voter.lastHeartbeat = System.currentTimeMillis();
        //       return true;
        //     }
        //   }

        return false; // Replace
    }

    /**
     * Node becomes leader
     *
     * TODO: Implement leader initialization
     * 1. Set role to LEADER
     * 2. Initialize nextIndex and matchIndex for followers
     * 3. Start sending AppendEntries (heartbeats)
     */
    private void becomeLeader(int nodeId) {
        // TODO: Update role
        //   RaftNode leader = nodes.get(nodeId);
        //   leader.role = NodeRole.LEADER;

        // TODO: Initialize leader state
        //   int lastLogIndex = leader.getLastLogIndex();
        //   for (int id : nodes.keySet()) {
        //     if (id != nodeId) {
        //       leader.nextIndex.put(id, lastLogIndex + 1);
        //       leader.matchIndex.put(id, 0);
        //     }
        //   }

        System.out.println("Node " + nodeId + " became LEADER for term " +
            nodes.get(nodeId).currentTerm);
    }

    /**
     * Append entry to log (client request to leader)
     * Time: O(N) for replication
     *
     * TODO: Implement log entry append
     * 1. Add entry to leader's log
     * 2. Replicate to followers via AppendEntries
     * 3. Commit when majority replicated
     */
    public boolean appendEntry(int leaderId, String command) {
        // TODO: Check if node is leader
        //   RaftNode leader = nodes.get(leaderId);
        //   if (leader.role != NodeRole.LEADER) {
        //     return false; // Not leader
        //   }

        // TODO: Create log entry
        //   int newIndex = leader.getLastLogIndex() + 1;
        //   LogEntry entry = new LogEntry(leader.currentTerm, command, newIndex);
        //   leader.log.add(entry);

        // TODO: Replicate to followers
        //   int replicatedCount = 1; // Leader has it
        //   for (Map.Entry<Integer, RaftNode> e : nodes.entrySet()) {
        //     int nodeId = e.getKey();
        //     if (nodeId != leaderId && nodeActive.get(nodeId)) {
        //       boolean success = sendAppendEntries(leaderId, nodeId);
        //       if (success) replicatedCount++;
        //     }
        //   }

        // TODO: Commit if majority replicated
        //   if (replicatedCount >= majoritySize) {
        //     leader.commitIndex = newIndex;
        //     return true;
        //   }

        return false; // Replace
    }

    /**
     * AppendEntries RPC (log replication and heartbeat)
     * Time: O(E) where E = number of entries to send
     *
     * TODO: Implement AppendEntries RPC
     * 1. Send log entries to follower
     * 2. Check if follower's log is consistent
     * 3. Append entries if consistent
     * 4. Update commitIndex
     */
    private boolean sendAppendEntries(int leaderId, int followerId) {
        // TODO: Get leader and follower
        //   RaftNode leader = nodes.get(leaderId);
        //   RaftNode follower = nodes.get(followerId);

        // TODO: Get entries to send
        //   int nextIdx = leader.nextIndex.get(followerId);
        //   List<LogEntry> entries = new ArrayList<>();
        //   for (int i = nextIdx; i <= leader.getLastLogIndex(); i++) {
        //     entries.add(leader.log.get(i - 1)); // Assuming 1-indexed
        //   }

        // TODO: Send AppendEntries
        //   boolean success = appendEntries(followerId, leader.currentTerm,
        //       leaderId, nextIdx - 1,
        //       nextIdx > 1 ? leader.log.get(nextIdx - 2).term : 0,
        //       entries, leader.commitIndex);

        // TODO: Update nextIndex and matchIndex
        //   if (success) {
        //     leader.nextIndex.put(followerId, leader.getLastLogIndex() + 1);
        //     leader.matchIndex.put(followerId, leader.getLastLogIndex());
        //   } else {
        //     leader.nextIndex.put(followerId, Math.max(1, nextIdx - 1));
        //   }

        return false; // Replace
    }

    /**
     * AppendEntries RPC handler
     *
     * TODO: Implement follower's AppendEntries logic
     * 1. Check term
     * 2. Verify log consistency
     * 3. Append new entries
     * 4. Update commitIndex
     */
    private boolean appendEntries(int followerId, int leaderTerm, int leaderId,
                                   int prevLogIndex, int prevLogTerm,
                                   List<LogEntry> entries, int leaderCommit) {
        // TODO: Get follower
        //   RaftNode follower = nodes.get(followerId);

        // TODO: Check term
        //   if (leaderTerm < follower.currentTerm) {
        //     return false; // Reject outdated leader
        //   }

        // TODO: Update term and reset election timer
        //   follower.lastHeartbeat = System.currentTimeMillis();
        //   if (leaderTerm > follower.currentTerm) {
        //     follower.currentTerm = leaderTerm;
        //     follower.votedFor = -1;
        //   }
        //   follower.role = NodeRole.FOLLOWER;

        // TODO: Check log consistency
        //   if (prevLogIndex > 0) {
        //     if (prevLogIndex > follower.getLastLogIndex() ||
        //         follower.log.get(prevLogIndex - 1).term != prevLogTerm) {
        //       return false; // Log inconsistent
        //     }
        //   }

        // TODO: Append entries
        //   int idx = prevLogIndex;
        //   for (LogEntry entry : entries) {
        //     idx++;
        //     if (idx <= follower.log.size()) {
        //       if (follower.log.get(idx - 1).term != entry.term) {
        //         // Conflict: remove this and all following entries
        //         follower.log.subList(idx - 1, follower.log.size()).clear();
        //         follower.log.add(entry);
        //       }
        //     } else {
        //       follower.log.add(entry);
        //     }
        //   }

        // TODO: Update commit index
        //   if (leaderCommit > follower.commitIndex) {
        //     follower.commitIndex = Math.min(leaderCommit, follower.getLastLogIndex());
        //   }

        return true; // Replace
    }

    /**
     * Get current leader
     */
    public int getLeader() {
        for (RaftNode node : nodes.values()) {
            if (node.role == NodeRole.LEADER && nodeActive.get(node.id)) {
                return node.id;
            }
        }
        return -1;
    }

    /**
     * Get committed log entries
     */
    public List<LogEntry> getCommittedEntries(int nodeId) {
        RaftNode node = nodes.get(nodeId);
        if (node == null) return Collections.emptyList();

        List<LogEntry> committed = new ArrayList<>();
        for (int i = 0; i < node.commitIndex && i < node.log.size(); i++) {
            committed.add(node.log.get(i));
        }
        return committed;
    }

    public void simulateFailure(int nodeId) {
        nodeActive.put(nodeId, false);
        System.out.println("Node " + nodeId + " FAILED");
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class RaftClient {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Raft Consensus Demo ===\n");

        Set<Integer> nodeIds = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        RaftConsensus raft = new RaftConsensus(nodeIds);

        // Test 1: Leader election
        System.out.println("--- Test 1: Leader Election ---");
        raft.startElection(1);
        Thread.sleep(100);
        int leader = raft.getLeader();
        System.out.println("Elected leader: " + leader);

        // Test 2: Log replication
        System.out.println("\n--- Test 2: Log Replication ---");
        boolean success = raft.appendEntry(leader, "SET x=1");
        System.out.println("Append 'SET x=1': " + (success ? "SUCCESS" : "FAILED"));

        success = raft.appendEntry(leader, "SET y=2");
        System.out.println("Append 'SET y=2': " + (success ? "SUCCESS" : "FAILED"));

        success = raft.appendEntry(leader, "DELETE z");
        System.out.println("Append 'DELETE z': " + (success ? "SUCCESS" : "FAILED"));

        // Test 3: Check committed entries
        System.out.println("\n--- Test 3: Committed Entries ---");
        for (int id : nodeIds) {
            List<RaftConsensus.LogEntry> entries = raft.getCommittedEntries(id);
            System.out.println("Node " + id + " committed: " + entries);
        }

        // Test 4: Leader failure and re-election
        System.out.println("\n--- Test 4: Leader Failure ---");
        System.out.println("Simulating failure of leader: " + leader);
        raft.simulateFailure(leader);

        // New election
        int newCandidate = (leader % 5) + 1;
        raft.startElection(newCandidate);
        Thread.sleep(100);

        int newLeader = raft.getLeader();
        System.out.println("New leader: " + newLeader);

        // Test 5: Append to new leader
        System.out.println("\n--- Test 5: Append to New Leader ---");
        success = raft.appendEntry(newLeader, "SET a=100");
        System.out.println("Append 'SET a=100': " + (success ? "SUCCESS" : "FAILED"));
    }
}
```

---

### Pattern 3: Distributed Locks

**Concept:** Mechanism to ensure mutual exclusion across distributed systems, preventing concurrent access to shared resources.

**Use case:** Job schedulers, resource allocation, preventing duplicate processing.

```java
import java.util.*;
import java.util.concurrent.*;

/**
 * Distributed Lock with Fencing Tokens
 *
 * Features:
 * - Lock acquisition with timeout
 * - Automatic expiration (prevent deadlock)
 * - Fencing tokens (prevent stale lock holders)
 * - Lock renewal (extend lease)
 */
public class DistributedLock {

    static class Lock {
        String resourceId;
        String ownerId;
        long fencingToken; // Monotonically increasing
        long acquiredAt;
        long expiresAt;
        int renewalCount;

        Lock(String resourceId, String ownerId, long fencingToken, long ttlMs) {
            this.resourceId = resourceId;
            this.ownerId = ownerId;
            this.fencingToken = fencingToken;
            this.acquiredAt = System.currentTimeMillis();
            this.expiresAt = acquiredAt + ttlMs;
            this.renewalCount = 0;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiresAt;
        }

        void renew(long ttlMs) {
            this.expiresAt = System.currentTimeMillis() + ttlMs;
            this.renewalCount++;
        }
    }

    private final Map<String, Lock> locks; // resourceId -> Lock
    private final AtomicLong tokenCounter; // Global fencing token counter
    private final long defaultTTL;
    private final ScheduledExecutorService cleanupScheduler;

    public DistributedLock(long defaultTTL) {
        this.locks = new ConcurrentHashMap<>();
        this.tokenCounter = new AtomicLong(0);
        this.defaultTTL = defaultTTL;
        this.cleanupScheduler = Executors.newScheduledThreadPool(1);

        // Cleanup expired locks
        startCleanupTask();
    }

    /**
     * Acquire lock on resource
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement lock acquisition
     * 1. Check if resource is locked
     * 2. If locked and not expired, fail
     * 3. If unlocked or expired, acquire lock
     * 4. Generate fencing token
     */
    public Lock tryAcquire(String resourceId, String ownerId) {
        return tryAcquire(resourceId, ownerId, defaultTTL);
    }

    public Lock tryAcquire(String resourceId, String ownerId, long ttlMs) {
        // TODO: Check existing lock
        //   Lock existingLock = locks.get(resourceId);
        //
        //   if (existingLock != null && !existingLock.isExpired()) {
        //     // Lock is held by another owner
        //     if (!existingLock.ownerId.equals(ownerId)) {
        //       return null; // Failed to acquire
        //     }
        //     // Same owner, allow re-entrant
        //   }

        // TODO: Acquire lock with new fencing token
        //   long fencingToken = tokenCounter.incrementAndGet();
        //   Lock newLock = new Lock(resourceId, ownerId, fencingToken, ttlMs);
        //   locks.put(resourceId, newLock);
        //
        //   return newLock;

        return null; // Replace
    }

    /**
     * Acquire lock with retry (blocking with timeout)
     * Time: O(attempts), Space: O(1)
     *
     * TODO: Implement blocking lock acquisition
     * 1. Try to acquire lock
     * 2. If failed, wait and retry
     * 3. Continue until timeout or success
     */
    public Lock acquire(String resourceId, String ownerId, long timeoutMs) {
        // TODO: Calculate deadline
        //   long deadline = System.currentTimeMillis() + timeoutMs;

        // TODO: Retry loop
        //   while (System.currentTimeMillis() < deadline) {
        //     Lock lock = tryAcquire(resourceId, ownerId);
        //     if (lock != null) {
        //       return lock; // Success
        //     }
        //
        //     // Wait before retry
        //     try {
        //       Thread.sleep(50); // Backoff
        //     } catch (InterruptedException e) {
        //       Thread.currentThread().interrupt();
        //       return null;
        //     }
        //   }

        return null; // Replace - timeout
    }

    /**
     * Release lock
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement lock release
     * 1. Verify owner
     * 2. Verify fencing token (prevent stale release)
     * 3. Remove lock
     */
    public boolean release(String resourceId, String ownerId, long fencingToken) {
        // TODO: Check if lock exists
        //   Lock lock = locks.get(resourceId);
        //   if (lock == null) {
        //     return false; // Lock doesn't exist
        //   }

        // TODO: Verify owner and token
        //   if (!lock.ownerId.equals(ownerId)) {
        //     return false; // Not the owner
        //   }
        //
        //   if (lock.fencingToken != fencingToken) {
        //     return false; // Stale token
        //   }

        // TODO: Release lock
        //   locks.remove(resourceId);
        //   return true;

        return false; // Replace
    }

    /**
     * Renew lock (extend lease)
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement lock renewal
     * 1. Verify owner
     * 2. Check if expired
     * 3. Extend expiration time
     */
    public boolean renew(String resourceId, String ownerId, long fencingToken) {
        return renew(resourceId, ownerId, fencingToken, defaultTTL);
    }

    public boolean renew(String resourceId, String ownerId, long fencingToken, long ttlMs) {
        // TODO: Get lock
        //   Lock lock = locks.get(resourceId);
        //   if (lock == null || lock.isExpired()) {
        //     return false; // Lock doesn't exist or expired
        //   }

        // TODO: Verify owner and token
        //   if (!lock.ownerId.equals(ownerId) || lock.fencingToken != fencingToken) {
        //     return false; // Not authorized
        //   }

        // TODO: Renew lease
        //   lock.renew(ttlMs);
        //   return true;

        return false; // Replace
    }

    /**
     * Check if resource is locked
     * Time: O(1)
     */
    public boolean isLocked(String resourceId) {
        Lock lock = locks.get(resourceId);
        return lock != null && !lock.isExpired();
    }

    /**
     * Get lock info
     */
    public Lock getLockInfo(String resourceId) {
        return locks.get(resourceId);
    }

    /**
     * Cleanup expired locks (background task)
     */
    private void startCleanupTask() {
        cleanupScheduler.scheduleAtFixedRate(() -> {
            // TODO: Remove expired locks
            //   locks.entrySet().removeIf(entry -> entry.getValue().isExpired());
        }, defaultTTL, defaultTTL / 2, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        cleanupScheduler.shutdown();
    }

    /**
     * Get all active locks
     */
    public Map<String, Lock> getAllLocks() {
        Map<String, Lock> activeLocks = new HashMap<>();
        for (Map.Entry<String, Lock> entry : locks.entrySet()) {
            if (!entry.getValue().isExpired()) {
                activeLocks.put(entry.getKey(), entry.getValue());
            }
        }
        return activeLocks;
    }
}
```

**Runnable Client Code:**

```java
public class DistributedLockClient {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Distributed Lock Demo ===\n");

        DistributedLock lockService = new DistributedLock(5000); // 5 sec TTL

        // Test 1: Acquire and release
        System.out.println("--- Test 1: Acquire and Release ---");
        DistributedLock.Lock lock = lockService.tryAcquire("resource1", "client1");
        if (lock != null) {
            System.out.println("Client1 acquired lock: token=" + lock.fencingToken);

            boolean released = lockService.release("resource1", "client1", lock.fencingToken);
            System.out.println("Lock released: " + released);
        }

        // Test 2: Concurrent acquisition
        System.out.println("\n--- Test 2: Concurrent Acquisition ---");
        lock = lockService.tryAcquire("resource2", "client1");
        System.out.println("Client1 acquired: " + (lock != null));

        DistributedLock.Lock lock2 = lockService.tryAcquire("resource2", "client2");
        System.out.println("Client2 acquire (should fail): " + (lock2 != null));

        // Test 3: Lock expiration
        System.out.println("\n--- Test 3: Lock Expiration ---");
        DistributedLock shortLock = new DistributedLock(1000); // 1 sec TTL
        lock = shortLock.tryAcquire("resource3", "client1");
        System.out.println("Client1 acquired: " + (lock != null));
        System.out.println("Is locked: " + shortLock.isLocked("resource3"));

        Thread.sleep(1500);
        System.out.println("After 1.5s, is locked: " + shortLock.isLocked("resource3"));

        lock2 = shortLock.tryAcquire("resource3", "client2");
        System.out.println("Client2 acquired after expiry: " + (lock2 != null));

        // Test 4: Lock renewal
        System.out.println("\n--- Test 4: Lock Renewal ---");
        lock = lockService.tryAcquire("resource4", "client1", 2000);
        System.out.println("Client1 acquired with 2s TTL");

        Thread.sleep(1000);
        boolean renewed = lockService.renew("resource4", "client1", lock.fencingToken);
        System.out.println("Renewed after 1s: " + renewed);

        Thread.sleep(1500);
        System.out.println("After 2.5s total, is locked: " + lockService.isLocked("resource4"));

        // Test 5: Fencing tokens
        System.out.println("\n--- Test 5: Fencing Tokens ---");
        lock = lockService.tryAcquire("resource5", "client1");
        System.out.println("Client1 lock token: " + lock.fencingToken);

        lockService.release("resource5", "client1", lock.fencingToken);

        lock2 = lockService.tryAcquire("resource5", "client2");
        System.out.println("Client2 lock token: " + lock2.fencingToken);
        System.out.println("Token increased: " + (lock2.fencingToken > lock.fencingToken));

        // Test 6: Blocking acquire with timeout
        System.out.println("\n--- Test 6: Blocking Acquire ---");
        lock = lockService.tryAcquire("resource6", "client1");
        System.out.println("Client1 acquired");

        // Try to acquire in another thread
        Thread t = new Thread(() -> {
            System.out.println("Client2 trying to acquire with 3s timeout...");
            DistributedLock.Lock l = lockService.acquire("resource6", "client2", 3000);
            System.out.println("Client2 result: " + (l != null ? "SUCCESS" : "TIMEOUT"));
        });
        t.start();

        Thread.sleep(1500);
        lockService.release("resource6", "client1", lock.fencingToken);
        System.out.println("Client1 released after 1.5s");

        t.join();

        lockService.shutdown();
        shortLock.shutdown();
    }
}
```

---

### Pattern 4: Quorum-Based Consensus

**Concept:** Achieve consistency by requiring a majority (quorum) of nodes to agree on reads and writes.

**Use case:** Distributed databases (Cassandra, DynamoDB), multi-datacenter replication, high availability systems.

```java
import java.util.*;
import java.util.concurrent.*;

/**
 * Quorum-Based Consensus
 *
 * Key concepts:
 * - Read quorum (R) + Write quorum (W) > N for strong consistency
 * - W = majority for consistency
 * - R = 1, W = N for read-optimized
 * - R = N, W = 1 for write-optimized
 */
public class QuorumConsensus {

    static class Version {
        long timestamp;
        int vectorClock; // Simplified version vector

        Version(long timestamp, int vectorClock) {
            this.timestamp = timestamp;
            this.vectorClock = vectorClock;
        }

        @Override
        public String toString() {
            return String.format("v%d@%d", vectorClock, timestamp);
        }
    }

    static class VersionedValue {
        String value;
        Version version;

        VersionedValue(String value, Version version) {
            this.value = value;
            this.version = version;
        }

        @Override
        public String toString() {
            return String.format("%s[%s]", value, version);
        }
    }

    static class Node {
        int id;
        Map<String, VersionedValue> data;
        boolean active;

        Node(int id) {
            this.id = id;
            this.data = new ConcurrentHashMap<>();
            this.active = true;
        }

        void put(String key, VersionedValue value) {
            data.put(key, value);
        }

        VersionedValue get(String key) {
            return data.get(key);
        }
    }

    private final Map<Integer, Node> nodes;
    private final int replicationFactor;
    private final int readQuorum;
    private final int writeQuorum;
    private final ExecutorService executor;
    private final AtomicInteger versionCounter;

    /**
     * @param numNodes Total number of nodes
     * @param replicationFactor How many copies of each key
     * @param readQuorum R - nodes to read from
     * @param writeQuorum W - nodes to write to
     */
    public QuorumConsensus(int numNodes, int replicationFactor,
                           int readQuorum, int writeQuorum) {
        this.nodes = new ConcurrentHashMap<>();
        this.replicationFactor = replicationFactor;
        this.readQuorum = readQuorum;
        this.writeQuorum = writeQuorum;
        this.executor = Executors.newFixedThreadPool(numNodes);
        this.versionCounter = new AtomicInteger(0);

        // Initialize nodes
        for (int i = 1; i <= numNodes; i++) {
            nodes.put(i, new Node(i));
        }

        // Validate quorum settings
        if (readQuorum + writeQuorum <= replicationFactor) {
            System.err.println("Warning: R + W <= N, may have inconsistent reads");
        }
    }

    /**
     * Write value to quorum of nodes
     * Time: O(W) where W = write quorum
     *
     * TODO: Implement quorum write
     * 1. Select W nodes (consistent hashing or random)
     * 2. Create versioned value
     * 3. Write to all selected nodes concurrently
     * 4. Wait for W successful responses
     * 5. Return success if quorum reached
     */
    public boolean write(String key, String value) {
        // TODO: Select replica nodes
        //   List<Node> replicas = selectNodes(key, replicationFactor);

        // TODO: Create versioned value
        //   Version version = new Version(
        //       System.currentTimeMillis(),
        //       versionCounter.incrementAndGet()
        //   );
        //   VersionedValue versionedValue = new VersionedValue(value, version);

        // TODO: Write to nodes concurrently
        //   CountDownLatch latch = new CountDownLatch(writeQuorum);
        //   AtomicInteger successCount = new AtomicInteger(0);
        //
        //   for (Node node : replicas) {
        //     executor.submit(() -> {
        //       try {
        //         if (node.active) {
        //           node.put(key, versionedValue);
        //           successCount.incrementAndGet();
        //         }
        //       } finally {
        //         latch.countDown();
        //       }
        //     });
        //   }

        // TODO: Wait for quorum
        //   try {
        //     latch.await(1, TimeUnit.SECONDS);
        //   } catch (InterruptedException e) {
        //     return false;
        //   }
        //
        //   return successCount.get() >= writeQuorum;

        return false; // Replace
    }

    /**
     * Read value from quorum of nodes
     * Time: O(R) where R = read quorum
     *
     * TODO: Implement quorum read
     * 1. Select R nodes
     * 2. Read from all concurrently
     * 3. Wait for R responses
     * 4. Resolve conflicts (pick latest version)
     * 5. Optionally perform read-repair
     */
    public VersionedValue read(String key) {
        // TODO: Select replica nodes
        //   List<Node> replicas = selectNodes(key, replicationFactor);

        // TODO: Read from nodes concurrently
        //   List<VersionedValue> responses = new ArrayList<>();
        //   CountDownLatch latch = new CountDownLatch(Math.min(readQuorum, replicas.size()));
        //
        //   for (Node node : replicas) {
        //     executor.submit(() -> {
        //       try {
        //         if (node.active) {
        //           VersionedValue value = node.get(key);
        //           if (value != null) {
        //             synchronized (responses) {
        //               responses.add(value);
        //             }
        //           }
        //         }
        //       } finally {
        //         latch.countDown();
        //       }
        //     });
        //   }

        // TODO: Wait for quorum responses
        //   try {
        //     latch.await(1, TimeUnit.SECONDS);
        //   } catch (InterruptedException e) {
        //     return null;
        //   }

        // TODO: Resolve conflicts - pick latest version
        //   if (responses.size() >= readQuorum) {
        //     return resolveConflicts(responses);
        //   }

        return null; // Replace
    }

    /**
     * Resolve conflicts by picking value with latest version
     * Time: O(R)
     *
     * TODO: Implement conflict resolution
     * 1. Compare versions (timestamp and vector clock)
     * 2. Return value with highest version
     * 3. Optionally detect concurrent writes (conflict)
     */
    private VersionedValue resolveConflicts(List<VersionedValue> values) {
        // TODO: Find latest version
        //   VersionedValue latest = null;
        //
        //   for (VersionedValue v : values) {
        //     if (latest == null) {
        //       latest = v;
        //     } else {
        //       // Compare versions
        //       if (v.version.vectorClock > latest.version.vectorClock) {
        //         latest = v;
        //       } else if (v.version.vectorClock == latest.version.vectorClock) {
        //         // Same version, use timestamp
        //         if (v.version.timestamp > latest.version.timestamp) {
        //           latest = v;
        //         }
        //       }
        //     }
        //   }
        //
        //   return latest;

        return null; // Replace
    }

    /**
     * Select nodes for replication (simplified)
     * In production: use consistent hashing
     *
     * TODO: Implement node selection
     * 1. Hash the key
     * 2. Select N consecutive nodes on hash ring
     */
    private List<Node> selectNodes(String key, int count) {
        // TODO: Simple implementation - use key hash
        //   int hash = Math.abs(key.hashCode());
        //   List<Node> selected = new ArrayList<>();
        //
        //   List<Node> activeNodes = new ArrayList<>();
        //   for (Node node : nodes.values()) {
        //     if (node.active) activeNodes.add(node);
        //   }
        //
        //   if (activeNodes.isEmpty()) return selected;
        //
        //   int startIdx = hash % activeNodes.size();
        //   for (int i = 0; i < Math.min(count, activeNodes.size()); i++) {
        //     int idx = (startIdx + i) % activeNodes.size();
        //     selected.add(activeNodes.get(idx));
        //   }
        //
        //   return selected;

        return new ArrayList<>(); // Replace
    }

    /**
     * Read repair: propagate latest value to lagging replicas
     * Time: O(N)
     *
     * TODO: Implement read repair
     * 1. Identify stale replicas (old versions)
     * 2. Write latest value to them
     */
    private void readRepair(String key, VersionedValue latestValue) {
        // TODO: Get all replicas
        //   List<Node> replicas = selectNodes(key, replicationFactor);

        // TODO: Update stale replicas
        //   for (Node node : replicas) {
        //     if (!node.active) continue;
        //
        //     VersionedValue current = node.get(key);
        //     if (current == null ||
        //         current.version.vectorClock < latestValue.version.vectorClock) {
        //       node.put(key, latestValue);
        //     }
        //   }
    }

    /**
     * Hinted handoff: store writes for failed nodes
     * Time: O(1)
     *
     * TODO: Implement hinted handoff
     * 1. If replica node is down, write hint to another node
     * 2. When node comes back up, replay hints
     */
    private void hintedHandoff(String key, VersionedValue value, int failedNodeId) {
        // TODO: Select temporary node to store hint
        // TODO: Store hint with metadata (target node, key, value)
        // TODO: Periodically check if target node is back up
        // TODO: Replay hints when target recovers
    }

    /**
     * Simulate node failure
     */
    public void simulateFailure(int nodeId) {
        Node node = nodes.get(nodeId);
        if (node != null) {
            node.active = false;
            System.out.println("Node " + nodeId + " FAILED");
        }
    }

    /**
     * Simulate node recovery
     */
    public void simulateRecovery(int nodeId) {
        Node node = nodes.get(nodeId);
        if (node != null) {
            node.active = true;
            System.out.println("Node " + nodeId + " RECOVERED");
        }
    }

    /**
     * Get cluster status
     */
    public void printStatus() {
        System.out.println("Cluster: " + nodes.size() + " nodes, " +
            "R=" + readQuorum + ", W=" + writeQuorum + ", RF=" + replicationFactor);

        for (Node node : nodes.values()) {
            System.out.println("  Node " + node.id + ": " +
                (node.active ? "ACTIVE" : "FAILED") +
                " (" + node.data.size() + " keys)");
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
```

**Runnable Client Code:**

```java
public class QuorumClient {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Quorum Consensus Demo ===\n");

        // Create cluster: 5 nodes, RF=3, R=2, W=2
        // R + W = 4 > RF = 3, so we have strong consistency
        QuorumConsensus cluster = new QuorumConsensus(5, 3, 2, 2);

        cluster.printStatus();

        // Test 1: Write and read
        System.out.println("\n--- Test 1: Write and Read ---");
        boolean writeSuccess = cluster.write("user:1", "Alice");
        System.out.println("Write 'user:1=Alice': " + writeSuccess);

        QuorumConsensus.VersionedValue value = cluster.read("user:1");
        System.out.println("Read 'user:1': " + value);

        // Test 2: Multiple writes
        System.out.println("\n--- Test 2: Multiple Writes ---");
        cluster.write("user:2", "Bob");
        cluster.write("user:3", "Charlie");
        cluster.write("user:4", "Diana");

        System.out.println("Read 'user:2': " + cluster.read("user:2"));
        System.out.println("Read 'user:3': " + cluster.read("user:3"));
        System.out.println("Read 'user:4': " + cluster.read("user:4"));

        // Test 3: Node failure
        System.out.println("\n--- Test 3: Node Failure ---");
        cluster.simulateFailure(2);
        cluster.printStatus();

        writeSuccess = cluster.write("user:5", "Eve");
        System.out.println("Write after failure: " + writeSuccess);

        value = cluster.read("user:5");
        System.out.println("Read after failure: " + value);

        // Test 4: Multiple node failures
        System.out.println("\n--- Test 4: Multiple Failures ---");
        cluster.simulateFailure(4);
        cluster.printStatus();

        writeSuccess = cluster.write("user:6", "Frank");
        System.out.println("Write with 2 nodes down: " + writeSuccess);

        // Test 5: Node recovery
        System.out.println("\n--- Test 5: Node Recovery ---");
        cluster.simulateRecovery(2);
        cluster.simulateRecovery(4);
        cluster.printStatus();

        cluster.write("user:7", "Grace");
        System.out.println("Read 'user:7': " + cluster.read("user:7"));

        // Test 6: Version conflict resolution
        System.out.println("\n--- Test 6: Updates ---");
        cluster.write("counter", "1");
        Thread.sleep(10);
        cluster.write("counter", "2");
        Thread.sleep(10);
        cluster.write("counter", "3");

        value = cluster.read("counter");
        System.out.println("Read 'counter' (should be latest): " + value);

        cluster.shutdown();
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for when to use each consensus pattern.

### Question 1: Leader Election vs Leaderless?

Answer after implementation:

**Use Leader Election when:**
- Single coordinator needed: _[One node must make decisions]_
- Simplify operations: _[Leader handles all writes]_
- Strong consistency: _[Leader ensures ordering]_
- Examples: _[Master-worker, coordinator services]_

**Use Leaderless (Quorum) when:**
- High availability: _[No single point of failure]_
- Multi-datacenter: _[Local writes in each DC]_
- Read/write balance: _[Tune R/W for workload]_
- Examples: _[Cassandra, DynamoDB]_

### Question 2: When to use Raft vs Paxos?

**Raft when:**
- Understandability: _[Easier to implement and reason about]_
- Log replication: _[Need ordered log of operations]_
- Modern systems: _[etcd, Consul use Raft]_

**Paxos when:**
- Proven formal correctness: _[Mathematically proven]_
- Legacy systems: _[Google Chubby uses Paxos]_
- Academic interest: _[Understanding distributed consensus theory]_

### Question 3: When to use distributed locks?

**Use distributed locks when:**
- Mutual exclusion: _[Only one process should access resource]_
- Job scheduling: _[Prevent duplicate job execution]_
- Leader election: _[Simple leader election mechanism]_

**Avoid distributed locks when:**
- Performance critical: _[Locks add latency]_
- Can use optimistic locking: _[Version-based concurrency control]_
- Idempotent operations: _[Can safely retry without lock]_

### Your Decision Tree

Build this after solving practice scenarios:

```
Consensus Pattern Selection
│
├─ Need single coordinator?
│   ├─ YES → Leader Election
│   │   ├─ Simple cluster? → Bully algorithm
│   │   └─ Production system? → Raft
│   └─ NO → Continue to Q2
│
├─ Q2: What's the consistency requirement?
│   ├─ Strong consistency → Raft or Paxos
│   ├─ Eventual consistency → Quorum with R=1, W=1
│   └─ Tunable consistency → Quorum with configurable R/W
│
├─ Q3: What's the failure scenario?
│   ├─ Network partitions → Raft (CP in CAP)
│   ├─ Node failures → Quorum with hints
│   └─ Split-brain → Leader election with fencing
│
└─ Q4: What's the use case?
    ├─ Configuration management → Raft (etcd, ZooKeeper)
    ├─ Distributed database → Quorum consensus
    ├─ Job coordination → Distributed locks
    └─ Cache invalidation → No consensus needed (best-effort)
```

### The "Kill Switch" - When NOT to use each

**Don't use Leader Election when:**
1. _[High write throughput needed - leader bottleneck]_
2. _[Multi-region writes - leader far from writers]_
3. _[Can tolerate temporary inconsistency]_

**Don't use Raft when:**
1. _[Very large clusters (>7 nodes) - election overhead]_
2. _[Don't need ordered log - simpler consensus works]_
3. _[Network partitions common - may have availability issues]_

**Don't use Distributed Locks when:**
1. _[Lock holder crashes - deadlock risk]_
2. _[Network delays unpredictable - lock expiry issues]_
3. _[Operations are idempotent - don't need lock]_

**Don't use Quorum when:**
1. _[Small cluster (< 3 nodes) - can't form majority]_
2. _[Read latency critical - quorum reads slower]_
3. _[Strong consistency with single writer - just use leader]_

### The Rule of Three: Alternatives

**Option 1: Raft (Leader-based)**
- Pros: _[Strong consistency, ordered log, understandable]_
- Cons: _[Leader bottleneck, election downtime, complex implementation]_
- Use when: _[Configuration management, log replication]_

**Option 2: Quorum (Leaderless)**
- Pros: _[High availability, tunable consistency, multi-datacenter]_
- Cons: _[Conflict resolution needed, read repair overhead, eventual consistency by default]_
- Use when: _[Distributed databases, high throughput]_

**Option 3: Distributed Locks (Coordination)**
- Pros: _[Simple mutual exclusion, works across services, fencing prevents errors]_
- Cons: _[Deadlock risk, latency overhead, requires lock service]_
- Use when: _[Job scheduling, leader election, resource allocation]_

---

## Practice

### Scenario 1: Distributed Database Leader Election

**Requirements:**
- 5-node database cluster
- Need single primary for writes
- Automatic failover on primary failure
- Must prevent split-brain
- Downtime < 5 seconds on failure

**Your design:**

Leader election algorithm: _[Raft or Bully? Why?]_

Reasoning:
- Election speed: _[Fill in]_
- Split-brain prevention: _[How?]_
- Failure detection: _[Heartbeat timeout?]_

Implementation details:
1. _[Heartbeat interval and timeout values]_
2. _[How to handle network partition]_
3. _[Fencing mechanism to prevent dual-primary]_

### Scenario 2: Distributed Job Scheduler

**Requirements:**
- 1000 scheduled jobs
- Must run exactly once
- Multiple scheduler instances for HA
- Jobs can take 1-60 minutes
- Must handle scheduler crashes

**Your design:**

Lock mechanism: _[Distributed locks or leader election?]_

Why?
1. _[Exactly-once execution guarantee]_
2. _[How to handle lock holder crash]_
3. _[Lock timeout calculation]_

Lock implementation:
- TTL: _[How long?]_
- Renewal: _[When and how often?]_
- Fencing: _[Prevent duplicate execution how?]_

### Scenario 3: Multi-Region Key-Value Store

**Requirements:**
- 3 datacenters (US, EU, Asia)
- Need low latency reads in each region
- Eventual consistency acceptable
- 1M writes/sec globally
- Must survive datacenter failure

**Your design:**

Consensus approach: _[Quorum or leader?]_

Quorum configuration:
- Replication factor: _[How many DCs?]_
- Read quorum: _[R = ?]_
- Write quorum: _[W = ?]_
- Reasoning: _[R + W > N?]_

Trade-offs:
1. _[Consistency vs availability]_
2. _[Cross-DC write latency]_
3. _[Conflict resolution strategy]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
  - [ ] Leader election works (Bully algorithm)
  - [ ] Raft election and log replication work
  - [ ] Distributed locks acquire, release, renew work
  - [ ] Quorum reads and writes work
  - [ ] All client code runs successfully

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Understand split-brain problem
  - [ ] Know how Raft achieves consensus
  - [ ] Understand fencing tokens
  - [ ] Know how quorums provide consistency

- [ ] **Failure Scenarios**
  - [ ] Leader failure and re-election
  - [ ] Lock holder crashes (deadlock prevention)
  - [ ] Network partition (split-brain)
  - [ ] Quorum not reachable
  - [ ] Node recovery and catch-up

- [ ] **Decision Making**
  - [ ] Know when to use leader vs leaderless
  - [ ] Know when to use Raft vs Paxos
  - [ ] Completed practice scenarios
  - [ ] Can explain trade-offs in CAP theorem

- [ ] **Mastery Check**
  - [ ] Could implement leader election from memory
  - [ ] Could design consensus for new system
  - [ ] Understand Raft log replication
  - [ ] Know how to prevent split-brain
  - [ ] Can calculate quorum sizes for requirements

---

**Next:** [15. Full System Designs →](15-full-system-designs.md)

**Back:** [13. Distributed Transactions ←](13-distributed-transactions.md)

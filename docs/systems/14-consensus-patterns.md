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

## Quick Quiz (Do BEFORE implementing)

**Your task:** Test your intuition about distributed consensus without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **Leader election with N nodes (Bully algorithm):**
    - Time complexity: _[Your guess: O(?)]_
    - Message complexity: _[How many messages?]_
    - Verified after learning: _[Actual: O(?)]_

2. **Raft log replication to majority of N nodes:**
    - Time complexity: _[Your guess: O(?)]_
    - When is an entry committed: _[Your guess]_
    - Verified: _[Actual]_

3. **Quorum read with R nodes, N total nodes:**
    - Time complexity: _[Your guess: O(?)]_
    - Space complexity per node: _[Your guess: O(?)]_
    - Verified: _[Actual]_

### Scenario Predictions

**Scenario 1:** 5-node cluster, leader fails during log replication

- **What happens to uncommitted entries?** _[Lost/Preserved - Why?]_
- **How long until new leader elected?** _[Depends on what?]_
- **Can clients write during election?** _[Yes/No - Why?]_

**Scenario 2:** Network partition splits 5 nodes into {3 nodes, 2 nodes}

- **Which partition can elect a leader?** _[3-node/2-node/Both - Why?]_
- **What happens to writes in minority partition?** _[Fill in]_
- **Is this a split-brain scenario?** _[Yes/No - Why?]_

**Scenario 3:** Distributed lock with 30-second TTL, holder crashes after 10 seconds

- **When can another process acquire the lock?** _[Immediately/After 20s/Never]_
- **Why that timing?** _[Fill in your reasoning]_
- **What could go wrong?** _[Fill in]_

### Trade-off Quiz

**Question:** When would leaderless (quorum) be BETTER than Raft for consensus?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning]_

**Question:** What's the MAIN requirement for achieving strong consistency with quorums?

-   [ ] R + W > N (where N is replication factor)
-   [ ] R + W = N
-   [ ] R = W = majority
-   [ ] R = N, W = 1

Verify after implementation: _[Which one(s)? Why?]_

**Question:** Why use fencing tokens with distributed locks?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after implementing Pattern 3]_

---

## Before/After: Why Consensus Matters

**Your task:** Compare naive distributed coordination vs proper consensus to understand the impact.

### Example: Leader Election Without Consensus

**Problem:** Multiple nodes need to coordinate on a single leader for a distributed database.

#### Approach 1: Naive Leader Election (No Consensus)

```java
// Naive approach - Highest ID claims leadership
public class NaiveLeaderElection {
    private int myId;
    private int leaderId;

    public void electLeader(Set<Integer> visibleNodes) {
        // Just pick the highest ID we can see
        int maxId = myId;
        for (int nodeId : visibleNodes) {
            if (nodeId > maxId) {
                maxId = nodeId;
            }
        }
        leaderId = maxId;

        if (leaderId == myId) {
            System.out.println("I am the leader!");
        }
    }
}
```

**What goes wrong: Network Partition Scenario**

```
Before partition:
Cluster: [Node 1, Node 2, Node 3, Node 4, Node 5]
Leader: Node 5 (highest ID)

After network partition:
Partition A: [Node 1, Node 2, Node 3]
Partition B: [Node 4, Node 5]

Partition A thinks: Node 3 is leader (highest visible)
Partition B thinks: Node 5 is leader (highest visible)

SPLIT-BRAIN: Two leaders accepting writes simultaneously!

Result:
- Data divergence (inconsistent state)
- Lost updates when partition heals
- Violated uniqueness guarantee
```

**Analysis:**

- Time: O(N) to scan visible nodes
- Space: O(1)
- Problem: No consensus, **split-brain during partition!**

- Failure rate: ~50% in networks with partitions

#### Approach 2: Raft Consensus (Safe Leader Election)

```java
// Raft approach - Majority vote required
public class RaftLeaderElection {
    private int currentTerm;
    private int votedFor;
    private int myId;

    public boolean electLeader(Set<Integer> allNodes) {
        currentTerm++;
        votedFor = myId;

        int votesReceived = 1; // Vote for self
        int majoritySize = (allNodes.size() / 2) + 1;

        // Request votes from all nodes
        for (int nodeId : allNodes) {
            if (nodeId != myId && requestVote(nodeId, currentTerm)) {
                votesReceived++;
            }
        }

        // Only become leader if MAJORITY votes received
        if (votesReceived >= majoritySize) {
            System.out.println("I am leader with " + votesReceived + " votes");
            return true;
        }
        return false;
    }
}
```

**Same network partition with Raft:**

```
After network partition:
Partition A: [Node 1, Node 2, Node 3] - 3 nodes, majority = 2
Partition B: [Node 4, Node 5]         - 2 nodes, majority = 2

Partition A attempts election:
- Node 3 requests votes from Node 1, Node 2 (both visible)
- Node 3 gets 3 votes total → SUCCESS (3 ≥ 2 majority)
- Node 3 becomes leader ✓

Partition B attempts election:
- Node 5 requests votes from Node 4 (only visible node)
- Node 5 gets 2 votes total → FAIL (2 < 3 majority of 5 total)
- No leader elected ✗

Result:
- Only ONE leader (Node 3)
- Partition B cannot accept writes (no leader)
- Partition A continues operating safely
- No split-brain! ✓
- When partition heals, Node 5 recognizes Node 3 as leader
```

**Analysis:**

- Time: O(N) to request votes
- Space: O(1)
- Safety: **Prevents split-brain through majority requirement**

- Availability: Minority partition cannot elect leader (trade-off for safety)

#### Performance Comparison: Failure Scenarios

| Scenario | Naive Election | Raft Consensus |
|----------|----------------|----------------|
| Network partition | Split-brain (2 leaders) | Single leader in majority |
| Node failure | Immediate re-election | Election only if leader fails |
| Data consistency | Violated during partition | Preserved (CP in CAP) |
| Write availability | Both partitions accept | Only majority partition |

#### Why Does Raft Work?

**Key insight: The Majority Principle**

With 5 nodes, majority = 3:
- Any two majorities must overlap by at least 1 node
- That overlapping node prevents conflicting decisions
- Example: {Node 1, 2, 3} and {Node 3, 4, 5} both contain Node 3

```
Election term visualization:
Term 1: Node 5 is leader (got votes from 1, 3, 5)
Network partition occurs
Term 2: Node 3 attempts election
        - Gets votes from 1, 2, 3 (majority) → SUCCESS
        - Node 5 in minority cannot get majority → FAILS
Term 3: When partition heals, Node 5 sees Node 3 has higher term → steps down
```

**After implementing, explain in your own words:**

- _[Why does majority prevent split-brain?]_
- _[What's the trade-off between safety and availability?]_
- _[Why can't the minority partition elect a leader?]_

### Real-World Impact

**Without consensus (naive approach):**

- Google Cloud DNS split-brain (2015): Traffic routed to wrong servers
- MongoDB 2.4 split-brain: Accepted conflicting writes, data corruption
- Recovery time: Hours to manually resolve conflicts

**With consensus (Raft/Paxos):**

- etcd (Kubernetes): Thousands of clusters, zero split-brain incidents
- Consul: Service discovery with guaranteed consistency
- Recovery time: Seconds (automatic election)

**Your calculation:** For a 7-node cluster with network partition into {4, 3}:
- Naive approach: _____ leaders elected (how many?)
- Raft consensus: _____ leader(s) elected (in which partition?)
- Which partition can serve writes: _____

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

## Debugging Challenges

**Your task:** Find and fix bugs in broken consensus implementations. This tests your understanding of distributed systems failure modes.

### Challenge 1: Split-Brain in Leader Election

```java
/**
 * Leader election that's supposed to prevent split-brain.
 * This has 2 CRITICAL BUGS that allow multiple leaders.
 * Find them!
 */
public class BuggyLeaderElection {
    private Map<Integer, Node> nodes;
    private int majoritySize;

    public void startElection(int candidateId) {
        Node candidate = nodes.get(candidateId);
        candidate.role = NodeRole.CANDIDATE;
        candidate.currentTerm++;

        int votesReceived = 1; // Vote for self

        // BUG 1: Something wrong with vote counting
        for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
            int nodeId = entry.getKey();
            if (nodeId != candidateId) {
                boolean voteGranted = requestVote(nodeId, candidateId);
                votesReceived++;  // Counting even if vote not granted!
            }
        }

        // BUG 2: Wrong comparison operator
        if (votesReceived > majoritySize) {  // Should this be > or >= ?
            becomeLeader(candidateId);
        }
    }
}
```

**Your debugging:**

- **Bug 1 location:** _[Which line?]_
- **Bug 1 explanation:** _[What happens when vote is denied?]_
- **Bug 1 fix:** _[How to fix?]_

- **Bug 2 location:** _[Which line?]_
- **Bug 2 explanation:** _[When does > fail vs >= ?]_
- **Bug 2 fix:** _[Which operator is correct?]_

**Split-brain scenario:**

- 5 nodes, majoritySize = 3
- Network partition: {1, 2} and {3, 4, 5}
- Node 1 starts election, gets vote from Node 2
- Node 3 starts election, gets votes from 4, 5
- With bugs: _[How many leaders? Why?]_
- After fixes: _[How many leaders? Why?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 15):** Increments `votesReceived` unconditionally, even when `voteGranted` is false. Should only increment when vote is granted.

**Fix:**
```java
if (voteGranted) votesReceived++;
```

**Bug 2 (Line 19):** Uses `>` instead of `>=`. With 5 nodes, majority is 3. If candidate gets exactly 3 votes, `3 > 3` is false, so no leader elected!

**Fix:**
```java
if (votesReceived >= majoritySize) {
```

**With bugs:** In partition {1, 2}, Node 1 gets 2 votes but bug counts as 3+ → becomes leader. In partition {3, 4, 5}, Node 3 gets 3 votes → becomes leader. **Two leaders!**

**After fixes:** Node 1 gets 2 votes < 3 majority → no leader. Node 3 gets 3 votes ≥ 3 majority → becomes leader. **One leader only.**
</details>

---

### Challenge 2: Lost Commits in Raft

```java
/**
 * Raft log replication with a CRITICAL BUG.
 * Committed entries can be LOST after leader failure!
 */
public class BuggyRaftReplication {

    public boolean appendEntry(int leaderId, String command) {
        RaftNode leader = nodes.get(leaderId);
        if (leader.role != NodeRole.LEADER) return false;

        // Create log entry
        int newIndex = leader.getLastLogIndex() + 1;
        LogEntry entry = new LogEntry(leader.currentTerm, command, newIndex);
        leader.log.add(entry);

        // BUG: Replicate to nodes
        int replicatedCount = 0;  // Forgot to count leader!

        for (Map.Entry<Integer, RaftNode> e : nodes.entrySet()) {
            int nodeId = e.getKey();
            if (nodeId != leaderId && nodeActive.get(nodeId)) {
                boolean success = sendAppendEntries(leaderId, nodeId);
                if (success) replicatedCount++;
            }
        }

        // Commit if majority replicated
        if (replicatedCount >= majoritySize) {
            leader.commitIndex = newIndex;
            return true;
        }

        return false;
    }
}
```

**Your debugging:**

- **Bug location:** _[Which line?]_
- **Bug explanation:** _[Why can commits be lost?]_
- **Bug fix:** _[What should the count start at?]_

**Failure scenario:**

- 5 nodes (Node 1 = leader), majoritySize = 3
- Leader appends entry "SET x=1"
- Entry replicated to Node 2, Node 3 (2 nodes)
- Bug: replicatedCount = 2 < 3 majority → NOT committed
- Leader crashes before replicating to Node 4
- With bug: Entry lost (never committed)
- Trace through: _[Step by step, what happens?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (Line 17):** Initializes `replicatedCount = 0`, forgetting that the leader already has the entry in its log. Should start at 1.

**Fix:**
```java
int replicatedCount = 1; // Leader has it
```

**Why it matters:** With 5 nodes, majority = 3. If leader + 2 followers have the entry, that's 3 copies (majority). But bug counts only 2 followers, thinks it's not committed, and entry could be lost if leader crashes.

**Correct behavior:** Leader counts self + 2 followers = 3 ≥ majority → committed. Entry is safe even if leader fails.
</details>

---

### Challenge 3: Term Confusion in Raft

```java
/**
 * Raft RequestVote RPC with TERM HANDLING BUG.
 * Can accept votes from candidates with STALE terms!
 */
public class BuggyRequestVote {

    private boolean requestVote(int voterId, int candidateId, int candidateTerm) {
        RaftNode voter = nodes.get(voterId);

        // Check term
        if (candidateTerm < voter.currentTerm) {
            return false; // Reject outdated candidate
        }

        // BUG: Update voter's term
        voter.currentTerm = candidateTerm;
        // Missing: What should happen to voter.votedFor?

        // Grant vote if haven't voted
        if (voter.votedFor == -1) {
            voter.votedFor = candidateId;
            return true;
        }

        return false;
    }
}
```

**Your debugging:**

- **Bug location:** _[Which lines?]_
- **Bug explanation:** _[What state is not reset?]_
- **Bug fix:** _[What's missing after term update?]_

**Failure scenario:**

- Term 1: Node 3 votes for Node 5
- Term 2: Node 1 starts election, requests vote from Node 3
- Node 3's state: currentTerm=1, votedFor=5
- Node 1's term: currentTerm=2
- With bug: What happens to Node 3's votedFor?
- Expected: _[Should vote be granted? Why?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (After line 14):** When updating term, must reset `votedFor = -1` to allow voting in new term. Current code leaves old vote in place.

**Fix:**
```java
if (candidateTerm > voter.currentTerm) {
    voter.currentTerm = candidateTerm;
    voter.votedFor = -1;  // Reset vote for new term!
}
```

**Why it matters:** In new term, voter should be able to vote again. Without reset, voter stays committed to old vote, can't vote for anyone in new term, election may fail.

**Correct:** When Node 3 sees candidateTerm=2 > currentTerm=1, it resets votedFor=-1, then can vote for Node 1.
</details>

---

### Challenge 4: Log Inconsistency in Raft

```java
/**
 * Raft AppendEntries with LOG CONSISTENCY BUG.
 * Follower can accept entries that create holes in log!
 */
public class BuggyAppendEntries {

    private boolean appendEntries(int followerId, int leaderTerm,
                                   int prevLogIndex, int prevLogTerm,
                                   List<LogEntry> entries) {
        RaftNode follower = nodes.get(followerId);

        // Check term
        if (leaderTerm < follower.currentTerm) {
            return false;
        }

        // BUG: Missing log consistency check!
        // Should verify follower has entry at prevLogIndex with prevLogTerm

        // Append entries
        for (LogEntry entry : entries) {
            follower.log.add(entry);
        }

        return true;
    }
}
```

**Your debugging:**

- **Bug location:** _[What's missing?]_
- **Bug explanation:** _[What log inconsistency can occur?]_
- **Bug fix:** _[What check is needed?]_

**Failure scenario:**

- Leader log: [e1(term=1), e2(term=1), e3(term=2)]
- Follower log: [e1(term=1), e2(term=2)] (e2 has wrong term!)
- Leader sends: prevLogIndex=2, prevLogTerm=1, entries=[e3]
- With bug: _[What happens?]_
- Expected behavior: _[Should append be accepted?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (After line 16):** Missing log consistency check. Must verify that follower's log at `prevLogIndex` has term `prevLogTerm`.

**Fix:**
```java
// Check log consistency
if (prevLogIndex > 0) {
    if (prevLogIndex > follower.getLastLogIndex()) {
        return false; // Follower's log too short
    }
    if (follower.log.get(prevLogIndex - 1).term != prevLogTerm) {
        return false; // Term mismatch
    }
}
```

**Why it matters:** Raft requires logs to be consistent before appending. If follower has conflicting entry (different term at same index), must reject and let leader retry with earlier index.

**Correct:** Leader's prevLogTerm=1, but follower has term=2 at index 2 → reject. Leader decrements prevLogIndex and retries until logs match.
</details>

---

### Challenge 5: Distributed Lock Deadlock

```java
/**
 * Distributed lock with DEADLOCK BUG.
 * Lock can remain held forever if holder crashes!
 */
public class BuggyDistributedLock {

    public Lock tryAcquire(String resourceId, String ownerId) {
        Lock existingLock = locks.get(resourceId);

        // BUG: No expiration check!
        if (existingLock != null) {
            if (!existingLock.ownerId.equals(ownerId)) {
                return null; // Lock held by someone else
            }
        }

        // Acquire lock
        long fencingToken = tokenCounter.incrementAndGet();
        Lock newLock = new Lock(resourceId, ownerId, fencingToken, ttl);
        locks.put(resourceId, newLock);

        return newLock;
    }
}
```

**Your debugging:**

- **Bug location:** _[Which line?]_
- **Bug explanation:** _[When does deadlock occur?]_
- **Bug fix:** _[What check is missing?]_

**Failure scenario:**

- Client A acquires lock with 30s TTL
- Client A crashes after 5 seconds (doesn't release)
- Client B tries to acquire lock after 40 seconds
- With bug: _[Can Client B acquire lock? Why?]_
- Expected: _[Should lock be available?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (Line 11):** Doesn't check if existing lock is expired. Should call `existingLock.isExpired()` before rejecting acquisition.

**Fix:**
```java
if (existingLock != null && !existingLock.isExpired()) {
    if (!existingLock.ownerId.equals(ownerId)) {
        return null; // Lock still valid and held by someone else
    }
}
```

**Why it matters:** If lock holder crashes, lock expires after TTL. Without expiration check, lock remains held forever → deadlock. Other processes can never acquire.

**Correct:** After TTL expires, lock is considered released, can be acquired by another process. Prevents deadlock from crashed holders.
</details>

---

### Challenge 6: Quorum Read Inconsistency

```java
/**
 * Quorum read with CONSISTENCY BUG.
 * Can return stale data even with proper R/W settings!
 */
public class BuggyQuorumRead {

    public VersionedValue read(String key) {
        List<Node> replicas = selectNodes(key, replicationFactor);
        List<VersionedValue> responses = new ArrayList<>();

        // Read from R nodes
        int successCount = 0;
        for (Node node : replicas) {
            if (node.active && successCount < readQuorum) {
                VersionedValue value = node.get(key);
                if (value != null) {
                    responses.add(value);
                    successCount++;
                }
            }
        }

        // BUG: Return first response
        if (!responses.isEmpty()) {
            return responses.get(0);  // Wrong! Might be stale!
        }

        return null;
    }
}
```

**Your debugging:**

- **Bug location:** _[Which line?]_
- **Bug explanation:** _[Why can this return stale data?]_
- **Bug fix:** _[What should be returned instead?]_

**Inconsistency scenario:**

- R=2, W=2, N=3 (strong consistency: R+W > N)
- Node 1: value="v1" (version=1)
- Node 2: value="v2" (version=2, latest)
- Node 3: value="v2" (version=2)
- Read from Node 1, Node 2 (in that order)
- With bug: _[What value is returned?]_
- Expected: _[What should be returned?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (Line 23):** Returns first response without comparing versions. First response might be stale (lower version).

**Fix:**
```java
if (responses.size() >= readQuorum) {
    return resolveConflicts(responses); // Pick latest version
}
```

**Conflict resolution:**
```java
private VersionedValue resolveConflicts(List<VersionedValue> values) {
    VersionedValue latest = values.get(0);
    for (VersionedValue v : values) {
        if (v.version.vectorClock > latest.version.vectorClock) {
            latest = v;
        }
    }
    return latest;
}
```

**Why it matters:** Even with quorum, responses can have different versions. Must compare and return latest to guarantee consistency.

**Correct:** Compare Node 1 (v1) and Node 2 (v2), return v2 (higher version). Client sees consistent, latest data.
</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

-   [ ] Found all 8+ bugs across 6 challenges
-   [ ] Understood consensus failure modes (split-brain, lost commits, inconsistency)
-   [ ] Could explain WHY each bug violates safety properties
-   [ ] Learned common distributed systems mistakes

**Common consensus bugs you discovered:**

1. _[List patterns: vote counting, term handling, etc.]_
2. _[Fill in]_
3. _[Fill in]_

**Safety properties that bugs violated:**

- **Agreement:** _[Which bugs caused nodes to disagree?]_
- **Validity:** _[Which bugs caused invalid states?]_
- **Termination:** _[Which bugs caused deadlock/livelock?]_

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

-   [ ] **Implementation**
    -   [ ] Leader election works (Bully algorithm)
    -   [ ] Raft election and log replication work
    -   [ ] Distributed locks acquire, release, renew work
    -   [ ] Quorum reads and writes work
    -   [ ] All client code runs successfully

-   [ ] **Understanding**
    -   [ ] Filled in all ELI5 explanations
    -   [ ] Understand split-brain problem
    -   [ ] Know how Raft achieves consensus
    -   [ ] Understand fencing tokens
    -   [ ] Know how quorums provide consistency

-   [ ] **Failure Scenarios**
    -   [ ] Leader failure and re-election
    -   [ ] Lock holder crashes (deadlock prevention)
    -   [ ] Network partition (split-brain)
    -   [ ] Quorum not reachable
    -   [ ] Node recovery and catch-up

-   [ ] **Decision Making**
    -   [ ] Know when to use leader vs leaderless
    -   [ ] Know when to use Raft vs Paxos
    -   [ ] Completed practice scenarios
    -   [ ] Can explain trade-offs in CAP theorem

-   [ ] **Mastery Check**
    -   [ ] Could implement leader election from memory
    -   [ ] Could design consensus for new system
    -   [ ] Understand Raft log replication
    -   [ ] Know how to prevent split-brain
    -   [ ] Can calculate quorum sizes for requirements

---

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery of consensus patterns through explanation and application. You cannot move forward until you can confidently complete this section.

### Gate 1: Explain to a Systems Engineer

**Scenario:** A backend engineer asks you about distributed consensus for a new service.

**Your explanation (write it out):**

> "Distributed consensus is..."
>
> _[Fill in your explanation in plain English - 3-4 sentences max]_

**When to use each pattern:**

> "Use leader election when..."
>
> _[Fill in - 1 sentence]_

> "Use Raft when..."
>
> _[Fill in - 1 sentence]_

> "Use quorum consensus when..."
>
> _[Fill in - 1 sentence]_

**Self-assessment:**

- Clarity score (1-10): ___
- Could your explanation convince someone to use Raft vs Paxos? _[Yes/No]_
- Did you explain the split-brain problem? _[Yes/No]_

If you scored below 7 or answered "No" to either question, revise your explanation.

---

### Gate 2: Whiteboard Exercise

**Task:** Draw how Raft handles a network partition, without looking at code.

**Draw the cluster state transitions:**

```
5-node cluster: [1, 2, 3, 4, 5]
Initial state: Node 5 is leader (Term 1)

Network partition splits into {1, 2, 3} and {4, 5}

Step 1: [Draw initial state with leader and followers]
        _________________________________

Step 2: [Draw partition - which can elect new leader?]
        _________________________________

Step 3: [Draw election in majority partition]
        _________________________________

Step 4: [What happens to writes in each partition?]
        _________________________________
```

**Verification:**

-   [ ] Drew term numbers correctly
-   [ ] Showed vote counts and majority calculation
-   [ ] Explained why minority partition cannot elect leader
-   [ ] Identified what happens when partition heals

---

### Gate 3: Pattern Recognition Test

**Without looking at your notes, classify these scenarios:**

| Scenario | Pattern | Why? |
|----------|---------|------|
| Need single coordinator for database writes | _[Fill in]_ | _[Explain]_ |
| Multi-datacenter database with high availability | _[Fill in]_ | _[Explain]_ |
| Prevent duplicate job execution across workers | _[Fill in]_ | _[Explain]_ |
| Configuration store (like etcd/ZooKeeper) | _[Fill in]_ | _[Explain]_ |
| Globally distributed key-value store | _[Fill in]_ | _[Explain]_ |
| Coordination service with strong consistency | _[Fill in]_ | _[Explain]_ |

**Score:** ___/6 correct

If you scored below 5/6, review the decision framework and try again.

---

### Gate 4: Failure Mode Analysis

**Complete this table from memory:**

| Failure Scenario | Leader Election | Raft | Quorum | Why? |
|------------------|----------------|------|--------|------|
| Network partition | _[Impact?]_ | _[Impact?]_ | _[Impact?]_ | _[Explain]_ |
| Leader crashes | _[Impact?]_ | _[Impact?]_ | _[Impact?]_ | _[Explain]_ |
| Minority nodes fail | _[Impact?]_ | _[Impact?]_ | _[Impact?]_ | _[Explain]_ |
| Clock skew | _[Impact?]_ | _[Impact?]_ | _[Impact?]_ | _[Explain]_ |

**Deep question:** How does Raft prevent split-brain during network partition?

Your answer: _[Fill in - explain the majority principle]_

**Deep question:** What does R + W > N guarantee in quorum systems?

Your answer: _[Fill in - explain consistency guarantee]_

---

### Gate 5: Trade-off Decision

**Scenario:** You're designing a distributed database that must survive datacenter failures.

**Requirements:**

- 3 datacenters (US-East, US-West, EU)
- Need strong consistency
- Must handle datacenter network partition
- 1M writes/second
- Read latency critical

**Option A:** Raft consensus (single leader)
- Consistency: _[Strong/Eventual]_
- Availability during partition: _[Fill in]_
- Write latency: _[Fill in - cross-datacenter?]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Option B:** Quorum (R=2, W=2, N=3)
- Consistency: _[Strong/Eventual]_
- Availability during partition: _[Fill in]_
- Write latency: _[Fill in - local?]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Your decision:** I would choose _[A/B]_ because...

_[Fill in your reasoning - consider CAP theorem, latency, and failure modes]_

**What would make you change your decision?**

- _[Fill in - what constraints would flip your choice?]_

---

### Gate 6: Implement Leader Election from Memory (Final Test)

**Set a 15-minute timer. Implement without looking at notes:**

```java
/**
 * Implement: Raft leader election (simplified)
 * - Start election for a candidate node
 * - Request votes from all nodes
 * - Become leader if majority votes received
 * - Handle term numbers correctly
 */
public class RaftElection {
    static class Node {
        int id;
        int currentTerm;
        int votedFor;
        boolean isLeader;
    }

    private Map<Integer, Node> nodes;
    private int majoritySize;

    public boolean startElection(int candidateId) {
        // Your implementation here




        return false; // Replace
    }

    private boolean requestVote(int voterId, int candidateId, int candidateTerm) {
        // Your implementation here



        return false; // Replace
    }
}
```

**After implementing, test with:**

- 5 nodes, candidate = Node 3
- Expected: Node 3 becomes leader if gets ≥3 votes
- Test failure: What if Node 3 already voted in this term?

**Verification:**

-   [ ] Implemented term increment correctly
-   [ ] Counted votes including self-vote
-   [ ] Used majority check (>=, not >)
-   [ ] Handled term comparison in requestVote
-   [ ] Reset votedFor when seeing higher term

---

### Gate 7: Safety Property Check

**The ultimate test: Can you identify violations?**

**Task:** For each scenario, identify which safety property is violated (if any).

**Scenario 1:** Two nodes both think they're leaders in same term
- Property violated: _[Agreement/Validity/Termination/None]_
- Why: _[Fill in]_
- How consensus prevents it: _[Fill in]_

**Scenario 2:** Committed log entry is lost after leader failure
- Property violated: _[Agreement/Validity/Termination/None]_
- Why: _[Fill in]_
- How Raft prevents it: _[Fill in]_

**Scenario 3:** Node accepts vote from candidate with older term
- Property violated: _[Agreement/Validity/Termination/None]_
- Why: _[Fill in]_
- How to prevent: _[Fill in]_

**Scenario 4:** Distributed lock remains held forever after holder crashes
- Property violated: _[Agreement/Validity/Termination/None]_
- Why: _[Fill in]_
- How to prevent: _[Fill in]_

**Scenario 5:** Quorum read returns stale data with R=2, W=2, N=3
- Property violated: _[Agreement/Validity/Termination/None]_
- Why: _[Fill in - what went wrong?]_
- How to prevent: _[Fill in]_

---

### Gate 8: Teaching Check

**The ultimate test of understanding is teaching.**

**Task:** Explain to an imaginary junior developer why we need consensus in distributed systems.

Your explanation:

> "Imagine you're building a distributed database with 5 servers..."
>
> _[Fill in - use a concrete example of what goes wrong without consensus]_

**Common misconceptions to address:**

1. "Can't we just use the highest node ID as leader?"
    - Your answer: _[Fill in - explain split-brain]_

2. "Why not just retry on failure instead of using consensus?"
    - Your answer: _[Fill in - explain why retries aren't enough]_

3. "Isn't consensus slow because of all the messaging?"
    - Your answer: _[Fill in - explain trade-offs]_

---

### Gate 9: CAP Theorem Application

**Task:** Classify each consensus pattern in CAP theorem.

**Raft:**

- During normal operation: _[CP/AP/CA - which two?]_
- During network partition: _[Chooses C or A?]_
- Why: _[Fill in reasoning]_

**Quorum (R=1, W=1, N=3):**

- Consistency: _[Strong/Eventual]_
- Availability: _[High/Medium/Low]_
- CAP classification: _[CP/AP]_

**Quorum (R=2, W=2, N=3):**

- Consistency: _[Strong/Eventual - why different?]_
- Availability: _[High/Medium/Low]_
- CAP classification: _[CP/AP]_

**Distributed Locks:**

- Depends on: _[What makes it CP vs AP?]_
- With expiration: _[CP/AP - why?]_
- Without expiration: _[CP/AP - why?]_

**Deep question:** Can you design an eventually consistent leader election?

Your answer: _[Yes/No - explain why or why not]_

---

### Mastery Certification

**I certify that I can:**

-   [ ] Implement leader election (Bully or Raft) from memory
-   [ ] Explain split-brain problem and how consensus prevents it
-   [ ] Design consensus strategy for new distributed system
-   [ ] Identify correct consensus pattern for requirements
-   [ ] Analyze failure modes and their impact
-   [ ] Debug common consensus bugs (vote counting, term handling, etc.)
-   [ ] Explain trade-offs between leader-based and leaderless
-   [ ] Apply CAP theorem to consensus decisions
-   [ ] Calculate quorum sizes for consistency requirements
-   [ ] Teach consensus concepts to someone else

**Self-assessment score:** ___/10

**Proof of mastery (choose one to complete):**

**Option A:** Implement complete Raft leader election + log replication from memory (30 mins)

**Option B:** Design consensus strategy for real-world system:
- E-commerce order processing across 3 datacenters
- Must prevent duplicate orders
- Must handle datacenter failure
- Specify: _[Leader election/Raft/Quorum/Locks - why?]_
- Draw architecture: _[How does it work?]_
- Failure analysis: _[What happens when...?]_

**Option C:** Debug a complex consensus bug:
- 5-node Raft cluster
- Client writes "SET x=1", leader replicates to 2 nodes, commits
- Leader crashes, new leader elected
- New leader doesn't have "SET x=1" in committed log
- What went wrong? _[Detailed analysis]_
- How to fix? _[Explain the bug and solution]_

**If score < 8:** Review the sections where you struggled, implement the proof of mastery, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered distributed consensus. Proceed to the next topic.

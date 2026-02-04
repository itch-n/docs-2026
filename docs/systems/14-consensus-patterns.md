# 14. Consensus Patterns

> Leader election, Raft consensus, distributed locks, and quorum-based systems

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing consensus patterns, explain them simply.

**Prompts to guide you:**

1. **What is consensus in one sentence?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

2. **Why do distributed systems need consensus?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

3. **Real-world analogy for leader election:**
    - Example: "Leader election is like choosing a class president where..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **What is the split-brain problem in one sentence?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

5. **Real-world analogy for distributed locks:**
    - Example: "A distributed lock is like a bathroom key that..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

6. **Why do we need quorums?**
    - Your answer: <span class="fill-in">[Fill in after practice]</span>

</div>

---

## Quick Quiz (Do BEFORE implementing)

<div class="learner-section" markdown>

**Your task:** Test your intuition about distributed consensus without looking at code. Answer these, then verify after
implementation.

### Complexity Predictions

1. **Leader election with N nodes (Bully algorithm):**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Message complexity: <span class="fill-in">[How many messages?]</span>
    - Verified after learning: <span class="fill-in">[Actual: O(?)]</span>

2. **Raft log replication to majority of N nodes:**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - When is an entry committed: <span class="fill-in">[Your guess]</span>
    - Verified: <span class="fill-in">[Actual]</span>

3. **Quorum read with R nodes, N total nodes:**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Space complexity per node: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified: <span class="fill-in">[Actual]</span>

### Scenario Predictions

**Scenario 1:** 5-node cluster, leader fails during log replication

- **What happens to uncommitted entries?** <span class="fill-in">[Lost/Preserved - Why?]</span>
- **How long until new leader elected?** <span class="fill-in">[Depends on what?]</span>
- **Can clients write during election?** <span class="fill-in">[Yes/No - Why?]</span>

**Scenario 2:** Network partition splits 5 nodes into {3 nodes, 2 nodes}

- **Which partition can elect a leader?** <span class="fill-in">[3-node/2-node/Both - Why?]</span>
- **What happens to writes in minority partition?** <span class="fill-in">[Fill in]</span>
- **Is this a split-brain scenario?** <span class="fill-in">[Yes/No - Why?]</span>

**Scenario 3:** Distributed lock with 30-second TTL, holder crashes after 10 seconds

- **When can another process acquire the lock?** <span class="fill-in">[Immediately/After 20s/Never]</span>
- **Why that timing?** <span class="fill-in">[Fill in your reasoning]</span>
- **What could go wrong?** <span class="fill-in">[Fill in]</span>

### Trade-off Quiz

**Question:** When would leaderless (quorum) be BETTER than Raft for consensus?

- Your answer: <span class="fill-in">[Fill in before implementation]</span>
- Verified answer: <span class="fill-in">[Fill in after learning]</span>

**Question:** What's the MAIN requirement for achieving strong consistency with quorums?

-   [ ] R + W > N (where N is replication factor)
-   [ ] R + W = N
-   [ ] R = W = majority
-   [ ] R = N, W = 1

Verify after implementation: <span class="fill-in">[Which one(s)? Why?]</span>

**Question:** Why use fencing tokens with distributed locks?

- Your answer: <span class="fill-in">[Fill in before implementation]</span>
- Verified answer: <span class="fill-in">[Fill in after implementing Pattern 3]</span>

</div>

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

| Scenario           | Naive Election            | Raft Consensus                |
|--------------------|---------------------------|-------------------------------|
| Network partition  | Split-brain (2 leaders)   | Single leader in majority     |
| Node failure       | Immediate re-election     | Election only if leader fails |
| Data consistency   | Violated during partition | Preserved (CP in CAP)         |
| Write availability | Both partitions accept    | Only majority partition       |

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

<div class="learner-section" markdown>

- <span class="fill-in">[Why does majority prevent split-brain?]</span>
- <span class="fill-in">[What's the trade-off between safety and availability?]</span>
- <span class="fill-in">[Why can't the minority partition elect a leader?]</span>

</div>

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

- Naive approach: <span class="fill-in">_____</span> leaders elected (how many?)
- Raft consensus: <span class="fill-in">_____</span> leader(s) elected (in which partition?)
- Which partition can serve writes: <span class="fill-in">_____</span>

---

## Core Implementation

### Pattern 1: Leader Election

**Concept:** Distributed algorithm to elect a single leader node from a cluster of nodes, ensuring only one leader
exists at any time.

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

**Concept:** Consensus algorithm that ensures replicated log consistency across distributed nodes through leader
election and log replication.

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

**Concept:** Mechanism to ensure mutual exclusion across distributed systems, preventing concurrent access to shared
resources.

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

**Your task:** Find and fix bugs in broken consensus implementations. This tests your understanding of distributed
systems failure modes.

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

        for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
            int nodeId = entry.getKey();
            if (nodeId != candidateId) {
                boolean voteGranted = requestVote(nodeId, candidateId);
                votesReceived++;  // Counting even if vote not granted!
            }
        }

        if (votesReceived > majoritySize) {  // Should this be > or >= ?
            becomeLeader(candidateId);
        }
    }
}
```

**Your debugging:**

- Bug 1: <span class="fill-in">[What\'s the bug?]</span>

- Bug 2: <span class="fill-in">[What\'s the bug?]</span>

**Split-brain scenario:**

- 5 nodes, majoritySize = 3
- Network partition: {1, 2} and {3, 4, 5}
- Node 1 starts election, gets vote from Node 2
- Node 3 starts election, gets votes from 4, 5
- With bugs: <span class="fill-in">[How many leaders? Why?]</span>
- After fixes: <span class="fill-in">[How many leaders? Why?]</span>

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 15):** Increments `votesReceived` unconditionally, even when `voteGranted` is false. Should only increment
when vote is granted.

**Fix:**

```java
if (voteGranted) votesReceived++;
```

**Bug 2 (Line 19):** Uses `>` instead of `>=`. With 5 nodes, majority is 3. If candidate gets exactly 3 votes, `3 > 3`
is false, so no leader elected!

**Fix:**

```java
if (votesReceived >= majoritySize) {
```

**With bugs:** In partition {1, 2}, Node 1 gets 2 votes but bug counts as 3+ → becomes leader. In partition {3, 4, 5},
Node 3 gets 3 votes → becomes leader. **Two leaders!**

**After fixes:** Node 1 gets 2 votes < 3 majority → no leader. Node 3 gets 3 votes ≥ 3 majority → becomes leader. **One
leader only.**
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

- Bug: <span class="fill-in">[What\'s the bug?]</span>

**Failure scenario:**

- 5 nodes (Node 1 = leader), majoritySize = 3
- Leader appends entry "SET x=1"
- Entry replicated to Node 2, Node 3 (2 nodes)
- Bug: replicatedCount = 2 < 3 majority → NOT committed
- Leader crashes before replicating to Node 4
- With bug: Entry lost (never committed)
- Trace through: <span class="fill-in">[Step by step, what happens?]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (Line 17):** Initializes `replicatedCount = 0`, forgetting that the leader already has the entry in its log.
Should start at 1.

**Fix:**

```java
int replicatedCount = 1; // Leader has it
```

**Why it matters:** With 5 nodes, majority = 3. If leader + 2 followers have the entry, that's 3 copies (majority). But
bug counts only 2 followers, thinks it's not committed, and entry could be lost if leader crashes.

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

- Bug: <span class="fill-in">[What\'s the bug?]</span>

**Failure scenario:**

- Term 1: Node 3 votes for Node 5
- Term 2: Node 1 starts election, requests vote from Node 3
- Node 3's state: currentTerm=1, votedFor=5
- Node 1's term: currentTerm=2
- With bug: What happens to Node 3's votedFor?
- Expected: <span class="fill-in">[Should vote be granted? Why?]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (After line 14):** When updating term, must reset `votedFor = -1` to allow voting in new term. Current code leaves
old vote in place.

**Fix:**

```java
if (candidateTerm > voter.currentTerm) {
    voter.currentTerm = candidateTerm;
    voter.votedFor = -1;  // Reset vote for new term!
}
```

**Why it matters:** In new term, voter should be able to vote again. Without reset, voter stays committed to old vote,
can't vote for anyone in new term, election may fail.

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

- Bug: <span class="fill-in">[What\'s the bug?]</span>

**Failure scenario:**

- Leader log: [e1(term=1), e2(term=1), e3(term=2)]
- Follower log: [e1(term=1), e2(term=2)] (e2 has wrong term!)
- Leader sends: prevLogIndex=2, prevLogTerm=1, entries=[e3]
- With bug: <span class="fill-in">[What happens?]</span>
- Expected behavior: <span class="fill-in">[Should append be accepted?]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (After line 16):** Missing log consistency check. Must verify that follower's log at `prevLogIndex` has term
`prevLogTerm`.

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

**Why it matters:** Raft requires logs to be consistent before appending. If follower has conflicting entry (different
term at same index), must reject and let leader retry with earlier index.

**Correct:** Leader's prevLogTerm=1, but follower has term=2 at index 2 → reject. Leader decrements prevLogIndex and
retries until logs match.
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

- Bug: <span class="fill-in">[What\'s the bug?]</span>

**Failure scenario:**

- Client A acquires lock with 30s TTL
- Client A crashes after 5 seconds (doesn't release)
- Client B tries to acquire lock after 40 seconds
- With bug: <span class="fill-in">[Can Client B acquire lock? Why?]</span>
- Expected: <span class="fill-in">[Should lock be available?]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (Line 11):** Doesn't check if existing lock is expired. Should call `existingLock.isExpired()` before rejecting
acquisition.

**Fix:**

```java
if (existingLock != null && !existingLock.isExpired()) {
    if (!existingLock.ownerId.equals(ownerId)) {
        return null; // Lock still valid and held by someone else
    }
}
```

**Why it matters:** If lock holder crashes, lock expires after TTL. Without expiration check, lock remains held
forever → deadlock. Other processes can never acquire.

**Correct:** After TTL expires, lock is considered released, can be acquired by another process. Prevents deadlock from
crashed holders.
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

        if (!responses.isEmpty()) {
            return responses.get(0);  // Wrong! Might be stale!
        }

        return null;
    }
}
```

**Your debugging:**

- Bug: <span class="fill-in">[What\'s the bug?]</span>

**Inconsistency scenario:**

- R=2, W=2, N=3 (strong consistency: R+W > N)
- Node 1: value="v1" (version=1)
- Node 2: value="v2" (version=2, latest)
- Node 3: value="v2" (version=2)
- Read from Node 1, Node 2 (in that order)
- With bug: <span class="fill-in">[What value is returned?]</span>
- Expected: <span class="fill-in">[What should be returned?]</span>

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

**Why it matters:** Even with quorum, responses can have different versions. Must compare and return latest to guarantee
consistency.

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

1. <span class="fill-in">[List patterns: vote counting, term handling, etc.]</span>
2. <span class="fill-in">[Fill in]</span>
3. <span class="fill-in">[Fill in]</span>

**Safety properties that bugs violated:**

- **Agreement:** <span class="fill-in">[Which bugs caused nodes to disagree?]</span>
- **Validity:** <span class="fill-in">[Which bugs caused invalid states?]</span>
- **Termination:** <span class="fill-in">[Which bugs caused deadlock/livelock?]</span>

---

## Decision Framework

**Your task:** Build decision trees for when to use each consensus pattern.

### Question 1: Leader Election vs Leaderless?

Answer after implementation:

**Use Leader Election when:**

- Single coordinator needed: <span class="fill-in">[One node must make decisions]</span>
- Simplify operations: <span class="fill-in">[Leader handles all writes]</span>
- Strong consistency: <span class="fill-in">[Leader ensures ordering]</span>
- Examples: <span class="fill-in">[Master-worker, coordinator services]</span>

**Use Leaderless (Quorum) when:**

- High availability: <span class="fill-in">[No single point of failure]</span>
- Multi-datacenter: <span class="fill-in">[Local writes in each DC]</span>
- Read/write balance: <span class="fill-in">[Tune R/W for workload]</span>
- Examples: <span class="fill-in">[Cassandra, DynamoDB]</span>

### Question 2: When to use Raft vs Paxos?

**Raft when:**

- Understandability: <span class="fill-in">[Easier to implement and reason about]</span>
- Log replication: <span class="fill-in">[Need ordered log of operations]</span>
- Modern systems: <span class="fill-in">[etcd, Consul use Raft]</span>

**Paxos when:**

- Proven formal correctness: <span class="fill-in">[Mathematically proven]</span>
- Legacy systems: <span class="fill-in">[Google Chubby uses Paxos]</span>
- Academic interest: <span class="fill-in">[Understanding distributed consensus theory]</span>

### Question 3: When to use distributed locks?

**Use distributed locks when:**

- Mutual exclusion: <span class="fill-in">[Only one process should access resource]</span>
- Job scheduling: <span class="fill-in">[Prevent duplicate job execution]</span>
- Leader election: <span class="fill-in">[Simple leader election mechanism]</span>

**Avoid distributed locks when:**

- Performance critical: <span class="fill-in">[Locks add latency]</span>
- Can use optimistic locking: <span class="fill-in">[Version-based concurrency control]</span>
- Idempotent operations: <span class="fill-in">[Can safely retry without lock]</span>

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

Leader election algorithm: <span class="fill-in">[Raft or Bully? Why?]</span>

Reasoning:

- Election speed: <span class="fill-in">[Fill in]</span>
- Split-brain prevention: <span class="fill-in">[How?]</span>
- Failure detection: <span class="fill-in">[Heartbeat timeout?]</span>

Implementation details:

1. <span class="fill-in">[Heartbeat interval and timeout values]</span>
2. <span class="fill-in">[How to handle network partition]</span>
3. <span class="fill-in">[Fencing mechanism to prevent dual-primary]</span>

### Scenario 2: Distributed Job Scheduler

**Requirements:**

- 1000 scheduled jobs
- Must run exactly once
- Multiple scheduler instances for HA
- Jobs can take 1-60 minutes
- Must handle scheduler crashes

**Your design:**

Lock mechanism: <span class="fill-in">[Distributed locks or leader election?]</span>

Why?

1. <span class="fill-in">[Exactly-once execution guarantee]</span>
2. <span class="fill-in">[How to handle lock holder crash]</span>
3. <span class="fill-in">[Lock timeout calculation]</span>

Lock implementation:

- TTL: <span class="fill-in">[How long?]</span>
- Renewal: <span class="fill-in">[When and how often?]</span>
- Fencing: <span class="fill-in">[Prevent duplicate execution how?]</span>

### Scenario 3: Multi-Region Key-Value Store

**Requirements:**

- 3 datacenters (US, EU, Asia)
- Need low latency reads in each region
- Eventual consistency acceptable
- 1M writes/sec globally
- Must survive datacenter failure

**Your design:**

Consensus approach: <span class="fill-in">[Quorum or leader?]</span>

Quorum configuration:

- Replication factor: <span class="fill-in">[How many DCs?]</span>
- Read quorum: <span class="fill-in">[R = ?]</span>
- Write quorum: <span class="fill-in">[W = ?]</span>
- Reasoning: <span class="fill-in">[R + W > N?]</span>

Trade-offs:

1. <span class="fill-in">[Consistency vs availability]</span>
2. <span class="fill-in">[Cross-DC write latency]</span>
3. <span class="fill-in">[Conflict resolution strategy]</span>

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
- Specify: <span class="fill-in">[Leader election/Raft/Quorum/Locks - why?]</span>
- Draw architecture: <span class="fill-in">[How does it work?]</span>
- Failure analysis: <span class="fill-in">[What happens when...?]</span>

**Option C:** Debug a complex consensus bug:

- 5-node Raft cluster
- Client writes "SET x=1", leader replicates to 2 nodes, commits
- Leader crashes, new leader elected
- New leader doesn't have "SET x=1" in committed log
- What went wrong? <span class="fill-in">[Detailed analysis]</span>
- How to fix? <span class="fill-in">[Explain the bug and solution]</span>

**If score < 8:** Review the sections where you struggled, implement the proof of mastery, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered distributed consensus. Proceed to the next topic.

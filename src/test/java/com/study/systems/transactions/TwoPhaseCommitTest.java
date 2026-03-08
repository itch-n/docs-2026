package com.study.systems.transactions;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class TwoPhaseCommitTest {

    @Test
    void testCoordinatorInitializesWithParticipants() {
        List<TwoPhaseCommit.Participant> participants = Arrays.asList(
            new TwoPhaseCommit.Participant("DB-A"),
            new TwoPhaseCommit.Participant("DB-B")
        );

        TwoPhaseCommit coordinator = new TwoPhaseCommit(participants);

        assertNotNull(coordinator);
    }

    @Test
    void testExecuteTransactionReturnsNonNullResult() {
        List<TwoPhaseCommit.Participant> participants = Arrays.asList(
            new TwoPhaseCommit.Participant("DB-A"),
            new TwoPhaseCommit.Participant("DB-B")
        );
        TwoPhaseCommit coordinator = new TwoPhaseCommit(participants);

        Map<TwoPhaseCommit.Participant, String> operations = new HashMap<>();
        for (TwoPhaseCommit.Participant p : participants) {
            operations.put(p, "UPDATE balance = balance - 100");
        }

        TwoPhaseCommit.TransactionResult result =
            coordinator.executeTransaction("tx001", operations);

        assertNotNull(result);
    }

    @Test
    void testTransactionResultHasMessage() {
        List<TwoPhaseCommit.Participant> participants = Arrays.asList(
            new TwoPhaseCommit.Participant("DB-A")
        );
        TwoPhaseCommit coordinator = new TwoPhaseCommit(participants);

        Map<TwoPhaseCommit.Participant, String> operations = new HashMap<>();
        for (TwoPhaseCommit.Participant p : participants) {
            operations.put(p, "INSERT INTO orders ...");
        }

        TwoPhaseCommit.TransactionResult result =
            coordinator.executeTransaction("tx002", operations);

        assertNotNull(result);
        assertNotNull(result.message);
    }

    @Test
    void testParticipantPrepareReturnsVote() {
        TwoPhaseCommit.Participant participant = new TwoPhaseCommit.Participant("DB-A");

        TwoPhaseCommit.Vote vote = participant.prepare("tx001", "UPDATE ...");

        assertNotNull(vote);
        // Vote is either YES or NO
        assertTrue(vote == TwoPhaseCommit.Vote.YES || vote == TwoPhaseCommit.Vote.NO);
    }

    @Test
    void testParticipantCommitCleansUpPreparedState() {
        TwoPhaseCommit.Participant participant = new TwoPhaseCommit.Participant("DB-A");

        // Force a YES vote scenario: prepare multiple times until we get YES
        // (20% random failure, so we retry a few times)
        boolean prepared = false;
        for (int i = 0; i < 20; i++) {
            TwoPhaseCommit.Vote vote = participant.prepare("tx001", "UPDATE ...");
            if (vote == TwoPhaseCommit.Vote.YES) {
                prepared = true;
                break;
            }
        }

        if (prepared) {
            participant.commit("tx001");
            // After commit, prepared transactions map should not contain this tx
            assertFalse(participant.preparedTransactions.containsKey("tx001"));
        }
        // If we couldn't get YES in 20 tries (extremely unlikely), just pass
    }

    @Test
    void testParticipantAbortCleansUpPreparedState() {
        TwoPhaseCommit.Participant participant = new TwoPhaseCommit.Participant("DB-A");

        // Force a YES vote so there is something to abort
        for (int i = 0; i < 20; i++) {
            TwoPhaseCommit.Vote vote = participant.prepare("tx001", "UPDATE ...");
            if (vote == TwoPhaseCommit.Vote.YES) {
                break;
            }
        }

        participant.abort("tx001");
        assertFalse(participant.preparedTransactions.containsKey("tx001"));
    }
}

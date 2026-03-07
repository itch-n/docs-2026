package com.study.systems.eventsourcing;

import java.util.List;

public interface Projection {

    /**
     * Process a single domain event and update the read model.
     *
     * TODO: Implement in each concrete projection
     * - Dispatch on event type to the appropriate handler method
     * - Update the projection store (database table, cache, etc.)
     * - Must be idempotent: if this event was already applied, do not double-count
     * - Record the event version as the new checkpoint after successful update
     *
     * @param event   the event to process
     */
    void on(DomainEvent event);

    /**
     * Return the version of the last event successfully applied.
     * Used to resume from a checkpoint after restart.
     *
     * TODO: Implement getCheckpoint
     * - Load the persisted checkpoint from durable storage
     * - Return 0 if no checkpoint exists (projection has never run)
     */
    long getCheckpoint();

    /**
     * Reset the projection to empty state.
     * Called at the start of a full rebuild.
     *
     * TODO: Implement reset
     * - Delete or truncate the projection store
     * - Reset the checkpoint to 0
     */
    void reset();
}

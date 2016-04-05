package Model;

import io.vertx.core.Future;

/**
 * @author Robin Duda
 */
public interface AsyncVotingStore {
    /**
     * Receives voting data.
     *
     * @param future
     * @param vote   The upload to be inserted.
     */
    void upload(Future<Void> future, VoteBallot vote);

    /**
     * Get the results of a voting (includes voted keys)
     *
     * @param future
     * @param id     the id of the upload to retrieve.
     */
    void results(Future<VotingResult> future, String id);

    /**
     * Adds a new voting for record-keeping.
     *
     * @param future
     * @param voting the voting to be registered.
     */
    void add(Future<Void> future, Voting voting);

    /**
     * Terminates a voting, removing all attached content.
     *
     * @param future
     * @param voting the voting to be terminated.
     */
    void terminate(Future<Void> future, Voting voting);
}

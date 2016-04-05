package Model;

import io.vertx.core.Future;

/**
 * @author Robin Duda
 */
public interface AsyncClientMaster {
    /**
     * Sends the result of a voting to transmitter clients.
     *
     * @param votings reference to the database where the results may be fetched.
     * @param voting  contains the duration and id of the event, triggers the post
     *                event when the duration has passed.
     */
    void upload(Future<Void> future, AsyncVotingStore votings, Voting voting);

    /**
     * Instructs a receiver to receive votes for specified voting.
     *
     * @param voting containing the metadata for the vote.
     */
    void create(Future<Void> future, Voting voting);
}

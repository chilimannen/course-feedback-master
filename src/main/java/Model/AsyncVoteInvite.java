package Model;

import io.vertx.core.Future;

/**
 * @author Robin Duda
 *         <p/>
 *         Notifies the invited users on a specific domain.
 */
public interface AsyncVoteInvite {

    /**
     * Notifies users in the domain of a new course feedback request.
     * @param voting specifies the id of the voting and the users
     *               which should be notified by its group-domain.
     */
    void notify(Future<Void> future, Voting voting);
}

package Model;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.UpdateOptions;

/**
 * @author Robin Duda
 *         <p/>
 *         Async voting store implementation using MongoDB.
 */
public class VotingDB implements AsyncVotingStore {
    private static final String VOTES = "master_votes";
    private static final String METADATA = "master_metadata";
    private MongoClient client;

    public VotingDB(MongoClient client) {
        this.client = client;
    }


    @Override
    public void upload(Future<Void> future, VoteBallot vote) {

        JsonObject query = new JsonObject().put("id", vote.getId());
        UpdateOptions options = new UpdateOptions().setUpsert(true);
        JsonObject data = new JsonObject()
                .put("$pushAll", new JsonObject()
                        .put("votes", new JsonArray(Serializer.pack(vote.getVotes()))));

        client.updateWithOptions(VOTES, query, data, options, result -> {
            if (result.succeeded())
                future.complete();
            else {
                try {
                    throw result.cause();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                future.fail(result.cause());
            }
        });
    }

    @Override
    public void results(Future<VotingResult> future, String id) {
        JsonObject query = new JsonObject().put("id", id);

        client.findOne(METADATA, query, null, meta -> {

            if (meta.succeeded()) {
                client.findOne(VOTES, query, null, votes -> {
                    if (votes.succeeded() && votes.result() != null) {
                        VoteBallot ballot = ((VoteBallot) Serializer.unpack(votes.result(), VoteBallot.class));
                        VotingResult result = new VotingResult(ballot, (Voting) Serializer.unpack(meta.result(), Voting.class));

                        future.complete(result);
                    } else
                        future.fail(votes.cause());
                });
            } else
                future.fail(meta.cause());
        });
    }

    @Override
    public void add(Future<Void> future, Voting voting) {
        JsonObject query = new JsonObject(Serializer.pack(voting));

        client.save(METADATA, query, result -> {
            if (result.succeeded()) {
                future.complete();
            } else
                future.fail(result.cause());
        });
    }

    @Override
    public void terminate(Future<Void> future, Voting voting) {
        JsonObject query = new JsonObject().put("id", voting.getId());

        client.removeOne(VOTES, query, outer -> {

            if (outer.succeeded())
                client.removeOne(METADATA, query, inner -> {
                    if (inner.succeeded())
                        future.complete();
                    else
                        future.fail(inner.cause());
                });
            else
                future.fail(outer.cause());
        });
    }
}

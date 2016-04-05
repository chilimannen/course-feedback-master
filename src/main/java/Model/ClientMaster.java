package Model;

import Configuration.Config;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author Robin Duda
 *         <p/>
 *         Transmits the results of a voting to a
 */
public class ClientMaster implements AsyncClientMaster {
    private TokenFactory serverToken = new TokenFactory(Config.SERVER_SECRET);
    private static final int SYNCHRONIZE_MS = 2500;
    private Vertx vertx;

    public ClientMaster(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void upload(Future<Void> future, AsyncVotingStore votings, Voting voting) {
        vertx.setTimer(getMillisDuration(voting), event -> {
            Future<VotingResult> storage = Future.future();

            storage.setHandler(result -> {
                if (result.succeeded()) {

                    vertx.createHttpClient().post(Config.TRANSMITTER_PORT, "localhost", "/api/results", handler -> {
                        if (handler.statusCode() == HttpResponseStatus.OK.code())
                            future.complete();
                        else
                            future.fail(handler.statusMessage());

                    }).end(new JsonObject()
                            .put("voting", Serializer.json(result.result()))
                            .put("token", getServerToken())
                            .encode());
                } else
                    future.fail(result.cause());
            });
            votings.results(storage, voting.getId());
        });
    }

    @Override
    public void create(Future<Void> future, Voting voting) {

        vertx.createHttpClient().post(Config.RECEIVER_PORT, "localhost", "/api/create", handler -> {
            if (handler.statusCode() == HttpResponseStatus.OK.code())
                future.complete();
            else
                future.fail(handler.statusMessage());

        }).end(new JsonObject()
                .put("voting", Serializer.json(voting))
                .put("token", getServerToken())
                .encode());
    }


    private JsonObject getServerToken() {
        return Serializer.json(new Token(serverToken, Config.SERVER_NAME));
    }

    private long getMillisDuration(Voting voting) {
        return voting.getDuration().getEnd() - voting.getDuration().getBegin() + SYNCHRONIZE_MS;
    }
}

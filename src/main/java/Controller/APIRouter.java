package Controller;

import Configuration.Config;
import Model.*;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Robin Duda
 *         <p/>
 *         REST API Routes for creating new votes on this master,
 *         and to receive and aggregate votes from multiple clients.
 */
public class APIRouter {
    private TokenFactory serverToken = new TokenFactory(Config.SERVER_SECRET);
    private AsyncVotingStore votings;
    private AsyncClientMaster client;

    public APIRouter(Router router, AsyncVotingStore votings, AsyncClientMaster client) {
        this.votings = votings;
        this.client = client;

        router.post("/api/create").handler(this::create);
        router.post("/api/upload").handler(this::upload);
        router.post("/api/terminate").handler(this::terminate);
    }

    private void create(RoutingContext context) {
        HttpServerResponse response = context.response();

        if (authorized(context)) {
            Voting voting = (Voting) Serializer.unpack(context.getBodyAsJson().getJsonObject("voting"), Voting.class);
            Future<Void> future = Future.future();

            future.setHandler(storage -> {
                if (storage.succeeded())
                    response.setStatusCode(HttpResponseStatus.OK.code()).end();
                else
                    response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            });

            votings.add(future, voting);
            client.create(Future.future(), voting);
            client.upload(Future.future(), votings, voting);
        }
    }

    private void terminate(RoutingContext context) {
        HttpServerResponse response = context.response();

        if (authorized(context)) {
            Voting voting = (Voting) Serializer.unpack(context.getBodyAsJson().getJsonObject("voting"), Voting.class);
            Future<Void> future = Future.future();

            future.setHandler(storage -> {
                if (storage.succeeded())
                    response.setStatusCode(HttpResponseStatus.OK.code()).end();
                else
                    response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            });
            votings.terminate(future, voting);
        }
    }


    private void upload(RoutingContext context) {
        HttpServerResponse response = context.response();
        Future<Void> future = Future.future();

        if (authorized(context)) {
            VoteBallot vote = (VoteBallot) Serializer.unpack(context.getBodyAsJson().getJsonObject("voting"), VoteBallot.class);

            future.setHandler(storage -> {
                if (storage.succeeded())
                    response.setStatusCode(HttpResponseStatus.OK.code()).end();
                else
                    response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            });
            votings.upload(future, vote);
        }
    }


    private boolean authorized(RoutingContext context) {
        Token token = (Token) Serializer.unpack(context.getBodyAsJson().getJsonObject("token"), Token.class);
        boolean authorized = serverToken.verifyToken(token);

        if (!authorized)
            context.response().setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end();

        return authorized;
    }
}

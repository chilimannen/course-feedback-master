import Configuration.Config;
import Controller.WebServer;
import Model.*;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.Timeout;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Robin Duda
 *
 * Tests that votes are transmitted to clients on completion.
 */

@RunWith(VertxUnitRunner.class)
public class ClientMasterTest {
    private Vertx vertx;

    @Rule
    public Timeout timeout = Timeout.seconds(10);

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new WebServer(new VotingDBMock(), new ClientMaster(vertx)), context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }


    @Test
     public void shouldCreateVoteOnReceiver(TestContext context) {
        Async async = context.async();

        vertx.createHttpServer().requestHandler(request -> {
            request.bodyHandler(body -> {
                async.complete();
            });
        }).listen(Config.RECEIVER_PORT);

        vertx.createHttpClient().post(Config.WEB_PORT, "localhost", "/api/create", result -> {
            context.assertEquals(HttpResponseStatus.OK.code(), result.statusCode());

        }).end(new JsonObject()
                .put("token", APITest.getServerToken())
                .put("voting", APITest.getVotingConfiguration())
                .encode());
    }

    @Test
    public void shouldSendResultsToTransmitter(TestContext context) {
        Async async = context.async();

        vertx.createHttpServer().requestHandler(request -> {
            request.bodyHandler(body -> {
                async.complete();
            });
        }).listen(Config.TRANSMITTER_PORT);

        vertx.createHttpClient().post(Config.WEB_PORT, "localhost", "/api/create", result -> {
            context.assertEquals(HttpResponseStatus.OK.code(), result.statusCode());

        }).end(new JsonObject()
                .put("token", APITest.getServerToken())
                .put("voting", APITest.getVotingConfiguration())
                .encode());
    }
}

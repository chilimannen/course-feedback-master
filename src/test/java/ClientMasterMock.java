import Model.AsyncClientMaster;
import Model.AsyncVotingStore;
import Model.Voting;
import io.vertx.core.Future;

/**
 * Created by Robin on 2016-03-20.
 */
public class ClientMasterMock implements AsyncClientMaster {


    @Override
    public void upload(Future<Void> future, AsyncVotingStore votings, Voting voting) {
        future.complete();
    }

    @Override
    public void create(Future<Void> future, Voting voting) {
        future.complete();
    }
}

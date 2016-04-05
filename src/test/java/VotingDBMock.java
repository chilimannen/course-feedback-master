import Model.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

/**
 * @author Robin Duda
 *         <p/>
 *         Mock for the voting database.
 */
public class VotingDBMock implements AsyncVotingStore {
    private ArrayList<Voting> votings = new ArrayList<>();
    private ArrayList<VoteBallot> votes = new ArrayList<>();

    public VotingDBMock() {
        ArrayList<Query> options = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        values.add("a");
        values.add("b");
        values.add("c");

        options.add(
                new Query()
                        .setName("q1")
                        .setValues(values)
        );

        options.add(
                new Query()
                        .setName("q2")
                        .setValues(values)
        );

        votings.add(new Voting()
                        .setId("id")
                        .setTopic("Vote #1")
                        .setOwner("gosu")
                        .setOptions(options)
        );
        // upserted by mongodb
        votes.add(new VoteBallot("id"));

        ArrayList<Option> voteList = new ArrayList<>();
        Option option = new Option();
        option.setKey("my_key");
        option.setOption("the_option");
        option.setValue("the_value");
        voteList.add(option);

        upload(Future.future(), new VoteBallot().setId("id").setVotes(voteList));
    }

    @Override
    public void upload(Future<Void> future, VoteBallot vote) {
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).getId().equals(vote.getId()))
                for (Option option : vote.getVotes())
                    votes.get(i).getVotes().add(option);
        }

        future.complete();
    }

    @Override
    public void results(Future<VotingResult> future, String id) {
        future.complete(new VotingResult(votes.get(0), votings.get(0)));
    }

    @Override
    public void add(Future<Void> future, Voting voting) {
        votings.add(voting);
        future.complete();
    }

    @Override
    public void terminate(Future<Void> future, Voting voting) {
        for (int i = 0; i < votings.size(); i++) {
            if (votings.get(i).getId().equals(voting.getId())) {
                votings.remove(i);
                break;
            }
        }
        future.complete();
    }


}

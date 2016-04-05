import Model.*;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Robin Duda
 *
 * Verifies that the output of a VotingResult is correctly formatted.
 */

@RunWith(VertxUnitRunner.class)
public class VotingResultTest {

    @Test
    public void shouldMergeMetaAndVotes(TestContext context) {
        VotingResult result = getVotingResult();

        // Assert metadata.
        context.assertEquals("id", result.getId());
        context.assertEquals("ownar", result.getOwner());
        context.assertEquals("this the topic", result.getTopic());
        context.assertEquals(0L, result.getDuration().getBegin());
        context.assertEquals(0L, result.getDuration().getEnd());

        // Assert options.
        context.assertEquals(3, result.getOptions().size());
        context.assertEquals(1, result.getOptions().get(0).getValues().size());
        context.assertEquals(2, result.getOptions().get(1).getValues().size()); // yes - no
        context.assertEquals(1, result.getOptions().get(2).getValues().size());

        context.assertEquals("smurfs", result.getOptions().get(0).getName());
        context.assertEquals("yes", result.getOptions().get(0).getValues().get(0).getName());
        context.assertEquals(2, result.getOptions().get(0).getValues().get(0).getCount());
        context.assertTrue(result.getOptions().get(0).getValues().get(0).getKeys().contains("key1"));
        context.assertTrue(result.getOptions().get(0).getValues().get(0).getKeys().contains("key2"));
        context.assertFalse(result.getOptions().get(0).getValues().get(0).getKeys().contains("key3"));
    }

    public static VotingResult getVotingResult() {
        Voting voting = new Voting();
        VoteBallot ballot = new VoteBallot();

        voting.setId("id");
        voting.setOwner("ownar");
        voting.setDuration(new Duration().setBegin(0).setEnd(0));
        voting.setTopic("this the topic");

        ballot.setId("id");
        ballot.getVotes().add(new Option("key1", "smurfs", "yes"));
        ballot.getVotes().add(new Option("key2", "smurfs", "yes"));
        ballot.getVotes().add(new Option("key1", "kebab", "no"));
        ballot.getVotes().add(new Option("key2", "attack", "yes"));
        ballot.getVotes().add(new Option("key3", "attack", "no"));

        return new VotingResult(ballot, voting);
    }

}

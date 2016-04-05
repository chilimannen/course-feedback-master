package Model;

import java.util.ArrayList;

/**
 * @author Robin Duda
 *
 * Contains the results of a voting.
 */
public class VoteBallot {
    private String id;
    private ArrayList<Option> votes = new ArrayList<>();

    public VoteBallot() {
    }

    public VoteBallot(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public VoteBallot setId(String id) {
        this.id = id;
        return this;
    }

    public ArrayList<Option> getVotes() {
        return votes;
    }

    public VoteBallot setVotes(ArrayList<Option> votes) {
        this.votes = votes;
        return this;
    }
}

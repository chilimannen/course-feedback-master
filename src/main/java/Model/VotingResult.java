package Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Robin Duda
 *
 * Contains a complete voting result for transmission to distribution clients.
 */
public class VotingResult {
    private String owner;
    private String topic;
    private String id;
    private Duration duration;
    private ArrayList<Value> options = new ArrayList<>();

    public VotingResult() {
    }

    public VotingResult(VoteBallot result, Voting voting) {
        this.owner = voting.getOwner();
        this.topic = voting.getTopic();
        this.id = voting.getId();
        this.duration = voting.getDuration();

        HashMap<String, HashMap<String, VoteCount>> map = new HashMap<>();

        for (Option option : result.getVotes()) {

            // add all options not present in map.
            if (!map.containsKey(option.getOption()))
                map.put(option.getOption(), new HashMap<>());

            // add all values in the option not yet present
            if (!map.get(option.getOption()).containsKey(option.getValue()))
                map.get(option.getOption()).put(option.getValue(), new VoteCount());

            map.get(option.getOption()).get(option.getValue()).update(option.getKey());
        }

        int index = 0;
        // iterate the map and build the new format.
        for (String option : map.keySet().toArray(new String[map.keySet().size()])) {
            options.add(new Value(option));
            for (String value : map.get(option).keySet().toArray(new String[map.get(option).keySet().size()])) {
                options.get(index).getValues().add(map.get(option).get(value).setName(value));
            }
            index++;
        }
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getOwner() {
        return owner;
    }

    public VotingResult setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public VotingResult setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getId() {
        return id;
    }

    public VotingResult setId(String id) {
        this.id = id;
        return this;
    }

    public ArrayList<Value> getOptions() {
        return options;
    }

    public VotingResult setOptions(ArrayList<Value> options) {
        this.options = options;
        return this;
    }
}

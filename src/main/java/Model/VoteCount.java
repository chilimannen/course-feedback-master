package Model;

import java.util.ArrayList;

/**
 * @author Robin Duda
 *
 * Counts the votes cast on a single option.
 */
public class VoteCount {
    private String name;
    private int count;
    private ArrayList<String> keys = new ArrayList<>();


    public void update(String key) {
        this.count++;
        this.keys.add(key);
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public VoteCount setKeys(ArrayList<String> keys) {
        this.keys = keys;
        return this;
    }

    public String getName() {
        return name;
    }

    public VoteCount setName(String name) {
        this.name = name;
        return this;
    }

    public int getCount() {
        return count;
    }

    public VoteCount setCount(int count) {
        this.count = count;
        return this;
    }
}

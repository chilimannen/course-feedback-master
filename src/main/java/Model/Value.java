package Model;

import java.util.ArrayList;

/**
 * @author Robin Duda
 *
 * Holds a query and voted keys.
 */
public class Value {
    private String name;
    private ArrayList<VoteCount> values = new ArrayList<>();

    public Value() {
    }

    public Value(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<VoteCount> getValues() {
        return values;
    }

    public void setValues(ArrayList<VoteCount> values) {
        this.values = values;
    }
}

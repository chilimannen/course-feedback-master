package Model;

/**
 * @author Robin Duda
 *         <p/>
 *         An individual option in the upload.
 */
public class Option {
    private String key;
    private String option;
    private String value;

    public Option() {
    }

    public Option(String key, String option, String value) {
        this.key = key;
        this.option = option;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

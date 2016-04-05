package Model;


/**
 * @author Robin Duda
 *         <p/>
 *         The duration of a upload.
 */
public class Duration {
    private long begin;
    private long end;

    public long getBegin() {
        return begin;
    }

    public Duration setBegin(long begin) {
        this.begin = begin;
        return this;
    }

    public long getEnd() {
        return end;
    }

    public Duration setEnd(long end) {
        this.end = end;
        return this;
    }
}

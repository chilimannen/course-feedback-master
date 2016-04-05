package Configuration;

/**
 * @author Robin Duda
 *         <p/>
 *         Configuration file.
 */
public class Config {
    public static final int TRANSMITTER_PORT = 7670;
    public static final int RECEIVER_PORT = 9450;
    public static final int WEB_PORT = 9494;
    public static final String CONNECTION_STRING = "mongodb://localhost:27017/";
    public static final String DB_NAME = "vote";
    public static final String SERVER_NAME = "server.receiver";
    public static final byte[] SERVER_SECRET = "!!!!!!!!!!!server_secret!!!!!!!!!!".getBytes();
}

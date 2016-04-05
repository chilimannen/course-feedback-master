package Model;

import Configuration.Config;
import Configuration.Email;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.mail.*;

import java.util.ArrayList;

/**
 * @author Robin Duda
 *         <p/>
 *         Sample implementation of the notification system, this component
 *         communicates with the user directory to obtain users emails from a
 *         domain name. The domain name could be a course ID.
 *         <p/>
 *         WARNING - FOR DEMO USE ONLY!
 */
public class InvitationMailer implements AsyncVoteInvite {
    private MailClient client;

    public InvitationMailer(Vertx vertx) {
        MailConfig config = new MailConfig()
                .setPort(Email.PORT)
                .setHostname(Email.HOST)
                .setUsername(Email.USERNAME)
                .setPassword(Email.PASSWORD)
                .setStarttls(StartTLSOptions.REQUIRED)
                .setSsl(true)
                .setLogin(LoginOption.REQUIRED);

        client = MailClient.createNonShared(vertx, config);
    }

    /**
     * Sample implementation of a notification system.
     */
    @Override
    public void notify(Future<Void> future, Voting voting) {

        for (String mail : getUsers(voting.getOwner())) {
            MailMessage message = new MailMessage()
                    .setText(contents(voting, mail))
                    .setSubject("Course evaluation for " + voting.getTopic())
                    .setTo(mail)
                    .setFrom(Email.USERNAME);

            client.sendMail(message, result -> {
                if (result.failed())
                    result.cause().printStackTrace();
            });
        }
        future.complete();
    }

    private String contents(Voting voting, String user) {
        return "Please fill in the course evaluation here http://localhost:"
                + Config.RECEIVER_PORT
                + "/?id="
                + voting.getId()
                + "&user=" + user;
    }

    /**
     * Implement logic here to get the emails.
     */
    private ArrayList<String> getUsers(String domain) {
        ArrayList<String> users = new ArrayList<>();
        users.add("chilimannen93@gmail.com");
        return users;
    }
}

package config.services;

import config.beans.Moderator;
import config.beans.Poll;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by sudh on 2/27/2015.
 */
@Component
public interface IModeratorService {

    public Moderator createModerator(Moderator newMod);

    public Moderator viewModerator(int modId);

    public Moderator updateModerator(int id, Moderator mod);

    public Poll createPoll(int id, Poll newPoll);

    public Poll viewPoll(int modId, String pollId);

    public Poll viewPollWithoutResult(String pollId);

    public List<Poll> listPolls(int modId);

    void deletePoll(int modId, String pollId);

    void voteAPoll(String pollId, int choice);
}

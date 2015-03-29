package config.beans;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by sudh on 2/27/2015.
 */
@Document(collection="polls")
public class Poll {

    @Id
    String id;
    String question;
    String started_at;
    String expired_at;
    String[] choice;
    int[] results;

    Poll()
    {
        results = new int[2];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getStarted_at() {
        return started_at;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }

    public String[] getChoice() {
        return choice;
    }

    public void setChoice(String[] choices) {
        this.choice = choices;
    }

    public int[] getResults() {
        return results;
    }

    public void setResults(int[] results) {
        this.results = results;
    }
}

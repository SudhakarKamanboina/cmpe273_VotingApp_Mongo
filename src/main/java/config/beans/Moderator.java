package config.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sudh on 2/21/2015.
 */

@Document(collection="moderators")
public class Moderator {

    private Map<String, Poll> polls;

    @Id
    private int id;

    private String name;

    @NotEmpty @Email(message="Invalid Email Format")
    private String email;

    @NotEmpty @Size(min=2)
    private String password;
    private String created_at;
    private Poll poll;

    public Moderator()
    {
        polls = new HashMap<String, Poll>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @JsonIgnore
    public Poll getPoll(String id) {
        return polls.get(id);
    }

    public void setPoll(String id, Poll poll) {
        polls.put(id, poll);
    }

    @JsonIgnore
    public Collection<Poll> getAllPolls()
    {
        return polls.values();
    }

    public void deletePoll(String id)
    {
        polls.remove(id);
    }
}

package config.services;

import config.beans.Moderator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Created by sudh on 3/28/2015.
 */

@Component
public interface ModeratorRepository extends MongoRepository<Moderator, Integer> {

    Moderator save(Moderator saved);

    //Moderator findOne(int id);
}

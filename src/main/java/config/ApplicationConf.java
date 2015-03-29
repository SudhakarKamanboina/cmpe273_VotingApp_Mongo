package config;

/**
 * Created by sudh on 2/21/2015.
 */
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import config.services.ModeratorRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ComponentScan(basePackages = {"config"})
@EnableAutoConfiguration
@EnableMongoRepositories
@Configuration
    public class ApplicationConf extends AbstractMongoConfiguration
{
    public static void main(String[] args) {
        SpringApplication.run(ApplicationConf.class, args);
    }

    @Override
    protected String getDatabaseName() {
        return  "cmpe273";
    }

    @Override
    public MongoClient mongo() throws Exception {
        return new MongoClient("localhost",27017);
    }

    @Override
    protected String getMappingBasePackage() {
        return "config.beans";
    }

    @Bean
    MongoMappingContext springDataMongoMappingContext() {
        return new MongoMappingContext();
    }

}

package app.config;

import org.jongo.Jongo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

@Configuration
public class MongoDBConfig implements InitializingBean {

	@Value("${mongo.host:localhost}")
	private String mongoHost;

	@Value("${mongo.port:27017}")
	private Integer mongoPort;

	@Value("${mongo.db:mongoDemo}")
	private String mongoDatabase;

	private Mongo mongo;
	private DB db;

	@Bean
	public Jongo jongo() throws Exception {
		return new Jongo(db);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		mongo = new MongoClient(mongoHost, mongoPort);
		mongo.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		db = mongo.getDB(mongoDatabase);
	}

}

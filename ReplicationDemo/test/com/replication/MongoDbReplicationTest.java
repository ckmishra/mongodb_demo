package com.replication;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.ReplicaSetStatus;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;

public class MongoDbReplicationTest {

	MongoClient client;

	MongoCollection<Document> collection;

	@Before
	public void setUp() throws Exception {
		List<ServerAddress> servers = new ArrayList<>();
		servers.add(new ServerAddress("localhost", 27017));
		//servers.add(new ServerAddress("localhost", 27018));
		//servers.add(new ServerAddress("localhost", 27019));

		client = new MongoClient(servers);

		collection = client.getDatabase("demo").getCollection("users");

	}

	//@After
	public void tearDown() throws Exception {
		collection.drop();
		client.close();	
	}

	//@Test
	public void testFailOver() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			if (i == 20) {
				// step down
				ServerAddress master = client.getReplicaSetStatus().getMaster();
			}
			try {
				collection.insertOne(new Document().append("_id", i));
				System.out.println("Inserted Document : " + i);
			} catch (MongoException m) {
				System.out
						.println("Exception Occured while inserting document : "
								+ m.getMessage());
			}
			Thread.sleep(500);
		}

	}

	//@Test
	public void testWriteOnlyOnPrimary() {
		client.setWriteConcern(WriteConcern.W1);
		long startTime = System.currentTimeMillis();
		collection.insertOne(new Document().append("_id", 1));
		long endTime = System.currentTimeMillis();
		long diff = endTime - startTime;
		System.out.println("Total time (in mill seconds) for Primary : "
				+ (diff));
	}

	
	
	
	//@Test
	public void testWriteAndJournalOnlyOnPrimary() {
		client.setWriteConcern(WriteConcern.W1);
		client.setWriteConcern(WriteConcern.JOURNALED);
		long startTime = System.currentTimeMillis();
		collection.insertOne(new Document().append("_id", 1));
		long endTime = System.currentTimeMillis();

		long diff = endTime - startTime;

		System.out.println("Total time (in milli seconds) for Primary Journeled : "
				+ (diff ));
	}

	//@Test
	public void testWriteOnOneSecondary() {
		client.setWriteConcern(WriteConcern.W2);
		client.setWriteConcern(WriteConcern.JOURNALED);
		long startTime = System.currentTimeMillis();
		collection.insertOne(new Document().append("_id", 1));
		long endTime = System.currentTimeMillis();

		long diff = endTime - startTime;

		System.out
				.println("Total time (in milli seconds) for Primary and Secondary : "
						+ (diff ));

	}

	//@Test
	public void testWriteOnbothSecondary() {
		client.setWriteConcern(WriteConcern.W3);
		client.setWriteConcern(WriteConcern.JOURNALED);
		long startTime = System.currentTimeMillis();
		collection.insertOne(new Document().append("_id", 1));
		long endTime = System.currentTimeMillis();

		long diff = endTime - startTime;

		System.out
				.println("Total time (in milliseconds) for Primary and both secondary : "
						+ (diff ));

	}
	
	@Test
	public void testfindOnShard0() {
		long startTime = System.currentTimeMillis();
		collection.find(new Document().append("user_id", 3555));
		long endTime = System.currentTimeMillis();
		long diff = endTime - startTime;

		System.out.println("Total time (in milli seconds) for find on s0 : "
				+ (diff ));
	}
	
	@Test
	public void testfindOnShard1() {
		long startTime = System.currentTimeMillis();
		collection.find(new Document().append("user_id", 1));
		long endTime = System.currentTimeMillis();
		long diff = endTime - startTime;

		System.out.println("Total time (in milli seconds) for find on s1: "
				+ (diff ));
	}
	
	@Test
	public void testfindOnShard2() {
		long startTime = System.currentTimeMillis();
		collection.find(new Document().append("user_id", 15));
		long endTime = System.currentTimeMillis();
		long diff = endTime - startTime;

		System.out.println("Total time (in milli seconds) for find on s2 : "
				+ (diff ));
	}

}

package com.replication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.BSON;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

public class MongoDbSharding {

	/*
	 * public static void main(String[] args) throws InterruptedException {
	 * 
	 * MongoClient client = new MongoClient("localhost", 27017); MongoDatabase
	 * db = client.getDatabase("school"); MongoCollection<Document> students =
	 * db.getCollection("students");
	 * 
	 * List<String> types = new ArrayList<String>(Arrays.asList("exam", "quiz",
	 * "homework")); // 10000 students for (int i = 0; i < 10000; i++) { // take
	 * for (int class_counter = 0; class_counter < 10; class_counter++) {
	 * List<Document> scores = new ArrayList<>(); // and each class has 4 grades
	 * for (int j = 0; j < 3; j++) { scores.add(new Document("type",
	 * types.get(j)).append( "score", Math.random() * 100)); }
	 * 
	 * // there are 200 different classes that they can take int class_id =
	 * (int) Math.floor(Math.random() * 501); // get a // // // class // // id
	 * // // // between // 0 // and // 200 Document record = new
	 * Document("student_id", i).append( "scores", scores).append("class_id",
	 * class_id); students.insertOne(record);
	 * 
	 * } } }
	 */
	public static void main(String[] args) throws InterruptedException {
		MongoClient client = new MongoClient("localhost", 27017);
		String database = "demo";
		String collectionName = "users";

		// db.drop();

		// MongoDatabase adminDb = client.getDatabase("admin");
		// final BasicDBObject cmd = new BasicDBObject("enableSharding",
		// database);
		// cmd.put("shardCollection", database + "." + collectionName);
		// cmd.put("key", new Document().append("zipCode", "hashed"));
		// adminDb.runCommand(cmd);

		// get application database
		MongoDatabase db = client.getDatabase(database);
		MongoCollection<Document> users = db.getCollection(collectionName);
		List<Integer> zipCodes = new ArrayList<Integer>();

		/*
		 * zipCodes.addAll(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h",
		 * "i", "j"));
		 */

		for (int i = 0; i < 1000; i++) {
			zipCodes.add(i);
		}

		// 1000 user
		for (int i = 1; i < 10000; i++) {
			Document record = new Document("user_id", i).append("name",
					"TestName_" + i).append("zipCode", zipCodes.get(i % 1000));
			users.insertOne(record);

		}
	}
}

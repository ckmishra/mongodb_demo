package app.service;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Counter;

@Service
public class SequenceGenerator {

	@Autowired
	Jongo jongo;

	private MongoCollection counters;

	// workaround to achieve auto increment
	public int getNextSequenceValue() {
		counters = jongo.getCollection("counters");

		// counters.insert("{_id:transactionid,sequenceValue:0}");

		Counter counter = counters.findAndModify("{_id:#}", "transactionid")
				.with("{$inc:{sequenceValue:1}}").upsert().as(Counter.class);

		// System.out.println("Entity Id : " + entity.getSequenceValue());
		return counter.getSequenceValue();
	}
}

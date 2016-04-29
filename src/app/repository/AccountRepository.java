package app.repository;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import app.domain.Account;

@Repository
public class AccountRepository {

	private MongoCollection accounts;

	@Autowired
	public AccountRepository(Jongo jongo) {
		this.accounts = jongo.getCollection("accounts");
	}

	public void insert(String id, int balance, Object[] pendingTransactions) {
		accounts.insert("{_id: #, balance: #, pendingTransactions: #}", id,
				balance, pendingTransactions);
	}

	public boolean find(String id) {
		if (accounts.findOne("{_id: #}", id).as(Account.class) != null)
			return true;
		else
			return false;

	}

	public void updateBalanceAndPushToPendingTransactions(String accountId,
			int amount, String transactionId) {
		accounts.update("{ _id: #, pendingTransactions: { $ne: #}},",
				accountId, transactionId).with(
				"{ $inc: { balance: #}, $push: { pendingTransactions: #}}",
				amount, transactionId);
	}

	public void updateBalanceAndPullFromPendingTransactions(String accountId,
			int amount, String transactionId) {
		accounts.update("{ _id: #, pendingTransactions: #},", accountId,
				transactionId).with(
				"{ $inc: { balance: #}, $pull: { pendingTransactions: #}}",
				amount, transactionId);
	}

	public void updatePullFromPendingTransactions(String accountId,
			String transactionId) {
		accounts.update("{ _id: #, pendingTransactions: #},", accountId,
				transactionId).with("{ $pull: { pendingTransactions: #}}",
				transactionId);
	}
}

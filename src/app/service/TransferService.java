package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Transaction;
import app.domain.TransactionState;

@Service
@SuppressWarnings("CPD-START")
public class TransferService {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private AccountService accountService;

	public void transfer(Transaction transaction) {

		transactionService.updateState(transaction.getId(),
				TransactionState.INITIAL, TransactionState.PENDING);

		boolean sourceExist = accountService.find(transaction.getSource());
		boolean destinationExist = accountService.find(transaction
				.getDestination());
		if (sourceExist && destinationExist) {

			accountService.updateBalanceAndPushToPendingTransactions(
					transaction.getSource(), -transaction.getValue(),
					transaction.getId());
			accountService.updateBalanceAndPushToPendingTransactions(
					transaction.getDestination(), transaction.getValue(),
					transaction.getId());

			transactionService.updateState(transaction.getId(),
					TransactionState.PENDING, TransactionState.APPLIED);

			accountService.updatePullFromPendingTransactions(
					transaction.getSource(), transaction.getId());
			accountService.updatePullFromPendingTransactions(
					transaction.getDestination(), transaction.getId());

			transactionService.updateState(transaction.getId(),
					TransactionState.APPLIED, TransactionState.DONE);
		}
	}

	public void recoverPending(Transaction transaction) {

		boolean sourceExist = accountService.find(transaction.getSource());
		boolean destinationExist = accountService.find(transaction
				.getDestination());
		if (sourceExist && destinationExist) {

			accountService.updateBalanceAndPushToPendingTransactions(
					transaction.getSource(), -transaction.getValue(),
					transaction.getId());
			accountService.updateBalanceAndPushToPendingTransactions(
					transaction.getDestination(), transaction.getValue(),
					transaction.getId());

			transactionService.updateState(transaction.getId(),
					TransactionState.PENDING, TransactionState.APPLIED);

			accountService.updatePullFromPendingTransactions(
					transaction.getSource(), transaction.getId());
			accountService.updatePullFromPendingTransactions(
					transaction.getDestination(), transaction.getId());

			transactionService.updateState(transaction.getId(),
					TransactionState.APPLIED, TransactionState.DONE);
		}
	}

	public void recoverApplied(Transaction transaction) {

		boolean sourceExist = accountService.find(transaction.getSource());
		boolean destinationExist = accountService.find(transaction
				.getDestination());
		if (sourceExist && destinationExist) {
			accountService.updatePullFromPendingTransactions(
					transaction.getSource(), transaction.getId());
			accountService.updatePullFromPendingTransactions(
					transaction.getDestination(), transaction.getId());

			transactionService.updateState(transaction.getId(),
					TransactionState.APPLIED, TransactionState.DONE);
		}
	}

	public void cancelPending(Transaction transaction) {

		transactionService.updateState(transaction.getId(),
				TransactionState.PENDING, TransactionState.CANCELING);

		accountService.updateBalanceAndPullFromPendingTransactions(
				transaction.getSource(), transaction.getValue(),
				transaction.getId());
		accountService.updateBalanceAndPullFromPendingTransactions(
				transaction.getDestination(), -transaction.getValue(),
				transaction.getId());

		transactionService.updateState(transaction.getId(),
				TransactionState.CANCELING, TransactionState.CANCELED);
	}

	@Autowired
	SequenceGenerator sequenceGenerator;

	public Transaction undoApplied(Transaction transaction) {

		// complete this and create new transaction by reversing
		recoverApplied(transaction);

		Transaction reverseTransaction = new Transaction();
		reverseTransaction.setId(String.valueOf(sequenceGenerator
				.getNextSequenceValue()));
		reverseTransaction.setSource(transaction.getDestination());
		reverseTransaction.setDestination(transaction.getSource());
		reverseTransaction.setValue(transaction.getValue());
		reverseTransaction.setState(TransactionState.INITIAL);

		transactionService.insert(reverseTransaction.getId(),
				reverseTransaction.getSource(),
				reverseTransaction.getDestination(),
				reverseTransaction.getValue(), reverseTransaction.getState());

		transfer(reverseTransaction);

		return reverseTransaction;
	}

	public void failScenario(Transaction transaction, TransactionState state) {

		transactionService.updateState(transaction.getId(),
				TransactionState.INITIAL, TransactionState.PENDING);
		if (state == TransactionState.PENDING)
			return;
		accountService.updateBalanceAndPushToPendingTransactions(
				transaction.getSource(), -transaction.getValue(),
				transaction.getId());
		accountService.updateBalanceAndPushToPendingTransactions(
				transaction.getDestination(), transaction.getValue(),
				transaction.getId());

		transactionService.updateState(transaction.getId(),
				TransactionState.PENDING, TransactionState.APPLIED);

		if (state == TransactionState.APPLIED)
			return;

		accountService.updatePullFromPendingTransactions(
				transaction.getSource(), transaction.getId());
		accountService.updatePullFromPendingTransactions(
				transaction.getDestination(), transaction.getId());

		transactionService.updateState(transaction.getId(),
				TransactionState.APPLIED, TransactionState.DONE);

	}

}

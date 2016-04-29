package app.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.domain.Account;
import app.domain.Transaction;
import app.service.AccountService;
import app.service.SequenceGenerator;
import app.service.TransactionService;
import app.service.TransferService;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private Jongo jongo;

	@Autowired
	AccountService accountService;

	@Autowired
	TransferService transferService;

	@Autowired
	TransactionService transactionService;

	@Autowired
	SequenceGenerator sequenceGenerator;

	MongoCollection accounts;
	MongoCollection transactions;

	@PostConstruct
	public void init() {
		accounts = jongo.getCollection("accounts");
		transactions = jongo.getCollection("transactions");

	}

	@RequestMapping("/addAccount")
	public ModelAndView addAccount(HttpServletRequest request) {
		String username = request.getParameter("username");
		Integer amount = Integer.valueOf(request.getParameter("amount"));

		accountService.insert(username, amount, new Object[0]);

		Account retrievedAccount = accounts.findOne("{_id:#}", username).as(
				Account.class);

		assertThat(retrievedAccount.getBalance(), is(amount));
		assertThat(retrievedAccount.getPendingTransactions(), is(new Object[0]));

		return new ModelAndView("redirect:/", "success", "Account created");
	}

	// @RequestMapping("/transfer")
	public ModelAndView transfer(HttpServletRequest request) {
		String from_account = request.getParameter("from");
		String to_account = request.getParameter("to");
		Integer amount = Integer.valueOf(request.getParameter("amount"));

		Transaction transaction = new Transaction();
		transaction.setId(String.valueOf(sequenceGenerator
				.getNextSequenceValue()));
		transaction.setSource(from_account);
		transaction.setDestination(to_account);
		transaction.setValue(amount);

		transferService.transfer(transaction);

		return new ModelAndView("redirect:/", "success", "Amount Transferd");

	}

}
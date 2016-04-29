package app.controller;

import javax.annotation.PostConstruct;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.domain.Transaction;
import app.domain.TransactionState;
import app.service.SequenceGenerator;
import app.service.TransactionService;
import app.service.TransferService;

@Controller
@RequestMapping("/transfer")
public class TransferController {

	@Autowired
	TransferService transferService;

	@Autowired
	TransactionService transactionService;

	@Autowired
	private Jongo jongo;

	@Autowired
	private SequenceGenerator sequenceGenerator;

	MongoCollection accounts;
	MongoCollection transactions;

	@PostConstruct
	public void init() {
		accounts = jongo.getCollection("accounts");
		transactions = jongo.getCollection("transactions");
	}

	@RequestMapping(value = "/start", method = RequestMethod.GET)
	public ModelAndView transfer() {
		return new ModelAndView("transfer", "command", new Transaction());
	}

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public String transfer(
			@ModelAttribute("mongoDemo") Transaction transaction,
			ModelMap model, RedirectAttributes redirectAttrs) {
		transaction.setId(String.valueOf(sequenceGenerator
				.getNextSequenceValue()));
		if (transaction.getState() == null) {
			transaction.setState(TransactionState.INITIAL);
		}
		transactionService.insert(transaction.getId(), transaction.getSource(),
				transaction.getDestination(), transaction.getValue(),
				transaction.getState());

		transferService.transfer(transaction);

		model.addAttribute("Id", transaction.getId());
		model.addAttribute("Source", transaction.getSource());
		model.addAttribute("Destination", transaction.getDestination());
		model.addAttribute("Value", transaction.getValue());
		model.addAttribute("State", transaction.getState());

		return "result";
	}

	@RequestMapping(value = "/fail", method = RequestMethod.GET)
	public ModelAndView failScenario() {
		return new ModelAndView("fail", "command", new Transaction());
	}

	@RequestMapping(value = "/fail", method = RequestMethod.POST)
	public String failScenario(
			@ModelAttribute("mongoDemo") Transaction transaction,
			ModelMap model, RedirectAttributes redirectAttrs) {
		transaction.setId(String.valueOf(sequenceGenerator
				.getNextSequenceValue()));
		if (transaction.getState() == null) {
			transaction.setState(TransactionState.INITIAL);
		}
		transactionService.insert(transaction.getId(), transaction.getSource(),
				transaction.getDestination(), transaction.getValue(),
				transaction.getState());

		transferService.failScenario(transaction, transaction.getState());
		model.addAttribute("Id", transaction.getId());
		model.addAttribute("Source", transaction.getSource());
		model.addAttribute("Destination", transaction.getDestination());
		model.addAttribute("Value", transaction.getValue());
		model.addAttribute("State", transaction.getState());

		return "result";
	}

	@RequestMapping(value = "/recoverPending", method = RequestMethod.POST)
	public String recoverPending(
			@ModelAttribute("mongoDemo") Transaction transaction,
			ModelMap model, RedirectAttributes redirectAttrs) {
		transaction = transactionService
				.findTransactionByStateAndLastModified(TransactionState.PENDING);
		if (transaction != null) {
			transferService.recoverPending(transaction);
			model.addAttribute("Id", transaction.getId());
			model.addAttribute("Source", transaction.getSource());
			model.addAttribute("Destination", transaction.getDestination());
			model.addAttribute("Value", transaction.getValue());
			model.addAttribute("State", transaction.getState());

			return "result";
		} else {
			redirectAttrs.addFlashAttribute("status", "Recover PENDING");
			return "redirect:/";
		}

	}

	@RequestMapping(value = "/recoverApplied", method = RequestMethod.POST)
	public String recoverApplied(ModelMap model,
			RedirectAttributes redirectAttrs) {
		Transaction transaction = transactionService
				.findTransactionByStateAndLastModified(TransactionState.APPLIED);
		if (transaction != null) {
			transferService.recoverApplied(transaction);
			model.addAttribute("Id", transaction.getId());
			model.addAttribute("Source", transaction.getSource());
			model.addAttribute("Destination", transaction.getDestination());
			model.addAttribute("Value", transaction.getValue());
			model.addAttribute("State", transaction.getState());

			return "result";
		} else {
			redirectAttrs.addFlashAttribute("status", "Recover Applied");
			return "redirect:/";
		}

	}

	@RequestMapping(value = "/cancelPending", method = RequestMethod.POST)
	public String cancelPending(ModelMap model, RedirectAttributes redirectAttrs) {
		Transaction transaction = transactionService
				.findTransactionByStateAndLastModified(TransactionState.PENDING);
		if (transaction != null) {
			transferService.cancelPending(transaction);
			model.addAttribute("Id", transaction.getId());
			model.addAttribute("Source", transaction.getSource());
			model.addAttribute("Destination", transaction.getDestination());
			model.addAttribute("Value", transaction.getValue());
			model.addAttribute("State", transaction.getState());

			return "result";
		} else {
			redirectAttrs.addFlashAttribute("status",
					"Cancel Pending Transaction");
			return "redirect:/";
		}

	}

	@RequestMapping(value = "/rollbackApplied", method = RequestMethod.POST)
	public String rollBackApplied(ModelMap model,
			RedirectAttributes redirectAttrs) {
		Transaction transaction = transactionService
				.findTransactionByStateAndLastModified(TransactionState.APPLIED);
		if (transaction != null) {

			Transaction reverseTransaction = transferService
					.undoApplied(transaction);

			model.addAttribute("Id", reverseTransaction.getId());
			model.addAttribute("Source", reverseTransaction.getSource());
			model.addAttribute("Destination",
					reverseTransaction.getDestination());
			model.addAttribute("Value", reverseTransaction.getValue());
			model.addAttribute("State", reverseTransaction.getState());

			return "result";
		} else {
			redirectAttrs.addFlashAttribute("status", "Rollback Applied");
			return "redirect:/";
		}

	}

}

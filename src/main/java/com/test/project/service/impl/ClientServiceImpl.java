package com.test.project.service.impl;

import com.test.project.dto.ClientDto;
import com.test.project.entity.Account;
import com.test.project.entity.Client;
import com.test.project.entity.Mail;
import com.test.project.entity.Phone;
import com.test.project.exception.ClientException;
import com.test.project.exception.ExceptionMessageCreator;
import com.test.project.repository.AccountRepository;
import com.test.project.repository.ClientRepository;
import com.test.project.repository.MailRepository;
import com.test.project.repository.PhoneRepository;
import com.test.project.service.ClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.test.project.util.Constants.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;
	private final PhoneRepository phoneRepository;
	private final MailRepository mailRepository;
	private final AccountRepository accountRepository;
	private final ExceptionMessageCreator messageCreator;
	private final ModelMapper modelMapper;

	@Override
	public Page<Client> getClientsByName(int page, int size, String name) {
		Pageable pageable = PageRequest.of(page, size);
		return clientRepository.findByNameContainingIgnoreCaseOrderByName(pageable, name);
	};

	@Override
	public Page<Client>  getClientsByBirthdate(int page, int size, LocalDate birthdate) {
		Pageable pageable = PageRequest.of(page, size);
		return clientRepository.findByBirthdateAfterOrderByBirthdate(pageable, birthdate);
	};

	@Cacheable(value = "clientByPhoneCache", key = "#phone"/*,condition = "#phone.equals(...)"*/)
	@Override
	public Client  getClientByPhone(String phone){
		Phone p = phoneRepository.getFirstByPhone(phone).orElseThrow(() -> ClientException.of(messageCreator.createMessage(PHONE_NOT_FOUND)));
		return p.getClient();
	};

	@Override
	public Client getClientByMail(String mail) {
		Mail m = mailRepository.getFirstByMail(mail).orElseThrow(() -> ClientException.of(messageCreator.createMessage(MAIL_NOT_FOUND)));
		return m.getClient();
	}

	@Transactional
	@Override
	public Client registerClient(ClientDto clientDto) {
		Client client = modelMapper.map(clientDto, Client.class);
		return clientRepository.saveAndFlush(client);
	}

	@Transactional
	@Override
	public boolean moneyTransfer(long senderId, long recipientId, float amount){

		Account sender;
		Account recipient;

		if(Float.isInfinite(amount))
			throw ClientException.of(messageCreator.createMessage(AMOUNT_IS_INFINITE));

		if(Float.isNaN(amount))
			throw ClientException.of(messageCreator.createMessage(AMOUNT_IS_NAN));

		BigDecimal amountAsBigDecimal = BigDecimal.valueOf(amount);

		List<Account> senderAccounts = accountRepository.getAccountByClientId(senderId);
		List<Account> recipientAccounts = accountRepository.getAccountByClientId(recipientId);

		if (senderAccounts.isEmpty()) {
			throw ClientException.of(messageCreator.createMessage(SENDER_ID_ACCOUNT_NOT_FOUND));
		} else {
			sender = senderAccounts.get(0);
		}

		if (recipientAccounts.isEmpty()) {
			throw ClientException.of(messageCreator.createMessage(RECIPIENT_ID_ACCOUNT_NOT_FOUND));
		} else {
			recipient = recipientAccounts.get(0);
		}

		if (sender.getBalance().compareTo(amountAsBigDecimal) >= 0 ) {
			sender.getAccountLock().writeLock().lock();
			recipient.getAccountLock().writeLock().lock();
			sender.setBalance(sender.getBalance().subtract(amountAsBigDecimal));
			sender.getAccountLock().writeLock().unlock();
			recipient.setBalance(recipient.getBalance().add(amountAsBigDecimal));
			recipient.getAccountLock().writeLock().unlock();
			return true;
		} else {
			throw ClientException.of(messageCreator.createMessage(NOT_ENOUGH_FUNDS));
		}
	};
}
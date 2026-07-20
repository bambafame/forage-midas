package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {

  private final UserRepository userRepository;
  private final TransactionRepository transactionRepository;
  private final RestTemplate restTemplate;

  public TransactionService(UserRepository userRepository,
      TransactionRepository transactionRepository, RestTemplate restTemplate) {
    this.userRepository = userRepository;
    this.transactionRepository = transactionRepository;
    this.restTemplate = restTemplate;
  }


  @Transactional
  public void process(Transaction tx) {
    UserRecord sender = userRepository.findById(tx.getSenderId());
    UserRecord recipient = userRepository.findById(tx.getRecipientId());
    if (sender == null || recipient == null) return;
    if (sender.getBalance() < tx.getAmount()) return;

    Incentive incentive = restTemplate
        .postForEntity("http://localhost:8080/incentive", tx, Incentive.class)
        .getBody();

    float incentiveAmount = incentive != null ? incentive.getAmount() : 0f;


    sender.setBalance(sender.getBalance() - tx.getAmount());
    recipient.setBalance(recipient.getBalance() + tx.getAmount() + incentiveAmount);
    userRepository.save(sender);
    userRepository.save(recipient);
    transactionRepository.save(new TransactionRecord(tx.getAmount(), sender, recipient, incentiveAmount));
  }

  public Balance getUserBalance(long userId) {
    UserRecord user = userRepository.findById(userId);
    float amount = user != null ? user.getBalance() : 0f;
    return new Balance(amount);
  }
}

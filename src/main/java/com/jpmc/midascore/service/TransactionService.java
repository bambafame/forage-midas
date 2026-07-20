package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

  private final UserRepository userRepository;
  private final TransactionRepository transactionRepository;

  public TransactionService(UserRepository userRepository,
      TransactionRepository transactionRepository) {
    this.userRepository = userRepository;
    this.transactionRepository = transactionRepository;
  }


  @Transactional
  public void process(Transaction tx) {
    UserRecord sender = userRepository.findById(tx.getSenderId());
    UserRecord recipient = userRepository.findById(tx.getRecipientId());
    if (sender == null || recipient == null) return;
    if (sender.getBalance() < tx.getAmount()) return;

    sender.setBalance(sender.getBalance() - tx.getAmount());
    recipient.setBalance(recipient.getBalance() + tx.getAmount());
    userRepository.save(sender);
    userRepository.save(recipient);
    transactionRepository.save(new TransactionRecord(tx.getAmount(), sender, recipient));
  }
}

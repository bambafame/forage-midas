package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

  private final TransactionService transactionService;

  public UserController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @GetMapping("balance")
  public ResponseEntity<Balance> getBalance(@RequestParam long userId) {
    return ResponseEntity.ok(transactionService.getUserBalance(userId));
  }
}

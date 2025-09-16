package com.example.account.service;
import com.example.account.entity.Account;
import com.example.account.entity.Transfer;
import com.example.account.repo.AccountRepository;
import com.example.account.repo.TransferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountServiceImpl {
  private final AccountRepository accountRepo;
  private final TransferRepository transferRepo;
  private final KafkaTemplate<String,String> kafka;
  private final ObjectMapper om = new ObjectMapper();

  public AccountServiceImpl(AccountRepository accountRepo, TransferRepository transferRepo, KafkaTemplate<String,String> kafka){
    this.accountRepo = accountRepo; this.transferRepo = transferRepo; this.kafka = kafka;
  }

  @Transactional
  public Transfer doTransfer(Long fromId, Long toId, BigDecimal amount, String idempotencyKey){
    if (idempotencyKey != null) {
      Optional<Transfer> ex = transferRepo.findByTransferId(idempotencyKey);
      if (ex.isPresent()) return ex.get();
    }
    Account from = accountRepo.findById(fromId).orElseThrow();
    Account to = accountRepo.findById(toId).orElseThrow();
    if (from.getBalance().compareTo(amount) < 0) throw new RuntimeException("insufficient funds");
    from.setBalance(from.getBalance().subtract(amount));
    to.setBalance(to.getBalance().add(amount));
    accountRepo.save(from); accountRepo.save(to);
    Transfer t = new Transfer();
    t.setTransferId(idempotencyKey == null ? java.util.UUID.randomUUID().toString() : idempotencyKey);
    t.setFromAccountId(fromId); t.setToAccountId(toId); t.setAmount(amount); t.setStatus("COMPLETED");
    transferRepo.save(t);
    try {
      kafka.send("transfer.completed", om.writeValueAsString(Map.of("transferId", t.getTransferId(), "from", fromId, "to", toId, "amount", amount)));
    } catch(Exception e){}
    return t;
  }

  @Transactional
  public Account createAccountForUser(Long userId){
    var a = new Account(); a.setUserId(userId); a.setBalance(BigDecimal.ZERO);
    return accountRepo.save(a);
  }
}

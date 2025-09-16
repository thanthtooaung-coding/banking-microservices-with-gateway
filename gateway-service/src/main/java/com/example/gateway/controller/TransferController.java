package com.example.gateway.controller;
import com.example.proto.account.AccountProto;
import com.example.proto.account.AccountServiceGrpc;
import com.example.gateway.util.JwtUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TransferController {
  private final ManagedChannel accountChannel = ManagedChannelBuilder.forAddress("localhost",9091).usePlaintext().build();
  private final AccountServiceGrpc.AccountServiceBlockingStub accountStub = AccountServiceGrpc.newBlockingStub(accountChannel);
  private final JwtUtil jwt = new JwtUtil("my-very-secret-change-this", 3600000);

  @PostMapping("/transfer")
  public ResponseEntity<?> transfer(@RequestHeader("Authorization") String auth, @RequestBody Map body){
    String token = auth.replace("Bearer ","");
    try { jwt.verify(token); } catch(Exception e){ return ResponseEntity.status(401).build(); }
    long from = Long.parseLong(String.valueOf(body.get("fromAccountId")));
    long to = Long.parseLong(String.valueOf(body.get("toAccountId")));
    double amount = Double.parseDouble(String.valueOf(body.get("amount")));
    String idemp = (String) body.getOrDefault("idempotencyKey", java.util.UUID.randomUUID().toString());
    var req = AccountProto.TransferRequest.newBuilder().setFromAccountId(from).setToAccountId(to).setAmount(amount).setIdempotencyKey(idemp).build();
    var resp = accountStub.transfer(req);
    if (resp.getSuccess()) return ResponseEntity.ok(Map.of("transferId", resp.getTransferId()));
    return ResponseEntity.status(400).body(Map.of("error", resp.getMessage()));
  }
}

package com.example.account.grpc;
import com.example.proto.account.AccountProto;
import com.example.proto.account.AccountServiceGrpc;
import com.example.account.service.AccountServiceImpl;
import io.grpc.stub.StreamObserver;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import net.devh.boot.grpc.server.service.GrpcService;
import com.fasterxml.jackson.databind.ObjectMapper;

@GrpcService
public class AccountGrpcService extends AccountServiceGrpc.AccountServiceImplBase {
  private final AccountServiceImpl svc;
  private final ObjectMapper om = new ObjectMapper();
  public AccountGrpcService(AccountServiceImpl svc){this.svc=svc;}
  @Override
  public void createAccountForUser(AccountProto.CreateAccountRequest req, StreamObserver<AccountProto.CreateAccountResponse> obs){
    var acc = svc.createAccountForUser(req.getUserId());
    var r = AccountProto.CreateAccountResponse.newBuilder().setAccountId(acc.getId()).setSuccess(true).build();
    obs.onNext(r); obs.onCompleted();
  }
  @Override
  public void transfer(AccountProto.TransferRequest req, StreamObserver<AccountProto.TransferResponse> obs){
    try {
      var t = svc.doTransfer(req.getFromAccountId(), req.getToAccountId(), java.math.BigDecimal.valueOf(req.getAmount()), req.getIdempotencyKey());
      obs.onNext(AccountProto.TransferResponse.newBuilder().setSuccess(true).setTransferId(t.getTransferId()).build());
    } catch(Exception e) {
      obs.onNext(AccountProto.TransferResponse.newBuilder().setSuccess(false).setMessage(e.getMessage()).build());
    }
    obs.onCompleted();
  }

  @KafkaListener(topics = "user.created", groupId = "account-service-group")
  public void onUserCreated(String payload){
    try {
      var m = om.readValue(payload, java.util.Map.class);
      Long userId = Long.valueOf(String.valueOf(m.get("id")));
      svc.createAccountForUser(userId);
    } catch(Exception e){}
  }
}

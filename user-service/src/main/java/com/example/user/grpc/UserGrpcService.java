package com.example.user.grpc;
import com.example.proto.user.UserProto;
import com.example.proto.user.UserServiceGrpc;
import com.example.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.stub.StreamObserver;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

  private final UserService svc;
  private final KafkaTemplate<String,String> kafka;
  private final ObjectMapper om = new ObjectMapper();

  public UserGrpcService(UserService svc, KafkaTemplate<String,String> kafka){
    this.svc = svc; this.kafka = kafka;
  }

  @Override
  public void createUser(UserProto.CreateUserRequest req, StreamObserver<UserProto.CreateUserResponse> obs){
    var user = svc.createUser(req.getUsername(), req.getEmail(), req.getPassword());
    try {
      var payload = om.writeValueAsString(java.util.Map.of("id", user.getId(), "email", user.getEmail()));
      kafka.send("user.created", payload);
    } catch (Exception e){}
    var resp = UserProto.CreateUserResponse.newBuilder()
      .setId(user.getId()).setUsername(user.getUsername()).setEmail(user.getEmail()).setSuccess(true).build();
    obs.onNext(resp); obs.onCompleted();
  }

  @Override
  public void authenticate(UserProto.AuthRequest req, StreamObserver<UserProto.AuthResponse> obs){
    var opt = svc.authenticate(req.getUsername(), req.getPassword());
    if (opt.isPresent()) {
      var u = opt.get();
      var r = UserProto.AuthResponse.newBuilder().setId(u.getId()).setUsername(u.getUsername()).setEmail(u.getEmail()).setSuccess(true).build();
      obs.onNext(r);
    } else {
      obs.onNext(UserProto.AuthResponse.newBuilder().setSuccess(false).setMessage("invalid").build());
    }
    obs.onCompleted();
  }

  @Override
  public void getUserById(UserProto.GetUserRequest req, StreamObserver<UserProto.GetUserResponse> obs){
    svc.findById(req.getId()).ifPresentOrElse(u->{
      obs.onNext(UserProto.GetUserResponse.newBuilder().setId(u.getId()).setUsername(u.getUsername()).setEmail(u.getEmail()).build());
      obs.onCompleted();
    }, () -> {
      obs.onError(new RuntimeException("not found"));
    });
  }
}

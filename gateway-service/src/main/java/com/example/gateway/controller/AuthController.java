package com.example.gateway.controller;
import com.example.proto.user.UserProto;
import com.example.proto.user.UserServiceGrpc;
import com.example.gateway.util.JwtUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final ManagedChannel userChannel = ManagedChannelBuilder.forAddress("infra-user-service",9090).usePlaintext().build();
  private final UserServiceGrpc.UserServiceBlockingStub userStub = UserServiceGrpc.newBlockingStub(userChannel);
  private final JwtUtil jwt = new JwtUtil("my-very-secret-change-this", 3600000);

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Map<String,String> body){
    var req = UserProto.CreateUserRequest.newBuilder().setUsername(body.get("username")).setEmail(body.get("email")).setPassword(body.get("password")).build();
    var r = userStub.createUser(req);
    return ResponseEntity.ok(Map.of("id", r.getId(), "username", r.getUsername()));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String,String> body){
    var req = UserProto.AuthRequest.newBuilder().setUsername(body.get("username")).setPassword(body.get("password")).build();
    var r = userStub.authenticate(req);
    if (!r.getSuccess()) return ResponseEntity.status(401).body(Map.of("error","invalid"));
    String token = jwt.generateToken(Map.of("userId", r.getId(), "username", r.getUsername()));
    return ResponseEntity.ok(Map.of("token", token));
  }
}

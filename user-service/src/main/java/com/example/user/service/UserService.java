package com.example.user.service;
import com.example.user.entity.User;
import com.example.user.repo.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {
  private final UserRepository repo;
  private final BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

  public UserService(UserRepository repo){this.repo=repo;}

  @Transactional
  public User createUser(String username,String email,String password){
    User u=new User();
    u.setUsername(username);u.setEmail(email);
    u.setPasswordHash(enc.encode(password));
    return repo.save(u);
  }

  public Optional<User> authenticate(String username,String password){
    return repo.findByUsername(username).filter(u->enc.matches(password,u.getPasswordHash()));
  }

  public Optional<User> findById(Long id){return repo.findById(id);}
}

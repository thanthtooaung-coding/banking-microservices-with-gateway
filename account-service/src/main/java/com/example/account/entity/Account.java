package com.example.account.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
@Table(name="accounts")
public class Account {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name="user_id")
  private Long userId;
  private BigDecimal balance = BigDecimal.ZERO;
  public Long getId(){return id;}
  public void setId(Long id){this.id=id;}
  public Long getUserId(){return userId;}
  public void setUserId(Long userId){this.userId=userId;}
  public BigDecimal getBalance(){return balance;}
  public void setBalance(BigDecimal balance){this.balance=balance;}
}

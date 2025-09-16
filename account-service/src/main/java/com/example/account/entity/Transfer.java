package com.example.account.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
@Entity
@Table(name="transfers")
public class Transfer {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name="transfer_id", unique=false)
  private String transferId;
  private Long fromAccountId;
  private Long toAccountId;
  private BigDecimal amount;
  private String status;
  private Instant createdAt = Instant.now();
  public String getTransferId(){return transferId;}
  public void setTransferId(String transferId){this.transferId=transferId;}
  public Long getFromAccountId(){return fromAccountId;}
  public void setFromAccountId(Long fromAccountId){this.fromAccountId=fromAccountId;}
  public Long getToAccountId(){return toAccountId;}
  public void setToAccountId(Long toAccountId){this.toAccountId=toAccountId;}
  public BigDecimal getAmount(){return amount;}
  public void setAmount(BigDecimal amount){this.amount=amount;}
  public String getStatus(){return status;}
  public void setStatus(String status){this.status=status;}
}

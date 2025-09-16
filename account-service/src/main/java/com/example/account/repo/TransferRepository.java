package com.example.account.repo;
import com.example.account.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface TransferRepository extends JpaRepository<Transfer, Long> {
  Optional<Transfer> findByTransferId(String transferId);
}

package market.henry.auth.repo;

import market.henry.auth.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
@Transactional
public interface AccountRepo extends JpaRepository<Account,Long> {
    @Query("select a from Account a where a.firstName =?1 and a.surname =?2 and a.dob =?3")
    Account findbyFirstName(String firstName, String lastName, LocalDate dob);
}

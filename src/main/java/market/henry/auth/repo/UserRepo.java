package market.henry.auth.repo;

import market.henry.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
@Repository
public interface UserRepo  extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id = ?1")
    User getBy(Long id);

    @Query("select u from User u where u.email= ?1")
    User getByEmail(String email);

    @Query("select u from User u where u.phoneNumber= ?1")
    User getByPhoneNumber(String phoneNumber);

    @Query("select u.accountBalance from User u where u.accountNumber= ?1")
    BigDecimal accountBalanceInquiry(String accountNumber);

    @Query("select new market.henry.auth.model.User(u.title,u.firstName,u.lastName,u.gender,u.bvn,u.phoneNumber,u.email,u.dob) from User u where u.bvn= ?1")
    User getByBvn(String bvn);

    @Query("select new market.henry.auth.model.User(u.title,u.firstName,u.lastName,u.gender,u.bvn,u.phoneNumber,u.email,u.dob) from User u where u.firstName= ?1 and u.lastName =?2")
    User checkAccountExist(String firstName, String lastName);

    @Query("select new market.henry.auth.model.User(u.title,u.firstName,u.lastName,u.gender,u.bvn,u.phoneNumber,u.email,u.dob,u.accountNumber) from User u where u.accountNumber= ?1")
    User accountInquiry(String accountNumber);

    @Query("select count (u.id) from User u")
    long getCount();
}

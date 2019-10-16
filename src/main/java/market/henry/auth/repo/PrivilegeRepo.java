package market.henry.auth.repo;

import market.henry.auth.enums.UserType;
import market.henry.auth.model.Privilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.SortedSet;


@Transactional
@Repository
public interface PrivilegeRepo extends JpaRepository<Privilege, Long> {

    @Query("select p from Privilege p where (?1) MEMBER OF p.userTypes or 'ALL' MEMBER OF p.userTypes order by p.name desc ")
    SortedSet<Privilege> getAllBy(UserType userType);
}


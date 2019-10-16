package market.henry.auth.repo;

import market.henry.auth.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Transactional
@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    @Query("select r from Role r where r.id = ?1 ")
    Role get(Long id);

    @Query("select r from Role r where (r.type = ?1 or r.type = 'ALL') order by r.name desc ")
    Set<Role> getAllBy(String type);

}

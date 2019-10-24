package market.henry.auth.repo;

import market.henry.auth.model.StartWorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface StarWorkRepo extends JpaRepository<StartWorkFlow,Long> {
    @Query("select new market.henry.auth.model.StartWorkFlow(s.applicationNumber,s.loanStatus) from StartWorkFlow s where s.applicationNumber in ?1")
    List<StartWorkFlow> getApprovedStar(List<String> applicationNumbers);
}

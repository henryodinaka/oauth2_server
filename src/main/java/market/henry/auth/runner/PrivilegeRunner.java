package market.henry.auth.runner;

import lombok.RequiredArgsConstructor;
import market.henry.auth.enums.Privileges;
import market.henry.auth.enums.UserType;
import market.henry.auth.model.Privilege;
import market.henry.auth.repo.PrivilegeRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class PrivilegeRunner implements ApplicationRunner {

    private final PrivilegeRepo privilegeRepo;

    @Override
    public void run(ApplicationArguments args){

        if(privilegeRepo.count() != Privileges.values().length) {
            List<Privilege> privileges = Arrays.asList(Privileges.values()).stream()
                    .map(it ->
                            new Privilege(it.name(), new HashSet<UserType>(Arrays.asList(it.getUserTypes())))
                    )
                    .collect(Collectors.toList());
                try {
                    privilegeRepo.saveAll(privileges);
                }catch (Exception e){
                    //e.printStackTrace();
                }
           /* privileges.parallelStream().forEach(it->{
                try {
                    privilegeRepo.save(it);
                }catch (Exception e){
                    //e.printStackTrace();
                }
            });*/
        }
    }
}

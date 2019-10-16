package market.henry.auth.runner;

import lombok.RequiredArgsConstructor;
import market.henry.auth.enums.UserType;
import market.henry.auth.model.Privilege;
import market.henry.auth.model.Role;
import market.henry.auth.repo.PrivilegeRepo;
import market.henry.auth.repo.RoleRepo;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class RoleRunner implements ApplicationRunner {

    private final RoleRepo roleRepo;
    private final PrivilegeRepo privilegeRepo;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        Set<Privilege> privileges = privilegeRepo.getAllBy(UserType.ADMIN);

        if(roleRepo.count() == 0) {
            try {
                Set<Privilege> uniquePrivileges  =  privilegeRepo.getAllBy(UserType.ADMIN);
                uniquePrivileges.addAll(privileges);

             if(privileges.isEmpty()){
                 System.out.println("No privileges found for Roles");
                 return;
             }
                Role nibssRole = new Role();
                nibssRole.setPrivileges(uniquePrivileges);
                nibssRole.setName("System Admin");
                nibssRole.setType(UserType.ADMIN.name());
                roleRepo.save(nibssRole);

            } catch (Exception e) {

                e.printStackTrace();
            }

            try {

                Set<Privilege> uniquePrivileges  =  privilegeRepo.getAllBy(UserType.USER);
                uniquePrivileges.addAll(privileges);

                Role nibssRole = new Role();
                nibssRole.setPrivileges(uniquePrivileges);
                nibssRole.setName("Normal User");
                nibssRole.setType(UserType.USER.name());
                roleRepo.save(nibssRole);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {

                Set<Privilege> uniquePrivileges  =  privilegeRepo.getAllBy(UserType.SERVICE);
                uniquePrivileges.addAll(privileges);
                Role nibssRole = new Role();
                nibssRole.setName("Service client");
                nibssRole.setPrivileges(uniquePrivileges);
                nibssRole.setType(UserType.SERVICE.name());
                roleRepo.save(nibssRole);
            } catch (Exception e) {
            }
        }
    }
}

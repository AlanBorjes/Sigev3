package mx.edu.utez.sigev.util;


import mx.edu.utez.sigev.entity.Roles;
import mx.edu.utez.sigev.entity.Users;
import mx.edu.utez.sigev.repository.IRolesRepository;
import mx.edu.utez.sigev.repository.IUserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Configuration
@Component
public class Runner implements CommandLineRunner {
    private static final Logger logger = LogManager.getLogger(Runner.class);

    @Autowired
    private IRolesRepository roleInterface;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        Date today = new Date();
        logger.info("STARTING RUNNER");
        if(roleInterface.count() == 0){
            logger.info("CREATING ROLES");
            roleInterface.save(new Roles(1L, "ROL_ADMINISTRADOR"));
            roleInterface.save(new Roles(2L, "ROL_ENLACE"));
            roleInterface.save(new Roles(3L, "ROL_MIEMBRO"));
            roleInterface.save(new Roles(4L, "ROL_PRESIDENTE"));
        }
        if(userRepository.count() == 0) {
            logger.info("CREATING ADMIN USER");
            Set<Roles> roles1 = new HashSet<>();
            roles1.add(new Roles(1L, "ROL_ADMINISTRADOR"));
            Set<Roles> roles2 = new HashSet<>();
            roles2.add(new Roles(2L, "ROL_ENLACE"));
            Set<Roles> roles3 = new HashSet<>();
            roles3.add(new Roles(3L, "ROL_MIEMBRO"));
            Set<Roles> roles4 = new HashSet<>();
            roles4.add(new Roles(4L, "ROL_PRESIDENTE"));
            System.out.println(roles1);
            Users personModel = new Users(1L, "Alan", "Valladares", "Borjes", "alan@gmail.com", "7771144520","$2a$10$sd1g2blVOAwoDa1yZm4ob./ht1r.cyd89L3JZVkMh6ABC/ih.XQJO", 1, "alan@gmail.com", new Date(), roles1);
            Users personModel2 = new Users(2L, "Luis", "Pozo", "Torres", "pozo@gmail.com", "7771144520","$2a$10$sd1g2blVOAwoDa1yZm4ob./ht1r.cyd89L3JZVkMh6ABC/ih.XQJO", 1, "pozo@gmail.com", new Date(),roles2);
            Users personModel3 = new Users(3L, "Max", "mendoza ", "luna ", "max@gmail.com", "7771144520","$2a$10$sd1g2blVOAwoDa1yZm4ob./ht1r.cyd89L3JZVkMh6ABC/ih.XQJO", 1, "max@gmail.com", new Date(), roles3);
            Users personModel4 = new Users(4L, "kemish", "Jimenez ", "Mahonri ", "Kemish@gmail.com", "7771144520","$2a$10$sd1g2blVOAwoDa1yZm4ob./ht1r.cyd89L3JZVkMh6ABC/ih.XQJO", 1, "Kemish@gmail.com", new Date(), roles4);

            userRepository.save(personModel);
            userRepository.save(personModel2);
            userRepository.save(personModel3);
            userRepository.save(personModel4);

        }
        logger.info("END RUNNER");


    }
}

package mx.edu.utez.sigev.util;


import mx.edu.utez.sigev.entity.*;
import mx.edu.utez.sigev.repository.*;
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
    private IStateRepository stateRepository;

    @Autowired
    private ICityRepository cityRepository;

    @Autowired
    private ISuburbRepository suburbRepository;

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

        if (stateRepository.count() == 0){
            logger.info("CREATING STATES");
            State aguascalientes = new State(1L, "Aguascalientes", 1);
            State bajaCalifornia = new State(2L, "Baja California", 1);
            State bajaCaliforniaSur = new State(3L, "Baja California Sur", 1);
            State campeche = new State(4L, "Campeche", 1);
            State chiapas = new State(5L, "Chiapas", 1);
            State chihuahua = new State(6L, "Chihuahua", 1);
            State coahuila = new State(7L, "Coahuila", 1);
            State colima = new State(8L, "Colima", 1);
            State cdmx = new State(9L, "Ciudad de México", 1);
            State durango = new State(10L, "Durango", 1);
            State guanajuato = new State(11L, "Guanajuato", 1);
            State guerrero = new State(12L, "Guerrero", 1);
            State hidalgo = new State(13L, "Hidalgo", 1);
            State jalisco = new State(14L, "Jalisco", 1);
            State mexico = new State(15L, "Estado de México", 1);
            State michoacan = new State(16L, "Michoacán", 1);
            State morelos = new State(17L, "Morelos", 1);
            State nayarit = new State(18L, "Nayarit", 1);
            State nuevoLeon = new State(19L, "Nuevo León", 1);
            State oaxaca = new State(20L, "Oaxaca", 1);
            State puebla = new State(21L, "Puebla", 1);
            State queretaro = new State(22L, "Querétaro", 1);
            State quintanaRoo = new State(23L, "Quintana Roo", 1);
            State sanLuisPotosi = new State(24L, "San Luis Potosí", 1);
            State sinaloa = new State(25L, "Sinaloa", 1);
            State sonora = new State(26L, "Sonora", 1);
            State tabasco = new State(27L, "Tabasco", 1);
            State tamaulipas = new State(28L, "Tamaulipas", 1);
            State tlaxcala = new State(29L, "Tlaxcala", 1);
            State veracruz = new State(30L, "Veracruz", 1);
            State yucatan = new State(31L, "Yucatán", 1);
            State zacatecas = new State(32L, "Zacatecas", 1);

            stateRepository.save(aguascalientes);
            stateRepository.save(bajaCalifornia);
            stateRepository.save(bajaCaliforniaSur);
            stateRepository.save(campeche);
            stateRepository.save(chiapas);
            stateRepository.save(chihuahua);
            stateRepository.save(coahuila);
            stateRepository.save(colima);
            stateRepository.save(cdmx);
            stateRepository.save(durango);
            stateRepository.save(guanajuato);
            stateRepository.save(guerrero);
            stateRepository.save(hidalgo);
            stateRepository.save(jalisco);
            stateRepository.save(mexico);
            stateRepository.save(michoacan);
            stateRepository.save(morelos);
            stateRepository.save(nayarit);
            stateRepository.save(nuevoLeon);
            stateRepository.save(oaxaca);
            stateRepository.save(puebla);
            stateRepository.save(queretaro);
            stateRepository.save(quintanaRoo);
            stateRepository.save(sanLuisPotosi);
            stateRepository.save(sinaloa);
            stateRepository.save(sonora);
            stateRepository.save(tabasco);
            stateRepository.save(tamaulipas);
            stateRepository.save(tlaxcala);
            stateRepository.save(veracruz);
            stateRepository.save(yucatan);
            stateRepository.save(zacatecas);
        }

        if (cityRepository.count() == 0){
            logger.info("CREATING CITIES");
            City amacuzac = new City(1L, "Amacuzac", 1,"1");
            City atlatlahucan = new City(2L, "Atlatlahucan", 1,"1");
            City axochiapan = new City(3L, "Axochiapan", 1,"1");
            City ayala = new City(4L, "Ayala", 1,"1");
            City coatetelco = new City(5L, "Coatetelco", 1,"1");
            City cuautla = new City(6L, "Cuautla", 1,"1");
            City cuernavaca = new City(7L, "Cuernavaca", 1,"1");
            City emilianoZapata = new City(8L, "Emiliano Zapata", 1,"1");
            City huitzilac = new City(9L, "Huitzilac", 1 ,"1");
            City jantetelco = new City(10L, "Jantetelco", 1,"1");
            City jiutepec = new City(11L, "Jiutepec", 1,"1");
            City jojutla = new City(12L, "Jojutla", 1,"1");
            City jonacatepec = new City(13L, "Jonacatepec", 1,"1");
            City mazatepec = new City(14L, "Mazatepec", 1,"1");
            City miacatlan = new City(15L, "Miacatlán", 1,"1");
            City ocuituco = new City(16L, "Ocuituco", 1,"1");
            City puenteDeIxtla = new City(17L, "Puente de Ixtla", 1,"1");
            City temixco = new City(18L, "Temixco", 1,"1");
            City tepalcingo = new City(19L, "Tepalcingo", 1,"1");
            City tepoztlan = new City(20L, "Tepoztlán", 1,"1");
            City tetecala = new City(21L, "Tetecala", 1,"1");
            City tetelaDelVolcan = new City(22L, "Tetela del Volcán", 1,"1");
            City tlalnepantla = new City(23L, "Tlalnepantla", 1,"1");
            City tlaltizapan = new City(24L, "Tlaltizapán", 1,"1");
            City tlalquiltenango = new City(25L, "Tlalquiltenango", 1,"1");
            City tlaxco = new City(26L, "Tlaquiltenango", 1,"1");
            City toluca = new City(27L, "Toluca", 1,"1");
            City totolapan = new City(28L, "Totolapan", 1,"1");
            City xochitepec = new City(29L, "Xochitepec", 1,"1");
            City yecapixtla = new City(30L, "Yecapixtla", 1,"1");
            City yecuatla = new City(31L, "Yecuatla", 1,"1");
            City zacatepec = new City(32L, "Zacatepec", 1,"1");
            City zacualpan = new City(33L, "Zacualpan", 1,"1");

            cityRepository.save(amacuzac);
            cityRepository.save(atlatlahucan);
            cityRepository.save(axochiapan);
            cityRepository.save(ayala);
            cityRepository.save(coatetelco);
            cityRepository.save(cuautla);
            cityRepository.save(cuernavaca);
            cityRepository.save(emilianoZapata);
            cityRepository.save(huitzilac);
            cityRepository.save(jantetelco);
            cityRepository.save(jiutepec);
            cityRepository.save(jojutla);
            cityRepository.save(jonacatepec);
            cityRepository.save(mazatepec);
            cityRepository.save(miacatlan);
            cityRepository.save(ocuituco);
            cityRepository.save(puenteDeIxtla);
            cityRepository.save(temixco);
            cityRepository.save(tepalcingo);
            cityRepository.save(tepoztlan);
            cityRepository.save(tetecala);
            cityRepository.save(tetelaDelVolcan);
            cityRepository.save(tlalnepantla);
            cityRepository.save(tlaltizapan);
            cityRepository.save(tlalquiltenango);
            cityRepository.save(tlaxco);
            cityRepository.save(toluca);
            cityRepository.save(totolapan);
            cityRepository.save(xochitepec);
            cityRepository.save(yecapixtla);
            cityRepository.save(yecuatla);
            cityRepository.save(zacatepec);
            cityRepository.save(zacualpan);

        }

        if (suburbRepository.count()==0){

            Suburb suburb1 = new Suburb(1L, "Adolfo López Mateos", "62764", 1, new City(7L, "Cuernavaca", 1, "1"));
            Suburb suburb2 = new Suburb(2L, "Chapultepec", "62763", 1, new City(7L, "Cuernavaca", 1, "1"));
            suburbRepository.save(suburb1);
            suburbRepository.save(suburb2);
        }
        logger.info("END RUNNER");


    }
}

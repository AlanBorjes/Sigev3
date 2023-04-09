package mx.edu.utez.sigev.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LogManager.getLogger(WebSecurityConfiguration.class);


    @Autowired
    private DataSource dataSource;

    @Autowired
    private SimpleAuthenticationSuccessHandler successHandler;

    @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
				.authoritiesByUsernameQuery("SELECT u.username, r.authority FROM user_role AS ur "
						+ "INNER JOIN users AS u ON u.id = ur.user "
						+ "INNER JOIN roles AS r ON r.id = ur.role WHERE u.username = ?");
	}

    @Override
        public void configure(HttpSecurity httpSecurity) throws Exception {
        try {
        httpSecurity.authorizeRequests().antMatchers(
            "/css/**", "/js/**", "/image/**","/img/**", "/error/**", "/images/**", "imagenes/**", "/docs/**").permitAll()
            .antMatchers("/", "/signup", "/encriptar/**").permitAll()
            .antMatchers("/city/**").hasAnyAuthority("ROL_ADMINISTRADOR")
            .antMatchers("/users/**").hasAnyAuthority("ROL_ADMINISTRADOR")
            .antMatchers("/category/**").hasAnyAuthority("ROL_ADMINISTRADOR")
            .antMatchers("/requests/**").hasAnyAuthority("ROL_ENLACE")
            .antMatchers("/suburb/**").hasAnyAuthority("ROL_ENLACE")
            .antMatchers("/president/**").hasAnyAuthority("ROL_PRESIDENTE")
            .anyRequest().authenticated()
            .and().formLogin().successHandler(successHandler).loginPage("/login").permitAll();
            logger.info("Todo salio bien");
        } catch (Exception exception) {
            logger.error("WEBSECURITYCONFIG ERROR: {}", exception.getMessage());

        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}

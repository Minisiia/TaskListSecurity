package TaskListManagmentSecurity.config;

import TaskListManagmentSecurity.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //for @preauthorize
public class SecurityConfig /* extends WebSecurityConfigurerAdapter */ {
    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public SecurityConfig(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    /**
     * @Override protected void configure(HttpSecurity http) throws Exception {
     * http.authorizeRequests()
     * .antMatchers("/admin/**").hasRole("ADMIN")
     * .antMatchers("/auth/login", "/error", "/auth/registration", "/hello", "/tasks").permitAll()
     * .anyRequest().hasAnyRole("USER", "ADMIN")
     * .and()
     * .formLogin().loginPage("/auth/login")
     * .loginProcessingUrl("/process_login")
     * .defaultSuccessUrl("/hello", true)
     * .failureUrl("/auth/login&error")
     * .and()
     * .logout()
     * .logoutUrl("/logout")
     * .logoutSuccessUrl("/auth/login");
     * }
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/auth/login", "/error", "/auth/registration", "/hello", "/tasks").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/hello", true)
                .failureUrl("/auth/login&error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login")
                .and()
                .build();
    }

    /**
     * @Override protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     * auth.userDetailsService(myUserDetailsService)
     * .passwordEncoder(getPasswordEncoder());
     * }
     */

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("user")
                .password("password")
                .roles("USER")
                .passwordEncoder(passwordEncoder::encode)
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

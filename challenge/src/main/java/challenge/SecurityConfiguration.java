package challenge;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeRequests()
            .antMatchers("/h2-console/*").permitAll()
            .antMatchers("/readMessage").permitAll()
        	.antMatchers("/getFollowerFollowing").permitAll()
        	.antMatchers("/startFollowing").permitAll()
        	.antMatchers("/unfollow").permitAll();
        
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
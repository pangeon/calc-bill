package pl.strefakursow.swamp_of_pox.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.httpBasic().disable();
        security.authorizeRequests().anyRequest().permitAll();
    }
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.POST, "/**");
        web.ignoring().antMatchers(HttpMethod.PUT, "/**");
        web.ignoring().antMatchers(HttpMethod.PATCH, "/**");
        web.ignoring().antMatchers(HttpMethod.DELETE, "/**");
    }
}

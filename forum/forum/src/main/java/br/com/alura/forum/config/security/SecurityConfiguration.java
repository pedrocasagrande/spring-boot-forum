package br.com.alura.forum.config.security;

import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Profile("producao")
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AutenticacaoService autenticacaoService;

    private final TokenService tokenService;

    private final UsuarioRepository repositorio;

    public SecurityConfiguration(AutenticacaoService autenticacaoService, TokenService tokenService, UsuarioRepository repositorio) {
        this.autenticacaoService = autenticacaoService;
        this.tokenService = tokenService;
        this.repositorio = repositorio;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //Configurações de Autenticação
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //Configurações de Autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().
                antMatchers(HttpMethod.GET, "/topicos").permitAll().
                antMatchers(HttpMethod.GET, "/topicos/*").permitAll().
                antMatchers(HttpMethod.POST, "/auth").permitAll().
                antMatchers(HttpMethod.GET, "/actuator/**").permitAll().
                antMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll().
                antMatchers(HttpMethod.DELETE, "/topicos/**").hasRole("MODERADOR").
                anyRequest().authenticated().
                and().csrf().disable().
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().addFilterBefore(new AutenticacaoTokenFilter(tokenService, repositorio), UsernamePasswordAuthenticationFilter.class);
    }

    //Configurações de Recursos Estaticos (js, css, img, etc.)
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().
                antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
    }


}

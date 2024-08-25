package com.example.SpeakLink.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig
{

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public static PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}


/*	@Bean
	public WebSecurityCustomizer webSecurityCustomizer()
	{
		return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**");
	}*/

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.csrf().disable()
				.authorizeHttpRequests((authorize) ->
						authorize
								.requestMatchers("/css/**", "/js/**", "/img/**", "/img/bootstrap-icons/**").permitAll()
								.requestMatchers("/register/**").permitAll()
								.requestMatchers("/index").permitAll()
								.requestMatchers("/users").hasRole("ADMIN")
								.requestMatchers("/chat", "/chat/**").authenticated()
								.anyRequest().permitAll()
				).formLogin(
						form -> form
								.loginPage("/login")
								.loginProcessingUrl("/login")
								.defaultSuccessUrl("/chat", true)
								.permitAll()
				)
				.logout(
						logout -> logout
								.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
								.permitAll()
				)
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.invalidSessionUrl("/login")
				.maximumSessions(10)
				.expiredUrl("/login");
		return http.build();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
	{
		auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}
}

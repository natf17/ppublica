package com.ancientdawn.ppublica.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.ancientdawn.ppublica.security.CustomLoginFilter;
import com.ancientdawn.ppublica.security.JwtAuthenticationFilter;
import com.ancientdawn.ppublica.security.JwtAuthenticationProvider;
import com.ancientdawn.ppublica.security.JwtAuthenticationSuccessHandler;
import com.ancientdawn.ppublica.security.LoginSuccessHandler;
import com.ancientdawn.ppublica.security.RestAuthenticationEntryPoint;
import com.ancientdawn.ppublica.security.authorization.AuthorizationUtility;


@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private AuthorizationUtility authorizationUtility;
	
	@Configuration
	@Order(2)
	public static class ApiConfigurer extends WebSecurityConfigurerAdapter {
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			System.out.println("CAL CONGIF");

			http.requestMatchers()
					.mvcMatchers("/69th", "/69th/edit", "/schedules", "/myslots", "/logout", "/api/publisher")
					.mvcMatchers("/schedules/add")
					.mvcMatchers("/", "/login", "/69th/**", "/myslots", "/logout", "/schedules/**", "/newPublisher", "/publishers", "/api/**")
				.and()
				.authorizeRequests()
					.antMatchers(GET, "/api/**").permitAll()
					.antMatchers(DELETE, "/api/assignments/{timeSlotId}/{publisherId}").access("@authorizationUtility.check(authentication, #publisherId)")
					.mvcMatchers("/api/**", "/myslots", "/schedules/add", "/newPublisher").authenticated()
					.regexMatchers("^(?!.*edit).*$").access("@authorizationUtility.allowUnauthorized(authentication)")
					//.mvcMatchers("/", "/login", "/69th", "/69th/edit", "/publishers", "/schedules").access("@authorizationUtility.allowUnauthorized(authentication)")
					.antMatchers("/api/publisher/{id}").access("@authorizationUtility.check(authentication, #id)")
					.anyRequest().authenticated();	
			
			http.logout().logoutSuccessUrl("/loggingOut");
			http.exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint());
			http.csrf().disable();
			http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
			
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			
		}
		
		public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
			JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
			jwtAuthenticationFilter.setAuthenticationSuccessHandler(jwtSuccessHandler());
			jwtAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());

			return jwtAuthenticationFilter;
		}
		
		@Override
		public void configure(WebSecurity web) throws Exception {
			web.debug(true);
			
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) {
			auth.authenticationProvider(jwtAuthenticationProvider());
		}
		
		@Bean(name="jwtAuthenticationProvider")
		public AuthenticationProvider jwtAuthenticationProvider() {
			JwtAuthenticationProvider provider = new JwtAuthenticationProvider();
			//provider.setUserDetailsService(jdbcDaoImpl());
			
			return provider;
		}
		
		@Bean(name="jwtSuccessHandler")
		public AuthenticationSuccessHandler jwtSuccessHandler() {
			return new JwtAuthenticationSuccessHandler();
		}
		
		@Bean( name="myAuthenticationManager1")
		@Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	         return super.authenticationManagerBean();
	     }

		
	}
		@Configuration
		@Order(1)
		public static class LoginConfigurer extends WebSecurityConfigurerAdapter {
			@Autowired
			private DataSource dataSource;
			
			@Override
			protected void configure(HttpSecurity http) throws Exception {
				System.out.println("Login Config");
				http.requestMatcher(new AntPathRequestMatcher("/login", "POST"))
					.authorizeRequests().anyRequest().permitAll();
				http.exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint());
				
				http.csrf().disable();
				http.addFilterBefore(customLoginFilter(), UsernamePasswordAuthenticationFilter.class);
				http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				
			}
			
			
			
			
			@Override
			public void configure(WebSecurity web) throws Exception {
				web.debug(true);
				
			}
			
			public CustomLoginFilter customLoginFilter() throws Exception {
				CustomLoginFilter customLoginFilter = new CustomLoginFilter();
				customLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
				customLoginFilter.setAuthenticationManager(authenticationManagerBean());
				return customLoginFilter;
			}
			
			@Bean(name="daoAuthenticationProvider")
			public AuthenticationProvider daoAuthenticationProvider() {
				DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
				provider.setUserDetailsService(jdbcDaoImpl());
				
				return provider;
			}
			
			public UserDetailsService jdbcDaoImpl() {
				String selectAuthByUsernameQuery = "select username,role from user_roles where username = ?";
				String selectUsersByUsernameQuery = "select username,password,enabled from publisher where username = ?";
				
				JdbcDaoImpl jdbcDaoImpl = new JdbcDaoImpl();
				jdbcDaoImpl.setDataSource(dataSource);
				jdbcDaoImpl.setAuthoritiesByUsernameQuery(selectAuthByUsernameQuery);
				jdbcDaoImpl.setUsersByUsernameQuery(selectUsersByUsernameQuery);
				
				
				
				return jdbcDaoImpl;
			}
			
			@Bean( name="myAuthenticationManager2")
		     @Override
		     public AuthenticationManager authenticationManagerBean() throws Exception {
		         return super.authenticationManagerBean();
		     }
			
			@Override
			protected void configure(AuthenticationManagerBuilder auth) {
				auth.authenticationProvider(daoAuthenticationProvider());
			}
			
			@Bean(name="loginSuccessHandler")
			public AuthenticationSuccessHandler loginSuccessHandler() {
				return new LoginSuccessHandler();
			}
			
			
			

		}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	// custom implementation of UserDetailsService
	private final UserService userService;
	
	//provides authentication services
	private final TokenAuthenticationService tokenAuthenticationService;


	public SecurityConfig() {
		
		

		// disable defaults
		super(true);
		
		this.userService = new UserService();
		
		this.tokenAuthenticationService = new TokenAuthenticationService("mySecretKey", userService);
		
	}
	/*
	 * Because defaults have been disabled... manually add......
	 * *exception handling (adds EcxeptionTranslationFilter to filter chain)
	 * *anonymous user representation
	 * *implementation of methods that use SecurityContext - populates SecurityContextHolderAwareFilter,
	 * **which links SecurityContext with HttpServletRequest methods
	 * default headers to response - disable these, only include cache control
	 * AND ALSO ADD custom filter
	 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.exceptionHandling().and()
			.securityContext().and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.anonymous().and()
			.servletApi().and()
			.headers().defaultsDisabled().cacheControl()
			;
		
		http
			.authorizeRequests()
			.antMatchers("/publisher/all").authenticated()
			.antMatchers("/**").permitAll().and()
			.addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class)
			;

	}
	
	/*
	 * Used by impl of authenticationManager() to obtain an AuthenticationManager
	 * Specify the AuthenticationManager
	 * call userDetailsService() to add authentication based upn custom UserDetailsService that is passed in
	 * The userDetailsService() method returns a DaoAuthenticationConfigurer to allow...
	 * ...customization of the authentication
	 
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	/*
	 * expose AuthenticationManager created by configure(...)
	 
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	/*
	 * return new (custom) instance of UserDetailsService
	 
	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return userService;
	}
	
	@Bean
	public TokenAuthenticationService tokenAuthenticationService() {
		return tokenAuthenticationService;
	}
	
	*/
}

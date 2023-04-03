package com.nk.discoveryserver.config;

//@Configuration
public class SecurityConfig{ //extends WebSecurityConfigurerAdapter {

//    @Value("${app.eureka.username}")
//    private String username;
//    @Value("${app.eureka.password}")
//    private String password;
//
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .passwordEncoder(NoOpPasswordEncoder.getInstance())
//                .withUser(username).password(password)
//                .authorities("USER");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests().anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();
//    }
}

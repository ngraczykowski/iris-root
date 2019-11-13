package com.silenteight.sens.webapp.backend.config;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.config.CasProperties.Logout;
import com.silenteight.sens.webapp.backend.config.token.TokenAuthenticationFilter;
import com.silenteight.sens.webapp.backend.config.token.TokenAuthenticationProvider;
import com.silenteight.sens.webapp.backend.security.RestAccessDeniedHandler;
import com.silenteight.sens.webapp.backend.security.cas.CasAuthenticationUserDetailsService;
import com.silenteight.sens.webapp.backend.security.cas.RestCasAuthenticationEntryPoint;
import com.silenteight.sens.webapp.backend.security.cas.RestLogoutSuccessHandler;
import com.silenteight.sens.webapp.backend.security.cas.SingleSignOutFilter;
import com.silenteight.sens.webapp.backend.security.dto.PrincipalDtoMapper;
import com.silenteight.sens.webapp.backend.security.login.SaveToDatabaseAuthenticationSuccessListener;
import com.silenteight.sens.webapp.backend.security.logout.RemoveSessionApplicationListener;
import com.silenteight.sens.webapp.backend.security.logout.UpdateSessionApplicationListener;
import com.silenteight.sens.webapp.user.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.session.SingleSignOutHandler;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.*;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@RequiredArgsConstructor
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

  private static final RequestMatcher PROTECTED_URLS =
      new AntPathRequestMatcher(RestConstants.ROOT + "/**");

  private final CorsFilter corsFilter;
  private final WebApplicationProperties webApplicationProperties;
  private final ObjectMapper objectMapper;
  private final UserService userService;
  private final TokenAuthenticationProvider tokenAuthenticationProvider;
  private final SimpleUrlAuthenticationSuccessHandler successHandler;

  @Override
  public void configure(WebSecurity web) {
    web
        .ignoring()
        .antMatchers(HttpMethod.OPTIONS, "/**")
        .antMatchers("/app/**/*.{js,html}")
        .antMatchers("/assets/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        // Cross-Site Request Forgery and security Headers disabled for REST API
        .csrf()
        .disable()
        .headers()
        .disable()

        .addFilter(casAuthenticationFilter())
        .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class)
        .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(tokenAuthenticationFilter(), BasicAuthenticationFilter.class)

        /*
          Support for login:
            - when access is forbidden accessing /api/** URLs, RestCasAuthenticationEntryPoint
              will be triggered,
            - when access is forbidden accessing other URLs, standard redirecting
              CasAuthenticationEntryPoint will get triggered.
         */
        .exceptionHandling()
        .defaultAuthenticationEntryPointFor(restCasAuthenticationEntryPoint(),
                                            restLoginRequestMatcher())
        .defaultAuthenticationEntryPointFor(casAuthenticationEntryPoint(),
                                            standardLoginRequestMatcher())
        .accessDeniedHandler(restAccessDeniedHandler())
        .and()

        /*
          Support logging out with:
            - POST to /api/logout - results in JSON response,
            - GET/POST/PUT/DELETE to /logout - results in redirection to logoutUrl.
         */
        .logout()
        .invalidateHttpSession(true)
        .logoutRequestMatcher(logoutRequestMatcher())
        .defaultLogoutSuccessHandlerFor(redirectingLogoutSuccessHandler(),
                                        standardLogoutRequestMatcher())
        .defaultLogoutSuccessHandlerFor(restLogoutSuccessHandler(), restLogoutRequestMatcher())
        .and()

        .authorizeRequests()
        .antMatchers(RestConstants.MANAGEMENT_PREFIX + "/health").permitAll()
        // FIXME(ahaczewski): Protect /management/**
        .antMatchers(RestConstants.MANAGEMENT_PREFIX + "/**").permitAll()

        .anyRequest().authenticated();
  }

  private static RequestMatcher restLoginRequestMatcher() {
    String apiPrefix = RestConstants.ROOT + "/**";
    String managementPrefix = RestConstants.MANAGEMENT_PREFIX + "/**";

    return new OrRequestMatcher(
        new AntPathRequestMatcher(apiPrefix, HttpMethod.GET.name()),
        new AntPathRequestMatcher(apiPrefix, HttpMethod.POST.name()),
        new AntPathRequestMatcher(apiPrefix, HttpMethod.PUT.name()),
        new AntPathRequestMatcher(apiPrefix, HttpMethod.DELETE.name()),
        new AntPathRequestMatcher(managementPrefix, HttpMethod.GET.name())
    );
  }

  private static RequestMatcher standardLoginRequestMatcher() {
    return new AndRequestMatcher(
        new AntPathRequestMatcher("**", HttpMethod.GET.name()),
        new NegatedRequestMatcher(restLoginRequestMatcher())
    );
  }

  private SingleSignOutFilter singleSignOutFilter() {
    return new SingleSignOutFilter(singleSignOutHandler());
  }

  private static RequestMatcher logoutRequestMatcher() {
    return new OrRequestMatcher(standardLogoutRequestMatcher(), restLogoutRequestMatcher());
  }

  private static RequestMatcher standardLogoutRequestMatcher() {
    return new OrRequestMatcher(
        new AntPathRequestMatcher(RestConstants.LOGOUT_URL, HttpMethod.GET.name()),
        new AntPathRequestMatcher(RestConstants.LOGOUT_URL, HttpMethod.POST.name()),
        new AntPathRequestMatcher(RestConstants.LOGOUT_URL, HttpMethod.PUT.name()),
        new AntPathRequestMatcher(RestConstants.LOGOUT_URL, HttpMethod.DELETE.name())
    );
  }

  private static RequestMatcher restLogoutRequestMatcher() {
    return new AntPathRequestMatcher(RestConstants.API_LOGOUT_URL, HttpMethod.POST.name());
  }

  private SimpleUrlLogoutSuccessHandler redirectingLogoutSuccessHandler() {
    SimpleUrlLogoutSuccessHandler handler = new SimpleUrlLogoutSuccessHandler();
    handler.setAlwaysUseDefaultTargetUrl(true);
    handler.setDefaultTargetUrl(webApplicationProperties.getLogoutUrl());
    return handler;
  }

  private RestLogoutSuccessHandler restLogoutSuccessHandler() {
    return new RestLogoutSuccessHandler(
        serviceProperties(), webApplicationProperties.getLogoutUrl(), objectMapper);
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(casAuthenticationProvider())
        .authenticationProvider(tokenAuthenticationProvider);
  }

  @Bean
  CasAuthenticationProvider casAuthenticationProvider() {
    CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
    casAuthenticationProvider.setAuthenticationUserDetailsService(createUserDetailsService());
    casAuthenticationProvider.setServiceProperties(serviceProperties());
    casAuthenticationProvider.setTicketValidator(cas30ServiceTicketValidator());
    casAuthenticationProvider.setKey(webApplicationProperties.getProviderKey());
    return casAuthenticationProvider;
  }

  private CasAuthenticationUserDetailsService createUserDetailsService() {
    return new CasAuthenticationUserDetailsService(userService);
  }

  @Bean
  Cas30ServiceTicketValidator cas30ServiceTicketValidator() {
    return new Cas30ServiceTicketValidator(webApplicationProperties.getTicketValidatorUrl());
  }

  @Bean
  RestCasAuthenticationEntryPoint restCasAuthenticationEntryPoint() {
    return new RestCasAuthenticationEntryPoint(
        serviceProperties(), webApplicationProperties.getLoginUrl(), objectMapper);
  }

  @Bean
  CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
    CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
    entryPoint.setLoginUrl(webApplicationProperties.getLoginUrl());
    entryPoint.setServiceProperties(serviceProperties());
    return entryPoint;
  }

  @Bean
  ServiceProperties serviceProperties() {
    ServiceProperties properties = new ServiceProperties();
    properties.setService(webApplicationProperties.getServiceId());
    properties.setSendRenew(false);
    return properties;
  }

  @Bean
  CasAuthenticationFilter casAuthenticationFilter() throws Exception {
    CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
    casAuthenticationFilter.setAuthenticationManager(authenticationManager());
    casAuthenticationFilter.setSessionAuthenticationStrategy(sessionStrategy());
    return casAuthenticationFilter;
  }

  @Bean
  SessionAuthenticationStrategy sessionStrategy() {
    return new SessionFixationProtectionStrategy();
  }

  @Bean
  SingleSignOutHandler singleSignOutHandler() {
    Logout logout = webApplicationProperties.getLogout();

    SingleSignOutHandler handler = new SingleSignOutHandler();
    handler.setArtifactParameterName(logout.getArtifactParameterName());
    handler.setLogoutParameterName(logout.getLogoutParameterName());
    handler.setRelayStateParameterName(logout.getRelayStateParameterName());
    handler.setCasServerUrlPrefix(StringUtils.trimToEmpty(logout.getCasServerUrlPrefix()));
    handler.setArtifactParameterOverPost(logout.isArtifactParameterOverPost());
    handler.setEagerlyCreateSessions(logout.isEagerlyCreateSessions());

    handler.init();
    return handler;
  }

  @Bean
  RemoveSessionApplicationListener removeSessionApplicationListener(SingleSignOutHandler handler) {
    return new RemoveSessionApplicationListener(handler.getSessionMappingStorage());
  }

  @Bean
  UpdateSessionApplicationListener updateSessionApplicationListener(SingleSignOutHandler handler) {
    return new UpdateSessionApplicationListener(handler.getSessionMappingStorage());
  }

  @Bean
  RestAccessDeniedHandler restAccessDeniedHandler() {
    return new RestAccessDeniedHandler(objectMapper);
  }

  @Bean
  PrincipalDtoMapper principalDtoMapper() {
    return new PrincipalDtoMapper();
  }

  @Bean
  TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
    final TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(successHandler);
    return filter;
  }

  @Bean
  ApplicationListener<AbstractAuthenticationEvent> saveToDatabaseAuthenticationSuccessHandler() {
    return new SaveToDatabaseAuthenticationSuccessListener(userService);
  }
}

package com.silenteight.searpaymentsmockup;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.security.channel.ChannelSecurityInterceptor;
import org.springframework.integration.security.channel.SecuredChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableIntegration
class SearPaymentsConfiguration {

  private static final String ALERT_READY_FOR_CALLBACK_CHANNEL = "alertReadyForCallbackChannel";

  @Bean
  LogRequestAspect logRequestAspect(ApplicationContext applicationContext) {
    return new LogRequestAspect(applicationContext);
  }

  @Bean
  CmapiAuthenticateAspect cmapiAuthenticate(ApplicationContext applicationContext) {
    return new CmapiAuthenticateAspect(applicationContext);
  }

  @Bean
  AddDcToRequestDto addDcToRequestDto() {
    return new AddDcToRequestDto();
  }

  @Bean
  @SecuredChannel(interceptor = "channelSecurityInterceptor", sendAccess = "ROLE_SUBMIT_REQUEST")
  MessageChannel submitRequestChannel() {
    return new QueueChannel(2);
  }

  @Bean
  PollerSpec submitRequestPoller() {
    return Pollers
        .fixedDelay(100)
        .receiveTimeout(0)
        .taskExecutor(new ThreadPoolExecutor(
            2,
            2,
            Long.MAX_VALUE, TimeUnit.DAYS,
            new ArrayBlockingQueue<>(Runtime.getRuntime().availableProcessors()),
            new NamedThreadFactory("requestToAlert"),
            noopRejectedExecutionHandler()));
  }

  private static RejectedExecutionHandler noopRejectedExecutionHandler() {
    return (r, executor) -> {
    };
  }

  @Bean
  MockAlertEtl alertEtl() {
    return new MockAlertEtl();
  }

  @Bean
  MessageChannel alertEtlCompletedChannel() {
    return new QueueChannel(2);
  }

  @Bean
  IntegrationFlow processSubmittedRequests(
      MessageChannel submitRequestChannel,
      PollerSpec submitRequestPoller,
      IntegrationFlow subflowAlertNotObsolete) {

    return IntegrationFlows.from(submitRequestChannel)
        .bridge(ec -> ec.poller(submitRequestPoller))
        .log(Level.INFO, "mockup-training.collected-request", m -> m)
        .split(RequestDto.class, RequestDto::getAlerts)
        .enrichHeaders(
            hec -> hec.header(MessageHeaders.ERROR_CHANNEL, "damagedAlertDtoChannel")
                .<AlertDto>headerFunction("systemId", a -> a.getPayload().getSystemId()))
        .<AlertDto, Boolean>route(
            SearPaymentsConfiguration::alertIsObsolete,
            m -> m.channelMapping(true, "obsoleteAlertChannel")
                .subFlowMapping(false, subflowAlertNotObsolete))
        .get();
  }

  @Bean
  IntegrationFlow subflowAlertNotObsolete(
      IntegrationFlow subflowProcessAlert) {
    return sf -> sf.<AlertDto, Boolean>route(
        SearPaymentsConfiguration::alertIsPaired,
        m2 -> m2.channelMapping(true, "pairedAlertChannel")
            .subFlowMapping(false, subflowProcessAlert));
  }

  @Bean
  IntegrationFlow pairedAlertIntegrationFlow() {
    // @TODO
    return IntegrationFlows.from("pairedAlertChannel")
        .log(Level.INFO, "mockup-training.collected-paired-alert", m -> m)
        .get();
  }

  @Bean
  IntegrationFlow obsoleteAlertIntegrationFlow() {
    // @TODO
    return IntegrationFlows.from("obsoleteAlertChannel")
        .log(Level.INFO, "mockup-training.collected-obsolete-alert", m -> m)
        .get();
  }

  @Bean
  IntegrationFlow subflowProcessAlert(
      AlertEtl alertEtl) {
    return sf -> sf
        .<AlertDto>handle((p, h) -> alertEtl.invoke(p))
        .enrichHeaders(Map.of(MessageHeaders.ERROR_CHANNEL, "damagedAlertChannel"))
        .log(Level.INFO, "mockup-training.processing-alert", m -> m)
        .handle(this::recommendAlertFunc)
        .channel(ALERT_READY_FOR_CALLBACK_CHANNEL);
  }

  @Bean
  IntegrationFlow alertReadyForCallbackIntegrationFlow() {

    return IntegrationFlows.from(ALERT_READY_FOR_CALLBACK_CHANNEL)
        .handle(this::createCallbackResponseFunc)
        .log(Level.INFO, "mockup-training.alert-processed", m -> m)
        .handle(
            Http.outboundChannelAdapter(
                "http://localhost:24609/callback-recommendation")
                .httpMethod(HttpMethod.POST)
                .expectedResponseType(String.class),
            ec -> ec.advice(new RequestHandlerLogMessageAdvice())
        ).get();
  }

  CallbackResponseDto createCallbackResponseFunc(long alertId, MessageHeaders mh) {
    return new MockCreateCallbackResponse(alertId).invoke();
  }

  long recommendAlertFunc(long alertId, MessageHeaders mh) {
    new MockRecommendAlert(alertId).invoke();
    return alertId;
  }

  private static boolean alertIsPaired(AlertDto alertDto) {
    return false;
  }

  private static boolean alertIsObsolete(AlertDto alertDto) {
    return false;
  }

  @Bean
  IntegrationFlow damagedAlertDtoIntegrationFlow() {
    return IntegrationFlows.from("damagedAlertDtoChannel")
        .log(Level.INFO, "mockup-training.collected-damaged-alert-dto", m -> m)
        .handle(this::buildAlertFromDamagedAlertDto)
        .channel(ALERT_READY_FOR_CALLBACK_CHANNEL)
        .get();
  }

  long buildAlertFromDamagedAlertDto(AlertDto alertDto, MessageHeaders mh) {
    // build alert here, use some class, bean, etc.
    // return alertId
    return 1L;
  }

  @Bean
  IntegrationFlow damagedAlertIntegrationFlow() {
    return IntegrationFlows.from("damagedAlertChannel")
        .log(Level.INFO, "mockup-training.collected-damaged-alert", m -> m)
        .handle(this::markAlertAsDamaged)
        .channel(ALERT_READY_FOR_CALLBACK_CHANNEL)
        .get();
  }

  long markAlertAsDamaged(long alertId, MessageHeaders mh) {
    // Process alert here, use some class, bean, etc.
    return alertId;
  }

  @Bean
  public ChannelSecurityInterceptor channelSecurityInterceptor(
      AuthenticationManager authenticationManager,
      AccessDecisionManager accessDecisionManager) {
    var channelSecurityInterceptor = new ChannelSecurityInterceptor();
    channelSecurityInterceptor.setAuthenticationManager(authenticationManager);
    channelSecurityInterceptor.setAccessDecisionManager(accessDecisionManager);
    return channelSecurityInterceptor;
  }
}

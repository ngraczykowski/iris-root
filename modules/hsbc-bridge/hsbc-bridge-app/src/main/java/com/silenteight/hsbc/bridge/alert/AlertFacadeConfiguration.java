package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.retention.AlertRetentionSender;
import com.silenteight.hsbc.bridge.retention.DataCleaner;
import com.silenteight.hsbc.bridge.retention.DataRetentionMessageSender;
import com.silenteight.hsbc.bridge.retention.DryRunDataCleaner;
import com.silenteight.hsbc.bridge.util.CustomDateTimeFormatter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({ AnalystDecisionProperties.class, LearningProperties.class })
class AlertFacadeConfiguration {

  private final AlertRepository alertRepository;
  private final AlertPayloadConverter alertPayloadConverter;
  private final AlertPayloadRepository alertPayloadRepository;
  private final WarehouseApi warehouseApi;
  private final AgentApi agentApi;
  private final CustomDateTimeFormatter dateTimeFormatter;
  private final AnalystDecisionProperties decisionProperties;
  private final LearningProperties learningProperties;
  private final DataRetentionMessageSender dataRetentionMessageSender;
  private final AlertReProcessor reProcessor;

  @Bean
  AlertFacade alertFacade(EntityManager entityManager) {
    return AlertFacade.builder()
        .alertPayloadConverter(alertPayloadConverter)
        .repository(alertRepository)
        .alertReProcessor(reProcessor)
        .entityManager(entityManager)
        .build();
  }

  @Bean
  DataCleaner alertDataCleaner() {
    return new AlertDataCleaner(alertPayloadRepository);
  }

  @Bean
  AlertRetentionSender alertRetentionSender() {
    return new AlertRetentionMessageSender(alertRepository, dataRetentionMessageSender);
  }

  @Bean
  LearningAlertProcessor learningAlertProcessor(EntityManager entityManager) {
    return new LearningAlertProcessor(
        learningProperties.getBatchSize(),
        entityManager,
        alertRepository,
        alertSender());
  }

  @Bean
  DryRunDataCleaner dryRunDataCleaner() {
    return new AlertDryRunDataCleaner(alertPayloadRepository);
  }

  private AlertSender alertSender() {
    return new AlertSender(warehouseApi, agentApi, alertMapper(), alertRepository);
  }

  private AlertMapper alertMapper() {
    return new AlertMapper(alertPayloadConverter, analystDecisionMapper(), caseCommentsMapper());
  }

  private AnalystDecisionMapper analystDecisionMapper() {
    return new AnalystDecisionMapper(decisionProperties.getMap());
  }

  private CaseCommentsMapper caseCommentsMapper() {
    return new CaseCommentsMapper(dateTimeFormatter.getDateTimeFormatter());
  }
}

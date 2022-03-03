package com.silenteight.customerbridge.common.batch.ecm;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import com.silenteight.customerbridge.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.customerbridge.common.batch.DateConverter;
import com.silenteight.customerbridge.common.batch.SuspectDataFetcher;
import com.silenteight.customerbridge.common.batch.ecm.EcmRecordCompositeFetcher.EcmFetcherConfiguration;
import com.silenteight.customerbridge.common.config.FetcherConfiguration;
import com.silenteight.customerbridge.common.hitdetails.HitDetailsParser;
import com.silenteight.customerbridge.common.quartz.EcmAnalystDecision;

import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static com.silenteight.customerbridge.cbs.gateway.CbsGatewayFactory.getHitDetailsHelperFetcher;


@AllArgsConstructor(access = AccessLevel.PACKAGE)
class EcmAlertFetcherConfigurationHelper {

  private final DateConverter dateConverter;
  private final ApplicationEventPublisher eventPublisher;
  private final int queryTimeout;
  private final List<EcmAnalystDecision> ecmAnalystDecisions;

  EcmAlertFetcherConfigurationHelper(
      ScbBridgeConfigProperties configProperties,
      ApplicationEventPublisher eventPublisher,
      List<EcmAnalystDecision> ecmAnalystDecisions) {
    this.dateConverter = new DateConverter(configProperties.getTimeZone());
    this.queryTimeout = configProperties.getQueryTimeout();
    this.eventPublisher = eventPublisher;
    this.ecmAnalystDecisions = ecmAnalystDecisions;
  }

  EcmRecordCompositeFetcher createEcmRecordCompositeFetcher(
      String dbRelationName, String cbsHitsDetailsHelperViewName) {

    return new EcmRecordCompositeFetcher(
        createEcmAlertCompositeRowProcessor(),
        new EcmFetcherConfiguration(dbRelationName, queryTimeout),
        eventPublisher,
        createSuspectDataFetcher(cbsHitsDetailsHelperViewName));
  }

  EcmRecordDecisionsFetcher createEcmDecisionsFetcher(String ecmView) {
    return new EcmRecordDecisionsFetcher(ecmDecisionRowMapper(), ecmView);
  }

  private SuspectDataFetcher createSuspectDataFetcher(String cbsHitsDetailsHelperViewName) {
    FetcherConfiguration configuration = createFetcherConfiguration(cbsHitsDetailsHelperViewName);

    return new SuspectDataFetcher(
        new HitDetailsParser(), getHitDetailsHelperFetcher(configuration));
  }

  private EcmAlertCompositeRowProcessor createEcmAlertCompositeRowProcessor() {
    return new EcmAlertCompositeRowProcessor(dateConverter);
  }

  private FetcherConfiguration createFetcherConfiguration(String dbRelationName) {
    return new FetcherConfiguration(dbRelationName, queryTimeout);
  }

  private EcmDecisionRowMapper ecmDecisionRowMapper() {
    return new EcmDecisionRowMapper(dateConverter, ecmAnalystDecisionMapper());
  }

  private EcmAnalystDecisionMapper ecmAnalystDecisionMapper() {
    return new EcmAnalystDecisionMapper(ecmAnalystDecisions);
  }
}

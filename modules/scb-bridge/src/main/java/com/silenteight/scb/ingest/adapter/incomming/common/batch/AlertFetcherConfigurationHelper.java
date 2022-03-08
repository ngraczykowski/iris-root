package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.GnsSolutionMapper;
import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.config.FetcherConfiguration;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;

import org.springframework.context.ApplicationEventPublisher;

import static com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsGatewayFactory.getHitDetailsHelperFetcher;


@AllArgsConstructor(access = AccessLevel.PACKAGE)
class AlertFetcherConfigurationHelper {

  private final DateConverter dateConverter;
  private final ApplicationEventPublisher eventPublisher;
  private final int queryTimeout;
  private final GnsSolutionMapper gnsSolutionMapper;

  AlertFetcherConfigurationHelper(
      ScbBridgeConfigProperties configProperties,
      ApplicationEventPublisher eventPublisher,
      GnsSolutionMapper gnsSolutionMapper) {
    this.dateConverter = new DateConverter(configProperties.getTimeZone());
    this.queryTimeout = configProperties.getQueryTimeout();
    this.eventPublisher = eventPublisher;
    this.gnsSolutionMapper = gnsSolutionMapper;
  }

  RecordCompositeFetcher createRecordCompositeFetcher(
      String dbRelationName, String cbsHitsDetailsHelperViewName, boolean watchlistLevel) {

    return new RecordCompositeFetcher(
        createAlertCompositeRowProcessor(watchlistLevel),
        createFetcherConfiguration(dbRelationName),
        createSuspectDataFetcher(cbsHitsDetailsHelperViewName),
        eventPublisher);
  }

  RecordDecisionsFetcher createDecisionsFetcher(String dbRelationName) {
    return new RecordDecisionsFetcher(decisionRowMapper(), dbRelationName);
  }

  private SuspectDataFetcher createSuspectDataFetcher(String cbsHitsDetailsHelperViewName) {
    FetcherConfiguration configuration = createFetcherConfiguration(cbsHitsDetailsHelperViewName);
    return new SuspectDataFetcher(
        new HitDetailsParser(), getHitDetailsHelperFetcher(configuration));
  }

  private AlertCompositeRowProcessor createAlertCompositeRowProcessor(boolean watchlistLevel) {
    return new AlertCompositeRowProcessor(dateConverter, watchlistLevel);
  }

  private FetcherConfiguration createFetcherConfiguration(String dbRelationName) {
    return new FetcherConfiguration(dbRelationName, queryTimeout);
  }

  private DecisionRowMapper decisionRowMapper() {
    return new DecisionRowMapper(dateConverter, gnsSolutionMapper);
  }
}

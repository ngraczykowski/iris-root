/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm.EcmRecordCompositeFetcher.EcmFetcherConfiguration;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.CbsGatewayFactory;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.DateConverter;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.SuspectDataFetcher;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.config.FetcherConfiguration;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.quartz.EcmAnalystDecision;

import org.springframework.context.ApplicationEventPublisher;

import java.util.List;


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

  private SuspectDataFetcher createSuspectDataFetcher(String cbsHitsDetailsHelperViewName) {
    FetcherConfiguration configuration = createFetcherConfiguration(cbsHitsDetailsHelperViewName);

    return new SuspectDataFetcher(
        new HitDetailsParser(), CbsGatewayFactory.getHitDetailsHelperFetcher(configuration));
  }

  private FetcherConfiguration createFetcherConfiguration(String dbRelationName) {
    return new FetcherConfiguration(dbRelationName, queryTimeout);
  }

  private EcmAlertCompositeRowProcessor createEcmAlertCompositeRowProcessor() {
    return new EcmAlertCompositeRowProcessor(dateConverter);
  }

  EcmRecordDecisionsFetcher createEcmDecisionsFetcher(String ecmView) {
    return new EcmRecordDecisionsFetcher(ecmDecisionRowMapper(), ecmView);
  }

  private EcmDecisionRowMapper ecmDecisionRowMapper() {
    return new EcmDecisionRowMapper(dateConverter, ecmAnalystDecisionMapper());
  }

  private EcmAnalystDecisionMapper ecmAnalystDecisionMapper() {
    return new EcmAnalystDecisionMapper(ecmAnalystDecisions);
  }
}

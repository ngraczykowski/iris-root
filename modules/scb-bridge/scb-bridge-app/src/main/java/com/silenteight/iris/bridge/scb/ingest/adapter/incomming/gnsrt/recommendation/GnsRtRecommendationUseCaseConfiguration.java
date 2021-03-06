/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.trafficmanagement.TrafficManager;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtRequestToAlertMapper;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtResponseMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
@RequiredArgsConstructor
class GnsRtRecommendationUseCaseConfiguration {

  private final GnsRtRecommendationProperties gnsRtRecommendationProperties;
  private final GnsRtRequestToAlertMapper gnsRtRequestToAlertMapper;
  private final GnsRtResponseMapper gnsRtResponseMapper;
  private final BatchAlertIngestService ingestService;
  private final RawAlertService rawAlertService;
  private final BatchInfoService batchInfoService;
  private final GnsRtRecommendationService gnsRtRecommendationService;
  private final TrafficManager trafficManager;

  @Bean
  GnsRtRecommendationUseCase gnsRtRecommendationUseCase() {
    return GnsRtRecommendationUseCaseImpl.builder()
        .alertMapper(gnsRtRequestToAlertMapper)
        .responseMapper(gnsRtResponseMapper)
        .ingestService(ingestService)
        .rawAlertService(rawAlertService)
        .batchInfoService(batchInfoService)
        .gnsRtRecommendationService(gnsRtRecommendationService)
        .trafficManager(trafficManager)
        .recommendationProperties(gnsRtRecommendationProperties)
        .scheduler(scheduler())
        .build();
  }

  private Scheduler scheduler() {
    return Schedulers.newBoundedElastic(
        gnsRtRecommendationProperties.getSchedulerThreadCap(),
        gnsRtRecommendationProperties.getSchedulerQueuedTaskCap(),
        "gns-rt-scheduler");
  }
}

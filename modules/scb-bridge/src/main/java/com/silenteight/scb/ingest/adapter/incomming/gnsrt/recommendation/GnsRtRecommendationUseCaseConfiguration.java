package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.proto.serp.v1.api.ReactorRecommendationGatewayGrpc;
import com.silenteight.proto.serp.v1.api.ReactorRecommendationGatewayGrpc.ReactorRecommendationGatewayStub;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo.AlertInfoService;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtRequestToAlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtResponseMapper;
import com.silenteight.scb.ingest.domain.AlertService;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class GnsRtRecommendationUseCaseConfiguration {

  private final GnsRtRecommendationProperties gnsRtRecommendationProperties;
  private final GnsRtRequestToAlertMapper gnsRtRequestToAlertMapper;
  private final GnsRtResponseMapper gnsRtResponseMapper;
  private final AlertInfoService alertInfoService;
  private final ScbRecommendationService scbRecommendationService;
  private final AlertService alertService;

  @Setter(onMethod_ = @GrpcClient("gateway"))
  private Channel gatewayChannel;

  @Bean
  GnsRtRecommendationUseCase gnsRtRecommendationUseCase() {
    return GnsRtRecommendationUseCaseImpl.builder()
        .alertMapper(gnsRtRequestToAlertMapper)
        .responseMapper(gnsRtResponseMapper)
        .alertInfoService(alertInfoService)
        .storeGnsRtRecommendationUseCase(storeGnsRtRecommendationUseCase())
        .recommendationService(recommendationGatewayService())
        .alertService(alertService)
        .build();
  }

  private StoreGnsRtRecommendationUseCase storeGnsRtRecommendationUseCase() {
    return new StoreGnsRtRecommendationUseCase(scbRecommendationService);
  }

  private RecommendationGatewayService recommendationGatewayService() {
    return RecommendationGatewayService.builder()
        .deadlineInSeconds(gnsRtRecommendationProperties.getDeadlineInSeconds())
        .recommendationGatewayStub(recommendationGatewayStub())
        .build();
  }

  private ReactorRecommendationGatewayStub recommendationGatewayStub() {
    return ReactorRecommendationGatewayGrpc
        .newReactorStub(gatewayChannel)
        .withWaitForReady();
  }
}

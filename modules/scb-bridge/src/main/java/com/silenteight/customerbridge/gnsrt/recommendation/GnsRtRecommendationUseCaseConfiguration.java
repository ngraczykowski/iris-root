package com.silenteight.customerbridge.gnsrt.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.customerbridge.common.recommendation.ScbRecommendationService;
import com.silenteight.customerbridge.common.recommendation.alertinfo.AlertInfoService;
import com.silenteight.customerbridge.gnsrt.mapper.GnsRtRequestToAlertMapper;
import com.silenteight.customerbridge.gnsrt.mapper.GnsRtResponseMapper;
import com.silenteight.proto.serp.v1.api.ReactorRecommendationGatewayGrpc;
import com.silenteight.proto.serp.v1.api.ReactorRecommendationGatewayGrpc.ReactorRecommendationGatewayStub;

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

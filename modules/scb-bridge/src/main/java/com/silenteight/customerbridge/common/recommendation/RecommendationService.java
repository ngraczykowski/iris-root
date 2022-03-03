package com.silenteight.customerbridge.common.recommendation;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.util.CensorshipUtils;

import java.util.Optional;

@Builder
@Slf4j
class RecommendationService {

  private final SystemIdFinder systemIdFinder;
  private final ScbRecommendationService scbRecommendationService;
  private final RecommendationOrderHandler orderService;
  private final RecommendationOrderProperties recommendationOrderProperties;

  RecommendationDto getRecommendation(@NonNull RecommendationRequest request) {
    String systemId = getSystemId(request);

    if (recommendationOrderProperties.isOnDemandEnabled()) {
      return findCurrentRecommendation(systemId).orElseGet(
          () -> orderService.orderAndReceiveRecommendation(systemId));
    } else {
      return findCurrentRecommendation(systemId)
          .or(() -> findLatestRecommendation(systemId))
          .orElseThrow(SystemIdNotFoundException::new);
    }
  }

  RecommendationDto getExistingRecommendationOrOnDemand(@NonNull RecommendationRequest request) {
    String systemId = getSystemId(request);

    return findCurrentRecommendation(systemId).orElseGet(
        () -> orderService.orderAndReceiveRecommendation(systemId));
  }

  private Optional<RecommendationDto> findCurrentRecommendation(String systemId) {
    return scbRecommendationService.findCurrentRecommendation(systemId)
        .map(ScbRecommendation::toRecommendationDto);
  }

  private Optional<RecommendationDto> findLatestRecommendation(String systemId) {
    return scbRecommendationService.findCurrentOrLatestRecommendation(systemId)
        .map(r -> r.toOutdatedRecommendationDto(
            recommendationOrderProperties.getOutdatedMessage()));
  }

  private String getSystemId(RecommendationRequest request) {
    if (request.hasSystemId())
      return request.getSystemId();

    Optional<String> foundSystemId = systemIdFinder.find(createSystemIdDto(request));
    if (foundSystemId.isEmpty()) {
      logError(request);
      throw new SystemIdNotFoundException();
    }

    return foundSystemId.get();
  }

  private static void logError(RecommendationRequest request) {
    log.error("SystemId cannot be found for unit={}, account={}",
        request.getUnit(), CensorshipUtils.maskEnd(request.getAccount()));
  }

  private static AlertDescriptorDto createSystemIdDto(RecommendationRequest request) {
    return new AlertDescriptorDto(
        request.getUnit(), request.getAccount(), request.getRecordDetails());
  }
}

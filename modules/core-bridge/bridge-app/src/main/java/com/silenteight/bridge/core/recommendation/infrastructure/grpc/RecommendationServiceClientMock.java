package com.silenteight.bridge.core.recommendation.infrastructure.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.recommendation.*;
import com.silenteight.bridge.core.registration.domain.model.Alert.Status;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
class RecommendationServiceClientMock implements RecommendationServiceClient {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Override
  public Collection<RecommendationWithMetadataOut> getRecommendations(@NotNull String analysis) {
    log.info("MOCK: Get recommendations from AE");
    var alertNames = getAlertNames(analysis);
    return alertNames.stream()
        .map(this::getRecommendation)
        .toList();
  }

  private List<AlertIdName> getAlertNames(String analysis) {
    var namedParameters = new MapSqlParameterSource()
        .addValue("analysisName", analysis)
        .addValue("status", Status.ERROR.name());
    var alertNamesFromDb = jdbcTemplate.query(
        """
            SELECT name, alert_id 
            FROM alerts a
            LEFT JOIN batches b ON a.batch_id = b.batch_id
            WHERE analysis_name = :analysisName
            AND a.status != :status
            """,
        namedParameters,
        (rs, rowNum) -> new AlertIdName(rs.getString("ALERT_ID"), rs.getString("NAME")));
    if (alertNamesFromDb.isEmpty()) {
      return List.of(getRandomAlertIdName(), getRandomAlertIdName());
    }
    return alertNamesFromDb;
  }

  private AlertIdName getRandomAlertIdName() {
    return new AlertIdName(UUID.randomUUID().toString(), UUID.randomUUID().toString());
  }

  private RecommendationWithMetadataOut getRecommendation(AlertIdName alertIdName) {
    return RecommendationWithMetadataOut.builder()
        .name(UUID.randomUUID().toString())
        .alert(alertIdName.alertName())
        .recommendedAction(RecommendedAction.getRandom())
        .recommendationComment("recommendationComment/mock")
        .date(OffsetDateTime.now())
        .metadata(createMockMetadata(alertIdName.alertId()))
        .build();
  }

  private RecommendationMetadataOut createMockMetadata(String alertId) {
    var recommendationMetadata = new RecommendationMetadataOut();
    var metadata = new MatchMetadataOut();
    metadata.setMatch("alerts/" + alertId + "/matches/" + UUID.randomUUID());
    metadata.setSolution("solution/mock");
    metadata.setReason(getReason());
    metadata.setCategories(Map.of("mockCategory", "someCategory"));
    metadata.setFeatures(Map.of("mockFeature", createMockFeatureMetadata()));

    recommendationMetadata.setMatchesMetadata(List.of(metadata));
    return recommendationMetadata;
  }

  private Map<String, String> getReason() {
    return Map.of(
        "step", "steps/II-ab56-4576-b653-1cdceb2d25e7",
        "policy", "policies/5afc2f12-85c0-4fb3-992e-1552ac843ceb",
        "feature_vector_signature", "J4VGkp1+FaNsaGDtBXgQsWpUYDo="
    );
  }

  private FeatureMetadataOut createMockFeatureMetadata() {
    var featureMetadata = new FeatureMetadataOut();
    featureMetadata.setSolution("solution/mock");
    featureMetadata.setAgentConfig("agentConfig/mock");
    featureMetadata.setReason(Map.of("mockReason", "someReason"));

    return featureMetadata;
  }


  private enum RecommendedAction {
    ACTION_INVESTIGATE,
    ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE,
    ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE,
    ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE,
    ACTION_FALSE_POSITIVE,
    ACTION_POTENTIAL_TRUE_POSITIVE;

    static String getRandom() {
      var actions = List.of(RecommendedAction.values());
      var length = RecommendedAction.values().length;
      Random random = new Random();
      return actions.get(random.nextInt(length)).name();
    }
  }


  private record AlertIdName(String alertId, String alertName) {}
}

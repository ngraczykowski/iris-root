package com.silenteight.bridge.core.recommendation.infrastructure.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.recommendation.*;
import com.silenteight.bridge.core.registration.domain.model.AlertStatus;

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

  private List<AlertDto> getAlertNames(String analysis) {
    var namedParameters = new MapSqlParameterSource()
        .addValue("analysisName", analysis)
        .addValue("status", AlertStatus.ERROR.name());
    var alertNamesFromDb = jdbcTemplate.query(
        """
            SELECT a.id, name, alert_id
            FROM core_bridge_alerts a
            LEFT JOIN core_bridge_batches b ON a.batch_id = b.batch_id
            WHERE analysis_name = :analysisName
            AND a.status != :status
            """,
        namedParameters,
        (rs, rowNum) -> new AlertDto(
            rs.getInt("ID"),
            rs.getString("ALERT_ID"),
            rs.getString("NAME")));
    if (alertNamesFromDb.isEmpty()) {
      return List.of(getRandomAlertIdName(), getRandomAlertIdName());
    }
    return alertNamesFromDb;
  }

  private AlertDto getRandomAlertIdName() {
    return new AlertDto(1, UUID.randomUUID().toString(), UUID.randomUUID().toString());
  }

  private RecommendationWithMetadataOut getRecommendation(AlertDto alertIdName) {
    return RecommendationWithMetadataOut.builder()
        .name(UUID.randomUUID().toString())
        .alert(alertIdName.alertName())
        .recommendedAction(RecommendedAction.getRandom())
        .recommendationComment("recommendationComment/mock")
        .date(OffsetDateTime.now())
        .metadata(createMockMetadata(alertIdName.id()))
        .build();
  }

  private RecommendationMetadataOut createMockMetadata(Integer alertId) {
    var recommendationMetadata = new RecommendationMetadataOut();
    var matchesMetadata = getMatchMetadataOuts(alertId);
    recommendationMetadata.setMatchesMetadata(matchesMetadata);
    return recommendationMetadata;
  }

  private List<MatchMetadataOut> getMatchMetadataOuts(Integer alertId) {
    var namedParameters = new MapSqlParameterSource()
        .addValue("alertId", alertId);
    var matchesFromDb = jdbcTemplate.queryForList(
        """
            SELECT name
            FROM core_bridge_matches m
            WHERE alert_id = :alertId
            """,
        namedParameters, String.class);

    return matchesFromDb.stream()
        .map(matchName -> {
          var metadata = new MatchMetadataOut();
          metadata.setMatch(matchName);
          metadata.setSolution("EXACT_MATCH");
          metadata.setReason(getReason());
          metadata.setCategories(Map.of("mockCategory", "someCategory"));
          metadata.setFeatures(Map.of("mockFeature", createMockFeatureMetadata()));
          return metadata;
        }).toList();
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


  private record AlertDto(Integer id, String alertId, String alertName) {}
}

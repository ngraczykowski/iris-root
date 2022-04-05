package com.silenteight.bridge.core.registration.infrastructure.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.util.TimeStampUtil;
import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata;
import com.silenteight.bridge.core.registration.domain.model.AlertStatus;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({ "dev", "test" })
class RecommendationCreatorMock {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public Collection<RecommendationInfo> getRecommendations(@NotNull String analysisName) {
    return getAlertNames(analysisName).stream()
        .map(alert -> createRecommendationInfo(alert, analysisName))
        .toList();
  }

  private List<AlertDto> getAlertNames(String analysisName) {
    var namedParameters = new MapSqlParameterSource()
        .addValue("analysisName", analysisName)
        .addValue("status", AlertStatus.ERROR.name());
    var alertNamesFromDb = jdbcTemplate.query(
        """
            SELECT a.id, a.name, a.alert_id
            FROM core_bridge_alerts a
            LEFT JOIN core_bridge_batches b ON a.batch_id = b.batch_id
            WHERE b.analysis_name = :analysisName
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

  private RecommendationInfo createRecommendationInfo(AlertDto alertDto, String analysisName) {
    var recommendationName = "analysis/" + analysisName + "/recommendation/" + UUID.randomUUID();
    var alert = "alerts/" + alertDto.alertName;

    return RecommendationInfo.newBuilder()
        .setRecommendation(recommendationName)
        .setAlert(alert)
        .setValue(getRecommendation(alertDto, recommendationName))
        .setMetadata(getMockMetadata(alertDto, recommendationName))
        .build();
  }

  private Recommendation getRecommendation(AlertDto alertDto, String recommendationName) {
    return Recommendation.newBuilder()
        .setName(recommendationName)
        .setAlert(alertDto.alertName)
        .setCreateTime(TimeStampUtil.fromOffsetDateTime(OffsetDateTime.now()))
        .setRecommendedAction(RecommendedAction.getRandom())
        .setRecommendationComment("recommendationComment/mock")
        .build();
  }

  private RecommendationMetadata getMockMetadata(AlertDto alertDto, String recommendationName) {
    var metadataName = recommendationName + "/metadata";
    return RecommendationMetadata.newBuilder()
        .setName(metadataName)
        .setAlert(alertDto.alertName)
        .addAllMatches(getMatchMetadata(alertDto.id))
        .build();
  }

  private List<MatchMetadata> getMatchMetadata(Integer alertId) {
    var namedParameters = new MapSqlParameterSource()
        .addValue("alertId", alertId);
    var matchesFromDb = jdbcTemplate.queryForList(
        """
            SELECT name
            FROM core_bridge_matches
            WHERE alert_id = :alertId
            """,
        namedParameters, String.class);

    return matchesFromDb.stream()
        .map(matchName ->
            MatchMetadata.newBuilder()
                .setMatch(matchName)
                .setSolution("SOLUTION_POTENTIAL_TRUE_POSITIVE")
                .setReason(getReason())
                .putAllCategories(Map.of("mockCategory", "someCategory"))
                .putAllFeatures(Map.of("mockFeature", getMockFeatureMetadata()))
                .build())
        .toList();
  }

  private Struct getReason() {
    return Struct.newBuilder()
        .putAllFields(Map.of(
            "step", getStructValue("steps/II-ab56-4576-b653-1cdceb2d25e7"),
            "policy", getStructValue("policies/5afc2f12-85c0-4fb3-992e-1552ac843ceb"),
            "feature_vector_signature", getStructValue("J4VGkp1+FaNsaGDtBXgQsWpUYDo=")))
        .build();
  }

  private FeatureMetadata getMockFeatureMetadata() {
    var reason = Struct.newBuilder()
        .putAllFields(Map.of("mockReason", getStructValue("someReason")))
        .build();

    return FeatureMetadata.newBuilder()
        .setSolution("solution/mock")
        .setAgentConfig("agentConfig/mock")
        .setReason(reason)
        .build();
  }

  private Value getStructValue(String value) {
    return Value.newBuilder()
        .setStringValue(value)
        .build();
  }

  private enum RecommendedAction {
    ACTION_INVESTIGATE,
    ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE,
    ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE,
    ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE,
    ACTION_FALSE_POSITIVE,
    ACTION_POTENTIAL_TRUE_POSITIVE;

    private static final Random RANDOM = new Random();

    static String getRandom() {
      var actions = List.of(RecommendedAction.values());
      var length = RecommendedAction.values().length;
      return actions.get(RANDOM.nextInt(length)).name();
    }
  }

  private record AlertDto(Integer id, String alertId, String alertName) {}
}

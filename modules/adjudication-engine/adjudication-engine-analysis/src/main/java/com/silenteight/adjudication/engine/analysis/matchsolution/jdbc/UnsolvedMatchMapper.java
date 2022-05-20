package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.Category;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.Feature;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.UnsolvedMatch;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import static java.util.stream.Collectors.toUnmodifiableList;

@Slf4j
final class UnsolvedMatchMapper implements RowMapper<UnsolvedMatch> {

  private static final ObjectMapper MAPPER = JsonConversionHelper.INSTANCE.objectMapper();

  @Nullable
  @Override
  public UnsolvedMatch mapRow(ResultSet rs, int rowNum) throws SQLException {
    var alertId = rs.getObject(1, Long.class);
    var matchId = rs.getObject(2, Long.class);
    var clientMatchIdentifier = rs.getString(3);
    var categoryNames = rs.getArray(4);
    var categoryValues = rs.getArray(5);
    var featureNames = rs.getArray(6);
    var featureValues = rs.getArray(7);
    var featureReasons = rs.getArray(8);
    var agentConfigs = rs.getArray(9);

    var areIdColumnsValid =
        isNotNull("alert_id", alertId, rowNum) &&
            isNotNull("match_id", matchId, rowNum) &&
            isNotNull("client_match_identifier", clientMatchIdentifier, rowNum);

    var areCategoryColumnsValid =
        isExpectedArrayBaseType(categoryNames, "_category_names", Types.VARCHAR, rowNum) &&
            isExpectedArrayBaseType(categoryValues, "_category_values", Types.VARCHAR, rowNum);

    var areFeatureColumnsValid =
        isExpectedArrayBaseType(featureNames, "_feature_names", Types.VARCHAR, rowNum) &&
            isExpectedArrayBaseType(featureValues, "_feature_values", Types.VARCHAR, rowNum) &&
            isExpectedArrayBaseType(featureReasons, "_feature_reasons", Types.OTHER, rowNum) &&
            isExpectedArrayBaseType(agentConfigs, "_agent_configs", Types.VARCHAR, rowNum);

    if (!areIdColumnsValid || !areCategoryColumnsValid || !areFeatureColumnsValid) {
      return null;
    }

    return UnsolvedMatch.builder()
        .alertId(alertId)
        .matchId(matchId)
        .clientMatchIdentifier(clientMatchIdentifier)
        .categories(mapCategories(
            (String[]) categoryNames.getArray(),
            (String[]) categoryValues.getArray()))
        .features(mapFeatures(
            (String[]) featureNames.getArray(),
            (String[]) featureValues.getArray(),
            (String[]) featureReasons.getArray(),
            (String[]) agentConfigs.getArray()))
        .build();
  }

  private static boolean isNotNull(String name, @Nullable Object value, int rowNum) {
    if (value == null) {
      log.warn("Row with NULL {}: rowNum={}", name, rowNum);
      return false;
    }
    return true;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private static boolean isExpectedArrayBaseType(
      @Nullable Array arrayColumn, String columnName, int expectedType, int rowNum) throws
      SQLException {

    if (arrayColumn == null) {
      log.warn("Row with NULL {}: rowNum={}", columnName, rowNum);
      return false;
    }

    if (arrayColumn.getBaseType() != expectedType) {
      log.warn("Column {} has invalid array base type: baseType={}, rowNum={}",
          columnName, arrayColumn.getBaseTypeName(), rowNum);
      return false;
    }

    return true;
  }

  private static List<Category> mapCategories(String[] categoryNames, String[] categoryValues) {
    Preconditions.checkArgument(
        categoryNames.length == categoryValues.length,
        "Category names and values should have the same size");

    return IntStream.range(0, categoryNames.length)
        .mapToObj(idx -> new Category(categoryNames[idx], categoryValues[idx]))
        .collect(toUnmodifiableList());
  }

  private static List<Feature> mapFeatures(
      String[] featureNames,
      String[] featureValues,
      String[] featureReasons,
      String[] agentConfigs) {

    Preconditions.checkArgument(
        Stream.of(featureValues.length, featureReasons.length, agentConfigs.length)
            .allMatch(l -> l == featureNames.length),
        "Feature names, values and reasons should have the same size");

    return IntStream.range(0, featureNames.length)
        .mapToObj(idx -> Feature.builder()
            .name(featureNames[idx])
            .value(featureValues[idx])
            .reason(readReasonNode(featureReasons[idx]))
            .agentConfig(agentConfigs[idx])
            .build())
        .collect(toUnmodifiableList());
  }

  private static ObjectNode readReasonNode(String featureReason) {
    try {
      return MAPPER.readValue(featureReason, ObjectNode.class);
    } catch (JsonProcessingException e) {
      throw new FeatureReasonJsonNodeReadException(e);
    }
  }

  static class FeatureReasonJsonNodeReadException extends RuntimeException {

    private static final long serialVersionUID = -2841087665480518098L;

    FeatureReasonJsonNodeReadException(Throwable cause) {
      super(cause);
    }
  }
}

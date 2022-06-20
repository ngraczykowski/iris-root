/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing;

import lombok.NonNull;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.SourceApplicationValues;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.CbsAlertRecommendation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;

import java.sql.Types;
import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

public class StandardRecomFunctionExecutorService implements RecomFunctionExecutorService {

  private static final String TEMPLATE_VARIABLE = ":recomFunctionName";

  private final JdbcTemplate jdbcTemplate;
  private final SourceApplicationValues sourceApplicationValues;
  private final String queryRecomFunction;

  public StandardRecomFunctionExecutorService(
      @NonNull String recomFunctionName,
      @NonNull JdbcTemplate jdbcTemplate,
      @NonNull SourceApplicationValues sourceApplicationValues) {
    this.jdbcTemplate = jdbcTemplate;
    this.sourceApplicationValues = sourceApplicationValues;
    this.queryRecomFunction = prepareQuery(recomFunctionName);
  }

  private static String prepareQuery(String recomFunctionName) {
    return getQueryTemplate().replace(TEMPLATE_VARIABLE, recomFunctionName);
  }

  private static String getQueryTemplate() {
    return String.format("SELECT %s(?, ?, ?, ?, ?, ?, ?, ?) FROM dual", TEMPLATE_VARIABLE);
  }

  @Override
  public String execute(
      @NonNull CbsAlertRecommendation alertRecommendation) {
    Object[] parameters = createParameters(alertRecommendation);

    return requireNonNull(
        jdbcTemplate.queryForObject(queryRecomFunction, parameters, String.class));
  }

  @Nonnull
  private Object[] createParameters(CbsAlertRecommendation alertRecommendation) {
    return new Object[] {
        sourceApplicationValues.getSourceApplicationValue(alertRecommendation.isWatchlistLevel()),
        alertRecommendation.getAlertExternalId(),
        alertRecommendation.getBatchId(),
        alertRecommendation.getHitWatchlistId(),
        alertRecommendation.getHitRecommendedStatus(),
        createClob(alertRecommendation.getHitRecommendedComments()),
        alertRecommendation.getListRecommendedStatus(),
        createClob(alertRecommendation.getListRecommendedComments())
    };
  }

  private static SqlParameterValue createClob(String text) {
    return new SqlParameterValue(Types.CLOB, new SqlLobValue(text));
  }
}

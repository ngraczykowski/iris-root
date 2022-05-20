package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;

import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
final class AlertRecommendationExtractor implements ResultSetExtractor<Integer> {

  private final Consumer<AlertRecommendation> consumer;

  @Override
  public Integer extractData(ResultSet rs) throws SQLException {
    var rowNum = 0;

    while (rs.next()) {
      var alertRecommendation = AlertRecommendationMapper.INSTANCE.mapRow(rs, ++rowNum);

      // NOTE(ahaczewski): Consuming recommendation can fail, i.e., the consumer can
      //  fail and throw an exception. There are three ways to handle that situation:
      //    - pass the exception up the stack,
      //    - log and ignore the exception, continuing consuming records from the result set,
      //    - log and ignore the exception, but stopping processing records.
      //  Now the third option is implemented, as callbacks shouldn't affect the calling code.
      if (!consumeRecommendation(alertRecommendation))
        break;
    }

    return rowNum;
  }

  /**
   * Passes alert recommendation to consumer, returning whether the recommendation was successfully
   * consumed.
   *
   * @param alertRecommendation
   *     the alert recommendation
   *
   * @return true when there was no exception during consumption, false when exception was thrown
   */
  private boolean consumeRecommendation(AlertRecommendation alertRecommendation) {
    try {
      consumer.accept(alertRecommendation);
    } catch (Exception e) {
      log.warn("Alert recommendation callback failed, stopping", e);
      return false;
    }

    return true;
  }
}

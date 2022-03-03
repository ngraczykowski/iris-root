package com.silenteight.customerbridge.common.recommendation;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.batch.DateConverter;
import com.silenteight.customerbridge.common.config.FetcherConfiguration;

import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@RequiredArgsConstructor
@Slf4j
class DiscriminatorFetcher {

  @Language("Oracle")
  private static final String DISCRIMINATOR_QUERY =
      "SELECT R.FILTERED, D.DECISION_DATE"
          + "      FROM :dbRelationName R"
          + "      LEFT JOIN FFF_DECISIONS D ON D.SYSTEM_ID = R.SYSTEM_ID AND D.TYPE IN (0, 12) AND"
          + "                                   D.OPERATOR IN ('FSK', 'FFFFEED')"
          + "      WHERE R.SYSTEM_ID = ?"
          + "      ORDER BY D.DECISION_DATE DESC";

  private final DataSource externalDataSource;
  private final FetcherConfiguration configuration;
  private final DateConverter dateConverter;

  Optional<String> fetch(@NonNull String systemId) {
    return fetchRecord(systemId)
        .map(this::getDiscriminatorValue)
        .orElseThrow(DiscriminatorNotFoundException::new);
  }

  private Optional<String> getDiscriminatorValue(DiscriminatorRow row) {
    String discriminatorValue = row.hasDecisionDate() ? row.getDecisionDate() : row.getFiltered();
    return dateConverter.convert(discriminatorValue).map(String::valueOf);
  }

  private Optional<DiscriminatorRow> fetchRecord(String systemId) {
    try (
        Connection connection = externalDataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(prepareQuery())) {
      statement.setFetchSize(1);
      statement.setString(1, systemId);
      statement.setQueryTimeout(configuration.getTimeout());
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next())
          return of(mapRow(resultSet));
      }
    } catch (SQLException ex) {
      log.error("Discriminator cannot be found for systemId={}", systemId, ex);
    }
    return Optional.empty();
  }

  private DiscriminatorRow mapRow(ResultSet resultSet) throws SQLException {
    return new DiscriminatorRow(
        resultSet.getString("DECISION_DATE"),
        resultSet.getString("FILTERED"));
  }

  private String prepareQuery() {
    return DISCRIMINATOR_QUERY.replace(":dbRelationName", configuration.getDbRelationName());
  }

  static class DiscriminatorNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -811047401348460908L;
  }

  @Data
  private static class DiscriminatorRow {

    private final String decisionDate;
    private final String filtered;

    boolean hasDecisionDate() {
      return isNotEmpty(decisionDate);
    }
  }
}

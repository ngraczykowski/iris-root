package com.silenteight.customerbridge.common.recommendation;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

import static com.silenteight.customerbridge.common.indicator.RecordSignCreator.fromSourceDetails;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@RequiredArgsConstructor
@Slf4j
class SystemIdFinder {

  private final FinderConfiguration configuration;

  @Language("Oracle")
  private static final String QUERY =
      "SELECT R.SYSTEM_ID, R.RECORD FROM :dbRelationName R WHERE R.UNIT = ? and R.DB_ACCOUNT = ?";

  Optional<String> find(@NonNull AlertDescriptorDto request) {
    return fetchRecordCandidates(request)
        .stream()
        .filter(r -> hasSameRecordDetails(r.getRecord(), request.getRecordDetails()))
        .map(AlertInfo::getSystemId)
        .findFirst();
  }

  private List<AlertInfo> fetchRecordCandidates(AlertDescriptorDto request) {
    String query = prepareQuery();
    try (
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {

      preparedStatement.setString(1, request.getUnit());
      preparedStatement.setString(2, request.getAccount());
      return getAlertRecords(preparedStatement);
    } catch (SQLException ex) {
      log.error("Cannot fetch record candidates", ex);
      return emptyList();
    }
  }

  private List<AlertInfo> getAlertRecords(PreparedStatement statement) throws SQLException {
    List<AlertInfo> result = new ArrayList<>();

    try (ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        AlertInfo alertInfo = AlertInfo.builder()
            .systemId(resultSet.getString("system_id"))
            .record(resultSet.getString("record"))
            .build();
        result.add(alertInfo);
      }
    } catch (SQLTimeoutException e) {
      log.error("Timeout on fetchRecordCandidates after: {} seconds",
          configuration.getQueryTimeout(), e);
      return emptyList();
    }
    return result;
  }

  private static boolean hasSameRecordDetails(String dbRecordDetails, String recordDetails) {
    if (isEmpty(dbRecordDetails))
      return false;

    return fromSourceDetails(dbRecordDetails).equals(fromSourceDetails(recordDetails));
  }

  private Connection getConnection() throws SQLException {
    return configuration.getDataSource().getConnection();
  }

  private String prepareQuery() {
    return QUERY.replace(":dbRelationName", configuration.getDbRelationName());
  }

  @Getter
  @Value(staticConstructor = "of")
  static class FinderConfiguration {

    private final String dbRelationName;
    private final DataSource dataSource;
    private final int queryTimeout;
  }

  @Builder
  @Getter
  private static class AlertInfo {

    private final String systemId;
    private final String record;
  }
}

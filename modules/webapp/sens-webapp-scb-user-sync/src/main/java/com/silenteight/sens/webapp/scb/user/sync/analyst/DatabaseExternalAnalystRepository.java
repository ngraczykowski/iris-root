package com.silenteight.sens.webapp.scb.user.sync.analyst;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.scb.user.sync.analyst.dto.Analyst;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static java.lang.String.format;

@Slf4j
class DatabaseExternalAnalystRepository implements ExternalAnalystRepository {

  private static final String QUERY = "SELECT DISTINCT %s, %s FROM %s WHERE %s IS NOT NULL";
  private static final String USER_NAME_COLUMN_NAME = "login";
  private static final String DISPLAY_NAME_COLUMN_NAME = "description";

  private final JdbcTemplate jdbcTemplate;
  private final String activeUsersQuery;

  DatabaseExternalAnalystRepository(
      @NonNull String userDbRelationName, @NonNull JdbcTemplate jdbcTemplate) {

    this.jdbcTemplate = jdbcTemplate;
    activeUsersQuery = format(
        QUERY,
        USER_NAME_COLUMN_NAME,
        DISPLAY_NAME_COLUMN_NAME,
        userDbRelationName,
        USER_NAME_COLUMN_NAME);
  }

  @Override
  public Collection<Analyst> list() {
    log.info(USER_MANAGEMENT, "Querying Analysts. query={}", activeUsersQuery);

    List<Analyst> analysts =
        jdbcTemplate.query(activeUsersQuery, (rs, rowNum) -> createAnalyst(rs));

    log.info(USER_MANAGEMENT, "Found {} Analysts", analysts.size());

    return analysts;
  }

  private static Analyst createAnalyst(ResultSet resultSet) throws SQLException {
    return Analyst
        .builder()
        .userName(resultSet.getString(USER_NAME_COLUMN_NAME))
        .displayName(resultSet.getString(DISPLAY_NAME_COLUMN_NAME))
        .build();
  }
}

package com.silenteight.payments.bridge.svb.newlearning.step.unregistered;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import javax.sql.DataSource;

@RequiredArgsConstructor
@Slf4j
abstract class BaseCompositeFetcher<I, O> {

  private final DataSource dataSource;

  public O fetchWithConnection(I fkcoIds) {
    try {
      return fetchInTransaction(fkcoIds);
    } catch (SQLException e) {
      log.error("Cannot fetch Composite data!", e);
      throw new FetchingComposeDataException(e);
    }
  }

  protected abstract O fetchWithConnection(Connection connection, I output);

  O fetchInTransaction(I output) throws SQLException {
    O result;
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      result = fetchWithConnection(connection, output);
    }
    return result;
  }

  protected static String prepareQuery(String query, Collection<Long> ids) {
    return String.format(query, String.join(",", Collections.nCopies(ids.size(), "?")));
  }

  protected static void setQueryParameters(PreparedStatement stat, Collection<Long> alertIds) throws
      SQLException {
    int systemIdIdx = 1;
    for (var id : alertIds) {
      stat.setLong(systemIdIdx, id);
      systemIdIdx++;
    }
  }
}

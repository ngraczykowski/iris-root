package com.silenteight.payments.bridge.svb.newlearning.step.unregistered;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;

import org.intellij.lang.annotations.Language;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Service
class AlertCompositeFetcher {

  private final DataSource dataSource;

  @Language("PostgreSQL")
  private static final String HITS_QUERY =
      "SELECT fkco_messages, learning_hit_id FROM pb_learning_hit WHERE fkco_messages IN (%s)";

  public List<AlertComposite> fetch(List<Long> fkcoIds) {
    try {
      return fetchInTransaction(fkcoIds);
    } catch (SQLException e) {
      log.error("Cannot fetch AlertComposite data!", e);
      return emptyList();
    }
  }

  private List<AlertComposite> fetchInTransaction(List<Long> fkcoIds) throws SQLException {
    List<AlertComposite> result;
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      result = fetch(connection, fkcoIds);

      connection.commit();
    }
    return result;
  }

  private static List<AlertComposite> fetch(Connection connection, List<Long> fkcoIds) {
    var hits = fetchHits(connection, fkcoIds);
    return fkcoIds
        .stream()
        .map(alertId -> AlertComposite.builder().fkcoId(alertId).hits(hits.get(alertId)).build())
        .collect(
            Collectors.toList());
  }

  private static Map<Long, List<HitComposite>> fetchHits(
      Connection connection, List<Long> fkcoIds) {
    log.debug("Fetching hits for alerts = {}", fkcoIds);

    var preparedQuery = prepareQuery(HITS_QUERY, fkcoIds);
    var result = new HashMap<Long, List<HitComposite>>();

    try (PreparedStatement statement = connection.prepareStatement(preparedQuery)) {
      setQueryParameters(statement, fkcoIds);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          var hit = CompositeHitRowMapper.mapRow(resultSet);
          var fkcoId = resultSet.getLong("fkco_messages");

          addHit(result, hit, fkcoId);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return result;
  }

  private static void addHit(
      HashMap<Long, List<HitComposite>> result, HitComposite hit, long fkcoId) {
    if (result.containsKey(fkcoId)) {
      result.get(fkcoId).add(hit);
      return;
    }
    var hits = new ArrayList<HitComposite>();
    hits.add(hit);
    result.put(fkcoId, hits);
  }

  private static String prepareQuery(String query, Collection<Long> ids) {
    return String.format(query, String.join(",", Collections.nCopies(ids.size(), "?")));
  }

  private static void setQueryParameters(PreparedStatement stat, Collection<Long> alertIds) throws
      SQLException {
    int systemIdIdx = 1;
    for (var id : alertIds) {
      stat.setLong(systemIdIdx, id);
      systemIdIdx++;
    }
  }
}

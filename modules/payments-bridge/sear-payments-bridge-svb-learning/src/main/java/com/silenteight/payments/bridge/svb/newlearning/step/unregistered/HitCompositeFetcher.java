package com.silenteight.payments.bridge.svb.newlearning.step.unregistered;

import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;

import org.intellij.lang.annotations.Language;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

@Service
class HitCompositeFetcher extends BaseCompositeFetcher<List<Long>, Map<Long, List<HitComposite>>> {

  @Language("PostgreSQL")
  private static final String HITS_QUERY =
      "SELECT fkco_messages, learning_hit_id FROM pb_learning_hit WHERE fkco_messages IN (%s)";

  public HitCompositeFetcher(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  protected Map<Long, List<HitComposite>> fetchWithConnection(
      Connection connection, List<Long> fkcoIds) {
    var preparedQuery = prepareQuery(HITS_QUERY, fkcoIds);

    try (PreparedStatement statement = connection.prepareStatement(preparedQuery)) {
      setQueryParameters(statement, fkcoIds);
      try (ResultSet resultSet = statement.executeQuery()) {
        return createHits(resultSet);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static Map<Long, List<HitComposite>> createHits(ResultSet resultSet) throws SQLException {
    var result = new HashMap<Long, List<HitComposite>>();
    while (resultSet.next()) {
      var hit = mapRow(resultSet);
      var fkcoId = resultSet.getLong("fkco_messages");

      if (result.containsKey(fkcoId)) {
        result.get(fkcoId).add(hit);
        continue;
      }

      var hits = new LinkedList<HitComposite>();
      hits.add(hit);
      result.put(fkcoId, hits);
    }
    return result;
  }

  private static HitComposite mapRow(ResultSet resultSet) throws SQLException {
    return HitComposite.builder()
        .hitId(resultSet.getLong("learning_hit_id"))
        .build();
  }
}

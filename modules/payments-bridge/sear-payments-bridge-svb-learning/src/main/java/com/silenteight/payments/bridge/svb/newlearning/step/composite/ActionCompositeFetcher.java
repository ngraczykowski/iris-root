package com.silenteight.payments.bridge.svb.newlearning.step.composite;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.app.LearningProperties;
import com.silenteight.payments.bridge.svb.newlearning.domain.ActionComposite;
import com.silenteight.payments.bridge.svb.newlearning.step.composite.exception.FetchingComposeDataException;

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

import static com.silenteight.payments.bridge.svb.newlearning.step.composite.ActionCompositeRowMapper.mapRow;

@Service
@Slf4j
class ActionCompositeFetcher
    extends BaseCompositeFetcher<List<Long>, Map<Long, List<ActionComposite>>> {

  private final LearningProperties properties;

  @Language("PostgreSQL")
  private static final String ACTIONS_QUERY =
      "SELECT fkco_messages, learning_action_id, fkco_d_action_datetime, fkco_v_status_name"
          + " ,fkco_v_status_behavior "
          + " FROM pb_learning_action WHERE fkco_messages IN (%s)"
          + " ORDER BY fkco_d_action_datetime ASC";

  public ActionCompositeFetcher(DataSource dataSource, LearningProperties properties) {
    super(dataSource);
    this.properties = properties;
  }

  @Override
  protected Map<Long, List<ActionComposite>> fetchWithConnection(
      Connection connection, List<Long> fkcoIds) {
    var preparedQuery = prepareQuery(ACTIONS_QUERY, fkcoIds);

    try (PreparedStatement statement = connection.prepareStatement(preparedQuery)) {
      setQueryParameters(statement, fkcoIds);
      try (ResultSet resultSet = statement.executeQuery()) {
        return createActions(resultSet, properties.getTimeZone());
      }
    } catch (SQLException e) {
      log.error("Failed do fetch action details: {}", e.getMessage());
      throw new FetchingComposeDataException(e);
    }
  }

  private static Map<Long, List<ActionComposite>> createActions(
      ResultSet resultSet, String timezone) throws
      SQLException {
    var result = new HashMap<Long, List<ActionComposite>>();
    while (resultSet.next()) {
      var action = mapRow(resultSet, timezone);
      var fkcoId = resultSet.getLong("fkco_messages");

      if (result.containsKey(fkcoId)) {
        result.get(fkcoId).add(action);
        continue;
      }

      var actions = new LinkedList<ActionComposite>();
      actions.add(action);
      result.put(fkcoId, actions);
    }
    return result;
  }
}

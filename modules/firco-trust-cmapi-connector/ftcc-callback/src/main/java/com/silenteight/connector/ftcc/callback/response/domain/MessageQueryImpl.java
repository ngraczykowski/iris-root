package com.silenteight.connector.ftcc.callback.response.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static java.util.UUID.fromString;

@Component
@RequiredArgsConstructor
@Slf4j
class MessageQueryImpl implements MessageQuery {

  @Language("PostgreSQL")
  private static final String SELECT_QUERY = "select fm.message_id, \n"
      + "       fm.payload -> 'Message' -> 'Unit'         as Unit,\n"
      + "       fm.payload -> 'Message' -> 'BusinessUnit' as BusinessUnit,\n"
      + "       fm.payload -> 'Message' -> 'MessageID'    as MessageID,\n"
      + "       fm.payload -> 'Message' -> 'SystemID'     as SystemID,\n"
      + "       fm.payload -> 'Message' -> 'LastOperator' as LastOperator,\n"
      + "       fm.payload -> 'Message' -> 'NextStatuses' as NextStatuses,\n"
      + "       fm.payload -> 'Message' -> 'CurrentStatus' as CurrentStatus\n"
      + "from ftcc_message fm \n"
      + "where fm.batch_id=:batchId";


  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;

  @Override
  public List<MessageEntity> findByBatchId(String batchId) {
    return jdbcTemplate.query(SELECT_QUERY, Map.of("batchId", fromString(batchId)),
        (rs, rowNum) -> deserialize(batchId, rs));
  }

  private MessageEntity deserialize(String batchId, ResultSet rs) throws SQLException {
    return MessageEntity.builder()
        .id(fromString(rs.getString("message_id")))
        .batchId(batchId)
        .unit(rs.getString("Unit"))
        .businessUnit(rs.getString("BusinessUnit"))
        .messageID(rs.getString("MessageID"))
        .systemID(rs.getString("SystemID"))
        .lastOperator(rs.getString("LastOperator"))
        .nextStatuses(deserializeNextStatuses(rs))
        .currentStatus(deserializeCurrentStatuse(rs))
        .build();
  }

  private List<NextStatusEntity> deserializeNextStatuses(ResultSet rs) throws SQLException {
    try {
      var matchListType = this.objectMapper
          .getTypeFactory()
          .constructCollectionType(List.class, NextStatusEntity.class);
      return objectMapper.readValue(rs.getString("NextStatuses"), matchListType);
    } catch (JsonProcessingException e) {
      log.error("Error while parsing 'NextStatuses'", e);
      throw new RuntimeException(e);
    }
  }

  private StatusEntity deserializeCurrentStatuse(ResultSet rs) throws SQLException {
    try {
      var matchListType = this.objectMapper
          .getTypeFactory()
          .constructType(StatusEntity.class);
      return objectMapper.readValue(rs.getString("CurrentStatus"), matchListType);
    } catch (JsonProcessingException e) {
      log.error("Error while parsing 'CurrentStatus'", e);
      throw new RuntimeException(e);
    }
  }
}

package com.silenteight.connector.ftcc.request.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.request.details.MessageDetailsQuery;
import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto;
import com.silenteight.connector.ftcc.request.details.dto.NextStatusDto;
import com.silenteight.connector.ftcc.request.details.dto.StatusDto;
import com.silenteight.connector.ftcc.request.domain.exception.MessageNotFoundException;
import com.silenteight.connector.ftcc.request.get.MessageByIdsQuery;
import com.silenteight.connector.ftcc.request.get.dto.MessageDto;
import com.silenteight.connector.ftcc.request.status.MessageCurrentStatusQuery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.UUID.fromString;

@RequiredArgsConstructor
@Slf4j
class MessageQuery implements MessageByIdsQuery, MessageDetailsQuery, MessageCurrentStatusQuery {

  @Language("PostgreSQL")
  private static final String SELECT_DETAILS_QUERY = "SELECT fm.message_id,"
      + "       fm.payload -> 'Message' ->> 'Unit'         as Unit,"
      + "       fm.payload -> 'Message' ->> 'BusinessUnit' as BusinessUnit,"
      + "       fm.payload -> 'Message' ->> 'MessageID'    as MessageID,"
      + "       fm.payload -> 'Message' ->> 'SystemID'     as SystemID,"
      + "       fm.payload -> 'Message' ->> 'LastOperator' as LastOperator,"
      + "       fm.payload -> 'Message' ->> 'NextStatuses' as NextStatuses,"
      + "       fm.payload -> 'Message' ->> 'CurrentStatus' as CurrentStatus"
      + " FROM ftcc_message fm"
      + " WHERE fm.batch_id=:batchId";

  @Language("PostgreSQL")
  private static final String SELECT_CURRENT_STATUS_NAME_QUERY = "SELECT"
      + "       fm.payload -> 'Message' -> 'CurrentStatus' ->> 'Name' as CurrentStatusName"
      + " FROM ftcc_message fm"
      + " WHERE fm.batch_id=:batchId"
      + "       AND fm.message_id=:messageId";

  @NonNull
  private final MessageRepository messageRepository;
  @NonNull
  private final NamedParameterJdbcTemplate jdbcTemplate;
  @NonNull
  private final ObjectMapper objectMapper;

  @Override
  public MessageDto get(@NonNull UUID batchId, @NonNull UUID messageId) {
    return messageRepository
        .findByBatchIdAndMessageId(batchId, messageId)
        .map(MessageEntity::toDto)
        .orElseThrow(() -> new MessageNotFoundException(batchId, messageId));
  }

  @Override
  public List<MessageDetailsDto> details(@NonNull UUID batchId) {
    return jdbcTemplate.query(SELECT_DETAILS_QUERY, Map.of("batchId", batchId),
        (resultSet, rowNum) -> deserialize(batchId, resultSet));
  }

  private MessageDetailsDto deserialize(UUID batchId, ResultSet resultSet) throws SQLException {
    return MessageDetailsDto.builder()
        .id(fromString(resultSet.getString("message_id")))
        .batchId(batchId)
        .unit(resultSet.getString("Unit"))
        .businessUnit(resultSet.getString("BusinessUnit"))
        .messageID(resultSet.getString("MessageID"))
        .systemID(resultSet.getString("SystemID"))
        .lastOperator(resultSet.getString("LastOperator"))
        .nextStatuses(deserializeNextStatuses(resultSet))
        .currentStatus(deserializeCurrentStatus(resultSet))
        .build();
  }

  private List<NextStatusDto> deserializeNextStatuses(ResultSet resultSet) throws SQLException {
    try {
      var matchListType = objectMapper
          .getTypeFactory()
          .constructCollectionType(List.class, NextStatusDto.class);
      return objectMapper.readValue(resultSet.getString("NextStatuses"), matchListType);
    } catch (JsonProcessingException e) {
      log.error("Error while parsing 'NextStatuses'", e);
      throw new RuntimeException(e);
    }
  }

  private StatusDto deserializeCurrentStatus(ResultSet resultSet) throws SQLException {
    try {
      var matchListType = objectMapper
          .getTypeFactory()
          .constructType(StatusDto.class);
      return objectMapper.readValue(resultSet.getString("CurrentStatus"), matchListType);
    } catch (JsonProcessingException e) {
      log.error("Error while parsing 'CurrentStatus'", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public String currentStatus(@NonNull UUID batchId, @NonNull UUID messageId) {
    return jdbcTemplate.query(
        SELECT_CURRENT_STATUS_NAME_QUERY,
        Map.of("batchId", batchId, "messageId", messageId),
        (ResultSet resultSet) -> this.deserializeCurrentStatusName(resultSet, batchId, messageId));
  }

  private String deserializeCurrentStatusName(
      ResultSet resultSet, UUID batchId, UUID messageId) throws SQLException {

    try {
      if (!resultSet.next())
        throw new MessageNotFoundException(batchId, messageId);

      var matchListType = objectMapper
          .getTypeFactory()
          .constructType(String.class);
      return objectMapper.readValue(resultSet.getString("CurrentStatusName"), matchListType);
    } catch (JsonProcessingException e) {
      log.error("Error while parsing 'CurrentStatusName'", e);
      throw new RuntimeException(e);
    }
  }
}

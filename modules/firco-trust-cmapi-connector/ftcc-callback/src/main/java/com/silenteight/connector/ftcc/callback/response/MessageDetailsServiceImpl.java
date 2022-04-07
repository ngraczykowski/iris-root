package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.exception.NonRecoverableCallbackException;
import com.silenteight.connector.ftcc.callback.exception.RecoverableCallbackException;
import com.silenteight.connector.ftcc.common.resource.BatchResource;
import com.silenteight.connector.ftcc.request.details.MessageDetailsQuery;
import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.RecoverableDataAccessException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Slf4j
class MessageDetailsServiceImpl implements MessageDetailsService {

  private final MessageDetailsQuery messageDetailsQuery;

  @Override
  public Map<UUID, MessageDetailsDto> messages(String batchName) {
    UUID batchId = BatchResource.fromResourceName(batchName);
    try {
      return map(messageDetailsQuery.details(batchId));
    } catch (RecoverableDataAccessException e) {
      log(batchId, e);
      throw new RecoverableCallbackException(e);
    } catch (DataAccessException e) {
      log(batchId, e);
      throw new NonRecoverableCallbackException(e);
    }
  }

  @Override
  public MessageDetailsDto messageFrom(Map<UUID, MessageDetailsDto> messages, String messageName) {
    try {
      return MessageDetailsService.super.messageFrom(messages, messageName);
    } catch (Exception e) {
      log.error("Can't Access Data with messageName={}", messageName, e);
      throw new NonRecoverableCallbackException(e);
    }
  }

  private static Map<UUID, MessageDetailsDto> map(List<MessageDetailsDto> list) {
    return list
        .stream()
        .collect(toMap(MessageDetailsDto::getId, identity()));
  }

  private static void log(UUID batchId, DataAccessException e) {
    log.error("Can't Access Data with batchId={}", batchId, e);
  }
}

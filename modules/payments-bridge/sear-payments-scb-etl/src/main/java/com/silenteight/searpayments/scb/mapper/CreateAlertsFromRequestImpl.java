package com.silenteight.searpayments.scb.mapper;

import com.silenteight.sep.base.aspects.logging.LogContext;
import com.silenteight.sep.base.aspects.logging.LogContext.LogContextAction;
import com.silenteight.searpayments.scb.domain.Alert;
import com.silenteight.searpayments.bridge.dto.input.RequestBodyDto;
import com.silenteight.searpayments.bridge.dto.input.RequestDto;
import com.silenteight.searpayments.bridge.dto.input.RequestMessageDto;
import com.silenteight.searpayments.bridge.dto.input.RequestSendMessageDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@Slf4j
//@RequiredArgsConstructor
//class CreateAlertsFromRequestImpl implements CreateAlertsFromRequest {
//
//  @NonNull private final RequestDto requestDto;
//  @NonNull private final CreateAlertFromMessageFactory createAlertFactory;
//
//  public List<Alert> create() {
//    return Optional.of(requestDto)
//        .map(RequestDto::getBody)
//        .map(RequestBodyDto::getMessageDto)
//        .map(RequestSendMessageDto::getMessages)
//        .map(this::processMessages)
//        .orElseThrow();
//  }
//
//  private List<Alert> processMessages(List<RequestMessageDto> messages) {
//    return messages
//        .stream()
//        .map(this::processMessage)
//        .filter(Optional::isPresent)
//        .map(Optional::get)
//        .collect(Collectors.toList());
//  }
//
//  @LogContext(LogContextAction.CLEAR_PRESERVE)
//  private Optional<Alert> processMessage(RequestMessageDto messageDto) {
//    MDC.put("systemId", messageDto.getMessage().getSystemId());
//    return createAlertFactory.create(messageDto, requestDto.getDataCenter()).create();
//  }
//}

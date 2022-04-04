package com.silenteight.connector.ftcc.request.details.dto;

import lombok.Builder;
import lombok.Value;

import com.silenteight.connector.ftcc.common.dto.input.NextStatusDto;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class MessageDetailsDto {

  UUID id;
  UUID batchId;
  List<com.silenteight.connector.ftcc.request.details.dto.NextStatusDto> nextStatuses;
  StatusDto currentStatus;

  String unit;
  String businessUnit;
  String systemID;
  String messageID;
  String lastOperator;

  public List<NextStatusDto> nextStatusesDto() {
    return nextStatuses
        .stream()
        .map(nextStatus -> new NextStatusDto(nextStatus.statusInfoDto()))
        .collect(toList());
  }
}

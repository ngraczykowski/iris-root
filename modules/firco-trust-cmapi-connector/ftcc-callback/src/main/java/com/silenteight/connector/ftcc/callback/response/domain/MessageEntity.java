package com.silenteight.connector.ftcc.callback.response.domain;

import lombok.Builder;
import lombok.Data;

import com.silenteight.connector.ftcc.common.dto.input.NextStatusDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class MessageEntity {

  private UUID id;
  private UUID batchId;
  private List<NextStatusEntity> nextStatuses;
  private StatusEntity currentStatus;

  private String unit;
  private String businessUnit;
  private String systemID;
  private String messageID;
  private String lastOperator;

  public List<NextStatusDto> nextStatusesDto() {
    return nextStatuses
        .stream()
        .map(nextStatus -> new NextStatusDto(nextStatus.statusInfoDto()))
        .collect(Collectors.toList());
  }
}

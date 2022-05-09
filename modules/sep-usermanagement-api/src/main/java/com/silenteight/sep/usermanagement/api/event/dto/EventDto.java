package com.silenteight.sep.usermanagement.api.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.sep.usermanagement.api.event.EventType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

  private EventType type;
  private String codeId;
  private String userId;
  private String userName;
  private String ipAddress;
  private long timestamp;
}

package com.silenteight.connector.ftcc.callback.newdecision;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import com.silenteight.connector.ftcc.common.dto.input.StatusInfoDto;

import static lombok.AccessLevel.PRIVATE;

@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class DestinationStatus {

  StatusInfoDto status;

  boolean valid;
}

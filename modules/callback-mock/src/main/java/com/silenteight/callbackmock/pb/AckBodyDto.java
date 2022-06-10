/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.callbackmock.pb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
@ToString
class AckBodyDto implements Serializable {

  private static final long serialVersionUID = 8305008907553975177L;

  @JsonProperty("msg_Acknowledgement")
  private final AckMessageDto messageDto;
}

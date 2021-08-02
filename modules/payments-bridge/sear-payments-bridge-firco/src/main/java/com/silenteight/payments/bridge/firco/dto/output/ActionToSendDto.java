package com.silenteight.payments.bridge.firco.dto.output;

import lombok.Data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@Data
@JsonSerialize(using = ActionToSendDtoSerializer.class)
public class ActionToSendDto implements Serializable {

  private static final long serialVersionUID = -4215114603801984462L;
}

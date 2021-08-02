package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessageDto implements Serializable {

  private static final long serialVersionUID = 4493075170429206804L;

  @JsonProperty("Message")
  @NotNull
  @Valid
  private AlertMessageDto message;
}

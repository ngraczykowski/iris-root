package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAnalystSolutionsDto implements Serializable {

  private static final long serialVersionUID = -7868232254808751898L;
  @JsonProperty("VersionTag")
  String versionTag;
  @JsonProperty("Authentication")
  RequestAuthenticationDto authentication;
  @JsonProperty("Messages")
  @Size(min = 1)
  @NotNull
  @Valid
  List<AnalystSolutionDto> solutions;
}

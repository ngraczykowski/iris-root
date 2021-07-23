package com.silenteight.serp.governance.model.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

import static com.silenteight.serp.governance.model.common.ModelResource.toResourceName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "model", "checksum" })
public class TransferredModelRootDto implements Serializable {

  private static final long serialVersionUID = -215030170844762036L;

  @NonNull
  private TransferredModelDto model;

  @NonNull
  private String checksum;

  public String toJson() {
    return JsonConversionHelper.INSTANCE.serializeToString(this);
  }

  @JsonIgnore
  public String getModelVersion() {
    return getModel().getModelVersion();
  }

  @JsonIgnore
  public String getModelName() {
    return toResourceName(getModel().getModelId());
  }
}

package com.silenteight.serp.governance.model.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

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
}

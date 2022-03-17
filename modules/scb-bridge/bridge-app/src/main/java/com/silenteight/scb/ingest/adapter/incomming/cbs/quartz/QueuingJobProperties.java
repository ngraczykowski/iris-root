package com.silenteight.scb.ingest.adapter.incomming.cbs.quartz;

import lombok.Data;

import com.silenteight.scb.ingest.adapter.incomming.common.validation.OracleRelationName;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Validated
public class QueuingJobProperties {

  private boolean ackRecords;

  @NotBlank
  private String cronExpression;

  private boolean enabled;

  @OracleRelationName
  private String hitDetailsView;

  @NotBlank
  private String name;

  @Min(1)
  @Max(10)
  private int priority;

  private boolean watchlistLevel;

  @OracleRelationName
  private String recordsView;
}

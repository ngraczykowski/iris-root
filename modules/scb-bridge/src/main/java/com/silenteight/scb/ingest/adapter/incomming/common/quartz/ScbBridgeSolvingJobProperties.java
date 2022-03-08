package com.silenteight.scb.ingest.adapter.incomming.common.quartz;

import lombok.Data;

import com.silenteight.scb.ingest.adapter.incomming.common.validation.OracleRelationName;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
class ScbBridgeSolvingJobProperties {

  @NotBlank
  private String cronExpression;

  private boolean ackRecords;

  private boolean enabled;

  @OracleRelationName
  private String dbRelationName = "SENS_V_FFF_RECORDS";

  /**
   * Name of the CBS view that exposes N/E/O flag information. The view name be empty, in which case
   * a fallback logic will be used to determine new, existing and obsolete hits.
   * <p/>
   * The standard views are "CBS_HITS_DETAILS_WL_HLPR_V" for Watchlist-level and
   * "CBS_HITS_DETAILS_ALRT_HLPR_V" for Alert-level solving.
   */
  @OracleRelationName
  private String cbsHitsDetailsHelperViewName = "";
}

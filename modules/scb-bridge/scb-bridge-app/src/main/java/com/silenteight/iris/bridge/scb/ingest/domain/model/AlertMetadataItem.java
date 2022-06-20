/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain.model;

import lombok.Value;

import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Validated
public class AlertMetadataItem {

  HeaderInfo headerInfo;
  @NotNull MatchRecords matchRecords;

  @Value
  public static class HeaderInfo {

    String currentVersionId;
    StopDescriptorsItem stopDescriptors;
  }

  @Value
  @Validated
  public static class MatchRecords {

    List<MatchRecord> records;

    @Value
    @Validated
    public static class MatchRecord {

      @NotBlank String matchId;
    }
  }
}

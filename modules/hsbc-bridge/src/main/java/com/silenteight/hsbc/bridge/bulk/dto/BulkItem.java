package com.silenteight.hsbc.bridge.bulk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.silenteight.hsbc.bridge.bulk.BulkStatus;

//create entity or immutable class
@Data
@AllArgsConstructor
public class BulkItem {
  Integer id;
  BulkStatus status;
}

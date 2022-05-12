package com.silenteight.warehouse.indexer.query.dto;

import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AlertDetailsRequest {
  @NonNull
  List<String> fields;
}

package com.silenteight.hsbc.bridge.bulk.event;

import lombok.*;

import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;

import java.util.Collection;

@Builder
@Value
public class BulkPreProcessingFinishedEvent {

  @NonNull String bulkId;
  @NonNull Collection<AlertMatchIdComposite> alertMatchIdComposites;
}

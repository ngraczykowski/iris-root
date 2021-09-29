package com.silenteight.warehouse.indexer.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.Alert;

import java.util.List;
import javax.annotation.concurrent.NotThreadSafe;
import javax.validation.constraints.NotNull;

@NotThreadSafe
@RequiredArgsConstructor
public class FixedIndexedResolver implements WriteIndexResolver {

  @NotNull
  private final String indexName;

  @Override
  public void prepareIndexNames(List<Alert> alert) {
    // do nothing
  }

  @Override
  public String getIndexName(Alert alert) {
    return indexName;
  }
}

package com.silenteight.warehouse.indexer.alert;

import com.silenteight.data.api.v1.Alert;

import java.util.List;

public interface WriteIndexResolver {

  void prepareIndexNames(List<Alert> alert);

  String getIndexName(Alert alert);
}

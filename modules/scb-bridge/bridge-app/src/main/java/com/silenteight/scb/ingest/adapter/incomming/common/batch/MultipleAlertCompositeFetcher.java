package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import java.util.List;

public interface MultipleAlertCompositeFetcher {

  List<AlertComposite> fetch(List<String> systemIds);
}

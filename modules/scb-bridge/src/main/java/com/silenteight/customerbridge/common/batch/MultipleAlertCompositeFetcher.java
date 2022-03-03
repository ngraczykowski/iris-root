package com.silenteight.customerbridge.common.batch;

import java.util.List;

public interface MultipleAlertCompositeFetcher {

  List<AlertComposite> fetch(List<String> systemIds);
}

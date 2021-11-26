package com.silenteight.adjudication.engine.analysis.pii;

import java.util.List;

public interface PiiDataAccess {

  void removePiiData(List<Long> alertsIds);
}

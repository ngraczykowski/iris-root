package com.silenteight.adjudication.engine.dataset.dataset;

import java.util.List;

public interface DatasetAlertDataAccess {

  List<Long> selectDatasetsByAlerts(List<Long> alertIds);
}

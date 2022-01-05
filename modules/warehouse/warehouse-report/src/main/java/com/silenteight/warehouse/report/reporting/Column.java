package com.silenteight.warehouse.report.reporting;

import java.util.Collection;

interface Column {

  String getName();

  Collection<String> getLabels();
}

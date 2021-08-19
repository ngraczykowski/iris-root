package com.silenteight.warehouse.report.metrics.generation;

import java.util.Collection;

interface Column {

  String getName();

  Collection<String> getLabels();
}

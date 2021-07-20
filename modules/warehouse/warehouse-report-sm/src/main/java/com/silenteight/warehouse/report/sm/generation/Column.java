package com.silenteight.warehouse.report.sm.generation;

import java.util.Collection;

interface Column {

  String getName();

  Collection<String> getLabels();
}

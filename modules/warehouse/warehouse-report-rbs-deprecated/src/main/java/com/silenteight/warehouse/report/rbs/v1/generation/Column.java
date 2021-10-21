package com.silenteight.warehouse.report.rbs.v1.generation;

import java.util.Collection;

interface Column {

  String getName();

  Collection<String> getLabels();
}

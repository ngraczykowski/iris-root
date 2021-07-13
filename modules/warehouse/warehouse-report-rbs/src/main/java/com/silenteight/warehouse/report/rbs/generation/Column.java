package com.silenteight.warehouse.report.rbs.generation;

import java.util.Collection;

interface Column {

  String getName();

  Collection<String> getLabels();
}

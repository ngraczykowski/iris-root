package com.silenteight.warehouse.indexer.query.single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AlertProvider {

  Map<String, String> getSingleAlertAttributes(List<String> fields, String discriminator);

  Collection<Map<String, String>> getMultipleAlertsAttributes(
      List<String> fields, List<String> discriminatorList);
}

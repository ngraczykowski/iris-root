package com.silenteight.warehouse.indexer.query.single;

import java.util.List;

/**
 * Service provides randomized alerts for further investigation.
 */
public interface RandomAlertService {

  /**
   * Generates sampling alerts based on the defined request.
   * @param alertsSampleRequest requested used to get limited scope of alerts
   * @return list of alert id
   */
  List<String> getRandomAlertNameByCriteria(AlertSearchCriteria alertsSampleRequest);
}

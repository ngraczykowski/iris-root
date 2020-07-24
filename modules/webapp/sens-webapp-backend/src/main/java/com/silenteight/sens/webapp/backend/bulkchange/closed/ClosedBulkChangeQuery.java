package com.silenteight.sens.webapp.backend.bulkchange.closed;

import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeDto;

import java.util.List;

public interface ClosedBulkChangeQuery {

  List<BulkChangeDto> listClosed();
}

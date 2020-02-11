package com.silenteight.sens.webapp.user.sync.analyst;

import com.silenteight.sens.webapp.common.repository.BasicInMemoryRepository;
import com.silenteight.sens.webapp.user.sync.analyst.dto.InternalAnalyst;

import java.util.List;

import static java.util.stream.Collectors.toList;

class InMemoryAnalystRepository
    extends BasicInMemoryRepository<String, InternalAnalyst>
    implements AnalystQueryRepository {

  @Override
  public List<InternalAnalyst> findAll() {
    return stream().collect(toList());
  }

  InternalAnalyst save(InternalAnalyst analyst) {
    getInternalStore().put(analyst.getUserName(), analyst);
    return analyst;
  }
}

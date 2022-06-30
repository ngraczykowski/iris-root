package com.silenteight.agent.facade.datasource;

import java.util.List;

public interface DataSourceClient<C, R> {

  List<R> getMatchInputs(C command);
}

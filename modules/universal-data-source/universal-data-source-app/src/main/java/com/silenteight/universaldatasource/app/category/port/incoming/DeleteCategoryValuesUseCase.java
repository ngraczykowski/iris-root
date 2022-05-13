package com.silenteight.universaldatasource.app.category.port.incoming;

import java.util.List;

public interface DeleteCategoryValuesUseCase {

  void delete(List<String> alerts);

}

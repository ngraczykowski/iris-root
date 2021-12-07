package com.silenteight.universaldatasource.api.library.country.v1;

import java.util.List;

public interface CountryInputClientService {

  List<BatchGetMatchCountryInputsOut> getBatchGetMatchCountryInputs(
      BatchGetMatchCountryInputsIn request);
}

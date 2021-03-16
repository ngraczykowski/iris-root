package com.silenteight.hsbc.datasource.date;

import com.silenteight.hsbc.datasource.date.dto.DateInputRequest;
import com.silenteight.hsbc.datasource.date.dto.DateInputResponse;

public class DateInputProvider {

  public DateInputResponse provideInput(DateInputRequest request) {
    return new DateInputResponse();
  }
}

package com.silenteight.sep.base.aspects.validation.business;

import com.silenteight.sep.base.aspects.validation.business.model.BusinessObject;
import com.silenteight.sep.base.aspects.validation.business.model.BusinessRequest;

import javax.validation.Valid;
import javax.validation.constraints.Size;

public class BusinessService {

  private final BusinessFinder finder = new BusinessFinder();

  @Valid
  public BusinessObject performLogic(@Valid BusinessRequest request) {
    return new BusinessObject(
        request.getRequiredText() + "/" + request.getShortText() + "@" + request.getInteger());
  }

  @Valid
  public static BusinessObject businessStaticMethod(@Valid BusinessRequest request) {
    return new BusinessObject(
        request.getRequiredText() + "/" + request.getShortText() + "@" + request.getInteger());
  }

  public void businessMethod(@Valid BusinessObject object, @Size(max = 4) String text) {
    // do something
  }

  public void businessMethod(String personIdentifier) {
    finder.find(personIdentifier);
  }

  @Valid
  public BusinessRequest createRequest(int integer, String shortText, String requiredText) {
    return BusinessRequest.builder()
        .integer(integer)
        .shortText(shortText)
        .requiredText(requiredText)
        .build();
  }
}

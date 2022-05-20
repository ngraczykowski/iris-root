package com.silenteight.sep.base.aspects.validation.business;

import com.silenteight.sep.base.aspects.validation.business.model.BusinessObjectView;

import org.hibernate.validator.constraints.pl.PESEL;

class BusinessFinder {

  BusinessObjectView find(@PESEL String pesel) {
    return new BusinessObjectView();
  }
}

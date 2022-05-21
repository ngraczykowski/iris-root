package com.silenteight.hsbc.datasource.feature.gender;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;
import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import java.util.Optional;
import javax.annotation.Nullable;

@RequiredArgsConstructor(staticName = "of")
class GenderFieldsWrapper {

  private static final String CORRECT_GENDER_FLAG = "N";

  @Nullable
  private final String gender;
  @Nullable
  private final String genderDerivedFlag;

  static GenderFieldsWrapper fromPrivateListIndividual(PrivateListIndividual indv) {
    return GenderFieldsWrapper.of(indv.getGender(), indv.getGenderDerivedFlag());
  }

  static GenderFieldsWrapper fromWorldCheckIndividual(WorldCheckIndividual indv) {
    return GenderFieldsWrapper.of(indv.getGender(), indv.getGenderDerivedFlag());
  }

  static GenderFieldsWrapper fromCustomerIndividual(CustomerIndividual indv) {
    return GenderFieldsWrapper.of(indv.getGender(), indv.getGenderDerivedFlag());
  }

  Optional<String> tryExtracting() {
    if (gender == null || genderDerivedFlag == null) {
      return Optional.empty();
    }

    if (!genderDerivedFlag.equals(CORRECT_GENDER_FLAG)) {
      return Optional.empty();
    }

    return Optional.of(gender);
  }
}

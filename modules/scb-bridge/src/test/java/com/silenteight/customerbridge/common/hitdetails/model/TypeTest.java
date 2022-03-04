package com.silenteight.customerbridge.common.hitdetails.model;

import com.silenteight.customerbridge.common.hitdetails.model.Type.TypeNotFoundException;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class TypeTest {

  @Test(expected = TypeNotFoundException.class)
  public void invalidAbbreviationTest() {
    Type.parse("invalid");
  }

  @Test
  public void validAbbreviationTest() {
    Type type = Type.parse("I");

    assertThat(type).isEqualTo(Type.INDIVIDUAL);
  }
}
/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class ResourcesNameGenderDataTest {

  @Test
  public void wrongFilePath_exception() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new ResourcesNameGenderData("test", "test"));
  }

  @Test
  public void correctFile_listWithNames() {
    ResourcesNameGenderData genderData = new ResourcesNameGenderData(
        "com/silenteight/iris/bridge/scb/ingest/adapter/incomming/common/gender/male-names.csv",
        "com/silenteight/iris/bridge/scb/ingest/adapter/incomming/common/gender/female-names.csv");

    assertThat(genderData.getMaleNames()).containsExactly("Kamil", "Piotr", "Łukasz");
    assertThat(genderData.getFemaleNames()).containsExactly("Aleksandra", "Emilia", "Joanna");
  }

  @Test
  public void canCreateWithResources() {
    NameGenderData genderData = ResourcesNameGenderData.create();

    assertThat(genderData).isNotNull();
  }
}

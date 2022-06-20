/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.WlName;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.WlNameType;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.support.SynonymsList;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

class SuspectNameMergingTest {

  @Test
  void mergeInactiveSynonyms() {
    Suspect suspect1 = suspectWithSynonyms(
        "MAIN NAME", null, singletonList("SYNONYM 1"));
    Suspect suspect2 = suspectWithSynonyms(
        "MAIN NAME", null, singletonList("SYNONYM 2"));

    suspect1.merge(suspect2);

    assertThat(suspect1.getNameSynonyms().asListOfNames()).containsOnly("SYNONYM 1", "SYNONYM 2");
  }

  private Suspect suspectWithSynonyms(
      String mainName,
      String activeSynonym,
      List<String> otherSynonyms) {

    return suspectWithSynonyms(mainName, activeSynonym, otherSynonyms, Tag.NAME);
  }

  private Suspect suspectWithSynonyms(
      String mainName,
      String activeSynonym,
      List<String> otherSynonyms,
      Tag tag) { // NO MERGING OF ACTIVE NAMES if TAG != TAG.NAME

    Suspect suspect = new Suspect();

    suspect.setName(mainName);
    suspect.setTag(tag);

    SynonymsList nameSynonyms = suspect.getNameSynonyms();
    nameSynonyms.add(new Synonym(activeSynonym, true));
    otherSynonyms.stream().map(name -> new Synonym(name, false)).forEach(nameSynonyms::add);

    return suspect;
  }

  @Test
  void mergeActiveAndInactiveSynonyms() {
    Suspect suspect1 = suspectWithSynonyms(
        "MAIN NAME", null, singletonList("SYNONYM 1"));
    Suspect suspect2 = suspectWithSynonyms(
        "MAIN NAME", "SYNONYM 1", singletonList("SYNONYM 2"));

    suspect1.merge(suspect2);

    assertThat(suspect1.getNameSynonyms().asListOfNames()).containsOnly("SYNONYM 1", "SYNONYM 2");
    assertThat(suspect1.getNameSynonyms())
        .anyMatch(synonym -> synonym.isActive() && synonym.getText().equals("SYNONYM 1"));
  }

  @Test
  void mergeActiveSynonymsFromSuspects() {
    Suspect suspect1 = suspectWithSynonyms(
        "MAIN NAME", "AN ACTIVE SYNONYM", asList("SYNONYM 1", "SYNONYM 2"));
    Suspect suspect2 = suspectWithSynonyms(
        "MAIN NAME", "OTHER ACTIVE SYNONYM", asList("SYNONYM 1", "SYNONYM 2"));

    suspect1.merge(suspect2);

    assertThat(suspect1.getName()).isEqualTo("MAIN NAME");
    assertThat(suspect1.getActiveNames()).containsOnly(
        new WlName("AN ACTIVE SYNONYM", WlNameType.ALIAS),
        new WlName("OTHER ACTIVE SYNONYM", WlNameType.ALIAS));
    assertThat(suspect1.getNameSynonyms().asListOfNames())
        .containsOnly("AN ACTIVE SYNONYM", "OTHER ACTIVE SYNONYM", "SYNONYM 1", "SYNONYM 2");
  }

  @Test
  void activeSynonymAndActiveNameMerged() {
    Suspect suspect1 = suspectWithSynonyms(
        "MAIN NAME", "AN ACTIVE SYNONYM", asList("SYNONYM 1", "SYNONYM 2"));
    Suspect suspect2 = suspectWithSynonyms(
        "MAIN NAME", null, asList("SYNONYM 1", "SYNONYM 2"));

    suspect1.merge(suspect2);

    assertThat(suspect1.getName()).isEqualTo("MAIN NAME");
    assertThat(suspect1.getActiveNames()).containsOnly(
        new WlName("AN ACTIVE SYNONYM", WlNameType.ALIAS),
        new WlName("MAIN NAME", WlNameType.NAME));
    assertThat(suspect1.getNameSynonyms().asListOfNames())
        .containsOnly("AN ACTIVE SYNONYM", "SYNONYM 1", "SYNONYM 2");
  }

  @Test
  void differentMainNames() {
    Suspect suspect1 = suspectWithSynonyms(
        "FIRST MAIN NAME", null, asList("SYNONYM 1", "SYNONYM 2"));
    Suspect suspect2 = suspectWithSynonyms(
        "SECOND MAIN NAME", null, asList("SYNONYM 1", "SYNONYM 2"));

    suspect1.merge(suspect2);

    assertThat(suspect1.getName()).isEqualTo("FIRST MAIN NAME");
    assertThat(suspect1.getActiveNames()).containsOnly(
        new WlName("FIRST MAIN NAME", WlNameType.NAME),
        new WlName("SECOND MAIN NAME", WlNameType.NAME));
    assertThat(suspect1.getNameSynonyms().asListOfNames()).containsOnly("SYNONYM 1", "SYNONYM 2");
  }

  @Test
  void mainNameAsSynonyms() {
    Suspect suspect1 = suspectWithSynonyms(
        "MAIN NAME", null, asList("SYNONYM 1", "SYNONYM 2", "MAIN NAME"));
    Suspect suspect2 = suspectWithSynonyms(
        "MAIN NAME", "ACTIVE SYNONYM", asList("SYNONYM 1", "SYNONYM 2"));

    suspect1.merge(suspect2);

    assertThat(suspect1.getName()).isEqualTo("MAIN NAME");
    assertThat(suspect1.getActiveNames()).containsOnly(
        new WlName("MAIN NAME", WlNameType.NAME),
        new WlName("ACTIVE SYNONYM", WlNameType.ALIAS));
    assertThat(suspect1.getNameSynonyms().asListOfNames())
        .containsOnly("SYNONYM 1", "SYNONYM 2", "MAIN NAME", "ACTIVE SYNONYM");
  }

  @Test
  void mergeNamesWhenBothTagAreNotNames() {
    Suspect suspect1 = suspectWithSynonyms(
        "MAIN NAME", null, singletonList("SYNONYM 1"), Tag.SEARCH_CODE);
    Suspect suspect2 = suspectWithSynonyms(
        "OTHER MAIN NAME", null, singletonList("SYNONYM 2"), Tag.PASSPORT);

    suspect1.merge(suspect2);

    assertThat(suspect1.getName()).isEqualTo("MAIN NAME");
    assertThat(suspect1.getActiveNames()).containsOnly(
        new WlName("MAIN NAME", WlNameType.NAME),
        new WlName("OTHER MAIN NAME", WlNameType.NAME));
    assertThat(suspect1.getNameSynonyms().asListOfNames()).containsOnly("SYNONYM 1", "SYNONYM 2");
  }

  @Test
  void mergeActiveNameWhenMergingCodToNam() {
    Suspect suspect1 = suspectWithSynonyms(
        "MAIN NAME", null, asList("SYNONYM 1", "SYNONYM 2"), Tag.SEARCH_CODE);
    Suspect suspect2 = suspectWithSynonyms(
        "MAIN NAME", "SYNONYM 2", singletonList("SYNONYM 1"), Tag.NAME);

    suspect1.merge(suspect2);

    assertThat(suspect1.getName()).isEqualTo("MAIN NAME");
    assertThat(suspect1.getActiveNames()).containsOnly(new WlName("SYNONYM 2", WlNameType.ALIAS));
    assertThat(suspect1.getNameSynonyms().asListOfNames()).containsOnly("SYNONYM 1", "SYNONYM 2");
  }

  @Test
  void mergeActiveNameWhenMergingCodToCodToNam() {
    Suspect suspect1 = suspectWithSynonyms(
        "MAIN NAME", null, asList("SYNONYM 1", "SYNONYM 2"), Tag.SEARCH_CODE);
    Suspect suspect2 = suspectWithSynonyms(
        "MAIN NAME", null, asList("SYNONYM 1", "SYNONYM 2"), Tag.SEARCH_CODE);
    Suspect suspect3 = suspectWithSynonyms(
        "MAIN NAME", "SYNONYM 2", singletonList("SYNONYM 1"), Tag.NAME);

    suspect2.merge(suspect3);
    suspect1.merge(suspect2);

    assertThat(suspect1.getName()).isEqualTo("MAIN NAME");
    assertThat(suspect1.getActiveNames()).containsOnly(new WlName("SYNONYM 2", WlNameType.ALIAS));
    assertThat(suspect1.getNameSynonyms().asListOfNames()).containsOnly("SYNONYM 1", "SYNONYM 2");
  }

  @Test
  void mainNameNotActiveWhenNotNam() {
    Suspect suspect1 = suspectWithSynonyms(
        "FIRST MAIN NAME", null, asList("SYNONYM 1", "SYNONYM 2"));
    Suspect suspect2 = suspectWithSynonyms(
        "SECOND MAIN NAME", null, asList("SYNONYM 1", "SYNONYM 2"), Tag.SEARCH_CODE);

    suspect1.merge(suspect2);

    assertThat(suspect1.getName()).isEqualTo("FIRST MAIN NAME");
    assertThat(suspect1.getActiveNames()).containsOnly(
        new WlName("FIRST MAIN NAME", WlNameType.NAME));
    assertThat(suspect1.getNameSynonyms().asListOfNames()).containsOnly("SYNONYM 1", "SYNONYM 2");
  }
}

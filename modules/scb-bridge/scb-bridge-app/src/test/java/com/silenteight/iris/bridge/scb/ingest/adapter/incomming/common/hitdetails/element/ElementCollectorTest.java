/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.element;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Attribute;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Synonym;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(Enclosed.class)
public class ElementCollectorTest {

  public abstract static class GivenElementCollector {

    protected List<String> actions;
    protected ElementCollector elementCollector;

    @Before
    public void setUp() {
      this.actions = new ArrayList<>();
      elementCollector = new ElementCollector(new ElementCollectorEventListenerSpy());
    }

    protected void assertActions(String... actions) {
      assertThat(this.actions).containsExactly(actions);
    }

    private class ElementCollectorEventListenerSpy implements ElementCollectorEventListener {

      @Override
      public void onDivider() {
        actions.add("onDivider");
      }

      @Override
      public void onAttribute(Attribute attribute) {
        actions.add(String.format("addAttribute[key: '%s', value: '%s']",
            attribute.getKey(), attribute.getValue()));
      }

      @Override
      public void onSynonym(Synonym synonym) {
        actions.add(String.format("addSynonym[text: '%s', isActive: %s]", synonym.getText(),
            synonym.isActive()));
      }

      @Override
      public void onText(String text) {
        actions.add(String.format("onText[text: '%s']", text));
      }
    }
  }

  public static class IllegalArgsTest extends GivenElementCollector {

    @Test(expected = NullPointerException.class)
    public void onNullKey_throwsNPE() {
      elementCollector.onKey(0, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void onNullText_throwsNPE() {
      elementCollector.onText(0, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void onNullError_throwsNPE() {
      elementCollector.onError(0, 0, null);
    }
  }

  public static class SimpleTest extends GivenElementCollector {

    @Test
    public void onDivider_shouldHaveValidActions() {
      elementCollector.onStart();
      elementCollector.onSectionDivider(0, 0);
      elementCollector.onFinish();

      assertActions("onDivider");
    }

    @Test
    public void onKeyAndOnText_shouldHaveValidActions() {
      elementCollector.onStart();
      elementCollector.onKey(0, 0, "key");
      elementCollector.onText(0, 0, "text");
      elementCollector.onFinish();

      assertActions("addAttribute[key: 'key', value: 'text']");
    }

    @Test
    public void onKeyWithoutText_shouldHaveValidActions() {
      elementCollector.onStart();
      elementCollector.onKey(0, 0, "key");
      elementCollector.onFinish();

      assertActions("addAttribute[key: 'key', value: 'null']");
    }

    @Test
    public void onKeyAndTextNone_shouldHaveValidActions() {
      elementCollector.onStart();
      elementCollector.onKey(0, 0, "key");
      elementCollector.onText(0, 0, "none");
      elementCollector.onFinish();

      assertActions("addAttribute[key: 'key', value: 'null']");

    }

    @Test
    public void onMultipleKeyValues_shouldHaveValidActions() {
      elementCollector.onStart();
      elementCollector.onKey(0, 0, "key1");
      elementCollector.onText(0, 0, "text1");
      elementCollector.onKey(0, 0, "key2");
      elementCollector.onText(0, 0, "text2");
      elementCollector.onKey(0, 0, "key3");
      elementCollector.onText(0, 0, "none");
      elementCollector.onKey(0, 0, "key4");
      elementCollector.onKey(0, 0, "key5");
      elementCollector.onText(0, 0, "text5");
      elementCollector.onFinish();

      assertActions(
          "addAttribute[key: 'key1', value: 'text1']",
          "addAttribute[key: 'key2', value: 'text2']",
          "addAttribute[key: 'key3', value: 'null']",
          "addAttribute[key: 'key4', value: 'null']",
          "addAttribute[key: 'key5', value: 'text5']");
    }

    @Test
    public void onKeyValueWithSynonym_shouldHaveValidActions() {
      elementCollector.onStart();
      elementCollector.onKey(0, 0, "key");
      elementCollector.onText(0, 0, "text");
      elementCollector.onKey(0, 0, "Synonyms");
      elementCollector.onListPrefix(0, 0);
      elementCollector.onText(0, 0, "synonym");
      elementCollector.onFinish();

      assertActions(
          "addAttribute[key: 'key', value: 'text']",
          "addSynonym[text: 'synonym', isActive: false]");

    }

    @Test
    public void onKeyValueWithEmptySynonyms_shouldHaveValidActions() {
      elementCollector.onStart();
      elementCollector.onKey(0, 0, "key");
      elementCollector.onText(0, 0, "text");
      elementCollector.onKey(0, 0, "Synonyms");
      elementCollector.onText(0, 0, "none");
      elementCollector.onFinish();

      assertActions("addAttribute[key: 'key', value: 'text']");
    }

    @Test
    public void onKeyValueWithMultipleSynonyms_shouldHaveValidActions() {
      elementCollector.onStart();
      elementCollector.onKey(0, 0, "key");
      elementCollector.onText(0, 0, "text");
      elementCollector.onKey(0, 0, "Synonyms");
      elementCollector.onListPrefix(0, 0);
      elementCollector.onText(0, 0, "synonym1");
      elementCollector.onActiveListPrefix(0, 0);
      elementCollector.onText(0, 0, "synonym2");
      elementCollector.onListPrefix(0, 0);
      elementCollector.onText(0, 0, "synonym3");
      elementCollector.onFinish();

      assertActions(
          "addAttribute[key: 'key', value: 'text']",
          "addSynonym[text: 'synonym1', isActive: false]",
          "addSynonym[text: 'synonym2', isActive: true]",
          "addSynonym[text: 'synonym3', isActive: false]");
    }

    @Test
    public void onText_shouldHaveValidActions() {
      elementCollector.onStart();
      elementCollector.onText(0, 0, "text");
      elementCollector.onFinish();

      assertActions("onText[text: 'text']");
    }
  }

  public static class ComplexTest extends GivenElementCollector {

    @Test
    public void givenMultipleEvents_shouldHaveValidActions() {
      elementCollector.onStart();
      addText("Suspect(s) detected by OFAC-Agent:15");
      addKeyWithoutValue("SystemId");
      addKeyWithoutValue("Associate");

      addDivider();
      addText("Suspect detected #1");
      addKeyValue("OFAC ID", "AS00019883");
      addKeyValue("TAG", "NAM");
      addKeyValue("NAME", "QUDDUS, AMINULLAH AMIN");
      addSynonyms(1, "???? ???? ???? ????", "YUSUF, MUHAMMAD", "AMIN, AMINULLAH");
      addKeyWithoutValue("ADDRESS");
      addSynonyms(-1);

      addDivider();
      addText("Suspect detected #2");
      addKeyValue("OFAC ID", "AS00019884");
      addKeyValue("TAG", "NAM");
      addKeyValue("NAME", "FADHIL, MUSTAFA MOHAMED");
      addSynonyms(0, "JIHAD, ABU", "YUSUF, MUHAMMAD");
      addKeyWithoutValue("ADDRESS");
      addSynonyms(-1);

      addDivider();
      addText("*** INTERNAL OFAC DETAILS ***");
      addText("HasSndRcvIn");
      addKeyValue("Limited", "0");
      addText("|WL00038027|0.00|NAM|63|69|169|170|");
      elementCollector.onFinish();

      assertActions(
          "onText[text: 'Suspect(s) detected by OFAC-Agent:15']",
          "addAttribute[key: 'SystemId', value: 'null']",
          "addAttribute[key: 'Associate', value: 'null']",
          "onDivider",
          "onText[text: 'Suspect detected #1']",
          "addAttribute[key: 'OFAC ID', value: 'AS00019883']",
          "addAttribute[key: 'TAG', value: 'NAM']",
          "addAttribute[key: 'NAME', value: 'QUDDUS, AMINULLAH AMIN']",
          "addSynonym[text: '???? ???? ???? ????', isActive: false]",
          "addSynonym[text: 'YUSUF, MUHAMMAD', isActive: true]",
          "addSynonym[text: 'AMIN, AMINULLAH', isActive: false]",
          "addAttribute[key: 'ADDRESS', value: 'null']",
          "onDivider",
          "onText[text: 'Suspect detected #2']",
          "addAttribute[key: 'OFAC ID', value: 'AS00019884']",
          "addAttribute[key: 'TAG', value: 'NAM']",
          "addAttribute[key: 'NAME', value: 'FADHIL, MUSTAFA MOHAMED']",
          "addSynonym[text: 'JIHAD, ABU', isActive: true]",
          "addSynonym[text: 'YUSUF, MUHAMMAD', isActive: false]",
          "addAttribute[key: 'ADDRESS', value: 'null']",
          "onDivider",
          "onText[text: '*** INTERNAL OFAC DETAILS ***']",
          "onText[text: 'HasSndRcvIn']",
          "addAttribute[key: 'Limited', value: '0']",
          "onText[text: '|WL00038027|0.00|NAM|63|69|169|170|']");
    }

    private void addKeyValue(String key, String value) {
      elementCollector.onKey(0, 0, key);
      elementCollector.onText(0, 0, value);
    }

    private void addKeyWithoutValue(String key) {
      elementCollector.onKey(0, 0, key);
    }

    private void addSynonyms(int activeSynonym, String... synonyms) {
      elementCollector.onKey(0, 0, "Synonyms");
      if (synonyms.length == 0) {
        elementCollector.onText(0, 0, "none");
      }
      for (int i = 0; i < synonyms.length; i++) {
        if (activeSynonym == i)
          elementCollector.onActiveListPrefix(0, 0);
        else
          elementCollector.onListPrefix(0, 0);
        elementCollector.onText(0, 0, synonyms[i]);
      }
    }

    private void addText(String text) {
      elementCollector.onText(0, 0, text);
    }

    private void addDivider() {
      elementCollector.onSectionDivider(0, 0);
    }
  }
}

/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.lexer;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.ResourceUtil.readLinesFromResource;
import static com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.ResourceUtil.readTextFromResource;
import static org.assertj.core.api.Assertions.*;

@RunWith(Enclosed.class)
public class LexerTest {

  public abstract static class GivenLexer {

    List<String> actions;
    Lexer lexer;

    @Before
    public void givenLexer() {
      this.actions = new ArrayList<>();
      lexer = new Lexer(new LexerEventListenerSpy());
    }

    void assertActions(String... values) {
      assertThat(actions).containsExactly(values);
    }

    private class LexerEventListenerSpy implements LexerEventListener {

      @Override
      public void onStart() {
        actions.add("onStart");
      }

      @Override
      public void onEmptyLine() {
        actions.add("onEmptyLine");
      }

      @Override
      public void onFinish() {
        actions.add("onFinish");
      }

      @Override
      public void onKey(int lineIndex, int positionIndex, String key) {
        actions
            .add(String
                .format("onKey[line: %d, pos: %d, key: '%s']", lineIndex, positionIndex, key));
      }

      @Override
      public void onText(int lineIndex, int positionIndex, String text) {
        actions.add(
            String.format("onText[line: %d, pos: %d, text: '%s']", lineIndex, positionIndex, text));
      }

      @Override
      public void onSectionDivider(int lineIndex, int positionIndex) {
        actions.add(String.format("onSectionDivider[line: %d, pos: %d]", lineIndex, positionIndex));
      }

      @Override
      public void onListPrefix(int lineIndex, int positionIndex) {
        actions.add(String.format("onListPrefix[line: %d, pos: %d]", lineIndex, positionIndex));
      }

      @Override
      public void onActiveListPrefix(int lineIndex, int positionIndex) {
        actions
            .add(String.format("onActiveListPrefix[line: %d, pos: %d]", lineIndex, positionIndex));
      }

      @Override
      public void onError(int lineIndex, int positionIndex, String text) {
        actions.add(
            String
                .format("onError[line: %d, pos: %d, text: '%s']", lineIndex, positionIndex, text));
      }
    }
  }

  public static class SimpleLexerTest extends GivenLexer {

    @Test
    public void givenTextLineInput_actionsContainsValidEntries() {
      lexer.lex("Suspect(s) detected by OFAC-Agent:15");

      assertActions(
          "onStart",
          "onText[line: 0, pos: 0, text: 'Suspect(s) detected by OFAC-Agent:15']",
          "onFinish");
    }

    @Test
    public void givenDividerLineInput_actionsContainsValidEntries() {
      lexer.lex("=============================");

      assertActions(
          "onStart",
          "onSectionDivider[line: 0, pos: 0]",
          "onFinish");
    }

    @Test
    public void givenKeyValue1LineInput_actionsContainsValidEntries() {
      lexer.lex("SystemId: systemId");

      assertActions(
          "onStart",
          "onKey[line: 0, pos: 0, key: 'SystemId']",
          "onText[line: 0, pos: 10, text: 'systemId']",
          "onFinish"
      );
    }

    @Test
    public void givenKeyValue1LineInputWithoutValue_actionsContainsValidEntries() {
      lexer.lex("SystemId:");

      assertActions(
          "onStart",
          "onKey[line: 0, pos: 0, key: 'SystemId']",
          "onFinish"
      );
    }

    @Test
    public void givenKeyValue1LineWithWhiteSpacesInput_actionsContainsValidEntries() {
      lexer.lex("   SystemId:    systemId    ");

      assertActions(
          "onStart",
          "onKey[line: 0, pos: 3, key: 'SystemId']",
          "onText[line: 0, pos: 16, text: 'systemId']",
          "onFinish"
      );
    }

    @Test
    public void givenKeyValue1Synonym_actionsContainsValidEntries() {
      lexer.lex("  Synonym:  none");

      assertActions(
          "onStart",
          "onKey[line: 0, pos: 2, key: 'Synonym']",
          "onText[line: 0, pos: 12, text: 'none']",
          "onFinish"
      );
    }

    @Test
    public void givenKeyValue2_actionsContainsValidEntries() {
      lexer.lex("OFAC ID:AS00019883");

      assertActions(
          "onStart",
          "onKey[line: 0, pos: 0, key: 'OFAC ID']",
          "onText[line: 0, pos: 8, text: 'AS00019883']",
          "onFinish"
      );
    }

    @Test
    public void givenKeyValue2WithoutValue_actionsContainsValidEntries() {
      lexer.lex("OFAC ID:   ");

      assertActions(
          "onStart",
          "onKey[line: 0, pos: 0, key: 'OFAC ID']",
          "onFinish"
      );
    }

    @Test
    public void givenListValue_actionsContainsValidEntries() {
      lexer.lex("   - FEDERAL REPUBLIC OF GERMANY");

      assertActions(
          "onStart",
          "onListPrefix[line: 0, pos: 3]",
          "onText[line: 0, pos: 5, text: 'FEDERAL REPUBLIC OF GERMANY']",
          "onFinish"
      );

    }

    @Test
    public void givenOnlyNewLines_actionsAreAsExpected() {
      lexer.lex("    \n\n\n\n          \n       \n   ");

      assertActions(
          "onStart",
          "onEmptyLine",
          "onEmptyLine",
          "onEmptyLine",
          "onEmptyLine",
          "onEmptyLine",
          "onEmptyLine",
          "onEmptyLine",
          "onFinish");
    }
  }

  public static class ComplexLexerTest extends GivenLexer {

    @Test
    public void complexTestResource1() {
      lexer.lex(readTextFromResource("LexerTest/complex_test_resource_1.txt"));

      assertActions(
          readLinesFromResource("LexerTest/complex_test_resource_1_expected_actions.txt"));
    }
  }
}

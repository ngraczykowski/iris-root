package com.silenteight.agent.common.dictionary;

import lombok.NoArgsConstructor;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class DictionaryLineTransformers {

  /**
   * Line Format: <pre>key1;key2=value1;value2</pre>
   */
  public static final Function<String, KeysAndMultipleValues> KEYS_AND_VALUES =
      l -> new KeysAndMultipleValues(l, false);

  /**
   * Line Format: <pre>value1;value2=key1;key2</pre>
   */
  public static final Function<String, KeysAndMultipleValues> INVERTED_KEYS_AND_VALUES =
      l -> new KeysAndMultipleValues(l, true);

  /**
   * Line Format: <pre>key=value1;value2</pre>
   */
  public static final Function<String, KeysAndSingleValue> KEYS_AND_VALUE =
      l -> new KeysAndSingleValue(l, false);

  /**
   * Line Format: <pre>value1;value2=key</pre>
   */
  public static final Function<String, KeysAndSingleValue> INVERTED_KEYS_AND_VALUE =
      l -> new KeysAndSingleValue(l, true);

  /**
   * Line Format: <pre>value1;value2;value3</pre>
   */
  public static final Function<String, MultipleValues> MULTIPLE_VALUES =
      MultipleValues::new;

  /**
   * Trims string and convert to upper case.
   */
  public static final UnaryOperator<String> TRIM_AND_UPPER_CASE = l -> l.trim().toUpperCase();
}

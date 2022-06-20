/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.builder.consumerrepo;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.builder.consumerrepo.MappingConsumerRepository.MappingValueException;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

public class MappingConsumerRepositoryTest {

  private List<String> actions;

  @Before
  public void setUp() {
    actions = new ArrayList<>();
  }

  @Test
  public void givenRepoWithoutRegistration_actionsShouldBeEmpty() {
    new MappingConsumerRepositorySpy(spy -> {
    });

    assertThat(actions).isEmpty();
  }

  @Test(expected = NullPointerException.class)
  public void givenNullKey_throwsNPE() {
    new MappingConsumerRepositorySpy(spy -> {
    }).find(null);
  }

  @Test
  public void givenRepoWithSimpleConsumer_actionsShouldBeEmpty() {
    new MappingConsumerRepositorySpy(spy -> spy.register("key"));

    assertThat(actions).containsExactly(
        "register[key: 'key']"
    );
  }

  @Test
  public void givenRepoWithMappingConsumer_actionsShouldHaveValidEntries() {
    new MappingConsumerRepositorySpy(spy -> spy.registerWithMapper("key"));

    assertThat(actions).containsExactly(
        "register[key: 'key']"
    );
  }

  @Test
  public void givenMultipleDifferentConsumers_actionsShouldHaveValidEntries() {
    new MappingConsumerRepositorySpy(spy -> {
      spy.registerWithMapper("key1");
      spy.register("key2");
      spy.registerWithMapper("key3");
      spy.registerWithMapper("key4");
      spy.register("key5");
      spy.registerWithMapper("key6");
    });

    assertThat(actions).containsExactly(
        "register[key: 'key1']",
        "register[key: 'key2']",
        "register[key: 'key3']",
        "register[key: 'key4']",
        "register[key: 'key5']",
        "register[key: 'key6']"
    );
  }

  @Test
  public void givenMiltipleDifferentConsumers_findSomeConsumers_actionsShouldHaveValidEntries() {
    MappingConsumerRepositorySpy repo = new MappingConsumerRepositorySpy(
        spy -> {
          spy.registerWithMapper("key1");
          spy.register("key2");
          spy.registerWithMapper("key3");
          spy.register("key4");
        });

    repo.find("key3");
    repo.find("key2");
    repo.find("notExistingKey");

    assertThat(actions).containsExactly(
        "register[key: 'key1']",
        "register[key: 'key2']",
        "register[key: 'key3']",
        "register[key: 'key4']",
        "find[key: 'key3']",
        "map[text2: 'text2-1']",
        "consume[text1: 'text1-1', text2: 'text2-1']",
        "find[key: 'key2']",
        "consume[text1: 'text1-2', text2: 'text2-2']",
        "find[key: 'notExistingKey']"
    );
  }

  @Test(expected = MappingValueException.class)
  public void givenFailingMappingConsumer_findFailingMappingConsumer_throwsMappingValueException() {
    MappingConsumerRepositorySpy repo = new MappingConsumerRepositorySpy(
        spy -> spy.registerWithFailingMapper("key"));
    repo.find("key");
  }

  private class MappingConsumerRepositorySpy extends MappingConsumerRepository<String, String> {

    private final AtomicInteger counter = new AtomicInteger();

    MappingConsumerRepositorySpy(Consumer<MappingConsumerRepositorySpy> consumer) {
      consumer.accept(this);
    }

    void register(String key) {
      register(key, (t1, t2) ->
          actions.add(String.format("consume[text1: '%s', text2: '%s']", t1, t2)));
    }

    @Override
    public void register(String key, BiConsumer<String, String> consumer) {
      super.register(key, consumer);
      actions.add(String.format("register[key: '%s']", key));
    }

    @Override
    public <T> void register(
        String key, Function<String, T> mapper,
        BiConsumer<String, T> consumer) {
      super.register(key, mapper, consumer);
      actions.add(String.format("register[key: '%s']", key));
    }

    @Override
    public Optional<BiConsumer<String, String>> find(String key) {
      actions.add(String.format("find[key: '%s']", key));
      Optional<BiConsumer<String, String>> stringStringBiConsumer = super.find(key);
      int index = counter.incrementAndGet();
      stringStringBiConsumer.ifPresent(c -> c.accept("text1-" + index, "text2-" + index));
      return stringStringBiConsumer;
    }

    void registerWithMapper(String key) {
      register(
          key,
          t2 -> {
            actions.add(String.format("map[text2: '%s']", t2));
            return t2;
          },
          (t1, t2) -> actions.add(String.format("consume[text1: '%s', text2: '%s']", t1, t2)));
    }

    void registerWithFailingMapper(String key) {
      register(
          key,
          t2 -> {
            throw new IllegalStateException();
          },
          (t1, t2) -> {
          });
    }
  }
}

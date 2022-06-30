package com.silenteight.agent.common.messaging.proto;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.google.protobuf.Message;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.silenteight.agents.logging.AgentLogger.trace;

@Slf4j
public class MessageRegistryFactory {

  private static final String MESSAGE_INTERFACE = Message.class.getName();

  // NOTE(ahaczewski): Always scan with at least 2 threads, at most number of available threads.
  private static final int NUM_SCAN_THREADS =
      Math.max(2, Runtime.getRuntime().availableProcessors());

  private final List<String> whitelistPackages = new ArrayList<>();

  public MessageRegistryFactory(@NonNull String firstPackage, String... whitelistPackages) {
    this.whitelistPackages.add(firstPackage);
    Collections.addAll(this.whitelistPackages, whitelistPackages);
  }

  public MessageRegistry create() {
    return new MessageRegistry(scan());
  }

  private List<Class<? extends Message>> scan() {
    try (ScanResult scanResult = getScanResult()) {
      return getMessageClasses(scanResult);
    }
  }

  private ScanResult getScanResult() {
    return new ClassGraph()
        .acceptPackages(whitelistPackages.toArray(String[]::new))
        .enableClassInfo()
        .enableMethodInfo()
        .scan(NUM_SCAN_THREADS);
  }

  private static List<Class<? extends Message>> getMessageClasses(ScanResult scanResult) {
    return scanResult
        .getClassesImplementing(MESSAGE_INTERFACE)
        .stream()
        .filter(MessageRegistryFactory::isValidMessageClass)
        .map(MessageRegistryFactory::loadClass)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private static boolean isValidMessageClass(ClassInfo classInfo) {
    return classInfo.hasMethod("getDescriptor")
        && !classInfo.isAbstract()
        && !classInfo.isInterfaceOrAnnotation()
        && !classInfo.isAnonymousInnerClass();
  }

  private static Class<? extends Message> loadClass(ClassInfo classInfo) {
    Class<? extends Message> type = classInfo.loadClass(Message.class, true);

    if (type == null) {
      log.warn("Could not load message type: class={}", classInfo.getName());
    } else {
      trace(log, "Loaded message type: class={}", type::getName);
    }

    return type;
  }
}

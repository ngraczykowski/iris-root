package com.silenteight.sens.webapp.common.app;

import java.io.File;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

import static java.util.Arrays.asList;

class HomeDirectoryDiscoverer {

  private static final Pattern JAR_FILE_PATTERN =
      Pattern.compile("(file:)?(?<path>[^!]*)(!.*)?");

  private final String homeEnvVariable;

  private List<PathSupplier> pathSuppliers;

  HomeDirectoryDiscoverer(String homeEnvVariable) {
    this.homeEnvVariable = homeEnvVariable;

    pathSuppliers = asList(
        this::getHomeFromEnvironment,
        com.silenteight.sens.webapp.common.app.HomeDirectoryDiscoverer
            ::discoverHomeFromUserWorkingDirectory,
        com.silenteight.sens.webapp.common.app.HomeDirectoryDiscoverer::discoverHomeFromJarFile
    );
  }

  Optional<Path> discover() {
    for (PathSupplier supplier : pathSuppliers) {
      Optional<Path> path = supplier.get();
      if (path.isPresent())
        return path;
    }
    return Optional.empty();
  }

  private Optional<Path> getHomeFromEnvironment() {
    try {
      return Optional.ofNullable(System.getenv(homeEnvVariable)).map(Paths::get);
    } catch (InvalidPathException ignored) {
      return Optional.empty();
    }
  }

  private static Optional<Path> discoverHomeFromUserWorkingDirectory() {
    return getUserWorkingDirectory().flatMap(
        com.silenteight.sens.webapp.common.app.HomeDirectoryDiscoverer::discoverHomeFrom);
  }

  private static Optional<Path> getUserWorkingDirectory() {
    return Optional.ofNullable(System.getProperty("user.dir")).map(Paths::get);
  }

  private static Optional<Path> discoverHomeFromJarFile() {
    return getJarDirectory().flatMap(com.silenteight.sens.webapp.common.app.HomeDirectoryDiscoverer
        ::discoverHomeFrom);
  }

  private static Optional<Path> getJarDirectory() {
    return Optional
        .of(com.silenteight.sens.webapp.common.app.HomeDirectoryDiscoverer.class)
        .map(Class::getProtectionDomain)
        .filter(protectionDomain -> protectionDomain.getCodeSource() != null)
        .map(ProtectionDomain::getCodeSource)
        .filter(codeSource -> codeSource.getLocation() != null)
        .map(CodeSource::getLocation)
        .map(URL::getPath)
        .map(JAR_FILE_PATTERN::matcher)
        .filter(Matcher::matches)
        .map(m -> m.group("path"))
        .map(Paths::get);
  }

  private static Optional<Path> discoverHomeFrom(@Nullable Path current) {
    if (current != null) {
      if (hasConfDirectory(current))
        return Optional.of(current);
      else
        return discoverHomeFrom(current.getParent());
    }
    return Optional.empty();
  }

  private static boolean hasConfDirectory(Path parent) {
    File confDirectory = parent.resolve("conf").toFile();
    return confDirectory.exists() && confDirectory.isDirectory();
  }

  private interface PathSupplier extends Supplier<Optional<Path>> {
  }
}

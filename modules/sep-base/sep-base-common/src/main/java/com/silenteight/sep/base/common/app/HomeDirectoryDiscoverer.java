package com.silenteight.sep.base.common.app;

import lombok.extern.slf4j.Slf4j;

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

import static com.google.common.base.Strings.emptyToNull;
import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static java.util.Arrays.asList;

@Slf4j
public class HomeDirectoryDiscoverer {

  private static final Pattern JAR_FILE_PATTERN =
      Pattern.compile("(file:)?(?<path>[^!]*)(!.*)?");

  private final String homeEnvVariable;
  private final String homeProperty;

  private List<PathSupplier> pathSuppliers;

  public HomeDirectoryDiscoverer(String homeEnvVariable, String homeProperty) {
    this.homeEnvVariable = homeEnvVariable;
    this.homeProperty = homeProperty;

    pathSuppliers = asList(
        this::getHomeFromEnvironment,
        this::getHomeFromProperties,
        HomeDirectoryDiscoverer::discoverHomeFromUserWorkingDirectory,
        HomeDirectoryDiscoverer::discoverHomeFromJarFile
    );
  }

  Path configureHomeDirectory() {
    var homeDirectory = discoverOrFallBack();
    setHomeDirectory(homeDirectory);
    return homeDirectory;
  }

  private Path discoverOrFallBack() {
    return discover().orElseGet(() -> {
      Path userDir = getUserDir();
      log.warn("Home directory not found! Falling back to: {}", userDir);
      return userDir;
    });
  }

  private Optional<Path> discover() {
    for (PathSupplier supplier : pathSuppliers) {
      Optional<Path> path = supplier.get();
      if (path.isPresent())
        return path;
    }
    return Optional.empty();
  }

  private static Path getUserDir() {
    return Paths.get(getProperty("user.dir", "."));
  }

  private void setHomeDirectory(Path homePath) {
    setProperty(homeProperty, homePath.toString());
  }

  private Optional<Path> getHomeFromEnvironment() {
    try {
      return Optional.ofNullable(System.getenv(homeEnvVariable)).map(Paths::get);
    } catch (InvalidPathException ignored) {
      return Optional.empty();
    }
  }

  private Optional<Path> getHomeFromProperties() {
    return Optional.ofNullable(emptyToNull(getProperty(homeProperty))).map(Paths::get);
  }

  private static Optional<Path> discoverHomeFromUserWorkingDirectory() {
    return getUserWorkingDirectory().flatMap(HomeDirectoryDiscoverer::discoverHomeFrom);
  }

  private static Optional<Path> getUserWorkingDirectory() {
    return Optional.ofNullable(System.getProperty("user.dir")).map(Paths::get);
  }

  private static Optional<Path> discoverHomeFromJarFile() {
    return getJarDirectory().flatMap(HomeDirectoryDiscoverer::discoverHomeFrom);
  }

  private static Optional<Path> getJarDirectory() {
    return Optional
        .of(HomeDirectoryDiscoverer.class)
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

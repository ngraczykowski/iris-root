package com.silenteight.sens.webapp.common.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.Installer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class ApplicationBootstrapper {

  private final String appName;
  private final String homeProperty;
  private final HomeDirectoryDiscoverer homeDiscoverer;

  private String homeDirectory;

  void bootstrapApplication() {
    setupInstrumentation();
    setupHomeDirectory();
    setupPlugins();
    setupSystemProperties();
    createLogDirectory();
  }

  private static void setupInstrumentation() {
    ByteBuddyAgent.install();
  }

  private void setupHomeDirectory() {
    homeDiscoverer
        .discover()
        .map(Path::toString)
        .ifPresentOrElse(this::setHomeDirectory, this::homeNotSet);
  }

  private void homeNotSet() {
    String userDir = System.getProperty("user.dir", ".");
    log.warn("Home directory not found! Setting to: {}", userDir);
    setHomeDirectory(userDir);
  }

  private void setHomeDirectory(String homeDirectory) {
    String homeDir = System.getProperty(homeProperty);
    if (homeDir == null)
      System.setProperty(homeProperty, homeDirectory);

    this.homeDirectory = homeDirectory;
  }

  private void setupPlugins() {
    URI[] plugins = discoverPlugins();
    if (plugins.length > 0) {
      appendPluginsToClassLoader(plugins);
      extendSystemClassPathProperty(plugins);
      loadPlugins();
    }
  }

  @NotNull
  private URI[] discoverPlugins() {
    Path pluginsDirectory = Paths.get(homeDirectory, "plugin", appName);

    try (Stream<Path> entries = Files.list(pluginsDirectory)) {
      List<URI> urls = entries
          .filter(com.silenteight.sens.webapp.common.app.ApplicationBootstrapper::isJarFile)
          .map(Path::toUri)
          .collect(toList());

      if (urls.isEmpty())
        return new URI[0];

      return urls.toArray(URI[]::new);
    } catch (NoSuchFileException ignored) {
      log.debug("Plugin directory or plugin file does not exist, will not load plugins:"
                    + " directory={}", pluginsDirectory);
      return new URI[0];
    } catch (IOException e) {
      log.warn("Unable to discover plugins: directory={}", pluginsDirectory, e);
      return new URI[0];
    }
  }

  private static boolean isJarFile(Path path) {
    Path fileName = path.getFileName();
    return Files.isRegularFile(path) && fileName.toString().endsWith(".jar");
  }

  private static void appendPluginsToClassLoader(URI[] plugins) {
    log.debug("Enabling plugins: {}", (Object) plugins);

    for (URI url : plugins) {
      try {
        JarFile jarFile = new JarFile(new File(url));
        Installer.getInstrumentation().appendToSystemClassLoaderSearch(jarFile);
      } catch (IOException e) {
        log.warn("Failed to load plugin: file={}", url, e);
      }
    }
  }

  private static void extendSystemClassPathProperty(URI[] plugins) {
    String currentSystemClassPath = System.getProperty("java.class.path", "");
    String[] pluginPaths = Stream.of(plugins).map(URI::toString).toArray(String[]::new);
    String additionalClassPath = String.join(":", pluginPaths);
    System.setProperty(
        "java.class.path", String.join(":", currentSystemClassPath, additionalClassPath));
  }

  private static void loadPlugins() {
    // NOTE(ahaczewski): Forces loading of still unloaded JAR files.
    PluginLoader.INSTANCE.loadPlugins();
  }

  private static void setupSystemProperties() {
    // NOTE(ahaczewski): Force use of fast random source.
    System.setProperty("java.security.egd", "file:/dev/./urandom");
  }

  private void createLogDirectory() {
    try {
      Set<PosixFilePermission> filePermissions = PosixFilePermissions.fromString("rwxr-x---");

      Files.createDirectories(
          Paths.get(homeDirectory, "log", appName),
          PosixFilePermissions.asFileAttribute(filePermissions));
    } catch (IOException ignore) {
      // Intentionally left blank.
    }
  }
}

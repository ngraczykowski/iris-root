package com.silenteight.sep.base.common.app;

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
import java.nio.file.attribute.PosixFilePermissions;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@RequiredArgsConstructor
@Slf4j
public class ApplicationBootstrapper {

  private final String appName;
  private final String homeProperty;
  private final HomeDirectoryDiscoverer homeDiscoverer;

  private String homeDirectory;

  public void bootstrapApplication() {
    setUpInstrumentation();
    setUpSecuritySystemProperties();
    setUpHomeDirectory();
    setUpPlugins();
    setUpSystemProperties();
    createLogDirectory();
  }

  private static void setUpInstrumentation() {
    ByteBuddyAgent.install();
  }

  private void setUpHomeDirectory() {
    homeDiscoverer
        .discover()
        .map(Path::toString)
        .ifPresentOrElse(this::setHomeDirectory, this::homeNotSet);
  }

  private void homeNotSet() {
    var userDir = getProperty("user.dir", ".");
    log.warn("Home directory not found! Setting to: {}", userDir);
    setHomeDirectory(userDir);
  }

  private void setHomeDirectory(String homeDirectory) {
    var serpHome = getProperty(homeProperty);
    if (serpHome == null)
      setProperty(homeProperty, homeDirectory);

    this.homeDirectory = homeDirectory;
  }

  private void setUpPlugins() {
    var plugins = discoverPlugins();
    if (plugins.length > 0) {
      appendPluginsToClassLoader(plugins);
      extendSystemClassPathProperty(plugins);
      loadPlugins();
    }
  }

  @NotNull
  private URI[] discoverPlugins() {
    var pluginsDirectory = Paths.get(homeDirectory, "plugin", appName);

    try (var entries = Files.list(pluginsDirectory)) {
      var urls = entries
          .filter(ApplicationBootstrapper::isJarFile)
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
    return Files.isRegularFile(path) && path.getFileName().toString().endsWith(".jar");
  }

  private static void appendPluginsToClassLoader(URI[] plugins) {
    log.debug("Enabling plugins: {}", (Object) plugins);

    for (URI url : plugins) {
      try {
        var jarFile = new JarFile(new File(url));
        Installer.getInstrumentation().appendToSystemClassLoaderSearch(jarFile);
      } catch (IOException e) {
        log.warn("Failed to load plugin: file={}", url, e);
      }
    }
  }

  private static void extendSystemClassPathProperty(URI[] plugins) {
    var currentSystemClassPath = getProperty("java.class.path", "");
    var pluginPaths = Stream.of(plugins).map(URI::toString).toArray(String[]::new);
    var additionalClassPath = String.join(":", pluginPaths);
    setProperty(
        "java.class.path", String.join(":", currentSystemClassPath, additionalClassPath));
  }

  private static void loadPlugins() {
    // NOTE(ahaczewski): Forces loading of still unloaded JAR files.
    PluginLoader.INSTANCE.loadPlugins();
  }

  private static void setUpSystemProperties() {
    // NOTE(ahaczewski): Force use of fast random source.
    setProperty("java.security.egd", "file:/dev/./urandom");
  }

  private void createLogDirectory() {
    try {
      var filePermissions = PosixFilePermissions.fromString("rwxr-x---");

      Files.createDirectories(
          Paths.get(homeDirectory, "log", appName),
          PosixFilePermissions.asFileAttribute(filePermissions));
    } catch (IOException ignore) {
      // Intentionally left blank.
    }
  }

  private static void setUpSecuritySystemProperties() {
    setSystemPropertiesFromEnvironmentVariable("javax.net.ssl.trustStore",
        "TRUSTSTORE_PATH");
    setSystemPropertiesFromEnvironmentVariable("javax.net.ssl.trustStorePassword",
        "TRUSTSTORE_PASSWORD");
  }

  private static void setSystemPropertiesFromEnvironmentVariable(String property,
                                                                 String environmentVariable) {
    var environmentVariableValue = System.getenv(environmentVariable);

    if (getProperty(property) == null && isNotBlank(environmentVariableValue))
      setProperty(property, environmentVariableValue);
  }
}

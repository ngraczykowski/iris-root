package com.silenteight.agent.configloader;

import lombok.NonNull;

import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.handler.IgnoreErrorsBindHandler;
import org.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import org.springframework.boot.context.properties.bind.handler.NoUnboundElementsBindHandler;
import org.springframework.boot.context.properties.bind.validation.ValidationBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.UnboundElementsSourceFilter;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.IOException;
import java.util.List;

class SpringConfigurationPropertiesLoader {

  private static final boolean IGNORE_INVALID_FIELDS = false;
  private static final boolean IGNORE_UNKNOWN_FIELDS = true;

  private static final PropertySourceLoader[] LOADERS = new PropertySourceLoader[] {
      new PropertiesPropertySourceLoader(),
      new YamlPropertySourceLoader(),
  };

  private final Binder binder;

  SpringConfigurationPropertiesLoader(String resourceLocation) {
    DefaultResourceLoader resourceLoader = new DefaultResourceLoader(getClass().getClassLoader());
    Resource resource = resourceLoader.getResource(resourceLocation);
    String name = resource.getFilename() != null ? resource.getFilename() : resourceLocation;
    PropertySourceLoader loader = getLoader(resourceLocation);

    binder = createBinder(loadPropertySources(resource, name, loader));
  }

  private static PropertySourceLoader getLoader(String filename) {
    for (PropertySourceLoader loader : LOADERS) {
      for (String fileExtension : loader.getFileExtensions()) {
        if (filename.endsWith(fileExtension))
          return loader;
      }
    }

    return LOADERS[0];
  }

  private static List<PropertySource<?>> loadPropertySources(
      Resource resource, String name, PropertySourceLoader loader) {
    try {
      return loader.load(name, resource);
    } catch (IOException e) {
      throw new FailedToLoadPropertiesException(resource, e);
    }
  }

  private static Binder createBinder(List<PropertySource<?>> sources) {
    return new Binder(ConfigurationPropertySources.from(sources));
  }

  public <T> T load(@NonNull String prefix, @NonNull Class<T> propertiesClass) {
    BindResult<T> bindResult = binder.bind(
        prefix,
        Bindable.of(propertiesClass),
        getBindHandler());

    return bindResult
        .orElseThrow(() -> new FailedToBindPropertiesException(prefix, propertiesClass));
  }

  private static BindHandler getBindHandler() {
    BindHandler handler = new IgnoreTopLevelConverterNotFoundBindHandler();

    if (IGNORE_INVALID_FIELDS)
      handler = new IgnoreErrorsBindHandler(handler);

    if (IGNORE_UNKNOWN_FIELDS) {
      UnboundElementsSourceFilter filter = new UnboundElementsSourceFilter();
      handler = new NoUnboundElementsBindHandler(handler, filter);
    }

    handler = new ValidationBindHandler(handler, getValidator());

    return handler;
  }

  private static LocalValidatorFactoryBean getValidator() {
    LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
    validatorFactory.afterPropertiesSet();
    return validatorFactory;
  }

  private static final class FailedToLoadPropertiesException extends RuntimeException {

    private static final long serialVersionUID = 4703187358042795764L;

    FailedToLoadPropertiesException(Resource resource, IOException cause) {
      super("Failed to load properties from '" + resource + "'", cause);
    }
  }

  private static final class FailedToBindPropertiesException extends RuntimeException {

    private static final long serialVersionUID = -1214605465150681377L;

    FailedToBindPropertiesException(String prefix, Class<?> propertiesClass) {
      super("Failed to bind properties prefixed with '" + prefix + "' to " + propertiesClass);
    }
  }
}

package com.silenteight.adjudication.engine.comments.comment;

import com.mitchellbosecke.pebble.loader.Loader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PebbleConfiguration {

  @Bean
  public Loader<?> pebbleLoader() {
    return new PebbleDbLoader();
  }

}

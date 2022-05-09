function fn() {

  var env = karate.env;
  karate.log('Environment: ', env);

  var config = {
    env: env,
    baseUrl: null,
    maxWaitTime: 15
  }

  if (env == 'bravo-dev') {
    config.baseUrl = 'https://bravo.dev.silenteight.com/'
  } else if (env == '?') {
    // to customize
  }

  return config;
}

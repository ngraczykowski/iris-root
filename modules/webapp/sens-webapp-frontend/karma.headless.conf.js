// Karma configuration file, see link for more information
// https://karma-runner.github.io/1.0/config/configuration-file.html
var baseConfig = require('./karma.conf.js');

module.exports = function(config){
  baseConfig(config);

  config.set({
    singleRun: true,
    autoWatch: false,
    plugins: config.plugins.concat([
      require('karma-junit-reporter'),
    ]),
    browsers: ['HeadlessChrome'],
    reporters: ['dots', 'junit'],
    junitReporter: {
      outputDir: 'target/surefire-reports'
    },
    customLaunchers:{
      HeadlessChrome:{
        base: 'ChromeHeadless',
        flags: ['--no-sandbox']
      }
    }
  });
};

import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AUTHORITIES_CONFIGURATION_TOKEN } from '@core/authorities/services/authorities-configuration.token';

import { AppModule } from './app/app.module';
import { authoritiesConfig } from './authorities';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();
}

platformBrowserDynamic([
    { provide: AUTHORITIES_CONFIGURATION_TOKEN, useValue: authoritiesConfig }
  ]).bootstrapModule(AppModule)
  .then((success) => console.log('Application started'))
  .catch(err => console.log(err));

import { ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthoritiesModule } from '@core/authorities/authorities.module';
import { BrowserModule } from '@core/browser/browser.module';
import { ExternalAppsConfiguration } from '@core/external-apps/model/external-apps-configuration';
import { ExternalAppsService } from '@core/external-apps/services/external-apps.service';
import { EXTERNAL_APPS_CONFIGURATION } from '@core/external-apps/services/external-apps.token';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    AuthoritiesModule,
    BrowserModule
  ]
})
export class ExternalAppsModule {
  static forRoot(config: ExternalAppsConfiguration): ModuleWithProviders<ExternalAppsModule> {
    return {
      ngModule: ExternalAppsModule,
      providers: [{
        provide: EXTERNAL_APPS_CONFIGURATION,
        useValue: config
      }, ExternalAppsService]
    };
  }
}

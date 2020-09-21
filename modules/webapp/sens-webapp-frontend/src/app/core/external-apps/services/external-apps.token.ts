import { InjectionToken } from '@angular/core';
import { ExternalAppsConfiguration } from '@core/external-apps/model/external-apps-configuration';

export const EXTERNAL_APPS_CONFIGURATION: InjectionToken<ExternalAppsConfiguration> =
    new InjectionToken('EXTERNAL_APPS_CONFIGURATION');

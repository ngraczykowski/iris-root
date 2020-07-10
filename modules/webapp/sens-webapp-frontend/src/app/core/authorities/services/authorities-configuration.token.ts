import { InjectionToken } from '@angular/core';
import { AuthoritiesConfiguration } from '@core/authorities/model/authorities-configuration';

export const AUTHORITIES_CONFIGURATION_TOKEN: InjectionToken<AuthoritiesConfiguration> =
    new InjectionToken<AuthoritiesConfiguration>('AUTHORITIES_CONFIGURATION_TOKEN');

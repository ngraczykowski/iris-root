import { Authority } from '@core/authorities/model/authority.enum';
import { ExternalApp } from '@endpoint/external-apps/model/external-app.enum';

export interface ExternalAppConfiguration {
  authority: Authority;
  apps: ExternalApp[];
}

export interface ExternalAppsConfiguration {
  availableApps: ExternalAppConfiguration[];
}

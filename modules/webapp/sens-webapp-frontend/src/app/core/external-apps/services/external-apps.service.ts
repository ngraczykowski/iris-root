import { Inject, Injectable } from '@angular/core';
import { AuthoritiesService } from '@core/authorities/services/authorities.service';
import { WindowService } from '@core/browser/services/window.service';
import {
  ExternalAppConfiguration,
  ExternalAppsConfiguration
} from '@core/external-apps/model/external-apps-configuration';
import { EXTERNAL_APPS_CONFIGURATION } from '@core/external-apps/services/external-apps.token';
import { ExternalApp } from '@endpoint/external-apps/model/external-app.enum';
import { ExternalAppsEndpointService } from '@endpoint/external-apps/services/external-apps-endpoint.service';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExternalAppsService {

  constructor(private externalAppsEndpointService: ExternalAppsEndpointService,
              private authorityService: AuthoritiesService,
              private windowService: WindowService,
              @Inject(EXTERNAL_APPS_CONFIGURATION) private externalAppsConfiguration: ExternalAppsConfiguration) { }

  public openApp(app: ExternalApp): void {
    this.externalAppsEndpointService.getLink(app)
        .subscribe((link: string) => this.redirect(link));
  }

  public getAvailableApps(): Observable<ExternalApp[]> {
    let apps: ExternalApp[] = [];
    this.externalAppsConfiguration.availableApps.filter((app: ExternalAppConfiguration) =>
        this.authorityService.hasAuthority(app.authority)).forEach(
            (app: ExternalAppConfiguration) => apps = apps.concat(app.apps));
    return of(apps);
  }

  private redirect(link: string): void {
    this.windowService.nativeWindow.open(link, '_blank');
  }
}

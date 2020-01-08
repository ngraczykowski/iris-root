import { environment } from '@env/environment.prod';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakOptions } from 'keycloak-angular/lib/core/interfaces/keycloak-options';
import { EMPTY, from, Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

const keycloakConfig: KeycloakOptions = {
  config: {
    url: environment.auth.keycloak.url,
    realm: environment.auth.keycloak.realm,
    clientId: environment.auth.keycloak.clientId
  },
  initOptions: {
    onLoad: 'check-sso',
    checkLoginIframe: false,
  },
  bearerExcludedUrls: environment.auth.keycloak.excludedTokenUrls
};

export class KeycloakInitializer {
  constructor(private readonly keycloakService: KeycloakService) {
  }

  doInitialize(): Observable<void> {
    const keycloakInitPromise: Promise<boolean> = this.keycloakService.init(keycloakConfig);

    return from(keycloakInitPromise)
        .pipe(
            // .init() returns information whatever user is authenticated or not,
            // which we get rid of
            switchMap(() => EMPTY)
        );
  }
}

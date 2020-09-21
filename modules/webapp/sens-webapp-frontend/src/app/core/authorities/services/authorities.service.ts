import { Inject, Injectable } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { AuthoritiesConfiguration } from '@core/authorities/model/authorities-configuration';
import { Authority } from '@core/authorities/model/authority.enum';
import { AUTHORITIES_CONFIGURATION_TOKEN } from '@core/authorities/services/authorities-configuration.token';

@Injectable({
  providedIn: 'root'
})
export class AuthoritiesService {

  constructor(@Inject(AUTHORITIES_CONFIGURATION_TOKEN) private config: AuthoritiesConfiguration,
              private authenticatedUserFacade: AuthenticatedUserFacade) { }

  public hasAuthorityForFeature(feature: string): boolean {
    const requiredAuthorities: Authority[] = this.config.features[feature];

    if (!requiredAuthorities) {
      return true;
    }

    const userRoles: any[] = this.getRoles();
    const userRolesContainsRequiredRole: boolean =
        userRoles.some(userRole => requiredAuthorities.includes(userRole));

    return userRolesContainsRequiredRole;
  }

  public hasAuthority(authority: Authority): boolean {
    const userRoles: any[] = this.getRoles();
    return userRoles.includes(authority);
  }

  private getRoles(): any[] {
    return this.authenticatedUserFacade.getUserRoles();
  }
}

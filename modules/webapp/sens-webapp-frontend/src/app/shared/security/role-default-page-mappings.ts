import { InjectionToken } from '@angular/core';

export const ROLES_REDIRECT_CONFIG = new InjectionToken<RoleDefaultPageMappings>('rolesRedirectConfig');

export interface RoleDefaultPageMappings {
  getForRoles(roles: string[]): string | undefined;
}

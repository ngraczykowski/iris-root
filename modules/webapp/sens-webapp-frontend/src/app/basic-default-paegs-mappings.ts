import { RoleDefaultPageMappings } from '@app/shared/security/role-default-page-mappings';

export class BasicRoleDefaultPageMappings implements RoleDefaultPageMappings {

  constructor(private readonly roleByRedirectUri: Map<string, string>) {
  }

  getForRoles(roles: Array<string>): string | undefined {
    const rolesWithRedirection = roles
        .map(userRole => this.roleByRedirectUri.get(userRole))
        .filter(mappedRole => mappedRole !== undefined);

    return rolesWithRedirection[0];
  }
}

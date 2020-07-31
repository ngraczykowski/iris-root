import { selectSecurity } from '@app/reducers';
import { Principal } from '@app/shared/security/principal.model';
import { SecurityState } from '@app/shared/security/store/security.state';
import { createSelector } from '@ngrx/store';

export const isLoggedIn = createSelector(
    selectSecurity,
    (state: SecurityState) => state.isLoggedIn
);

export const getLoggedInPrincipal = createSelector(
    selectSecurity,
    (state: SecurityState) => state.principal
);

export const hasAllAuthorities = (authority: string[]) => createSelector(
    getLoggedInPrincipal,
    (principal: Principal) =>
        principal != null && authority.every(auth => principal.hasAuthority(auth))
);

export const getSessionExpireTime = () => createSelector(
    selectSecurity,
    (state: SecurityState) => state.sessionExpireTime
);

import { selectSecurity } from '@app/reducers';
import { Principal } from '@app/shared/security/principal.model';
import { createSelector } from '@ngrx/store';
import { State } from './security.reducer';

export const isLoggedIn = createSelector(
    selectSecurity,
    (state: State) => state.isLoggedIn
);

export const getLoggedInPrincipal = createSelector(
    selectSecurity,
    (state: State) => state.principal
);

export const hasAllAuthorities = (authority: string[]) => createSelector(
    getLoggedInPrincipal,
    (principal: Principal) =>
        principal != null && authority.every(auth => principal.hasAuthority(auth))
);

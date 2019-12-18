import { selectAuth } from '@app/reducers';
import { Principal } from '@app/shared/auth/principal.model';
import { createSelector } from '@ngrx/store';
import { State } from './auth.reducer';

export const isLoggedIn = createSelector(
    selectAuth,
    (state: State) => state.isLoggedIn
);

export const getLoggedInPrincipal = createSelector(
    selectAuth,
    (state: State) => state.principal
);

export const hasAllAuthorities = (authority: string[]) => createSelector(
    getLoggedInPrincipal,
    (principal: Principal) =>
        principal != null && authority.every(auth => principal.hasAuthority(auth))
);

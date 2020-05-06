import * as fromSecurity from '@app/shared/security/store/security.reducer';
import { environment } from '@env/environment';
import { ActionReducerMap, createSelector, MetaReducer } from '@ngrx/store';

export interface State {
  security: fromSecurity.State;
}

export const reducers: ActionReducerMap<State> = {
  security: fromSecurity.reducer
};

export const selectSecurity = (state: State) => state.security;

export const metaReducers: MetaReducer<State>[] = !environment.production ? [] : [];

import * as fromSecurity from '@app/shared/security/store/security.reducer';
import { SecurityState } from '@app/shared/security/store/security.state';
import { environment } from '@env/environment';
import { ActionReducerMap, MetaReducer } from '@ngrx/store';

export interface State {
  security: SecurityState;
}

export const reducers: ActionReducerMap<State> = {
  security: fromSecurity.reducer
};

export const selectSecurity = (state: State) => state.security;

export const metaReducers: MetaReducer<State>[] = !environment.production ? [] : [];

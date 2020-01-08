import authReducer, { State as AuthReducerState } from '@app/shared/security/store/security.reducer';
import * as fromUserManagement from '@app/templates/user-management/store/reducers/userManagement.reducer';
import { environment } from '@env/environment';
import { ActionReducerMap, createSelector, MetaReducer } from '@ngrx/store';

export interface State {
  userManagement: fromUserManagement.State;
  security: AuthReducerState;
}

export const reducers: ActionReducerMap<State> = {
  userManagement: fromUserManagement.reducer,
  security: authReducer
};

export const selectUserManagement = (state: State) => state.userManagement;

export const selectSecurity = (state: State) => state.security;

// TODO(bgulowaty): move those to dedicated userManagment selectors file
export const getUsers = createSelector(
    selectUserManagement,
    (state: fromUserManagement.State) => state.usersTable
);

export const getUsersLoaded = createSelector(
    selectUserManagement,
    (state: fromUserManagement.State) => state.loaded
);

export const getUsersTablePage = createSelector(
  selectUserManagement,
  (state: fromUserManagement.State, props: {page: number, size: number}) => {
    return {
      total: state.usersTable.total,
      results: state.usersTable.results.slice(props.page * props.size, (props.page + 1) * props.size)
    };
  }
);

export const getUserById = createSelector(
  selectUserManagement,
  (state: fromUserManagement.State, props: {userId: number}) => {
    return state.usersTable.results.filter(user => user.id === props.userId)[0];
  }
);

export const getUserDetails = createSelector(
  selectUserManagement,
  (state: fromUserManagement.State) => state.usersDetails
);

export const metaReducers: MetaReducer<State>[] = !environment.production ? [] : [];

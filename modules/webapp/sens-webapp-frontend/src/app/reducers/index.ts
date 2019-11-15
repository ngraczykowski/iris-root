import {
  ActionReducer,
  ActionReducerMap,
  createFeatureSelector,
  createSelector,
  MetaReducer
} from '@ngrx/store';
import { environment } from '../../environments/environment';
import * as fromUserManagement from '@app/templates/user-management/store/reducers/userManagement.reducer';

export interface State {
  userManagement: fromUserManagement.State;
}

export const reducers: ActionReducerMap<State> = {
  userManagement: fromUserManagement.reducer
};

export const selectUserManagement = (state: State) => state.userManagement;

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

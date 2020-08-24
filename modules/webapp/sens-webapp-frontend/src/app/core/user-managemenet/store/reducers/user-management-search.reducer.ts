import {
  UserManagementSearchAction,
  UserManagementSearchActionTypes
} from '@core/user-managemenet/store/actions/user-management-search.actions';

import {
  userManagementListInitialState,
  UserManagementListState
} from '@core/user-managemenet/store/user-management-list.state';

export function userManagementSearchReducer(
    state = userManagementListInitialState,
    action: UserManagementSearchAction
): UserManagementListState {
  switch (action.type) {
    case UserManagementSearchActionTypes.search:
      return {
        ...state,
        loaded: false,
        loading: true
      };
    case UserManagementSearchActionTypes.results:
      return {
        ...state,
        list: action.payload,
        loaded: true,
        loading: false
      };
    default:
      return state;
  }
}

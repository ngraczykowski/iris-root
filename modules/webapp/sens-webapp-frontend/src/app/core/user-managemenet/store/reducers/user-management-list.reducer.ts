import {
  UserManagementListAction,
  UserManagementListActionTypes
} from '@core/user-managemenet/store/actions/user-management-list.actions';
import {
  userManagementListInitialState,
  UserManagementListState
} from '@core/user-managemenet/store/user-management-list.state';

export function userManagementListReducer(
    state = userManagementListInitialState,
    action: UserManagementListAction
): UserManagementListState {
  switch (action.type) {
    case UserManagementListActionTypes.load:
      return {
        ...state,
        loaded: false,
        loading: true
      };
    case UserManagementListActionTypes.loadSuccess:
      return {
        ...state,
        loaded: true,
        loading: false,
        list: action.payload.content
      };
    case UserManagementListActionTypes.loadFail:
      return {
        ...state,
        loading: false,
        loaded: false,
        error: true
      };
    default:
      return state;
  }
}

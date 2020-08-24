import { LoaderState } from '@core/state/utils/loader-state';
import { UserManagementListItem } from '@endpoint/user-management/model/user-management-list-item';

export const USER_MANAGEMENT_LIST_STATE_NAME = 'user_management_list';

export interface UserManagementListState extends LoaderState {
  list: UserManagementListItem[];
}

export interface StateWithUserManagementList {
  [USER_MANAGEMENT_LIST_STATE_NAME]: UserManagementListState;
}

export const userManagementListInitialState: UserManagementListState = {
  list: null
};

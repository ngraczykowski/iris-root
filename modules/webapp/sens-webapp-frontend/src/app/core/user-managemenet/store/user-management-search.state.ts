import { UserManagementListState } from '@core/user-managemenet/store/user-management-list.state';

export const USER_MANAGEMENT_SEARCH_STATE_NAME = 'user_management_search';

export interface StateWithUserManagementSearch {
  [USER_MANAGEMENT_SEARCH_STATE_NAME]: UserManagementListState;
}

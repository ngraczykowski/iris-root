import {
  CellViewFactory,
  LabelViewFactory
} from '../../../../components/dynamic-view-table/table-data-mapper';
import { View } from '../../../../components/dynamic-view/dynamic-view.component';
import { User, UserTableCell } from '../../../model/user.model';
import {
  EditProfileViewComponent,
  EditProfileViewData
} from './edit-profile-view/edit-profile-view.component';
import { LabelViewComponent, LabelViewData } from './label-view/label-view.component';
import { RolesViewComponent, RolesViewData } from './roles-view/roles-view.component';
import { StatusViewComponent, StatusViewData } from './status-view/status-view.component';
import { UserNameViewComponent, UserNameViewData } from './user-name-view/user-name-view.component';
import { UserTypeViewComponent, UserTypeViewData } from './user-type-view/user-type-view.component';
import {
  DisplayNameViewComponent,
  DisplayNameViewData
} from '@app/templates/user-management/user-table/views/display-name-view/display-name-view.component';

export interface UserCellViewData {
  active: boolean;
}

export class UserLabelViewFactory implements LabelViewFactory {

  constructor(private label: string) {}

  create(): View {
    return {
      component: LabelViewComponent,
      data: <LabelViewData>{
        label: this.label
      }
    };
  }
}

export class UserNameCellViewFactory implements CellViewFactory<User> {
  create(entry: UserTableCell): View {
    return {
      component: UserNameViewComponent,
      data: <UserNameViewData> {
        active: entry.active,
        userName: entry.userName,
        filterQuery: entry.filterQuery
      }
    };
  }
}

export class UserDisplayNameCellViewFactory implements CellViewFactory<User> {
  create(entry: UserTableCell): View {
    return {
      component: DisplayNameViewComponent,
      data: <DisplayNameViewData> {
        active: entry.active,
        displayName: entry.displayName,
        filterQuery: entry.filterQuery
      }
    };
  }
}

export class UserRolesCellViewFactory implements CellViewFactory<User> {
  create(entry: User): View {
    return {
      component: RolesViewComponent,
      data: <RolesViewData> {
        active: entry.active,
        roles: entry.roles,
        superUser: entry.superUser
      }
    };
  }
}

export class UserStatusCellViewFactory implements CellViewFactory<User> {
  create(entry: User): View {
    return {
      component: StatusViewComponent,
      data: <StatusViewData> {
        active: entry.active
      }
    };
  }
}

export class UserTypeCellViewFactory implements CellViewFactory<User> {
  create(entry: User): View {
    return {
      component: UserTypeViewComponent,
      data: <UserTypeViewData> {
        active: entry.active,
        userType: entry.type
      }
    };
  }
}

export class UserEditProfileCellViewFactory implements CellViewFactory<User> {
  create(entry: User): View {
    return {
      component: EditProfileViewComponent,
      data: <EditProfileViewData> {
        user: entry
      }
    };
  }
}

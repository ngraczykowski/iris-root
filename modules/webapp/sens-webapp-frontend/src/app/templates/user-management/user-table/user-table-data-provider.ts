import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { TableData } from '../../../components/dynamic-view-table/dynamic-view-table.component';
import {
  TableDataMapper,
  TableDataMapperConfiguration
} from '../../../components/dynamic-view-table/table-data-mapper';
import { TableDataProvider } from '../../../components/pageable-dynamic-table/pageable-dynamic-table.component';
import { User } from '../../model/user.model';
import {
  UserDisplayNameCellViewFactory,
  UserEditProfileCellViewFactory,
  UserLabelViewFactory,
  UserNameCellViewFactory,
  UserRolesCellViewFactory,
  UserStatusCellViewFactory,
  UserTypeCellViewFactory
} from './views/user-view-factories';
import { Store, select } from '@ngrx/store';
import * as fromRoot from '@app/reducers/index';

const mapperConfig: TableDataMapperConfiguration<User> = {
  columnDefinitions: [
    {
      labelFactory: new UserLabelViewFactory('user-management.userList.labels.userName'),
      cellFactory: new UserNameCellViewFactory()
    },
    {
      labelFactory: new UserLabelViewFactory('user-management.userList.labels.displayName'),
      cellFactory: new UserDisplayNameCellViewFactory()
    },
    {
      labelFactory: new UserLabelViewFactory('user-management.userList.labels.roles'),
      cellFactory: new UserRolesCellViewFactory()
    },
    {
      labelFactory: new UserLabelViewFactory('user-management.userList.labels.status'),
      cellFactory: new UserStatusCellViewFactory()
    },
    {
      labelFactory: new UserLabelViewFactory('user-management.userList.labels.type'),
      cellFactory: new UserTypeCellViewFactory()
    },
    {
      labelFactory: new UserLabelViewFactory('user-management.userList.labels.userProfile'),
      cellFactory: new UserEditProfileCellViewFactory()
    }
  ]
};

@Injectable()
export class UserTableDataMapper extends TableDataMapper<User> {
  constructor() {
    super(mapperConfig);
  }
}

@Injectable()
export class UserTableDataProvider implements TableDataProvider {

  constructor(
    private store: Store<fromRoot.State>,
    private userTableDataMapper: UserTableDataMapper
  ) { }

  getPage(page: number, size: number, userNameFilter?: string): Observable<TableData> {
    const pageIndex = page - 1;
    const parameters = this.getQueryParameters(pageIndex, size, userNameFilter ? true : false);
    return this.store.pipe(
      select(fromRoot.getUsersTablePage, parameters),
      map(data => {
        if (userNameFilter) {
          return this.userTableDataMapper.map(data.total, this.filterUsers(data.results, userNameFilter), userNameFilter);
        } else {
          return this.userTableDataMapper.map(data.total, data.results);
        }
      })
    );
  }

  private filterUsers(users: User[], userNameFilter: string): User[] {
    return users.filter((user) => user.userName.indexOf(userNameFilter) > -1
      || (user.displayName !== null && user.displayName.indexOf(userNameFilter) > -1));
  }

  private getQueryParameters(pageIndex, size, userNameFilterSet) {
    return {
      page: userNameFilterSet ? 0 : pageIndex,
      size:  userNameFilterSet ? 9999 : size
    };
  }
}

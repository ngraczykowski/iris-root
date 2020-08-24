import { Routes } from '@angular/router';
import { UserManagementListComponent } from '@app/miniapps/user-management/containers/user-management-list/user-management-list.component';

export const userManagementRoutes: Routes = [{
  path: 'users/:phrase',
  component: UserManagementListComponent
}, {
  path: 'users',
  redirectTo: 'users/',
  pathMatch: 'full'
}, {
  path: '',
  redirectTo: 'users/',
  pathMatch: 'full'
}];

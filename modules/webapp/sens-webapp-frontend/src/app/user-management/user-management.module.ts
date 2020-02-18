import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserManagementRoutingModule } from './user-management-routing.module';
import { UserManagementPageComponent } from './containers/user-management-page/user-management-page.component';
import { ApplicationHeaderModule } from '@app/components/application-header/application-header.module';
import { UserTableComponent } from './components/user-table/user-table.component';
import { SearchComponent } from './components/search/search.component';
import { SharedModule } from '@app/shared/shared.module';

@NgModule({
  declarations: [
    UserManagementPageComponent,
    UserTableComponent,
    SearchComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    ApplicationHeaderModule,
    UserManagementRoutingModule
  ]
})
export class UserManagementModule { }

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserManagementRoutingModule } from './user-management-routing.module';
import { UserManagementPageComponent } from './containers/user-management-page/user-management-page.component';
import { ApplicationHeaderModule } from '@app/components/application-header/application-header.module';

@NgModule({
  declarations: [
    UserManagementPageComponent
  ],
  imports: [
    CommonModule,
    ApplicationHeaderModule,
    UserManagementRoutingModule
  ]
})
export class UserManagementModule { }

import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { SharedModule } from '../../../../shared/shared.module';
import { UserPasswordOptionComponent } from './user-password-option/user-password-option.component';
import { UserRolesOptionComponent } from './user-roles-option/user-roles-option.component';
import { UserStatusOptionComponent } from './user-status-option/user-status-option.component';
import { UserSuperUserOptionComponent } from './user-super-user-option/user-super-user-option.component';
import { UserUsernameOptionComponent } from './user-username-option/user-username-option.component';
import { UserAssignmentsComponent } from './user-assignments/user-assignments.component';
import { UserDecisionTreeSelectComponent } from './user-decision-tree-select/user-decision-tree-select.component';
import { UserDisplayNameOptionComponent } from '@app/templates/user-management/user-profile/options/user-display-name-option/user-display-name-option.component';

@NgModule({
  imports: [
    SharedModule,
    BrowserModule,
    ReactiveFormsModule
  ],
  declarations: [
    UserPasswordOptionComponent,
    UserUsernameOptionComponent,
    UserDisplayNameOptionComponent,
    UserRolesOptionComponent,
    UserStatusOptionComponent,
    UserSuperUserOptionComponent,
    UserAssignmentsComponent,
    UserDecisionTreeSelectComponent,
  ],
  exports: [
    UserPasswordOptionComponent,
    UserUsernameOptionComponent,
    UserDisplayNameOptionComponent,
    UserRolesOptionComponent,
    UserStatusOptionComponent,
    UserSuperUserOptionComponent,
    UserAssignmentsComponent,
  ]
})
export class UserOptionsModule {}

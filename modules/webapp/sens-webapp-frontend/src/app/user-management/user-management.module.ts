import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HintFeedbackModule } from '@app/components/hint-feedback/hint-feedback.module';
import { AngularSvgIconModule } from 'angular-svg-icon';

import { UserManagementRoutingModule } from './user-management-routing.module';
import { UserManagementPageComponent } from './containers/user-management-page/user-management-page.component';
import { ApplicationHeaderModule } from '@app/components/application-header/application-header.module';
import { UserTableComponent } from './components/user-table/user-table.component';
import { SearchComponent } from './components/search/search.component';
import { SharedModule } from '@app/shared/shared.module';
import { UserFormComponent } from './components/user-form/user-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { NewUserProfileModule } from '@app/templates/user-management/user-profile/new-user-profile/new-user-profile.module';
import { UserManagementClient } from '@app/templates/user-management/user-management-client';
import { UserFormContainerComponent } from './containers/user-form-container/user-form-container.component';
import { ValidationFeedbackComponent } from './components/validation-feedback/validation-feedback.component';

@NgModule({
  declarations: [
    UserManagementPageComponent,
    UserTableComponent,
    SearchComponent,
    UserFormComponent,
    UserFormContainerComponent,
    ValidationFeedbackComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    ReactiveFormsModule,
    ApplicationHeaderModule,
    UserManagementRoutingModule,
    NewUserProfileModule,
    AngularSvgIconModule,
    HintFeedbackModule
  ],
  providers: [
    UserManagementClient
  ]
})
export class UserManagementModule { }

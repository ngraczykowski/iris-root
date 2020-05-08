import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AngularSvgIconModule } from 'angular-svg-icon';

import { UserManagementRoutingModule } from './user-management-routing.module';
import { UserManagementPageComponent } from './containers/user-management-page/user-management-page.component';
import { UserTableComponent } from './components/user-table/user-table.component';
import { SearchComponent } from './components/search/search.component';
import { SharedModule } from '@app/shared/shared.module';
import { UserFormComponent } from './components/user-form/user-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { UserFormContainerComponent } from './containers/user-form-container/user-form-container.component';
import { ValidationFeedbackComponent } from './components/validation-feedback/validation-feedback.component';
import { UserManagementService } from './services/user-management.service';
import { HighlightSearchPipe } from './highlight-search.pipe';

@NgModule({
  declarations: [
    UserManagementPageComponent,
    UserTableComponent,
    SearchComponent,
    UserFormComponent,
    UserFormContainerComponent,
    ValidationFeedbackComponent,
    HighlightSearchPipe
  ],
  imports: [
    CommonModule,
    SharedModule,
    ReactiveFormsModule,
    UserManagementRoutingModule,
    AngularSvgIconModule,
  ],
  providers: [
    UserManagementService
  ]
})
export class UserManagementModule { }

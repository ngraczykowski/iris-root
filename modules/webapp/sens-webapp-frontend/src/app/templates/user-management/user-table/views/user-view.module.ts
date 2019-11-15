import { NgModule } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { EditProfileViewComponent } from './edit-profile-view/edit-profile-view.component';
import { LabelViewComponent } from './label-view/label-view.component';
import { RolesViewComponent } from './roles-view/roles-view.component';
import { StatusViewComponent } from './status-view/status-view.component';
import { UserNameViewComponent } from './user-name-view/user-name-view.component';
import { UserTypeViewComponent } from './user-type-view/user-type-view.component';
import {DisplayNameViewComponent} from '@app/templates/user-management/user-table/views/display-name-view/display-name-view.component';

@NgModule({
  imports: [
    SharedModule,
  ],
  declarations: [
    EditProfileViewComponent,
    LabelViewComponent,
    RolesViewComponent,
    StatusViewComponent,
    UserNameViewComponent,
    DisplayNameViewComponent,
    UserTypeViewComponent
  ],
  entryComponents: [
    EditProfileViewComponent,
    LabelViewComponent,
    RolesViewComponent,
    StatusViewComponent,
    UserNameViewComponent,
    DisplayNameViewComponent,
    UserTypeViewComponent
  ]
})
export class UserViewModule {}

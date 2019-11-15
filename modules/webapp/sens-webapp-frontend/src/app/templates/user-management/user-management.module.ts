import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { UserManagementClient } from './user-management-client';
import { UserManagementComponent } from './user-management.component';
import { EditUserProfileModule } from './user-profile/edit-user-profile/edit-user-profile.module';
import { NewUserProfileModule } from './user-profile/new-user-profile/new-user-profile.module';
import { UserTableModule } from './user-table/user-table.module';
import { StoreModule } from '@ngrx/store';
import * as fromUserManagement from './store/reducers/userManagement.reducer';
import { EffectsModule } from '@ngrx/effects';
import { UserManagementEffects } from './store/effects/userManagement.effects';
import { HighlightSearchPipe } from './highlight-search.pipe';

@NgModule({
  imports: [
    SharedModule,
    NewUserProfileModule,
    EditUserProfileModule,
    UserTableModule,
    StoreModule.forFeature('userManagement', fromUserManagement.reducer),
    EffectsModule.forFeature([UserManagementEffects])
  ],
  providers: [
    UserManagementClient
  ],
  declarations: [
    UserManagementComponent
  ],
  exports: [
    UserManagementComponent
  ]
})
export class UserManagementModule {}

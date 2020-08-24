import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserManagementListService } from '@core/user-managemenet/services/user-management-list.service';
import { UserManagementSearchService } from '@core/user-managemenet/services/user-management-search.service';
import { UserManagementListEffects } from '@core/user-managemenet/store/effects/user-management-list.effects';
import { UserManagementSearchEffects } from '@core/user-managemenet/store/effects/user-management-search.effects';
import { userManagementListReducer } from '@core/user-managemenet/store/reducers/user-management-list.reducer';
import { userManagementSearchReducer } from '@core/user-managemenet/store/reducers/user-management-search.reducer';
import { USER_MANAGEMENT_LIST_STATE_NAME } from '@core/user-managemenet/store/user-management-list.state';
import { USER_MANAGEMENT_SEARCH_STATE_NAME } from '@core/user-managemenet/store/user-management-search.state';
import { EndpointModule } from '@endpoint/endpoint.module';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    EndpointModule,
    StoreModule.forFeature(USER_MANAGEMENT_LIST_STATE_NAME, userManagementListReducer),
    StoreModule.forFeature(USER_MANAGEMENT_SEARCH_STATE_NAME, userManagementSearchReducer),
    EffectsModule.forFeature([UserManagementListEffects, UserManagementSearchEffects])
  ],
  providers: [
    UserManagementSearchService,
    UserManagementListService
  ]
})
export class UserManagemenetModule { }

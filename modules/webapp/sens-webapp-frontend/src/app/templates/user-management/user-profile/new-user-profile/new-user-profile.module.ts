import { NgModule } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { NewUserProfileComponent } from './new-user-profile.component';
import { NewUserProfileService } from './new-user-profile.service';
import { NewUserTemplateModule } from './new-user-template/new-user-template.module';

@NgModule({
  imports: [
    SharedModule,
    NewUserTemplateModule
  ],
  declarations: [
    NewUserProfileComponent,
  ],
  providers: [
    NewUserProfileService,
  ],
  exports: [
    NewUserProfileComponent
  ]
})
export class NewUserProfileModule {}

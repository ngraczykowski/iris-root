import { NgModule } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { EditUserProfileComponent } from './edit-user-profile.component';
import { EditUserProfileService } from './edit-user-profile.service';
import { EditUserTemplateModule } from './edit-user-template/edit-user-template.module';

@NgModule({
  imports: [
    SharedModule,
    EditUserTemplateModule
  ],
  declarations: [
    EditUserProfileComponent,
  ],
  providers: [
    EditUserProfileService,
  ],
  exports: [
    EditUserProfileComponent
  ]
})
export class EditUserProfileModule {}

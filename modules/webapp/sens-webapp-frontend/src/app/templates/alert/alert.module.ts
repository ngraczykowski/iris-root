import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { AlertDetailsComponent } from './alert-details/alert-details.component';
import { AlertComponent } from './alert.component';
import { AlertService } from './alert.service';

@NgModule({
  imports: [
    SharedModule
  ],
  providers: [
    AlertService
  ],
  declarations: [
    AlertComponent,
    AlertDetailsComponent
  ],
  exports: [
    AlertComponent
  ]
})
export class AlertModule {}

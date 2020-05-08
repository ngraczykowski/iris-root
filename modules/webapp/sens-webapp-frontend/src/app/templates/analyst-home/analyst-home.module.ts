import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { AnalystHomeComponent } from './analyst-home.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    AnalystHomeComponent
  ],
  exports: [
    AnalystHomeComponent
  ]
})
export class AnalystHomeModule {}

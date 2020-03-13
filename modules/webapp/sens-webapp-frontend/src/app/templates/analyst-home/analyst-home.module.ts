import { NgModule } from '@angular/core';
import { ApplicationHeaderModule } from '@app/components/application-header/application-header.module';
import { SharedModule } from '@app/shared/shared.module';
import { AnalystHomeComponent } from './analyst-home.component';

@NgModule({
  imports: [
    SharedModule,
    ApplicationHeaderModule
  ],
  declarations: [
    AnalystHomeComponent
  ],
  exports: [
    AnalystHomeComponent
  ]
})
export class AnalystHomeModule {}

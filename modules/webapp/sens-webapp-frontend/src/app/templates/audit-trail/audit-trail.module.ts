import { NgModule } from '@angular/core';
import { ApplicationHeaderModule } from '@app/components/application-header/application-header.module';
import { SharedModule } from '../../shared/shared.module';
import { AuditTrailComponent } from './audit-trail.component';
import { GenerateReportComponent } from './generate-report/generate-report.component';

@NgModule({
  imports: [
    SharedModule,
    ApplicationHeaderModule
  ],
  declarations: [
    GenerateReportComponent,
    AuditTrailComponent
  ],
  exports: [
    AuditTrailComponent
  ]
})
export class AuditTrailModule {}

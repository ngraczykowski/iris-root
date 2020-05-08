import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { AuditTrailComponent } from './audit-trail.component';
import { GenerateReportComponent } from './generate-report/generate-report.component';

@NgModule({
  imports: [
    SharedModule
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

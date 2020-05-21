import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { SecurityMatrixComponent } from './audit-trail.component';
import { GenerateReportComponent } from './generate-report/generate-report.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    GenerateReportComponent,
    SecurityMatrixComponent
  ],
  exports: [
    SecurityMatrixComponent
  ]
})
export class SecurityMatrixModule {}

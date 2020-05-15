import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatStepperModule } from '@angular/material/stepper';
import { UiComponentsModule } from '@app/ui-components/ui-components.module';
import { TranslateModule } from '@ngx-translate/core';
import { AuditTrailComponent } from './containers/audit-trail/audit-trail.component';
import { ConfigureAuditTrailComponent } from './components/configure-audit-trail/configure-audit-trail.component';
import { DownloadAuditTrailComponent } from './components/download-audit-trail/download-audit-trail.component';
import { GenerateAuditTrailComponent } from './components/generate-audit-trail/generate-audit-trail.component';

@NgModule({
  declarations: [
    AuditTrailComponent,
    ConfigureAuditTrailComponent,
    DownloadAuditTrailComponent,
    GenerateAuditTrailComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatStepperModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatProgressSpinnerModule,
    UiComponentsModule,
    TranslateModule
  ],
  providers: [
    MatDatepickerModule,
    MatNativeDateModule
  ]
})
export class AuditTrailModule { }

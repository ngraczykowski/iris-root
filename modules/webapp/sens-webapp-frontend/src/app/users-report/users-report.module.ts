import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
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
import { UsersReportComponent } from './containers/users-report/users-report.component';
import { ConfigureStepComponent } from './components/configure-step/configure-step.component';
import { DownloadStepComponent } from './components/download-step/download-step.component';
import { GenerateStepComponent } from './components/generate-step/generate-step.component';

@NgModule({
  declarations: [
    UsersReportComponent,
    ConfigureStepComponent,
    DownloadStepComponent,
    GenerateStepComponent
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
  ]
})
export class UsersReportModule { }

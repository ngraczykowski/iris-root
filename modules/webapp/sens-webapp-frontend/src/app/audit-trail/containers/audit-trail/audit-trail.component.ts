import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { GenerateReportPayload } from '@app/audit-trail/models/generate-report-payload';
import { AuditTrailService } from '@app/audit-trail/services/audit-trail.service';
import { MatStepper } from '@angular/material/stepper';
import { Subscription } from 'rxjs';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-audit-trail',
  templateUrl: './audit-trail.component.html',
  styleUrls: ['./audit-trail.component.scss']
})
export class AuditTrailComponent implements OnInit, OnDestroy {
  @ViewChild(MatStepper, { static: true }) stepper: MatStepper;

  inProgress = false;
  error = false;

  generateReportSubscription: Subscription;

  constructor(
    private auditTrailService: AuditTrailService
  ) { }

  ngOnInit() {
  }

  ngOnDestroy() {
    if (this.generateReportSubscription) {
      this.generateReportSubscription.unsubscribe();
    }
  }

  generateReport(formData: GenerateReportPayload) {
    if (formData.endDate && formData.startDate) {
      this.inProgress = true;
      this.error = false;

      this.generateReportSubscription =
        this.auditTrailService.getReport(formData).subscribe(
          (data: any) => {
            this.inProgress = false;
            this.stepper.next();
            this.downloadReport(data);
          },
          () => this.error = true
        );
    }
  }

  abortGenerateReport() {
    this.generateReportSubscription.unsubscribe();
    this.inProgress = false;
    this.error = false;
    this.stepper.reset();
  }

  downloadReport(data) {
    const blob = new Blob([data.file], { type: 'text/csv' });
    FileSaver.saveAs(blob, data.filename);
  }

  resetStepper() {
    this.stepper.reset();
  }
}

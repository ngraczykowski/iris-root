import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatStepper } from '@angular/material/stepper';
import { Header } from '@app/ui-components/header/header';
import { ReportService } from '@app/users-report/services/report.service';
import * as FileSaver from 'file-saver';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-users-report',
  templateUrl: './users-report.component.html',
  styleUrls: ['./users-report.component.scss']
})
export class UsersReportComponent implements OnInit, OnDestroy {
  @ViewChild(MatStepper, { static: true }) stepper: MatStepper;

  inProgress = false;
  error = false;

  generateReportSubscription: Subscription;

  header: Header = {
    title: 'usersReport.title',
  };

  constructor(
    private reportService: ReportService
  ) { }

  ngOnInit() {
  }

  ngOnDestroy() {
    if (this.generateReportSubscription) {
      this.generateReportSubscription.unsubscribe();
    }
  }

  generateReport() {
    this.inProgress = true;
    this.generateReportSubscription =
      this.reportService.getReport().subscribe(
        (data: any) => {
          this.inProgress = false;
          this.stepper.next();
          this.downloadReport(data);
        },
        () => this.error = true
      );
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

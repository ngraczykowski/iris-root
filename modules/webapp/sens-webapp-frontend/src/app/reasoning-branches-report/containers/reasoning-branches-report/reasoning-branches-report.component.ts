import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatStepper } from '@angular/material/stepper';
import { ConfigureReasoningBranchesReportComponent } from '@app/reasoning-branches-report/components/configure-reasoning-branches-report/configure-reasoning-branches-report.component';
import { ReasoningBranchesReportService } from '@app/reasoning-branches-report/services/reasoning-branches-report.service';
import { DialogComponent } from '@app/ui-components/dialog/dialog.component';
import { Header } from '@app/ui-components/header/header';
import { StateContent } from '@app/ui-components/state/state';
import { Subscription } from 'rxjs';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-reasoning-branches-report',
  templateUrl: './reasoning-branches-report.component.html',
  styleUrls: ['./reasoning-branches-report.component.scss']
})
export class ReasoningBranchesReportComponent implements OnInit {
  @ViewChild(MatStepper, {static: true}) stepper: MatStepper;
  @ViewChild(ConfigureReasoningBranchesReportComponent, {static: false}) reportConfig: ConfigureReasoningBranchesReportComponent;

  inProgress = false;

  translatePrefix = 'reasoningBranchesReport.';
  inProgressTranslatePrefix = this.translatePrefix + 'generate.';
  downloadTranslatePrefix = this.translatePrefix + 'download.';
  verificationFailedDialogTranslatePrefix = this.translatePrefix + 'configure.verificationFailedDialog.';

  stateInProgress: StateContent = {
    inProgress: true,
    title: this.inProgressTranslatePrefix + 'title',
    description: this.inProgressTranslatePrefix + 'description',
    button: this.inProgressTranslatePrefix + 'button'
  };

  stateDownload: StateContent = {
    title: this.downloadTranslatePrefix + 'title',
    description: this.downloadTranslatePrefix + 'description',
    button: this.downloadTranslatePrefix + 'button'
  };

  header: Header = {
    title: 'reasoningBranchesReport.title',
  };

  showForm = true;

  generateReportSubscription: Subscription;

  constructor(
      public dialog: MatDialog,
      private reasoningBranchesReportService: ReasoningBranchesReportService,
      private changeDetector: ChangeDetectorRef
  ) { }

  ngOnInit() {
  }

  showNextStep() {
    this.stepper.next();
  }

  showPreviousStep() {
    this.stepper.previous();
  }

  newReasoningBranchReport() {
    this.resetComponent();
    this.stepper.reset();
  }

  resetComponent() {
    this.showForm = false;
    this.changeDetector.detectChanges();
    this.showForm = true;
  }

  generateReport(decisionTreeID) {
    this.generateReportSubscription =
        this.reasoningBranchesReportService.getReport(decisionTreeID)
            .subscribe(
                (data: any) => {
                  this.inProgress = false;
                  this.showNextStep();
                  this.downloadReport(data);
                },
                () => {
                  this.showPreviousStep();
                  this.openDialog();
                  this.reportConfig.invalidDecisionTreeID();
                }
            );
  }

  openDialog(): void {
    this.dialog.open(DialogComponent, {
      width: '450px',
      data: {
        title: this.verificationFailedDialogTranslatePrefix + 'title',
        description: this.verificationFailedDialogTranslatePrefix + 'description',
        buttonClose: this.verificationFailedDialogTranslatePrefix + 'buttonClose'
      }
    });
  }

  downloadReport(data) {
    const blob = new Blob([data.file], {type: 'text/csv'});
    FileSaver.saveAs(blob, data.filename);
  }

  abortGenerateReport() {
    this.generateReportSubscription.unsubscribe();
    this.inProgress = false;
    this.stepper.reset();
  }
}

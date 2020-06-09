import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatStepper } from '@angular/material/stepper';
import { Header } from '@app/ui-components/header/header';
import { StateContent } from '@app/ui-components/state/state';

@Component({
  selector: 'app-reasoning-branches-report',
  templateUrl: './reasoning-branches-report.component.html',
  styleUrls: ['./reasoning-branches-report.component.scss']
})
export class ReasoningBranchesReportComponent implements OnInit {
  @ViewChild(MatStepper, { static: true }) stepper: MatStepper;

  translatePrefix = 'reasoningBranchesReport.';
  inProgressTranslatePrefix = this.translatePrefix + 'generate.';
  downloadTranslatePrefix = this.translatePrefix + 'download.';

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


  constructor() { }

  ngOnInit() {
  }

  showNextStep() {
    this.stepper.next();
  }

  showPreviousStep() {
    this.stepper.previous();
  }

  newReasoningBranchReport() {
    this.stepper.selectedIndex = 0;
  }
}

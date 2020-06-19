import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Header } from '@app/ui-components/header/header';
import { StateContent } from '@app/ui-components/state/state';
import { MatStepper } from '@angular/material/stepper';

@Component({
  selector: 'app-change-request',
  templateUrl: './change-request.component.html',
  styleUrls: ['./change-request.component.scss']
})
export class ChangeRequestComponent implements OnInit {
  @ViewChild(MatStepper, {static: true}) stepper: MatStepper;

  header: Header = {
    title: 'changeRequest.title',
  };

  stepsPrefix = 'changeRequest.steps.';

  changesConfirmation: StateContent = {
    centered: false,
    inProgress: false,
    image: null,
    title: 'changeRequest.confirmation.title',
    description: 'changeRequest.confirmation.description'
  };

  showForm = true;

  constructor(
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

  newChangeRequest() {
    this.showForm = false;
    this.changeDetector.detectChanges();
    this.showForm = true;
    this.stepper.reset();
  }
}

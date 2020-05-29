import { Component, OnInit, ViewChild, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { RbVerificationDialogComponent } from '@app/change-request/components/rb-verification-dialog/rb-verification-dialog.component';
import { FeatureVectorSignaturesErrorResponse } from '@app/change-request/models/feature-vector-signatures';
import { StateContent } from '@app/ui-components/state/state';
import { ChangeRequestService } from '@app/change-request/services/change-request.service';
import { MatStepper } from '@angular/material/stepper';
import { ValidateBranchIdsErrorResponse } from '@app/change-request/models/validate-branch-ids';

@Component({
  selector: 'app-select-reasoning-branch-container',
  templateUrl: './select-reasoning-branch-container.component.html'
})
export class SelectReasoningBranchContainerComponent implements OnInit {
  @ViewChild(MatStepper, {static: true}) stepper: MatStepper;
  @Output() goNextStep = new EventEmitter();

  translatePrefix = 'changeRequest.select.';

  verificationError = {
    margin: true,
    title: this.translatePrefix + 'verificationError.title',
    description: this.translatePrefix + 'verificationError.description',
    elements: [],
  };

  rbVerification: StateContent = {
    centered: false,
    inProgress: true,
    image: null,
    title: this.translatePrefix + 'verifying.title',
    description: this.translatePrefix + 'verifying.description',
  };

  verifingInProgress = false;
  verifingError = false;

  constructor(
    public dialog: MatDialog,
    private changeRequestService: ChangeRequestService
  ) { }

  ngOnInit() {
  }

  openDialog(): void {
    this.dialog.open(RbVerificationDialogComponent, {
      width: '450px',
      data: {
        title: this.translatePrefix + 'dialogVerificationError.title',
        description: this.translatePrefix + 'dialogVerificationError.description',
        cta: this.translatePrefix + 'dialogVerificationError.cta'
      }
    });
  }

  validate(formValue) {
    this.verifingError = false;
    this.verifingInProgress = true;
    if (formValue.reasoningBranchIds) {
      this.validateBranchIds(formValue);
    } else {
      this.validateBranchSignature(formValue);
    }
  }

  validateBranchIds(formValue) {
    this.changeRequestService.validateBranches(
      {
        decisionTreeId: formValue.decisionTreeId,
        branchIds: this.changeRequestService.parseBranchIds(formValue.reasoningBranchIds)
      }
    ).subscribe(
      (response) => {
        this.validationSuccess(response);
      },
      ({error}: ValidateBranchIdsErrorResponse) => {
        this.verificationError.elements = error.extras.branchIds;
        this.validationFail();
      }
    );
  }

  validateBranchSignature(formValue) {
    this.changeRequestService.validateFeatureVectorSignatures(
      {
        decisionTreeId: formValue.decisionTreeId,
        featureVectorSignatures: this.changeRequestService.parseFeatureVectorSignatures(formValue.reasoningBranchSignature)
      }
    ).subscribe(
      (response) => {
        this.validationSuccess(response);
      },
      ({error}: FeatureVectorSignaturesErrorResponse) => {
        this.verificationError.elements = error.extras.featureVectorSignatures;
        this.validationFail();
      }
    );
  }

  validationSuccess(response) {
    this.changeRequestService.setReasoningBranchIdsData(response);
    this.goNextStep.emit();
    this.verifingInProgress = false;
  }

  validationFail() {
    this.openDialog();
    this.verifingInProgress = false;
    this.verifingError = true;
  }

  formReset() {
    this.verifingError = false;
  }
}

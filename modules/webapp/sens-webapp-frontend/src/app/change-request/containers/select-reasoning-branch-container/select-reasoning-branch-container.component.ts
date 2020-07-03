import { HttpErrorResponse } from '@angular/common/http';
import { Component, ViewChild, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { RbVerificationDialogComponent } from '@app/change-request/components/rb-verification-dialog/rb-verification-dialog.component';
import { SelectReasoningBranchesFormComponent } from '@app/change-request/components/select-reasoning-branches-form/select-reasoning-branches-form.component';
import { ErrorMapper } from '@app/shared/http/error-mapper';
import { StateContent } from '@app/ui-components/state/state';
import { ChangeRequestService } from '@app/change-request/services/change-request.service';
import { MatStepper } from '@angular/material/stepper';
import { ValidateBranchIdsErrorResponse } from '@app/change-request/models/validate-branch-ids';
import { DecisionTreesService } from '@core/decision-trees/services/decision-trees.service';
import {
  DECISION_TREE_EXISTS_VALIDATOR_ERROR,
} from '@core/decision-trees/validators/decision-tree-exists.validator';
import { concat, Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-select-reasoning-branch-container',
  templateUrl: './select-reasoning-branch-container.component.html'
})
export class SelectReasoningBranchContainerComponent {
  @ViewChild(MatStepper, {static: true}) stepper: MatStepper;
  @Output() goNextStep = new EventEmitter();

  @ViewChild(SelectReasoningBranchesFormComponent, {static: true})
  selectReasoningBranchesForm: SelectReasoningBranchesFormComponent;

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
      private changeRequestService: ChangeRequestService,
      private decisionTreesService: DecisionTreesService
  ) {}

  openDialog(dialogConfigData: any): void {
    this.dialog.open(RbVerificationDialogComponent, {
      width: '450px',
      data: dialogConfigData
    });
  }

  submit(formValue) {
    this.verifingError = false;
    this.verifingInProgress = true;

    concat(
        this.validateDecisionTree(formValue.decisionTreeId),
        formValue.reasoningBranchIds ?
            this.validateBranchIds(formValue) :
            this.validateBranchSignature(formValue)
    ).subscribe(() => {}, () => {}, () => {
      this.save(formValue);
    });
  }

  validateDecisionTree(decisionTreeId: any): Observable<any> {
    return this.decisionTreesService.getDecisionTree(decisionTreeId).pipe(
        catchError(() => {
          this.treeValidationFail();
          return throwError(null);
        })
    );
  }

  validateBranchIds(formValue): Observable<any> {
    return this.changeRequestService.validateBranches(
      {
        decisionTreeId: formValue.decisionTreeId,
        branchIds: this.changeRequestService.parseBranchIds(formValue.reasoningBranchIds)
      }
    ).pipe(
        catchError((errorResponse: ValidateBranchIdsErrorResponse) => {
          if (ErrorMapper.hasErrorKey(errorResponse.error, 'BranchIdsNotFound')) {
            this.branchValidationFail(errorResponse.error.extras.branchIds);
          } else {
            this.validationFails();
          }
          return throwError(errorResponse);
        })
    );
  }

  validateBranchSignature(formValue): Observable<any> {
    return this.changeRequestService.validateFeatureVectorSignatures(
      {
        decisionTreeId: formValue.decisionTreeId,
        featureVectorSignatures: this.changeRequestService
            .parseFeatureVectorSignatures(formValue.reasoningBranchSignature)
      }
    ).pipe(
        catchError((errorResponse: ValidateBranchIdsErrorResponse) => {
          if (ErrorMapper.hasErrorKey(errorResponse.error, 'BranchIdsNotFound')) {
            this.branchValidationFail(errorResponse.error.extras.featureVectorSignatures);
          } else {
            this.validationFails();
          }
          return throwError(errorResponse);
        })
    );
  }

  save(response) {
    this.goNextStep.emit();
    this.verifingInProgress = false;
    this.changeRequestService.setReasoningBranchIdsData(response);
  }

  treeValidationFail(): void {
    this.selectReasoningBranchesForm.setErrors({
      [DECISION_TREE_EXISTS_VALIDATOR_ERROR]: true
    });
    this.openDialog({
      title: this.translatePrefix + 'treeVerificationError.title',
      description: this.translatePrefix + 'treeVerificationError.description',
      cta: this.translatePrefix + 'treeVerificationError.cta'
    });
    this.validationFails();
  }

  branchValidationFail(elements): void {
    this.verificationError.elements = elements;
    this.openDialog({
      title: this.translatePrefix + 'dialogVerificationError.title',
      description: this.translatePrefix + 'dialogVerificationError.description',
      cta: this.translatePrefix + 'dialogVerificationError.cta'
    });
    this.verifingError = true;
    this.validationFails();
  }

  validationFails() {
    this.verifingInProgress = false;
  }

  formReset() {
    this.verifingError = false;
  }
}

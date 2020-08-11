import { Component, Output, EventEmitter } from '@angular/core';
import { FormGroupDirective, ValidationErrors } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { SelectReasoningBranchesFormService } from '@app/change-request/services/select-reasoning-branches-form.service';
import { DECISION_TREE_EXISTS_VALIDATOR_ERROR } from '@core/decision-trees/validators/decision-tree-exists.validator';
import { environment } from '@env/environment';

@Component({
  selector: 'app-select-reasoning-branches',
  templateUrl: './select-reasoning-branches-form.component.html',
  providers: [SelectReasoningBranchesFormService]
})
export class SelectReasoningBranchesFormComponent {
  @Output() formSubmitted = new EventEmitter();
  @Output() formReset = new EventEmitter();

  branchesLimit: number = SelectReasoningBranchesFormService.MAX_NUMBER_OF_BRANCHES;
  decisionTreeExistsErrorCode: string = DECISION_TREE_EXISTS_VALIDATOR_ERROR;

  branchesIdsStateMatcher: ErrorStateMatcher =
      this.selectReasoningBranchesFormService.createBranchesInputStateMatcher();

  branchesSignaturesStateMatcher: ErrorStateMatcher =
      this.selectReasoningBranchesFormService.createBranchesInputStateMatcher();

  form = this.selectReasoningBranchesFormService.build();

  translatePrefix = 'changeRequest.select.';

  constructor(
      private activatedRoute: ActivatedRoute,
      private selectReasoningBranchesFormService: SelectReasoningBranchesFormService
  ) {
    this.activatedRoute.queryParams.subscribe(par => {
      this.loadReasoningBranchFromUrl(par['dt_id'], par['fv_ids']);
    });
  }

  public setErrors(errors: ValidationErrors): void {
    this.selectReasoningBranchesFormService.setErrors(errors);
  }

  loadReasoningBranchFromUrl(decisionTreeId, reasoningBranchId) {
    if (!(decisionTreeId && reasoningBranchId)) {
      return;
    }
    const dtId = parseInt(decisionTreeId, environment.decimal);
    const rbIds = reasoningBranchId.split(',').map(Number);

    if (!isNaN(dtId) && !rbIds.some(isNaN)) {
      this.form.controls.decisionTreeId.setValue(dtId);
      this.form.controls.reasoningBranchIds.setValue(rbIds.join('\n'));
    }
  }

  onFormSubmit(): void {
    if (this.form.valid) {
      this.formSubmitted.emit(this.selectReasoningBranchesFormService.getFormValue());
    } else {
      this.form.updateValueAndValidity();
    }
  }

  resetForm(formDir: FormGroupDirective): void {
    this.selectReasoningBranchesFormService.reset();
    this.formReset.emit();
  }
}

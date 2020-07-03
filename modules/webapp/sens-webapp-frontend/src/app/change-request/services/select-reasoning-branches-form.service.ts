import { Injectable, OnDestroy } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { DECISION_TREE_EXISTS_VALIDATOR_ERROR } from '@core/decision-trees/validators/decision-tree-exists.validator';
import { Subscription } from 'rxjs';
import { startWith } from 'rxjs/operators';

@Injectable()
export class SelectReasoningBranchesFormService implements OnDestroy {

  private formModel = {
    decisionTreeId: ['', [Validators.required]],
    group: ['id'],
    reasoningBranchIds: [''],
    reasoningBranchSignature: ['']
  };

  private form: FormGroup;
  private sub: Subscription;

  constructor(private formBuilder: FormBuilder) { }

  public build(): FormGroup {
    this.form = this.formBuilder.group(this.formModel);
    const groupControl: AbstractControl = this.form.get('group');
    this.sub = groupControl.valueChanges.pipe(
        startWith(groupControl.value as string)
    ).subscribe((value: string) => this.switchValidators(value));
    return this.form;
  }

  public getFormValue(): Object {
    const group: string = this.form.get('group').value;
    return {
      decisionTreeId: this.form.get('decisionTreeId').value,
      ...(group === 'id' ? {
        reasoningBranchIds: this.form.get('reasoningBranchIds').value
      } : {
        reasoningBranchSignature: this.form.get('reasoningBranchSignature').value
      })
    };
  }

  public reset(): void {
    this.form.reset();
    this.form.markAsPristine();
    Object.keys(this.form.controls).forEach(key => {
      this.form.get(key).setErrors(null);
      this.form.get(key).setValue('');
    });
    this.form.setErrors(null);
  }

  public setErrors(errors: ValidationErrors): void {
    if (errors[DECISION_TREE_EXISTS_VALIDATOR_ERROR]) {
      this.form.get('decisionTreeId').setErrors(errors);
    }
  }

  public ngOnDestroy(): void {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  private switchValidators(group: string) {

    this.form.get('reasoningBranchIds').setValidators(group === 'id' ? [
      Validators.required,
      Validators.maxLength(1000)] : []);

    this.form.get('reasoningBranchSignature').setValidators(group === 'id' ? [] :
        [Validators.required, Validators.maxLength(1000)]);

  }
}

import { Injectable, OnDestroy } from '@angular/core';
import {
  AbstractControl,
  FormBuilder, FormControl,
  FormGroup, FormGroupDirective, NgForm,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material';
import { LifecycleHelper } from '@app/shared/helpers/lifecycle-helper';
import { DECISION_TREE_EXISTS_VALIDATOR_ERROR } from '@core/decision-trees/validators/decision-tree-exists.validator';
import { ReasoningBranchParser } from '@core/reasoning-branches/utils/reasoning-branch-parser';
import { combineLatest, Observable } from 'rxjs';
import { debounceTime, startWith, takeUntil } from 'rxjs/operators';

@Injectable()
export class SelectReasoningBranchesFormService implements OnDestroy {

  public static MAX_NUMBER_OF_BRANCHES: number = 100;

  private formModel = {
    decisionTreeId: ['', [Validators.required]],
    group: ['id'],
    reasoningBranchIds: [''],
    reasoningBranchSignature: [''],
    numberOfParsedBranches: [0,
      [Validators.max(SelectReasoningBranchesFormService.MAX_NUMBER_OF_BRANCHES)]]
  };

  private form: FormGroup;
  private lifecycle: LifecycleHelper = new LifecycleHelper();

  constructor(private formBuilder: FormBuilder) { }

  public build(): FormGroup {
    this.form = this.formBuilder.group(this.formModel);

    const groupControl: AbstractControl = this.form.get('group');
    groupControl.valueChanges.pipe(
        startWith(groupControl.value as string),
        takeUntil(this.lifecycle.destroyed)
    ).subscribe((value: string) => this.switchValidators(value));

    combineLatest([
      this.getFormValueAsState('group'),
      this.getFormValueAsState('reasoningBranchIds'),
      this.getFormValueAsState('reasoningBranchSignature')]).pipe(
        debounceTime(1),
        takeUntil(this.lifecycle.destroyed)
    ).subscribe(
          ([group, reasoningBranchIds, reasoningBranchSignature]) =>
                    this.parseBranches(group, reasoningBranchIds, reasoningBranchSignature));

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

  public createBranchesInputStateMatcher(): ErrorStateMatcher {
    return {
      isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        const invalidCtrl = !!(control && control.invalid);
        const invalidParent = !!(control && control.parent
            && control.parent.controls['numberOfParsedBranches'].errors);
        return (invalidCtrl || invalidParent) && control.touched;
      }
    };
  }

  public ngOnDestroy(): void {
    this.lifecycle.destroy();
  }

  private getFormValueAsState(controlName: string): Observable<any> {
    return this.form.get(controlName).valueChanges
        .pipe(startWith(this.form.get(controlName).value as String));
  }

  private parseBranches(group: string, reasoningBranchIds: string,
                        reasoningBranchSignature: string): void {

    const parsedBranches: number[] = group === 'id' ?
        ReasoningBranchParser.parseIds(reasoningBranchIds) :
        ReasoningBranchParser.parseSignatures(reasoningBranchSignature);

    this.form.get('numberOfParsedBranches').setValue(parsedBranches.length);
  }

  private switchValidators(group: string) {

    this.form.get('reasoningBranchIds').setValidators(group === 'id' ?
        [Validators.required] : []);

    this.form.get('reasoningBranchSignature').setValidators(group === 'id' ? [] :
        [Validators.required]);

  }

}

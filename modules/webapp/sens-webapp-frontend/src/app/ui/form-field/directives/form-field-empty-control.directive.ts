import { Directive, DoCheck, Inject, Input, Optional, Self } from '@angular/core';
import { FormGroupDirective, NgControl, NgForm } from '@angular/forms';
import {
  ErrorStateMatcher,
  MAT_INPUT_VALUE_ACCESSOR,
  MatFormFieldControl, mixinErrorState
} from '@angular/material';
import { Subject } from 'rxjs';

class ControlBase {
  constructor(public _defaultErrorStateMatcher: ErrorStateMatcher,
              public _parentForm: NgForm,
              public _parentFormGroup: FormGroupDirective,
              public ngControl: NgControl) {}
}

@Directive({
  selector: '[formFieldEmptyControl]',
  providers: [{provide: MatFormFieldControl, useExisting: FormFieldEmptyControlDirective }]
})
export class FormFieldEmptyControlDirective extends mixinErrorState(ControlBase)
    implements MatFormFieldControl<any>, DoCheck {

  constructor(@Optional() @Self() @Inject(MAT_INPUT_VALUE_ACCESSOR) private inputValueAccessor: any,
              @Optional() @Self() ngControl: NgControl,
              @Optional() _parentForm: NgForm,
              @Optional() _parentFormGroup: FormGroupDirective,
              _defaultErrorStateMatcher: ErrorStateMatcher) {
    super(_defaultErrorStateMatcher, _parentForm, _parentFormGroup, ngControl);
  }

  readonly autofilled: boolean = false;
  readonly controlType: string = 'mat-input';
  readonly disabled: boolean = false;
  readonly empty: boolean = true;
  readonly errorState: boolean = true;
  readonly focused: boolean = false;
  readonly id: string;
  readonly ngControl: NgControl | null;
  readonly placeholder: string;
  readonly required: boolean;
  readonly shouldLabelFloat: boolean;
  readonly stateChanges: Subject<void> = new Subject<void>();

  @Input()
  get value(): string { return this.inputValueAccessor.value; }
  set value(value: string) {
    if (value !== this.value) {
      this.inputValueAccessor.value = value;
      this.stateChanges.next();
    }
  }

  onContainerClick(event: MouseEvent): void {
  }

  setDescribedByIds(ids: string[]): void {
  }

  ngDoCheck(): void {
    if (this.ngControl) {
      this.updateErrorState();
    }
  }

}

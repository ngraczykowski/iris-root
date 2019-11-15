import { FormControl, ValidatorFn } from '@angular/forms';

export function matchOtherValidator(otherControlName: string): ValidatorFn {

  let thisControl: FormControl;
  let otherControl: FormControl;

  function isNotValid() {
    return otherControl.value !== thisControl.value;
  }

  function initControls(control) {
    thisControl = control;
    otherControl = thisControl.parent.get(otherControlName) as FormControl;
    otherControl.valueChanges.subscribe(() => thisControl.updateValueAndValidity());
  }

  return (control: FormControl) => {
    if (control.parent) {
      if (!thisControl && !otherControl) {
        initControls(control);
      }

      if (isNotValid()) {
        return {mismatched: true};
      }
    }
    return null;
  };
}

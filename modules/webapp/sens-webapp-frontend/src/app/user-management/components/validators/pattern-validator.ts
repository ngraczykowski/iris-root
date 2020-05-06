import { FormControl, ValidatorFn } from '@angular/forms';

export function patternValidator(name: string, regex: RegExp): ValidatorFn {

  return (control: FormControl) => {
    if (control.value && !regex.test(control.value)) {
      const error = {};
      error[name] = true;
      return error;
    }
    return null;
  };
}


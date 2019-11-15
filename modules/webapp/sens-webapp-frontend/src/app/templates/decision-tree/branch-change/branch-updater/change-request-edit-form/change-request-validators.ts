import { FormControl, FormGroup, ValidatorFn } from '@angular/forms';
import { StatusChange } from '@app/templates/decision-tree/branch-change/branch-updater/change-request.model';

export class ChangeRequestValidators {
  static atLeastOneChangeValidator(): ValidatorFn {
    return createAtLeastOneChangeValidator();
  }
}

function createAtLeastOneChangeValidator(): ValidatorFn {

  let solution: FormControl;
  let status: FormControl;

  return (control: FormGroup) => {
    if (!solution && !status) {
      solution = <FormControl> control.get('solution');
      status = <FormControl> control.get('status');
    }

    if (solution.value === 'NONE' && status.value === StatusChange.NONE) {
      return {noChanges: true};
    }

    return null;
  };
}

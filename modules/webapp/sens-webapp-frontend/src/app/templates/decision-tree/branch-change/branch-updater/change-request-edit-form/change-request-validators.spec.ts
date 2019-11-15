import { FormControl, FormGroup, ValidatorFn } from '@angular/forms';
import { ChangeRequestValidators } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-validators';

describe('ChangeRequestValidators', () => {

  describe('atLeastOneChangeValidator', () => {

    let form: FormGroup;
    let validatorFn: ValidatorFn;

    const NONE_SOLUTION = 'NONE';

    beforeEach(() => {
      validatorFn = ChangeRequestValidators.atLeastOneChangeValidator();

      form = new FormGroup({
        solution: new FormControl(''),
        status: new FormControl('')
      });
    });

    it('should return no errors when solution is not NONE', () => {
      form.get('solution').setValue('FALSE_POSITIVE');
      form.get('status').setValue(NONE_SOLUTION);

      const errors = validatorFn(form);

      expect(errors).toBeFalsy();
    });

    it('should return no errors when status is not NONE', () => {
      form.get('solution').setValue(NONE_SOLUTION);
      form.get('status').setValue('POTENTIAL_TRUE_POSITIVE');

      const errors = validatorFn(form);

      expect(errors).toBeFalsy();
    });

    it('should return errors when solution and status are NONE', () => {
      form.get('solution').setValue(NONE_SOLUTION);
      form.get('status').setValue(NONE_SOLUTION);

      const errors = validatorFn(form);

      expect(errors).toEqual({noChanges: true});
    });
  });
});

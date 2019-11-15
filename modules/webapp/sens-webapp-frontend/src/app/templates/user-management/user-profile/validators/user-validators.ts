import { ValidatorFn, Validators } from '@angular/forms';
import { matchOtherValidator } from './match-other-validator';
import { patternValidator } from './pattern-validator';

export class UserValidators {
  static usernameMinLength(): ValidatorFn {
    return Validators.minLength(3);
  }

  static passwordMinLength(): ValidatorFn {
    return Validators.minLength(8);
  }

  static atLeastOneLetter(): ValidatorFn {
    return patternValidator('minOneLetter', /.*[A-Za-z].*/);
  }

  static atLeastOneDigit(): ValidatorFn {
    return patternValidator('minOneDigit', /.*[0-9].*/);
  }

  static matchPassword(): ValidatorFn {
    return matchOtherValidator('password');
  }
}

import { Injectable } from '@angular/core';

@Injectable()
export class TableHelper {

  hasOneValue(value) {
    return Array.isArray(value) ? value.length === 1 : value;
  }

  hasManyValues(value) {
    return Array.isArray(value) && value.length > 1;
  }

  isEmptyValue(value) {
    return Array.isArray(value) ? value.length === 0 : !value;
  }
}

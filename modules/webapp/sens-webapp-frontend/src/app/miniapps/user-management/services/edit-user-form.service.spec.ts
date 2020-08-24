import { TestBed } from '@angular/core/testing';

import { EditUserFormService } from './edit-user-form.service';

describe('EditUserFormService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: EditUserFormService = TestBed.get(EditUserFormService);
    expect(service).toBeTruthy();
  });
});

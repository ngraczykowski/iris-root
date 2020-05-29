import { TestBed } from '@angular/core/testing';

import { PendingChangesService } from './pending-changes.service';

describe('PendingChangesService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PendingChangesService = TestBed.get(PendingChangesService);
    expect(service).toBeTruthy();
  });
});

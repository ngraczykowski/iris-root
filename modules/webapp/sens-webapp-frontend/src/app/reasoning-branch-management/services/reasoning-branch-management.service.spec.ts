import { TestBed } from '@angular/core/testing';

import { ReasoningBranchManagementService } from './reasoning-branch-management.service';

describe('ReasoningBranchManagementService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ReasoningBranchManagementService = TestBed.get(ReasoningBranchManagementService);
    expect(service).toBeTruthy();
  });
});

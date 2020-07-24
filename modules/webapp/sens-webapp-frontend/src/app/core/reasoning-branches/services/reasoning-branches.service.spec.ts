import { TestBed } from '@angular/core/testing';

import { ReasoningBranchesService } from './reasoning-branches.service';

describe('ReasoningBranchesService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ReasoningBranchesService = TestBed.get(ReasoningBranchesService);
    expect(service).toBeTruthy();
  });
});

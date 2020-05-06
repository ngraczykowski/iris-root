import { TestBed } from '@angular/core/testing';

import { ReasoningBranchManagementService } from './reasoning-branch-management.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ReasoningBranchManagementService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [ HttpClientTestingModule ]
  }));

  it('should be created', () => {
    const service: ReasoningBranchManagementService = TestBed.get(ReasoningBranchManagementService);
    expect(service).toBeTruthy();
  });
});

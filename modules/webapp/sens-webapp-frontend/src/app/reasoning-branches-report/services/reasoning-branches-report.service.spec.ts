import { TestBed } from '@angular/core/testing';

import { ReasoningBranchesReportService } from './reasoning-branches-report.service';

describe('ReasoningBranchesReportService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ReasoningBranchesReportService = TestBed.get(ReasoningBranchesReportService);
    expect(service).toBeTruthy();
  });
});

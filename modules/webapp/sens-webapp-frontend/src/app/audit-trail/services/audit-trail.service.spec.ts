import { TestBed } from '@angular/core/testing';

import { AuditTrailService } from './audit-trail.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('AuditTrailService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule]
  }));

  it('should be created', () => {
    const service: AuditTrailService = TestBed.get(AuditTrailService);
    expect(service).toBeTruthy();
  });
});

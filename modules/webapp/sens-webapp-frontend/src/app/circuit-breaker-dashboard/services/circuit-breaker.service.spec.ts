import { TestBed } from '@angular/core/testing';

import { CircuitBreakerService } from './circuit-breaker.service';

describe('CircuitBreakerService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CircuitBreakerService = TestBed.get(CircuitBreakerService);
    expect(service).toBeTruthy();
  });
});

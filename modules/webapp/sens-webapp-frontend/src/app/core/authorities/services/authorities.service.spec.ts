import { TestBed } from '@angular/core/testing';

import { AuthoritiesService } from './authorities.service';

describe('AuthoritiesService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AuthoritiesService = TestBed.get(AuthoritiesService);
    expect(service).toBeTruthy();
  });
});

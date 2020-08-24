import { TestBed } from '@angular/core/testing';

import { UserManagementEndpointService } from './user-management-endpoint.service';

describe('UserManagementEndpointService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserManagementEndpointService = TestBed.get(UserManagementEndpointService);
    expect(service).toBeTruthy();
  });
});

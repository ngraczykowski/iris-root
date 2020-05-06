import { TestBed } from '@angular/core/testing';

import { UserManagementService } from './user-management.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestModule } from '@app/test/test.module';

describe('UserManagementService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [ UserManagementService, HttpClientTestingModule ],
    imports: [ TestModule ]
  }));

  it('should be created', () => {
    const service: UserManagementService = TestBed.get(UserManagementService);
    expect(service).toBeTruthy();
  });
});

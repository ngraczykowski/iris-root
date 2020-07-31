import { TestBed } from '@angular/core/testing';

import { AiSolutionsService } from './ai-solutions.service';

describe('AiSolutionsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AiSolutionsService = TestBed.get(AiSolutionsService);
    expect(service).toBeTruthy();
  });
});

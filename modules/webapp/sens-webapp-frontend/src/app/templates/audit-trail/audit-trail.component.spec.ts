import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../test/test.module';

import { SecurityMatrixComponent } from './audit-trail.component';
import { SecurityMatrixModule } from './audit-trail.module';
import { WINDOW } from '@app/shared/window.service';
import { NO_ERRORS_SCHEMA } from '@angular/core';

const MockWindow = {
  location: {
    assign(param) { }
  }
};

describe('AuditTrailComponent', () => {
  let component: SecurityMatrixComponent;
  let fixture: ComponentFixture<SecurityMatrixComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SecurityMatrixComponent ],
      imports: [TestModule],
      providers: [
        {
          provide: WINDOW, useValue: MockWindow
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SecurityMatrixComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

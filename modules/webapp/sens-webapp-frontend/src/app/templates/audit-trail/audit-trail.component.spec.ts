import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../test/test.module';

import { AuditTrailComponent } from './audit-trail.component';
import { AuditTrailModule } from './audit-trail.module';
import { WINDOW } from '@app/shared/window.service';

const MockWindow = {
  location: {
    assign(param) { }
  }
};

describe('AuditTrailComponent', () => {
  let component: AuditTrailComponent;
  let fixture: ComponentFixture<AuditTrailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule, AuditTrailModule],
      providers: [
        {
          provide: WINDOW, useValue: MockWindow
        }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuditTrailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

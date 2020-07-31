import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CircuitBreakerBranchListComponent } from './circuit-breaker-branch-list.component';

describe('CircuitBreakerListComponent', () => {
  let component: CircuitBreakerBranchListComponent;
  let fixture: ComponentFixture<CircuitBreakerBranchListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CircuitBreakerBranchListComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CircuitBreakerBranchListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

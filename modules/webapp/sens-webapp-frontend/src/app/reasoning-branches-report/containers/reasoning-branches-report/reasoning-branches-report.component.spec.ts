import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReasoningBranchesReportComponent } from './reasoning-branches-report.component';

describe('ReasoningBranchesReportComponent', () => {
  let component: ReasoningBranchesReportComponent;
  let fixture: ComponentFixture<ReasoningBranchesReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReasoningBranchesReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReasoningBranchesReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

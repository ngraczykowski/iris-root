import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscrepantBranchComponent } from './discrepant-branch.component';

describe('DiscrepantBranchComponent', () => {
  let component: DiscrepantBranchComponent;
  let fixture: ComponentFixture<DiscrepantBranchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DiscrepantBranchComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiscrepantBranchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

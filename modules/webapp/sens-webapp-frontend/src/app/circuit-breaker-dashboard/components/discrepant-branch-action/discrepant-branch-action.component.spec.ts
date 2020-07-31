import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscrepantBranchActionComponent } from './discrepant-branch-action.component';

describe('DiscrepantBranchActionComponent', () => {
  let component: DiscrepantBranchActionComponent;
  let fixture: ComponentFixture<DiscrepantBranchActionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DiscrepantBranchActionComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiscrepantBranchActionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

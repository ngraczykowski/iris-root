import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchViewsModule } from '@app/templates/decision-tree/branch-table/views/branch-views.module';

import { BranchReviewStatusViewDataComponent } from './branch-review-status-view-data.component';

describe('BranchReviewStatusViewDataComponent', () => {
  let component: BranchReviewStatusViewDataComponent;
  let fixture: ComponentFixture<BranchReviewStatusViewDataComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [BranchViewsModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchReviewStatusViewDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';

import { BranchReviewButtonModule } from './branch-review-button/branch-review-button.module';
import { ReasoningBranchDetailsModalModule } from './reasoning-branch-details-changelog-modal/reasoning-branch-details-changelog-modal.module';
import { ReasoningBranchDetailsComponent } from './reasoning-branch-details.component';

describe('ReasoningBranchDetailsComponent', () => {
  let component: ReasoningBranchDetailsComponent;
  let fixture: ComponentFixture<ReasoningBranchDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReasoningBranchDetailsComponent ],
      imports: [ TestModule, ReasoningBranchDetailsModalModule, BranchReviewButtonModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReasoningBranchDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

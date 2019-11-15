import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { BranchViewsModule } from '../branch-views.module';

import { BranchReviewedStatusViewComponent } from './branch-reviewed-status-view.component';

describe('BranchReviewedStatusViewComponent', () => {
  let component: BranchReviewedStatusViewComponent;
  let fixture: ComponentFixture<BranchReviewedStatusViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [BranchViewsModule, RouterTestingModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchReviewedStatusViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    component.data = {
      reviewed: true,
      reviewedAt: '2019-08-08',
      reviewedBy: 'Name',
    };

    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('should show tooltip when the reviewer name is long', () => {
    component.data = {
      reviewed: true,
      reviewedAt: '2019-08-08',
      reviewedBy: 'Very Long Test Reviewer Name',
    };

    fixture.detectChanges();

    const tooltip = fixture.debugElement.query(By.css('.tooltip-layer'));
    expect(tooltip).toBeTruthy();
  });

  it('should not show tooltip when the reviewer name is short', () => {
    component.data = {
      reviewed: true,
      reviewedAt: '2019-08-08',
      reviewedBy: 'Name',
    };

    fixture.detectChanges();

    const tooltip = fixture.debugElement.query(By.css('.tooltip-layer'));
    expect(tooltip).toBeFalsy();
  });
});

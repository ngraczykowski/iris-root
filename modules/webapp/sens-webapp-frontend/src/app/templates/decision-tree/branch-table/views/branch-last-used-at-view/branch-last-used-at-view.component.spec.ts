import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchViewsModule } from '@app/templates/decision-tree/branch-table/views/branch-views.module';

import { BranchLastUsedAtViewComponent } from './branch-last-used-at-view.component';

describe('BranchLastUsedAtViewComponent', () => {
  let component: BranchLastUsedAtViewComponent;
  let fixture: ComponentFixture<BranchLastUsedAtViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [BranchViewsModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchLastUsedAtViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

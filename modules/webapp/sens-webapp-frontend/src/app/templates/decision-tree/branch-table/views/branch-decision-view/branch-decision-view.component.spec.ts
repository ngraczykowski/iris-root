import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchViewsModule } from '../branch-views.module';
import { BranchDecisionViewComponent } from './branch-decision-view.component';

describe('BranchDecisionViewComponent', () => {
  let component: BranchDecisionViewComponent;
  let fixture: ComponentFixture<BranchDecisionViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [BranchViewsModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchDecisionViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

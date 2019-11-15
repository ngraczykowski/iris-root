import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchViewsModule } from '../branch-views.module';
import { BranchScoreViewComponent } from './branch-score-view.component';

describe('BranchScoreViewComponent', () => {
  let component: BranchScoreViewComponent;
  let fixture: ComponentFixture<BranchScoreViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [BranchViewsModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchScoreViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

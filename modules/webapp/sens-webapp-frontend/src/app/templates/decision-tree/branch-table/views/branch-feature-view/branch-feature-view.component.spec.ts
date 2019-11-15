import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchViewsModule } from '../branch-views.module';
import { BranchFeatureViewComponent } from './branch-feature-view.component';

describe('BranchFeatureViewComponent', () => {
  let component: BranchFeatureViewComponent;
  let fixture: ComponentFixture<BranchFeatureViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [BranchViewsModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchFeatureViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

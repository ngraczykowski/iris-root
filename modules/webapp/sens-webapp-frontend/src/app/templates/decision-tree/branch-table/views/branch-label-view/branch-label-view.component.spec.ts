import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchViewsModule } from '../branch-views.module';
import { BranchLabelViewComponent } from './branch-label-view.component';

describe('LabelViewComponent', () => {
  let component: BranchLabelViewComponent;
  let fixture: ComponentFixture<BranchLabelViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [BranchViewsModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchLabelViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

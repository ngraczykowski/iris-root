import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchViewsModule } from '../branch-views.module';
import { BranchIdViewComponent } from './branch-id-view.component';

describe('BranchIdViewComponent', () => {
  let component: BranchIdViewComponent;
  let fixture: ComponentFixture<BranchIdViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [BranchViewsModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchIdViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

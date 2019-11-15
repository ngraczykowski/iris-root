import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SharedModule } from '../../../../../shared/shared.module';

import { BranchStatusViewComponent } from './branch-status-view.component';

describe('BranchStatusViewComponent', () => {
  let component: BranchStatusViewComponent;
  let fixture: ComponentFixture<BranchStatusViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BranchStatusViewComponent ],
      imports: [SharedModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchStatusViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchDetailsComponent } from './branch-details.component';
import { TestModule } from '@app/test/test.module';

describe('BranchDetailsComponent', () => {
  let component: BranchDetailsComponent;
  let fixture: ComponentFixture<BranchDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BranchDetailsComponent ],
      imports: [ TestModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchDetailsComponent);
    component = fixture.componentInstance;
    component.branchDetails = {
      active: true,
      aiSolution: 'false',
      reasoningBranchId: 1
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

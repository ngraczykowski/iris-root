import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../test/test.module';

import { ReasoningBranchFeaturesComponent } from './reasoning-branch-features.component';

describe('ReasoningBranchFeaturesComponent', () => {
  let component: ReasoningBranchFeaturesComponent;
  let fixture: ComponentFixture<ReasoningBranchFeaturesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReasoningBranchFeaturesComponent ],
      imports: [TestModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReasoningBranchFeaturesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

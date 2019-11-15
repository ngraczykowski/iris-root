import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';

import { DecisionTreeInfoDetailsComponent } from './decision-tree-info-details.component';

describe('DecisionTreeInfoDetailsComponent', () => {
  let component: DecisionTreeInfoDetailsComponent;
  let fixture: ComponentFixture<DecisionTreeInfoDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DecisionTreeInfoDetailsComponent ],
      imports: [TestModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeInfoDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

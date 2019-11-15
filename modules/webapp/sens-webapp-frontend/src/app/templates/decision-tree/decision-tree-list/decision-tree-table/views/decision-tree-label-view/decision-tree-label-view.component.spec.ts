import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../../test/test.module';
import { DecisionTreeTableViewsModule } from '../decision-tree-table-views.module';

import { DecisionTreeLabelViewComponent } from './decision-tree-label-view.component';

describe('DecisionTreeLabelViewComponent', () => {
  let component: DecisionTreeLabelViewComponent;
  let fixture: ComponentFixture<DecisionTreeLabelViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            DecisionTreeTableViewsModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeLabelViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

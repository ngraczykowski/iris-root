import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../../test/test.module';
import { DecisionTreeTableViewsModule } from '../decision-tree-table-views.module';

import { DecisionTreeModelViewComponent } from './decision-tree-model-view.component';

describe('DecisionTreeModelViewComponent', () => {
  let component: DecisionTreeModelViewComponent;
  let fixture: ComponentFixture<DecisionTreeModelViewComponent>;

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
    fixture = TestBed.createComponent(DecisionTreeModelViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

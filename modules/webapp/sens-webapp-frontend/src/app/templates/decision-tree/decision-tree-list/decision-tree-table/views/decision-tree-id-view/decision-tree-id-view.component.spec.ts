import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../../test/test.module';
import { DecisionTreeTableViewsModule } from '../decision-tree-table-views.module';

import { DecisionTreeIdViewComponent } from './decision-tree-id-view.component';

describe('DecisionTreeIdViewComponent', () => {
  let component: DecisionTreeIdViewComponent;
  let fixture: ComponentFixture<DecisionTreeIdViewComponent>;

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
    fixture = TestBed.createComponent(DecisionTreeIdViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

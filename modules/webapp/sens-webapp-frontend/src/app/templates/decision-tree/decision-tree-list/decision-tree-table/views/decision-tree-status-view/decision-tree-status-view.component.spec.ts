import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';
import { DecisionTreeTableViewsModule } from '../decision-tree-table-views.module';

import { DecisionTreeStatusViewComponent } from './decision-tree-status-view.component';

describe('DecisionTreeStatusViewComponent', () => {
  let component: DecisionTreeStatusViewComponent;
  let fixture: ComponentFixture<DecisionTreeStatusViewComponent>;

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
    fixture = TestBed.createComponent(DecisionTreeStatusViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

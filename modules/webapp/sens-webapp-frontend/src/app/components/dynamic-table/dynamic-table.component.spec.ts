import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';
import { EMPTY } from 'rxjs';

import { DynamicComponentProvider, DynamicTableComponent } from './dynamic-table.component';

describe('DynamicTableComponent', () => {
  let component: DynamicTableComponent;
  let fixture: ComponentFixture<DynamicTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule]
    }).compileComponents();
  }));

  function initComponent(provider: DynamicComponentProvider) {
    fixture = TestBed.createComponent(DynamicTableComponent);
    component = fixture.componentInstance;
    component.provider = provider;
    fixture.detectChanges();
  }

  it('should create', () => {
    initComponent(() => EMPTY);

    expect(component).toBeTruthy();
  });
});

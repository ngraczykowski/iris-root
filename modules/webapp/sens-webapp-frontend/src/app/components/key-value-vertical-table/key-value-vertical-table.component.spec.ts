import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../test/test.module';

import { KeyValueVerticalTableComponent } from './key-value-vertical-table.component';

describe('KeyValueVerticalTableComponent', () => {
  let component: KeyValueVerticalTableComponent;
  let fixture: ComponentFixture<KeyValueVerticalTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KeyValueVerticalTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

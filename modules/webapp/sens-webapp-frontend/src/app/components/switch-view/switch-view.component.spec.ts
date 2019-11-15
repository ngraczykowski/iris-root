import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../test/test.module';

import { SelectEvent, SwitchViewComponent } from './switch-view.component';

describe('SwitchViewComponent', () => {
  let component: SwitchViewComponent;
  let fixture: ComponentFixture<SwitchViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule],
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SwitchViewComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('should select default item after init', () => {
    spyOn(component.select, 'emit');
    component.items = ['item1', 'item2', 'item3'];
    component.defaultIndex = 2;
    fixture.detectChanges();

    expect(component.isActive('item3')).toBeTruthy();
    expect(component.select.emit).toHaveBeenCalledWith(<SelectEvent> {item: 'item3', index: 2});
  });

  it('should return isActive true only for selected item', () => {
    component.items = ['item1', 'item2', 'item3'];
    fixture.detectChanges();

    component.onClick('item2');

    expect(component.isActive('item1')).toBeFalsy();
    expect(component.isActive('item2')).toBeTruthy();
    expect(component.isActive('item3')).toBeFalsy();
  });

  it('should return valid name when translatePrefix is not set', () => {
    fixture.detectChanges();

    expect(component.getName('item')).toEqual('item');
  });

  it('should return valid name when translatePrefix is set', () => {
    component.translatePrefix = 'prefix-';
    fixture.detectChanges();

    expect(component.getName('item')).toEqual('prefix-item');
  });

  it('should select valid item after item click', () => {
    component.items = ['item1', 'item2', 'item3'];
    fixture.detectChanges();
    spyOn(component.select, 'emit');

    component.onClick('item2');

    expect(component.isActive('item2')).toBeTruthy();
    expect(component.select.emit).toHaveBeenCalledWith(<SelectEvent> {item: 'item2', index: 1});
  });
});

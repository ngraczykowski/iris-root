import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../test/test.module';
import { DropdownSelectorComponent } from './dropdown-selector.component';

describe('DropdownSelectorComponent', () => {
  let component: DropdownSelectorComponent;
  let fixture: ComponentFixture<DropdownSelectorComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DropdownSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be hidden on init', () => {
    expect(component.show).toBeFalsy();
  });

  it('should show on dropdown click', () => {
    component.onDropdownClick();

    expect(component.show).toBeTruthy();
  });

  it('should be hidden on dropdown click twice', () => {
    component.onDropdownClick();
    component.onDropdownClick();

    expect(component.show).toBeFalsy();
  });

  it('should be hidden on select option', () => {
    component.onDropdownClick();

    component.onSelectOption(10);

    expect(component.show).toBeFalsy();
  });

  it('should emit change event on select option', () => {
    spyOn(component.optionChange, 'emit');

    component.onSelectOption(10);

    expect(component.optionChange.emit).toHaveBeenCalledWith(10);
  });

  it('should set rowCountPerPage on select option', () => {
    spyOn(component.optionChange, 'emit');

    component.onSelectOption(10);

    expect(component.option).toEqual(10);
  });
});

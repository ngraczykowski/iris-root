import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { RestrictionPanelModule } from '@app/templates/alert-restrictions-management/restriction-panel/restriction-panel.module';
import { UserManagementClient } from '@app/templates/user-management/user-management-client';
import { TestModule } from '@app/test/test.module';

import { RestrictionPanelEditorComponent } from './restriction-panel-editor.component';

describe('RestrictionPanelEditorComponent', () => {
  let component: RestrictionPanelEditorComponent;
  let fixture: ComponentFixture<RestrictionPanelEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        TestModule,
        RestrictionPanelModule,
        HttpClientTestingModule
      ],
      providers: [
        UserManagementClient
      ]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RestrictionPanelEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show the add header if element identifier is not specified', () => {
    fixture.detectChanges();

    const editHeader = fixture.debugElement.query(By.css('.popup-window-title')).nativeElement;
    expect(editHeader.textContent).toContain('alertRestrictionsManagement.modal.header.titleAdd');
  });

  it('should disabled the action button if restriction name is missing', () => {
    component.restrictionName = '';
    component.restrictionCountries = 'PL, US';
    component.restrictionUnits = 'UNIT_1, UNIT_2';
    component.status = true;
    fixture.detectChanges();

    const actionButton = fixture.debugElement.query(By.css('.button-action')).nativeElement;
    expect(actionButton.disabled).toBeTruthy();
  });

  it('should enabled the action button if restriction name was entered', () => {
    component.restrictionName = 'test';
    component.restrictionCountries = 'PL, US';
    component.restrictionUnits = 'UNIT_1, UNIT_2';
    component.status = true;
    fixture.detectChanges();

    const actionButton = fixture.debugElement.query(By.css('.button-action')).nativeElement;
    expect(actionButton.disabled).toBeFalsy();
  });

});

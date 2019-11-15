import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { SelectEvent } from '@app/components/switch-view/switch-view.component';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { ErrorMapper } from '@app/shared/http/error-mapper';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { UserType } from '../../../model/user.model';
import { NewUserProfileService } from './new-user-profile.service';
import { NewUserTemplateComponent } from './new-user-template/new-user-template.component';

@Component({
  selector: 'app-new-user-profile',
  templateUrl: './new-user-profile.component.html',
  styleUrls: ['../user-profile.scss']
})
export class NewUserProfileComponent implements OnInit, OnDestroy {

  private static CREATE_ERROR_MAPPER =
      new ErrorMapper({
        'UserAlreadyExistsException': 'USER_ALREADY_EXISTS'
      }, 'user-management.userProfile.error.create.');

  readonly availableUserTypes = Object.keys(UserType).map(k => <UserType> k);

  @ViewChild(NewUserTemplateComponent, {static: true}) templateComponent: NewUserTemplateComponent;

  @ViewChild('modalContentScroll', {static: true}) modalContentScroll;

  @ViewChild('switchView', {static: true}) child;

  show: boolean;
  inProgress: boolean;
  errorMessage: string;

  private selectedUserType: UserType;
  private subscriptions: Subscription[] = [];

  constructor(
      private eventService: LocalEventService,
      private newUserProfileService: NewUserProfileService
  ) { }

  ngOnInit() {
    this.subscriptions.push(
        this.eventService.subscribe(() => this.open(),
            [EventKey.OPEN_NEW_PROFILE])
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  onSelect(selectEvent: SelectEvent) {
    this.selectedUserType = selectEvent.item;
    this.reset();
  }

  onCancel() {
    this.close();
  }

  onSave() {
    this.create();
  }

  shouldDisableSaveButton() {
    return !this.templateComponent.isReady();
  }

  private open() {
    this.resetView();
    this.reset();
    this.show = true;
  }

  private resetView() {
    this.selectedUserType = this.availableUserTypes[0];
    this.child.ngOnInit();
  }

  private reset() {
    this.inProgress = false;
    this.errorMessage = null;
    this.templateComponent.load(this.selectedUserType);
    this.resetPerfectScrollbar();
  }

  private create() {
    this.inProgress = true;
    this.errorMessage = null;
    this.newUserProfileService.createUser(this.templateComponent.getTemplate())
        .pipe(finalize(() => this.inProgress = false))
        .subscribe(
            () => this.onSaveSuccess(),
            (err) => this.onSaveError(err));
  }

  private onSaveSuccess() {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'user-management.userProfile.success.create'
      }
    });
    this.close();
  }

  private onSaveError(error) {
    this.errorMessage = NewUserProfileComponent.CREATE_ERROR_MAPPER.get(error);
  }

  private close() {
    this.show = false;
  }

  private resetPerfectScrollbar() {
    this.modalContentScroll.directiveRef.scrollToTop(0);
  }

}

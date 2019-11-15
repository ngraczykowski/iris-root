import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Event, EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { ErrorMapper } from '@app/shared/http/error-mapper';
import { TranslateService } from '@ngx-translate/core';
import { Observable, of, Subscription } from 'rxjs';
import { finalize, switchMap, map, filter } from 'rxjs/operators';
import { User } from '../../../model/user.model';
import { EditUserProfileService } from './edit-user-profile.service';
import { EditUserTemplateComponent } from './edit-user-template/edit-user-template.component';
import { Store, select, ActionsSubject } from '@ngrx/store';
import * as fromRoot from '@app/reducers/index';
import { LoadUserDetails, UserManagementActionTypes, LoadUserDetailsSuccess } from '../../store/actions/userManagement.actions';
import { Actions, ofType } from '@ngrx/effects';
interface AssignedToTree {
  role: string;
  treeList: string[];
}

@Component({
  selector: 'app-edit-user-profile',
  templateUrl: './edit-user-profile.component.html',
  styleUrls: ['../user-profile.scss']
})
export class EditUserProfileComponent implements OnInit, OnDestroy {
  private static UPDATE_ERROR_MAPPER =
      new ErrorMapper({
        'UserIsUsedInWorkflowException': 'USER_ROLE_IN_USE'
      }, 'user-management.userProfile.error.update.');
  private static LOAD_ERROR_MAPPER =
      new ErrorMapper({
        'EntityNotFoundException': 'USER_NOT_FOUND'
      }, 'user-management.userProfile.error.load.');
  private readonly rolesDictPath = 'user-management.data.roles.';

  private userId: number;
  private subscriptions: Subscription[] = [];

  userName: string;
  displayName: string;
  show: boolean;
  inProgress: boolean;
  loadErrorMessage: string;
  actionSubscription: Subscription;
  updateErrorMessage: string;

  @ViewChild(EditUserTemplateComponent, {static: true}) templateComponent: EditUserTemplateComponent;
  @ViewChild('modalContentScroll', {static: true}) modalContentScroll;

  constructor(
    private actions$: Actions,
    private store: Store<fromRoot.State>,
    private eventService: LocalEventService,
    private editUserProfileService: EditUserProfileService,
    private translate: TranslateService
  ) {
    translate.setDefaultLang('en');
    translate.use('en');
  }

  ngOnInit() {
    this.subscriptions.push(
        this.eventService.subscribe(event => this.open(event),
            [EventKey.OPEN_EDIT_PROFILE])
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  onCancel() {
    this.close();
  }

  onSave() {
    this.update();
  }

  shouldDisableSaveButton() {
    return !this.templateComponent.isReady();
  }

  private open(event: Event) {
    this.userId = event.data.userId;
    this.reset();
    this.load();
    this.show = true;
  }

  private reset() {
    this.inProgress = false;
    this.updateErrorMessage = null;
    this.loadErrorMessage = null;
    this.resetPerfectScrollbar();
  }

  private load() {
    this.inProgress = true;
    this.loadErrorMessage = null;
    this.store.dispatch(new LoadUserDetails(this.userId));
    this.actionSubscription = this.actions$.pipe(
      ofType(UserManagementActionTypes.LoadUserDetailsSuccess)
    ).subscribe(
      (data: LoadUserDetailsSuccess) => this.onLoadSuccess(data.payload),
      (err) => this.onLoadError(err)
    );
  }

  private onLoadSuccess(user: User) {
    this.inProgress = false;
    this.userName = user.userName;
    this.displayName = user.displayName;
    this.templateComponent.load(user);
    this.actionSubscription.unsubscribe();
  }

  private onLoadError(error) {
    this.inProgress = false;
    this.loadErrorMessage = EditUserProfileComponent.LOAD_ERROR_MAPPER.get(error);
  }

  private update() {
    this.inProgress = true;
    this.updateErrorMessage = null;
    this.editUserProfileService.updateUser(this.userId, this.templateComponent.getTemplate())
        .pipe(finalize(() => this.inProgress = false))
        .subscribe(
            () => this.onUpdateSuccess(),
            (err) => this.onUpdateError(err));
  }

  private onUpdateError(error) {
    const message = EditUserProfileComponent.UPDATE_ERROR_MAPPER.get(error);
    if (error && error.error && error.error.extras) {
      this.setErrorMessageBasedOnExceptionData(error, message);
    } else {
      this.updateErrorMessage = message;
    }
  }

  private setErrorMessageBasedOnExceptionData(error: any, message: string) {
    if (error.error.key === 'UserIsUsedInWorkflowException') {
      this.getUserIsUsedInWorkflowFailedMessage(error, message)
          .subscribe(msg => this.updateErrorMessage = msg);
    }
  }

  private getUserIsUsedInWorkflowFailedMessage(error: any, message: string): Observable<string> {
    const values = [];
    (<AssignedToTree[]>error.error.extras['assignedTrees']).forEach(assignation =>
        this.getTranslation(assignation, message).subscribe(msg => values.push(msg)));
    return of(values.join('\n'));
  }

  private getTranslation(assing: AssignedToTree, message: string): Observable<string> {
    return this.translate.get(this.rolesDictPath + assing.role)
        .pipe(switchMap(roleName =>
            this.translate.get(message, {role: roleName, treeList: assing.treeList})));
  }

  private onUpdateSuccess() {
    this.sendUpdateSuccessNotificationEvent();
    this.close();
  }

  private sendUpdateSuccessNotificationEvent() {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'user-management.userProfile.success.update'
      }
    });
  }

  private close() {
    this.show = false;
  }

  private resetPerfectScrollbar() {
    this.modalContentScroll.directiveRef.scrollToTop(0);
  }

}

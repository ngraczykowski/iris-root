import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { ErrorMapper } from '@app/shared/http/error-mapper';
import { BranchDetails } from '@model/branch.model';
import { finalize } from 'rxjs/operators';
import { BranchReviewButtonService } from './branch-review-button.service';

@Component({
  selector: 'app-branch-review',
  templateUrl: './branch-review-button.component.html',
  styleUrls: ['./branch-review-button.component.scss']
})
export class BranchReviewButtonComponent implements OnInit {

  private readonly errorMapper = new ErrorMapper(
      {},
      'reasoningBranch.review.message.error.');

  @Input() branch: BranchDetails;
  @Output() review: EventEmitter<any> = new EventEmitter<any>();

  inProgress: boolean;
  reviewedButtonVisible = true;

  constructor(
      private reviewBranchService: BranchReviewButtonService,
      private eventService: LocalEventService
  ) { }

  ngOnInit() {
  }

  onBranchReview() {
    this.inProgress = true;
    this.reviewedButtonVisible = false;
    this.reviewBranchService.reviewBranch(this.branch.decisionTreeInfo.id, this.branch.matchGroupId)
        .pipe(finalize(() => this.inProgress = false))
        .subscribe(
            () => this.onReviewSuccess(),
            error => this.onReviewError(error)
        );
  }

  private onReviewSuccess() {
    this.review.emit();
    this.sendBriefMessage('reasoningBranch.infobox.details.review.status.briefMessage');
  }

  private onReviewError(error) {
    this.sendBriefMessage(this.errorMapper.get(error));
  }

  private sendBriefMessage(messageContent) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        message: messageContent
      }
    });
  }
}

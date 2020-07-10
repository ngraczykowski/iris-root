import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { AuthoritiesService } from '@core/authorities/services/authorities.service';

@Directive({
  selector: '[appIfAuthority]'
})
export class IfAuthorityDirective {
  private hasView = false;

  @Input() set ifAuthority(feature: string) {
    const hasAuthority: boolean = this.authoritiesService.hasAuthorityForFeature(feature);
    if (!hasAuthority && !this.hasView) {
      this.viewContainer.createEmbeddedView(this.templateRef);
      this.hasView = true;
    } else if (hasAuthority && this.hasView) {
      this.viewContainer.clear();
      this.hasView = false;
    }
  }

  constructor(private templateRef: TemplateRef<any>,
              private viewContainer: ViewContainerRef,
              private authoritiesService: AuthoritiesService) { }
}

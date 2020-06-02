import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: 'input[appNumbersOnly]'
})
export class NumbersOnlyDirective {

  private navigationKeys = [
    'Backspace',
    'Delete',
    'Tab',
    'Escape',
    'Enter',
    'Home',
    'End',
    'ArrowLeft',
    'ArrowRight',
    'Clear',
    'Copy',
    'Paste'
  ];

  inputElement: HTMLElement;

  constructor(public el: ElementRef) {
    this.inputElement = el.nativeElement;
  }

  @HostListener('keydown', ['$event'])
  onKeyDown(e: KeyboardEvent) {
    if (this.isAllowedKey(e)) {
      return;
    }
    if (this.isNotAllowedKey(e)) {
      e.preventDefault();
    }
  }

  isAllowedKey(e) {
    return this.navigationKeys.indexOf(e.key) > -1 || // Allow: navigation keys: backspace, delete, arrows etc.
        (e.key === 'a' && e.ctrlKey === true) || // Allow: Ctrl+A
        (e.key === 'c' && e.ctrlKey === true) || // Allow: Ctrl+C
        (e.key === 'v' && e.ctrlKey === true) || // Allow: Ctrl+V
        (e.key === 'x' && e.ctrlKey === true) || // Allow: Ctrl+X
        (e.key === 'a' && e.metaKey === true) || // Allow: Cmd+A (Mac)
        (e.key === 'c' && e.metaKey === true) || // Allow: Cmd+C (Mac)
        (e.key === 'v' && e.metaKey === true) || // Allow: Cmd+V (Mac)
        (e.key === 'x' && e.metaKey === true); // Allow: Cmd+X (Mac)
  }

  isNotAllowedKey(e) {
    return (e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) &&
        (e.keyCode < 96 || e.keyCode > 105);
  }

  digitsOnlyExpression() {
    return /\D/g;
  }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    event.preventDefault();
    const pastedInput: string = event.clipboardData
        .getData('text/plain')
        .replace(this.digitsOnlyExpression(), '');
    document.execCommand('insertText', false, pastedInput);
  }

  @HostListener('drop', ['$event'])
  onDrop(event: DragEvent) {
    event.preventDefault();
    const textData = event.dataTransfer.getData('text').replace(this.digitsOnlyExpression(), '');
    this.inputElement.focus();
    document.execCommand('insertText', false, textData);
  }
}

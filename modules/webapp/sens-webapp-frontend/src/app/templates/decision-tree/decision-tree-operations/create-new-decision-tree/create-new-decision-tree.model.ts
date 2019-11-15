export class DecisionTreeSettings {
  name: string;
  modelId: string;
}

export class Option {
  id: string;
  name: string;
  selected = false;
}

export class MultipleSelectedOptionsError extends Error { }

export class DropdownSelector {

  constructor(public options: Option[]) { }

  getSelected(): Option {
    const selected = this.options.filter(o => o.selected);
    if (selected.length > 1) {
      throw new MultipleSelectedOptionsError();
    }
    return selected.length === 1 ? selected[0] : null;
  }

  select(option: Option) {
    this.options.forEach(o => o.selected = false);
    option.selected = true;
  }
}

// TODO(iwnek) to remove
export class CheckboxSelector {

  constructor(public options: Option[]) { }

  getSelected(): Option[] {
    return this.options.filter(o => o.selected);
  }

  toggle(option: Option) {
    option.selected = !option.selected;
  }
}

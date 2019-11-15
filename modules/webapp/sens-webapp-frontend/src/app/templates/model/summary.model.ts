export class Summary {
  statGroups: StatisticGroup[];
}

export class StatisticGroup {
  name: string;
  stats: Statistic[];
}

export class Statistic {
  name: string;
  count: number;
}

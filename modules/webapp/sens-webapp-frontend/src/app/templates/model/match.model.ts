import { Page } from './collection-response.model';

export interface Match {
  id: number;
  externalId: string;
  discriminator: string;
  matchGroupId: number;
  matchFields: string[];
}

export interface MatchesPage extends Page {
  content: Match[];
}

export interface MatchPageResponse {
  matches: MatchesPage;
}

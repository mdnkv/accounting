import { Routes } from '@angular/router';

import {DashboardView} from './dashboard/views/dashboard-view/dashboard-view';
import {CreateOrganizationView} from './organizations/views/create-organization-view/create-organization-view';
import {UpdateOrganizationView} from './organizations/views/update-organization-view/update-organization-view';
import {OrganizationsView} from './organizations/views/organizations-view/organizations-view';
import {CreateAccountView} from './accounts/views/create-account-view/create-account-view';
import {UpdateAccountView} from './accounts/views/update-account-view/update-account-view';
import {AccountsView} from './accounts/views/accounts-view/accounts-view';
import {CreateTransactionView} from './transactions/views/create-transaction-view/create-transaction-view';
import {TransactionsView} from './transactions/views/transactions-view/transactions-view';
import {JournalsView} from './journals/views/journals-view/journals-view';
import {CreateJournalView} from './journals/views/create-journal-view/create-journal-view';
import {UpdateJournalView} from './journals/views/update-journal-view/update-journal-view';

import {HasActiveOrganizationGuard, HasAuthorityGuard} from './core/guards/organizations.guards';
import {ReportsHomeView} from './reports/views/reports-home-view/reports-home-view';

export const routes: Routes = [
  {path: 'dashboard', component: DashboardView},
  {path: 'organizations/create', component: CreateOrganizationView},
  {path: 'organizations/update/:id', component: UpdateOrganizationView, canActivate: [HasAuthorityGuard('organizations:update')]},
  {path: 'organizations', component: OrganizationsView},
  {path: 'accounts/create', component: CreateAccountView, canActivate: [HasAuthorityGuard('accounts:create')]},
  {path: 'accounts/update/:id', component: UpdateAccountView, canActivate: [HasAuthorityGuard('accounts:update')]},
  {path: 'accounts', component: AccountsView, canActivate:[HasActiveOrganizationGuard()]},
  {path: 'transactions/create', component: CreateTransactionView, canActivate: [HasAuthorityGuard('transactions:create')]},
  {path: 'transactions', component: TransactionsView, canActivate:[HasActiveOrganizationGuard()]},
  {path: 'journals', component: JournalsView, canActivate:[HasActiveOrganizationGuard()]},
  {path: 'journals/create', component: CreateJournalView, canActivate: [HasAuthorityGuard('journals:create')]},
  {path: 'journals/update/:id', component: UpdateJournalView, canActivate: [HasAuthorityGuard('journals:update')]},
  {path: 'reports', component: ReportsHomeView, canActivate: [HasAuthorityGuard('reports:view')]},
  {path: '', pathMatch: 'full', redirectTo: '/dashboard'}
];

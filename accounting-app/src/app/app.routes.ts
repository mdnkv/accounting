import { Routes } from '@angular/router';
import {DashboardView} from './dashboard/views/dashboard-view/dashboard-view';
import {RolesListView} from './roles/views/roles-list-view/roles-list-view';
import {OrganizationCreateView} from './organizations/views/organization-create-view/organization-create-view';
import {AccountsView} from './accounts/views/accounts-view/accounts-view';
import {CreateTransactionView} from './transactions/views/create-transaction-view/create-transaction-view';
import {TransactionsView} from './transactions/views/transactions-view/transactions-view';
import {UpdateAccountView} from './accounts/views/update-account-view/update-account-view';

export const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardView
  },
  {
    path: 'roles/list',
    component: RolesListView
  },
  {
    path: 'organizations/create',
    component: OrganizationCreateView
  },
  {
    path: 'accounts',
    component: AccountsView
  },
  {
    path: 'accounts/update/:id',
    component: UpdateAccountView
  },
  {
    path: 'transactions/create',
    component: CreateTransactionView
  },
  {
    path: 'transactions',
    component: TransactionsView
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/dashboard'
  }
];

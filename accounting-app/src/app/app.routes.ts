import { Routes } from '@angular/router';
import {DashboardView} from './dashboard/views/dashboard-view/dashboard-view';
import {RolesListView} from './roles/views/roles-list-view/roles-list-view';
import {OrganizationCreateView} from './organizations/views/organization-create-view/organization-create-view';

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
    path: '',
    pathMatch: 'full',
    redirectTo: '/dashboard'
  }
];

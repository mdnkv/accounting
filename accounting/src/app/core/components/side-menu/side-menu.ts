import {Component, inject} from '@angular/core';
import {RouterLink} from '@angular/router';

import {MatListModule} from '@angular/material/list';
import {MatIconModule} from '@angular/material/icon';

import {CoreStore} from '../../stores/core.store';
import {SideNavMenuItem} from '../../models/core.models';

@Component({
  selector: 'app-side-menu',
  imports: [MatListModule, MatIconModule, RouterLink],
  templateUrl: './side-menu.html',
  styleUrl: './side-menu.css'
})
export class SideMenu {

  readonly store = inject(CoreStore)

  SIDENAV_MENU_ITEMS: SideNavMenuItem[] = [
    {
      title: 'Dashboard',
      icon: 'dashboard',
      route: '/dashboard'
    },
    {
      title: 'Accounts',
      icon: 'assignment',
      route: '/accounts'
    },
    {
      title: 'Journals',
      icon: 'book',
      route: '/journals'
    },
    {
      title: 'Transactions',
      icon: 'receipt',
      route: '/transactions'
    },
    {
      title: 'Reports',
      icon: 'bar_chart',
      route: '/reports'
    }
  ]

}

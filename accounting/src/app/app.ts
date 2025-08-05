import {Component, effect, inject, signal} from '@angular/core';
import { RouterOutlet } from '@angular/router';

import {MatSidenavModule} from '@angular/material/sidenav';

import {Navbar} from './core/components/navbar/navbar';
import {SideMenu} from './core/components/side-menu/side-menu';
import {CoreStore} from './core/stores/core.store';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar, MatSidenavModule, SideMenu],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  readonly store = inject(CoreStore)

  sideNavWidth = signal('250px')

  constructor() {
    effect(() => {
      this.sideNavWidth.set((this.store.isSideMenuOpened()) ? '250px': '65px')
    })
  }

}

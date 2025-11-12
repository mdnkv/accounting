import {Component, inject, signal} from '@angular/core';
import {Router} from '@angular/router';

import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatToolbarModule} from '@angular/material/toolbar';

import {CoreStore} from '../../stores/core.store';
import {UserStore} from '../../../users/stores/user.store';

import Keycloak from 'keycloak-js';

@Component({
  selector: 'app-navbar',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {

  readonly coreStore = inject(CoreStore)
  readonly userStore = inject(UserStore)

  router: Router = inject(Router)
  keycloak = inject(Keycloak)

  isNavbarDropdownActive = signal(false)

  openLink (route: string){
    this.router.navigateByUrl(route)
    this.isNavbarDropdownActive.set(false)
  }

  toggleNavbarDropdown(){
    this.isNavbarDropdownActive.update(value => !value)
  }

  onLogout(){
    this.keycloak.logout()
  }


}

import {Component, inject, signal} from '@angular/core';

import Keycloak from 'keycloak-js';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [
    RouterLink
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {

  keycloak = inject(Keycloak)
  active = signal(false)

  toggleNavMenu(){
    this.active.update(value => !value)
  }

  logout() {
    this.keycloak.logout()
  }

}

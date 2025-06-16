import {Component, inject, signal} from '@angular/core';

import Keycloak from 'keycloak-js';

@Component({
  selector: 'app-navbar',
  imports: [],
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

import {Component, inject, input, output} from '@angular/core';
import {Router} from '@angular/router';

import {MatMenuModule} from '@angular/material/menu';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';

import {UserOrganization} from '../../models/organizations.models';
import {UserOrganizationStore} from '../../stores/user-organization.store';

@Component({
  selector: 'app-organization-card',
  imports: [
    MatCardModule,
    MatButtonModule,
    MatMenuModule,
    MatIconModule
  ],
  templateUrl: './organization-card.html',
  styleUrl: './organization-card.css'
})
export class OrganizationCard {

  readonly store = inject(UserOrganizationStore)
  router: Router = inject(Router)

  userOrganization = input.required<UserOrganization>()

  setActive() {
    this.store.setActiveOrganization(this.userOrganization())
  }

  update() {
    const id = this.userOrganization().organization.id!
    this.router.navigate(['/organizations/update', id])
  }

}

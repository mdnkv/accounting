import {Component, effect, inject, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';

import {OrganizationCard} from '../../components/organization-card/organization-card';
import {OrganizationStore} from '../../stores/organizations.store';

@Component({
  selector: 'app-organizations-view',
  imports: [
    OrganizationCard,
    RouterLink
  ],
  templateUrl: './organizations-view.html',
  styleUrl: './organizations-view.css'
})
export class OrganizationsView implements OnInit{

  readonly store = inject(OrganizationStore)

  ngOnInit() {
    this.store.getOrganizationsForUser()
  }

  onSetActiveOrganization(id: string){
    this.store.setActiveOrganization(id)
  }

}

import {Component, inject, OnInit} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {RouterLink} from '@angular/router';

import {OrganizationUserService} from '../../services/organization-user';
import {UserOrganization} from '../../models/organizations.models';
import {OrganizationCard} from '../../components/organization-card/organization-card';

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

  organizations: UserOrganization[] = []

  organizationUserService: OrganizationUserService = inject(OrganizationUserService)

  ngOnInit() {
    this.organizationUserService.getAllForUser().subscribe({
      next: result => {
        console.log(result)
        this.organizations = result
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }

  onSetActiveOrganization(id: string){
    this.organizationUserService.setActiveForUser(id).subscribe({
      next: result => {
        console.log(result)
        this.organizations = this.organizations.map(organization => {
          if (organization.id == id){
            organization.active = true
          } else {
            organization.active = false
          }
          return organization
        })
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }

}

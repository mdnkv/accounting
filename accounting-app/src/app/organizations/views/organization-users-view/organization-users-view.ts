import {Component, inject, input, OnInit} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {OrganizationUserService} from '../../services/organization-user';
import {CreateOrganizationUserRequest, OrganizationUser} from '../../models/organizations.models';
import {
  CreateOrganizationUserModal
} from '../../components/create-organization-user-modal/create-organization-user-modal';
import {OrganizationUserCard} from '../../components/organization-user-card/organization-user-card';

@Component({
  selector: 'app-organization-users-view',
  imports: [
    CreateOrganizationUserModal,
    OrganizationUserCard
  ],
  templateUrl: './organization-users-view.html',
  styleUrl: './organization-users-view.css'
})
export class OrganizationUsersView implements OnInit{

  id = input.required<string>()
  users: OrganizationUser[] = []

  organizationUserService: OrganizationUserService = inject(OrganizationUserService)

  ngOnInit() {
    this.organizationUserService.getUsersInOrganization(this.id()).subscribe({
      next: result => {
        this.users = result
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }

  onCreateNewOrganizationUser(payload: CreateOrganizationUserRequest){
    this.organizationUserService.createOrganizationUser(payload).subscribe({
      next: result => {
        this.users.push(result)
      },
      error: (err: HttpErrorResponse) => {
        if (err.status == 404){
          alert('The invitation was sent to the user email')
        }
      }
    })
  }



}

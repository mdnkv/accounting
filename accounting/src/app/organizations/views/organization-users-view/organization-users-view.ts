import {Component, effect, inject, input, OnInit} from '@angular/core';
import {Header} from '../../../core/components/header/header';
import {RoleStore} from '../../../roles/stores/role.store';
import {OrganizationUsersList} from '../../components/organization-users-list/organization-users-list';
import {OrganizationUserStore} from '../../stores/organization-user.store';
import {
  CreateOrganizationUserModal
} from '../../components/create-organization-user-modal/create-organization-user-modal';

@Component({
  selector: 'app-organization-users-view',
  imports: [
    Header,
    OrganizationUsersList,
    CreateOrganizationUserModal
  ],
  templateUrl: './organization-users-view.html',
  styleUrl: './organization-users-view.css'
})
export class OrganizationUsersView implements OnInit{

  organizationId = input.required<string>()

  organizationUserStore = inject(OrganizationUserStore)
  roleStore = inject(RoleStore)

  ngOnInit() {
    this.organizationUserStore.getUsers()
    this.roleStore.getRoles()
  }


  onCreateClicked() {
    this.organizationUserStore.showModal(true)
  }


}

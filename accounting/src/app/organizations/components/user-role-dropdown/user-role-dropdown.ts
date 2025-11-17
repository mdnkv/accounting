import {Component, inject, input, output, signal} from '@angular/core';
import {RoleStore} from '../../../roles/stores/role.store';
import {Role} from '../../../roles/models/roles.models';
import {OrganizationUser} from '../../models/organizations.models';

@Component({
  selector: 'app-user-role-dropdown',
  imports: [],
  templateUrl: './user-role-dropdown.html',
  styleUrl: './user-role-dropdown.css'
})
export class UserRoleDropdown {

  readonly roleStore = inject(RoleStore)
  isActive = signal(false)

  organizationUser = input.required<OrganizationUser>()
  selectRole = output<Role>()

  toggleDropdown(){
    this.isActive.update(v => !v)
  }

  onSelectRole(role: Role){
    this.isActive.set(false)
    this.selectRole.emit(role)
  }



}

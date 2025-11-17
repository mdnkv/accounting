import {Component, input, output} from '@angular/core';
import {OrganizationUser} from '../../models/organizations.models';
import {UserRoleDropdown} from '../user-role-dropdown/user-role-dropdown';
import {Role} from '../../../roles/models/roles.models';

@Component({
  selector: 'app-organization-user-card',
  imports: [
    UserRoleDropdown
  ],
  templateUrl: './organization-user-card.html',
  styleUrl: './organization-user-card.css'
})
export class OrganizationUserCard {

  organizationUser = input.required<OrganizationUser>()

  updateUser = output<OrganizationUser>()

  onChangeUserRole(role: Role){
    const user = this.organizationUser()
    user.role = role
    user.roleId = role.id!
    this.updateUser.emit(user)
  }

}

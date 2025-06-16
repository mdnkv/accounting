import {Component, inject, OnInit} from '@angular/core';

import {RouterLink} from '@angular/router';

import {RoleService} from '../../services/role';
import {Role} from '../../models/roles.models';
import {RoleCard} from '../../components/role-card/role-card';

@Component({
  selector: 'app-roles-list-view',
  imports: [RouterLink, RoleCard],
  templateUrl: './roles-list-view.html',
  styleUrl: './roles-list-view.css'
})
export class RolesListView implements OnInit{

  roleService: RoleService = inject(RoleService)
  roles: Role[] = []

  ngOnInit() {
    this.roleService.getRoles().subscribe({
      next: result => {
        console.log(result)
        this.roles = result
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  onSetActive(id: string) {
    this.roleService.setActiveRole(id).subscribe({
      next: result => {
        this.roles = this.roles.map(role => {
          if (role.id == id){
            role.active = true
          } else {
            role.active = false
          }
          return role
        })
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

}

import {Component, inject, OnInit, output, signal} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

import {CreateOrganizationUserRequest} from '../../models/organizations.models';
import {Role} from '../../../roles/models/roles.models';
import {RoleService} from '../../../roles/services/role';

@Component({
  selector: 'app-create-organization-user-modal',
  imports: [ReactiveFormsModule],
  templateUrl: './create-organization-user-modal.html',
  styleUrl: './create-organization-user-modal.css'
})
export class CreateOrganizationUserModal implements OnInit{

  roles: Role[] = []

  isActive = signal(false)
  createUser = output<CreateOrganizationUserRequest>()

  formBuilder: FormBuilder = inject(FormBuilder)
  roleService: RoleService = inject(RoleService)

  form: FormGroup = this.formBuilder.group({
    email: ['', [Validators.required, Validators.maxLength(255), Validators.email]],
    roleId: [null, [Validators.required]]
  })

  ngOnInit() {
    this.roleService.getRolesForOrganization().subscribe({
      next: result => {
        this.roles = result
      }
    })

  }

  formSubmit(){
    const organizationId = localStorage.getItem('activeOrganizationId') as string
    const payload: CreateOrganizationUserRequest = {
      email: this.form.get('email')?.value,
      roleId: this.form.get('roleId')?.value,
      organizationId
    }
    this.createUser.emit(payload)
    this.isActive.set(false)
    this.form.reset()
  }

  openModal(){
    this.isActive.set(true)
  }

  closeModal(){
    if (this.form.touched){
      if (confirm('Do you want to quit without saving?')){
        this.isActive.set(false)
      }
    } else {
      this.isActive.set(false)
    }
  }

}

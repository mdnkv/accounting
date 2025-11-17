import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';

import {OrganizationUserStore} from '../../stores/organization-user.store';
import {RoleStore} from '../../../roles/stores/role.store';
import {CreateOrganizationUserRequest} from '../../models/organizations.models';


@Component({
  selector: 'app-create-organization-user-modal',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
  ],
  templateUrl: './create-organization-user-modal.html',
  styleUrl: './create-organization-user-modal.css'
})
export class CreateOrganizationUserModal implements OnInit{

  readonly organizationUserStore = inject(OrganizationUserStore)
  readonly roleStore = inject(RoleStore)

  formBuilder: FormBuilder = inject(FormBuilder)
  form: FormGroup = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    roleId: [null, [Validators.required]]
  })

  ngOnInit() {
    this.roleStore.getRoles()
  }

  formSubmit(){
    const payload: CreateOrganizationUserRequest = {
      email: this.form.get('email')?.value,
      roleId: this.form.get('roleId')?.value,
    }
    console.log(payload)
    this.organizationUserStore.createOrganizationUser(payload)
    this.closeModal()

  }

  closeModal() {
    this.form.reset()
    this.organizationUserStore.showModal(false)
  }

}

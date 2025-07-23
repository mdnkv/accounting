import {Component, effect, inject, OnInit, output, signal} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

import {CreateOrganizationUserRequest} from '../../models/organizations.models';
import {RoleStore} from '../../../roles/stores/roles.store';
import {OrganizationStore} from '../../stores/organizations.store';

@Component({
  selector: 'app-create-organization-user-modal',
  imports: [ReactiveFormsModule],
  templateUrl: './create-organization-user-modal.html',
  styleUrl: './create-organization-user-modal.css'
})
export class CreateOrganizationUserModal implements OnInit{

  roleStore = inject(RoleStore)
  organizationStore = inject(OrganizationStore)

  isActive = signal(false)
  createUser = output<CreateOrganizationUserRequest>()

  formBuilder: FormBuilder = inject(FormBuilder)

  form: FormGroup = this.formBuilder.group({
    email: ['', [Validators.required, Validators.maxLength(255), Validators.email]],
    roleId: [null, [Validators.required]]
  })

  constructor() {
    effect(() => {
      if (this.organizationStore.isActiveOrganizationLoaded()){
        this.roleStore.getRoles()
      }
    })

    effect(() => {
      if (this.roleStore.areRolesLoaded()){
        const role = this.roleStore.roles()[0].id!
        this.form.get('roleId')?.setValue(role)
      }
    })
  }

  ngOnInit() {
    this.organizationStore.getActiveOrganization()
  }

  formSubmit(){
    const organizationId = this.organizationStore.activeOrganization()!.id!
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

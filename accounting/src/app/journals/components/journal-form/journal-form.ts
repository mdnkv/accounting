import {Component, effect, inject, input, output, signal} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {Journal} from '../../models/journals.models';

@Component({
  selector: 'app-journal-form',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCheckboxModule
  ],
  templateUrl: './journal-form.html',
  styleUrl: './journal-form.css'
})
export class JournalForm {

  saveJournal = output<Journal>()
  currentJournal = input<Journal>()

  isUpdate = signal(false)

  formBuilder: FormBuilder = inject(FormBuilder)
  form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required, Validators.maxLength(255)]],
    description: [''],
    active: [true]
  })

  updateForm(payload: Journal){
    this.form.get('name')?.setValue(payload.name)
    this.form.get('description')?.setValue(payload.description)
    this.form.get('active')?.setValue(payload.active)
  }

  constructor() {
    effect(() => {
      if (this.currentJournal() != undefined){
        this.isUpdate.set(true)
        this.updateForm(this.currentJournal()!)
      } else {
        this.isUpdate.set(false)
      }
    })
  }

  onSubmit (){
    if (this.isUpdate()) {
      // update
      const payload: Journal = {
        ...this.currentJournal()!,
        name: this.form.get('name')?.value,
        description: this.form.get('description')?.value,
        active: this.form.get('active')?.value,
      }

      this.saveJournal.emit(payload)

    } else {
      // create
      const payload: Journal = {
        name: this.form.get('name')?.value,
        description: this.form.get('description')?.value,
        active: true
      }

      this.saveJournal.emit(payload)
    }
  }

  onCancel () {}

}

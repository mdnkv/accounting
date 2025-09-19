import {Component, inject} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-delete-dialog',
  imports: [],
  templateUrl: './delete-dialog.html',
  styleUrl: './delete-dialog.css'
})
export class DeleteDialog {

  dialogRef:MatDialogRef<DeleteDialog> = inject(MatDialogRef<DeleteDialog>)

  onConfirm(){
    this.dialogRef.close(true)
  }

  onCancel() {
    this.dialogRef.close(false)
  }

}

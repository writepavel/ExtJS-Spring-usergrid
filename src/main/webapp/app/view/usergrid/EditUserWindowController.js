Ext.define('MyApp.view.usergrid.EditUserWindowController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.MessageBox',
        'Ext.grid.*'
    ],

    alias: 'controller.editUserWindow',


    insertUserToGroup: function (group, user) {
        user.data.text = user.data.firstName + "." + user.data.lastName;
        if (!group.data.root) {
            group.appendChild(user);
        } else {
            var firstSubgroup = group.findChild("leaf", false);
            group.insertBefore(user, firstSubgroup);
        }
    },

    onCreateUserFormSubmit: function () {
        var formPanel = this.lookupReference('windowForm'),
            form = formPanel.getForm(),
            record = form.getRecord();
        //console.log('onCreateUserFormSubmit. this.controller is:');
        //console.dir(this.controller);
        if (form.isValid()) { // make sure the form contains valid data before submitting            
            form.updateRecord(record); // update the record with the form data
            this.controller.insertUserToGroup(currentRecord, record);
            MyApp.getApplication().fireEvent('saveRecord', this, currentRecord);
            this.controller.closeUserEditorWindow();
        } else {
            Ext.Msg.alert('Invalid Data', 'Please correct form errors.');
        }
    },

    onEditUserFormSubmit: function () {
        var formPanel = this.lookupReference('windowForm'),
            form = formPanel.getForm(),
            record = form.getRecord();
        if (form.isValid()) {
            form.updateRecord(record);
            record.data.text = record.data.firstName + "." + record.data.lastName;
            //console.log("record.text is " + record.data.text);
            MyApp.getApplication().fireEvent('saveRecord', this, record);
            this.controller.closeUserEditorWindow();
        } else {
            Ext.Msg.alert('Invalid Data', 'Please correct form errors.');
        }
    },

    closeUserEditorWindow: function () {
        this.lookupReference('windowForm').getForm().reset();
        popupWindow.hide();
    }
});
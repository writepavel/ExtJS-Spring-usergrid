Ext.define('MyApp.view.usergrid.contextmenu.GroupMenuController', {
    extend: 'Ext.app.ViewController',

    /*requires: [
        'Ext.MessageBox',
        'Ext.grid.*'
    ],*/

    alias: 'controller.groupMenu',

        addUser: function (view, event) {

            var mainViewController = MyApp.getApplication().viewport.controller;

        var win = mainViewController.showUserEditorWindow("Create new user", cellEditorPanel.up(), "createUserWindow");
        win.on('sendButtonClicked', win.getController().onCreateUserFormSubmit);

        var formPanel = win.items.items[0],
            userForm = formPanel.getForm(),
            record = Ext.create('MyApp.model.Item', {
                leaf: true
            });
        userForm.loadRecord(record);
        win.show();
    },

        editGroup: function (view, event) {
        var editingPlugin = cellEditorPanel.getPlugin('celleditor');
        editingPlugin.startEdit(currentRecord, 0);
    },

    deleteGroup: function (view, event) {
        console.log('Invoked deleteGroup');
        var children = currentRecord.childNodes,
        root = currentRecord.getTreeStore().getRoot(),
        len = children.length;       
        for (var i = 0; i < len; i++) {             
           root.insertChild(i, children[0]); // while inserting, children array automatically decreases.
        }        
        currentRecord.remove();
        MyApp.getApplication().fireEvent('saveRecord', this, root);
    }

});

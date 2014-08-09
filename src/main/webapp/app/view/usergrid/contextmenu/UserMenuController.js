Ext.define('MyApp.view.usergrid.contextmenu.UserMenuController', {
    extend: 'Ext.app.ViewController',

    /*requires: [
        'Ext.MessageBox',
        'Ext.grid.*'
    ],*/

    alias: 'controller.userMenu',

        editUser: function (view, event) {
        var editingPlugin = cellEditorPanel.getPlugin('celleditor'); 
        editingPlugin.startEdit(currentRecord, 0);
    },

        deleteUser: function (view, event) {
        console.log('Invoked deleteUser');
        var root = currentRecord.getTreeStore().getRoot();
        currentRecord.remove();
        MyApp.getApplication().fireEvent('saveRecord', this, root);
    }
});
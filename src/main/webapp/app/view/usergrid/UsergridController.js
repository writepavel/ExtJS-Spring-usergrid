/**
 * This class is the main view for the application. It is specified in app.js as the
 * "autoCreateViewport" property. That setting automatically applies the "viewport"
 * plugin to promote that instance of this class to the body element.
 */
Ext.define('MyApp.view.usergrid.UsergridController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.MessageBox',
        'Ext.grid.*'
    ],

    alias: 'controller.usergrid',

    init: function () {
        var me = this;
        MyApp.getApplication().on({
            saveRecord: function (view, record) {
                me.saveRecord(record);
            }
        });
    },

    privates: {
        // Validating cells during in-cell editing.
        cellRegexp: {
            // server's parser of human-readable *.txt does not resolve cyrillic yet.
            //name: /[a-zA-Z\u0400-\u04FF|\d]*/i, 
            name: /^[a-zA-Z]+$/i,
            email: /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/i,
            phone: /^\+?[\d|\-]{7,}$/i,
            groupName: /[\w\d|\.]+$/i
        },

        cellRegexpTip: {
            name: "Did not changed. Please enter latin letters or digits here, without spaces and punctuation signs.",
            email: "Did not changed. Please enter email in format bob@company.com",
            phone: "Did not changed. Please enter digits or '-' with optional '+' at the beginning. At least 7 symbols.",
            groupName: "Did not changed. Please enter latin letters or dots."
        }
    },

    /**
     * Invokes by 'scroll' event from treeView.
     */
    saveScrollCoords: function (view, eOpts) {
        if (!view.store.getRoot().data.expanded) return false;
        var localStorage = Ext.util.LocalStorage.get('scroll');
        localStorage.setItem('X', view.getScrollX());
        localStorage.setItem('Y', view.getScrollY());
        localStorage.release();
    },

    /**
     * Invokes by 'refresh' event from treeView.
     */
    loadScrollCoords: function (view, eOpts) {
        var localStorage = Ext.util.LocalStorage.get('scroll');
        view.setScrollX(localStorage.getItem('X'));
        view.setScrollY(localStorage.getItem('Y'));
        localStorage.release();
    },

    /**
     * Invokes by "beforeitemclick" event from treeView
     */
    saveExpandedState: function(view, record) {
        var localStorage = Ext.util.LocalStorage.get('expanded');
        if (record.data.id === 'root' && localStorage.getItem('root') !== 'true'){
            localStorage.clear();
            localStorage.setItem('root', 'true');
        }
        else if (!(record.data.leaf)) {
            localStorage.setItem(record.data.id, !record.data.expanded);
        }
        localStorage.release();
    },

    // checks cells that are focused by TAB button
    beforeEditGridCell: function (editor, context) {
        if (context.record.data.leaf) { // editing user is enabled if focus is not on first column.
            if (context.column.dataIndex === "text") return false;
        } else { // editing group. Edit is enabled if focus is only on first column.
            if (!(context.column.dataIndex === "text")) return false;
            if (context.record.data.root) return false;
        }
    },

    afterEditGridCell: function (editor, context) {
        // if (context.record.data.leaf) return; // if (store.autoSync), works users' autosave by record.commit()
        context.record.save();
    },

    // if Value does not match validation rules, cell does not change.
    validateCellValue: function (editor, context) {
        var regexp,
            regexpTip;
        if (context.record.data.leaf) {
            switch (context.column.dataIndex) {
            case "firstName":
            case "lastName":
                regexp = this.cellRegexp.name;
                regexpTip = this.cellRegexpTip.name;
                break;
            case "email":
                regexp = this.cellRegexp.email;
                regexpTip = this.cellRegexpTip.email;
                break;
            case "phoneNumber":
                regexp = this.cellRegexp.phone;
                regexpTip = this.cellRegexpTip.phone;
                break;
            }
        } else {
            if (context.column.dataIndex === "text") {
                regexp = this.cellRegexp.groupName;
                regexpTip = this.cellRegexpTip.groupName;
            }
        }
        if (!regexp || !regexpTip) return false;
        if (!regexp.test(context.value)) {
            context.invalid = true;
            Ext.Msg.alert('Failure', regexpTip);
            return false;
        }
    },


    saveRecord: function (record) {
        record.save({
            success: function (record) {
                Ext.Msg.alert('Success', 'Users updated successfully.');
            },
            failure: function (record) {
                Ext.Msg.alert('Failure', 'Failed to update users.');
            }
        });
    },

    /**
    * Shows popup window with form for filling user's details.
    * Used for creating new user or editing current user.
    */
    showUserEditorWindow: function (title, mainView, referenceString) {
        var win = this.lookupReference('popupWindow');
        if (!win) {
            win = new MyApp.view.usergrid.EditUserWindow();

            mainView.add(win);
        }
        win.title = title;
        popupWindow = win;
        return win;
    },

    /**
    * Invokes by 'itemcontextmenu' event in treePanel.
    */
    showContextMenu: function (view, record, item, index, event) {
        event.stopEvent();
        currentRecord = record; // menu's action handlers will use this object.
        var mainTreePanel = view.panel.up(),
            menuReference,
            menuClass;
        if (record.data.leaf) { // Checks record: is it user or group.
            menuReference = 'userMenu';
            cellEditorPanel = mainTreePanel.items.items[2];
            menuClass = 'MyApp.view.usergrid.contextmenu.UserMenu';
        } else {
            menuReference = 'groupMenu';
            cellEditorPanel = mainTreePanel.items.items[0];
            menuClass = 'MyApp.view.usergrid.contextmenu.GroupMenu';
        }
        var menu = this.lookupReference(menuReference);
        if (!menu) {
            menu = Ext.create(menuClass);
        }
        // disable "edit group" and "delete group" of Root.
        if (!record.data.leaf) {
//            console.log('Group menu:');
//            console.dir(menu);
            menu.items.items[1].disabled = currentRecord.data.root;
            menu.items.items[2].disabled = currentRecord.data.root;
        }

        menu.showAt(event.getXY());
        return false;
    },

    /**
    * Invokes by 'itemdblclick' event in treePanel.
    */
    editUserInWindow: function (view, record, item, index, event) {
        if (!record.data.leaf) return; // Group cannot be edited by dblclick on it.
        var win = this.showUserEditorWindow("Edit user", this.getView(), 'editUserWindow');
        win.on('sendButtonClicked', win.getController().onEditUserFormSubmit);
        //console.log('editUserInWindow. received window is:');
        //console.dir(win);
        currentRecord = record;
        var formPanel = win.items.items[0],
            userForm = formPanel.getForm();
        userForm.loadRecord(currentRecord);
        win.show();
    }
});
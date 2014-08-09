/**
*  This store uses custom proxy that sends to server full user list on any update
    (by default server receives only user id and changed data)
    This is made for more human-readability of final txt-file, it does not contain any user id. 
    All user list handles by server and is rewritten in txt-file.
*/

Ext.define('MyApp.store.Usergrid', {
    extend: 'Ext.data.TreeStore',
    model: 'MyApp.model.Item',
    storeId: 'userGridStore',
    //  folderSort: true,
    //autoSync: true,
    autoLoad: true,
    expanded: false,
    listeners: {
        // for right autoload.        
        beforeload: function (store, operation, eOpts) {
            var localStorage = Ext.util.LocalStorage.get('expanded');
            if (localStorage.getItem('root') !== 'true') return false;
            if (store.isLoading()) return false;
        },
        // reading state of opened nodes
        load: function (store, _, records, successful, eOpts) {
            var localStorage = Ext.util.LocalStorage.get('expanded');
            records.forEach(
                function (element, index, array) {
                    if (!(element.data.leaf)) {
                        element.data.expanded = (localStorage.getItem(element.data.id) === 'true');
                    }
                },
                this)
        }
    }
});
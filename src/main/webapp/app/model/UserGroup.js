/**
 * Created by Pavel on 23.07.2014.
 */

Ext.define('MyApp.model.UserGroup', {
    extend: 'Ext.data.Model',
    identifier: 'uuid',

    alias: 'UserGroup',

/*    proxy: {
        type: 'localstorage',
        id: 'usergroup-model',
        reader: {
            type: 'json'
        }
    },*/
    fields: [
        {
            name: 'groupName',
            type: 'string',
            critical: true
        }
    ],
    hasMany: {
        model: 'MyApp.model.User',
        name: 'users'
    }
});
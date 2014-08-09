/**
 * Created by Pavel on 23.07.2014.
 * This Model used for users and for groups.
 * Groups differ from users by "leaf" property. groupName == "text" property.
 * In groups fields firstName, lastName, email and phoneNumber are ignored.
 */

Ext.define('MyApp.model.Item', {
    extend: 'Ext.data.TreeModel',

    alias: 'Item',
    identifier: 'uuid',

    requires: ['MyApp.proxy.AllUsersProxy'],

    proxy: {
        type: 'allusers',
        url: 'usersdb'
            //url: 'userstxt'
    },
    fields: [
        {
            name: 'leaf',
            type: 'boolean',
            critical: true
        },
        {
            name: 'firstName',
            type: 'string',
            critical: true
        },
        {
            name: 'lastName',
            type: 'string',
            critical: true
        },
        {
            name: 'email',
            type: 'string',
            vtype: 'email',
            critical: true
        },
        {
            name: 'phoneNumber',
            type: 'string',
            critical: true
        }
    ],

    validators: {
        firstName: 'presence',
        lastName: 'presence',
        phoneNumber: [
            {
                type: 'length',
                min: 7
            },
            {
                type: 'format',
                matcher: /^\+?[\d|\-]*/i
            }
        ]
    }
});
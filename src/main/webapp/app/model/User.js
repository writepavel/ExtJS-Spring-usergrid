/**
 * Created by Pavel on 23.07.2014.
 */

Ext.define('MyApp.model.User', {
    extend: 'Ext.data.Model',
    identifier: 'uuid',

    alias: 'User',

    fields: [
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
        },
        {
            name: 'groupName',
            type: 'string',
            critical: true
        },
        {
            name: 'userId',
            type: 'string',
            critical: true
        },
                        {
            name: 'groupId',
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
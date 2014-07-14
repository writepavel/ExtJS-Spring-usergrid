/**
 * Created by Pavel on 13.07.2014.
 */

//we want to setup a model and store instead of using dataUrl
Ext.define('Task', {
    extend: 'Ext.data.TreeModel',
    fields: [
        {name: 'task',     type: 'string'},
        {name: 'user',     type: 'string'},
        {name: 'duration', type: 'string'},
        {name: 'done',     type: 'boolean'}
    ]
});


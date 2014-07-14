/**
 * Created by Pavel on 13.07.2014.
 */

Ext.Loader.setConfig({
    enabled: true
});
Ext.Loader.setPath('Ext.ux', '../ux');

Ext.require([
    'Ext.data.*',
    'Ext.grid.*',
    'Ext.tree.*',
    'Ext.tip.*',
    'Ext.ux.CheckColumn'
]);

//we want to setup a model and store instead of using dataUrl
Ext.define('User', {
    extend: 'Ext.data.TreeModel',
    fields: [
        {name: 'first_name', type: 'string'},
        {name: 'last_name', type: 'string'},
        {name: 'email', type: 'string'},
        {name: 'phone_number', type: 'string'}
    ]
});

Ext.onReady(function () {
    Ext.tip.QuickTipManager.init();


    var store = Ext.create('Ext.data.TreeStore', {
        model: 'User',
        proxy: {
            type: 'ajax',
            //the store will get the content from the .json file
            url: 'users'
        },
        /*proxy: {
            type: 'ajax',
            //the store will get the content from the .json file
            url: 'treegrid.json'
        },*/
        /*listeners: {
            beforeload: function () {
                var name = document.location.search.slice(1);
                this.getProxy().setExtraParam('name', name);
            }
        },*/
        folderSort: true,
        autoSync: true,
	expanded: true
    });

    //Ext.ux.tree.TreeGrid is no longer a Ux. You can simply use a tree.TreePanel
    var tree = Ext.create('Ext.tree.Panel', {
        title: 'Users Grid',
        width: 800,
        height: 500,
        renderTo: Ext.getBody(),
        collapsible: true,
        useArrows: true,
        rootVisible: true,
        store: store,
        multiSelect: true,
        columns: [
            {
                xtype: 'treecolumn', //this is so we know which column will show the tree
                text: 'Item',
                width: 200,
                sortable: true,
                dataIndex: 'text',
                locked: true
            },
	    {
              //  xtype: 'treecolumn', //this is so we know which column will show the tree
                text: 'First Name',
                width: 120,
                sortable: true,
                dataIndex: 'first_name',
               // locked: true
            },
            {
               // xtype: 'treecolumn', //this is so we know which column will show the tree
                text: 'Last Name',
                width: 120,
                sortable: true,
                dataIndex: 'last_name'
               // locked: true
            },

           /* {
                //we must use the templateheader component so we can use a custom tpl
                xtype: 'templatecolumn',
                text: 'Duration',
                width: 150,
                sortable: true,
                dataIndex: 'duration',
                align: 'center',
                //add in the custom tpl for the rows
                tpl: Ext.create('Ext.XTemplate', '{duration:this.formatHours}', {
                    formatHours: function (v) {
                        if (v < 1) {
                            return Math.round(v * 60) + ' mins';
                        } else if (Math.floor(v) !== v) {
                            var min = v - Math.floor(v);
                            return Math.floor(v) + 'h ' + Math.round(min * 60) + 'm';
                        } else {
                            return v + ' hour' + (v === 1 ? '' : 's');
                        }
                    }
                })
            },*/
            {
                text: 'Email',
                width: 170,
                dataIndex: 'email',
                sortable: true
            },
            {
                text: 'Phone Number',
                width: 150,
                dataIndex: 'phone_number',
                sortable: true
            },
           /* {
                xtype: 'checkcolumn',
                header: 'Done',
                dataIndex: 'done',
                width: 40,
                stopSelection: false
            },
            {
                text: 'Edit',
                width: 40,
                menuDisabled: true,
                xtype: 'actioncolumn',
                tooltip: 'Edit task',
                align: 'center',
                icon: 'img/edit_task.png',
                handler: function (grid, rowIndex, colIndex, actionItem, event, record, row) {
                    Ext.Msg.alert('Editing' + (record.get('done') ? ' completed task' : ''), record.get('task'));
                },
                // Only leaf level tasks may be edited
                isDisabled: function (view, rowIdx, colIdx, item, record) {
                    return !record.data.leaf;
                }
            }*/
        ]
    });
});

// ==================================================================================
/*
 Ext.define('MyApp.MyPanel', {
 extend: 'Ext.Panel',
 width: 200,
 height: 150,
 bodyPadding: 5
 });

 Ext.application({
 name: 'MyApp',

 launch: function () {

 Ext.create('MyApp.MyPanel', {
 renderTo: Ext.getBody(),
 title: 'My First Panel',
 html: 'My First Panel'
 });

 Ext.create('MyApp.MyPanel', {
 renderTo: Ext.getBody(),
 title: 'My Second Panel',
 html: 'My Second Panel'
 });

 Ext.widget({
 renderTo: Ext.getBody(),
 xtype: 'grid',
 title: 'Grid',
 width: 650,
 height: 300,
 plugins: 'rowediting',
 store: {
 fields: [ 'name', 'age', 'votes', 'credits' ],
 data: [
 [ 'Bill', 35, 10, 427 ],
 [ 'Fred', 22, 4, 42 ]
 ]
 },
 columns: {
 defaults: {
 editor: 'numberfield',
 width: 120
 },
 items: [
 { text: 'Name', dataIndex: 'name', flex: 1, editor: 'textfield' },
 { text: 'Age', dataIndex: 'age' },
 { text: 'Votes', dataIndex: 'votes' },
 { text: 'Credits', dataIndex: 'credits' }
 ]
 }
 })
 }
 });
 */

/**
 * This class is the main view for the application. It is specified in app.js as the
 * "autoCreateViewport" property. That setting automatically applies the "viewport"
 * plugin to promote that instance of this class to the body element.
 */
Ext.define('MyApp.view.usergrid.Usergrid', {
    extend: 'Ext.container.Container',

    xtype: 'app-main',
    reference: 'mainapp',

    controller: 'usergrid',
    viewModel: {
        type: 'main'
    },
    items: [
        {
            xtype: 'treepanel',
            bind: {
                title: '{name}'
            },
            width: 800,
            height: 400,
            reference: 'main-treePanel',
            renderTo: Ext.getBody(),
            collapsible: true,
            useArrows: true,
            selType: 'cellmodel',
            plugins: {
                ptype: 'cellediting',
                pluginId: 'celleditor',
                listeners: {
                    // checks cells that are focused by TAB button
                    beforeedit: 'beforeEditGridCell',
                    edit: function (editor, context) {
                        context.record.save();
                    },
                    validateedit: 'validateCellValue',
                    dblclick: function () {
                        return false; // dblclick do not work in this plugin.
                    }
                }
            },
            viewConfig: {
                plugins: {
                    ptype: 'treeviewdragdrop'
                },
                listeners: {
                    drop: function (node, data, overModel, dropPosition, eOpts) {
                        data.records[0].save();
                    },
                   // afteritemexpand: 'saveExpandedGroupNodeState',
                   // afteritemcollapse: 'saveExpandedGroupNodeState',
                    beforeitemclick: 'saveExpandedState',// 'saveExpandedRootNodeState',
                    scroll: 'saveScrollCoords',
                    refresh: 'loadScrollCoords'
                }
            },
            rootVisible: true,
            store: 'Usergrid',
            columns: [
                {
                    xtype: 'treecolumn',
                    text: 'Item',
                    width: 200,
                    dataIndex: 'text',
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    },
                    locked: true
                },
                {
                    text: 'First Name',
                    width: 120,
                    dataIndex: 'firstName',
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                },
                {
                    text: 'Last Name',
                    width: 120,
                    sortable: true,
                    dataIndex: 'lastName',
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                },
                {
                    text: 'Email',
                    width: 170,
                    dataIndex: 'email',
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                },
                {
                    text: 'Phone Number',
                    width: 150,
                    dataIndex: 'phoneNumber',
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                }
            ],
            region: 'west',
            split: true,
            listeners: {
                itemcontextmenu: 'showContextMenu',
                itemdblclick: 'editUserInWindow'
            }
        }
    ]
});
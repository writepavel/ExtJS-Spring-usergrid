Ext.define('MyApp.view.usergrid.contextmenu.GroupMenu', {
    extend: 'Ext.menu.Menu',
    xtype: 'group-menu',
	reference: 'groupMenu',
	controller: 'groupMenu',
	plain: true,
	width: 100,
    //margin: '0 0 10 0',
    //floating: false,  // usually you want this set to True (default)
    renderTo: Ext.getBody(),  // usually rendered by it's containing component
    items: [{
        text: 'add user',
		handler: 'addUser'
    },{
        text: 'edit group',
		handler: 'editGroup'
    },{
        text: 'delete group',
		handler: 'deleteGroup'
    }]
	});
Ext.define('MyApp.view.usergrid.contextmenu.UserMenu', {
    extend: 'Ext.menu.Menu',
    xtype: 'user-menu',
	reference: 'userMenu',
	plain: true,
	controller: 'userMenu',
	width: 100,
    //margin: '0 0 10 0',
    //floating: false,  // usually you want this set to True (default)
    renderTo: Ext.getBody(),  // usually rendered by it's containing component
    items: [{
        text: 'edit user',
		handler: 'editUser'
    },{
        text: 'delete user',
		handler: 'deleteUser'
    }]
	});
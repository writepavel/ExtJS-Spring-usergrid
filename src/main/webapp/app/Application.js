/**
 * The main application class. An instance of this class is created by app.js when it calls
 * Ext.application(). This is the ideal place to handle application launch and initialization
 * details.
 */
Ext.define('MyApp.Application', {
    extend: 'Ext.app.Application',

    name: 'MyApp',

    views: [
        'usergrid.Usergrid',
        'usergrid.UsergridController',
        'usergrid.UsergridModel',
        'usergrid.EditUserWindow',
        'usergrid.EditUserWindowController',
        'usergrid.contextmenu.UserMenu',
        'usergrid.contextmenu.UserMenuController',
        'usergrid.contextmenu.GroupMenu',
        'usergrid.contextmenu.GroupMenuController'
    ],

    models: [
        'Item',
        'User',
        'UserGroup'
    ],
//
//    controllers: [
//        'Root'
//    ],

    stores: [
        'Usergrid'
    ],

    launch: function () {
        Ext.tip.QuickTipManager.init();
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
    }
});
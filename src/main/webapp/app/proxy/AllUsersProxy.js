Ext.define('MyApp.proxy.AllUsersProxy', {
    alias: 'proxy.allusers',
    extend: 'Ext.data.proxy.Ajax',

    limitParam: 'count',
    pageParam: null,

    constructor: function () {
        this.reader = {
            type: 'json'
        };
        this.callParent(arguments);
    },

    buildRequest: function (operation) {

        var request = this.callParent(arguments);

        /*
         *  if 'update', operation is strange and operation.action
         *  returns: "cannot 'getAction' for undefined". But console.dir(operation) shows it.
         */
        if (operation.action === 'read') {
            return operation.getRequest();
        } else {
            var store = operation.getRecords()[0].store,
                converter = Ext.create('MyApp.proxy.DataConverter', store),
                userArray;
            operation.setRecords(null);
            userArray = converter.iterateTree();
            operation.setRecords(userArray);
            request.setJsonData(this.reader.rawData); // = allUsersJson;
            request.jsonDataRaw = this.reader.rawData;
            return request;
        }
    }
});
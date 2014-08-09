// also supports: startTest(function(t) {
StartTest(function (t) {
    t.diag("Sanity");

    t.ok(Ext, 'ExtJS is here');
    t.ok(Ext.Window, '.. indeed');


    t.ok(MyApp, 'My project is here');
    t.ok(MyApp.view.usergrid.Usergrid, '.. indeed');

    var grid = Ext.ComponentQuery.query('gridpanel')[0],
        store = grid.getStore(), // current store for UI of application
        //   groupUsersData, // 
        converter, // converter of data-format: from treeItems to groups and users
        store2, // Ext.data.TreeStore - storage for sample treeItems data
        store3, // Ext.data.Store - storage for groups and users.
        store2Data,
        convertedData,
        iterated,
        sampleData,
        jsonStore3Data;

    t.ok(store, 'store is found!');

    //store.load();
    /*

    t.chain({
        action: "click",
        target: Ext.getCmp("treepanel-1011"),
        offset: [6, 13]
    }, {
        action: "click",
        target: "treepanel => table.x-grid-item:nth-child(1) .x-grid-cell:nth-child(1) .x-tree-elbow-img",
        offset: [6, 13]
    });

*/

    /* var record = store.getAt(0);
    t.ok(record, 'store.getAt(0) is found!');

    console.log("record is:");
    console.dir(record);

    record.save({ // save the record to the server success:
        success: function (record) {
            Ext.Msg.alert('Success', 'User saved successfully.');
        },
        failure: function (record) {
            Ext.Msg.alert('Failure', 'Failed to save user.');
        }
    });*/





    sampleData = Ext.create('MyApp.proxy.SampleData');

    store2 = new Ext.data.TreeStore({
        autoLoad: true,
        autoSync: true,
        data: sampleData.sampleItemData,
        proxy: {
            type: 'memory',
            reader: {
                type: 'json'
            }
        },
        model: 'MyApp.model.Item'
    });

    console.log("Created store2:");
    console.dir(store2);

    store3 = new Ext.data.Store({
        autoLoad: true,
        autoSync: true,
        proxy: {
            type: 'localstorage',
            id: 'store3',
            reader: {
                type: 'json'
            }
        },
        model: 'MyApp.model.User'
        //        sorters: ['userGroup', 'users']
    });

    store3.removeAll();

    converter = Ext.create('MyApp.proxy.DataConverter', store2);
    store2Data = store2.proxy.reader.rawData;

    t.todo('Final test for converter', function (todo) {

        convertedData = converter.convertItemsToGroups(store2);
        todo.is(convertedData, sampleData.sampleUserGroupData, 'Data converted successfully!');

    });



    iterated = converter.iterateTree();

    t.ok(iterated, "Tree is iterated");

    t.is(iterated.length, 3, "Iterated tree has 3 elements");
    console.dir(iterated);
    iterated.forEach(function (user) {
        console.dir(user);
        t.isInstanceOf(user, 'MyApp.model.User');
        store3.add(user);
        user.save({ // save the record to the server success:
            success: function (record) {
                console.log('User saved successfully.');
            },
            failure: function (record) {
                console.log('Failed to save user.');
            }
        });
    });

    console.log("store3 jsonData is:" + Ext.encode(Ext.Array.pluck(store3.data.items, 'data')));
    //    console.log("store3 jsonData is:" + Ext.util.JSON.encode(store3.data.items));
    console.dir(store3);

    //    function logArrayElements(element, index, array) {
    //        console.log("iterated[" + index + "] = " + element);
    //        t.isInstanceOf(element, 'MyApp.model.UserGroup');
    //    }

    //iterated.forEach(logArrayElements);


    console.log("store2 data length is:" + store2Data.length + '. Store is:');
    console.dir(store2);
    t.ok(store2Data, 'Data to new store is loaded');
    t.ok(converter.items, 'converter took items');



    t.done(); // Optional, marks the correct exit point from the test
});
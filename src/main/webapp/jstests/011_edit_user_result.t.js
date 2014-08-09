StartTest(function (t) {

    t.requireOk(
        [
          //  'MyApp.model.Item'
        //    ,            'MyApp.view.usergrid.Usergrid'
        ],

        function () {
            t.diag("Integration Test");
            var treepanel = Ext.getCmp("treepanel-1011"),
                treeview = Ext.getCmp("treeview-1019"),
                grid = Ext.ComponentQuery.query('gridpanel')[0],
                store = grid.getStore(),
                checkValueInGrid = function (row, column, value) {
                    t.matchGridCellContent(grid, row, column, value, "checkValueInGrid in cell [" + row + ", " + column + "] is " + value);
                },
                chackValueInTree = function (row, value) {
                    t.is(t.getCell(treeview.grid, row, 0).dom.innerText.trim(), value, 'Tree row ' + row + ' has right group name: ' + value);
                },
                getDataFromStore = function (num) {
                    console.log('invoked getDataFromStore. Store is:');
                    console.dir(store);
                    return grid.getStore().getAt(num).data;
                };


            // var record3 = Ext.ComponentQuery("#treeview-1019-record-3");
            t.ok(treepanel, 'Treepanel is found');
            t.ok(treeview, 'Treeview is found');
            t.ok(grid, 'GridPanel is found!');
            t.ok(store, 'Store is found!');
            //t.ok(record3, 'record3 is found');
            //console.dir(treeview);
            t.chain({
                    action: "click",
                    target: Ext.getCmp("treepanel-1011"),
                    offset: [6, 13]
                }, {
                    action: "click",
                    target: "treepanel => table.x-grid-item:nth-child(1) .x-grid-cell:nth-child(1) .x-tree-elbow-img",
                    offset: [6, 13]
                }, {
                    waitFor: "elementVisible",
                    args: ["treepanel => table#treeview-1019-record-4"]
                }, {
                    action: "click",
                    target: "treepanel => table#treeview-1019-record-4",
                    offset: [33, 15]
                },
                function (next) {
                    //checkValueInGrid(4, 1, getDataFromStore(4).lastName);
                    //t.matchGridCellContent(grid, 4, 1, , "User name exists in View and Store.");
                    next();
                },
                function (next) {
                    chackValueInTree(3, 'Footballers');
                    //t.is(t.getCell(treeview.grid, 3, 0).dom.innerText.trim(), 'Footballers', 'Tree has right group name');
                    next();
                }, {
                    action: "dblclick",
                    target: "gridpanel =>  table#gridview-1021-record-6 .x-grid-cell:nth-child(2)",
                    offset: [33, 15]
                }, {
                    action: "dblclick",
                    target: "[itemId=firstName] => .x-form-text",
                    offset: [54, 13]
                }, {
                    action: "type",
                    text: "3333334"
                }, {
                    action: "dblclick",
                    target: "[itemId=lastName] => .x-form-text",
                    offset: [55, 16]
                }, {
                    action: "type",
                    text: "55555"
                }, {
                    action: "click",
                    target: "button[text=Send] => .x-btn-inner",
                    offset: [8, 7]
                },
                function (next) {
                    checkValueInGrid(4, 1, '55555Casillas');
                    //t.matchGridCellContent(grid, 4, 1, '55555Casillas', "Last Name is 55555Casillas.");
                    next();
                }, {
                    action: "click",
                    target: "[itemId=ok] => .x-btn-inner",
                    offset: [10, 1]
                },
                function (next) {
                    checkValueInGrid(4, 1, '55555Casillas');
                    next();
                }

            );
            //t.hasValue(window.down('field[name=firstname]'), 'Slack', 'First name ok');

        }
    );
});
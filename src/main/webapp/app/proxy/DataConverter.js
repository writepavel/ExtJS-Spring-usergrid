/**
* This class is used for converting TreeModel of treepanel's items
* to array of users for sending to server.
*/
Ext.define("MyApp.proxy.DataConverter", {

    fromStore: null,
    items: function () {
        return this.fromStore.data.items;
    },
    iterateTree: function () {
//        console.log("Start iteration. store root = ");
//        console.dir(this.fromStore.getRoot());
        var root = this.fromStore.getRoot(),
            currentGroup = "Root",
            currentGroupId = "root",
            userArray = [];

        root.cascadeBy(function (childNode) {
            var user,
                isUser;
            isUser = childNode.data.leaf;
            if (isUser) {
                user = Ext.create('MyApp.model.User', {
                    firstName: childNode.data.firstName,
                    lastName: childNode.data.lastName,
                    email: childNode.data.email,
                    phoneNumber: childNode.data.phoneNumber,
                    groupName: currentGroup,
                    userId: childNode.id,
                    groupId: currentGroupId
                });
                userArray.push(user);
            } else {
                currentGroup = childNode.data.text;
                currentGroupId = childNode.id;
            }
        }, this);
        return userArray;
    },
    constructor: function (store) {
        this.fromStore = store;
    },
});
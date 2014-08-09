/**
 * Created by Pavel on 24.07.2014.
 */

Ext.define('MyApp.view.usergrid.EditUserWindow', {
    extend: 'Ext.window.Window',
    xtype: 'form-contact-window',

    viewModel: {
        type: 'main'
    },

    store: 'Usergrid',
    reference: 'popupWindow',
    controller: 'editUserWindow',

    title: 'Edit User',
    width: 400,
    minWidth: 300,
    minHeight: 300,
    layout: 'fit',
    resizable: true,
    modal: true,
    defaultFocus: 'firstName',
    closeAction: 'hide',

    items: [{
        xtype: 'form',
        reference: 'windowForm',

        listeners: {
            cancelButtonClicked: 'closeUserEditorWindow'
        },

        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,

        fieldDefaults: {
            msgTarget: 'side',
            labelAlign: 'top',
            labelWidth: 150,
            labelStyle: 'font-weight:bold'
        },

        items: [
            {
                xtype: 'textfield',
                name: 'firstName',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                // server's parser of human-readable *.txt does not resolve cyrillic yet.
                //maskRe: /[a-zA-Z\u0400-\u04FF|\d]/i,
                maskRe: /[A-Za-z|\d]/i,
                fieldLabel: 'First',
                allowBlank: false
            },
            {
                xtype: 'textfield',
                name: 'lastName',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                //maskRe: /[a-zA-Z\u0400-\u04FF|\d]/i,
                maskRe: /[A-Za-z|\d]/i,
                fieldLabel: 'Last',
                allowBlank: false
                //margin: '0 0 0 5'
            },
            {
                xtype: 'textfield',
                name: 'email',
                labelAlign: 'top',
                fieldLabel: 'Email Address',
                vtype: 'email',
                msgTarget: 'under', // location of the error message
                invalidText: 'Please enter correct email address' // custom error message text                
            },
            {
                xtype: 'textfield',
                name: 'phoneNumber',
                fieldLabel: 'Phone Number',
                regex: /^\+?[\d|\-]{7,}/i,
                maskRe: /(\+)|([\d|\-])/i,
                msgTarget: 'under',
                invalidText: 'Phone contains of digits or "-", and optionally "+" at the beginning. At least 7 symbols'
            }
        ],
        buttons: [
            {
                text: 'Cancel',
                initComponent: function () {
                    this.callParent();
                    this.enableBubble('cancelButtonClicked');
                },
                handler: function () {
                    this.fireEvent('cancelButtonClicked');
                }
            },
            {
                text: 'Send',
                initComponent: function () {
                    this.callParent();
                    this.enableBubble('sendButtonClicked');
                },
                // handler of new event "sendButtonClicked" is set by external controller.
                handler: function () {
                    this.fireEvent('sendButtonClicked');
                }
            }
        ]
    }]
});
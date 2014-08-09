var Harness = Siesta.Harness.Browser.ExtJS;

Harness.configure({
    title: 'Awesome Test Suite',
    // autoCheckGlobals: true,
    waitForAppReady: true,
    expectedGlobals: ['Ext', 'MyApp'],
    // автозапуск тестов
    autoRun: true,

    preload: [
        // version of ExtJS used by your application
        'http://cdn.sencha.com/ext/gpl/5.0.0/build/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css',
 //        '../resources/yourproject-css-all.css',

        // version of ExtJS used by your application
        'http://cdn.sencha.com/ext/gpl/5.0.0/build/ext-all-debug.js',
        'app.js',
        'jstests/js/underscore.js' // PhantomJS doesn't support bind yet
    ],

    listeners: {
        // добавляем обработчик события о заврешении очередного теста
        testfinalize: function (event, test) {
            var fail = test.$failCount,
                pass = test.$passCount;

            var log = (fail ? '~~~~~~~~\n FAILED  ' : '[PASSED] ') +
                test.url + ' [pass: ' + pass + ', fail: ' + fail + ']' +
                (fail ? '\n~~~~~~~~' : '');

            console.log(log);
        },

        // добавляем обработчик события о заврешении всего пакета тестов
        testsuiteend: function (event, harness) {
            console.log('END_TESTS');
        }
    }
});

//Function.prototype.bind = _.bind;

// Temporarly comment js-tests that contain user interaction. 

Harness.start(
    'jstests/010_sanity.t.js'
    /*'jstests/020_basic.t.js', 
    {
        // Specify your own HTML page if you want
        hostPageUrl: 'index.html',
        url: 'jstests/011_edit_user_result.t.js'
    }*/
);
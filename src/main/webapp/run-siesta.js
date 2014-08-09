var fs = require("fs"),
    isWindows = /^win/i.test(navigator.platform),
    noColor = !1,
    ARGV = require("system").args,
    binDir = isWindows ? ARGV[1].replace(/.$/, "") : ARGV[1] + "/",
     start = new Date().getTime(),
    system = require('system'),
    page = require('webpage').create();
phantom.injectJs(binDir + "launcher-common.js");

console.log("BINDIR = " + binDir);
//console.log(fs.list('.'));
eval(fs.read("jstests/js/underscore-min.js"));
//phantom.injectJs("underscore-min.js");


Function.prototype.bind = _.bind;

console.log(Function.prototype.bind);


console.log('\nStart tests...\n');

/**
 * Лог.
 */
var globalLog = (function()
{
    var store = [];

    return {
        /**
         * Добавляет сообщение в лог.
         *
         * @param msg
         */
        add: function(msg)
        {
            store.push(msg);
        },

        /**
         * Выводит логи в консоль.
         */
        console: function()
        {
            var log = '';

            for (var i= 0; i < store.length; i++)
            {
                log += store[i] + '\n';
            }

            console.log(log);
        }
    };
}());


if (system.args.length !== 2)
{
    globalLog.add('Usage: phantomjs run-siesta.js URL');
    myExit(1);
}

/**
 * Завершает работу программы и возвращает в консоль числовой код.
 *
 * @param exitCode код завершения (0 - ОК)
 */
function myExit(exitCode)
{
    globalLog.add('Total time: ' + (new Date().getTime() - start) + ' ms');
    globalLog.add('Exit code: ' + exitCode);
    globalLog.console();

    phantom.exit(exitCode);
}

/**
 * Отслеживает сообщения консоли на странице.
 * @param msg
 */
page.onConsoleMessage = function(msg)
{
    if (msg.match(/END_TESTS/))
    {
        var exitCode = page.evaluate(
            function()
            {
                var totalPass = document.getElementsByClassName('total-pass')[0].innerText;
                var totalFail = document.getElementsByClassName('total-fail')[0].innerText;

                if (totalFail !== '0')
                {
                    console.log('\nFailed!');
                }
                else
                {
                    console.log('\nCompleted!');
                }

                console.log('\nTotal pass: ' + totalPass);
                console.log('Total fail: ' + totalFail);

                return totalFail === '0' ? 0 : 1;
            }
        );

        myExit(exitCode);
    }
    else if (!msg.match(/\[object Object\]/))
    {
        console.log(msg);
    }
};

/**
 * Открывает страницу.
 * @param {String} URL страницы
 */
page.open(system.args[1],
    function(status)
    {
        if (status !== "success")
        {
            globalLog.add("Unable to access network");
            myExit(1);
        }
    }
);

/*// PhantomJS doesn't support bind yet
Function.prototype.bind = Function.prototype.bind || function (thisp) {
    var fn = this;
    return function () {
        return fn.apply(thisp, arguments);
    };
};*/
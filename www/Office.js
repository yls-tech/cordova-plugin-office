var exec = require('cordova/exec');

exports.open = function (arg0, canWrite, success, error) {
    exec(success, error, 'Office', 'open', [arg0, canWrite]);
};


var exec = require('cordova/exec');

var PLUGIN_NAME = 'ChirpPlugin';

var ChirpConnect = {
  saludo: function (name, successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "saludar", [name]);
  }
};

module.exports = ChirpConnect;


var exec = require('cordova/exec');

var PLUGIN_NAME = 'ChirpPlugin';

var ChirpPlugin = {
  start: function (successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "start", []);
  },
  stop: function (successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "stop", []);
  },
  saludo: function (name, successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "saludar", [name]);
  }  
};

module.exports = ChirpPlugin;

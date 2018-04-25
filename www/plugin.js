
var exec = require('cordova/exec');

var PLUGIN_NAME = 'ChirpPlugin';

var ChirpPlugin = {
  listen: function (successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "listen", []);
  },
  stop: function (successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "stop", []);
  },
  saludo: function (name, successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "saludar", [name]);
  }  
};

module.exports = ChirpPlugin;

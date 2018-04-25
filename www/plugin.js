
var exec = require('cordova/exec');

var PLUGIN_NAME = 'ChirpConnect';

var ChirpConnect = {
  start: function (successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "start", []);
  },
  saludo: function (name, successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "saludar", [name]);
  }  
};

module.exports = ChirpConnect;

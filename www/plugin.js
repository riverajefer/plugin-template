
var exec = require('cordova/exec');

var PLUGIN_NAME = 'ChirpPlugin';

var ChirpPlugin = {
  start: function (){
        exec(successCallback, errorCallback, PLUGIN_NAME, "start", []);
  },
  listen: function (successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "listen", []);
  },
  stop: function (successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "stop", []);
  }
};

module.exports = ChirpPlugin;

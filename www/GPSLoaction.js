

var cordova = require('cordova');



function GPSLoactionPlugin() {

}





GPSLoactionPlugin.prototype.getLocation = function(successCallback,errorCallback) {
    cordova.exec(successCallback,errorCallback,"GPSLoactionPlugin","getlocation",[]);
};

GPSLoactionPlugin.prototype.getLocationAllTime = function(successCallback,errorCallback,interval=5000) {
  cordova.exec(successCallback,errorCallback,"GPSLoactionPlugin","getlocationalltime",[interval]);
};




GPSLoactionPlugin.prototype.stop = function(successCallback,errorCallback) {

      
    cordova.exec(successCallback,errorCallback,"GPSLoactionPlugin","stop",[]);
};




module.exports = new GPSLoactionPlugin();

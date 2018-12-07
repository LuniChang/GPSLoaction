

var cordova = require('cordova');



function GPSLoactionPlugin() {

}





GPSLoactionPlugin.prototype.getLocation = function(successCallback,errorCallback,timeout=6000) {
    cordova.exec(successCallback,errorCallback,"GPSLoactionPlugin","getlocation",[timeout]);
};

GPSLoactionPlugin.prototype.getLocationAllTime = function(successCallback,errorCallback,interval=5000,timeout=6000) {
  cordova.exec(successCallback,errorCallback,"GPSLoactionPlugin","getlocationalltime",[interval,timeout]);
};




GPSLoactionPlugin.prototype.stop = function(successCallback,errorCallback) {

      
    cordova.exec(successCallback,errorCallback,"GPSLoactionPlugin","stop",[]);
};




module.exports = new GPSLoactionPlugin();

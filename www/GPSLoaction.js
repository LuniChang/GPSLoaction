

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


GPSLoactionPlugin.prototype.getGpsSign = function(successCallback,errorCallback,signEnale=29) {

      
    cordova.exec(successCallback,errorCallback,"GPSLoactionPlugin","getGpsSign",[signEnale]);
};

GPSLoactionPlugin.prototype.watchGpsSign = function(successCallback,errorCallback,signEnale=29,gpsSignInterval=1000) {

      
    cordova.exec(successCallback,errorCallback,"GPSLoactionPlugin","watchGpsSign",[signEnale,gpsSignInterval]);
};
GPSLoactionPlugin.prototype.stopWatchGpsSign = function(successCallback,errorCallback) {

      
    cordova.exec(successCallback,errorCallback,"GPSLoactionPlugin","stopWatchGpsSign",[]);
};


module.exports = new GPSLoactionPlugin();

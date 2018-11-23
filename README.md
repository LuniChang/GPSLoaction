
##安装

ionic cordova plugin add   插件路径

##使用

declare var GPSLocationPlugin:any;//声明

     GPSLocationPlugin.getLocationAllTime(data => {
          console.log("GPSLocationPlugin:"+data);
   
        let location = [data.longitude, data.latitude];
       
      }, msg => {
        console.log(JSON.stringify(msg));
    
      });

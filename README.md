
##install

ionic cordova plugin add   [your path]

##use

declare var GPSLocationPlugin:any;


     GPSLocationPlugin.getLocationAllTime(data => {
          console.log("GPSLocationPlugin:"+data);
   
        let location = [data.longitude, data.latitude];
       
      }, msg => {
        console.log(JSON.stringify(msg));
    
      });
	  
##base source:

https://github.com/yanxiaojun617/com.kit.cordova.amaplocation.git
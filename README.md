
##install

ionic cordova plugin add  https://github.com/LuniChang/GPSLoaction.git

##use

declare var GPSLocationPlugin:any;



     var timeout=10000;//GPS timeout
	 
	 var interval=1000;//GPS interval
    
     GPSLocationPlugin.getLocationAllTime(data => {
          console.log("GPSLocationPlugin:"+data);
   
        if(data.is_timeout==0){//no timeout,  1 is out
           let location = [data.longitude, data.latitude];
		}
        
       
      }, msg => {
        console.log(JSON.stringify(msg));
    
      },interval,timeout);
	  
	  
	  
	  
	  
	  GPSLocationPlugin.getLocation(data => {   //just once
          console.log("GPSLocationPlugin:"+data);
   
        if(data.is_timeout==0){//no timeout,  1 is out
           let location = [data.longitude, data.latitude];
		}
        
       
      }, msg => {
        console.log(JSON.stringify(msg));
    
      },timeout);
	  
	  
	  ##get gps enable satellites,  android only
	     let signRange=29;//set satellites which sign >29 ,default is 29
	     GPSLocationPlugin.getGpsSign(data => {
            console.log("GPSLocationPlugin:"+data);
      
            let satellites = data.enable_satellites;
        
        
        }, msg => {
          console.log(JSON.stringify(msg));
      
        },signRange);
	  
	  
#reference

https://github.com/yanxiaojun617/com.kit.cordova.amaplocation.git

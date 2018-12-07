
##install

ionic cordova plugin add   [your path]

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
	  
##base source:

https://github.com/yanxiaojun617/com.kit.cordova.amaplocation.git
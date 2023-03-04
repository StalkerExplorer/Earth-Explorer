package com.earth.explorer.GPS.Service;

import android.*;
import android.content.*;
import android.content.pm.*;
import android.icu.text.*;
import android.location.*;
import androidx.core.content.*;
import com.earth.explorer.Util.*;


public class LocationData
{

	Context context;
	DecimalFormat decimalFormat;
	SimpleDateFormat df;
	String no_data;

	//GPS
	public static Double latitudeGPS;
	public static Double longitudeGPS;
	public static Double altitudeGPS;
	public static Float speedGPS;
	public static Float accuracyHorizontalGPS;
	public static Float accuracyVerticalGPS;
	public static Float accuracySpeedGPS;
	public static Long timeGPS;
	//NETWORK
	public static Double latitudeNETWORK;
	public static Double longitudeNETWORK;
	public static Double altitudeNETWORK;
	public static Float speedNETWORK;
	public static Float accuracyHorizontalNETWORK;
	public static Float accuracyVerticalNETWORK;
	public static Float accuracySpeedNETWORK;
	public static Long timeNETWORK;


	//конструктор класса
	public LocationData(Context context){
		this.context=context;
		decimalFormat= new DecimalFormat("#.##");
		df = new SimpleDateFormat("HH:mm:ss");
		no_data="Нет данных";
		//выясним сведения, о последнем известном местоположении
		if(hasPermission()){
		      getLastKnownLocation();
	    }
	}



	//Для TextView, в первой странице адаптера пейджера
	//get-методы GPS, возвращают строку
	public synchronized String getLatitudeGPS(){return this.latitudeGPS!=null? DegreeToDegMinSek.latDegMinSek(this.latitudeGPS): no_data;}
	public synchronized String getLongitudeGPS(){return this.longitudeGPS!=null? DegreeToDegMinSek.lonDegMinSek(this.longitudeGPS): no_data;}
	public synchronized String getAltitudeGPS(){return this.altitudeGPS!=null? decimalFormat.format(this.altitudeGPS): no_data;}
	public synchronized String getSpeedGPS(){return this.speedGPS!=null? decimalFormat.format( (this.speedGPS*60d*60d/1000d) ): no_data;}
	public synchronized String getAccuracyHorizontalGPS(){return this.accuracyHorizontalGPS!=null? decimalFormat.format(this.accuracyHorizontalGPS): no_data;}
	public synchronized String getAccuracyVerticalGPS(){return this.accuracyVerticalGPS!=null? decimalFormat.format(this.accuracyVerticalGPS): no_data;}
	public synchronized String getAccuracySpeedGPS(){return this.accuracySpeedGPS!=null? decimalFormat.format( (this.accuracySpeedGPS*60d*60d/1000d) ): no_data;}
	public synchronized String getTimeGPS(){return this.timeGPS!=null? df.format(this.timeGPS): no_data;}
	//get-методы NETWORK, возвращают строку
	public synchronized String getLatitudeNETWORK(){return this.latitudeNETWORK!=null? DegreeToDegMinSek.latDegMinSek(this.latitudeNETWORK): no_data;}
	public synchronized String getLongitudeNETWORK(){return this.longitudeNETWORK!=null? DegreeToDegMinSek.lonDegMinSek(this.longitudeNETWORK): no_data;}
	public synchronized String getAltitudeNETWORK(){return this.altitudeNETWORK!=null? decimalFormat.format(this.altitudeNETWORK): no_data;}
	public synchronized String getSpeedNETWORK(){return this.speedNETWORK!=null? decimalFormat.format( (this.speedNETWORK*60d*60d/1000d) ): no_data;}
	public synchronized String getAccuracyHorizontalNETWORK(){return this.accuracyHorizontalNETWORK!=null? decimalFormat.format(this.accuracyHorizontalNETWORK): no_data;}
	public synchronized String getAccuracyVerticalNETWORK(){return this.accuracyVerticalNETWORK!=null? decimalFormat.format(this.accuracyVerticalNETWORK): no_data;}
	public synchronized String getAccuracySpeedNETWORK(){return this.accuracySpeedNETWORK!=null? decimalFormat.format( (this.accuracySpeedNETWORK*60d*60d/1000d) ): no_data;}
	public synchronized String getTimeNETWORK(){return this.timeNETWORK!=null? df.format(this.timeNETWORK): no_data;}




	//set-методы, инициализируют переменные класса. Вызываются из сервиса.
	//set-методы GPS
	public static void setLatitudeGPS(Double latitude_GPS){latitudeGPS=latitude_GPS;}
	public static void setLongitudeGPS(Double longitude_GPS){longitudeGPS=longitude_GPS;}
	public static void setAltitudeGPS(Double altitude_GPS){altitudeGPS=altitude_GPS;}
	public static void setSpeedGPS(Float speed_GPS){speedGPS=speed_GPS;}
	public static void setAccuracyHorizontalGPS(Float accuracyHorizontal_GPS){accuracyHorizontalGPS=accuracyHorizontal_GPS;}
	public static void setAccuracyVerticalGPS(Float accuracyVertical_GPS){accuracyVerticalGPS=accuracyVertical_GPS;}
	public static void setAccuracySpeedGPS(Float accuracySpeed_GPS){accuracySpeedGPS=accuracySpeed_GPS;}
	public static void setTimeGPS(Long time_GPS){timeGPS=time_GPS;}
	//set-методы NETWORK
	public static void setLatitudeNETWORK(Double latitude_NETWORK){latitudeNETWORK=latitude_NETWORK;}
	public static void setLongitudeNETWORK(Double longitude_NETWORK){longitudeNETWORK=longitude_NETWORK;}
	public static void setAltitudeNETWORK(Double altitude_NETWORK){altitudeNETWORK=altitude_NETWORK;}
	public static void setSpeedNETWORK(Float speed_NETWORK){speedNETWORK=speed_NETWORK;}
	public static void setAccuracyHorizontalNETWORK(Float accuracyHorizontal_NETWORK){accuracyHorizontalNETWORK=accuracyHorizontal_NETWORK;}
	public static void setAccuracyVerticalNETWORK(Float accuracyVertical_NETWORK){accuracyVerticalNETWORK=accuracyVertical_NETWORK;}
	public static void setAccuracySpeedNETWORK(Float accuracySpeed_NETWORK){accuracySpeedNETWORK=accuracySpeed_NETWORK;}
	public static void setTimeNETWORK(Long time_NETWORK){timeNETWORK=time_NETWORK;}








	//Чтобы при создании экземпляра этого класса, сразу выяснить сведения, о последнем известном местоположении, и тут же, инициализировать переменные этого класса
	public void getLastKnownLocation(){
		LocationManager mLocationManager=(LocationManager) context. getSystemService(Context.LOCATION_SERVICE);
		
		
		
		//получаем сведения о последнем известном местоположении
		//при этом смотрим двух провайдеров
		//GPS:
		Location locationGPS = mLocationManager
			.getLastKnownLocation(LocationManager.GPS_PROVIDER);//Получает последнее известное местоположение от данного поставщика или null, если последнего известного местоположения нет.
		//NETWORK:
		Location locationNetwork = mLocationManager
			.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);//Получает последнее известное местоположение от данного поставщика или null, если последнего известного местоположения нет.


		if(locationGPS!=null){//если locationGPS не ссылается на null
			//инициализация переменных:
			setLatitudeGPS(locationGPS.getLatitude());
			setLongitudeGPS(locationGPS.getLongitude());
			setAltitudeGPS(locationGPS.hasAltitude()?locationGPS.getAltitude():null);
			setSpeedGPS(locationGPS.hasSpeed()?locationGPS.getSpeed():null);
			setAccuracyHorizontalGPS(locationGPS.hasAccuracy()?locationGPS.getAccuracy():null);
			setAccuracyVerticalGPS(locationGPS.hasVerticalAccuracy()?locationGPS.getVerticalAccuracyMeters():null);
			setAccuracySpeedGPS(locationGPS.hasSpeedAccuracy()?locationGPS.getSpeedAccuracyMetersPerSecond():null);
			setTimeGPS(locationGPS.getTime());
		}


		if(locationNetwork!=null){//если locationNETWORK не ссылается на null
			//инициализация переменных класса LocationData:
			setLatitudeNETWORK(locationNetwork.getLatitude());
			setLongitudeNETWORK(locationNetwork.getLongitude());
			setAltitudeNETWORK(locationNetwork.hasAltitude()?locationNetwork.getAltitude():null);
			setSpeedNETWORK(locationNetwork.hasSpeed()?locationNetwork.getSpeed():null);
			setAccuracyHorizontalNETWORK(locationNetwork.hasAccuracy()?locationNetwork.getAccuracy():null);
			setAccuracyVerticalNETWORK(locationNetwork.hasVerticalAccuracy()?locationNetwork.getVerticalAccuracyMeters():null);
			setAccuracySpeedNETWORK(locationNetwork.hasSpeedAccuracy()?locationNetwork.getSpeedAccuracyMetersPerSecond():null);
			setTimeNETWORK(locationNetwork.getTime());
		}

	}
	
	
	
	//проверка наличия разрешения
	public boolean hasPermission(){
		return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED;
	}


}

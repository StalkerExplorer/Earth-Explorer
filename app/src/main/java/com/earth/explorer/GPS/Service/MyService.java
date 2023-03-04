package com.earth.explorer.GPS.Service;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.location.*;
import android.media.*;
import android.os.*;
import android.provider.*;
import androidx.core.app.*;
import androidx.core.content.*;
import com.earth.explorer.*;
import com.earth.explorer.GPS.*;
import com.earth.explorer.Util.*;
import java.util.concurrent.*;

import com.earth.explorer.R;



public class MyService extends Service implements SharedConstants
{

	static Tone tone;
	static Context context;
	static LocationManager mLocationManager;
	public static NotificationManager notificationManager;
	public static GnssStatusCallback gnssStatusCallback;
    public static LocationListener mLocationListener;
	public static Location locationGPS;
	public static Location locationNetwork;
    ExecutorService es;
	public static boolean serviceBoolean;
	private static String provider="";
	

	//private static final String TAG = "myLogs";


	//В сервисе нужны каналы. Каналы появились в API 26
	private void createNotificationChannel(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//найти табл. Каналы появились в API 26, но старые устройства будут просто игнорировать данный параметр при вызове конструктора NotificationCompat.Builder.
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Example Service Channel", NotificationManager.IMPORTANCE_DEFAULT);


			AudioAttributes audioAttributes = new AudioAttributes.Builder()
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.setUsage(AudioAttributes.USAGE_NOTIFICATION)
				.build();


			serviceChannel.setSound(null, audioAttributes);
			//создаём канал:
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
		}
	}


	@Override
	public IBinder onBind(Intent p1)
	{
		return null;
	}



	@Override
    public void onCreate() {
        super.onCreate();
		tone=new Tone();
		//проверим доступного провайдера
		provider = Settings.Secure.getString(getApplicationContext(). getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		context=getApplicationContext();
		createNotificationChannel();///
        //получаем LocationManager
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		gnssStatusCallback=new GnssStatusCallback(this);
		es = Executors.newFixedThreadPool(1);
    }







    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		
		
                     //чтобы показать уведомление и запустить startForeground
		             startForeground(NOTIFY_ID, getNotification());
                     
					 
		             Worker worker = new Worker();
		             es.execute(worker);
		             serviceBoolean=true;//при старте сервиса инициализируем
                     GPSActivity.resultBoolean=true;
		
		             //если вызвался этот метод, значит мы переключаем тумблер вкл/выкл
		             //обновляем данные адаптера пейджера
		             handler.sendEmptyMessage(0);

		             return Service.START_STICKY;
                  }

	

    @Override
    public void onDestroy() {
        super.onDestroy();
		//mLocationManager.unregisterGnssStatusCallback(GPSActivity. gnssStatusCallback);
		gnssStatusCallback.arraySat.clear();//очищаем arrayList
		gnssStatusCallback.countUsedSatellite=0;
		gnssStatusCallback.count=0;
		//снимаем регистрацию:
	    handler.sendEmptyMessage(2);
	    serviceBoolean=false;//при остановке сервиса инициализируем
		GPSActivity.resultBoolean=false;
		notificationManager.cancelAll();//удаляем все уведомления
		es.shutdown();
		
		//если вызвался этот метод, значит мы переключаем тумблер вкл/выкл
		//обновляем данные адаптера пейджера
		handler.sendEmptyMessage(0);
    }






	public static Notification getNotification(){//public  Notification getNotification()

		Notification notification;

		// Create PendingIntent
		//Чтобы выполнить какое-либо действие по нажатию на уведомление,
		//необходимо использовать PendingIntent. PendingIntent - это контейнер для Intent. 
		//Этот контейнер может быть использован для последующего запуска вложенного в него Intent.
		//https://startandroid.ru/ru/uroki/vse-uroki-spiskom/509-android-notifications-osnovy.html
		Intent resultIntent = new Intent(context, GPSActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
																	  PendingIntent.FLAG_IMMUTABLE);
        // Create Notification
		//Используем билдер, в котором указываем иконку, 
		//заголовок и текст для уведомления. 
		NotificationCompat.Builder builder =
			new NotificationCompat.Builder(context,CHANNEL_ID)
			.setSmallIcon(R.drawable.truiton_short)
			.setContentText(providerIsAvailable()? ""+getTextPosition() : "«GPS выключен»" )
			.setUsesChronometer(true)
			.setContentIntent(resultPendingIntent)//вот интент в активити
			//.setContentTitle("Foreground Service")
			.setColor(Color.parseColor("#ff4f00"));

        //Методом build получаем готовое уведомление.
		notification = builder.build();

		//Методом build получаем готовое уведомление.
		notification = builder.build();


		return notification;
	}//public  Notification getNotification()



	//используется в уведомлении выше
	public static String getTextPosition(){
		
		if(locationGPS!=null&&
		    LocationData.latitudeGPS!=null&
		    LocationData.longitudeGPS!=null&
		    LocationData.accuracyHorizontalGPS!=null
		){
		    return DegreeToDegMinSek.latDegMinSek(LocationData.latitudeGPS) +"   "+ DegreeToDegMinSek.lonDegMinSek(LocationData.longitudeGPS)+"  (±"+Math.round(LocationData.accuracyHorizontalGPS)+" м)"+" "+"GPS";
		}else{
		    return "Ожидаю расчёт местоположения...";
		}
	}






	//возвращает разность текущего времени, и времени последней фиксации местоположения, в миллисекундах.
	//нужен для хронометра, добавить к отсчёту, пройденное время
	public static Long getLastFixTimeGPS(){

		if(LocationData.timeGPS!=null){
			return System.currentTimeMillis() -((long) LocationData.timeGPS);
		}else{
			return null;
		}
	}

	//возвращает разность текущего времени, и времени последней фиксации местоположения, в миллисекундах.
	//нужен для хронометра, добавить к отсчёту, пройденное время
	public static Long getLastFixTimeNETWORK(){

		if(LocationData.timeNETWORK!=null){
			return System.currentTimeMillis() -((long) LocationData.timeNETWORK);
		}else{
			return null;
		}
	}


	public static synchronized boolean providerIsAvailable(){
		//надо проверить доступность провайдера
        LocationManager locationManager = (LocationManager) MainActivity.context.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isLocationEnabled();
	}

	
	
	

    //проверка наличия разрешения
	public static boolean hasPermission(){
		return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED;
	}



	public  static  Handler  handler = new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);

			switch(msg.what){
					//обновление данных адаптера пейджера
                case 0://вызывается из GnssStatusCallback,
					//а также при запуске и остановке сервиса
					   if(GPSActivity.viewPagerAdapter!=null){
					         GPSActivity.viewPagerAdapter.notifyDataSetChanged();
					   }
					break;
					
					
				//регистрируем
				case 1:
					mLocationManager.registerGnssStatusCallback(gnssStatusCallback);
					mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_FOR_UPDATES, mLocationListener);
					mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_FOR_UPDATES, mLocationListener);										
				break;
				
				//снимаем регистрацию
				case 2:
					mLocationManager.unregisterGnssStatusCallback(gnssStatusCallback);
					mLocationManager.removeUpdates(mLocationListener);
				break;
					
					
				case 3:
					tone.tone();
				break;
				
		    }
		}

	};


	
	
	
	
	
	


	//ВЫЗЫВАЕТСЯ В ДОЧЕРНЕМ ПОТОКЕ//ВЫЗЫВАЕТСЯ В ДОЧЕРНЕМ ПОТОКЕ//ВЫЗЫВАЕТСЯ В ДОЧЕРНЕМ ПОТОКЕ//ВЫЗЫВАЕТСЯ В ДОЧЕРНЕМ ПОТОКЕ
    //showCurrentLocation
	public static void showCurrentLocation() {
		if(hasPermission()){//если есть разрешения
		if(mLocationListener!=null){//если уже существует слушатель, откуда кстати вызывалось это всё
			if(serviceBoolean){//если работает сервис




				//получаем сведения о последнем известном местоположении
				//при этом смотрим двух провайдеров
				//GPS:
				locationGPS = MyService. mLocationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);//Получает последнее известное местоположение от данного поставщика или null, если последнего известного местоположения нет.
				//NETWORK:
				locationNetwork = MyService. mLocationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);//Получает последнее известное местоположение от данного поставщика или null, если последнего известного местоположения нет.


				if(locationGPS!=null){//если locationGPS уже перестал ссылаться на null
					//инициализация переменных класса LocationData:
					LocationData.setLatitudeGPS(locationGPS.getLatitude());
					LocationData.setLongitudeGPS(locationGPS.getLongitude());
					LocationData.setAltitudeGPS(locationGPS.hasAltitude()?locationGPS.getAltitude():null);
					LocationData.setSpeedGPS(locationGPS.hasSpeed()?locationGPS.getSpeed():null);
					LocationData.setAccuracyHorizontalGPS(locationGPS.hasAccuracy()?locationGPS.getAccuracy():null);
					LocationData.setAccuracyVerticalGPS(locationGPS.hasVerticalAccuracy()?locationGPS.getVerticalAccuracyMeters():null);
				    LocationData.setAccuracySpeedGPS(locationGPS.hasSpeedAccuracy()?locationGPS.getSpeedAccuracyMetersPerSecond():null);
					LocationData.setTimeGPS(locationGPS.getTime());
				}


				if(locationNetwork!=null){//если locationNETWORK уже перестал ссылаться на null
					//инициализация переменных класса LocationData:
					LocationData.setLatitudeNETWORK(locationNetwork.getLatitude());
					LocationData.setLongitudeNETWORK(locationNetwork.getLongitude());
					LocationData.setAltitudeNETWORK(locationNetwork.hasAltitude()?locationNetwork.getAltitude():null);
					LocationData.setSpeedNETWORK(locationNetwork.hasSpeed()?locationNetwork.getSpeed():null);
					LocationData.setAccuracyHorizontalNETWORK(locationNetwork.hasAccuracy()?locationNetwork.getAccuracy():null);
					LocationData.setAccuracyVerticalNETWORK(locationNetwork.hasVerticalAccuracy()?locationNetwork.getVerticalAccuracyMeters():null);
					LocationData.setAccuracySpeedNETWORK(locationNetwork.hasSpeedAccuracy()?locationNetwork.getSpeedAccuracyMetersPerSecond():null);
					LocationData.setTimeNETWORK(locationNetwork.getTime());
				}

				
				//обновляем уведомление
				notificationManager.notify(NOTIFY_ID, getNotification());

				//if(ViewPagerAdapter.page==0){
				//handler.sendEmptyMessage(0);
				//}



				


			}//if(serviceBoolean){//если работает сервис
		}//if(mLocationListener!=null){//если уже существует слушатель, откуда кстати вызывалось это всё
	  }//если есть разрешения
    }//showCurrentLocation
	//ВЫЗЫВАЕТСЯ В ДОЧЕРНЕМ ПОТОКЕ//ВЫЗЫВАЕТСЯ В ДОЧЕРНЕМ ПОТОКЕ//ВЫЗЫВАЕТСЯ В ДОЧЕРНЕМ ПОТОКЕ//ВЫЗЫВАЕТСЯ В ДОЧЕРНЕМ ПОТОКЕ



    

	class Worker implements Runnable
	{


		public void run() {//public void run() {
			Looper.prepare();//Инициализировать текущий дочерний поток как цикл.
			//____________________________________________________________________

            

			//LocationListener//LocationListener//LocationListener//LocationListener
			//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		    mLocationListener = new LocationListener() {



				@Override
				synchronized public void onLocationChanged(Location location) {
					//<<<<<<<в дочернем потоке>>>>>>
					//служба останавливает сама себя:
					if(hasPermission()){
						
						if(GPSActivity. providerIsAvailable()){//11
                              showCurrentLocation();
						      if(location.getProvider().equals(LocationManager.GPS_PROVIDER)){handler.sendEmptyMessage(3);}
						}//11
						
						if(!GPSActivity. providerIsAvailable()){//22
							  serviceStopSelf();
						}//22
					}
				}




				@Override
				synchronized public void onStatusChanged(String s, int i, Bundle b) {

				}



				//Вызывается, когда поставщик, у которого зарегистрирован этот прослушиватель, 
				//становится отключенным. Если провайдер отключен, когда этот слушатель зарегистрирован, 
				//этот обратный вызов будет вызван немедленно.
				synchronized public void onProviderDisabled(String s) {
					//если провайдеры стали недоступны 
					//(на случай, когда мы отключили в настройках локацию, 
					//во время работы сервиса)
			
					//служба останавливает сама себя:
					if(!GPSActivity. providerIsAvailable()){serviceStopSelf(); GPSActivity.myFinish(); }
				}



				//Вызывается, когда провайдер, с которым зарегистрирован этот прослушиватель, становится активным.
				synchronized public void onProviderEnabled(String s) {
					//Log.d(TAG, "onProviderEnabled(): "+ s);
				}


			};

			
			
			if(hasPermission()){//если есть разрешения
            // Регистрируемся для обновлений
			handler.sendEmptyMessage(1);
			//mLocationManager.registerGnssStatusCallback(GPSActivity.gnssStatusCallback);
            }//если есть разрешения
			
			
			
			//_________________________________________________________
			if(!hasPermission()){//останавливаем сервис
				serviceStopSelf();
			}
			
			
			Looper.loop();//Запустите очередь сообщений в этом потоке.
		}//закрылся public void run() {




	}


	public void serviceStopSelf(){
		stopSelf();//обьявлен как final (это значит что, он не может быть переопределен в подклассах)
		tone.toneError();
	}

}

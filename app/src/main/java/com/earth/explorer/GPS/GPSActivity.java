package com.earth.explorer.GPS;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.location.*;
import android.os.*;
import android.provider.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.Chronometer.*;
import android.widget.CompoundButton.*;
import android.widget.LinearLayout.*;
import androidx.core.app.*;
import androidx.core.content.*;
import androidx.viewpager2.widget.*;
import com.earth.explorer.*;
import com.earth.explorer.GPS.Service.*;
import com.earth.explorer.GPS.View.*;
import com.earth.explorer.Util.*;
import java.util.*;

import android.content.ClipboardManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import com.earth.explorer.R;
import com.earth.explorer.SunriseSunset.*;

public class GPSActivity extends Activity implements SharedConstants
{
	
	
	public static Indicator pagerIndicatorView;
	final String TAG="MyLog";
	//static Context context;
	public static GPSActivity mGPSActivity;
	public static boolean resultBoolean;
	public static ViewPager2 viewPager;
	public static ViewPagerAdapter viewPagerAdapter;
	public static LocationData locationData;
	//public static String provider="";
	public static int page=-1;
	public static int sort=1;
	static int mysortI=0;
	public static int q=0;
	public static int dialogSat;
	
	
	//первая страница
	static TextView[] textViewGPS;
	static TextView[] textViewNetWork;
	static TextView textViewCountSat;
	static TextView textViewSatelliteUsed;
	static ProgressBar progressBar0;//ProgressBar в заголовке, под этими Textview
	static TextView textViewProgress1;//TextView над progressBar1
	static ProgressBar progressBar1;//ProgressBar сверху, над SignalProgress
	static Switch switchCompat;
	static Chronometer chronometerGPS;
	static Chronometer chronometerNETWORK;
	static BarGraph barGraph;
	static SignalProgress signalProgress;
	static TextView tvGPS;
	static TextView tvNETWORK;
	//первая страница

	//вторая страница
	static LinearLayout satelliteHeaderLinearLayout;//layout заголовка
	static TextView satellitelistTextViewVisible;//первый TextView заголовка (в котором сначало текст Working, а затем: "всего в поле зрения")
	//static TextView satellitelistTextViewInfo;//второй TextView заголовка 
	static TextView satellitelistTextViewUse;//третий TextView заголовка (в котором сначало текст Working, а затем: "ожидаю расчет фиксации положения")
	static ProgressBar progressBar2;//ProgressBar в заголовке, под этими Textview
	static TextView satelliteSortTextView;//TextView в заголовке, с текстом "Отсортировано: используются в определении"

	static TextView textView_Header_Svid;
	static TextView textView_Header_ConstellationType;
	static TextView textView_Header_CarrierFrequencyHz;
	static TextView textView_Header_DbHz;
	static TextView textView_Header_ElevationDegrees;
	static TextView textView_Header_AzimuthDegrees;

	static ListView satellitelistListView1;
	//вторая страница
	
	//третья страница
	static SkyView skyView;
	static TextView skyTextView;
	static BarGraph barGraphBasebandCn0DbHz;
	static BarGraph barGraphCn0DbHz;
	static TextView textViewCountSatSky;
	static TextView textViewSatelliteUsedSky;
	static ProgressBar progressBar3;
	//третья страница
	
	//четвертая страница
	static TextView textView_Sunrise_Top;
	static TextView textView_Sunrise_Bottom;
	static TextView textView_Sunset_Top;
	static TextView textView_Sunset_Bottom;
	static TextView textViewTWILIGHTCIVILsunrise;
	static TextView textViewTWILIGHTCIVILsunset;
	static TextView textViewTWILIGHTNAUTICALsunrise;
	static TextView textViewTWILIGHTNAUTICALsunset;
	static TextView textViewTWILIGHTASTRONOMICALsunrise;
	static TextView textViewTWILIGHTASTRONOMICALsunset;
	static TextView textViewTWILIGHTGOLDENHOURsunrise;
	static TextView textViewTWILIGHTGOLDENHOURsunset;
	static TextView textViewTWILIGHTBLUEHOURsunrise;
	static TextView textViewTWILIGHTBLUEHOURsunset;
	static TextView textViewTWILIGHTNIGHTHOUR;
	static TextView textViewSunNOON;
	static TextView textViewSunNADIR;
	
	static TextView textViewAltitudeSun;
	static TextView textViewTrueAltitudeSun;
	static TextView textViewAzimuthSun;
	static TextView textViewDistanceSun;
	
	static TextView textView_RiseMoon;
	static TextView textView_SetMoon;
	static TextView textViewAltitudeMoon;
	static TextView textViewAzimuthMoon;
	static TextView textViewDistanceMoon;
	//четвертая страница
	
	
	
	
	
	
	public static boolean[] sortBoolean;//для сортировок убывание/возрастание (ряд TextView второй страницы)
	//массив "только подключенные"
	static HashMap myBooleanArray;
    // это будет именем файла настроек:
	final String APP_PREFERENCES = "mysettings"; 
	SharedPreferences mSettings;
	static SharedPreferences.Editor editor;
	
	static ArrayList <MySatellite> arrayMemory;
	public static boolean usedInFix[];
	public static boolean usedInFixFlash[];
	public static int basebandCn0DbHz[];
	public static float elevation[];
	public static float azimuth[];
	//public static String satString[];
	
	static boolean satelliteDialog;
	
	static boolean satB;
	static String constellationType1="";
	static int svid1=0;
	
	public static boolean GPSActivityBoolean;
	
	
    
	
	
	
     @Override
	 protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//раздуваем макет
		setContentView(R.layout.gps_layout);
		
		 
		
		 //context=this;
		 mGPSActivity=this;
		 
		 /*findView();
		 if(!page0_is_Null()){
			 pagerIndicatorView.setVisibility(View.INVISIBLE);
			 pagerIndicatorView.setEnabled(false);
		 }*/
		 
		 //надо проверить доступого провайдера
		 //provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		 
		 locationData=new LocationData(this);
		 
		 
		 //получим экземпляр класса SharedPreferences для получения доступа к настройкам
		 mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		 //чтобы вносить изменения в настройки, получим экземпляр класса SharedPreferences.Editor
		 editor = mSettings.edit();
	
		 //массив - чекнут/не чекнут
		 myBooleanArray=new HashMap();
		 //извлекаем чекнуты ли чекбоксы 2стр
		 //и сразу заполняем массив
		 myBooleanArray.put(BEIDOU,   mSettings.getBoolean(BEIDOU, true)  );
		 myBooleanArray.put(GALILEO,  mSettings.getBoolean(GALILEO, true) );
		 myBooleanArray.put(GLONASS,  mSettings.getBoolean(GLONASS, true) );
		 myBooleanArray.put(GPS,      mSettings.getBoolean(GPS, true)     );
		 myBooleanArray.put(IRNSS,    mSettings.getBoolean(IRNSS, true)   );
		 myBooleanArray.put(QZSS,     mSettings.getBoolean(QZSS, true)    );
		 myBooleanArray.put(SBAS,     mSettings.getBoolean(SBAS, true)    );
		 myBooleanArray.put(UNKNOWN,  mSettings.getBoolean(UNKNOWN, true) );
		 myBooleanArray.put(USEDINFIX,  mSettings.getBoolean(USEDINFIX, false) );

		 
		 //извлечем значение переменной для сортировки, и инициализируем переменную сортировки
		 sort=mSettings.getInt(SORT,1);

		 //извлечем массив сортировок возрастание/убывание
		 sortBoolean=new boolean[6];//массив сортировок возрастание убывание
		 for(int i=0;i<sortBoolean.length;i++){
			    sortBoolean[i]=mSettings.getBoolean(SORT_BOOLEAN[i], true);//извлечем из Б.Д.
		 }
		 
		
		 
		 arrayMemory=new ArrayList<MySatellite>();
		 
		 
		 //находим viewPager2:
		 viewPager= findViewById(R.id.gpslayoutViewPager);
		 //создаём адаптер:
		 viewPagerAdapter=new ViewPagerAdapter();
		 //назначаем ViewPager2 адаптер:
		 viewPager.setAdapter(viewPagerAdapter);
		 //регистрируем Listener
		 viewPager.registerOnPageChangeCallback(onPageChangeCallback);
		 
		 //viewPager.setOffscreenPageLimit(4);
		 Log.d(TAG, "onCreate()");
	}
	
	
	
	//проверка наличия разрешения
	public static synchronized boolean hasPermission(){
		return ContextCompat.checkSelfPermission(mGPSActivity, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED;
	}
	
	
	public static void permission(){
		//Если разрешения нет, то нам надо его запросить. 
		//Это выполняется методом requestPermissions. 
	 ActivityCompat.requestPermissions(mGPSActivity, new String[] {//Manifest.permission.ACCESS_BACKGROUND_LOCATION,
																		//Manifest.permission.ACCESS_COARSE_LOCATION,
																		Manifest.permission.ACCESS_FINE_LOCATION
																		//Manifest.permission.ACCESS_BACKGROUND_LOCATION 
																	},
																	REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
	}
	
	
	@Override//при переопределении метода лучше ничего лишнего в код не вписывать
	//особенно какое-нибудь обращение к активности, 
	//например textViewInfo.setText("text")
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION:
				if ((grantResults.length > 0)&& 
					(grantResults[0] == PackageManager.PERMISSION_GRANTED)
				//&& grantResults[1] == PackageManager.PERMISSION_GRANTED
					){

					//пользователь предоставил нужные разрешения
					//metod2();//выполняем остальной код
					if(providerIsAvailable()){//если вкл локация
						MyService.serviceBoolean=true;
						viewPager.unregisterOnPageChangeCallback(onPageChangeCallback);
						viewPager.setAdapter(null);
						resultBoolean=true;
						viewPager.setAdapter(viewPagerAdapter);
						viewPager.registerOnPageChangeCallback(onPageChangeCallback);
						onPageChangeCallback.onPageSelected(0);
					}
					
					if(!providerIsAvailable()){//если выкл локация
					    openLocationSetting();//просим пользователя включить локацию
					}
					
				} 


				//иначе пользователь отказал в разрешении или выбрал не те (нам нужно в любом режиме)
				else {

					//показываем пользователю диалог, 
					//что разрешение необходимо, и необходимо в любом режиме:
					//showDialog(2);
                    //finish();

					Toaster.toast(GPSActivity. mGPSActivity,"PERMISSION_DENIED" +"\n"+ "Предоставьте разрешения в настройках",2000);
				}
				return;
				
			//case REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION:
			//break;
				
		}
	}
	
	
	
	
	public void openLocationSetting(){
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		GPSActivity. mGPSActivity. startActivityForResult(intent,SETTINGS_CODE);
	}
	
	
	
	float textSize=18;
	public void onclTextView(View v){
		
		
		ClipboardManager clipboard = (ClipboardManager) mGPSActivity.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip;
		
		switch (v.getId()){
			
			case R.id.textViewGPS:
				//textSize= tvGPS.getTextSize();
				tvGPS.setTextSize(textSize +0.3f);
				tvGPS.setTextColor(Color.parseColor("#2d84ff"));
				tvGPS.postDelayed(myActionTvGPS,100);
				//копируем данные в буфер обмена
				clip = ClipData.newPlainText("", getTextInfo("GPS"));
				clipboard.setPrimaryClip(clip);
				Toaster.toast(mGPSActivity,"Данные скопированы");
				satelliteDialog=false;
				
				Vibration.vibrate(this);
			break;
			
			case R.id.textViewNETWORK:
				//textSize= tvNETWORK.getTextSize();
				tvNETWORK.setTextSize(textSize +0.3f);
				tvNETWORK.setTextColor(Color.parseColor("#2d84ff"));
				tvNETWORK.postDelayed(myActionTvNETWORK,100);
				//копируем данные в буфер обмена
				clip = ClipData.newPlainText("", getTextInfo("NETWORK"));
				clipboard.setPrimaryClip(clip);
				Toaster.toast(mGPSActivity,"Данные скопированы");
				
				Vibration.vibrate(this);
			break;
		}
		
	}
	
	
	Runnable myActionTvGPS = new Runnable(){
		@Override
		public void run()
		{
			tvGPS.setTextSize(textSize);
			tvGPS.setTextColor(Color.parseColor("#FF4908"));
		}
	};
	
	
	Runnable myActionTvNETWORK = new Runnable(){
		@Override
		public void run()
		{
			tvNETWORK.setTextSize(textSize);
			tvNETWORK.setTextColor(Color.parseColor("#FF4908"));
		}
	};
	
	
	
	
	
	
	public static String getTextInfo(String Provider){
		
		String str="";
		
		
		if(Provider.equals("GPS")){
			str= "Данные местоположения, определённые по спутникам:"+"\n\n"+
			     "Широта: "+textViewGPS[0].getText().toString()+"\n"+
				 "Долгота: "+textViewGPS[1].getText().toString()+"\n"+
				 "Высота: "+textViewGPS[2].getText().toString()+"\n"+
				 "Скорость: "+textViewGPS[3].getText().toString()+"\n"+
				 "Точность по горизонтали: "+textViewGPS[4].getText().toString()+"\n"+
				 "Точность по вертикали: "+textViewGPS[5].getText().toString()+"\n"+
				 "Точность скорости: "+textViewGPS[6].getText().toString()+"\n"+
				 "Время получения данных: "+textViewGPS[7].getText().toString()
			;
		}
		
		
		
		if(Provider.equals("NETWORK")){
            str= "Данные местоположения, определённые по сети:"+"\n\n"+
				 "Широта: "+textViewNetWork[0].getText().toString()+"\n"+
				 "Долгота: "+textViewNetWork[1].getText().toString()+"\n"+
				 "Высота: "+textViewNetWork[2].getText().toString()+"\n"+
				 "Скорость: "+textViewNetWork[3].getText().toString()+"\n"+
				 "Точность по горизонтали: "+textViewNetWork[4].getText().toString()+"\n"+
				 "Точность по вертикали: "+textViewNetWork[5].getText().toString()+"\n"+
				 "Точность скорости: "+textViewNetWork[6].getText().toString()+"\n"+
				 "Время получения данных: "+textViewNetWork[7].getText().toString()
				;
		}
		
		
		return str;
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO: Implement this method

		switch(requestCode){

				//если мы возвращались из манипуляций с вкл/выкл GPS
				//тогда был отправлен SETTINGS_CODE, который сюда пришел
			case SETTINGS_CODE:
				//надо проверить доступность провайдера
				//если включили локация, то выполнится код ниже
				if(providerIsAvailable()){
					/*MyService.serviceBoolean=true;
					if(page!=-1){onPageChangeCallback.onPageSelected(page);}//чтобы прошел вызов onPageSelected();
					viewPagerAdapter.notifyDataSetChanged();*/
					
					MyService.serviceBoolean=true;
		
					viewPager.unregisterOnPageChangeCallback(onPageChangeCallback);
					viewPager.setAdapter(null);
					resultBoolean=true;
					viewPager.setAdapter(viewPagerAdapter);
					viewPager.registerOnPageChangeCallback(onPageChangeCallback);
					onPageChangeCallback.onPageSelected(0);
					
			    }
				break;

		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
	
	@Override
	protected void onResume()
	{
	
		
		//resultBoolean=false;
		//если провайдер стал недоступен. Уходя на паузу, мы могли отключить локацию в настройках системы
		if(!providerIsAvailable()){
			viewPager.setCurrentItem(0);
			}

		//если отозвали разрешения во время работы программы
		if(!hasPermission()){
			viewPager.setCurrentItem(0);
			}
		
			
		
			
		viewPagerAdapter.notifyDataSetChanged();
	
		
		Log.d(TAG, "onResume()");
		super.onResume();
	}

	
	
	
	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		//page=-1;
		super.onPause();
	}

	
	
	public static void myFinish(){
		mGPSActivity. finish();
	}
	
	
	@Override
	public void finish()
	{
		// TODO: Implement this method
		super.finish();
	}
	
	
	

	
	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		page=-1;
		GPSActivityBoolean=false;
		Toaster.toast(this,"onDestroy()\nGPSActivity");
		super.onDestroy();
	}
	
	
	
	
	
	public static synchronized boolean providerIsAvailable(){
		//надо проверить доступность провайдера
        LocationManager locationManager = (LocationManager) MainActivity.context.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isLocationEnabled();
	}
	
	
	//при листании пейджера нужно находить элементы
	public static synchronized void findView(){

		pagerIndicatorView=mGPSActivity. findViewById(R.id.pagerIndicatorView1);
		
		//первая страница
		//GPS
		textViewGPS=new TextView[]{
		    mGPSActivity. findViewById(R.id.gpspage1TextView1),//широта
			mGPSActivity. findViewById(R.id.gpspage1TextView3),//долгота
			mGPSActivity. findViewById(R.id.gpspage1TextView5),//высота
		    mGPSActivity. findViewById(R.id.gpspage1TextView7),//скорость
			mGPSActivity. findViewById(R.id.gpspage1TextView9),//точности по горизонтали
		    mGPSActivity. findViewById(R.id.gpspage1TextView11),//Точность по вертикали
			mGPSActivity. findViewById(R.id.gpspage1TextView13),//Точность скорости
			mGPSActivity. findViewById(R.id.gpspage1TextView15),//Время
		};


		//NETWORK
		textViewNetWork=new TextView[]{
			mGPSActivity. findViewById(R.id.gpspage1TextView2),//широта
			mGPSActivity. findViewById(R.id.gpspage1TextView4),//долгота
		    mGPSActivity. findViewById(R.id.gpspage1TextView6),//высота
			mGPSActivity. findViewById(R.id.gpspage1TextView8),//скорость
			mGPSActivity. findViewById(R.id.gpspage1TextView10),//точности по горизонтали
			mGPSActivity. findViewById(R.id.gpspage1TextView12),//точность по вертикали
			mGPSActivity. findViewById(R.id.gpspage1TextView14),//точность скорости
			mGPSActivity. findViewById(R.id.gpspage1TextView16),//время

		};


		textViewCountSat=mGPSActivity. findViewById(R.id.textViewCountSat);
		textViewSatelliteUsed=mGPSActivity. findViewById(R.id.textViewSatelliteUsed);
		progressBar0=mGPSActivity. findViewById(R.id.progressBar0);
		textViewProgress1=mGPSActivity. findViewById(R.id.textViewProgress1);
		progressBar1=mGPSActivity. findViewById(R.id.progressBar1);
		switchCompat=mGPSActivity. findViewById(R.id.mylocationSwitch);
		chronometerGPS=mGPSActivity. findViewById(R.id.chronometerGPS);
		chronometerNETWORK=mGPSActivity. findViewById(R.id.chronometerNETWORK);
		barGraph=mGPSActivity. findViewById(R.id.barGraph);
		signalProgress=mGPSActivity. findViewById(R.id.signalProgress);
		tvGPS=mGPSActivity. findViewById(R.id.textViewGPS);
		tvNETWORK=mGPSActivity. findViewById(R.id.textViewNETWORK);
		
		//первая страница

		//вторая страница
		satelliteHeaderLinearLayout=mGPSActivity. findViewById(R.id.satelliteHeaderLinearLayout1);
		satellitelistTextViewVisible=mGPSActivity. findViewById(R.id.satellitelistTextViewVisible);
		//satellitelistTextViewInfo=mGPSActivity. findViewById(R.id.satellitelistTextViewVisibleInfo);
		satellitelistTextViewUse=mGPSActivity. findViewById(R.id.satellitelistTextViewUse);
		progressBar2=mGPSActivity. findViewById(R.id.progressBar2);
		satelliteSortTextView=mGPSActivity. findViewById(R.id.satelliteSort);
		textView_Header_Svid=mGPSActivity. findViewById(R.id.textView_Header_Svid);
		textView_Header_ConstellationType=mGPSActivity. findViewById(R.id.textView_Header_ConstellationType);
		textView_Header_CarrierFrequencyHz=mGPSActivity. findViewById(R.id.textView_Header_CarrierFrequencyHz);
		textView_Header_DbHz=mGPSActivity. findViewById(R.id.textView_Header_DbHz);
		textView_Header_ElevationDegrees=mGPSActivity. findViewById(R.id.textView_Header_ElevationDegrees);
		textView_Header_AzimuthDegrees=mGPSActivity. findViewById(R.id.textView_Header_AzimuthDegrees);
		satellitelistListView1=mGPSActivity. findViewById(R.id.satellitelistListView1);
	    //вторая страница
		
		//третья страница
		skyView=mGPSActivity.findViewById(R.id.skyView);
		skyTextView=mGPSActivity.findViewById(R.id.skyTextView1);
		barGraphBasebandCn0DbHz=mGPSActivity.findViewById(R.id.barGraphBasebandCn0DbHz);
		barGraphCn0DbHz=mGPSActivity.findViewById(R.id.barGraphCn0DbHz);
		textViewCountSatSky=mGPSActivity.findViewById(R.id.textViewCountSatSky);
		textViewSatelliteUsedSky=mGPSActivity.findViewById(R.id.textViewSatelliteUsedSky);
		progressBar3=mGPSActivity.findViewById(R.id.progressBar3);
		//третья страница
		
		//четвертая страница
		textView_Sunrise_Top=mGPSActivity.findViewById(R.id.textView_Sunrise_Top);
	    textView_Sunrise_Bottom=mGPSActivity.findViewById(R.id.textView_Sunrise_Bottom);
	    textView_Sunset_Top=mGPSActivity.findViewById(R.id.textView_Sunset_Top);
	    textView_Sunset_Bottom=mGPSActivity.findViewById(R.id.textView_Sunset_Bottom);
		
	    textViewTWILIGHTCIVILsunrise=mGPSActivity.findViewById(R.id.textViewTWILIGHTCIVILsunrise);
	    textViewTWILIGHTCIVILsunset=mGPSActivity.findViewById(R.id.textViewTWILIGHTCIVILsunset);
	    textViewTWILIGHTNAUTICALsunrise=mGPSActivity.findViewById(R.id.textViewTWILIGHTNAUTICALsunrise);
	    textViewTWILIGHTNAUTICALsunset=mGPSActivity.findViewById(R.id.textViewTWILIGHTNAUTICALsunset);
	    textViewTWILIGHTASTRONOMICALsunrise=mGPSActivity.findViewById(R.id.textViewTWILIGHTASTRONOMICALsunrise);
	    textViewTWILIGHTASTRONOMICALsunset=mGPSActivity.findViewById(R.id.textViewTWILIGHTASTRONOMICALsunset);
	    textViewTWILIGHTGOLDENHOURsunrise=mGPSActivity.findViewById(R.id.textViewTWILIGHTGOLDENHOURsunrise);
	    textViewTWILIGHTGOLDENHOURsunset=mGPSActivity.findViewById(R.id.textViewTWILIGHTGOLDENHOURsunset);
	    textViewTWILIGHTBLUEHOURsunrise=mGPSActivity.findViewById(R.id.textViewTWILIGHTBLUEHOURsunrise);
	    textViewTWILIGHTBLUEHOURsunset=mGPSActivity.findViewById(R.id.textViewTWILIGHTBLUEHOURsunset);
		textViewTWILIGHTNIGHTHOUR=mGPSActivity.findViewById(R.id.textViewTWILIGHTNIGHTHOUR);
	    textViewSunNOON=mGPSActivity.findViewById(R.id.textViewSunNOON);
	    textViewSunNADIR=mGPSActivity.findViewById(R.id.textViewSunNADIR);
		textViewAltitudeSun=mGPSActivity.findViewById(R.id.textViewAltitudeSun);
		textViewTrueAltitudeSun=mGPSActivity.findViewById(R.id.textViewTrueAltitudeSun);
		textViewAzimuthSun=mGPSActivity.findViewById(R.id.textViewAzimuthSun);
		textViewDistanceSun=mGPSActivity.findViewById(R.id.textViewDistanceSun);
		
	    textView_RiseMoon=mGPSActivity.findViewById(R.id.textView_Rise_Moon);
	    textView_SetMoon=mGPSActivity.findViewById(R.id.textView_SetMoon);
	    textViewAltitudeMoon=mGPSActivity.findViewById(R.id.textViewAltitudeMoon);
	    textViewAzimuthMoon=mGPSActivity.findViewById(R.id.textViewAzimuthMoon);
	    textViewDistanceMoon=mGPSActivity.findViewById(R.id.textViewDistanceMoon);
		//четвертая страница
		
		
		
		
	}
	
	
	
	
	
	
	static ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {

		@Override
		// Этот метод срабатывает, когда для текущей страницы есть какая-либо активность прокрутки.
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { 
			super.onPageScrolled(position, positionOffset, positionOffsetPixels); 
			//TODO: Implement this method
			findView();//находим элементы разметок
			if(pagerIndicatorView!=null){
			    pagerIndicatorView.setProgress(viewPager,position,positionOffset);
			}
		} 

		
		
		@Override
        // срабатывает при выборе новой страницы
		public void onPageSelected(int position) { 
			super.onPageSelected(position); 
			//Toaster.toast(mGPSActivity,"onPageSelected");
			//TODO: Implement this method
			page=position;//делаем видимой переменную для других методов 
			findView();//находим элементы разметок
			

			//Toaster.toast(mGPSActivity,"onPageSelected()");
			
			switch(page){
				case 0:
				
					
					//обновляем view в разметках 
					//(этот метод вызывается из GnssStatusCallback, и отсюда, при листании стр. А также вызовется при onResume())
					update();
			    break;
			}
		} 
	};

	
	
	//звоним сюда из GnssStatusCallback
	//обновлять элементы GUI из дочернего потока нельзя
    //мы используем Handler
	public static  Handler  handler = new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);

			switch(msg.what){

				case 0:
					update();//обновляем view в разметках (view.invalidate())
				break;
			}
		}
	};
	
	
	
	
	
	
	
	
	
	
	public static synchronized void update(){
		
		switch(page){
		
			case 0:
				     if(!page0_is_Null()){//если ни один обьект первой страницы пейджера, не ссылается на null
		                //вписываем информацию о местоположении
						//Toaster.toast(mGPSActivity,"update()");
		                setData();
						startChronometer();
                        signalProgress.invalidate();
						barGraph.invalidate();
						
						viewPagerAdapter.setVisible(switchCompat,
									                textViewCountSat,
									                textViewSatelliteUsed,
									                progressBar0,
									                textViewProgress1,
									                progressBar1);
						 
						 
				     }//если ни один обьект первой страницы пейджера, не ссылается на null
		    break;
			
			
			case 1:
				if(!page1_is_Null()){//если ни один обьект второй страницы пейджера, не ссылается на null
				    satellitelistListView1.setOnItemLongClickListener(onitemLong);
					satelliteHeaderLinearLayout.setOnLongClickListener(new OnLongClickListener(){

							@Override
							public boolean onLongClick(View p1)
							{
								dialog(satellitelistListView1);
							    Vibration.vibrate(mGPSActivity);
								return false;
							}
						});


					satelliteHeaderLinearLayout.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								// TODO: Implement this method
								satellitelistListView1.smoothScrollToPosition(0);
								Vibration.vibrate(mGPSActivity);
							}
					});

					//создадим слушателя
					TextViewOncl textViewOncl=new TextViewOncl(satellitelistListView1);
					//назначим этим TextView слушателя
					textView_Header_Svid.setOnClickListener(textViewOncl);
					textView_Header_ConstellationType.setOnClickListener(textViewOncl);
					textView_Header_CarrierFrequencyHz.setOnClickListener(textViewOncl);
					textView_Header_DbHz.setOnClickListener(textViewOncl);
					textView_Header_ElevationDegrees.setOnClickListener(textViewOncl);
					textView_Header_AzimuthDegrees.setOnClickListener(textViewOncl);
					//установим им BackgroundDrawable
					textView_Header_Svid.setBackgroundDrawable(mGPSActivity.getDrawable(R.drawable.selector_textview));
					textView_Header_ConstellationType.setBackgroundDrawable(mGPSActivity.getDrawable(R.drawable.selector_textview));
					textView_Header_CarrierFrequencyHz.setBackgroundDrawable(mGPSActivity.getDrawable(R.drawable.selector_textview));
					textView_Header_DbHz.setBackgroundDrawable(mGPSActivity.getDrawable(R.drawable.selector_textview));
					textView_Header_ElevationDegrees.setBackgroundDrawable(mGPSActivity.getDrawable(R.drawable.selector_textview));
					textView_Header_AzimuthDegrees.setBackgroundDrawable(mGPSActivity.getDrawable(R.drawable.selector_textview));


					satelliteHeaderLinearLayout.setBackgroundDrawable(mGPSActivity.getDrawable(R.drawable.states_shapes_header));

					

					satelliteSortTextView.setText(getSortText(sort, sortBoolean[sort]));//текст вида: "Отсортировано: ОНШП", и т.д.
					
					
					/*satelliteHeaderLinearLayout.setOnClickListener(new OnClickListener(){
                 
							@Override
							public void onClick(View p1)
							{
								
								sort=0;
								
								//if(sort<5){sort++;}else{sort=0;}
								satelliteSortTextView.setTextColor(Color.WHITE);
								satelliteSortTextView.setText(getSortText(sort, true));//текст вида: "Отсортировано: ОНШП", и т.д.
								satelliteSortTextView.postDelayed(myAction,100);
								satellitelistListView1.smoothScrollToPosition(0);
								editor.putInt(SORT,sort);//сразу положим в Б.Д.
								editor.apply();
								Vibration.vibrate(mGPSActivity);
							}
						});*/



					progressBar2.setVisibility(View.INVISIBLE);
					
					
					String str=getVisibleStr(MyService. gnssStatusCallback.count)+" спутников"+"<br>"+
					           getSatelliteTextViewInfo();
					
					Spanned spanned= Html.fromHtml(str);
			
					
					satellitelistTextViewVisible.setText(spanned);//текст вида "Всего в поле зрения: 47"
					//satellitelistTextViewInfo.setText(Html.fromHtml(  getVisibleInfoStr()  );
					



					//метка
					ViewPagerAdapter.satellitelistTextViewUse(satellitelistTextViewUse);
					

					//если хотя бы один спутник используется для расчета фиксации местоположения
					if(MyService. gnssStatusCallback.getCountUsedFix()!=0){//текст вида: "Использовалось в расчете последней фиксации положения: 5"
						//возвращаем gravity-параметр
						satellitelistTextViewUse.setGravity(Gravity.CENTER);
						//возращаем на место отступ:
						satellitelistTextViewUse.setPadding(3,3,3,3);
						satellitelistTextViewUse.setSingleLine(false);
						satellitelistTextViewUse.setText(Html.fromHtml(getUseSatelliteStr(MyService. gnssStatusCallback.getCountUsedFix())+"<br>"+
						
																	   "<font face=sans-serif-black color=#90CAF9>"+//03A9F4//
																	   "<small>"+
																	   "<small>"+
																	   "("+viewPagerAdapter.getUsedSat()+" )"+
																	   "</small>"+
																	   "</small>"+
																	   "</font>"));
																	   
																	   
					}



					if(MyService. gnssStatusCallback.count!=0){//и, если есть хотя бы 1 спутник в поле зрения
						mysort(satellitelistListView1);//вызываем метод сортировки
					}

					
				}//если ни один обьект второй страницы пейджера, не ссылается на null
			break;
			
			
			case 2:
				if(!page2_is_Null()){//если ни один обьект третьей страницы пейджера, не ссылается на null
				       skyView.invalidate();
					   barGraphBasebandCn0DbHz.invalidate();
					   barGraphCn0DbHz.invalidate();
					   
					viewPagerAdapter.setVisible2(textViewCountSatSky,
												 textViewSatelliteUsedSky,
												 progressBar3);
				}//если ни один обьект третьей страницы пейджера, не ссылается на null
			break;
				
			
			
			case 3:
				if(!page3_is_Null()){//если ни один обьект четвертой страницы пейджера, не ссылается на null
					if(!LocationData.locationIsNull()){//если уже инициализировались переменные
					    //восход/заход
                        String sunrise_sunset[] = MoonSunMetods.getSunriseSunset(LocationData.latitudeGPS,LocationData.longitudeGPS,LocationData.altitudeGPS);//вернулся четырехэлементный массив 
						textView_Sunrise_Top.setText(sunrise_sunset[0]);//восход солнца верхний край
						textView_Sunrise_Bottom.setText(sunrise_sunset[1]);//восход солнца нижний край
						textView_Sunset_Bottom.setText(sunrise_sunset[2]);//заход солнца нижний край
						textView_Sunset_Top.setText(sunrise_sunset[3]);//заход солнца верхний край
						//гражданские сумерки
						String twilight_civil[] = MoonSunMetods.getTwilightCivil(LocationData.latitudeGPS,LocationData.longitudeGPS,LocationData.altitudeGPS);//вернулся двуэлементный массив 
						textViewTWILIGHTCIVILsunrise.setText(twilight_civil[0]);//утренние гражданские сумерки
						textViewTWILIGHTCIVILsunset.setText(twilight_civil[1]);//вечерние гражданские сумерки
						//морские сумерки
						String twilightNautical[] = MoonSunMetods.getTwilightNautical(LocationData.latitudeGPS,LocationData.longitudeGPS,LocationData.altitudeGPS);//вернулся двуэлементный массив 
						textViewTWILIGHTNAUTICALsunrise.setText(twilightNautical[0]);//утренние морские сумерки
						textViewTWILIGHTNAUTICALsunset.setText(twilightNautical[1]);//вечерние морские сумерки
						//астрономические сумерки
						String twilightAstronomical[] = MoonSunMetods.getTwilightAstronomical(LocationData.latitudeGPS,LocationData.longitudeGPS,LocationData.altitudeGPS);//вернулся двуэлементный массив 
						textViewTWILIGHTASTRONOMICALsunrise.setText(twilightAstronomical[0]);//утренние астрономические сумерки
						textViewTWILIGHTASTRONOMICALsunset.setText(twilightAstronomical[1]);//вечерние астрономические сумерки
						//золотой час
						String twilighGoldenHour[] = MoonSunMetods.getTwilighGoldenHour(LocationData.latitudeGPS,LocationData.longitudeGPS,LocationData.altitudeGPS);//вернулся двуэлементный массив 
						textViewTWILIGHTGOLDENHOURsunrise.setText(twilighGoldenHour[0]);//утренний золотой час
						textViewTWILIGHTGOLDENHOURsunset.setText(twilighGoldenHour[1]);//вечерний золотой час
						//синий час
						String twilighBlueHour[] = MoonSunMetods.getTwilighBlueHour(LocationData.latitudeGPS,LocationData.longitudeGPS,LocationData.altitudeGPS);//вернулся двуэлементный массив 
						textViewTWILIGHTBLUEHOURsunrise.setText(twilighBlueHour[0]);//утренний синий час
						textViewTWILIGHTBLUEHOURsunset.setText(twilighBlueHour[1]);//вечерний синий час
						//ночной час
						String twilighNightHour=MoonSunMetods.getTwilighNightHour(LocationData.latitudeGPS,LocationData.longitudeGPS,LocationData.altitudeGPS);
						textViewTWILIGHTNIGHTHOUR.setText(twilighNightHour);
						//noon/nadir солнца
						String sunNoonNadir[] = MoonSunMetods.getSunNoonNadir(LocationData.latitudeGPS,LocationData.longitudeGPS,LocationData.altitudeGPS);//вернулся двуэлементный массив 
						textViewSunNOON.setText(sunNoonNadir[0]);
						textViewSunNADIR.setText(sunNoonNadir[1]);
						//
						double otherSunParametrs[] = MoonSunMetods.getOtherSunParametrs(LocationData.latitudeGPS,LocationData.longitudeGPS,LocationData.altitudeGPS);//вернулся четырехэлементный массив 
						textViewAltitudeSun.setText(DegreeToDegMinSek.getDegMinSek(otherSunParametrs[0]));
	                    textViewTrueAltitudeSun.setText(DegreeToDegMinSek.getDegMinSek(otherSunParametrs[1]));
	                    textViewAzimuthSun.setText(DegreeToDegMinSek.getDegMinSek(otherSunParametrs[2]));
						textViewDistanceSun.setText(String.valueOf((int)Math.round(otherSunParametrs[3]))+" км");
						//ЛУНА
						String moonriseMoonset[] = MoonSunMetods.getMoonriseMoonset(LocationData.latitudeGPS,LocationData.longitudeGPS,LocationData.altitudeGPS);//вернулся двуэлементный массив 
						textView_RiseMoon.setText(moonriseMoonset[0]);
						textView_SetMoon.setText(moonriseMoonset[1]);
						//
						double otherMoonParametrs[] = MoonSunMetods.getOtherMoonParametrs(LocationData.latitudeGPS,LocationData.longitudeGPS,LocationData.altitudeGPS);//вернулся трехэлементный массив 
						textViewAltitudeMoon.setText(DegreeToDegMinSek.getDegMinSek(otherMoonParametrs[0]));
	                    textViewAzimuthMoon.setText(DegreeToDegMinSek.getDegMinSek(otherMoonParametrs[1]));
						textViewDistanceMoon.setText(String.valueOf((int)Math.round(otherMoonParametrs[2]))+" км");
						
					}//если уже инициализировались переменные
				}//если ни один обьект четвертой страницы пейджера, не ссылается на null
			break;
			
			
			
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	public static String getSatelliteTextViewInfo(){

		int beidou=0;
		int galileo=0;
		int glonass=0;
		int gps=0;
		int irnss=0;
		int qzss=0;
		int sbas=0;
		int unknown=0;


		//переберем циклом весь массив
		for(MySatellite mySatellite:GnssStatusCallback.arraySat){//цикл 1


			switch(mySatellite.constellationType){

				case GnssStatus.CONSTELLATION_BEIDOU:
					beidou++;
					break;

				case GnssStatus.CONSTELLATION_GALILEO:
					galileo++;
					break;

				case GnssStatus.CONSTELLATION_GLONASS:
					glonass++;
					break;

				case GnssStatus.CONSTELLATION_GPS:
					gps++;
					break;

				case GnssStatus.CONSTELLATION_IRNSS:
					irnss++;
					break;

				case GnssStatus.CONSTELLATION_QZSS:
					qzss++;
					break;

				case GnssStatus.CONSTELLATION_SBAS:
					sbas++;
					break;

				case GnssStatus.CONSTELLATION_UNKNOWN:
					unknown++;
					break;

			}


		}//цикл 1


		//
		String str="";


		if(beidou!=0){
			str=str+"BEIDOU:"+beidou+"\t";
		}

		if(galileo!=0){
			str=str+"GALILEO:"+galileo+"\t";
		}

		if(glonass!=0){
			str=str+"GLONASS:"+glonass+"\t";
		}

		if(gps!=0){
			str=str+"GPS:"+gps+"\t";
		}

		if(irnss!=0){
			str=str+"IRNSS:"+irnss+"\t";
		}

		if(qzss!=0){
			str=str+"QZSS:"+qzss+"\t";
		}

		if(sbas!=0){
			str=str+"SBAS:"+sbas+"\t";
		}

		if(unknown!=0){
			str=str+"UNKNOWN:"+unknown+"\t";
		}


		return //вернем из метода это:
		
			"<font face=sans-serif-condensed color=#9E9E9E>"+//03A9F4//
			"<small>"+
		    "<small>"+
			str+
			"</small>"+
			"</small>"+
			"</font>";
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	public void onclInfo(View v){
		dialogInfoDevice();
		Vibration.vibrate(mGPSActivity);
	}
	
	
	
	Runnable myActionSky = new Runnable(){
		@Override
		public void run()
		{
			skyTextView.setTextColor(Color.parseColor("#696969"));
		}
	};
	
	
	
	int j=0;
	public void onclSky(View v){


		
		


		ArrayList <String>strArray = new ArrayList<String>();
		int mInt=0;
		int count1=0;
		
		boolean booleanBEIDOU=true;
		boolean booleanGALILEO=true;
		boolean booleanGLONASS=true;
		boolean booleanGPS=true;
		boolean booleanIRNSS=true;
		boolean booleanQZSS=true;
		boolean booleanSBAS=true;
		boolean booleanUNKNOWN=true;
		boolean booleanUseInFix=false;
		




    

		//mInt связан с количеством нажатий (нужен для блока кода внизу: if(j<mInt){j++;}else{j=0;})
		//существующие в данный момент нам нужны:
		for (MySatellite s : MyService. gnssStatusCallback. arraySat) {//цикл


			if(booleanBEIDOU&&(s.getConstellationType().equals("BEIDOU"))){
				mInt++;//инкрименируем
				booleanBEIDOU=false;//и сюда больше не попадаем, в случае нахождения ещё "BEIDOU"
				strArray.add("BEIDOU");
			}

			if(booleanGALILEO&&(s.getConstellationType().equals("GALILEO"))){
				mInt++;//инкрименируем
				booleanGALILEO=false;//и сюда больше не попадаем, в случае нахождения ещё "GALILEO"
				strArray.add("GALILEO");
			}

			if(booleanGLONASS&&(s.getConstellationType().equals("GLONASS"))){
				mInt++;//инкрименируем
				booleanGLONASS=false;//и сюда больше не попадаем, в случае нахождения ещё "GLONASS"
				strArray.add("GLONASS");
			}

			if(booleanGPS&&(s.getConstellationType().equals("GPS"))){
				mInt++;//инкрименируем
				booleanGPS=false;//и сюда больше не попадаем, в случае нахождения ещё "GPS"
				strArray.add("GPS");
			}

			if(booleanIRNSS&&(s.getConstellationType().equals("IRNSS"))){
				mInt++;//инкрименируем
				booleanIRNSS=false;//и сюда больше не попадаем, в случае нахождения ещё "IRNSS"
				strArray.add("IRNSS");
			}

			if(booleanQZSS&&(s.getConstellationType().equals("QZSS"))){
				mInt++;//инкрименируем
				booleanQZSS=false;//и сюда больше не попадаем, в случае нахождения ещё "QZSS"
				strArray.add("QZSS");
			}

			if(booleanSBAS&&(s.getConstellationType().equals("SBAS"))){
				mInt++;//инкрименируем
				booleanSBAS=false;//и сюда больше не попадаем, в случае нахождения ещё "SBAS"
				strArray.add("SBAS");
			}

			if(booleanUNKNOWN&&(s.getConstellationType().equals("UNKNOWN"))){
				mInt++;//инкрименируем
				booleanUNKNOWN=false;//и сюда больше не попадаем, в случае нахождения ещё "UNKNOWN"
				strArray.add("UNKNOWN");
			}

		}//цикл


		//закончился цикл, mInt инициализирована нужным числом
		//инкрименируем её ещё на 1 (все видимые устройством спутники)
		mInt++;
		strArray.add("ALL");//кладём несуществующий в myBooleanArray ключ 



		String[] str=new String[mInt];//распределяем память массива

		//заполняем массив
		for(String s:strArray){
			str[count1]=s;
			count1++;
		}




		//все в массив положим false
		boolean b=false;
		skyView. myBooleanArray.put("BEIDOU",b);
		skyView. myBooleanArray.put("GALILEO",b);
		skyView. myBooleanArray.put("GLONASS",b);
		skyView. myBooleanArray.put("GPS",b);
		skyView. myBooleanArray.put("IRNSS",b);
		skyView. myBooleanArray.put("QZSS",b);
		skyView. myBooleanArray.put("SBAS",b);
		skyView. myBooleanArray.put("UNKNOWN",b);
		//и единственную:
		if(skyView. myBooleanArray.containsKey(str[j])){//если содержит ключ
			skyView. myBooleanArray.put(str[j],true);//инициализируем true

			skyTextView.setTextColor(Color.parseColor("#808080"));
			skyTextView.setText("Показывать только спутники "+str[j]);
			skyTextView.postDelayed(myActionSky,70);


		}else{//если несуществющий ключ:
			b=true;
			skyView. myBooleanArray.put("BEIDOU",b);
			skyView. myBooleanArray.put("GALILEO",b);
			skyView. myBooleanArray.put("GLONASS",b);
			skyView. myBooleanArray.put("GPS",b);
			skyView. myBooleanArray.put("IRNSS",b);
			skyView. myBooleanArray.put("QZSS",b);
			skyView. myBooleanArray.put("SBAS",b);
			skyView. myBooleanArray.put("UNKNOWN",b);

			skyTextView.setTextColor(Color.parseColor("#808080"));
			skyTextView.setText("Показывать все видимые устройством спутники");
			skyTextView.postDelayed(myActionSky,70);

		}








		if(j<(mInt-1)){j++;}else{j=0;}

		//Toaster.toast( "mInt="+mInt+"\n"+
		//"j="+j);




		mInt=0;
		count1=0;
		strArray=null;
		booleanBEIDOU=true;
		booleanGALILEO=true;
		booleanGLONASS=true;
		booleanGPS=true;
		booleanIRNSS=true;
		booleanQZSS=true;
		booleanSBAS=true;
		booleanUNKNOWN=true;




        //снимаем "показ спутника" 
		//(когда мы кликали на спустник в списке)
		//SkyView.setNumber(-1);
		//SkyView.setSatellite(-1,"");

        
		skyView.invalidate();
		
		
		Vibration.vibrate(mGPSActivity);
	}
	
	
	
	
	
	
	
	static AdapterView.OnItemLongClickListener onitemLong= new AdapterView.OnItemLongClickListener(){

		@Override
		public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
		{
			dialogSat=p3;
			dialogSatellite(dialogSat);
			Vibration.vibrate(mGPSActivity);
			return false;
		}
	};
	
	
	
	
	
	
	//относится к заголовку второй стр
	//Метод возвращает строку, с подкрашенной циферкой
	public static String getUseSatelliteStr(int countUsedFix){

		//в зависимости от кол-ва используемых спутников
		//подкрасить цифру #03A9F4 или красным:

		switch (countUsedFix){
			case 0://если 0 используются для фиксации местоположения:
				return
					"<font color=#448AFF>"+//заключен между цветовыми тегами
					"Из них использовалось в расчете последней фиксации положения: "+//это текст
					"</font>"+//заключен между цветовыми тегами
					"<font color=#FF0000>"+
					countUsedFix +
					"</font>";//цвет


			default://во всех остальных случаях:
				return
					"<font color=#448AFF>"+//заключен между цветовыми тегами
					"Из них использовалось в расчете последней фиксации положения: "+//это текст
					"</font>"+//заключен между цветовыми тегами
					"<font color=#03A9F4>"+
					countUsedFix +
					"</font>";//цвет
		}
	}
	
	
	
	
	
	
	public static String waitingForPosition(){

		if(q<3){q++;}else{q=0;}

		switch (q){
			case 0:
				return "Ожидаю расчёт фиксации положения";
			case 1:
				return "Ожидаю расчёт фиксации положения.";
			case 2:
				return "Ожидаю расчёт фиксации положения..";
			case 3:
				return "Ожидаю расчёт фиксации положения...";

			default:
			    return "";
		}
	}
	
	
	//относится к заголовку второй стр
	//Метод возвращает строку, с подкрашенной циферкой
	public static String getVisibleStr(int countVisibleSatellite){

		//в зависимости от кол-ва видимых спутников
		//подкрасить цифру цветом #03A9F4 или красным цветом:

		switch (countVisibleSatellite){
			case 0://если 0 в поле зрения:
				return
					"<font color=#448AFF>"+//заключен между цветовыми тегами
					"Всего в поле зрения устройства: "+//это текст
					"</font>"+//заключен между цветовыми тегами
					"<font color=#448AFF>"+//красный (был #cc0029)
					countVisibleSatellite +
					"</font>";//цвет


			default://во всех остальных случаях:
				return
					"<font color=#448AFF>"+//заключен между цветовыми тегами
					"Всего в поле зрения устройства: "+//это текст
					"</font>"+//заключен между цветовыми тегами
					"<font color=#03A9F4>"+
					countVisibleSatellite +
					"</font>";//цвет
		}
	}
	
	
	
	//относится к заголовку второй стр
	public static String getSortText(int sort, boolean b){

		
		
		
		switch(sort){
			case 0:
				if(b){
				    return "Отсортировать по индивидуальным индентификационным номерам спутников (по убыванию)";
				}else{
					return "Отсортировать по индивидуальным индентификационным номерам спутников (по возрастанию)";
				}
			case 1:
				if(b){
				    return "Отсортировать по орбитальным группировкам (по убыванию)";
				}else{
					return "Отсортировать по орбитальным группировкам (по возрастанию)";
				}
			case 2:
				if(b){
				    return "Отсортировать по несущей частоте (по убыванию)";
				}else{
					return "Отсортировать по несущей частоте (по возрастанию)";
				}
			case 3:
				if(b){
				    return "Отсортировать: ОНШП (по убыванию)";
				}else{
					return "Отсортировать: ОНШП (по возрастанию)";
				}
			case 4:
				if(b){
				    return "Отсортировать: угол возвышения (по убыванию)";
				}else{
					return "Отсортировать: угол возвышения (по возрастанию)";
				}
			case 5:
				if(b){
				    return "Отсортировать по азимуту (по убыванию)";
                }else{
					return "Отсортировать по азимуту (по возрастанию)";
				}
			default:
			    return "";
		}
	}
	
	
	
	
	
	
	static ArrayList <MySatellite> array;
	//java.util.ConcurrentModificationException
	public static void mysort(ListView listView) {
		//сортируем полученные экзепляры класса (на помощь нам приходит реализованный в классе MySatellite компаратор):
		//сортировку производим в зависимости от указанной в satelliteHeaderLinearLayout.setOnClickListener()
		//изначально инициализирована public int sort=1;
		switch (sort){//switch (sort){



			/*case 0://использовались
				Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_USED_IN_FIX);
				break;*/

			case 0://svid

				//В зависимости от содержащейся в массиве sortBoolean переменной 
				if((boolean)sortBoolean[0]){
					//применяем компаратор для сортировки либо по убыванию
					Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_SVID_DESCENDING);//убывание
				}else{
					//либо применяем компаратор для сортировки по возрастанию
					Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_SVID_INCREASE);//возрастание
				}

				break;


			case 1://группа

				//В зависимости от содержащейся в массиве sortBoolean переменной 
				if((boolean)sortBoolean[1]){
					//применяем компаратор для сортировки либо по убыванию
					Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_CONSTELLATION_TYPE_DESCENDING);//убывание
				}else{
					//либо применяем компаратор для сортировки по возрастанию
					Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_CONSTELLATION_TYPE_INCREASE);//возрастание
				}

				break;




			case 2://несущая частота

				//В зависимости от содержащейся в массиве sortBoolean переменной 
				if((boolean)sortBoolean[2]){
					//применяем компаратор для сортировки либо по убыванию
					Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_CARRIER_FREQUENCY_DESCENDING);//убывание
				}else{
					//либо применяем компаратор для сортировки по возрастанию
					Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_CARRIER_FREQUENCY_INCREASE);//возрастание
				}

				break;




			case 3://плотность отношения несущей к шуму основной полосы частот спутника

				//В зависимости от содержащейся в массиве sortBoolean переменной 
				if((boolean)sortBoolean[3]){
					//применяем компаратор для сортировки либо по убыванию
					Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_BASEBAND_CN_DESCENDING);//убывание
				}else{
					//либо применяем компаратор для сортировки по возрастанию
					Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_BASEBAND_CN_INCREASE);//возрастание
				}

				break;



			case 4://высота

				//В зависимости от содержащейся в массиве sortBoolean переменной 
				if((boolean)sortBoolean[4]){
					//применяем компаратор для сортировки либо по убыванию
					Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_ELEVATION_DEGREES_DESCENDING);//убывание
				}else{
					//либо применяем компаратор для сортировки по возрастанию
					Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_ELEVATION_DEGREES_INCREASE);//возрастание
				}

				break;




			case 5://азимут

				//В зависимости от содержащейся в массиве sortBoolean переменной 
				if((boolean)sortBoolean[5]){
					//применяем компаратор для сортировки либо по убыванию
					Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_AZIMUTH_DEGREES_DESCENDING);//убывание
				}else{
					//либо применяем компаратор для сортировки по возрастанию
					Collections.sort(MyService. gnssStatusCallback.arraySat, MySatellite.COMPARE_AZIMUTH_DEGREES_INCREASE);//возрастание
				}

				break;




				/*

				 case 4:
				 Collections.sort(arraySat, MySatellite.COMPARE_SVID);
				 break;



				 case 3:
				 //плотность отношения несущей к шуму на антенне
				 Collections.sort(arraySat, MySatellite.COMPARE_CN0DBHZ);
				 break;


				 case 8:
				 //альманах
				 Collections.sort(arraySat, MySatellite.COMPARE_ALMANAC_DATA);
				 break;

				 case 9:
				 //эфемеридные данные
				 Collections.sort(arraySat, MySatellite.COMPARE_EPHEMERIS_DATA);
				 break;*/

		}//switch (sort)
		//сортируем полученные экзепляры класса (на помощь нам приходит реализованный в классе MySatellite компаратор):



		array=new ArrayList <MySatellite> ();
		//перепишем arraySat в array
		//переберем заполненный arrayList со спутниками
		for(MySatellite m: MyService. gnssStatusCallback.arraySat){//цикл for(MySatellite m:arraySat){






			//внешнее ветвление
			if((boolean)myBooleanArray.get(USEDINFIX)){//внешнее ветвление
				//если чекнут чекбокс "ТОЛЬКО ПОДКЛЮЧЕННЫЕ"


				//теперь в зависимости от чекнутых чекбоксов орбитальных группировок
				if((boolean)myBooleanArray.get( m.getConstellationType() )){//10

					if(m.getUsedInFix()){//0 если этот спутник используется в определении местоположения
						array.add(m);//заполняем массив для адаптера
					}//0

				}//10




			}else{//внешнее ветвление
				//иначе чекбокс "ТОЛЬКО ПОДКЛЮЧЕННЫЕ" не чекнут, мы просто заполним массив, не зависимо от того используется спутник в определении позиции или нет.
				//теперь в зависимости от чекнутых чекбоксов орбитальных группировок
				if((boolean)myBooleanArray.get( m.getConstellationType() )){//
					array.add(m);//заполняем массив для адаптера
				}//
			}//внешнее ветвление
			//внешнее ветвление





		}//закрылся цикл for(MySatellite m:arraySat){





		/*
		 sat=null;
		 sat=new String[size];
		 */
		//подготавливаем данные для адаптера
		int size=array.size();
		usedInFix=new boolean[size];
		int svid[]=new int[size];
		String constellationType[]=new String[size];
		String baseband[]=new String[size];
		basebandCn0DbHz=new int[size];
		elevation=new float[size];
		azimuth=new float[size];
		
		//satString=new String[size];


		usedInFixFlash=new boolean[size];
		//теперь, после сортировки arraylist, цикл for-each:
		for(MySatellite mySat:array){//начался цикл


			svid[mysortI]=mySat.getSvid();
			constellationType[mysortI]=""+mySat.getConstellationType();
			baseband[mysortI]=""+(mySat.getCarrierFrequencyHz()/1000000);
			basebandCn0DbHz[mysortI]= (int)(Math.round( mySat.getBasebandCn0DbHz() ));
			elevation[mysortI]= mySat.getElevationDegrees();
			azimuth[mysortI]=mySat.getAzimuthDegrees();

			//отдельно инициализируем булев массив (используется в адаптере (метод getView()))
			usedInFix[mysortI]=mySat.getUsedInFix();





            /*комментированно
			//для диалогового окна
			float carrierFrequency=mySat.getCarrierFrequencyHz()/1000000;
			String type=mySat.getConstellationType();
			int svidSat=mySat.getSvid();

			satString[mysortI]=
				"Идентификационный номер спутника (Svid): "+mySat.getSvid()+"\n"+//Идентификационный номер спутника (Svid)
				CarrierFreqUtils. getSatelliteName(type,svidSat)+//Космический аппарат: 
				"Орбитальная группировка: "+  type  +" "+getCountry(mySat.getConstellationType())+"\n"+//Группа
				"Азимут спутника: "+mySat.getAzimuthDegrees()+"°"+"\n"+//Азимут спутника
				"Угол возвышения: "+mySat.getElevationDegrees()+"°"+"\n"+//Высота спутника
				"Несущая частота: "+(carrierFrequency)+" МГц"+"\n"+//Несущая частота
				CarrierFreqUtils.getCarrierFrequencyBand(type, carrierFrequency, svidSat)+//Диапазон:
				"Плотность отношения несущей к шуму основной полосы частот спутника: "+mySat.getBasebandCn0DbHz()+" ДБ-Гц"+"\n"+//Плотность отношения несущей к шуму основной полосы частот спутника
				"Плотность отношения несущей к шуму на антенне спутника: "+mySat.getCn0DbHz()+" ДБ-Гц"+"\n"+//Плотность отношения несущей к шуму на антенне спутника
				"Альманах: "+mySat.hasAlmanacData()+"\n"+//hasAlmanacData
				"Эфемеридные данные: "+mySat.hasEphemerisData()+"\n"+//hasEphemerisData
				"Использовался ли спутник в расчете самой последней фиксации положения: "+ mySat.usedInFix()+//Использовался ли спутник в расчете самой последней фиксации положения
				"\n";

            //комментированно
			*/


			//теперь нам надо существующий в данный момент массив фикса array
			//сравнить с предыдущим массивом фикса
			//переберем весь массив фикса 
			//возьмем единственный элемент из массива array (это в данный момент mySat проходящего цикла)
			//и проверим есть ли такой же элемент в старом массиве

			//у единственного в данный момент mySat, который в итерации
			//извлечем данные для сравнения:
			String constellationType1=mySat.getConstellationType();  //группа спутников
			int svid1=mySat.getSvid();//идентификационный номер в данной группе спутников
			boolean used1=mySat.getUsedInFix();//используется ли спутник в данный момент для фиксации

			//если спутник используется в данный момент для фиксации
			if(used1){//if(used1) //будем перебирать весь предыдущий массив и сравнивать


				//переберем циклом весь предыдущий массив, и сравним с этими данными
				for(MySatellite mySatMemory:arrayMemory){//for2


					//инициализируемся
					String constellationType2=mySatMemory.getConstellationType();
					int svid2=mySatMemory.getSvid();
					boolean used2=mySatMemory.getUsedInFix();




					if(
					//если данные использующегося в данный момент для фиксации спутника совпадают
					//с данными одного из предыдущих спутников
						constellationType1.equals(constellationType2)&&
						svid1==svid2&&
						used1==used2//и при этом предыдущий спутник тоже использовался для фиксации
					//значит ничего не поменялось, мы подключены к тому же спутнику
						){
						usedInFixFlash[mysortI]=false;//и инициализируемся false
						break;//покидаем цикл и перебираем на совпадение следующий
					}else{
						usedInFixFlash[mysortI]=true;//инициализируемся true (значит мы подключились к новому спутнику)
					}


				}//for2


				//то использующийся в данный момент для фиксации спутник - новый (мы подключились к новому/другому)
				//usedInFixFlash[mysortI]=true;

			}//if(used1)








			mysortI++;
		}//закончился цикл
		
		
		
//постим текст в показанный диалог
		if(textViewDialog!=null){
			textViewDialog.setText(Html.fromHtml( getSatString(dialogSat) ));
		}
//постим текст в показанный диалог
		
		
		mysortI=0;
		//подготавливаем данные для адаптера




		ArrayList arrayLis=new ArrayList();
		HashMap hashMap;
		//упаковываем данные в понятную для адаптера структуру
		for(int i=0;i<size;i++){
			//создаю отдельный экземпляр
			//HashMap
			hashMap=new HashMap();
			//кладу данные ключ/значение
			//в каждый отдельный экземпляр
			hashMap.put("Key1",svid[i]+"");//положил строку
			hashMap.put("Key2",constellationType[i]);//положил строку
			hashMap.put("Key3",getIntImageRes(constellationType[i]));//положил int img=R.drawable.folder;
			hashMap.put("Key4",baseband[i]);//положил строку


			//добавляю этот обьект HashMap в ArrayList
			arrayLis.add(hashMap);
		}
		//в результате у меня создалось много HashMap'ов,
		//в каждом из которых лежат данные по одинаковым ключам Key1, Key2, key3...
		//и эти HashMap'ы я положил в ArrayList

		//Создаю массив имен атрибутов из которых будут читаться данные
		String from []={
			"Key1",
			"Key2",
			"Key3",
			"Key4"
		};

		//Создаю массив id View компонентов в которые будут вставляться данные
		int to[]={ 
			R.id.textViewSvid,
			R.id.textViewCountry1,
			R.id.imageViewCountry,
			R.id.textViewBaseband
		};








		//сохранение точной позиции списка
		Parcelable state = listView.onSaveInstanceState();

		listView.setAdapter(null);
		// создаем адаптер
		SatelliteAdapter satelliteAdapter=new SatelliteAdapter(mGPSActivity, arrayLis, R.layout.satellite_item, from,to);
		//adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item, sat);
		// присваиваем адаптер списку
		listView.setAdapter(satelliteAdapter);
		listView.setFastScrollEnabled(true);
		listView.setFastScrollAlwaysVisible(true);

		//восстановление точной позиции списка (этот метод периодически вызывает GnssStatusCallback, а мы список пальцем катаем. Если не проделать сохранение/востановление - список будет прыгать при вызовах)
		listView.onRestoreInstanceState(state);

		//после того как данные отданы адаптеру
		//запоминаем массив для сравнения с массивом при следующем фиксе
		arrayMemory.clear();//при этом всё предыдущее содержимое удаляем
		arrayMemory.addAll(array);//вот, теперь запоминаем


	}

	
	
	
	public static String getSatString(int dialog){
		
		
		if(array!=null){
			
			//единожды, при первом вызове метода
			//выясним какой именно спутник, находится в списке, по указанному индексу
			if(satB){
			MySatellite mySatellite=array.get(dialog);
			//извлечем данные для сравнения:
			constellationType1=mySatellite.getConstellationType();//группа спутников
			svid1=mySatellite.getSvid();//идентификационный номер в данной группе спутников
			satB=false;//следующие вызовы метода, уже не будут инициализировать переменные (инициализируется в true, в диалоге)
			}
			
			//теперь розыщем его именно в массиве (потому что список меняется, во время фиксов. А диалог висит.)
			for(MySatellite mySat: MyService.gnssStatusCallback.arraySat){
				//инициализируемся
				String constellationType2=mySat.getConstellationType();
				int svid2=mySat.getSvid();
				//если данные совпадают, значит в массиве мы нашли спутник, на который кликали в списке
				if(constellationType1.equals(constellationType2)&&
				   svid1==svid2){
					//тогда, извлечем из него текущие данные
					return mySat.getSatString();
				}
			}
			//закончился цикл перебора. Если мы здесь, спутника уже нет в списке видимых устройством
		}
		
		return "Пропал из поля зрения устройства."+"\n"+
		       "(Данного спутника, уже нет в списке видимых устройством спутников)";
	}
	
	
	
	
	
	
	public static String getCountry(String ConstellationType){

		String country="";

		switch(ConstellationType){


			case "BEIDOU":
				country= "(Китай)";
				break;

			case "GALILEO":
				country= "(Европейский союз)";
				break;

			case "GLONASS":
				country= "(Россия)";
				break;

			case "GPS":
				country= "(США Navstar)";
				break;

			case "IRNSS":
				country= "(Индия)";
				break;

			case "QZSS":
				country= "(Япония)";
				break;

			case "SBAS":
				country= "";
				break;

			case "UNKNOWN":
				country= "UNKNOWN";
				break;

		}

		return country;
	}


	public static int getIntImageRes(String constellationType){

		int image=R.drawable.unknown_48;
		switch(constellationType){


			case "BEIDOU":
				image=R.drawable.china_flag_48;// "(Китай COMPASS)";
				break;

			case "GALILEO":
				image=R.drawable.european_union_flag_48;//"(Европейский союз)";
				break;

			case "GLONASS":
				image=R.drawable.russia_flag_48;//"(Россия)";
				break;

			case "GPS":
				image=R.drawable.united_states_flag_48;//"(США Navstar)";
				break;

			case "IRNSS":
				image=R.drawable.india_flag_48;// "(Индия)";
				break;

			case "QZSS":
				image=R.drawable.japan_flag_48;//"(Япония)";
				break;

			case "SBAS":
				image=R.drawable.sbas_48;// "";//найти пикчу для SBAS
				break;

			case "UNKNOWN":
				image=R.drawable.unknown_48;//"UNKNOWN";
				break;

		}

		return image;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	static class TextViewOncl implements OnClickListener
	{

		ListView listView;

		public TextViewOncl(ListView listView){
			this.listView=listView;
		}


		@Override
		public void onClick(View p1)
		{

		   /*0  svid
			 1  по орбитальным группировкам
			 2  по несущей частоте
			 3  ОНШП
			 4  угол возвышения
			 5  по азимуту */
			
			

			switch(p1.getId()){

				
				case R.id.textView_Header_Svid://орбитальная группировка

				    sort=0;
					if(sortBoolean[0]){sortBoolean[0]=false;}else{sortBoolean[0]=true;}
					satelliteSortTextView.setText(getSortText(sort,sortBoolean[0]));
					editor.putBoolean(SORT_BOOLEAN[0],(boolean)sortBoolean[0]);//и сразу сохраним в Б.Д.
					editor.apply();
					break;

				case R.id.textView_Header_ConstellationType://орбитальная группировка

				    sort=1;
					if(sortBoolean[1]){sortBoolean[1]=false;}else{sortBoolean[1]=true;}
					satelliteSortTextView.setText(getSortText(sort,sortBoolean[1]));
					editor.putBoolean(SORT_BOOLEAN[1],(boolean)sortBoolean[1]);//и сразу сохраним в Б.Д.
					editor.apply();
					break;

				case R.id.textView_Header_CarrierFrequencyHz://несущая частота

					sort=2;
					if(sortBoolean[2]){sortBoolean[2]=false;}else{sortBoolean[2]=true;}
					satelliteSortTextView.setText(getSortText(sort,sortBoolean[2]));
					editor.putBoolean(SORT_BOOLEAN[2],(boolean)sortBoolean[2]);//и сразу сохраним в Б.Д.
					editor.apply();
					break;

				case R.id.textView_Header_DbHz://ОНПШ

					sort=3;
					if(sortBoolean[3]){sortBoolean[3]=false;}else{sortBoolean[3]=true;}
					satelliteSortTextView.setText(getSortText(sort,sortBoolean[3]));
					editor.putBoolean(SORT_BOOLEAN[3],(boolean)sortBoolean[3]);//и сразу сохраним в Б.Д.
					editor.apply();
					break;

				case R.id.textView_Header_ElevationDegrees://угол возвышения

					sort=4;
					if(sortBoolean[4]){sortBoolean[4]=false;}else{sortBoolean[4]=true;}
					satelliteSortTextView.setText(getSortText(sort,sortBoolean[4]));
					editor.putBoolean(SORT_BOOLEAN[4],(boolean)sortBoolean[4]);//и сразу сохраним в Б.Д.
					editor.apply();
					break;

				case R.id.textView_Header_AzimuthDegrees://азимут

					sort=5;
					if(sortBoolean[5]){sortBoolean[5]=false;}else{sortBoolean[5]=true;}
					satelliteSortTextView.setText(getSortText(sort,sortBoolean[5]));
					editor.putBoolean(SORT_BOOLEAN[5],(boolean)sortBoolean[5]);//и сразу сохраним в Б.Д.
					editor.apply();
					break;

			}

			
			

			satelliteSortTextView.setTextColor(Color.parseColor("#bdbdbd"));
			satelliteSortTextView.postDelayed(myAction,100);
			editor.putInt(SORT,sort);//сразу положим в Б.Д.
			editor.apply();
			
			
			
			if(MyService. gnssStatusCallback. count!=0){//и, если есть хотя бы 1 спутник в поле зрения
				mysort(listView);//вызываем метод сортировки
			}
			listView.smoothScrollToPosition(0);
			//просим адаптер списка обновить данные
			//notifyDataSetChanged();
			Vibration.vibrate(mGPSActivity);

		}
	}
	
	static final Runnable myAction = new Runnable(){
		@Override
		public void run()
		{
			satelliteSortTextView.setTextColor(Color.parseColor("#696969"));
		}
	};
	
	/*static final Runnable myAction2 = new Runnable(){
		@Override
		public void run()
		{
			satelliteSortTextView.setTextColor(Color.parseColor("#0000ff"));
		}
	};*/
	
	
	
	public static void dialogInfoDevice(){

		AlertDialog.Builder builder = new AlertDialog.Builder(mGPSActivity,R.style.MyDialogTheme);
		View myView= LayoutInflater.from(mGPSActivity).inflate(R.layout.dialog_info_device,null);
		builder.setView(myView);
		final AlertDialog alertDialog=  builder.create();
		alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background_inset);
		
		final TextView textViewInfo=myView.findViewById(R.id.dialoginfodeviceTextView1);
		String strInfo="";
		LocationManager locationManager= (LocationManager) mGPSActivity.getSystemService(Context.LOCATION_SERVICE);
		
		
		//>>>
		strInfo=
			"<font color=#BDBDBD>"+//заключен между цветовыми тегами
			"<b>"+//Задает полужирное начертание
			"На устройстве установлено:"+//это текст
			"<b>"+//Задает полужирное начертание
			"</font>"+//заключен между цветовыми тегами
			"<br><br>";
		
		//Возвращает название модели (включая производителя и версию аппаратного/программного обеспечения) аппаратного драйвера GNSS или null, если эта информация недоступна
		String hardwareModelName= locationManager.getGnssHardwareModelName();
		int year= locationManager.getGnssYearOfHardware();
		String yearOfHardware= year==0?"предшествует 2016 году":year+" г.";
		
		
		//собираем далее строку с информацией
		
		
		//название модели (включая производителя и версию аппаратного/программного обеспечения) аппаратного драйвера GNSS или null, если эта информация недоступна
		if(hardwareModelName!=null){
			strInfo=strInfo+
			"Название модели аппаратного драйвера GNSS (включает производителя и версию аппаратного/программного обеспечения):"+"<br>"+
			"<font color=#448AFF>"+//заключен между цветовыми тегами
			hardwareModelName+//это текст
			"</font>"+//заключен между цветовыми тегами
			"<br>";//перенос строки
		}
		
		
		strInfo=strInfo+
		"Год выпуска аппаратного и программного обеспечения GNSS: "+
		"<font color=#448AFF>"+//заключен между цветовыми тегами
		yearOfHardware+//это текст
		"</font>"+//заключен между цветовыми тегами
		"<br>";
		//>>>
		
		final String textInfoFinal=strInfo;
		
	
		textViewInfo.setText(Html.fromHtml(strInfo));
		//textViewInfo.setText(strInfo);
		satelliteDialog=true;

		textViewInfo.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					ClipboardManager clipboard = (ClipboardManager) mGPSActivity.getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData clip = ClipData.newPlainText("", textViewInfo.getText().toString());
					clipboard.setPrimaryClip(clip);
					if(satelliteDialog){
						Toast.makeText(mGPSActivity, "Скопировано в буфер обмена", Toast.LENGTH_SHORT).show();
						satelliteDialog=false;
					}
					//textViewInfo.setTextColor(Color.parseColor("#22bcff"));
					String text=
					"<font color=#bdbdbd>"+
					textInfoFinal+
					"</font>"
					;
					
					textViewInfo.setText(Html.fromHtml(text));
					textViewInfo.postDelayed(new Runnable(){
							@Override
							public void run()
							{
								//textViewInfo.setTextColor(Color.parseColor("#FE5723"));
								textViewInfo.setText(Html.fromHtml(textInfoFinal));
							}

						},100);
					Vibration.vibrate(mGPSActivity);
				}


			});
		
		
		
		alertDialog.show();
	}
	
	
	
	
	
	static TextView textViewDialog;
	public static void dialogSatellite(int dialogInt){

		

		AlertDialog.Builder builder = new AlertDialog.Builder(mGPSActivity,R.style.MyDialogTheme);
		View myView= LayoutInflater.from(mGPSActivity).inflate(R.layout.dialog_satellite,null);
		builder.setView(myView);
		TextView textView=new TextView(mGPSActivity);
		textView.setTextColor(Color.parseColor("#BDBDBD"));
		textView.setShadowLayer(3,3,3,Color.parseColor("#101010"));
		textView.setText("Подробно:");
		textView.setPadding(10,10,10,10);
		textView.setTypeface(null,Typeface.BOLD);
		textView.setTextSize(18);
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(lp);
		textView.setGravity(Gravity.CENTER);
		builder.setCustomTitle(textView);
		final AlertDialog alertDialog=  builder.create();
		alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background_inset);

		//найдем TextView
		textViewDialog=myView.findViewById(R.id.dialogSatellite);
		satB=true;
		
		
		
		
		textViewDialog.setText(Html.fromHtml( getSatString(dialogSat) ));
		satelliteDialog=true;
		
		textViewDialog.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					ClipboardManager clipboard = (ClipboardManager) mGPSActivity.getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData clip = ClipData.newPlainText("", textViewDialog.getText().toString());
					clipboard.setPrimaryClip(clip);
					if(satelliteDialog){
						Toast.makeText(mGPSActivity, "Скопировано в буфер обмена", Toast.LENGTH_SHORT).show();
						satelliteDialog=false;
					}
					
					String text=
						"<font color=#dadada>"+
						getSatString(dialogSat)+
						"</font>"
						;
						
					textViewDialog.setText(Html.fromHtml( text ));
					textViewDialog.postDelayed(new Runnable(){
							@Override
							public void run()
							{
								textViewDialog.setText(Html.fromHtml( getSatString(dialogSat) ));
							}

						},100);
					Vibration.vibrate(mGPSActivity);
				}


			});

		alertDialog.show();
	}
	
	
	
	
	public static void dialog(ListView listView){

		CheckBox chbGPS;
		CheckBox chbGLONASS;
		CheckBox chbGALILEO;
		CheckBox chbBEIDOU;
		CheckBox chbQZSS;
		CheckBox chbIRNSS;
		CheckBox chbSBAS;
		CheckBox chbUNKNOWN;
		CheckBox chbUsedInFix;

		AlertDialog.Builder builder = new AlertDialog.Builder(mGPSActivity,R.style.MyDialogTheme);
		View myView= LayoutInflater.from(mGPSActivity).inflate(R.layout.dialog_chb_gps,null);
		builder.setView(myView);
		TextView textView=new TextView(mGPSActivity);
		textView.setTextColor(Color.parseColor("#BDBDBD"));
		textView.setShadowLayer(3,3,3,Color.parseColor("#101010"));
		textView.setText("Показать в списке только:");
		textView.setPadding(10,10,10,10);
		textView.setTypeface(null,Typeface.BOLD);
		textView.setTextSize(18);
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(lp);
		textView.setGravity(Gravity.CENTER);
		builder.setCustomTitle(textView);
		final AlertDialog alertDialog=  builder.create();
		alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rectangle_rounded_all);

		//найдем чекбоксы, отметим чекнуты они или нет, и назначим им слушателя
		chbGPS=myView.findViewById(R.id.checkBoxGPS);
		chbGLONASS=myView.findViewById(R.id.checkBoxGLONASS);
		chbGALILEO=myView.findViewById(R.id.checkBoxGALILEO);
		chbBEIDOU=myView.findViewById(R.id.checkBoxBEIDOU);
		chbQZSS=myView.findViewById(R.id.checkBoxQZSS);
		chbIRNSS=myView.findViewById(R.id.checkBoxIRNSS);
		chbSBAS=myView.findViewById(R.id.checkBoxSBAS);
		chbUNKNOWN=myView.findViewById(R.id.checkBoxUNKNOWN);
		chbUsedInFix=myView.findViewById(R.id.checkBoxUseInFix);


		chbGPS.setChecked((boolean)myBooleanArray.get("GPS"));
		chbGLONASS.setChecked((boolean)myBooleanArray.get("GLONASS"));
		chbGALILEO.setChecked((boolean)myBooleanArray.get("GALILEO"));
		chbBEIDOU.setChecked((boolean)myBooleanArray.get("BEIDOU"));
		chbQZSS.setChecked((boolean)myBooleanArray.get("QZSS"));
		chbIRNSS.setChecked((boolean)myBooleanArray.get("IRNSS"));
		chbSBAS.setChecked((boolean)myBooleanArray.get("SBAS"));
		chbUNKNOWN.setChecked((boolean)myBooleanArray.get("UNKNOWN"));
		chbUsedInFix.setChecked((boolean)myBooleanArray.get("USEDINFIX"));


		OnchDialog onchDialog=new OnchDialog(listView);
		chbGPS.setOnCheckedChangeListener(onchDialog);
		chbGLONASS.setOnCheckedChangeListener(onchDialog);
		chbGALILEO.setOnCheckedChangeListener(onchDialog);
		chbBEIDOU.setOnCheckedChangeListener(onchDialog);
		chbQZSS.setOnCheckedChangeListener(onchDialog);
		chbSBAS.setOnCheckedChangeListener(onchDialog);
		chbUNKNOWN.setOnCheckedChangeListener(onchDialog);
		chbIRNSS.setOnCheckedChangeListener(onchDialog);
		chbUsedInFix.setOnCheckedChangeListener(onchDialog);



		alertDialog.show();
	}
	
	
	//слушатель чекбоксов диалогового окна второй стр
	static class OnchDialog implements OnCheckedChangeListener
	{
		ListView listView;

		public OnchDialog(ListView listView){
			this.listView=listView;
		}

		@Override
		public void onCheckedChanged(CompoundButton p1, boolean p2)
		{
			switch(p1.getId()){//switch(p1.getId()){

				case R.id.checkBoxGPS:
					myBooleanArray.put(GPS,p2);//переписываем значение по ключу
					editor.putBoolean(GPS,(boolean)myBooleanArray.get(GPS));//и сразу сохраним в Б.Д.
					editor.apply();
				    break;

				case R.id.checkBoxGLONASS:
					myBooleanArray.put(GLONASS,p2);
					editor.putBoolean(GLONASS,(boolean)myBooleanArray.get(GLONASS));
					editor.apply();
					break;

				case R.id.checkBoxGALILEO:
					myBooleanArray.put(GALILEO,p2);
					editor.putBoolean(GALILEO,(boolean)myBooleanArray.get(GALILEO));
					editor.apply();
					break;

				case R.id.checkBoxBEIDOU:
					myBooleanArray.put(BEIDOU,p2);
					editor.putBoolean(BEIDOU,(boolean)myBooleanArray.get(BEIDOU));
					editor.apply();
					break;

				case R.id.checkBoxQZSS:
					myBooleanArray.put(QZSS,p2);
					editor.putBoolean(QZSS,(boolean)myBooleanArray.get(QZSS));
					editor.apply();
					break;

				case R.id.checkBoxSBAS:
					myBooleanArray.put(SBAS,p2);
					editor.putBoolean(SBAS,(boolean)myBooleanArray.get(SBAS));
					editor.apply();
					break;

				case R.id.checkBoxIRNSS:
					myBooleanArray.put(IRNSS,p2);
					editor.putBoolean(IRNSS,(boolean)myBooleanArray.get(IRNSS));
					editor.apply();
					break;

				case R.id.checkBoxUNKNOWN:
					myBooleanArray.put(UNKNOWN,p2);
					editor.putBoolean(UNKNOWN,(boolean)myBooleanArray.get(UNKNOWN));
					editor.apply();
					break;

				case R.id.checkBoxUseInFix:
					myBooleanArray.put(USEDINFIX,p2);
					editor.putBoolean(USEDINFIX,(boolean)myBooleanArray.get(USEDINFIX));
					editor.apply();
					break;

			}//switch(p1.getId()){


			if(MyService. gnssStatusCallback.count!=0){//если есть хотя бы 1 спутник в поле зрения
				mysort(listView);//вызываем метод сортировки
			}

			listView.smoothScrollToPosition(0);

			Vibration.vibrate(mGPSActivity);
		}



	}
	
	
	
	//вписывает текста, в TextView
	//(информацию о местоположении)
	public static void setData(){
		//GPS
		textViewGPS[0].setText(GPSActivity.locationData.getLatitudeGPS());//широта
		textViewGPS[1].setText(GPSActivity.locationData.getLongitudeGPS());//долгота
		textViewGPS[2].setText(GPSActivity.locationData.getAltitudeGPS());//высота
		textViewGPS[3].setText(GPSActivity.locationData.getSpeedGPS());//скорость
		textViewGPS[4].setText(GPSActivity.locationData.getAccuracyHorizontalGPS());//точности по горизонтали
		textViewGPS[5].setText(GPSActivity.locationData.getAccuracyVerticalGPS());//Точность по вертикали
		textViewGPS[6].setText(GPSActivity.locationData.getAccuracySpeedGPS());//Точность скорости
		textViewGPS[7].setText(GPSActivity.locationData.getTimeGPS());//Время

		//NETWORK
		textViewNetWork[0].setText(GPSActivity.locationData.getLatitudeNETWORK());//широта
		textViewNetWork[1].setText(GPSActivity.locationData.getLongitudeNETWORK());//долгота
		textViewNetWork[2].setText(GPSActivity.locationData.getAltitudeNETWORK());//высота
		textViewNetWork[3].setText(GPSActivity.locationData.getSpeedNETWORK());//скорость
		textViewNetWork[4].setText(GPSActivity.locationData.getAccuracyHorizontalNETWORK());//точности по горизонтали
		textViewNetWork[5].setText(GPSActivity.locationData.getAccuracyVerticalNETWORK());//Точность по вертикали
		textViewNetWork[6].setText(GPSActivity.locationData.getAccuracySpeedNETWORK());//Точность скорости
		textViewNetWork[7].setText(GPSActivity.locationData.getTimeNETWORK());//Время
	}
	
	
	
	
	

	
	
	
	//при каждой фиксации, начинаем отситывать сколько времени прошло, с момента этой фиксации
	public static synchronized void startChronometer(){
        if(chronometerGPS!=null & chronometerNETWORK!=null){

			//назначим хронометрам прослушиватели, который будет вызываться при изменении хронометра.
			chronometerGPS.setOnChronometerTickListener(new myChronometer(chronometerGPS));
			chronometerNETWORK.setOnChronometerTickListener(new myChronometer(chronometerNETWORK));


			Long lastFixTimeGPS=MyService.getLastFixTimeGPS();
			Long lastFixTimeNETWORK=MyService.getLastFixTimeNETWORK();

			if(lastFixTimeGPS!=null){
				chronometerGPS.setVisibility(View.VISIBLE);
				chronometerGPS.stop();
				//найдем разность, чтобы отобразить прошедшее время назад
				chronometerGPS.setBase(SystemClock.elapsedRealtime()-lastFixTimeGPS);//вот, так покажет с разностью времени последней фиксации (плюс будет тикать время, прибавляться)
				//chronometer.setBase(SystemClock.elapsedRealtime());//а так, показал бы от нуля. И тикало/прибавлялось бы, от нуля
				//запустим чтобы тикало:
				chronometerGPS.start();
			}else{chronometerGPS.setVisibility(View.INVISIBLE);}



			if(lastFixTimeNETWORK!=null){
				chronometerNETWORK.setVisibility(View.VISIBLE);
				chronometerNETWORK.stop();
				//найдем разность, чтобы отобразить прошедшее время назад
				chronometerNETWORK.setBase(SystemClock.elapsedRealtime()-lastFixTimeNETWORK);//вот, так покажет с разностью времени последней фиксации (плюс будет тикать время, прибавляться)
				//chronometer.setBase(SystemClock.elapsedRealtime());//а так, показал бы от нуля. И тикало/прибавлялось бы, от нуля
				//запустим чтобы тикало:
				chronometerNETWORK.start();
			}else{chronometerNETWORK.setVisibility(View.INVISIBLE);}



		}
	}
	
	
	
	public static class myChronometer implements OnChronometerTickListener
	{

		Chronometer chronometer;

		public myChronometer(Chronometer chronometer){
			this.chronometer=chronometer;
		}


		@Override
		public void onChronometerTick(final Chronometer cArg) {
			long time = SystemClock.elapsedRealtime() - cArg.getBase();

			//установим формат отображаемого времени
			int h   = (int)(time /3600000);
			int m = (int)(time - h*3600000)/60000;
			int s= (int)(time - h*3600000- m*60000)/1000 ;
			final String hh = h < 10 ? "0"+h: h+"";
			final String mm = m < 10 ? "0"+m: m+"";
			final String ss = s < 10 ? "0"+s: s+"";
			cArg.setText(hh+" ч "+mm+" мин "+ss+" сек" +" назад");



			//внутреннее ветвление
			if(time > 5*60*1000) {//если прошло больше 5 минут
				chronometer.setTextColor(Color.parseColor("#ff0000"));
			}else{
				chronometer.setTextColor(Color.parseColor("#2d84ff"));
			}		 
			//внутреннее ветвление			 


		}


	}
	
	
	
	
	
	
	
	//метод проверяет, ссылается ли хоть один обьект первой страницы пейджера, на null
	//если ссылается, - возвращает true. Иначе false.
	public static boolean page0_is_Null(){

		for(int i=0;i<textViewGPS.length;i++){
			if(textViewGPS[i]==null){return true;}
		}

		for(int i=0;i<textViewNetWork.length;i++){
			if(textViewNetWork[i]==null){return true;}
		}

		if(textViewProgress1==null){return true;}
		if(progressBar1==null){return true;}
		if(textViewCountSat==null){return true;}
		if(textViewSatelliteUsed==null){return true;}
		if(progressBar0==null){return true;}
		if(switchCompat==null){return true;}
		if(chronometerGPS==null){return true;}
		if(chronometerNETWORK==null){return true;}
		if(signalProgress==null){return true;}
		if(barGraph==null){return true;}

	    return false;
	}
	
	
	
	
	
	//метод проверяет, ссылается ли хоть один обьект второй страницы пейджера, на null
	//если ссылается, - возвращает true. Иначе false.
	public static boolean page1_is_Null(){

		if(satelliteHeaderLinearLayout==null){return true;}//layout заголовка
		if(satellitelistTextViewVisible==null){return true;}//первый TextView заголовка (в котором сначало текст Working, а затем: "всего в поле зрения")
		//if(satellitelistTextViewInfo==null){return true;}//второй TextView заголовка 
		if(satellitelistTextViewUse==null){return true;}//третий TextView заголовка (в котором сначало текст Working, а затем: "ожидаю расчет фиксации положения")
		if(progressBar2==null){return true;}//ProgressBar в заголовке, под этими Textview
		if(satelliteSortTextView==null){return true;}//TextView в заголовке, с текстом "Отсортировано: используются в определении"
		if(textView_Header_Svid==null){return true;}
		if(textView_Header_ConstellationType==null){return true;}
		if(textView_Header_CarrierFrequencyHz==null){return true;}
		if(textView_Header_DbHz==null){return true;}
		if(textView_Header_ElevationDegrees==null){return true;}
		if(textView_Header_AzimuthDegrees==null){return true;}
		if(satellitelistListView1==null){return true;}

		return false;
	}
	
	
	//метод проверяет, ссылается ли хоть один обьект третьей страницы пейджера, на null
	//если ссылается, - возвращает true. Иначе false.
	public static boolean page2_is_Null(){

		if(skyView==null){return true;}//
		if(skyTextView==null){return true;}//
		if(barGraphBasebandCn0DbHz==null){return true;}//
		if(barGraphCn0DbHz==null){return true;}//
		if(textViewCountSatSky==null){return true;}//
		if(textViewSatelliteUsedSky==null){return true;}//
		if(progressBar3==null){return true;}//
		
		return false;
	}
	
	
	
	
	//метод проверяет, ссылается ли хоть один обьект четвертой страницы пейджера, на null
	//если ссылается, - возвращает true. Иначе false.
	public static boolean page3_is_Null(){

		if(textView_Sunrise_Top==null){return true;}//
		if(textView_Sunrise_Bottom==null){return true;}//
		if(textView_Sunset_Top==null){return true;}//
		if(textView_Sunset_Bottom==null){return true;}//
		if(textViewTWILIGHTCIVILsunrise==null){return true;}//
		if(textViewTWILIGHTCIVILsunset==null){return true;}//
		if(textViewTWILIGHTNAUTICALsunrise==null){return true;}//
		if(textViewTWILIGHTNAUTICALsunset==null){return true;}//
		if(textViewTWILIGHTASTRONOMICALsunrise==null){return true;}//
		if(textViewTWILIGHTASTRONOMICALsunset==null){return true;}//
		if(textViewTWILIGHTGOLDENHOURsunrise==null){return true;}//
		if(textViewTWILIGHTGOLDENHOURsunset==null){return true;}//
		if(textViewTWILIGHTBLUEHOURsunrise==null){return true;}//
		if(textViewTWILIGHTBLUEHOURsunset==null){return true;}//
		if(textViewTWILIGHTNIGHTHOUR==null){return true;}//
		if(textViewSunNOON==null){return true;}//
		if(textViewSunNADIR==null){return true;}//
		if(textViewAltitudeSun==null){return true;}//
		if(textViewTrueAltitudeSun==null){return true;}//
		if(textViewAzimuthSun==null){return true;}//
		if(textViewDistanceSun==null){return true;}//
		if(textView_RiseMoon==null){return true;}//
		if(textView_SetMoon==null){return true;}//
		if(textViewAltitudeMoon==null){return true;}//
		if(textViewAzimuthMoon==null){return true;}//
		if(textViewDistanceMoon==null){return true;}//
		
		return false;
	}
	
	
	
	
	
}

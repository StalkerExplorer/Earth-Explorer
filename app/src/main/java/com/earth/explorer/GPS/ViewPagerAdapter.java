package com.earth.explorer.GPS;


import android.*;
import android.content.*;
import android.graphics.*;
import android.location.*;
import android.provider.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.CompoundButton.*;
import androidx.appcompat.widget.*;
import androidx.core.app.*;
import androidx.recyclerview.widget.*;
import com.earth.explorer.*;
import com.earth.explorer.GPS.Service.*;
import com.earth.explorer.Util.*;
import java.util.*;

import android.view.View.OnClickListener;
import com.earth.explorer.R;
import com.earth.explorer.GPS.View.*;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> implements SharedConstants
{

	int page=-1;
	public static int i=0;
	static int colorUsed1;
	static int colorUsed2;
	static int colorNoUsed1;
	static int colorNoUsed2;
	static int colorNoUsed3;
	static int colorNoUsed4;
	
	
	public ViewPagerAdapter(){
		colorUsed1=Color.parseColor("#2d84ff");
		colorUsed2=Color.parseColor("#90CAF9");
		colorNoUsed1=Color.parseColor("#FE5723");
		colorNoUsed2=Color.parseColor("#616161");
		colorNoUsed3=Color.parseColor("#90CAF9");
		colorNoUsed4=Color.parseColor("#696969");//#82B1FF (оттенков синего)
	}


	//Вызывается, когда RecyclerView требуется новый RecyclerView.ViewHolder заданного типа для представления элемента.
	@Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(layoutGPS[viewType], parent, false);
		return new ViewHolder(view);
    }



	//Возвращает тип представления элемента в позиции для повторного использования представления.
	@Override
    public int getItemViewType(int position) {
		page=position;
		return position;
    }





	//Вызывается RecyclerView для отображения данных в указанной позиции.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
		
		//Toaster.toast(GPSActivity.mGPSActivity,"onBindViewHolder()");
		
		switch(page){
			case 0://первая страница
			
				Switch switchCompat=holder.switchCompat;
				TextView textViewCountSat=holder.textViewCountSat;
				TextView textViewSatelliteUsed=holder.textViewSatelliteUsed;
				ProgressBar progressBar0=holder.progressBar0;//ProgressBar в заголовке, под этими Textview
				TextView textViewProgress1=holder.textViewProgress1;//TextView над progressBar1
				ProgressBar progressBar1=holder.progressBar1;//ProgressBar сверху, над SignalProgress
	
				//назначаем слушатели
				switchCompat.setOnCheckedChangeListener(onch);
				
				
				if(GPSActivity. providerIsAvailable()){//если доступен провайдер
					switchCompat.setChecked(MyService.serviceBoolean);//это кроме прочего, еще и запустит, или не запустит сервис (смотреть onActivityResult())
					GPSActivity. viewPager.setUserInputEnabled(MyService.serviceBoolean);//скроллирование стр пейджера, возможно или невозможно, в зависимости от того, работает ли сейчас сервис. Если он работает, тумблер будет в положение вкл
				}else{//если провайдер недоступен, или стал недоступен, (если мы уходили на паузу, потом сработает onResume(), который вызовет notifyDataSetChanged() адаптера, это и приводит к вызову onBindViewHolder(), в котором мы сейчас. Суть здесь >> уходя на паузу, мы могли отключить локацию в настройках системы, тогда возвращаясь с паузы, обновится положение тумблера
					switchCompat.setChecked(false);//тумблер в положение выкл
					GPSActivity. viewPager.setUserInputEnabled(false);//блокируем, скроллирование страниц viewPager2
				}

				switchCompat.jumpDrawablesToCurrentState();//чтобы пропустить анимацию
				
				
				
				
				//проверим включена ли локация
				switchCompat.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View p1)
						{
							
							if(GPSActivity. hasPermission()){//если уже есть разрешения на доступ к местоположению
								//проверим включена ли локация
								if(!GPSActivity. providerIsAvailable()){//если локация выключена
									openLocationSetting();//Просим пользователя включить GPS
								}//если локация выключена
							}//если уже есть разрешения на доступ к местоположению
							
						}
				});
				
				
				
				
				
				setVisible(switchCompat,
				           textViewCountSat,
						   textViewSatelliteUsed,
						   progressBar0,
						   textViewProgress1,
						   progressBar1);
				
				
				setVisiblePagerIndicator(MyService.serviceBoolean);
				//переменная нужна, чтобы не срабатывала вибрация, при первом переходе на активность
				//Перешли на активность, первый раз прочиталась страница, инициализировали:
				GPSActivity.GPSActivityBoolean=true;
				//в false обращается в onDestroy(), класса GPSActivity
			break;//первая страница
			
			case 1://вторая страница
			    TextView satellitelistTextViewUse=holder.satellitelistTextViewUse;
				satellitelistTextViewUse(satellitelistTextViewUse);
			break;//вторая страница
			
			case 2:
				TextView textViewCountSatSky=holder.textViewCountSatSky;
				TextView textViewSatelliteUsedSky=holder.textViewSatelliteUsedSky;
				ProgressBar progressBar3=holder.progressBar3;
				
				
				
				
				setVisible2(textViewCountSatSky,
							textViewSatelliteUsedSky,
							progressBar3);
							
				
			break;
		}
		
	}





	//Возвращает общее количество элементов в наборе данных, удерживаемом адаптером.
    @Override
    public int getItemCount() {
        return layoutGPS.length;
    }

	
	
	
	//делаем видимым или невидимым индикатор пейджера
	public static void setVisiblePagerIndicator(boolean visible){
		Indicator indicator=GPSActivity.mGPSActivity.findViewById(R.id.pagerIndicatorView1);
		//if(indicator!=null){//если индикатор не ссылается на null
		       if(visible){
				   indicator.setVisibility(View.VISIBLE);
				   indicator.setEnabled(true);
		       }
		
			   if(!visible){
				   indicator.setVisibility(View.INVISIBLE);
				   indicator.setEnabled(false);
			   }
		//}//если индикатор не ссылается на null
	}
	
	
	
	//android:background="#1f2126"
	public static synchronized void setVisible2(TextView textViewCountSatSky, TextView textViewSatelliteUsedSky, ProgressBar progressBar3){
		
		int width=LinearLayout.LayoutParams.MATCH_PARENT;
		int height=10;
		
		//если количество спутников использующихся для фиксации, уже не равно нулю
		if(MyService.gnssStatusCallback.getCountUsedFix()!=0){
			progressBar3.setVisibility(View.INVISIBLE);//делаем невидимым progressBar
			//установим progressBar LayoutParams, чтобы он был нулевой высоты:
			LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(width,0);
			progressBar3.setLayoutParams(lp);
			textViewCountSatSky.setVisibility(View.VISIBLE);
			textViewSatelliteUsedSky.setVisibility(View.VISIBLE);

			String countUsed= "Спутники "+ MyService.gnssStatusCallback.getCountUsedFix()+"/"+ MyService.gnssStatusCallback.count;//текст
			//возвращаем gravity-параметр
			textViewCountSatSky.setGravity(Gravity.CENTER);
			//возращаем на место отступ:
			textViewCountSatSky.setPadding(3,3,3,3);
			textViewCountSatSky.setTextColor(colorUsed1);//цвет текста:
			textViewCountSatSky.setTextSize(14);
			textViewCountSatSky.setText(countUsed);

			textViewSatelliteUsedSky.setTextColor(colorUsed2);
			textViewSatelliteUsedSky.setText(getUsedSat());
		}


		//если количество спутников использующихся для фиксации, равно нулю
		if(MyService.gnssStatusCallback.getCountUsedFix()==0){
			progressBar3.setVisibility(View.VISIBLE);//
			//вернем на место progressBar LayoutParams
			LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(width,height);
			progressBar3.setLayoutParams(lp);
			textViewCountSatSky.setVisibility(View.VISIBLE);//
			textViewSatelliteUsedSky.setVisibility(View.INVISIBLE);
			
			int widthTextView=textViewCountSatSky.getWidth();//выясним, нарисовалось ли уже View
			if(widthTextView!=0){//если уже нарисовалось View (нужно при переходе на активность и страницу, чтобы текст влево не уезжал на мгноение)
				//снимаем gravity - параметр
				textViewCountSatSky.setGravity(Gravity.NO_GRAVITY);
				//затем
				//посчитаем ширину текста строки "Ожидаю расчёт фиксации положения.", и сделаем так, чтобы текст расположился по центру
				String string="Ожидаю расчёт фиксации положения";
				textViewCountSatSky.setTextSize(14);
				Paint textPaint = textViewCountSatSky.getPaint();
				float widthText = textPaint.measureText(string);
				int result=(int)((widthTextView-widthText)/2);//величина отступа
				textViewCountSatSky.setPadding(result,3,3,3);
				textViewCountSatSky.setSingleLine(true);
				textViewCountSatSky.setTextColor(colorNoUsed4);
				textViewCountSatSky.setText(waitingForPosition());
			}//если уже нарисовалось View (нужно при переходе на активность и страницу, чтобы текст влево не уезжал на мгноение)
			
			
		}
	}
	
	
	
	public static synchronized void setVisible(Switch switchCompat, TextView textViewCountSat, TextView textViewSatelliteUsed, ProgressBar progressBar0, TextView textViewProgress1, ProgressBar progressBar1){
		//если тумблер выключен, ProgressBar невидим, textViewProgress1 невидим
		if(!switchCompat.isChecked()){
			progressBar0.setVisibility(View.INVISIBLE); 
			progressBar1.setVisibility(View.INVISIBLE);
			textViewProgress1.setVisibility(View.INVISIBLE);
		}


		//progressBar1 и textViewProgress1 (видимые (пока устройство грузит данные о спутниках), и тут же невидимые, это делается в два блока. Это первый блок. А ниже if, - это второй блок)
		if(switchCompat.isChecked()){
			progressBar1.setVisibility(View.VISIBLE); 
			textViewCountSat.setVisibility(View.INVISIBLE); 
			textViewProgress1.setVisibility(View.VISIBLE);
			textViewProgress1.setText("Загрузка данных");
		}


		if(MyService.serviceBoolean){//если работает сервис,
			//и количество спутников использующихся для фиксации, уже не равно нулю
			if(MyService.gnssStatusCallback. count!=0){
				progressBar1.setVisibility(View.INVISIBLE);//делаем невидимым progressBar
				textViewProgress1.setVisibility(View.INVISIBLE);//и textView
				textViewCountSat.setVisibility(View.VISIBLE);
			}
		}



		//как только стал невидимым нижний progressBar1, делаем видимым textViewSatelliteUsed и progressBar0

		if(progressBar1.getVisibility()==View.VISIBLE){
			textViewSatelliteUsed.setVisibility(View.INVISIBLE);
			progressBar0.setVisibility(View.INVISIBLE);
		}else{
			textViewSatelliteUsed.setVisibility(View.VISIBLE);
			progressBar0.setVisibility(MyService.gnssStatusCallback.getCountUsedFix()==0? View.VISIBLE: View.INVISIBLE);//но если, количество спутников использующихся для фиксации, уже не равно нулю, progressBar0 нужно сделать невидимым
		}


		if(!switchCompat.isChecked()){progressBar0.setVisibility(View. INVISIBLE);}


		//поменяем текста и цвет 
		if(MyService.serviceBoolean){//если работает сервис
			setTextAndColor2(textViewCountSat, textViewSatelliteUsed);
		}//если работает сервис


		//поменяем текста и цвет 
		if(!switchCompat.isChecked()){//если не работает сервис (//если отключаем сервис, происходит вызов onPageSelected(), который вызовет update())
			setTextAndColor1(textViewCountSat, textViewSatelliteUsed);
		}//если не работает сервис


		//поменяем текста и цвет 
		if(!GPSActivity. providerIsAvailable()){//если провайдер стал недоступным
			setTextAndColor1(textViewCountSat, textViewSatelliteUsed);
		}//если провайдер стал недоступным


	}
	

	public static void setTextAndColor1(TextView textViewCountSat, TextView textViewSatelliteUsed){
		textViewCountSat.setText("");
		textViewSatelliteUsed.setTextSize(16);
		textViewSatelliteUsed.setTextColor(colorNoUsed2);
		textViewSatelliteUsed.setText("Последнее известное:");
	}

	
	
	public static synchronized void satellitelistTextViewUse(TextView satellitelistTextViewUse){
		//если ни один спутник не используется для фиксации местоположнния
		if(MyService.gnssStatusCallback.getCountUsedFix()==0){//текст вида: "Ожидаю расчёт фиксации положения"
			int widthTextView=satellitelistTextViewUse.getWidth();//выясним, нарисовалось ли уже View
			if(widthTextView!=0){//если уже нарисовалось View (нужно при переходе на активность и страницу, чтобы текст влево не уезжал на мгноение)
				//снимаем gravity - параметр
				satellitelistTextViewUse.setGravity(Gravity.NO_GRAVITY);
				//затем
				//посчитаем ширину текста строки "Ожидаю расчёт фиксации положения.", и сделаем так, чтобы текст расположился по центру
				String string="Ожидаю расчёт фиксации положения";
				Paint textPaint = satellitelistTextViewUse.getPaint();
				float widthText = textPaint.measureText(string);
				int result=(int)((widthTextView-widthText)/2);//величина отступа
				satellitelistTextViewUse.setPadding(result,3,3,3);
				satellitelistTextViewUse.setSingleLine(true);
				satellitelistTextViewUse.setText(waitingForPosition());
			}//если уже нарисовалось View (нужно при переходе на активность и страницу, чтобы текст влево не уезжал на мгноение)
			
		}
	}
	

	public static void setTextAndColor2(TextView textViewCountSat, TextView textViewSatelliteUsed){
		String countUsed= "Спутники "+ MyService.gnssStatusCallback.getCountUsedFix()+"/"+ MyService.gnssStatusCallback.count;//текст
		textViewCountSat.setTextColor(MyService.gnssStatusCallback.getCountUsedFix()!=0? colorUsed1: colorNoUsed1);//цвет текста:
		textViewCountSat.setText(countUsed);


		textViewSatelliteUsed.setTextSize(10);


		//если количество спутников использующихся для фиксации, уже не равно нулю
		//строка наподобие: "GPS 8, GALILEO 4, BEIDOU 5"
		if(MyService.gnssStatusCallback.getCountUsedFix()!=0){
			//возвращаем gravity-параметр
			textViewSatelliteUsed.setGravity(Gravity.CENTER);
			//возращаем на место отступ:
			textViewSatelliteUsed.setPadding(3,3,3,3);
			textViewSatelliteUsed.setTextColor(colorUsed2);
			textViewSatelliteUsed.setText(getUsedSat());//подробности, какие именно, и сколько используются для фиксации
		}


		//если количество спутников использующихся для фиксации, еще равно нулю
		//строка "Ожидаю расчёт фиксации положения..."
		if(MyService.gnssStatusCallback.getCountUsedFix()==0){
			int widthTextView=textViewSatelliteUsed.getWidth();//выясним, нарисовалось ли уже View
			if(widthTextView!=0){//если уже нарисовалось View (нужно при переходе на активность и страницу, чтобы текст влево не уезжал на мгноение)
				//снимаем gravity - параметр
				textViewSatelliteUsed.setGravity(Gravity.NO_GRAVITY);
				//затем
				//посчитаем ширину текста строки "Ожидаю расчёт фиксации положения.", и сделаем так, чтобы текст расположился по центру
				String string="Ожидаю расчёт фиксации положения";
				Paint textPaint = textViewSatelliteUsed.getPaint();
				float widthText = textPaint.measureText(string);
				int result=(int)((widthTextView-widthText)/2);//величина отступа
				textViewSatelliteUsed.setPadding(result,3,3,3);
			}//если уже нарисовалось View (нужно при переходе на активность, чтобы текст влево не уезжал на мгноение)
			//после подсчета ширины, и измерений отступов, впишем текст
			textViewSatelliteUsed.setTextColor(colorNoUsed4);
			textViewSatelliteUsed.setText(waitingForPosition());//Ожидаю расчёт фиксации положения 
		}
	}



	public static synchronized String waitingForPosition(){

		if(i<3){i++;}else{i=0;}

		switch (i){
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


	
	
	

	//возвращает строку, наподобие: "GPS 8, GALILEO 4, BEIDOU 5"
	public static synchronized String getUsedSat(){
		if(GnssStatusCallback. arraySat!=null){

			//соберем строку, наподобие: "GPS 8, GALILEO 4, BEIDOU 5"
			int beidou=0;
			int galileo=0;
			int glonass=0;
			int gps=0;
			int irnss=0;
			int qzss=0;
			int sbas=0;
			int unknown=0;
			ArrayList <String> arrayListUsedSat=new ArrayList<String>(8);
			arrayListUsedSat.add("");
			arrayListUsedSat.add("");
			arrayListUsedSat.add("");
			arrayListUsedSat.add("");
			arrayListUsedSat.add("");
			arrayListUsedSat.add("");
			arrayListUsedSat.add("");
			arrayListUsedSat.add("");




			//выясним для правильной постановки запятой, - больше ли одного разного типа спутников у нас
			int type=0;
			boolean a,b,c,d,e,f,g,h;
			a=true; b=true; c=true; d=true; e=true; f=true; g=true; h=true;
			for(MySatellite mySatellite:GnssStatusCallback.arraySat){//цикл 1

				if(mySatellite.usedInFix){//если спутник использовался для фиксации

					switch(mySatellite.constellationType){

						case GnssStatus.CONSTELLATION_BEIDOU:
							if(a){a=false;type++;}
							break;

						case GnssStatus.CONSTELLATION_GALILEO:
							if(b){b=false;type++;}
							break;

						case GnssStatus.CONSTELLATION_GLONASS:
							if(c){c=false;type++;}
							break;

						case GnssStatus.CONSTELLATION_GPS:
							if(d){d=false;type++;}
							break;

						case GnssStatus.CONSTELLATION_IRNSS:
							if(e){e=false;type++;}
							break;

						case GnssStatus.CONSTELLATION_QZSS:
							if(f){f=false;type++;}
							break;

						case GnssStatus.CONSTELLATION_SBAS:
							if(g){g=false;type++;}
							break;

						case GnssStatus.CONSTELLATION_UNKNOWN:
							if(h){h=false;type++;}
							break;

					}

				}//если спутник использовался для фиксации

			}//цикл 1
			//выясним для правильной постановки запятой, - больше ли одного разного типа спутников у нас


			//теперь инициализируем соответственно, либо "," либо ""
			//получится что если всего один тип, например GPS, то строка будет выглядить так: "GPS".
			//а если типов больше 1, например GPS и BAYDOU, то строка уже выглядит так GPS, BAYDOU,
			//так тоже дело не пойдет, мы в конце найдем последнее вхождение подстроки "," и удалим. Все получится без одной последней запятой.
			String punctuation=(type>1)?",":"";



			for(MySatellite mySatellite:GnssStatusCallback.arraySat){//цикл 2

				if(mySatellite.usedInFix){//если спутник использовался для фиксации

					switch(mySatellite.constellationType){
						case GnssStatus.CONSTELLATION_BEIDOU:
							//чтобы узнать, что больше 1 разного типа
							//мы в case одного и того же constellation типа, попадаем много раз,
							if(a){
								a=false;//блокируем попадание в блок.
								//в результате один constellation тип, нам сделает всего один инкримент  
								type++;
							}

							arrayListUsedSat.set(0, "BEIDOU"+" "+ ++beidou  +punctuation );
							break;
						case GnssStatus.CONSTELLATION_GALILEO:
							//чтобы узнать, что больше 1 разного типа
							//мы в case одного и того же constellation типа, попадаем много раз,
							if(b){
								b=false;//блокируем попадание в блок.
								//в результате один constellation тип, нам сделает всего один инкримент  
								type++;
							}

							arrayListUsedSat.set(1, "GALILEO"+" "+ ++galileo  +punctuation);
							break;
						case GnssStatus.CONSTELLATION_GLONASS:
							//чтобы узнать, что больше 1 разного типа
							//мы в case одного и того же constellation типа, попадаем много раз,
							if(c){
								c=false;//блокируем попадание в блок.
								//в результате один constellation тип, нам сделает всего один инкримент  
								type++;
							}

							arrayListUsedSat.set(2, "GLONASS"+" "+ ++glonass  +punctuation);
							break;
						case GnssStatus.CONSTELLATION_GPS:
							//чтобы узнать, что больше 1 разного типа
							//мы в case одного и того же constellation типа, попадаем много раз,
							if(d){
								d=false;//блокируем попадание в блок.
								//в результате один constellation тип, нам сделает всего один инкримент  
								type++;
							}

							arrayListUsedSat.set(3, "GPS"+" "+ ++gps  +punctuation);
							break;
						case GnssStatus.CONSTELLATION_IRNSS:
							//чтобы узнать, что больше 1 разного типа
							//мы в case одного и того же constellation типа, попадаем много раз,
							if(e){
								e=false;//блокируем попадание в блок.
								//в результате один constellation тип, нам сделает всего один инкримент  
								type++;
							}

							arrayListUsedSat.set(4, "IRNSS"+" "+ ++irnss  +punctuation);
							break;
						case GnssStatus.CONSTELLATION_QZSS:
							//чтобы узнать, что больше 1 разного типа
							//мы в case одного и того же constellation типа, попадаем много раз,
							if(f){
								f=false;//блокируем попадание в блок.
								//в результате один constellation тип, нам сделает всего один инкримент  
								type++;
							}

							arrayListUsedSat.set(5, "QZSS"+" "+ ++qzss  +punctuation);
							break;
						case GnssStatus.CONSTELLATION_SBAS:
							//чтобы узнать, что больше 1 разного типа
							//мы в case одного и того же constellation типа, попадаем много раз,
							if(g){
								g=false;//блокируем попадание в блок.
								//в результате один constellation тип, нам сделает всего один инкримент  
								type++;
							}

							arrayListUsedSat.set(6, "SBAS"+" "+ ++sbas  +punctuation);
							break;
						case GnssStatus.CONSTELLATION_UNKNOWN:
							//чтобы узнать, что больше 1 разного типа
							//мы в case одного и того же constellation типа, попадаем много раз,
							if(h){
								h=false;//блокируем попадание в блок.
								//в результате один constellation тип, нам сделает всего один инкримент  
								type++;
							}

							arrayListUsedSat.set(7, "UNKNOWN"+" "+ ++unknown  +punctuation);
							break;
					}

				}//если спутник использовался для фиксации

			}//цикл 2





			String result="";
			for(String str:arrayListUsedSat){

				result=result+str+"  ";
			}


			//здесь собралась результирующая строка
			//она либо состоит из одного типа, тогда там нет запятой
			//либо состоит из больше чем одного типа (значит строка выглядит так GPS, BAYDOU,)
			//тогда в конце запятая, её нужно удалить
			if(type>1){
				int lastIndex= result.lastIndexOf(",");
				result=result.substring(0,lastIndex);
			}


			return result;
		}//if(arraySat!=null){
		return "";
	}
	
	
	
	
	
	public void openLocationSetting(){
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		GPSActivity. mGPSActivity. startActivityForResult(intent,SETTINGS_CODE);
	}
	
	
	
	OnCheckedChangeListener onch=new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(CompoundButton p1, boolean p2)
		{
			if(GPSActivity. hasPermission()){//если есть разрешения


				
				//>>внутреннее ветвление1>>
				if(GPSActivity. providerIsAvailable()){//если доступен провайдер
					
					if(p2){
						//запускаем сервис
						MainActivity.position();
						setVisiblePagerIndicator(true);
					}else{
						//останавливаем сервис
						MainActivity.stopPosition();
						setVisiblePagerIndicator(false);
					}
				}//если доступен провайдер
				else{//иначе, провайдер недоступен
					p1.setChecked(false);//как будто не переключили тумблер
				}//>>внутреннее ветвление1>>



				if(page!=-1){GPSActivity. onPageChangeCallback.onPageSelected(page);}//чтобы прошел вызов onPageSelected();
				
				
			}else{////иначе разрешений нет
				//Toaster.toast(GPSActivity. mGPSActivity,"PERMISSION_DENIED");
				p1.setChecked(false);//как будто не переключили тумблер
				GPSActivity. permission();//Если разрешения нет, то нам надо его запросить. 
			}////если есть разрешения
			
			if(GPSActivity.GPSActivityBoolean){//чтобы при переходе на активность, не сработала вибрация
			Vibration.vibrate(GPSActivity. mGPSActivity);
			}//чтобы при переходе на активность, не сработала вибрация
			
		}
	};
	
	
	
	


	public class ViewHolder extends RecyclerView.ViewHolder {

		//первая стр
		Switch switchCompat;
		TextView textViewCountSat;
		TextView textViewSatelliteUsed;
		ProgressBar progressBar0;//ProgressBar в заголовке, под этими Textview
		TextView textViewProgress1;//TextView над progressBar1
		ProgressBar progressBar1;//ProgressBar сверху, над SignalProgress
		//первая стр
		
		//вторая стр
		TextView satellitelistTextViewUse;
		//вторая стр
		
		//третья стр
		TextView textViewCountSatSky;
		TextView textViewSatelliteUsedSky;
		ProgressBar progressBar3;
		//третья стр
		
		
		ViewHolder(View itemView)
		{
            super(itemView); 
			//switch(page){
				//case 0://первая страница
				    switchCompat=itemView. findViewById(R.id.mylocationSwitch);
					textViewCountSat=itemView. findViewById(R.id.textViewCountSat);
					textViewSatelliteUsed=itemView. findViewById(R.id.textViewSatelliteUsed);
					progressBar0=itemView. findViewById(R.id.progressBar0);
					textViewProgress1=itemView. findViewById(R.id.textViewProgress1);
					progressBar1=itemView. findViewById(R.id.progressBar1);
				//break;//первая страница
				
				//case 1://вторая страница
				    satellitelistTextViewUse=itemView.findViewById(R.id.satellitelistTextViewUse);
				//break;//вторая страница
				
				//case 2://третья страница
				    textViewCountSatSky=itemView.findViewById(R.id.textViewCountSatSky);
					textViewSatelliteUsedSky=itemView.findViewById(R.id.textViewSatelliteUsedSky);
					progressBar3=itemView.findViewById(R.id.progressBar3);
				//break;//третья страница
		//}
		}
	}


}

package com.earth.explorer.SunriseSunset;

import android.icu.text.*;
import java.util.*;
import org.shredzone.commons.suncalc.*;

public class MoonSunMetods
{
	static SimpleDateFormat detailsSimpleDateFormat=new SimpleDateFormat(
		"dd: MMMM: yyyy HH:mm:ss z", Locale.getDefault());
	
	
		
		
		
		
	
	//восход/заход Солнца
	//возвращает четырехэлементный строковый массив. 
	//первый элемент:время, когда верхний край солнца будет пересекать горизонт во время восхода солнца  (это строка в TextView выглядит так: "7 марта: 2023 06:32:00 GMT+3")
	//второй элемент:время, когда нижний край солнца будет пересекать горизонт во время восхода солнца
	//третий элемент:время, когда нижний край солнца будет пересекать горизонт во время захода солнца
	//четвертый элемент:время, когда верхний край солнца будет пересекать горизонт во время захода солнца
	public static String[] getSunriseSunset(double latitude, double longitude, double altitude){

		String topOfTheSun_Sunrise="";//верхний край солнца восход
		String bottomOfTheSun_Sunrise="";//нижний край солнца восход
		String topOfTheSun_Sunset="";//верхний край солнца заход
		String bottomOfTheSun_Sunset="";//нижний край солнца заход
		String sunrise_sunset[]=new String[4];
		SunTimes suntimes;
		SunTimes suntimesFull;
		SunTimes.Parameters param;
		SunTimes.Parameters paramFull;
		String  str;
		Date date=new Date();//по умолчанию сразу инициализируемся текущим временем
		param= SunTimes.compute();
		paramFull = SunTimes.compute();

		//ВОСХОД/ЗАХОД СОЛНЦА/ВОСХОД/ЗАХОД СОЛНЦА/ВОСХОД/ЗАХОД СОЛНЦА/ВОСХОД/ЗАХОД СОЛНЦА/ВОСХОД/ЗАХОД СОЛНЦА/ВОСХОД/ЗАХОД СОЛНЦА/
		//СЧИТАЕМ МОМЕНТ КОГДА ВИДИМЫЙ ВЕРХНИЙ КРАЙ СОЛНЦА ПЕРЕСЕЧЕТ ГОРИЗОНТ. это кстати значение по умолчанию
		//ОДИН ВЕРХНИЙ КРАЙ, ДЛЯ >>> ВОСХОДА И ЗАХОДА
		//создам отдельно параметры
		param.twilight(SunTimes.Twilight.VISUAL)//Момент, когда видимый верхний край солнца пересекает горизонт. Это значение по умолчанию.
			.on(date)       // set a date
			.at(latitude, longitude)   // set a location
			.height(altitude);//считаем всё с высотой.

		//и еще одни параметры
		paramFull
			.twilight(SunTimes.Twilight.VISUAL)//Момент, когда видимый верхний край солнца пересекает горизонт. Это значение по умолчанию.
			.fullCycle()//для полного цикла
			.on(date)       // set a date
			.at(latitude, longitude)
			.height(altitude);//считаем всё с высотой.



		//теперь выполню рассчет
		suntimes=param.execute();//создается обьект класса содержащий рассчет
		suntimesFull = paramFull.execute();//создается обьект класса содержащий рассчет
		//извлекается рассчет так:
		//suntimes.getRise();//для рассвета
		//suntimes.getSet();//для заката
		//возвращаются экземпляры класса Date
		//при этом может вернуться null


		//а мы не только извлекаем рассчет, но и сразу вписываем в строки массива, строчки с информацией:
		if(suntimes.getRise()!=null){//если солнце взойдет.
			   topOfTheSun_Sunrise=// "Восход: "+
				                    //"Верхний край Солнца пересечёт горизонт: "+
				                    detailsSimpleDateFormat.format(suntimes.getRise())+"\n";
			
		}else{//иначе оно сегодня не взойдет
			//выясню ближайшее время когда взойдет
			//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
			if(suntimesFull.getRise()!=null){
				topOfTheSun_Sunrise=// "Восход: "+
					                //">>Ближайшее>>"+
					                //"Верхний край Солнца пересечёт горизонт: "+
					                detailsSimpleDateFormat.format(suntimesFull.getRise())+"\n";
			}else{
				topOfTheSun_Sunrise="нет данных";//пишем что вернулся null
			}
		}


		if(suntimes.getSet()!=null){//если солнце зайдет
			topOfTheSun_Sunset=// "Заход: "+
				                    //"Верхний край Солнца пересечёт горизонт: "+
				                    detailsSimpleDateFormat.format(suntimes.getSet())+"\n";
		}else{//иначе солнце в этот день не зайдет
			//выясню ближайшее время когда зайдет
			//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
			if(suntimesFull.getSet()!=null){
				topOfTheSun_Sunset=// "Заход: "+
					                //">>Ближайшее>>"+
					                //"Верхний край Солнца пересечёт горизонт: "+
					                detailsSimpleDateFormat.format(suntimesFull.getSet())+"\n";
			}else{
				topOfTheSun_Sunset="нет данных";//пишем что вернулся null
			}
		}
		//ОДИН ВЕРХНИЙ КРАЙ, ДЛЯ >>> ВОСХОДА И ЗАХОДА
		//СЧИТАЕМ МОМЕНТ КОГДА ВИДИМЫЙ ВЕРХНИЙ КРАЙ СОЛНЦА ПЕРЕСЕЧЕТ ГОРИЗОНТ. это кстати значение по умолчанию

		//СЧИТАЕМ МОМЕНТ КОГДА ВИДИМЫЙ НИЖНИЙ КРАЙ СОЛНЦА ПЕРЕСЕЧЕТ ГОРИЗОНТ
		//ОДИН НИЖНИЙ КРАЙ, ДЛЯ >>> ВОСХОДА И ЗАХОДА
		//теперь меняю параметры
		param.twilight(SunTimes.Twilight.VISUAL_LOWER)//Момент, когда видимый нижний край солнца пересекает горизонт.
			.on(date)       // set a date
			.at(latitude, longitude)   // set a location
			.height(altitude);//считаем всё с высотой.

		//и еще одни параметры
		paramFull
			.twilight(SunTimes.Twilight.VISUAL_LOWER)//Момент, когда видимый нижний край солнца пересекает горизонт.
			.fullCycle()//для полного цикла
			.on(date)       // set a date
			.at(latitude, longitude)   // set a location
			.height(altitude);//считаем всё с высотой.



		//теперь выполню опять рассчет
		suntimes=param.execute();//теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет
		//и ещё один рассчет
		suntimesFull = paramFull.execute();////теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет



		//извлеку рассчет:
		//а мы не только извлекаем рассчет, но и сразу вписываем в строки массива, строчки с информацией:
		if(suntimes.getRise()!=null){//если солнце взойдет
			bottomOfTheSun_Sunrise=// "Восход: "+
				                    //"Нижний край Солнца пересечёт горизонт: "+
				                    detailsSimpleDateFormat.format(suntimes.getRise())+"\n";
		}else{//иначе солнце сегодня не взойдет
			//выясню ближайшее время когда взойдет
			//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
			if(suntimesFull.getRise()!=null){
				bottomOfTheSun_Sunrise=// "Восход: "+
					                    //">>Ближайшее>>"+
					                    //"Нижний край Солнца пересечёт горизонт: "+
					                    detailsSimpleDateFormat.format(suntimesFull.getRise())+"\n";
			}else{
				bottomOfTheSun_Sunrise="нет данных";//пишем что вернулся null
			}
		}

		if(suntimes.getSet()!=null){//если солнце зайдет
			    bottomOfTheSun_Sunset=// "Заход: "+
				                      //"Нижний край Солнца пересечёт горизонт: "+
				                      detailsSimpleDateFormat.format(suntimes.getSet())+"\n";
		}else{//иначе солнце в этот день не зайдет
			//выясню ближайшее время когда зайдет
			//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
			if(suntimesFull.getSet()!=null){
				bottomOfTheSun_Sunset=// "Заход: "+
					                  //">>Ближайшее>>"+
					                  //"Нижний край Солнца пересечёт горизонт: "+
					                  detailsSimpleDateFormat.format(suntimesFull.getSet())+"\n";
			}else{
				bottomOfTheSun_Sunset="нет данных";//пишем что вернулся null
			}
		}
		//СЧИТАЕМ МОМЕНТ КОГДА ВИДИМЫЙ НИЖНИЙ КРАЙ СОЛНЦА ПЕРЕСЕЧЕТ ГОРИЗОНТ
		//ОДИН НИЖНИЙ КРАЙ, ДЛЯ >>> ВОСХОДА И ЗАХОДА
		//ВОСХОД/ЗАХОД СОЛНЦА/ВОСХОД/ЗАХОД СОЛНЦА/ВОСХОД/ЗАХОД СОЛНЦА/ВОСХОД/ЗАХОД СОЛНЦА/ВОСХОД/ЗАХОД СОЛНЦА/ВОСХОД/ЗАХОД СОЛНЦА/




		sunrise_sunset[0]=topOfTheSun_Sunrise;//верхний край солнца восход
		sunrise_sunset[1]=bottomOfTheSun_Sunrise;//нижний край солнца восход

		sunrise_sunset[2]=bottomOfTheSun_Sunset;//нижний край солнца заход
		sunrise_sunset[3]=topOfTheSun_Sunset;//верхний край солнца заход

						  
		return sunrise_sunset;
	}
    //восход/заход Солнца

	
	
	
	
	
	//Гражданские сумерки
	//возвращает двуэлементный строковый массив. 
	//первый элемент:время, когда наступают утренние гражданские сумерки
	//второй элемент:время, когда наступают вечерние гражданские сумерки
	public static String[] getTwilightCivil(double latitude, double longitude, double altitude){

		    String twilightCivil[]=new String[2];
			SunTimes suntimes;
			SunTimes suntimesFull;
			SunTimes.Parameters param;
			SunTimes.Parameters paramFull;
			String  str;
			Date date=new Date();//по умолчанию сразу инициализируемся текущим временем
			param= SunTimes.compute();
			paramFull = SunTimes.compute();


			//ГРАЖДАНСКИЕ СУМЕРКИ//ГРАЖДАНСКИЕ СУМЕРКИ//ГРАЖДАНСКИЕ СУМЕРКИ//ГРАЖДАНСКИЕ СУМЕРКИ/
			//теперь меняю параметры
			param.twilight(SunTimes.Twilight.CIVIL)//Гражданские сумерки
				.on(date)       // set a date
				.at(latitude, longitude)   // set a location//и еще одни параметры
			    .height(altitude);//считаем всё с высотой.

			paramFull
				.twilight(SunTimes.Twilight.CIVIL)//Гражданские сумерки
				.fullCycle()//для полного цикла
				.on(date)       // set a date
				.at(latitude, longitude)
			    .height(altitude);//считаем всё с высотой.

			//теперь выполню опять рассчет
			suntimes=param.execute();//теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет
			//и ещё один рассчет
			suntimesFull = paramFull.execute();////теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет


			//СЧИТАЕМ КОГДА НАСТУПАЮТ ГРАЖДАНСКИЕ СУМЕРКИ ДЛЯ ВОСХОДА///СЧИТАЕМ КОГДА НАСТУПАЮТ ГРАЖДАНСКИЕ СУМЕРКИ ДЛЯ ВОСХОДА/
			if(suntimes.getRise()!=null){//если гражданские сумерки есть в этот день /для рассвета
				str= //"Утренние гражданские сумерки наступают: "+
					detailsSimpleDateFormat.format(suntimes.getRise());
				    twilightCivil[0]=str;//вот, инициализировал строку массива
			}else{//иначе гражданские сумерки в этот день на рассвете не наступают
				//выясню ближайшее время когда наступают для рассвета гражданские сумерки
				//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
				if(suntimesFull.getRise()!=null){
					str= //"Сегодня утренние гражданские сумерки не наступают\n"+
						 //">>Ближайшее>>"+
						 //"Утренние гражданские сумерки наступают: "+
						 detailsSimpleDateFormat.format(suntimesFull.getRise());
				         twilightCivil[0]=str;
				}else{
					str="нет данных";//пишем что вернулся null
					twilightCivil[0]=str;
				}

			}
			//СЧИТАЕМ КОГДА НАСТУПАЮТ ГРАЖДАНСКИЕ СУМЕРКИ ДЛЯ ВОСХОДА///СЧИТАЕМ КОГДА НАСТУПАЮТ ГРАЖДАНСКИЕ СУМЕРКИ ДЛЯ ВОСХОДА/


			//СЧИТАЕМ КОГДА НАСТУПАЮТ ГРАЖДАНСКИЕ СУМЕРКИ ДЛЯ ЗАХОДА///СЧИТАЕМ КОГДА НАСТУПАЮТ ГРАЖДАНСКИЕ СУМЕРКИ ДЛЯ ЗАХОДА/
			if(suntimes.getSet()!=null){//если гражданские сумерки есть в этот день /для захода
				str= //"Вечерние гражданские сумерки наступают: "+
					detailsSimpleDateFormat.format(suntimes.getSet());
			        twilightCivil[1]=str;//вот, инициализировал строку массива
			}else{//иначе гражданские сумерки в этот день для захода не наступают
				//выясню ближайшее время когда наступают для захода гражданские сумерки
				//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
				if(suntimesFull.getSet()!=null){
					str=//"Сегодня вечерние гражданские сумерки не наступают\n"+
						//">>Ближайшее>>"+
						//"Вечерние гражданские сумерки наступают: "+
						detailsSimpleDateFormat.format(suntimesFull.getSet())+"\n";
				        twilightCivil[1]=str;
				}else{
					    str="нет данных";//пишем что вернулся null
					    twilightCivil[1]=str;
				}

			}
			//СЧИТАЕМ КОГДА НАСТУПАЮТ ГРАЖДАНСКИЕ СУМЕРКИ ДЛЯ ЗАХОДА///СЧИТАЕМ КОГДА НАСТУПАЮТ ГРАЖДАНСКИЕ СУМЕРКИ ДЛЯ ЗАХОДА/

		    return twilightCivil;

	}
	
	
	
	
	//Морские сумерки
	//возвращает двуэлементный строковый массив. 
	//первый элемент:время, когда наступают утренние морские сумерки
	//второй элемент:время, когда наступают вечерние морские сумерки
	public static String[] getTwilightNautical(double latitude, double longitude, double altitude){

		    String twilightNautical[]=new String[2];
			SunTimes suntimes;
			SunTimes suntimesFull;
			SunTimes.Parameters param;
			SunTimes.Parameters paramFull;
			String  str;
			Date date=new Date();//по умолчанию сразу инициализируемся текущим временем
			param= SunTimes.compute();
			paramFull = SunTimes.compute();

			//МОРСКИЕ СУМЕРКИ//МОРСКИЕ СУМЕРКИ//МОРСКИЕ СУМЕРКИ//МОРСКИЕ СУМЕРКИ/
			//теперь меняю параметры
			param.twilight(SunTimes.Twilight.NAUTICAL)//Морские сумерки
				.on(date)       // set a date
				.at(latitude, longitude)   // set a location
			    .height(altitude);//считаем всё с высотой.

			//и еще одни параметры
			paramFull
				.twilight(SunTimes.Twilight.NAUTICAL)//Морские сумерки
				.fullCycle()//для полного цикла
				.on(date)       // set a date
				.at(latitude, longitude)
			    .height(altitude);//считаем всё с высотой.

			//теперь выполню опять рассчет
			suntimes=param.execute();//теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет
			//и ещё один рассчет
			suntimesFull = paramFull.execute();////теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет


			//СЧИТАЕМ КОГДА НАСТУПАЮТ МОРСКИЕ СУМЕРКИ ДЛЯ ВОСХОДА///СЧИТАЕМ КОГДА НАСТУПАЮТ МОРСКИЕ СУМЕРКИ ДЛЯ ВОСХОДА/
			if(suntimes.getRise()!=null){//если гражданские сумерки есть в этот день /для рассвета
				str= //"Утренние морские сумерки наступают: "+
					    detailsSimpleDateFormat.format(suntimes.getRise());
				        twilightNautical[0]=str;//вот, инициализировал строку массива
			}else{//иначе морские сумерки в этот день на рассвете не наступают
				//выясню ближайшее время когда наступают для рассвета морские сумерки
				//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
				if(suntimesFull.getRise()!=null){
					str=//"Сегодня утренние морские сумерки не наступают\n"+
						//">>Ближайшее>>"+
						//"Утренние морские сумерки наступают: "+
						detailsSimpleDateFormat.format(suntimesFull.getRise());
				        twilightNautical[0]=str;
				}else{
					    str="нет данных";//пишем что вернулся null
					    twilightNautical[0]=str;
				}

			}
			//СЧИТАЕМ КОГДА НАСТУПАЮТ МОРСКИЕ СУМЕРКИ ДЛЯ ВОСХОДА///СЧИТАЕМ КОГДА НАСТУПАЮТ МОРСКИЕ СУМЕРКИ ДЛЯ ВОСХОДА/


			//СЧИТАЕМ КОГДА НАСТУПАЮТ МОРСКИЕ СУМЕРКИ ДЛЯ ЗАХОДА///СЧИТАЕМ КОГДА НАСТУПАЮТ МОРСКИЕ СУМЕРКИ ДЛЯ ЗАХОДА/
			if(suntimes.getSet()!=null){//если морские сумерки есть в этот день /для захода
				str= //"Вечерние морские сумерки наступают: "+
					detailsSimpleDateFormat.format(suntimes.getSet());
			        twilightNautical[1]=str;//вот, инициализировал строку массива
			}else{//иначе морские сумерки в этот день для захода не наступают
				//выясню ближайшее время когда наступают для захода морские сумерки
				//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
				if(suntimesFull.getSet()!=null){
					str=//"Сегодня вечерние морские сумерки не наступают\n"+
						//">>Ближайшее>>"+
						//"Вечерние морские сумерки наступают: "+
						detailsSimpleDateFormat.format(suntimesFull.getSet());
				        twilightNautical[1]=str;
				}else{
					str="нет данных";//пишем что вернулся null
					twilightNautical[1]=str;
				}

			}
			//СЧИТАЕМ КОГДА НАСТУПАЮТ МОРСКИЕ СУМЕРКИ ДЛЯ ЗАХОДА///СЧИТАЕМ КОГДА НАСТУПАЮТ МОРСКИЕ СУМЕРКИ ДЛЯ ЗАХОДА/

			//МОРСКИЕ СУМЕРКИ//МОРСКИЕ СУМЕРКИ//МОРСКИЕ СУМЕРКИ//МОРСКИЕ СУМЕРКИ/
			
		    return twilightNautical;
	}
	
	
	
	
	//Астрономические сумерки
	//возвращает двуэлементный строковый массив. 
	//первый элемент:время, когда наступают утренние астрономические сумерки
	//второй элемент:время, когда наступают вечерние астрономические сумерки
	public static String[] getTwilightAstronomical(double latitude, double longitude, double altitude){

		    String twilightAstronomical[]=new String[2];
			SunTimes suntimes;
			SunTimes suntimesFull;
			SunTimes.Parameters param;
			SunTimes.Parameters paramFull;
			String  str;
			Date date=new Date();//по умолчанию сразу инициализируемся текущим временем
			param= SunTimes.compute();
			paramFull = SunTimes.compute();
			//АСТРОНОМИЧЕСКИЕ СУМЕРКИ//АСТРОНОМИЧЕСКИЕ СУМЕРКИ//АСТРОНОМИЧЕСКИЕ СУМЕРКИ//АСТРОНОМИЧЕСКИЕ СУМЕРКИ/
			//теперь меняю параметры
			param.twilight(SunTimes.Twilight.ASTRONOMICAL)//Астрономические сумерки
				.on(date)       // set a date
				.at(latitude, longitude)   // set a location
			    .height(altitude);//считаем всё с высотой.

			//и еще одни параметры
			paramFull
				.twilight(SunTimes.Twilight.ASTRONOMICAL)//Астрономические сумерки
				.fullCycle()//для полного цикла
				.on(date)       // set a date
				.at(latitude, longitude)
			    .height(altitude);//считаем всё с высотой.

			//теперь выполню опять рассчет
			suntimes=param.execute();//теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет
			//и ещё один рассчет
			suntimesFull = paramFull.execute();////теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет


			//СЧИТАЕМ КОГДА НАСТУПАЮТ АСТРОНОМИЧЕСКИЕ СУМЕРКИ ДЛЯ ВОСХОДА///СЧИТАЕМ КОГДА НАСТУПАЮТ АСТРОНОМИЧЕСКИЕ СУМЕРКИ ДЛЯ ВОСХОДА/
			if(suntimes.getRise()!=null){//если астрономические сумерки есть в этот день /для рассвета
				str= //"Утренние астрономические сумерки наступают: "+
					detailsSimpleDateFormat.format(suntimes.getRise());
			        twilightAstronomical[0]=str;//вот, инициализировал строку массива
			}else{//иначе астрономические сумерки в этот день на рассвете не наступают
				//выясню ближайшее время когда наступают для рассвета астрономические сумерки
				//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
				if(suntimesFull.getRise()!=null){
					str=//"Сегодня утренние астрономические сумерки не наступают\n"+
						//">>Ближайшее>>"+
						//"Утренние астрономические сумерки наступают: "+
						detailsSimpleDateFormat.format(suntimesFull.getRise());
				        twilightAstronomical[0]=str;
				}else{
					str="нет данных";//пишем что вернулся null
					twilightAstronomical[0]=str;
				}

			}
			//СЧИТАЕМ КОГДА НАСТУПАЮТ АСТРОНОМИЧЕСКИЕ СУМЕРКИ ДЛЯ ВОСХОДА///СЧИТАЕМ КОГДА НАСТУПАЮТ АСТРОНОМИЧЕСКИЕ СУМЕРКИ ДЛЯ ВОСХОДА/


			//СЧИТАЕМ КОГДА НАСТУПАЮТ АСТРОНОМИЧЕСКИЕ СУМЕРКИ ДЛЯ ЗАХОДА///СЧИТАЕМ КОГДА НАСТУПАЮТ АСТРОНОМИЧЕСКИЕ СУМЕРКИ ДЛЯ ЗАХОДА/
			if(suntimes.getSet()!=null){//если астрономические сумерки есть в этот день /для захода
				str= //"Вечерние астрономические сумерки наступают: "+
					detailsSimpleDateFormat.format(suntimes.getSet());
			        twilightAstronomical[1]=str;//вот, инициализировал строку массива
			}else{//иначе астрономические сумерки в этот день для захода не наступают
				//выясню ближайшее время когда наступают для захода астрономические сумерки
				//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
				if(suntimesFull.getSet()!=null){
					str=//"Сегодня вечерние астрономические сумерки не наступают\n"+
						//">>Ближайшее>>"+
						//"Вечерние астрономические сумерки наступают: "+
						detailsSimpleDateFormat.format(suntimesFull.getSet());
				        twilightAstronomical[1]=str;
				}else{
					    str="нет данных";//пишем что вернулся null
					    twilightAstronomical[1]=str;
				}

			}
			//СЧИТАЕМ КОГДА НАСТУПАЮТ АСТРОНОМИЧЕСКИЕ СУМЕРКИ ДЛЯ ЗАХОДА///СЧИТАЕМ КОГДА НАСТУПАЮТ АСТРОНОМИЧЕСКИЕ СУМЕРКИ ДЛЯ ЗАХОДА/

			//АСТРОНОМИЧЕСКИЕ СУМЕРКИ//АСТРОНОМИЧЕСКИЕ СУМЕРКИ//АСТРОНОМИЧЕСКИЕ СУМЕРКИ//АСТРОНОМИЧЕСКИЕ СУМЕРКИ/
			return twilightAstronomical;
	}

	

	//Золотой час
	//возвращает двуэлементный строковый массив. 
	//первый элемент:время, когда наступают утренний золотой час
	//второй элемент:время, когда наступают вечерний золотой час
	public static String[] getTwilighGoldenHour(double latitude, double longitude, double altitude){

		    String twilighGoldenHour[]=new String[2];
			SunTimes suntimes;
			SunTimes suntimesFull;
			SunTimes.Parameters param;
			SunTimes.Parameters paramFull;
			String  str;
			Date date=new Date();//по умолчанию сразу инициализируемся текущим временем
			param= SunTimes.compute();
			paramFull = SunTimes.compute();

			//ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/
			//теперь меняю параметры
			param.twilight(SunTimes.Twilight.GOLDEN_HOUR)//Астрономические сумерки
				.on(date)       // set a date
				.at(latitude, longitude)   // set a location
			    .height(altitude);//считаем всё с высотой.

			//и еще одни параметры
			paramFull
				.twilight(SunTimes.Twilight.GOLDEN_HOUR)//Астрономические сумерки
				.fullCycle()//для полного цикла
				.on(date)       // set a date
				.at(latitude, longitude)
			    .height(altitude);//считаем всё с высотой.

			//теперь выполню опять рассчет
			suntimes=param.execute();//теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет
			//и ещё один рассчет
			suntimesFull = paramFull.execute();////теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет


			//СЧИТАЕМ КОГДА НАСТУПАЕТ ЗОЛОТОЙ ЧАС ДЛЯ ВОСХОДА///СЧИТАЕМ КОГДА НАСТУПАЕТ ЗОЛОТОЙ ЧАС ДЛЯ ВОСХОДА/
			if(suntimes.getRise()!=null){//если золотой час есть в этот день /для рассвета
				str= //"Утренний золотой час наступает: "+
					detailsSimpleDateFormat.format(suntimes.getRise());
				    twilighGoldenHour[0]=str;//вот, инициализировал строку массива
			}else{//иначе золотой час в этот день на рассвете не наступают
				//выясню ближайшее время когда наступают для рассвета золотой час
				//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
				if(suntimesFull.getRise()!=null){
					str=//"Сегодня утренний золотой час не наступает\n"+
						//">>Ближайшее>>"+
						//"Утренний золотой час наступает: "+
						detailsSimpleDateFormat.format(suntimesFull.getRise());
					    twilighGoldenHour[0]=str;
				}else{
					str="нет данных";//пишем что вернулся null
					twilighGoldenHour[0]=str;
				}

			}
			//СЧИТАЕМ КОГДА НАСТУПАЕТ ЗОЛОТОЙ ЧАС ДЛЯ ВОСХОДА///СЧИТАЕМ КОГДА НАСТУПАЕТ ЗОЛОТОЙ ЧАС ДЛЯ ВОСХОДА/


			//СЧИТАЕМ КОГДА НАСТУПАЕТ ЗОЛОТОЙ ЧАС ДЛЯ ЗАХОДА///СЧИТАЕМ КОГДА НАСТУПАЕТ ЗОЛОТОЙ ЧАС ДЛЯ ЗАХОДА/
			if(suntimes.getSet()!=null){//если золотой час есть в этот день /для захода
				str= //"Вечерний золотой час наступает: "+
					detailsSimpleDateFormat.format(suntimes.getSet());
				    twilighGoldenHour[1]=str;//вот, инициализировал строку массива
			}else{//иначе золотой час в этот день для захода не наступает
				//выясню ближайшее время когда наступают для захода золотой час
				//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
				if(suntimesFull.getSet()!=null){
					str=//"Сегодня вечерний золотой час не наступает\n"+
						//">>Ближайшее>>"+
						//"Вечерний золотой час наступает: "+
						detailsSimpleDateFormat.format(suntimesFull.getSet());
					    twilighGoldenHour[1]=str;
				}else{
					str="нет данных";//пишем что вернулся null
					twilighGoldenHour[1]=str;
				}

			}
			//СЧИТАЕМ КОГДА НАСТУПАЕТ ЗОЛОТОЙ ЧАС ДЛЯ ЗАХОДА///СЧИТАЕМ КОГДА НАСТУПАЕТ ЗОЛОТОЙ ЧАС ДЛЯ ЗАХОДА/
			//ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/ЗОЛОТОЙ ЧАС/

			return twilighGoldenHour;
	}

	
	
	//Синий час
	//возвращает двуэлементный строковый массив. 
	//первый элемент:время, когда наступают утренний синий час
	//второй элемент:время, когда наступают вечерний синий час
	public static String[] getTwilighBlueHour(double latitude, double longitude, double altitude){

		    String twilighBlueHour[]=new String[2];
			SunTimes suntimes;
			SunTimes suntimesFull;
			SunTimes.Parameters param;
			SunTimes.Parameters paramFull;
			String  str;
			Date date=new Date();//по умолчанию сразу инициализируемся текущим временем
			param= SunTimes.compute();
			paramFull = SunTimes.compute();
			//СИНИЙ ЧАС/СИНИЙ ЧАС///СИНИЙ ЧАС/СИНИЙ ЧАС///СИНИЙ ЧАС/СИНИЙ ЧАС///СИНИЙ ЧАС/СИНИЙ ЧАС///СИНИЙ ЧАС/СИНИЙ ЧАС/
			//теперь меняю параметры
			param.twilight(SunTimes.Twilight.BLUE_HOUR)//Астрономические сумерки
				.on(date)       // set a date
				.at(latitude, longitude)   // set a location
			.height(altitude);//считаем всё с высотой.

			//и еще одни параметры
			paramFull
				.twilight(SunTimes.Twilight.BLUE_HOUR)//Астрономические сумерки
				.fullCycle()//для полного цикла
				.on(date)       // set a date
				.at(latitude, longitude)
			.height(altitude);//считаем всё с высотой.

			//теперь выполню опять рассчет
			suntimes=param.execute();//теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет
			//и ещё один рассчет
			suntimesFull = paramFull.execute();////теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет


			//СЧИТАЕМ КОГДА НАСТУПАЕТ СИНИЙ ЧАС ДЛЯ ВОСХОДА///СЧИТАЕМ КОГДА НАСТУПАЕТ СИНИЙ ЧАС ДЛЯ ВОСХОДА/
			if(suntimes.getRise()!=null){//если синий час есть в этот день /для рассвета
				str= //"Утренний синий час наступает: "+
					detailsSimpleDateFormat.format(suntimes.getRise());
				    twilighBlueHour[0]=str;//вот, инициализировал строку массива
			}else{//иначе синий час в этот день на рассвете не наступают
				//выясню ближайшее время когда наступают для рассвета синий час
				//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
				if(suntimesFull.getRise()!=null){
					str=//"Сегодня утренние синий час не наступает\n"+
						//">>Ближайшее>>"+
						//"Утренний синий час наступает: "+
						detailsSimpleDateFormat.format(suntimesFull.getRise());
					    twilighBlueHour[0]=str;
				}else{
					str="нет данных";//пишем что вернулся null
					twilighBlueHour[0]=str;
				}

			}
			//СЧИТАЕМ КОГДА НАСТУПАЕТ СИНИЙ ЧАС ДЛЯ ВОСХОДА///СЧИТАЕМ КОГДА НАСТУПАЕТ СИНИЙ ЧАС ДЛЯ ВОСХОДА/


			//СЧИТАЕМ КОГДА НАСТУПАЕТ СИНИЙ ЧАС ДЛЯ ЗАХОДА///СЧИТАЕМ КОГДА НАСТУПАЕТ СИНИЙ ЧАС ДЛЯ ЗАХОДА/
			if(suntimes.getSet()!=null){//если синий час есть в этот день /для захода
				str= //"Вечерний синий час наступает: "+
					detailsSimpleDateFormat.format(suntimes.getSet());
				    twilighBlueHour[1]=str;//вот, инициализировал строку массива
			}else{//иначе синий час в этот день для захода не наступает
				//выясню ближайшее время когда наступают для захода синий час
				//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
				if(suntimesFull.getSet()!=null){
					str=//"Сегодня вечерние синий час не наступает\n"+
						//">>Ближайшее>>"+
						//"Вечерний синий час наступает: "+
						detailsSimpleDateFormat.format(suntimesFull.getSet());
					    twilighBlueHour[1]=str;
				}else{
					str="нет данных";//пишем что вернулся null
					twilighBlueHour[1]=str;
				}

			}
			//СЧИТАЕМ КОГДА НАСТУПАЕТ СИНИЙ ЧАС ДЛЯ ЗАХОДА///СЧИТАЕМ КОГДА НАСТУПАЕТ СИНИЙ ЧАС ДЛЯ ЗАХОДА/
			//СИНИЙ ЧАС/СИНИЙ ЧАС///СИНИЙ ЧАС/СИНИЙ ЧАС///СИНИЙ ЧАС/СИНИЙ ЧАС///СИНИЙ ЧАС/СИНИЙ ЧАС///СИНИЙ ЧАС/СИНИЙ ЧАС/

			return twilighBlueHour;
	}
	
	
	
	
	//Ночной час
	//возвращает строку, когда наступает ночной час
	public static String getTwilighNightHour(double latitude, double longitude, double altitude){

		    String twilighNightHour="";
			SunTimes suntimes;
			SunTimes suntimesFull;
			SunTimes.Parameters param;
			SunTimes.Parameters paramFull;
			String  str;
			Date date=new Date();//по умолчанию сразу инициализируемся текущим временем
			param= SunTimes.compute();
			paramFull = SunTimes.compute();
			//НОЧНОЙ ЧАС///НОЧНОЙ ЧАС///НОЧНОЙ ЧАС///НОЧНОЙ ЧАС///НОЧНОЙ ЧАС///НОЧНОЙ ЧАС///НОЧНОЙ ЧАС/
			//теперь меняю параметры
			param.twilight(SunTimes.Twilight.NIGHT_HOUR)//
				.on(date)       // set a date
				.at(latitude, longitude)   // set a location
			    .height(latitude);//считаем всё с высотой.

			//и еще одни параметры
			paramFull
				.twilight(SunTimes.Twilight.NIGHT_HOUR)//
				.fullCycle()//для полного цикла
				.on(date)       // set a date
				.at(latitude, longitude)
			    .height(latitude);//считаем всё с высотой.

			//теперь выполню опять рассчет
			suntimes=param.execute();//теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет
			//и ещё один рассчет
			suntimesFull = paramFull.execute();////теперь обьект класса ссылается на тот же экземпляр, но уже содержащий новый рассчет


			//СЧИТАЕМ КОГДА НАСТУПАЕТ НОЧНОЙ ЧАС///СЧИТАЕМ КОГДА НАСТУПАЕТ НОЧНОЙ ЧАС/
			if(suntimes.getSet()!=null){//если ночной час есть в этот день (24часа вперед)
				str= //"Ночной час наступает: "+
					detailsSimpleDateFormat.format(suntimes.getSet());
				    twilighNightHour=str;//вот, инициализировал строку массива
			}else{//иначе ночной час в этот день не наступают
				//выясню ближайшее время когда наступают ночной час
				//но они возможно тоже могут вернуть нам null, поэтому предостережемся от сбоя
				if(suntimesFull.getSet()!=null){
					str=//"Сегодня ночной час не наступает\n"+
						//">>Ближайшее>>"+
						//"Ночной час наступает: "+
						detailsSimpleDateFormat.format(suntimesFull.getSet());
				        twilighNightHour=str;
				}else{
					str="нет данных";//пишем что вернулся null
					twilighNightHour=str;
				}

			}
			//СЧИТАЕМ КОГДА НАСТУПАЕТ НОЧНОЙ ЧАС///СЧИТАЕМ КОГДА НАСТУПАЕТ НОЧНОЙ ЧАС/
			//НОЧНОЙ ЧАС///НОЧНОЙ ЧАС///НОЧНОЙ ЧАС///НОЧНОЙ ЧАС///НОЧНОЙ ЧАС///НОЧНОЙ ЧАС///НОЧНОЙ ЧАС/


			return twilighNightHour;
	}
	
	
	

	
	//возвращает двуэлементный строковый массив. 
	//первый элемент:время, когда солнце достигает своей наивысшей точки в течение следующих 24 часов
	//второй элемент:время, когда солнце достигает своей самой низкой точки в течение следующих 24 часов
	public static String[] getSunNoonNadir(double latitude, double longitude, double altitude){
		String sunNoonNadir[]=new String[2];
		
		Date date=new Date();//по умолчанию сразу инициализируемся текущим временем
		
		//считаем время когда Солнце достигает своей самой низкой, и своей самой высокой точки 
		//в течении следующих 24 часов
		SunTimes suntimes = SunTimes.compute()
			.on(date)       // set a date
			.at(latitude, longitude)// set a location
			.height(altitude)//считаем всё с высотой.
			.execute();     // get the results
		//String noonText="Время, когда солнце достигает своей наивысшей точки в течение следующих 24 часов:";
		//String nadirText="Время, когда солнце достигает своей самой низкой точки в течение следующих 24 часов:";
		String noon= detailsSimpleDateFormat.format(suntimes.getNoon());//самая высокая точка
		String nadir= detailsSimpleDateFormat.format(suntimes.getNadir());//самая низкая точка
		//считаем время когда Солнце достигает своей самой низкой, и своей самой высокой точки 
		//в течении следующих 24 часов
		sunNoonNadir[0]=noon;
		sunNoonNadir[1]=nadir;
		
		return sunNoonNadir;
	}
	
	
	
	
	//возвращает четырехэлементый массив double. 
	//первый элемент:высота видимого Солнца над горизонтом в градусах.
	//второй элемент:истинная высота Солнца над горизонтом в градусах
	//третий элемент:азимут Солнца в градусах по северу
	//четвертый элемент:расстояние до Солнца в км
	public static double[] getOtherSunParametrs(double latitude, double longitude, double altitude){
		double otherSunParametrs[]=new double[4];
		
		Date date=new Date();//по умолчанию сразу инициализируемся текущим временем

		//считаем другое
		SunPosition.Parameters params = SunPosition.compute();
		params.on(date);
		params.at(latitude, longitude)
			  .height(altitude);//считаем всё с высотой.

		//теперь выполню рассчет
		SunPosition pos = params.execute();

		double altitudeSun=pos.getAltitude();//высота видимого Солнца над горизонтом в градусах.
		double trueAltitudeSun= pos.getTrueAltitude();//истинная высота Солнца над горизонтом в градусах
		double azimuthSun= pos.getAzimuth();//азимут Солнца в градусах по северу
		double distanceSun= pos.getDistance();//расстояние до Солнца в км
		
		otherSunParametrs[0]=altitudeSun;
		otherSunParametrs[1]=trueAltitudeSun;
		otherSunParametrs[2]=azimuthSun;
		otherSunParametrs[3]=distanceSun;
		
		return otherSunParametrs;
	}
	
	
	
	

}

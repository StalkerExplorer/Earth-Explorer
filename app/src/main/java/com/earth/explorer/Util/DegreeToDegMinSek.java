package com.earth.explorer.Util;



import java.math.*;


public class DegreeToDegMinSek
{



	//считаем градусы
	//метод на вход принимает градусы, возвращет 
	//полное число градусов (отбросив дробную часть).
	private static int degree(double mdouble){
		String str=mdouble+"";
		String str1=str.substring(0,str.indexOf('.'));
		return  (int)Math.abs(Double.parseDouble(str1));
	}
	//считаем минуты
	//метод принимает на вход градусы, возвращает
	//полное число минут. Без секунд
	private static int minut(double degree){


		double minut=дробная_часть(degree)*60;
		String str=minut+"";
		String str1=str.substring(0,str.indexOf('.'));
		return (int)Double.parseDouble(str1);
	}
	//считаем секунды
	//метод принимает на вход градусы, возвращает
	//полное число секунд. Без градусов и минут
	private static double sekond(double degree){
		double minut= дробная_часть(degree)*60;
		double sekond=дробная_часть(minut)*60;
		return round(sekond,2);
	}



//ВОЗВРАЩАЕМ СТРОКУ ГРАДУСЫ°МИНУТЫ'СЕКУНДЫ"
	//Метод на вход принимает градусы, возвращает строку
	//число полных градусов, число полных минут, число полных секунд.
	public static String latDegMinSek(double degree){

		String str;

        if(degree>0){
		    str="N "+degree(degree)+"°"+minut(degree)+"'"+sekond(degree)+"\"";
		}else{
			str="S "+degree(degree)+"°"+minut(degree)+"'"+sekond(degree)+"\"";
		}
		return str;
	}

//ВОЗВРАЩАЕМ СТРОКУ ГРАДУСЫ°МИНУТЫ'СЕКУНДЫ"
	//Метод на вход принимает градусы, возвращает строку
	//число полных градусов, число полных минут, число полных секунд.
	public static String lonDegMinSek(double degree){

		String str;

        if(degree>0){
		    str="E "+degree(degree)+"°"+minut(degree)+"'"+sekond(degree)+"\"";
		}else{
			str="W "+degree(degree)+"°"+minut(degree)+"'"+sekond(degree)+"\"";
		}
		return str;
	}


	//дробная часть числа
	public static double дробная_часть(double mdouble){

		String str=mdouble+"";
		String str1=str.substring(str.indexOf('.'));
		double дробная_часть=Double.parseDouble(str1);

		return Math.abs(дробная_часть);


	}


	/*
	 //ВОЗВРАЩАЕМ ШИРОТУ В ГРАДУСАХ
	 //"триммируем" этими методами строку с координатами центра
	 public double latitude(GeoPoint point){
	 String s=point.toString();
	 String str=s.substring(0,s.indexOf(','));
	 return Double.parseDouble(str);
	 }
	 //ВОЗВРАЩАЕМ ДОЛГОТУ В ГРАДУСАХ
	 public double longitude(GeoPoint point){
	 String s= point.toString();
	 String str0=s.substring(s.indexOf(',')+1);
	 String str=str0.substring(0,str0.indexOf(','));
	 return Double.parseDouble(str);
	 }
	 */



	//для округления
	private static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}


}

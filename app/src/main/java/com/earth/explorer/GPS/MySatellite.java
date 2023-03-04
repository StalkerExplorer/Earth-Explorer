package com.earth.explorer.GPS;


//INCREASE возрастание
//DESCENDING //убывание
//будем складывать в arraylist экземпляры этого класа
//задействуется такое для удобства дальнейшей сортировки
import com.earth.explorer.Util.*;
import android.location.*;
import java.util.*;

public class MySatellite
{

	//экземплярные переменные:
	private int svid;//идентификационный номер спутника 
	public int constellationType;//группа спутников: 
	private float azimuthDegrees;//азимут спутника
	private float elevationDegrees;//угол возвышения
	private boolean hasCarrierFrequencyHz=true;//доступен ли допустимый
	private float carrierFrequencyHz;//несущая частота
	private boolean hasBasebandCn0DbHz=true;//доступен ли допустимый
	private float basebandCn0DbHz;//плотность отношения несущей к шуму основной полосы частот спутника
	private float cn0DbHz;//плотность отношения несущей к шуму на антенне спутника
	private boolean almanacData;//альманах
	private boolean ephemerisData;//эфемеридные данные:
	public boolean usedInFix;//использовался ли спутник в расчете самой последней фиксации положения
    //добавим поле String, будет хранить информацию для диалогового окна
	//private String satString;
	
	
	//конструктор класса: 
	public MySatellite(){}

	//set-методы:
	public void setSvid(int svid){this.svid=svid;};
	public void setConstellationType(int constellationType){this.constellationType=constellationType;};
	public void setAzimuthDegrees(float azimuthDegrees){this.azimuthDegrees=azimuthDegrees;};
	public void setElevationDegrees(float elevationDegrees){this.elevationDegrees=elevationDegrees;};
	public void setHasCarrierFrequencyHz(boolean hasCarrierFrequencyHz){this.hasCarrierFrequencyHz=hasCarrierFrequencyHz;};
	public void setCarrierFrequencyHz(float carrierFrequencyHz){this.carrierFrequencyHz=carrierFrequencyHz;};
	public void setHasBasebandCn0DbHz(boolean hasBasebandCn0DbHz){this.hasBasebandCn0DbHz=hasBasebandCn0DbHz;};
	public void setBasebandCn0DbHz(float basebandCn0DbHz){this.basebandCn0DbHz=basebandCn0DbHz;};
	public void setCn0DbHz(float cn0DbHz){this.cn0DbHz=cn0DbHz;};
	public void setAlmanacData(boolean almanacData){this.almanacData=almanacData;};
	public void setEphemerisData(boolean ephemerisData){this.ephemerisData=ephemerisData;};
	public void setUsedInFix(boolean usedInFix){this.usedInFix=usedInFix;};
    //public void setSatString(String satString){this.satString=satString;};
	
	
	
	
	//get-методы:
	public int getSvid(){return svid;}

	public String getConstellationType(){
		switch (this.constellationType){
			case GnssStatus.CONSTELLATION_BEIDOU:
				return "BEIDOU";
			case GnssStatus.CONSTELLATION_GALILEO:
				return "GALILEO";
			case GnssStatus.CONSTELLATION_GLONASS:
				return "GLONASS";
			case GnssStatus.CONSTELLATION_GPS:
				return "GPS";
			case GnssStatus.CONSTELLATION_IRNSS:
				return "IRNSS";
			case GnssStatus.CONSTELLATION_QZSS:
				return "QZSS";
			case GnssStatus.CONSTELLATION_SBAS:
				return "SBAS";
			case GnssStatus.CONSTELLATION_UNKNOWN:
				return "UNKNOWN";
			default: return "unrecognized constellation sattelite!";
		}

	}

	
	
	//возвращает String, которое хранит информацию для диалогового окна
	public String getSatString(){
	
		//если спутник использовался в расчете последней фиксации, вернем подкрашенную строку
		
		if(usedInFix().equals("да")){
			return
			      "<font color=#448AFF>"+
			"Идентификационный номер спутника (Svid): "+getSvid()+"<br>"+//Идентификационный номер спутника (Svid)
			CarrierFreqUtils. getSatelliteName(getConstellationType(),getSvid())+//Космический аппарат: 
			"Орбитальная группировка: "+  getConstellationType()  +" "+GPSActivity. getCountry(getConstellationType())+"<br>"+//Группа
			"Азимут спутника: "+getAzimuthDegrees()+"°"+"<br>"+//Азимут спутника
			"Угол возвышения: "+getElevationDegrees()+"°"+"<br>"+//Высота спутника
			"Несущая частота: "+(getCarrierFrequencyHz()/1000000)+" МГц"+"<br>"+//Несущая частота
			CarrierFreqUtils.getCarrierFrequencyBand(getConstellationType(), getCarrierFrequencyHz()/1000000, getSvid())+//Диапазон:
			"Плотность отношения несущей к шуму основной полосы частот спутника: "+getBasebandCn0DbHz()+" ДБ-Гц"+"<br>"+//Плотность отношения несущей к шуму основной полосы частот спутника
			"Плотность отношения несущей к шуму на антенне спутника: "+getCn0DbHz()+" ДБ-Гц"+"<br>"+//Плотность отношения несущей к шуму на антенне спутника
			"Альманах: "+hasAlmanacData()+"<br>"+//hasAlmanacData
			"Эфемеридные данные: "+hasEphemerisData()+"<br>"+//hasEphemerisData
			"Использовался ли спутник в расчете самой последней фиксации положения: "+ usedInFix()+"<br>"+//Использовался ли спутник в расчете самой последней фиксации положения
					"</font>";
			}else{
			
			return
			
				"Идентификационный номер спутника (Svid): "+getSvid()+"<br>"+//Идентификационный номер спутника (Svid)
				CarrierFreqUtils. getSatelliteName(getConstellationType(),getSvid())+//Космический аппарат: 
				"Орбитальная группировка: "+  getConstellationType()  +" "+GPSActivity. getCountry(getConstellationType())+"<br>"+//Группа
				"Азимут спутника: "+getAzimuthDegrees()+"°"+"<br>"+//Азимут спутника
				"Угол возвышения: "+getElevationDegrees()+"°"+"<br>"+//Высота спутника
				"Несущая частота: "+(getCarrierFrequencyHz()/1000000)+" МГц"+"<br>"+//Несущая частота
				CarrierFreqUtils.getCarrierFrequencyBand(getConstellationType(), getCarrierFrequencyHz()/1000000, getSvid())+//Диапазон:
				"Плотность отношения несущей к шуму основной полосы частот спутника: "+getBasebandCn0DbHz()+" ДБ-Гц"+"<br>"+//Плотность отношения несущей к шуму основной полосы частот спутника
				"Плотность отношения несущей к шуму на антенне спутника: "+getCn0DbHz()+" ДБ-Гц"+"<br>"+//Плотность отношения несущей к шуму на антенне спутника
				"Альманах: "+hasAlmanacData()+"<br>"+//hasAlmanacData
				"Эфемеридные данные: "+hasEphemerisData()+"<br>"+//hasEphemerisData
				"Использовался ли спутник в расчете самой последней фиксации положения: "+ usedInFix()+//Использовался ли спутник в расчете самой последней фиксации положения
				"<br>";
		}
			
			};
	
	
	
	
	public float getAzimuthDegrees(){return azimuthDegrees;}
	public float getElevationDegrees(){return elevationDegrees;}
	public boolean hasCarrierFrequencyHz(){return hasCarrierFrequencyHz;}

	public Float getCarrierFrequencyHz(){
		if(hasCarrierFrequencyHz){//если доступен допустимый
			return carrierFrequencyHz;}//происходит возврат из метода
		return null;//если возврат не произошел, то мы пришли сюда. А значит допустимый недоступен, и возвращаем null
	}

	public boolean hasBasebandCn0DbHz(){return hasBasebandCn0DbHz;}

	public Float getBasebandCn0DbHz(){

		if(hasBasebandCn0DbHz){//если доступен допустимый
			return basebandCn0DbHz;}//происходит возврат из метода
		return null;//если возврат не произошел, то мы пришли сюда. А значит допустимый недоступен, и возвращаем null;
	}

	public float getCn0DbHz(){return cn0DbHz;};
	public boolean almanacData(){return almanacData;};

	public String  hasAlmanacData(){
		if(almanacData){
			return "да";}//если есть альманах возвращем "да"
		return "нет";//если возврат не произошел, то мы пришли сюда, и возвращаем "нет"
	}
	public boolean ephemerisData(){return ephemerisData;};

	public String  hasEphemerisData(){
		if(ephemerisData){
			return "да";}//если есть эфемеридные данные возвращем "да"
		return "нет";//если возврат не произошел, то мы пришли сюда, и возвращаем "нет"
	}

	public String usedInFix(){
		if(usedInFix){
			return "да";}//если спутник использовался в расчете самой последней фиксации положения: возвращем "да"
		return "нет";//если возврат не произошел, то мы пришли сюда, и возвращаем "нет"
	};

	public boolean getUsedInFix(){return this.usedInFix;}





	//реализуем компаратор для сортировки  
	//наличие информации об эфемеридах
	//этот компаратор не пока задействован, нигде не используется
	//https://ru.stackoverflow.com/questions/468248/%D0%A1%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B0-%D0%BF%D0%BE-%D0%BF%D0%BE%D0%BB%D1%8E-%D1%8D%D0%BB%D0%B5%D0%BC%D0%B5%D0%BD%D1%82%D0%B0
	public static final Comparator<MySatellite> COMPARE_EPHEMERIS_DATA = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return (String.valueOf(!p1. ephemerisData))  .compareTo(String.valueOf(!p2.ephemerisData));
		}

	};




	//реализуем компаратор для сортировки  
	//наличие альманаха
	//этот компаратор не пока задействован, нигде не используется
	//https://ru.stackoverflow.com/questions/468248/%D0%A1%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B0-%D0%BF%D0%BE-%D0%BF%D0%BE%D0%BB%D1%8E-%D1%8D%D0%BB%D0%B5%D0%BC%D0%B5%D0%BD%D1%82%D0%B0
	public static final Comparator<MySatellite> COMPARE_ALMANAC_DATA = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return (String.valueOf(!p1. almanacData))  .compareTo(String.valueOf(!p2.almanacData));
		}

	};




	//реализуем компаратор для сортировки
	//орбитальная группа//убывание
	//https://ru.stackoverflow.com/questions/468248/%D0%A1%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B0-%D0%BF%D0%BE-%D0%BF%D0%BE%D0%BB%D1%8E-%D1%8D%D0%BB%D0%B5%D0%BC%D0%B5%D0%BD%D1%82%D0%B0
	public static final Comparator<MySatellite> COMPARE_CONSTELLATION_TYPE_DESCENDING = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return p2.getConstellationType()  .compareTo(p1.getConstellationType());
		}

	};

	//реализуем компаратор для сортировки
	//орбитальная группа//возрастание
	//https://ru.stackoverflow.com/questions/468248/%D0%A1%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B0-%D0%BF%D0%BE-%D0%BF%D0%BE%D0%BB%D1%8E-%D1%8D%D0%BB%D0%B5%D0%BC%D0%B5%D0%BD%D1%82%D0%B0
	public static final Comparator<MySatellite> COMPARE_CONSTELLATION_TYPE_INCREASE = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return p1.getConstellationType()  .compareTo(p2.getConstellationType());
		}

	};







	//реализуем компаратор для сортировки
	//используются в определении
	//https://ru.stackoverflow.com/questions/468248/%D0%A1%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B0-%D0%BF%D0%BE-%D0%BF%D0%BE%D0%BB%D1%8E-%D1%8D%D0%BB%D0%B5%D0%BC%D0%B5%D0%BD%D1%82%D0%B0
	public static final Comparator<MySatellite> COMPARE_USED_IN_FIX = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return (String.valueOf(!p1. usedInFix))  .compareTo(String.valueOf(!p2.usedInFix));
		}

	};







	//плотность отношения несущей к шуму основной полосы//убывание
	public static final Comparator<MySatellite> COMPARE_BASEBAND_CN_DESCENDING = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return (int)(p2.getBasebandCn0DbHz() - p1.getBasebandCn0DbHz());
		}

	};

	//плотность отношения несущей к шуму основной полосы//возрастание
	public static final Comparator<MySatellite> COMPARE_BASEBAND_CN_INCREASE = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return (int)(p1.getBasebandCn0DbHz() - p2.getBasebandCn0DbHz());
		}

	};







	//этот компаратор не пока задействован, нигде не используется
	//плотность отношения несущей к шуму на антенне 
	public static final Comparator<MySatellite> COMPARE_CN0DBHZ = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return (int)(p2.getCn0DbHz() - p1.getCn0DbHz());
		}

	};






	//реализуем компаратор для сортировки   
	//https://ru.stackoverflow.com/questions/468248/%D0%A1%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B0-%D0%BF%D0%BE-%D0%BF%D0%BE%D0%BB%D1%8E-%D1%8D%D0%BB%D0%B5%D0%BC%D0%B5%D0%BD%D1%82%D0%B0
	//несущая частота//убывание
	public static final Comparator<MySatellite> COMPARE_CARRIER_FREQUENCY_DESCENDING = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return (int)(p2.getCarrierFrequencyHz() - p1.getCarrierFrequencyHz());
		}

	};

	//реализуем компаратор для сортировки   
	//https://ru.stackoverflow.com/questions/468248/%D0%A1%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B0-%D0%BF%D0%BE-%D0%BF%D0%BE%D0%BB%D1%8E-%D1%8D%D0%BB%D0%B5%D0%BC%D0%B5%D0%BD%D1%82%D0%B0
	//несущая частота//возрастание
	public static final Comparator<MySatellite> COMPARE_CARRIER_FREQUENCY_INCREASE = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return (int)(p1.getCarrierFrequencyHz() - p2.getCarrierFrequencyHz());
		}

	};







	//реализуем компаратор для сортировки
	//азимут//убывание
	//https://ru.stackoverflow.com/questions/468248/%D0%A1%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B0-%D0%BF%D0%BE-%D0%BF%D0%BE%D0%BB%D1%8E-%D1%8D%D0%BB%D0%B5%D0%BC%D0%B5%D0%BD%D1%82%D0%B0
	public static final Comparator<MySatellite> COMPARE_AZIMUTH_DEGREES_DESCENDING = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return (int)(p2.getAzimuthDegrees() - p1.getAzimuthDegrees());
		}

	};

    //реализуем компаратор для сортировки
	//азимут//возрастание
	//https://ru.stackoverflow.com/questions/468248/%D0%A1%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B0-%D0%BF%D0%BE-%D0%BF%D0%BE%D0%BB%D1%8E-%D1%8D%D0%BB%D0%B5%D0%BC%D0%B5%D0%BD%D1%82%D0%B0
	public static final Comparator<MySatellite> COMPARE_AZIMUTH_DEGREES_INCREASE = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return (int)(p1.getAzimuthDegrees() - p2.getAzimuthDegrees());
		}

	};









	//реализуем компаратор для сортировки  
	//угол возвышения//убывание
	//https://ru.stackoverflow.com/questions/468248/%D0%A1%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B0-%D0%BF%D0%BE-%D0%BF%D0%BE%D0%BB%D1%8E-%D1%8D%D0%BB%D0%B5%D0%BC%D0%B5%D0%BD%D1%82%D0%B0
	public static final Comparator<MySatellite> COMPARE_ELEVATION_DEGREES_DESCENDING = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return (int)(p2.getElevationDegrees() - p1.getElevationDegrees());
		}

	};

	//реализуем компаратор для сортировки  
	//угол возвышения//возрастание
	//https://ru.stackoverflow.com/questions/468248/%D0%A1%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B0-%D0%BF%D0%BE-%D0%BF%D0%BE%D0%BB%D1%8E-%D1%8D%D0%BB%D0%B5%D0%BC%D0%B5%D0%BD%D1%82%D0%B0
	public static final Comparator<MySatellite> COMPARE_ELEVATION_DEGREES_INCREASE = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{
			return (int)(p1.getElevationDegrees() - p2.getElevationDegrees());
		}

	};

	
	
	
	
	
	////реализуем компаратор для сортировки. Сортировать От меньшего к большему 
	public static final Comparator<MySatellite> COMPARE_SVID_INCREASE = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{

			int svid1=p1.svid;
			int svid2=p2.svid;

			if (svid1 > svid2) {
				return 1;
			} else if (svid1 < svid2) {
				return -1;
			} else {
				return 0;	
			}

		}

	};
	
	
	
	
	
	
	
	////реализуем компаратор для сортировки. Сортировать От большего к меньшему
	public static final Comparator<MySatellite> COMPARE_SVID_DESCENDING = new Comparator<MySatellite>() {

		@Override
		public int compare(MySatellite p1, MySatellite p2)
		{

			int svid1=p1.svid;
			int svid2=p2.svid;

			if (svid1 > svid2) {
				return -1;
			} else if (svid1 < svid2) {
				return 1;
			} else {
				return 0;	
			}

		}

	};

}

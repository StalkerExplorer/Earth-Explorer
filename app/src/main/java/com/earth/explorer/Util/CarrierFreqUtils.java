package com.earth.explorer.Util;

public class CarrierFreqUtils
{

	static double cfMhz;
	static String band="Диапазон: ";
	static String satName="";
	static String spacecraft="Космический аппарат: ";
	static String newLine="\n";


	//метод обрезает число
	public static double getValueMhz(double value){

		double m=0;

		String str=value+"";
		String substring1="12345.0";//= str.substring(0,(str.indexOf('.')+2));


		int indexOfPoint=str.indexOf('.')+1;
		int lenght=str.length();//кол-во символов


		//Если мы обратимся к несуществующему индексу в строке, это вызовет сбой
		//нам надо учесть варианты.

		//если количество цифр после . больше двух (это значит что после точки, находится три или больше цифры)
		if(  (lenght-(indexOfPoint))>2){
			//то мы можем вызвать str.indexOf('.')+3   без сбоя
			substring1= str.substring(0,(str.indexOf('.')+1 +3));//находим эту подстроку "1561.098"
		}


		//если количество цифр после . больше 1 но меньше трех (это значит что после точки, находится две цифры)
		if(  (lenght-(indexOfPoint))>1&&
		   (lenght-(indexOfPoint))<3){
			//то мы можем вызвать str.indexOf('.')+1   без сбоя
			substring1= str.substring(0,(str.indexOf('.')+1 +2));//находим эту подстроку "1575.42"
		}


		//если после точки, находится одна цифра
		if(  (lenght-(indexOfPoint))==1){
			//то мы можем вызвать str.indexOf('.')   без сбоя
			substring1= str.substring(0,(str.indexOf('.')+1 +1));//находим эту подстроку "1602.0"
		}


		m= Double.valueOf(substring1);
		return m;
	}





	public static String getCarrierFrequencyBand(String constellationType, double Mhz, int svid){

		String band="";
		cfMhz= getValueMhz( Mhz );


		switch (constellationType) {

            case "GPS":
                band= getNavstarCF(cfMhz);
				break;

            case "GLONASS":
                band= getGlonassCf(cfMhz);
				break;

            case "BEIDOU":
                band= getBeidoucCf(cfMhz);
				break;

            case "QZSS":
                band= getQzssCf(cfMhz);
				break;

            case "GALILEO":
                band= getGalileoCf(cfMhz);
				break;

            case "IRNSS":
                band= getIrnssCf(cfMhz);
				break;

            case "SBAS":
                band= getSbasCf(svid, cfMhz);
				break;

            case "UNKNOWN":
				band="";
				break;

        }

        return band;
	}




	//GPS
    public static String getNavstarCF(double carrierFrequencyMhz) {
        if (carrierFrequencyMhz==1575.42) {
            return band+"L1"+newLine;
        } else if (carrierFrequencyMhz==1227.6) {
            return band+"L2"+newLine;
        } else if (carrierFrequencyMhz==1381.05) {
            return band+"L3"+newLine;
        } else if (carrierFrequencyMhz==1379.913) {
            return band+"L4"+newLine;
        } else if (carrierFrequencyMhz==1176.45) {
            return band+"L5"+newLine;
        } else {
            return "";
        }
    }



    //Russia GLONASS
    public static String getGlonassCf(double carrierFrequencyMhz) {
        if (carrierFrequencyMhz >= 1598.0 && carrierFrequencyMhz <= 1606.0) {
            // Actual range is 1598.0625 MHz to 1605.375, but allow padding for float comparisons - #103
            return band+"L1"+newLine;
        } else if (carrierFrequencyMhz >= 1242.0 && carrierFrequencyMhz <= 1249.0) {
            // Actual range is 1242.9375 MHz to 1248.625, but allow padding for float comparisons - #103
            return band+"L2"+newLine;
        } else if (carrierFrequencyMhz==1207.14) {
            // Exact range is unclear - appears to be 1202.025 - 1207.14 - #103
            return band+"L3"+newLine;
        } else if (carrierFrequencyMhz==1176.45) {
            return band+"L5"+newLine;
        } else if (carrierFrequencyMhz==1575.42) {
            return band+"L1-C"+newLine;
        } else {
            return "";
        }
    }




    //Chinese Beidou
    public static String getBeidoucCf(double carrierFrequencyMhz) {
        if (carrierFrequencyMhz==1561.098) {
            return band+"B1"+newLine;
        } else if (carrierFrequencyMhz==1589.742) {
            return band+"B1-2"+newLine;
        } else if (carrierFrequencyMhz==1575.42) {
            return band+"B1C"+newLine;
        } else if (carrierFrequencyMhz==1207.14) {
            return band+"B2"+newLine;
        } else if (carrierFrequencyMhz==1176.45) {
            return band+"B2a"+newLine;
        } else if (carrierFrequencyMhz==1268.52) {
            return band+"B3"+newLine;
        } else {
            return "";
        }
    }



    //Japanese QZSS 
    public static String getQzssCf(double carrierFrequencyMhz) {
        if (carrierFrequencyMhz==1575.42) {
            return band+"L1"+newLine;
        } else if (carrierFrequencyMhz==1227.6) {
            return band+"L2"+newLine;
        } else if (carrierFrequencyMhz==1176.45) {
            return band+"L5"+newLine;
        } else if (carrierFrequencyMhz==1278.75) {
            return band+"L6"+newLine;
        } else {
            return "";
        }
    }



    //EU Galileo 
    public static String getGalileoCf(double carrierFrequencyMhz) {
        if (carrierFrequencyMhz==1575.42) {
            return band+"E1"+newLine;
        } else if (carrierFrequencyMhz==1191.795) {
            return band+"E5"+newLine;
        } else if (carrierFrequencyMhz==1176.45) {
            return band+"E5a"+newLine;
        } else if (carrierFrequencyMhz==1207.14) {
            return band+"E5b"+newLine;
        } else if (carrierFrequencyMhz==1278.75) {
            return band+"E6"+newLine;
        } else {
            return "";
        }
    }




    //Indian IRNSS 
    public static String getIrnssCf(double carrierFrequencyMhz) {
        if (carrierFrequencyMhz==1176.45) {
            return band+"L5"+newLine;
        } else if (carrierFrequencyMhz==2492.028) {
            return band+"S"+newLine;
        } else {
            return "";
        }
    }




	//SBAS
    public static String getSbasCf(int svid, double carrierFrequencyMhz) {
        if (svid == 120 || svid == 123 || svid == 126 || svid == 136) {
            // EGNOS - https://gssc.esa.int/navipedia/index.php/EGNOS_Space_Segment
            if (carrierFrequencyMhz==1575.42) {
                return band+"L1"+newLine;
            } else if (carrierFrequencyMhz==1176.45) {
                return band+"L5"+newLine;
            }
        } else if (svid == 129 || svid == 137) {
            // MSAS (Japan) - https://gssc.esa.int/navipedia/index.php/MSAS_Space_Segment
            if (carrierFrequencyMhz==1575.42) {
                return band+"L1"+newLine;
            } else if (carrierFrequencyMhz==1176.45) {
                return band+"L5"+newLine;
            }
        } else if (svid == 127 || svid == 128 || svid == 139) {
            // GAGAN (India)
            if (carrierFrequencyMhz==1575.42) {
                return band+"L1"+newLine;
            }
        } else if (svid == 131 || svid == 133 || svid == 135 || svid == 138) {
            // WAAS (US)
            if (carrierFrequencyMhz==1575.42) {
                return band+"L1"+newLine;
            } else if (carrierFrequencyMhz==1176.45) {
                return band+"L5"+newLine;
            }
        }
        return "";
    }






	//Типы спутниковых систем дополнений. 
	public enum SbasType {
		WAAS, EGNOS, GAGAN, MSAS, SDCM, SNAS, SACCSA, UNKNOWN
		}





	//метод возвращает строку: "Космический аппарат: Луч-5Б" и т.д.
	public static String getSatelliteName(String constellationType, int svid){



		switch (constellationType){//switch (constellationType){



			case "GPS":
                switch (svid){//switch (svid){

					case 1:
						satName=spacecraft+"2011-036A"+newLine;
						break;

					case 2:
						satName=spacecraft+"2004-045A"+newLine;
						break;

					case 3:
						satName=spacecraft+"2014-068A"+newLine;
						break;

					case 4:
						satName=spacecraft+"2018-109A"+newLine;
						break;

					case 5:
						satName=spacecraft+"2009-043A"+newLine;
						break;

					case 6:
						satName=spacecraft+"2014-026A"+newLine;
						break;

					case 7:
						satName=spacecraft+"2008-012A"+newLine;
						break;

					case 8:
						satName=spacecraft+"2015-033A"+newLine;
						break;

					case 9:
						satName=spacecraft+"2014-045A"+newLine;
						break;

					case 10:
						satName=spacecraft+"2015-062A"+newLine;
						break;

					case 11:
						satName=spacecraft+"2021-054A"+newLine;
						break;

					case 12:
						satName=spacecraft+"2006-052A"+newLine;
						break;

					case 13:
						satName=spacecraft+"1997-035A"+newLine;
						break;

					case 14:
						satName=spacecraft+"2020-078A"+newLine;
						break;

					case 15:
						satName=spacecraft+"2007-047A"+newLine;
						break;

					case 16:
						satName=spacecraft+"2003-005A"+newLine;
						break;

					case 17:
						satName=spacecraft+"2005-038A"+newLine;
						break;

					case 18:
						satName=spacecraft+"2019-056A"+newLine;
						break;

					case 19:
						satName=spacecraft+"2004-009A"+newLine;
						break;

					case 20:
						satName=spacecraft+"2000-025A"+newLine;
						break;

					case 21:
						satName=spacecraft+"2003-010A"+newLine;
						break;

					case 22:
						satName=spacecraft+"2000-071A"+newLine;
						break;

					case 23:
						satName=spacecraft+"2020-041A"+newLine;
						break;

					case 24:
						satName=spacecraft+"2012-053A"+newLine;
						break;

					case 25:
						satName=spacecraft+"2010-022A"+newLine;
						break;

					case 26:
						satName=spacecraft+"2015-013A"+newLine;
						break;

					case 27:
						satName=spacecraft+"2013-023A"+newLine;
						break;

					case 28:
						satName=spacecraft+"2000-040A"+newLine;
						break;

					case 29:
						satName=spacecraft+"2007-062A"+newLine;
						break;

					case 30:
						satName=spacecraft+"2014-008A"+newLine;
						break;

					case 31:
						satName=spacecraft+"2006-042A"+newLine;
						break;

					case 32:
						satName=spacecraft+"2016-007A"+newLine;
						break;
				}//switch (svid){
				break;



            case "GLONASS":
                switch (svid){//switch (svid){

					case 1:
						satName=spacecraft+"Космос 2456"+newLine;
						break;

					case 2:
						satName=spacecraft+"Космос 2485"+newLine;
						break;

					case 3:
						satName=spacecraft+"Космос 2476"+newLine;
						break;

					case 4:
						satName=spacecraft+"Космос 2544"+newLine;
						break;

					case 5:
						satName=spacecraft+"Космос 2527"+newLine;
						break;

					case 6:
						satName=spacecraft+"Космос 2457"+newLine;
						break;

					case 7:
						satName=spacecraft+"Космос 2477"+newLine;
						break;

					case 8:
						satName=spacecraft+"Космос 2475"+newLine;
						break;

					case 9:
						satName=spacecraft+"Космос 2501"+newLine;
						break;

					case 10:
						satName=spacecraft+"Космос 2436"+newLine;
						break;

					case 11:
						satName=spacecraft+"Космос 2547"+newLine;
						break;

					case 12:
						satName=spacecraft+"Космос 2534"+newLine;
						break;

					case 13:
						satName=spacecraft+"Космос 2434"+newLine;
						break;

					case 14:
						satName=spacecraft+"Космос 2522"+newLine;
						break;

					case 15:
						satName=spacecraft+"Космос 2529"+newLine;
						break;

					case 16:
						satName=spacecraft+"Космос 2464"+newLine;
						break;

					case 17:
						satName=spacecraft+"Космос 2514"+newLine;
						break;

					case 18:
						satName=spacecraft+"Космос 2492"+newLine;
						break;

					case 19:
						satName=spacecraft+"Космос 2433"+newLine;
						break;

					case 20:
						satName=spacecraft+"Космос 2432"+newLine;
						break;

					case 21:
						satName=spacecraft+"Космос 2500"+newLine;
						break;

					case 22:
						satName=spacecraft+"Космос 2461"+newLine;
						break;

					case 23:
						satName=spacecraft+"Космос 2460"+newLine;
						break;

					case 24:
						satName=spacecraft+"Космос 2545"+newLine;
						break;
				}//switch (svid){
				break;




            case "BEIDOU":
                switch (svid){//switch (svid){

					case 1:
						satName=spacecraft+"2019-027A"+newLine;
						break;

					case 2:
						satName=spacecraft+"2012-059A"+newLine;
						break;

					case 3:
						satName=spacecraft+"2016-037A"+newLine;
						break;

					case 4:
						satName=spacecraft+"2010-057A"+newLine;
						break;

					case 5:
						satName=spacecraft+"2012-008A"+newLine;
						break;

					case 6:
						satName=spacecraft+"2010-036A"+newLine;
						break;

					case 7:
						satName=spacecraft+"2010-068A"+newLine;
						break;

					case 8:
						satName=spacecraft+"2011-013A"+newLine;
						break;

					case 9:
						satName=spacecraft+"2011-038A"+newLine;
						break;

					case 10:
						satName=spacecraft+"2011-073A"+newLine;
						break;

					case 11:
						satName=spacecraft+"2012-018A"+newLine;
						break;

					case 12:
						satName=spacecraft+"2012-018B"+newLine;
						break;

					case 13:
						satName=spacecraft+"2016-021A"+newLine;
						break;

					case 14:
						satName=spacecraft+"2012-050B"+newLine;
						break;

					case 15:
						satName=spacecraft+"2016-021A"+newLine;
						break;

					case 16:
						satName=spacecraft+"2018-057A"+newLine;
						break;

					case 17:
						satName=spacecraft+"2016-037A"+newLine;
						break;

					case 18:
						satName=spacecraft+"2019-027A"+newLine;
						break;

					case 19:
						satName=spacecraft+"2017-069A"+newLine;
						break;

					case 20:
						satName=spacecraft+"2017-069B"+newLine;
						break;

					case 21:
						satName=spacecraft+"2018-018B"+newLine;
						break;

					case 22:
						satName=spacecraft+"2018-018A"+newLine;
						break;

					case 23:
						satName=spacecraft+"2018-062A"+newLine;
						break;

					case 24:
						satName=spacecraft+"2018-062B"+newLine;
						break;

					case 25:
						satName=spacecraft+"2018-067B"+newLine;
						break;

					case 26:
						satName=spacecraft+"2018-067A"+newLine;
						break;

					case 27:
						satName=spacecraft+"2018-003A"+newLine;
						break;

					case 28:
						satName=spacecraft+"2018-003B"+newLine;
						break;

					case 29:
						satName=spacecraft+"2018-029A"+newLine;
						break;

					case 30:
						satName=spacecraft+"2018-029B"+newLine;
						break;

					case 31:
						satName=spacecraft+"unknown"+newLine;
						break;

					case 32:
						satName=spacecraft+"2018-072A"+newLine;
						break;

					case 33:
						satName=spacecraft+"2018-072B"+newLine;
						break;

					case 34:
						satName=spacecraft+"2018-078B"+newLine;
						break;

					case 35:
						satName=spacecraft+"2018-078A"+newLine;
						break;

					case 36:
						satName=spacecraft+"2018-093A"+newLine;
						break;

					case 37:
						satName=spacecraft+"2018-093B"+newLine;
						break;
				}//switch (svid){
				break;



            case "QZSS":
                satName="";
				break;






            case "GALILEO":
				switch (svid){//switch (svid){

					case 1:
						satName=spacecraft+"2016-030B"+newLine;
						break;

					case 2:
						satName=spacecraft+"2016-030A"+newLine;
						break;

					case 3:
						satName=spacecraft+"2016-069B"+newLine;
						break;

					case 4:
						satName=spacecraft+"2016-069C"+newLine;
						break;

					case 5:
						satName=spacecraft+"2016-069D"+newLine;
						break;

					case 6:
						satName=spacecraft+"unknown"+newLine;
						break;

					case 7:
						satName=spacecraft+"2016-069A"+newLine;
						break;

					case 8:
						satName=spacecraft+"2015-079B"+newLine;
						break;

					case 9:
						satName=spacecraft+"2015-079A"+newLine;
						break;

					case 10:
						satName=spacecraft+"2021-116B"+newLine;
						break;

					case 11:
						satName=spacecraft+"2011-060A"+newLine;
						break;

					case 12:
						satName=spacecraft+"2011-060B"+newLine;
						break;

					case 13:
						satName=spacecraft+"2018-060D"+newLine;
						break;

					case 14:
						satName=spacecraft+"unknown"+newLine;
						break;

					case 15:
						satName=spacecraft+"2018-060A"+newLine;
						break;

					case 16:
						satName=spacecraft+"unknown"+newLine;
						break;

					case 17:
						satName=spacecraft+"unknown"+newLine;
						break;

					case 18:
						satName=spacecraft+"2014-050A"+newLine;
						break;

					case 19:
						satName=spacecraft+"2012-055A"+newLine;
						break;

					case 20:
						satName=spacecraft+"unknown"+newLine;
						break;

					case 21:
						satName=spacecraft+"2017-079A"+newLine;
						break;

					case 22:
						satName=spacecraft+"2015-017B"+newLine;
						break;

					case 23:
						satName=spacecraft+"unknown"+newLine;
						break;

					case 24:
						satName=spacecraft+"2015-045A"+newLine;
						break;

					case 25:
						satName=spacecraft+"2017-079B"+newLine;
						break;

					case 26:
						satName=spacecraft+"2015-017A"+newLine;
						break;

					case 27:
						satName=spacecraft+"2017-079C"+newLine;
						break;

					case 28:
						satName=spacecraft+"unknown"+newLine;
						break;

					case 29:
						satName=spacecraft+"unknown"+newLine;
						break;

					case 30:
						satName=spacecraft+"2015-045B"+newLine;
						break;

					case 31:
						satName=spacecraft+"2017-079D"+newLine;
						break;

					case 32:
						satName=spacecraft+"unknown"+newLine;
						break;

					case 33:
						satName=spacecraft+"2018-060B"+newLine;
						break;

					case 34:
						satName=spacecraft+"2021-116A"+newLine;
						break;

					case 35:
						satName=spacecraft+"unknown"+newLine;
						break;

					case 36:
						satName=spacecraft+"2018-060C"+newLine;
						break;
				}//switch (svid){
				break;

            case "IRNSS":
                satName="";
				break;




            case "SBAS"://case "SBAS":
                switch (svid){//switch (svid){

					case 120:
						satName=spacecraft+"INMARSAT_3F2"+newLine;
						break;

					case 123:
						satName=spacecraft+"ASTRA_5B"+newLine;
						break;

					case 125:
						satName=spacecraft+"Луч-5Б"+newLine;
						break;

					case 126:
						satName=spacecraft+"INMARSAT_3F5"+newLine;
						break;

					case 131:
						satName=spacecraft+"GEO5"+newLine;
						break;

					case 133:
						satName=spacecraft+"INMARSAT_4F3"+newLine;
						break;

					case 135:
						satName=spacecraft+"GALAXY_15"+newLine;
						break;

					case 136:
						satName=spacecraft+"SES_5"+newLine;
						break;

					case 138:
						satName=spacecraft+"ANIK"+newLine;
						break;

					case 140:
						satName=spacecraft+"Луч-5В"+newLine;
						break;

					case 141:
						satName=spacecraft+"Луч-5А"+newLine;
						break;


				}//switch (svid){
				break;//case "SBAS":




            case "UNKNOWN":
				satName="";
				break;

		}//switch (constellationType){




		return satName;
	}




}

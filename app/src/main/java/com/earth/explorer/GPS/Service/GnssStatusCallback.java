package com.earth.explorer.GPS.Service;



import com.earth.explorer.*;
import com.earth.explorer.GPS.*;
import com.earth.explorer.Util.*;
import android.content.*;
import android.location.*;
import java.util.*;

public class GnssStatusCallback extends GnssStatus.Callback implements SharedConstants
{

	static Context mContext;
	//Tone tone;
	public static int count=0;//количество видимых спутников
	public static ArrayList <MySatellite> arraySat;
	public static int countUsedFix=0;
	public static int countUsedSatellite=0;
	float summCn0DbHz=0;
	int countNO_DATA=0;
	float averageCn0DbHz=0;
	int i=0;
	int sort=0;


    //если регистрировать в дочернем потоке, то это будет дочерний 
    //поток executor pool-n-thread-1
	//если через handler, в основном, то это будет поток main (основной поток)
	public GnssStatusCallback(Context context){
	    mContext=context;
		//tone=new Tone();
		//onFirstFix=true;
		//создадим arrayList в который сложим экземпляры класса MySatellite, 
		//делаем это для удобства дальнейшей сортировки полученных данных:
		arraySat=new ArrayList <MySatellite>();

	}

	
	
	
	
	

	@Override
	public void onSatelliteStatusChanged(final GnssStatus status) {
		
		
		//Toaster.toast(GPSActivity.mGPSActivity ,Thread.currentThread().getName());
		

		arraySat.clear();//очищаем arrayList
		//Количество спутников (всего)
		count = status.getSatelliteCount();//Получает общее количество спутников в списке спутников.



		//извлекаем циклом сведения о спутниках:
		for(int i = 0; i<count; i++){///123цикл начался

			//Создаем экземпляры класса MySatellite, 
			MySatellite mySatellite=new MySatellite();
			//и инициализируем экземплярные переменные созданных классов
			//по мере интераций цикла:
			mySatellite.setSvid(status.getSvid(i));//Получает идентификационный номер спутника по указанному индексу.
			mySatellite.setConstellationType(status.getConstellationType(i));//Извлекает к группировки каких спутников, относится спутник по указанному индексу.
			mySatellite.setAzimuthDegrees(status.getAzimuthDegrees(i));//Получает азимут спутника по указанному индексу.
			mySatellite.setElevationDegrees(status.getElevationDegrees(i));//Получает высоту спутника по указанному индексу.
			mySatellite.setCarrierFrequencyHz(status.getCarrierFrequencyHz(i));//Получает несущую частоту отслеживаемого сигнала.//Например, это может быть центральная частота GPS для L1 = 1575,45 МГц или L2 = 1227,60 МГц, L5 = 1176,45 МГц, различные каналы GLO и т. д. Значение доступно только в том случае, если hasCarrierFrequencyHz(int)есть true.
			mySatellite.setBasebandCn0DbHz(status.getBasebandCn0DbHz(i));//}//Получает плотность отношения несущей к шуму основной полосы частот спутника с указанным индексом в дБ-Гц.
			mySatellite.setCn0DbHz(status.getCn0DbHz(i));//Извлекает плотность отношения несущей к шуму на антенне спутника с указанным индексом в дБ-Гц.
			mySatellite.setAlmanacData(status.hasAlmanacData(i));//Сообщает, есть ли у спутника с указанным индексом данные альманаха.
			mySatellite.setEphemerisData(status.hasEphemerisData(i));//Сообщает, есть ли у спутника с указанным индексом данные об эфемеридах.
			mySatellite.setUsedInFix(status.usedInFix(i));//Сообщает, использовался ли спутник с указанным индексом в расчете самой последней фиксации положения.

			//по мере прохождения цикла,
			//складываем в arrayList экземпляры класса MySatellite 
			//каждый экземпляр класса теперь содержит сведения об отдельном спутнике
			arraySat.add(mySatellite);


			//по ходу прохождения циклом,
			//выясним сколько всего спутников использовалось в расчете самой последней фиксации положения:
			if(mySatellite.getUsedInFix()){countUsedFix++;}


			//также по ходу прохождения циклом суммируем все значения
			//плотностей отношения несущей к шуму основной полосы частот спутников,
			//а затем поделим на их количество
			//и таким образом выясним среднее значение "условия приёма"
			//затем будем использовать при синтезе речи - плохие условия приёма/хорошие условия приёма
			summCn0DbHz=summCn0DbHz+mySatellite.getCn0DbHz();

			//но для правильной математической операции нужно выяснить
			//количество спутников без тех, у которых getBasebandCn0DbHz=0
			//иначе мы неправильно найдём среднее арифметическое
			if(mySatellite.getCn0DbHz()==NO_DATA){
				countNO_DATA++;//найдем сумму тех, у которых getBasebandCn0DbHz=0
			}//а потом найдем разность count-countNO_DATA, и поделим суммарное значение плотностей на эту разность. Получится правильное сред. арифм.


			//идёт цикл


		}///123цикл закончился


		//цикл закончился,
		//считаем среднее значение
		//плотностей отношения несущей к шуму основной полосы частот спутников,
		//(используется в звуковом уведомление "условия приёма")
		if((count-countNO_DATA)!=0){
			averageCn0DbHz=summCn0DbHz/(count-countNO_DATA);//делим суммарное значение плотностей на эту разность
		}


		//обновляем view в разметках 
		GPSActivity. handler.sendEmptyMessage(0);
		
		
		//tone.tone();


		//countUsedSatellite обнуляется в сервисе, при откл сервиса
		countUsedSatellite=countUsedFix;
		//не забыть их обнулять, а то они насуммируются
		summCn0DbHz=0;
		countNO_DATA=0;
		countUsedFix=0;//обнуляем инициализированную в цикле выше переменную


	}//закрылся метод onSatelliteStatusChanged



	
		
	
	
	//возвращает кол-во спутников, использующихся в расчете последней фиксации местоположения
	public static int getCountUsedFix(){
		return countUsedSatellite;
	}



}

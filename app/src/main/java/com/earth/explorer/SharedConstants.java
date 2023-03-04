package com.earth.explorer;

public interface SharedConstants
{
	//массив разметок GPS
	int[] layoutGPS=new int[]{
		R.layout.gps_page_1,
		R.layout.gps_page_2,
	    R.layout.gps_page_3,
	};
	
	
	float NO_DATA = 0.0f;
	// Идентификатор канала
	String CHANNEL_ID = "Cat channel";
	int NOTIFY_ID = 101;
	
	long MINIMUM_DISTANCE_FOR_UPDATES = 0; // в метрах
    long MINIMUM_TIME_BETWEEN_UPDATES = 500; // в мс
	
	int SETTINGS_CODE = 123;
	int REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION=11;
	
	
	
	//ключи чекнут/не чекнут
	String BEIDOU="BEIDOU";
	String GALILEO="GALILEO";
	String GLONASS="GLONASS";
	String GPS="GPS";
	String IRNSS="IRNSS";
	String QZSS="QZSS";
	String SBAS="SBAS";
	String UNKNOWN="UNKNOWN";
	String USEDINFIX="USEDINFIX";

	
	
	//ключ сортировки
	String SORT="SORT";
	//ключи сортировок списка (при нажатии на TextView, соптировать убывание/возрастание) вторая стр, заголовок
	String SORT_BOOLEAN[]={
		"KEY0",
		"KEY1",
		"KEY2",
		"KEY3",
		"KEY4",
		"KEY5"
	};
	
}

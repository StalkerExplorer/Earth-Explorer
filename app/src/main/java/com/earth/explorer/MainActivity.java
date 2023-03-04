package com.earth.explorer;

import com.earth.explorer.GPS.*;
import com.earth.explorer.GPS.Service.*;
import com.earth.explorer.Util.*;
import android.app.*;
import android.content.*;
import android.location.*;
import android.os.*;
import android.provider.*;
import android.view.*;

public class MainActivity extends Activity implements SharedConstants
{
	public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		context=this;
    }





	
	 //Запуск и остановку сервиса,
	 //удобно организовать через два отдельных метода. 
	 //И в них звонить, из тех мест, в которых возникает эта надобность.
	 //Мы вызываем запуск/остановку сервиса, 
	 //>>Включая/выключая тумблер (SwitchCompat),
	 //>>Нажимая кнопки, расположенные на главном экране (это были кнопки Position, и TrackRecord)
	 //>>и наверное с чекбоксов обязательно будем вызывать.
	 //расположить эти методы неплохо в классе, где код всех кнопок. (У нас это был класс MyButton)
	 
	 //>>Код внутри методов ниже, надо будет ограничить проверками 
	 //наличия разрешений, и
	 //доступности провайдера.
	 //Здесь, в методах ниже, специально, проверки не вкодили, чтобы снизить громоздкость кода. Это расчитано на то, что мы их вкодим потом.
	 //А во всех остальных методах, этой программы, - они уже есть.
	 
	public static void position(){
		context. startForegroundService(new Intent(context, MyService.class));
	}


	public static void stopPosition(){
		context. stopService(new Intent(context, MyService.class));
	}












	public void oncl(View v){

		switch(v.getId()){

			case R.id.buttonGPSActivity:
				Intent intent = new Intent(getApplicationContext(), GPSActivity.class);
				startActivity(intent);
				break;

			case R.id.buttonStartService:
				position();
				break;

			case R.id.buttonStopService:
				stopPosition();
				break;
		}


		Vibration.vibrate(this);
	}






	//так как в android 12 ввели какие-то неудобные новшества с onDestroy()
	@Override
	public void onBackPressed()
	{
		// TODO: Implement this method
		finish();
		super.onBackPressed();
	}


	@Override
	protected void onDestroy()
	{
		//уничтожаем сервис:
		stopService(new Intent(MainActivity.this, MyService.class));
		Toaster.toast(this, "onDestroy()\nMainActivity");
		super.onDestroy();
	}
}



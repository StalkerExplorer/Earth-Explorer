package com.earth.explorer.GPS.View;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import androidx.viewpager2.widget.*;
import com.earth.explorer.*;
import com.earth.explorer.Util.*;

public class Indicator extends LinearLayout
{
	Context context;
	int itemCount=3;//➭инициализировать эту переменную, для количества вкладок
	String itemTab[];//➭инициализировать массив
	Band band;//класс с тонкой нижней полоской
	Tabs tabs;//класс с вкладками
	ViewPager2 viewPager2;
	
	// Конструктор, необходимый для создания элемента внутри кода программы
    public Indicator(Context context) {
        super(context);
		init(context);
    }

    // Конструктор, необходимый для наполнения элемента из файла с ресурсом
    public Indicator (Context context, AttributeSet attrs) {
        super(context, attrs);
		init(context);
    }

    // Конструктор, необходимый для наполнения элемента из файла с ресурсом
    public Indicator (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
		init(context);
    }
	
	
	public void init(Context context){
		this.context=context;
		itemTab=new String[]{
			"Данные",
			"Список",
			"Небосвод",
		};
		setOrientation(LinearLayout.VERTICAL);
		band=getBand();//получим экземпляп класса с полоской
		tabs=getTabs();//получим экземпляр класса с вкладками
		//добавим в class Indicator (он у нас наследует LinearLayout)
		addView(tabs);//добавляем вкладки
		addView(band);//затем, ниже добавляем полосу
	}
	
	
	//ЭТО МЕТОД УПРАВЛЕНИЯ
	//➭задействовать вызов этого метода в onPageScrolled
	//метод setProgress, общий для двух внутренних классов
	//вызовет одноименные методы этих классов. В результате у нас "ездит полоска", и "горят вкладки"
	public void setProgress(ViewPager2 viewPager2, int index, float offset) {
		this.viewPager2=viewPager2;
		band.setProgress(index,offset);
		tabs.setProgress(index,offset);
	}
	
	
	
	
	
	public Tabs getTabs(){
		Tabs tabs=new Tabs();
		int weight=1;
		LinearLayout.LayoutParams lParams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,weight);
		tabs.setLayoutParams(lParams);
		return tabs;
	}
	
	public Band getBand(){
		Band band=new Band();
		int weight=5;
		LinearLayout.LayoutParams lParams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,weight);
		band.setLayoutParams(lParams);
		return band;
	}
	
	
	
	
	
	
	//класс с вкладками
	public class Tabs extends LinearLayout implements View.OnClickListener
	{
		TextView textView[]=new TextView[itemCount];
		float offset = 0;
		int index = 0;
		int textColor1=Color.parseColor("#bdbdbd");
		int textColor2=Color.parseColor("#2d84ff");
		int textColor3=Color.parseColor("#353535");
		//int textColor4=Color.parseColor("#2e2e2e");
		int textColor5=Color.parseColor("#696969");
		int textShadowLayer=Color.parseColor("#000000");
		// Конструктор, необходимый для создания элемента внутри кода программы
		Tabs() {
			super(context);
			init();
		}

		void init(){
			setOrientation(LinearLayout.HORIZONTAL);
			for(int i=0;i<itemCount;i++){
				textView[i]=getTextView(i);
				textView[i].setId(i);//назначим id, таким образом получится что id будет соответствовать стр пейджера
				textView[i].setOnClickListener(this);
				addView(textView[i]);
				if(i!=(itemCount-1)){//если это не последний TextView
				     addView(getVerticalDivider());//добавим вертикальный разделитель между TextView
				}//если это не последний TextView
			}
		}
		
		TextView getTextView(int count){
			TextView textView;
			int weight=1;
			LinearLayout.LayoutParams lParams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,weight);
			textView=new TextView(context);
			textView.setLayoutParams(lParams);
			textView.setTextColor(textColor5);
			textView.setShadowLayer(0,2,2,textShadowLayer);
			textView.setGravity(Gravity.CENTER);
			textView.setBackgroundResource(R.drawable.states_shapes_indicator);
			//textView.setBackgroundResource(R.drawable.selector_textview);
			//textView.setTextSize(10);
			//textView.setTypeface(null,Typeface.BOLD);
			String text=getText(count);
			textView.setText(text);
			return textView;
		}
		
		
		View getVerticalDivider(){
			View view=new View(context);
			LinearLayout.LayoutParams lParams=new LinearLayout.LayoutParams(2,LayoutParams.MATCH_PARENT);
			//lParams.setMargins(0,5,0,5);
			view.setBackgroundColor(textColor3);
			view.setLayoutParams(lParams);
			return view;
		}
		
		public String getText(int i){
			return itemTab[i];
		}
		
		
		@Override
		public void onClick(View p1)
		{
			//число у id соответствует стр пейджера
			int id= p1.getId();
			viewPager2.setCurrentItem(id);
			Vibration.vibrate(context);
		}
		
		
		
		//устанавливаем текущий прогресс, в котором просим View обновиться
		public void setProgress(int index, float offset) {
			this.index = index;
			this.offset = offset;
			
			//покрасим все обычным,невыбранным цветом: textColor1
			for(int i=0;i<itemCount;i++){
				if(itemCount!=index){
					textView[i].setTextColor(textColor5);
					//textView[i].setBackgroundColor(textColor5);
				}
			}
			//ну а тот TextView, который соответствует выбранной странице, покрасим цветом textColor2
			textView[index].setTextColor(textColor2);
			//textView[index].setBackgroundColor(textColor4);
			invalidate();
		}
	}
	
	
	
	
	
	
	
	//класс с полосой
	public class Band extends View{
		float offset = 0;
		int index = 0;
		Paint paint;
		// Конструктор, необходимый для создания элемента внутри кода программы
		public Band() {
			super(context);
			init();
		}
		
		public void init(){
			paint = new Paint();
			paint.setColor(Color.parseColor("#424242"));//#353535
		}
		
		
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			//определю переменную для левого краешка
			float left=(index+ offset ) * getWidth() / itemCount;
			//определю переменную для правого краешка
			float right=(1 + index + offset) * getWidth() / itemCount;
			//рисуем прямоугольник
			//canvas.drawRect(left,top,right,bottom,paint);
			canvas.drawRect(left,0,right,getHeight(),paint);        
		}
		
		
		//устанавливаем текущий прогресс, в котором просим View обновиться
		public void setProgress(int index, float offset) {
			this.index = index;
			this.offset = offset;
			invalidate();
		}
	}
	
	
	
	
	
	
}

package com.earth.explorer.GPS.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;




public class PagerIndicatorView extends View {
    Paint paint;
	Paint paintCyan;
	Paint paint757575;
	Paint paint212121;
    int index = 0;
    float offset = 0;
	int itemCount=4;
	private Rect mTextBoundRect;
	String text = "1";
	int textWidth,textHeight;


    // Конструктор, необходимый для создания элемента внутри кода программы
    public PagerIndicatorView(Context context) {
        super(context);
		init();
    }

    // Конструктор, необходимый для наполнения элемента из файла с ресурсом
    public PagerIndicatorView (Context context, AttributeSet attrs) {
        super(context, attrs);
		init();
    }

    // Конструктор, необходимый для наполнения элемента из файла с ресурсом
    public PagerIndicatorView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
		init();
    }

    public void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#353535"));
		paintCyan = new Paint();
        paintCyan.setColor(Color.CYAN);
		paintCyan.setTextSize(15);
		paintCyan.setTextAlign(Paint.Align.CENTER);
		paint757575 = new Paint();
        paint757575.setColor(Color.parseColor("#757575"));
		paint757575.setAntiAlias(true);//сглаживание текста
		paint757575.setTextSize(14);
		paint757575.setTextAlign(Paint.Align.CENTER);
		paint212121 = new Paint();
        paint212121.setColor(Color.parseColor("#212121"));
		paint212121.setAntiAlias(true);//сглаживание текста
		paint212121.setTextSize(14);
		paint212121.setTextAlign(Paint.Align.CENTER);
		mTextBoundRect = new Rect();
		//paintCyan.getTextBounds(text, 0, text.length(), mTextBoundRect);
		paint757575.getTextBounds(text, 0, text.length(), mTextBoundRect);
        //textWidth = textBounds.width();
        // Используем measureText для измерения ширины
		// textWidth = paint.measureText(text);
        textHeight = mTextBoundRect.height();



    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


		/*
		 //метод drawRect рисует прямоугольник.
		 //конструктор метода drawRect:
		 canvas.drawRect(float left, 
		 float top,
		 float right,
		 float bottom,
		 Paint paint);
		 */

		//определю переменную для левого краешка
		float left=(index+ offset ) * getWidth() / itemCount;

		//определю переменную для правого краешка
		float right=(1 + index + offset) * getWidth() / itemCount;

        canvas.drawRect(left,//left
						0,                                      //top
						right,  //right
						getHeight(),                            //bottom
						paint);                                 //paint


	    //(right-left)/2 центр по ширине
		float centr=left + (right-left)/2f;


		//вписываем текст в прямоугольник:
		//canvas.drawText(index+1+"",centr , 6.5f+textHeight/2,paint212121);

    }

    public void setProgress(int index, float offset) {

        this.index = index;
        this.offset = offset;

        invalidate();
    }

	//сигнализируем из класса Element, метода onClick, что нужно кисточку менять
	public void colorPaint(boolean transparent){

		if(transparent){
			paint.setColor(Color.parseColor("#75404040"));
		}else{
			paint.setColor(Color.parseColor("#FF404040"));
		}


	}

}

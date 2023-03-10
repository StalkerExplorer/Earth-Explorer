package com.earth.explorer.GPS.View;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;




public class MyViewBasebandCn0DbHz extends View
{
	Rect myRect;
	private Rect mTextBoundRect = new Rect();
	int left,top,right,bottom;
	float width, height;
	float lengthRect;
	int size;
	Paint paint, paintLine, paintRed,paintOrange,paintYellow,paintGreen;
	String text="";
	int progress;



	public MyViewBasebandCn0DbHz(Context context) {
        super(context);
        init();
    }

    public MyViewBasebandCn0DbHz(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyViewBasebandCn0DbHz(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


	public void init()
	{

		myRect = new Rect();

		paint = new Paint();
		paint.setColor(Color.parseColor("#448AFF"));
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setSubpixelText(true);

		paintLine = new Paint();
		paintLine.setColor(Color.parseColor("#696969"));
		paintLine.setStyle(Paint.Style.FILL);
		paintLine.setAntiAlias(true);
		paintLine.setSubpixelText(true);
		paintLine.setStrokeWidth(1);

		paintRed = new Paint();
		paintRed.setColor(Color.parseColor("#FF0000"));
		paintRed.setStyle(Paint.Style.FILL);
		paintRed.setAntiAlias(true);
		paintRed.setSubpixelText(true);
		paintRed.setTextSize(20);

		paintOrange = new Paint();
		paintOrange.setColor(Color.parseColor("#FF5722"));
		paintOrange.setStyle(Paint.Style.FILL);
		paintOrange.setAntiAlias(true);
		paintOrange.setSubpixelText(true);
		paintOrange.setTextSize(20);
		
		paintYellow = new Paint();
		paintYellow.setColor(Color.parseColor("#FFC107"));
		paintYellow.setStyle(Paint.Style.FILL);
		paintYellow.setAntiAlias(true);
		paintYellow.setSubpixelText(true);
		paintYellow.setTextSize(20);

		paintGreen = new Paint();
		paintGreen.setColor(Color.parseColor("#00FF00"));
		paintGreen.setStyle(Paint.Style.FILL);
		paintGreen.setAntiAlias(true);
		paintGreen.setSubpixelText(true);
		paintGreen.setTextSize(20);
	}

	
	
	

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		//canvas.drawColor(Color.parseColor("#212121"));
		
		width = canvas. getWidth();
		height = canvas. getHeight();
		
		
      
		size=(int)height/5;//???????????? ???????????? ???????????????????????????? 

		left=(int)( Math.round(width/3.5f) );
		top=(int)height/2 + (-size/2);
		//right=(int)left;
		bottom=(int)height/2 + (size/2);
		right= (int)Math.round(progress) +left;
		lengthRect=width-left;//???????????? ?????????? ???????????? ????????????????????????????
		
		//???????????? ???????????????????? ?????????????? ?????????? ?? ???????????? ???????????? ??????????
		myRect.set(left,top,right+2,bottom);//right+2 ?????????? ?????? ???????? ?????????? ???????? right==0, ???????????????????? ?????????????? ?????????? ?? 2 ???????????? ??????????????

		//?????????? ???????????? ??????????
		canvas.drawLine(left,bottom,width-20,bottom,paintLine);

		//?? ????????????
		//???????????? ?????????????????????????? (???????? ?????????????????????????? ???????????????? ?????? ???????????????????? ??????????????????, ?????? ???????????? ???????????????????? right)
		canvas.drawRect(myRect, getPaint((progress)));


		text=""+progress;
		//?????????????? ???????????????? ???? ??????????????????????:
		float lenght= left;//?????????? ??????????????
		float textLenght = paintRed.measureText(text);//?????????? ???? ?????????????????? ????????????
		float x=(lenght-textLenght)/2;//?????????????????? ?????????? ?????? ???????????? ????????????, ?????????? ?????????? ???????????????? ????????????????????
		//float x=0;

		//?????????????? ???????????????? ???? ??????????????????:
        paint.getTextBounds(text, 0, text.length(), mTextBoundRect);
        float textHeight = mTextBoundRect.height();//???????????? ????????????
		float y=height/2 + textHeight/2;//?????????????????? ?????????? ?????? ???????????? ???????????? ???? ?????? y, ?????????? ?????????? ???????????????? ????????????????????

		canvas.drawText(text,x,y,getPaint(progress));
        

	}




	public void setProgress(int progress){
		this.progress=progress;
		invalidate();
	}

	


	public Paint getPaint(float Cn0DbHz){


		if((Cn0DbHz==0)){
			paint=paintRed;
		}

		if((Cn0DbHz>0)&(Cn0DbHz<20)){
			paint=paintOrange;
		}

		if((Cn0DbHz>=20)&(Cn0DbHz<30)){
			paint=paintYellow;
		}

		if((Cn0DbHz>=30)){
			paint=paintGreen;
		}

		return paint;

	}



}

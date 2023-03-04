package com.earth.explorer.Util;


//класс с ToneGenerator
import android.media.AudioManager;
import android.media.ToneGenerator;

public class Tone
{
	static ToneGenerator toneGenerator;
	static int toneType;
	static int durationMs;
    public static boolean toneBoolean;


	public Tone(){
		toneBoolean=true;
		toneType=ToneGenerator.TONE_PROP_BEEP;
		durationMs=300;
		int streamType = AudioManager.STREAM_MUSIC;
		int volume = ToneGenerator.MAX_VOLUME;
		toneGenerator = new ToneGenerator(streamType, volume);
	}

	public Tone(int tone, int duration){
		toneBoolean=true;
		toneType=tone;
		durationMs=duration;
		int streamType = AudioManager.STREAM_MUSIC;
		int volume = ToneGenerator.MAX_VOLUME;
		toneGenerator = new ToneGenerator(streamType, volume);
	}

	public static void tone(){//сам тональник, который пищит
		if(toneBoolean){
			toneGenerator.startTone(toneType, durationMs);
		}
	}

	
	
	public static void toneFirstFix(){//сам тональник, который пищит
		if(toneBoolean){
			toneType=ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK;
			toneGenerator.startTone(toneType, durationMs);
		}
	}
	
	
	
	public static void toneStopped(){//сам тональник, который пищит
		if(toneBoolean){
			toneType=ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD;
			toneGenerator.startTone(toneType, durationMs);
		}
	}
	
	
	
	public static void toneStarted(){//сам тональник, который пищит
		if(toneBoolean){   
			toneType=ToneGenerator.TONE_PROP_ACK;
			toneGenerator.startTone(toneType, durationMs);
		}
	}
	
	
	public static void toneError(){//сам тональник, который пищит
		if(toneBoolean){
			toneType=ToneGenerator.TONE_SUP_ERROR;
			toneGenerator.startTone(toneType, durationMs);
		}
	}
	
	//TONE_CDMA_SOFT_ERROR_LITE
	
	public void setToneType(int tone){//установим звук
		toneType=tone;
	}

	public void setDuration(int duration){//установим длительность
		durationMs=duration;
	}
}
	
	

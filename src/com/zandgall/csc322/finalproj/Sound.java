package com.zandgall.csc322.finalproj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import com.jogamp.openal.*;
import com.jogamp.openal.util.*;

public class Sound implements Serializable {

	public static final float DEFAULT_SMOOTHING = (1.0f / 16.0f) / (130.0f / 60.f);

	private static AL al;

	static {
		try {
			ALut.alutInit();
			al = ALFactory.getAL();
			al.alGetError();
		} catch(ALException e) {
			e.printStackTrace();
		}
	}

	public static Sound
		Noise = new Sound("/sound/noise.wav"), // Constant
		Wind = new Sound("/sound/wind.wav"), // Used for trees
		Piano = new Sound("/sound/piano.wav"),
		EPiano = new Sound("/sound/epiano.wav"),
		Drums = new Sound("/sound/drums.wav"),
		Plorp = new Sound("/sound/plorp.wav"),
		BossDrums = new Sound("/sound/bossDrums.wav"),
		BossEPiano = new Sound("/sound/bossEPiano.wav"),
		BossBass = new Sound("/sound/bossBass.wav"),
		BossGuitar = new Sound("/sound/bossGuitar.wav"),
		BossCymbals = new Sound("/sound/bossCymbals.wav"),
		EndIt = new Sound("/sound/endit.wav"),
		TheKill = new Sound("/sound/thekill.wav"),
		Heaven = new Sound("/sound/heaven.wav"),
		EffectPluck = new Sound("/sound/pluck.wav"),
		EffectBonk = new Sound("/sound/bonk.wav");

	private static double Timer = 0;

	protected int source, charges = 0;

	protected float volume = 0.0f, targetVolume = 0.0f;
	protected float smoothing = DEFAULT_SMOOTHING;

	public Sound(String resource) {
		int[] buffer = new int[1];
		int[] source = new int[1];

		int[] format = new int[1];
		int[] size = new int[1];
		ByteBuffer[] data = new ByteBuffer[1];
		int[] freq = new int[1];
		int[] loop = new int[1];
		al.alGenBuffers(1, buffer, 0);
		ALut.alutLoadWAVFile(Sound.class.getResourceAsStream(resource), format, data, size, freq, loop);
		al.alBufferData(buffer[0], format[0], data[0], size[0], freq[0]);

		al.alGenSources(1, source, 0);
		al.alSourcei(source[0], AL.AL_BUFFER, buffer[0]);
		al.alSourcei(source[0], AL.AL_LOOPING, 1);
		al.alSourcef(source[0], AL.AL_GAIN, 0.0f);
		this.source = source[0];
		// al.alSourcePlay(this.source);
	}

	public static void init() {
		Noise.fadeTo(1.0f);
		Noise.setVolume(1.0f);
		Piano.fadeTo(1.f);
		Wind.setSmoothing(DEFAULT_SMOOTHING * 16);
		Plorp.setSmoothing(DEFAULT_SMOOTHING * 16);

		Noise.play();
		Wind.play();
		Piano.play();
		EPiano.play();
		Drums.play();
		Plorp.play();
		BossDrums.play();
		BossEPiano.play();
		BossBass.play();
		BossGuitar.play();
		BossCymbals.play();
		EndIt.play();
		Heaven.play();

		TheKill.setVolume(1.f);
		TheKill.fadeTo(1.f);
		TheKill.stopLooping();

		EffectBonk.setVolume(1.f);
		EffectBonk.fadeTo(1.f);
		EffectBonk.stopLooping();
		EffectPluck.setVolume(1.f);
		EffectPluck.fadeTo(1.f);
		EffectPluck.stopLooping();
	}

	private void tick() {
		if(Math.abs(volume - targetVolume) > smoothing * Main.TIMESTEP)
			volume +=smoothing * Main.TIMESTEP * Math.signum(targetVolume - volume);
		al.alSourcef(source, AL.AL_GAIN, volume);

		double pTimer = Timer;
		Timer += Main.TIMESTEP * 60.f / 130.f;
		if(Math.floor(Timer) != Math.floor(pTimer)) {
			if(EffectBonk.charges > 0) {
				EffectBonk.play();
				EffectBonk.charges--;
			}
			if(EffectPluck.charges > 0) {
				EffectPluck.play();
				EffectPluck.charges--;
			}
		}
	}

	public void fadeTo(float target) {
		targetVolume = target;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		al.alSourcef(source, AL.AL_GAIN, volume);
	}

	public float getVolume() {
		return volume;
	}

	public void setMinVolume(float minVolume) {
		if(volume < minVolume)
			setVolume(minVolume);
	}

	public void setSmoothing(float smoothing) {
		this.smoothing = smoothing;
	}

	public void play() {
		al.alSourcePlay(source);
	}

	public void stopLooping() {
		al.alSourcei(source, AL.AL_LOOPING, 0);
	}

	public void charge() {
		charges++;
	}

	public static void update() {
		Noise.tick();
		Wind.tick();
		Piano.tick();
		EPiano.tick();
		Drums.tick();
		Plorp.tick();
		BossDrums.tick();
		BossEPiano.tick();
		BossBass.tick();
		BossGuitar.tick();
		BossCymbals.tick();
		EndIt.tick();
		Heaven.tick();
		TheKill.tick();
	}

	public static void save(ObjectOutputStream out) throws IOException {
		out.writeObject(Noise);
		out.writeObject(Wind);
		out.writeObject(Piano);
		out.writeObject(EPiano);
		out.writeObject(Drums);
		out.writeObject(Plorp);
		out.writeObject(BossDrums);
		out.writeObject(BossEPiano);
		out.writeObject(BossBass);
		out.writeObject(BossGuitar);
		out.writeObject(BossCymbals);
		out.writeObject(EndIt);
		out.writeObject(TheKill);
		out.writeObject(Heaven);
		out.writeObject(EffectPluck);
		out.writeObject(EffectBonk);
	}

	public static void load(ObjectInputStream in) throws IOException {
		try {
			Noise = (Sound)in.readObject();
			Wind = (Sound)in.readObject();
			Piano = (Sound)in.readObject();
			EPiano = (Sound)in.readObject();
			Drums = (Sound)in.readObject();
			Plorp = (Sound)in.readObject();
			BossDrums = (Sound)in.readObject();
			BossEPiano = (Sound)in.readObject();
			BossBass = (Sound)in.readObject();
			BossGuitar = (Sound)in.readObject();
			BossCymbals = (Sound)in.readObject();
			EndIt = (Sound)in.readObject();
			TheKill = (Sound)in.readObject();
			Heaven = (Sound)in.readObject();
			EffectPluck = (Sound)in.readObject();
			EffectBonk = (Sound)in.readObject();
		} catch(ClassNotFoundException e) {
			System.err.println("Couldn't load sound data");
			e.printStackTrace();
		}
	}
}

package com.zandgall.csc322.finalproj;

import java.io.InputStream;
import java.net.URISyntaxException;

import java.nio.ByteBuffer;

import com.jogamp.openal.*;
import com.jogamp.openal.util.*;

import javafx.util.Duration;

public class Sound {

	public static final float SMOOTHING = (1.0f / 16.0f) / (130.0f / 60.f);

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

	public static final Sound
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
		BossCymbals = new Sound("/sound/bossCymbals.wav");

	protected int source;

	protected float volume = 0.0f, targetVolume = 0.0f;

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
		al.alSourcePlay(this.source);
	}

	public static void init() {
		Noise.fadeTo(1.0f);
		Noise.setVolume(1.0f);
		Piano.fadeTo(1.f);
	}

	private void tick() {
		if(Math.abs(volume - targetVolume) > SMOOTHING * Main.TIMESTEP)
			volume += SMOOTHING * Main.TIMESTEP * Math.signum(targetVolume - volume);
		al.alSourcef(source, AL.AL_GAIN, volume);
	}

	public void fadeTo(float target) {
		targetVolume = target;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		al.alSourcef(source, AL.AL_GAIN, volume);
	}

	public void setMinVolume(float minVolume) {
		if(volume < minVolume)
			setVolume(minVolume);
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
	}
}

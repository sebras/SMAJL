/*
 * SourceDataLineSoundGenerator
 *
 * Copyright (C) 2016  Christian Brunschen
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package com.brunschen.christian.smil.sound;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * @author Christian Brunschen
 */
public class SourceDataLineSoundGenerator extends SoundGenerator {  
  private static final float[] sampleRates = new float[] { 48000.0f, 44100.0f, 32000.0f, 22500.0f, 16000.0f, 11250.0f, 8000.0f };
  private static final int[] sampleSizes = new int[] { 16, 8 };
  private static final int[] channelNumbers = new int[] { 1, 2 };
  private static final boolean[] signedOptions = new boolean[] { true, false };
  private static final boolean[] bigEndianOptions = new boolean[] { false, true };

  private AudioFormat format;
  private SourceDataLine sourceDataLine = null;
  private Lock dataLineLock = new ReentrantLock();

  public static Collection<AudioFormat> supportedFormats() {
    Collection<AudioFormat> formats = new LinkedList<AudioFormat>();
    for (float s : sampleRates) {
      for (int n : sampleSizes) {
        for (int c : channelNumbers) {
          for (boolean sig : signedOptions) {
            for (boolean big : bigEndianOptions) {
              AudioFormat f = new AudioFormat(s, n, c, sig, big);
              DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, f);
              if (AudioSystem.isLineSupported(dataLineInfo)) {
                // System.err.format("%s%n", f);
                formats.add(f);
              }
            }
          }
        }
      }
    }
    return formats;
  }
  
  public static AudioFormat findFormat() {
    Collection<AudioFormat> formats = supportedFormats();
    for (AudioFormat format : formats) {
      try {
        SourceDataLine line = AudioSystem.getSourceDataLine(format);
        line.open(format, format.getFrameSize() * 128);
        line.close();
        return format;
      } catch (LineUnavailableException e) {
        System.err.format("! %s%n", format);
      }
    }
    return null;
  }

  public SourceDataLineSoundGenerator() {
    try {
      format = findFormat();
      if (format == null) {
        System.err.format("Cannot find suitable audio format, sorry\n");
      } else {
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        if (AudioSystem.isLineSupported(dataLineInfo)) {
          dataLineLock.lock();
          try {
            sourceDataLine = AudioSystem.getSourceDataLine(format);
            sourceDataLine.addLineListener(lineListener());
          } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
          } finally {
            dataLineLock.unlock();
          }

          sampleRate = format.getSampleRate();
          samplesPerTenthSecond = (int) (sampleRate / 10);
          bitsPerSample = format.getSampleSizeInBits();
          channels = format.getChannels();
          signed = format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED;
          bigEndian = format.isBigEndian();

          bytesPerSample = bitsPerSample >> 3;
          bytesPerFrame = bytesPerSample * channels;
          amp = (1 << bitsPerSample - 1) - 1;
          mid = signed ? 0 : amp + 1;

          setBufferLengthMillis(50);

//          System.err.format(
//              "found source data line with %f samples per second, %d bits per sample, %d channels, %s-endian, encoding %s, %d ms buffer size\n",
//              sampleRate, bitsPerSample, channels, (bigEndian ? "big" : "little"), format.getEncoding(),
//              bufferLengthMillis);
        } else {
          System.err.format("Cannot generate sound, sorry\n");
        }
      }
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  public boolean canGenerateSound() {
    dataLineLock.lock();
    try {
      return sourceDataLine != null;
    } finally {
      dataLineLock.unlock();
    }
  }

  public SourceDataLineBuffer makeBuffer(int capacity) {
    return SourceDataLineBuffer.overwritingBuffer(capacity);
  }

  public void pushBufferToDestination() {
    // read past frames from the buffer and write to the sourceDataLine
    // int nRead = 0;
    dataLineLock.lock();
    try {
      // nRead = 
      ((SourceDataLineBuffer) buffer).read(sourceDataLine);
    } finally {
      dataLineLock.unlock();
    }
      
//    System.err.format("moved %d bytes from buffer to dataLine, %d remaining in buffer\n",
//        nRead, buffer.size());
  }
  
  public void startDestination() {
    dataLineLock.lock();
    try {
      sourceDataLine.open(format, bufferSizeBytes);
      sourceDataLine.start();
    } catch (LineUnavailableException e) {
      throw new RuntimeException(e);
    } finally {
      dataLineLock.unlock();
    }
  }
  
  public void stopDestination(boolean finishPlaying, boolean retainData) {
    dataLineLock.lock();
    try {
      if (finishPlaying) {
        sourceDataLine.drain();
      } 
      sourceDataLine.stop();
      if (!retainData) {
        sourceDataLine.flush();
      }
      sourceDataLine.close();
    } finally {
      dataLineLock.unlock();
    }
//    System.err.format("%s: SourceDataLineSoundGenerator stopped\n", java.lang.Thread.currentThread().getName());
  }


  public LineListener lineListener() {
    return new LineListener() {
      public void update(LineEvent e) {
//        System.err.format("received line event '%s'\n", e);
        if (e.getType() == LineEvent.Type.STOP) {
//          System.err.format("Data Line Underrun!\n");
        }
      }
    };
  }
}

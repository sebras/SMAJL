/*
 * SourceDataLineBuffer
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

import javax.sound.sampled.SourceDataLine;

/**
 * @author Christian Brunschen
 */
public class SourceDataLineBuffer extends CircularByteBuffer {
  
  public static SourceDataLineBuffer extendingBuffer(int capacity) {
    return new SourceDataLineBuffer(capacity, OnOverrun.EXTEND);
  }
    
  public static SourceDataLineBuffer overwritingBuffer(int capacity) {
    return new SourceDataLineBuffer(capacity, OnOverrun.OVERWRITE);
  }
    
  public static SourceDataLineBuffer throwingBuffer(int capacity) {
    return new SourceDataLineBuffer(capacity, OnOverrun.THROW);
  }
  
  public SourceDataLineBuffer(int capacity, OnOverrun defaultOnOverrun) {
    super(capacity, defaultOnOverrun);
  }
  
  public SourceDataLineBuffer(int capacity) {
    this(capacity, OnOverrun.EXTEND);
  }

  public synchronized int read(SourceDataLine sourceDataLine) {
    int len = sourceDataLine.available();
    
    if (nItems == 0) {
      return 0;
    }
    
    if (next > first) {
      int nAvailable = next - first;
      int n = nAvailable < len ? nAvailable : len;
      sourceDataLine.write(items, first, n);
      first += n;
      nItems -= n;
      return n;
    } else {
      // first batch - from 'first' to end of buffer
      int nAvailable = capacity - first;
      int n = nAvailable < len ? nAvailable : len;
      sourceDataLine.write(items, first, n);
      first = (first + n) % capacity;
      nItems -= n;

      // if necessary, second batch - from 0 to next - using a recursive call
      if (len > n) {
        return n + read(sourceDataLine);
      } else {
        return n;
      }
    }
  }
}

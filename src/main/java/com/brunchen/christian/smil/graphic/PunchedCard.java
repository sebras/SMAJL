package com.brunschen.christian.graphic;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PunchedCard extends AbstractGraphic {
  
  private static final Charset LATIN1 = Charset.forName("ISO-8859-1"); 
  private static final CharsetEncoder LATIN_1_ENCODER = LATIN1.newEncoder();
  private static final byte PRINT_CHARS[][] = {
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x7d, 0x00, 0x00 },
    { 0x00, 0x00, 0x60, 0x00, 0x60 },
    { 0x14, 0x77, 0x00, 0x77, 0x14 },
    { 0x12, 0x2a, 0x6b, 0x2a, 0x24 },
    { 0x63, 0x64, 0x08, 0x13, 0x63 },
    { 0x36, 0x49, 0x35, 0x02, 0x05 },
    { 0x00, 0x00, 0x60, 0x60, 0x00 },
    { 0x00, 0x1c, 0x22, 0x41, 0x00 },
    { 0x00, 0x41, 0x22, 0x1c, 0x00 },
    { 0x54, 0x38, 0x7c, 0x38, 0x54 },
    { 0x08, 0x08, 0x3e, 0x08, 0x08 },
    { 0x00, 0x00, 0x0d, 0x0e, 0x00 },
    { 0x04, 0x04, 0x04, 0x04, 0x04 },
    { 0x00, 0x00, 0x03, 0x03, 0x00 },
    { 0x02, 0x04, 0x08, 0x10, 0x20 },
    { 0x3e, 0x41, 0x41, 0x3e, 0x00 },
    { 0x00, 0x21, 0x7f, 0x01, 0x00 },
    { 0x27, 0x49, 0x49, 0x49, 0x31 },
    { 0x22, 0x41, 0x49, 0x49, 0x36 },
    { 0x0c, 0x14, 0x24, 0x7f, 0x04 },
    { 0x72, 0x51, 0x51, 0x51, 0x4e },
    { 0x1e, 0x29, 0x49, 0x49, 0x46 },
    { 0x40, 0x47, 0x48, 0x50, 0x60 },
    { 0x36, 0x49, 0x49, 0x49, 0x36 },
    { 0x30, 0x49, 0x49, 0x4a, 0x3c },
    { 0x00, 0x00, 0x36, 0x36, 0x00 },
    { 0x00, 0x00, 0x6d, 0x6e, 0x00 },
    { 0x08, 0x14, 0x22, 0x41, 0x00 },
    { 0x14, 0x14, 0x14, 0x14, 0x14 },
    { 0x41, 0x22, 0x14, 0x08, 0x00 },
    { 0x20, 0x40, 0x4d, 0x30, 0x00 },
    { 0x26, 0x49, 0x4f, 0x41, 0x3e },
    { 0x1f, 0x24, 0x44, 0x24, 0x1f },
    { 0x41, 0x7f, 0x49, 0x49, 0x36 },
    { 0x3e, 0x41, 0x41, 0x41, 0x22 },
    { 0x41, 0x7f, 0x41, 0x41, 0x3e },
    { 0x7f, 0x49, 0x49, 0x41, 0x41 },
    { 0x7f, 0x48, 0x48, 0x40, 0x40 },
    { 0x3e, 0x41, 0x41, 0x49, 0x4f },
    { 0x7f, 0x08, 0x08, 0x08, 0x7f },
    { 0x00, 0x41, 0x7f, 0x41, 0x00 },
    { 0x02, 0x01, 0x01, 0x01, 0x7e },
    { 0x7f, 0x08, 0x14, 0x22, 0x41 },
    { 0x7f, 0x01, 0x01, 0x01, 0x01 },
    { 0x7f, 0x20, 0x18, 0x20, 0x7f },
    { 0x7f, 0x20, 0x10, 0x08, 0x7f },
    { 0x3e, 0x41, 0x41, 0x41, 0x3e },
    { 0x7f, 0x48, 0x48, 0x48, 0x30 },
    { 0x3e, 0x41, 0x45, 0x42, 0x3d },
    { 0x7f, 0x48, 0x4c, 0x4a, 0x31 },
    { 0x22, 0x51, 0x49, 0x45, 0x22 },
    { 0x40, 0x40, 0x7f, 0x40, 0x40 },
    { 0x7e, 0x01, 0x01, 0x01, 0x7e },
    { 0x70, 0x0c, 0x03, 0x0c, 0x70 },
    { 0x7f, 0x02, 0x04, 0x02, 0x7f },
    { 0x63, 0x14, 0x08, 0x14, 0x63 },
    { 0x60, 0x10, 0x0f, 0x10, 0x60 },
    { 0x43, 0x45, 0x49, 0x51, 0x61 },
    { 0x00, 0x7f, 0x41, 0x41, 0x00 },
    { 0x20, 0x10, 0x08, 0x04, 0x02 },
    { 0x00, 0x41, 0x41, 0x7f, 0x00 },
    { 0x10, 0x20, 0x40, 0x20, 0x10 },
    { 0x01, 0x01, 0x01, 0x01, 0x01 },
    { 0x00, 0x00, 0x40, 0x20, 0x00 },
    { 0x02, 0x15, 0x15, 0x15, 0x0f },
    { 0x7f, 0x11, 0x11, 0x11, 0x0e },
    { 0x0e, 0x11, 0x11, 0x11, 0x11 },
    { 0x0e, 0x11, 0x11, 0x11, 0x7f },
    { 0x0e, 0x15, 0x15, 0x15, 0x09 },
    { 0x08, 0x3f, 0x48, 0x20, 0x00 },
    { 0x09, 0x15, 0x15, 0x15, 0x0e },
    { 0x7f, 0x08, 0x10, 0x10, 0x0f },
    { 0x00, 0x11, 0x5f, 0x01, 0x00 },
    { 0x02, 0x01, 0x01, 0x5e, 0x00 },
    { 0x7f, 0x04, 0x04, 0x0a, 0x11 },
    { 0x00, 0x7e, 0x01, 0x01, 0x02 },
    { 0x1f, 0x10, 0x0c, 0x10, 0x0f },
    { 0x1f, 0x08, 0x10, 0x10, 0x0f },
    { 0x0e, 0x11, 0x11, 0x11, 0x0e },
    { 0x1f, 0x12, 0x12, 0x12, 0x0c },
    { 0x0c, 0x12, 0x12, 0x12, 0x1f },
    { 0x1f, 0x08, 0x10, 0x10, 0x08 },
    { 0x09, 0x15, 0x15, 0x15, 0x12 },
    { 0x10, 0x7e, 0x11, 0x11, 0x02 },
    { 0x1e, 0x01, 0x01, 0x02, 0x1f },
    { 0x1c, 0x02, 0x01, 0x02, 0x1c },
    { 0x1e, 0x01, 0x02, 0x01, 0x1e },
    { 0x11, 0x0a, 0x04, 0x0a, 0x11 },
    { 0x18, 0x05, 0x02, 0x04, 0x18 },
    { 0x11, 0x13, 0x15, 0x19, 0x11 },
    { 0x08, 0x08, 0x36, 0x41, 0x41 },
    { 0x00, 0x00, 0x7f, 0x00, 0x00 },
    { 0x41, 0x41, 0x36, 0x08, 0x08 },
    { 0x08, 0x10, 0x08, 0x04, 0x08 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00 },
    { 0x00, 0x00, 0x5f, 0x00, 0x00 },
    { 0x1c, 0x22, 0x63, 0x22, 0x00 },
    { 0x09, 0x3e, 0x49, 0x21, 0x02 },
    { 0x22, 0x1c, 0x14, 0x1c, 0x22 },
    { 0x48, 0x28, 0x1f, 0x28, 0x48 },
    { 0x00, 0x00, 0x77, 0x00, 0x00 },
    { 0x00, 0x29, 0x55, 0x4a, 0x00 },
    { 0x00, 0x40, 0x00, 0x40, 0x00 },
    { 0x1c, 0x2a, 0x36, 0x22, 0x1c },
    { 0x00, 0x3a, 0x2a, 0x1a, 0x00 },
    { 0x08, 0x14, 0x00, 0x08, 0x14 },
    { 0x04, 0x04, 0x04, 0x04, 0x07 },
    { 0x00, 0x04, 0x04, 0x04, 0x00 },
    { 0x1c, 0x22, 0x3e, 0x2a, 0x1c },
    { 0x00, 0x40, 0x40, 0x40, 0x00 },
    { 0x00, 0x70, 0x50, 0x70, 0x00 },
    { 0x11, 0x11, 0x7d, 0x11, 0x11 },
    { 0x00, 0x4c, 0x54, 0x24, 0x00 },
    { 0x00, 0x54, 0x54, 0x28, 0x00 },
    { 0x00, 0x00, 0x20, 0x40, 0x00 },
    { 0x0f, 0x02, 0x02, 0x02, 0x0c },
    { 0x30, 0x78, 0x7f, 0x00, 0x7f },
    { 0x00, 0x08, 0x1c, 0x08, 0x00 },
    { 0x00, 0x01, 0x05, 0x03, 0x00 },
    { 0x00, 0x24, 0x7c, 0x04, 0x00 },
    { 0x00, 0x09, 0x15, 0x09, 0x00 },
    { 0x14, 0x08, 0x00, 0x14, 0x08 },
    { 0x78, 0x00, 0x02, 0x06, 0x0b },
    { 0x78, 0x00, 0x09, 0x0b, 0x05 },
    { 0x48, 0x78, 0x32, 0x06, 0x0b },
    { 0x06, 0x59, 0x01, 0x02, 0x00 },
    { 0x0f, 0x52, 0x32, 0x12, 0x0f },
    { 0x0f, 0x12, 0x32, 0x52, 0x0f },
    { 0x0f, 0x32, 0x52, 0x32, 0x0f },
    { 0x2f, 0x52, 0x32, 0x52, 0x0f },
    { 0x0f, 0x52, 0x12, 0x52, 0x0f },
    { 0x0f, 0x12, 0x52, 0x12, 0x0f },
    { 0x3f, 0x44, 0x7f, 0x49, 0x09 },
    { 0x38, 0x45, 0x45, 0x47, 0x28 },
    { 0x1f, 0x55, 0x35, 0x11, 0x11 },
    { 0x1f, 0x15, 0x35, 0x51, 0x11 },
    { 0x1f, 0x35, 0x55, 0x31, 0x11 },
    { 0x1f, 0x55, 0x15, 0x51, 0x11 },
    { 0x00, 0x51, 0x3f, 0x11, 0x00 },
    { 0x00, 0x11, 0x3f, 0x51, 0x00 },
    { 0x00, 0x31, 0x5f, 0x31, 0x00 },
    { 0x00, 0x51, 0x1f, 0x51, 0x00 },
    { 0x08, 0x7f, 0x49, 0x41, 0x3e },
    { 0x3f, 0x48, 0x24, 0x42, 0x1f },
    { 0x0e, 0x51, 0x31, 0x11, 0x0e },
    { 0x0e, 0x11, 0x31, 0x51, 0x0e },
    { 0x0e, 0x31, 0x51, 0x31, 0x0e },
    { 0x2e, 0x51, 0x31, 0x51, 0x0e },
    { 0x0e, 0x51, 0x11, 0x51, 0x0e },
    { 0x22, 0x14, 0x08, 0x14, 0x22 },
    { 0x1d, 0x26, 0x2a, 0x32, 0x5c },
    { 0x1e, 0x41, 0x21, 0x01, 0x1e },
    { 0x1e, 0x01, 0x21, 0x41, 0x1e },
    { 0x1e, 0x21, 0x41, 0x21, 0x1e },
    { 0x1e, 0x41, 0x01, 0x41, 0x1e },
    { 0x10, 0x08, 0x27, 0x48, 0x10 },
    { 0x7f, 0x14, 0x14, 0x08, 0x00 },
    { 0x3f, 0x4a, 0x34, 0x00, 0x00 },
    { 0x03, 0x4b, 0x2b, 0x0b, 0x07 },
    { 0x03, 0x0b, 0x2b, 0x4b, 0x07 },
    { 0x03, 0x2b, 0x4b, 0x2b, 0x07 },
    { 0x23, 0x4b, 0x2b, 0x4b, 0x07 },
    { 0x03, 0x2b, 0x0b, 0x2b, 0x07 },
    { 0x03, 0x0b, 0x2b, 0x0b, 0x07 },
    { 0x0b, 0x0b, 0x0f, 0x0d, 0x0d },
    { 0x04, 0x0a, 0x0b, 0x0b, 0x0a },
    { 0x0e, 0x4d, 0x2d, 0x0d, 0x0d },
    { 0x0e, 0x0d, 0x2d, 0x4d, 0x0d },
    { 0x0e, 0x2d, 0x4d, 0x2d, 0x0d },
    { 0x0e, 0x2d, 0x4d, 0x2d, 0x0d },
    { 0x00, 0x49, 0x2f, 0x01, 0x00 },
    { 0x00, 0x09, 0x2f, 0x41, 0x00 },
    { 0x00, 0x29, 0x4f, 0x21, 0x00 },
    { 0x00, 0x29, 0x0f, 0x21, 0x00 },
    { 0x56, 0x29, 0x59, 0x0e, 0x00 },
    { 0x2f, 0x48, 0x28, 0x48, 0x07 },
    { 0x06, 0x49, 0x29, 0x09, 0x06 },
    { 0x06, 0x09, 0x29, 0x49, 0x06 },
    { 0x06, 0x29, 0x49, 0x29, 0x06 },
    { 0x26, 0x49, 0x29, 0x49, 0x06 },
    { 0x06, 0x29, 0x09, 0x29, 0x06 },
    { 0x08, 0x08, 0x2a, 0x08, 0x08 },
    { 0x0d, 0x12, 0x15, 0x09, 0x16 },
    { 0x0e, 0x41, 0x21, 0x01, 0x0f },
    { 0x0e, 0x01, 0x21, 0x41, 0x0f },
    { 0x0e, 0x21, 0x41, 0x21, 0x0f },
    { 0x0e, 0x21, 0x01, 0x21, 0x0f },
    { 0x09, 0x05, 0x25, 0x45, 0x0e },
    { 0x3e, 0x14, 0x14, 0x08, 0x00 },
    { 0x09, 0x25, 0x05, 0x25, 0x0e },
  };

  private static final Ellipse PRINT_ELLIPSES[][] = new Ellipse[PRINT_CHARS.length][];
  
  private static final Color INK = Color.hex(20, 20, 20, 185);
  private static final Color PRINT = Color.hex(20, 20, 160, 225);

  public static final Color CREAM = Color.hex(240, 227, 190);
  public static final Color BLUE = Color.hex(142, 167, 172);
  public static final Color PINK = Color.hex(240, 176, 160);
  public static final Color RED = Color.hex(250, 100, 130);
  
  /**
   * Full mapping of 8-bit bytes to 12-row card punches.
   * <p>Can be used to map ASCII (low 7 bits), any of the 8-bit ISO character sets,
   * or in theory any other 8-bit-wide data, including pure binary, or UTF-8-encoded
   * Unicode characters. In 
   * 
   * @see {@link http://www.ecma-international.org/publications/files/ECMA-ST-WITHDRAWN/ECMA-44,%201st%20Edition,%20September%201975.pdf}
   */
  
  static final short ECMA_44_PUNCHES[] = {
      05403, /* 00 : 12 0 9 8 1 */
      04401, /* 01 : 12 9 1 */
      04201, /* 02 : 12 9 2 */
      04101, /* 03 : 12 9 3 */
      00005, /* 04 : 9 7 */
      01023, /* 05 : 0 9 8 5 */
      01013, /* 06 : 0 9 8 6 */
      01007, /* 07 : 0 9 8 7 */
      02011, /* 08 : 11 9 6 */
      04021, /* 09 : 12 9 5 */
      01021, /* 0a : 0 9 5 */
      04103, /* 0b : 12 9 8 3 */
      04043, /* 0c : 12 9 8 4 */
      04023, /* 0d : 12 9 8 5 */
      04013, /* 0e : 12 9 8 6 */
      04007, /* 0f : 12 9 8 7 */
      06403, /* 10 : 12 11 9 8 1 */
      02401, /* 11 : 11 9 1 */
      02201, /* 12 : 11 9 2 */
      02101, /* 13 : 11 9 3 */
      00043, /* 14 : 9 8 4 */
      00023, /* 15 : 9 8 5 */
      00201, /* 16 : 9 2 */
      01011, /* 17 : 0 9 6 */
      02003, /* 18 : 11 9 8 */
      02403, /* 19 : 11 9 8 1 */
      00007, /* 1a : 9 8 7 */
      01005, /* 1b : 0 9 7 */
      02043, /* 1c : 11 9 8 4 */
      02023, /* 1d : 11 9 8 5 */
      02013, /* 1e : 11 9 8 6 */
      02007, /* 1f : 11 9 8 7 */
      00000, /* 20 :  */
      04006, /* 21 : 12 8 7 */
      00006, /* 22 : 8 7 */
      00102, /* 23 : 8 3 */
      02102, /* 24 : 11 8 3 */
      01042, /* 25 : 0 8 4 */
      04000, /* 26 : 12 */
      00022, /* 27 : 8 5 */
      04022, /* 28 : 12 8 5 */
      02022, /* 29 : 11 8 5 */
      02042, /* 2a : 11 8 4 */
      04012, /* 2b : 12 8 6 */
      01102, /* 2c : 0 8 3 */
      02000, /* 2d : 11 */
      04102, /* 2e : 12 8 3 */
      01400, /* 2f : 0 1 */
      01000, /* 30 : 0 */
      00400, /* 31 : 1 */
      00200, /* 32 : 2 */
      00100, /* 33 : 3 */
      00040, /* 34 : 4 */
      00020, /* 35 : 5 */
      00010, /* 36 : 6 */
      00004, /* 37 : 7 */
      00002, /* 38 : 8 */
      00001, /* 39 : 9 */
      00202, /* 3a : 8 2 */
      02012, /* 3b : 11 8 6 */
      04042, /* 3c : 12 8 4 */
      00012, /* 3d : 8 6 */
      01012, /* 3e : 0 8 6 */
      01006, /* 3f : 0 8 7 */
      00042, /* 40 : 8 4 */
      04400, /* 41 : 12 1 */
      04200, /* 42 : 12 2 */
      04100, /* 43 : 12 3 */
      04040, /* 44 : 12 4 */
      04020, /* 45 : 12 5 */
      04010, /* 46 : 12 6 */
      04004, /* 47 : 12 7 */
      04002, /* 48 : 12 8 */
      04001, /* 49 : 12 9 */
      02400, /* 4a : 11 1 */
      02200, /* 4b : 11 2 */
      02100, /* 4c : 11 3 */
      02040, /* 4d : 11 4 */
      02020, /* 4e : 11 5 */
      02010, /* 4f : 11 6 */
      02004, /* 50 : 11 7 */
      02002, /* 51 : 11 8 */
      02001, /* 52 : 11 9 */
      01200, /* 53 : 0 2 */
      01100, /* 54 : 0 3 */
      01040, /* 55 : 0 4 */
      01020, /* 56 : 0 5 */
      01010, /* 57 : 0 6 */
      01004, /* 58 : 0 7 */
      01002, /* 59 : 0 8 */
      01001, /* 5a : 0 9 */
      04202, /* 5b : 12 8 2 */
      01202, /* 5c : 0 8 2 */
      02202, /* 5d : 11 8 2 */
      02006, /* 5e : 11 8 7 */
      01022, /* 5f : 0 8 5 */
      00402, /* 60 : 8 1 */
      05400, /* 61 : 12 0 1 */
      05200, /* 62 : 12 0 2 */
      05100, /* 63 : 12 0 3 */
      05040, /* 64 : 12 0 4 */
      05020, /* 65 : 12 0 5 */
      05010, /* 66 : 12 0 6 */
      05004, /* 67 : 12 0 7 */
      05002, /* 68 : 12 0 8 */
      05001, /* 69 : 12 0 9 */
      06400, /* 6a : 12 11 1 */
      06200, /* 6b : 12 11 2 */
      06100, /* 6c : 12 11 3 */
      06040, /* 6d : 12 11 4 */
      06020, /* 6e : 12 11 5 */
      06010, /* 6f : 12 11 6 */
      06004, /* 70 : 12 11 7 */
      06002, /* 71 : 12 11 8  */
      06001, /* 72 : 12 11 9 */
      03200, /* 73 : 11 0 2 */
      03100, /* 74 : 11 0 3 */
      03040, /* 75 : 11 0 4 */
      03020, /* 76 : 11 0 5 */
      03010, /* 77 : 11 0 6 */
      03004, /* 78 : 11 0 7 */
      03002, /* 79 : 11 0 8 */
      03001, /* 7a : 11 0 9 */
      05000, /* 7b : 12 0 */
      06000, /* 7c : 12 11 */
      03000, /* 7d : 11 0 */
      03400, /* 7e : 11 0 1 */
      04005, /* 7f : 12 9 7 */
      03403, /* 80 : 11 0 9 8 1 */
      01401, /* 81 : 0 9 1 */
      01201, /* 82 : 0 9 2 */
      01101, /* 83 : 0 9 3 */
      01041, /* 84 : 0 9 4 */
      02021, /* 85 : 11 9 5 */
      04011, /* 86 : 12 9 6 */
      02005, /* 87 : 11 9 7 */
      01003, /* 88 : 0 9 8 */
      01403, /* 89 : 0 9 8 1 */
      01203, /* 8a : 0 9 8 2 */
      01103, /* 8b : 0 9 8 3 */
      01043, /* 8c : 0 9 8 4 */
      04403, /* 8d : 12 9 8 1 */
      04203, /* 8e : 12 9 8 2 */
      02103, /* 8f : 11 9 8 3 */
      07403, /* 90 : 12 11 0 9 8 1 */
      00401, /* 91 : 9 1 */
      02203, /* 92 : 11 9 8 2 */
      00101, /* 93 : 9 3 */
      00041, /* 94 : 9 4 */
      00021, /* 95 : 9 5 */
      00011, /* 96 : 9 6 */
      04003, /* 97 : 12 9 8 */
      00003, /* 98 : 9 8 */
      00403, /* 99 : 9 8 1 */
      00203, /* 9a : 9 8 2 */
      00103, /* 9b : 9 8 3 */
      04041, /* 9c : 12 9 4 */
      02041, /* 9d : 11 9 4 */
      00013, /* 9e : 9 8 6 */
      03401, /* 9f : 11 0 9 1 */
      05401, /* a0 : 12 0 9 1 */
      05201, /* a1 : 12 0 9 2 */
      05101, /* a2 : 12 0 9 3 */
      05041, /* a3 : 12 0 9 4 */
      05021, /* a4 : 12 0 9 5 */
      05011, /* a5 : 12 0 9 6 */
      05005, /* a6 : 12 0 9 7 */
      05003, /* a7 : 12 0 9 8 */
      04402, /* a8 : 12 8 1 */
      06401, /* a9 : 12 11 9 1 */
      06201, /* aa : 12 11 9 2 */
      06101, /* ab : 12 11 9 3 */
      06041, /* ac : 12 11 9 4 */
      06021, /* ad : 12 11 9 5 */
      06011, /* ae : 12 11 9 6 */
      06005, /* af : 12 11 9 7 */
      06003, /* b0 : 12 11 9 8 */
      02402, /* b1 : 11 8 1 */
      03201, /* b2 : 11 0 9 2 */
      03101, /* b3 : 11 0 9 3 */
      03041, /* b4 : 11 0 9 4 */
      03021, /* b5 : 11 0 9 5 */
      03011, /* b6 : 11 0 9 6 */
      03005, /* b7 : 11 0 9 7 */
      03003, /* b8 : 11 0 9 8 */
      01402, /* b9 : 0 8 1 */
      07000, /* ba : 12 11 0 */
      07401, /* bb : 12 11 0 9 1 */
      07201, /* bc : 12 11 0 9 2 */
      07101, /* bd : 12 11 0 9 3 */
      07041, /* be : 12 11 0 9 4 */
      07021, /* bf : 12 11 0 9 5 */
      07011, /* c0 : 12 11 0 9 6 */
      07005, /* c1 : 12 11 0 9 7 */
      07003, /* c2 : 12 11 0 9 8 */
      05402, /* c3 : 12 0 8 1 */
      05202, /* c4 : 12 0 8 2 */
      05102, /* c5 : 12 0 8 3 */
      05042, /* c6 : 12 0 8 4 */
      05022, /* c7 : 12 0 8 5 */
      05012, /* c8 : 12 0 8 6 */
      05006, /* c9 : 12 0 8 7 */
      06402, /* ca : 12 11 8 1 */
      06202, /* cb : 12 11 8 2 */
      06102, /* cc : 12 11 8 3 */
      06042, /* cd : 12 11 8 4 */
      06022, /* ce : 12 11 8 5 */
      06012, /* cf : 12 11 8 6 */
      06006, /* d0 : 12 11 8 7 */
      03402, /* d1 : 11 0 8 1 */
      03202, /* d2 : 11 0 8 2 */
      03102, /* d3 : 11 0 8 3 */
      03042, /* d4 : 11 0 8 4 */
      03022, /* d5 : 11 0 8 5 */
      03012, /* d6 : 11 0 8 6 */
      03006, /* d7 : 11 0 8 7 */
      07402, /* d8 : 12 11 0 8 1 */
      07400, /* d9 : 12 11 0 1 */
      07200, /* da : 12 11 0 2 */
      07100, /* db : 12 11 0 3 */
      07040, /* dc : 12 11 0 4 */
      07020, /* dd : 12 11 0 5 */
      07010, /* de : 12 11 0 6 */
      07004, /* df : 12 11 0 7 */
      07002, /* e0 : 12 11 0 8 */
      07001, /* e1 : 12 11 0 9 */
      07202, /* e2 : 12 11 0 8 2 */
      07102, /* e3 : 12 11 0 8 3 */
      07042, /* e4 : 12 11 0 8 4 */
      07022, /* e5 : 12 11 0 8 5 */
      07012, /* e6 : 12 11 0 8 6 */
      07006, /* e7 : 12 11 0 8 7 */
      05203, /* e8 : 12 0 9 8 2 */
      05103, /* e9 : 12 0 9 8 3 */
      05043, /* ea : 12 0 9 8 4 */
      05023, /* eb : 12 0 9 8 5 */
      05013, /* ec : 12 0 9 8 6 */
      05007, /* ed : 12 0 9 8 7 */
      06203, /* ee : 12 11 9 8 2 */
      06103, /* ef : 12 11 9 8 3 */
      06043, /* f0 : 12 11 9 8 4 */
      06023, /* f1 : 12 11 9 8 5 */
      06013, /* f2 : 12 11 9 8 6 */
      06007, /* f3 : 12 11 9 8 7 */
      03203, /* f4 : 11 0 9 8 2 */
      03103, /* f5 : 11 0 9 8 3 */
      03043, /* f6 : 11 0 9 8 4 */
      03023, /* f7 : 11 0 9 8 5 */
      03013, /* f8 : 11 0 9 8 6 */
      03007, /* f9 : 11 0 9 8 7 */
      07203, /* fa : 12 11 0 9 8 2 */
      07103, /* fb : 12 11 0 9 8 3 */
      07043, /* fc : 12 11 0 9 8 4 */
      07023, /* fd : 12 11 0 9 8 5 */
      07013, /* fe : 12 11 0 9 8 6 */
      07007, /* ff : 12 11 0 9 8 7 */
    };
  private static final Map<Short, Character> CHARACTERS_BY_PUNCH = new HashMap<Short, Character>();
  private static final Map<Character, Short> PUNCHES_BY_CHARACTER = new HashMap<Character, Short>();
  static {
    for (int c = 0; c < ECMA_44_PUNCHES.length; c++) {
      short punch = ECMA_44_PUNCHES[c];
      if (punch >= 0) {
        PUNCHES_BY_CHARACTER.put((char) c, ECMA_44_PUNCHES[c]);
        if (!CHARACTERS_BY_PUNCH.containsKey(ECMA_44_PUNCHES[c])) {
          CHARACTERS_BY_PUNCH.put(ECMA_44_PUNCHES[c], (char) c);
        } else {
          System.err.format("Unexpected duplicate punch %05o for char %d already for %d%n",
              punch, c, (int) (char) CHARACTERS_BY_PUNCH.get(punch));
        }
      } else {
        System.err.format("unknown punch for char %d%n", c);
      }
    }
  }
  
  private static final Path DIGIT_PATHS[] = {
    new Path()
    .moveTo(0, 140)
    .moveTo(30, 140)
    .cubicTo(10, 140, 0, 130, 0, 110)
    .lineTo(0, 30)
    .cubicTo(0, 10, 10, 0, 30, 0)
    .cubicTo(50, 0, 60, 10, 60, 30)
    .lineTo(60, 110)
    .cubicTo(60, 130, 50, 140, 30, 140)
    .closePath()
    .moveTo(30, 120)
    .cubicTo(36, 120, 40, 116, 40, 110)
    .lineTo(40, 30)
    .cubicTo(40, 24, 36, 20, 30, 20)
    .cubicTo(24, 20, 20, 24, 20, 30)
    .lineTo(20, 110)
    .cubicTo(20, 116, 24, 120, 30, 120)
    .closePath(),

  new Path()
    .moveTo(0, 140)
    .moveTo(22, 0)
    .lineTo(42, 0)
    .lineTo(42, 140)
    .lineTo(30, 140)
    .lineTo(12, 122)
    .lineTo(12, 110)
    .lineTo(22, 110)
    .closePath(),

  new Path()
    .moveTo(0, 140)
    .moveTo(0, 0)
    .lineTo(60, 0)
    .lineTo(60, 20)
    .lineTo(20, 20)
    .cubicTo(20, 20, 58, 90, 58, 110)
    .cubicTo(58, 130, 48, 140, 30, 140)
    .cubicTo(12, 140, 2, 130, 2, 110)
    .lineTo(20, 110)
    .cubicTo(20, 116, 24, 120, 30, 120)
    .cubicTo(36, 120, 40, 116, 40, 110)
    .cubicTo(40, 100, 0, 34, 0, 14)
    .closePath(),

  new Path()
    .moveTo(0, 140)
    .moveTo(0, 30)
    .cubicTo(0, 9, 10, 0, 30, 0)
    .cubicTo(50, 0, 60, 10, 60, 30)
    .cubicTo(60, 42, 60, 36, 60, 44)
    .cubicTo(60, 64, 56, 66, 48, 74)
    .cubicTo(56, 82, 58, 84, 58, 104)
    .cubicTo(58, 110, 58, 100, 58, 110)
    .cubicTo(58, 130, 48, 140, 30, 140)
    .cubicTo(12, 140, 2, 130, 2, 110)
    .lineTo(20, 110)
    .cubicTo(20, 116, 24, 120, 30, 120)
    .cubicTo(36, 120, 40, 116, 40, 110)
    .cubicTo(40, 105, 40, 100, 40, 94)
    .cubicTo(40, 88, 35, 84, 30, 84)
    .cubicTo(27, 84, 20, 84, 20, 84)
    .lineTo(20, 64)
    .cubicTo(20, 64, 28, 64, 30, 64)
    .cubicTo(35, 64, 40, 60, 40, 54)
    .cubicTo(40, 47, 40, 35, 40, 30)
    .cubicTo(40, 24, 36, 20, 30, 20)
    .cubicTo(24, 20, 20, 24, 20, 30)
    .closePath(),

  new Path()
    .moveTo(0, 140)
    .moveTo(30, 140)
    .lineTo(0, 50)
    .lineTo(0, 30)
    .lineTo(30, 30)
    .lineTo(30, 0)
    .lineTo(50, 0)
    .lineTo(50, 30)
    .lineTo(60, 30)
    .lineTo(60, 50)
    .lineTo(50, 50)
    .lineTo(50, 140)
    .closePath()
    .moveTo(30, 92)
    .lineTo(30, 50)
    .lineTo(16, 50)
    .closePath(),

  new Path()
    .moveTo(0, 140)
    .moveTo(2, 66)
    .cubicTo(8, 66, 10, 66, 16, 66)
    .cubicTo(21, 66, 24, 70, 30, 70)
    .cubicTo(36, 70, 40, 66, 40, 60)
    .cubicTo(40, 54, 40, 30, 40, 30)
    .cubicTo(40, 24, 36, 20, 30, 20)
    .cubicTo(24, 20, 20, 24, 20, 30)
    .cubicTo(20, 40, 20, 40, 20, 40)
    .lineTo(0, 40)
    .cubicTo(0, 40, 0, 50, 0, 30)
    .cubicTo(0, 10, 10, 0, 30, 0)
    .cubicTo(50, 0, 60, 10, 60, 30)
    .cubicTo(60, 50, 60, 40, 60, 60)
    .cubicTo(60, 80, 50, 90, 30, 90)
    .cubicTo(26, 90, 22, 88, 22, 88)
    .lineTo(24, 118)
    .lineTo(56, 118)
    .lineTo(56, 138)
    .lineTo(6, 138)
    .closePath(),

  new Path()
    .moveTo(0, 140)
    .moveTo(30, 140)
    .cubicTo(10, 140, 0, 130, 0, 110)
    .lineTo(0, 30)
    .cubicTo(0, 10, 10, 0, 30, 0)
    .cubicTo(50, 0, 60, 10, 60, 30)
    .lineTo(60, 60)
    .cubicTo(60, 80, 50, 90, 30, 90)
    .cubicTo(24, 90, 20, 88, 20, 88)
    .lineTo(20, 110)
    .cubicTo(20, 116, 24, 120, 30, 120)
    .cubicTo(36, 120, 40, 116, 40, 110)
    .lineTo(60, 110)
    .cubicTo(60, 130, 50, 140, 30, 140)
    .closePath()
    .moveTo(30, 70)
    .cubicTo(36, 70, 40, 66, 40, 60)
    .lineTo(40, 30)
    .cubicTo(40, 24, 36, 20, 30, 20)
    .cubicTo(24, 20, 20, 24, 20, 30)
    .lineTo(20, 60)
    .cubicTo(20, 66, 24, 70, 30, 70)
    .closePath(),

  new Path()
    .moveTo(0, 140)
    .moveTo(14, 0)
    .lineTo(34, 0)
    .lineTo(56, 118)
    .lineTo(56, 138)
    .lineTo(4, 138)
    .lineTo(4, 118)
    .lineTo(38, 118)
    .closePath(),

  new Path()
    .moveTo(0, 140)
    .moveTo(30, 140)
    .cubicTo(14, 140, 2, 132, 2, 110)
    .lineTo(2, 104)
    .cubicTo(2, 84, 4, 82, 12, 74)
    .cubicTo(4, 66, 0, 64, 0, 44)
    .lineTo(0, 30)
    .cubicTo(0, 10, 10, 0, 30, 0)
    .cubicTo(50, 0, 60, 10, 60, 30)
    .lineTo(60, 44)
    .cubicTo(60, 64, 56, 66, 48, 74)
    .cubicTo(56, 82, 58, 84, 58, 104)
    .lineTo(58, 110)
    .cubicTo(58, 130, 46, 140, 30, 140)
    .closePath()
    .moveTo(30, 120)
    .cubicTo(36, 120, 40, 116, 40, 110)
    .lineTo(40, 94)
    .cubicTo(40, 88, 36, 84, 30, 84)
    .cubicTo(24, 84, 20, 88, 20, 94)
    .lineTo(20, 110)
    .cubicTo(20, 116, 24, 120, 30, 120)
    .closePath()
    .moveTo(30, 64)
    .cubicTo(36, 64, 40, 60, 40, 54)
    .lineTo(40, 30)
    .cubicTo(40, 24, 36, 20, 30, 20)
    .cubicTo(24, 20, 20, 24, 20, 30)
    .lineTo(20, 54)
    .cubicTo(20, 60, 24, 64, 30, 64)
    .closePath(),

  new Path()
    .moveTo(0, 140)
    .moveTo(30, 140)
    .cubicTo(10, 140, 0, 130, 0, 110)
    .lineTo(0, 80)
    .cubicTo(0, 60, 10, 50, 30, 50)
    .cubicTo(36, 50, 40, 52, 40, 52)
    .lineTo(40, 30)
    .cubicTo(40, 24, 36, 20, 30, 20)
    .cubicTo(24, 20, 20, 24, 20, 30)
    .lineTo(0, 30)
    .cubicTo(0, 10, 10, 0, 30, 0)
    .cubicTo(50, 0, 60, 10, 60, 30)
    .lineTo(60, 110)
    .cubicTo(60, 130, 50, 140, 30, 140)
    .closePath()
    .moveTo(30, 120)
    .cubicTo(36, 120, 40, 116, 40, 110)
    .lineTo(40, 80)
    .cubicTo(40, 74, 36, 70, 30, 70)
    .cubicTo(24, 70, 20, 74, 20, 80)
    .lineTo(20, 110)
    .cubicTo(20, 116, 24, 120, 30, 120)
    .closePath(),
  };
  private static final int LEGEND_DIGIT_FONT_WIDTH = 60;
  private static final int LEGEND_DIGIT_FONT_HEIGHT = 140;
  
  private static final double MARGIN = .05;

  public static final int ROWS = 12;
  public static final int COLUMNS = 80;

  private static final double WIDTH = 7.375;
  private static final double HEIGHT = 3.250;
  private static final double COLUMN_WIDTH = .087;
  private static final double HOLE_WIDTH = .055;
  private static final double COLUMN_CENTER_OFFSET = .251;
  private static final double COLUMN_HOLE_OFFSET = COLUMN_CENTER_OFFSET - HOLE_WIDTH / 2.0;
  private static final double ROW_HEIGHT = .250;
  private static final double HOLE_HEIGHT = .125;
  private static final double ROW_CENTER_OFFSET = ROW_HEIGHT;
  private static final double ROW_HOLE_OFFSET = ROW_CENTER_OFFSET + HOLE_HEIGHT / 2.0;
  
  private static final double LEGEND_DIGIT_WIDTH = 0.0375;
  private static final double LEGEND_DIGIT_HEIGHT = 0.0875;
  private static final double LEGEND_DIGIT_SCALE = Math.min(LEGEND_DIGIT_WIDTH / LEGEND_DIGIT_FONT_WIDTH, LEGEND_DIGIT_HEIGHT / LEGEND_DIGIT_FONT_HEIGHT);
  private static final double LEGEND_X_OFFSET = COLUMN_CENTER_OFFSET - LEGEND_DIGIT_SCALE * LEGEND_DIGIT_FONT_WIDTH / 2.0;
  private static final double LEGEND_Y_OFFSET = ROW_CENTER_OFFSET - LEGEND_DIGIT_SCALE * LEGEND_DIGIT_FONT_HEIGHT / 2.0;
  
  private static final double LEGEND_SMALL_DIGIT_RELAVITE_SIZE = 0.6; // estimated

  private static final double LEGEND_SMALL_DIGIT_SCALE = LEGEND_DIGIT_SCALE * LEGEND_SMALL_DIGIT_RELAVITE_SIZE;
  private static final double LEGEND_SMALL_DIGIT_WIDTH = LEGEND_DIGIT_WIDTH * LEGEND_SMALL_DIGIT_RELAVITE_SIZE;
  private static final double LEGEND_SMALL_DIGIT_HEIGHT = LEGEND_DIGIT_HEIGHT * LEGEND_SMALL_DIGIT_RELAVITE_SIZE;
  private static final double LEGEND_SMALL_SINGLE_DIGIT_X_OFFSET = COLUMN_CENTER_OFFSET - LEGEND_SMALL_DIGIT_SCALE * LEGEND_DIGIT_FONT_WIDTH / 2.0;
  private static final double LEGEND_SMALL_DIGIT_SPACING = 1.0 / 6.0;
  private static final double LEGEND_SMALL_DOUBLE_DIGIT_X_OFFSET = COLUMN_CENTER_OFFSET - LEGEND_SMALL_DIGIT_SCALE * LEGEND_DIGIT_FONT_WIDTH * (2 + LEGEND_SMALL_DIGIT_SPACING) / 2.0;

  private static final double ROUND_CORNER_RADIUS = .250;
  private static final double ROUND_CORNER_DIAMETER = ROUND_CORNER_RADIUS * 2;
  private static final double CORNER_CUT_WIDTH = .250;
  private static final double CORNER_CUT_HEIGHT = .4375;
  
  // Angles for offsetting the corner cut
  private static final double CORNER_CUT_OUTSET_DY = Math.tan(Math.atan2(CORNER_CUT_WIDTH, CORNER_CUT_HEIGHT) / 2);
  private static final double CORNER_CUT_OUTSET_DX = Math.tan(Math.atan2(CORNER_CUT_HEIGHT, CORNER_CUT_WIDTH) / 2);

  static {
    int columns = PRINT_CHARS[0].length;
    double diameter = COLUMN_WIDTH / (columns + 1);
    double xOffset = diameter * columns / 2.0;

    for (int i = 0; i < PRINT_CHARS.length; i++) {
      byte[] matrix = PRINT_CHARS[i];
      
      int n = hammingWeight(matrix[0]) + hammingWeight(matrix[1]) 
          + hammingWeight(matrix[2]) + hammingWeight(matrix[3])
          + hammingWeight(matrix[4]);
      
      PRINT_ELLIPSES[i] = new Ellipse[n];
      
      int j = 0;
      for (int column = 0; column < matrix.length; column++) {
        byte dots = matrix[column];
        if (dots != 0) {
          for (int bit = 0; bit < 7; bit++) {
            if ((dots & (((byte) 1) << bit)) != 0) {
              PRINT_ELLIPSES[i][j++] = new Ellipse(diameter * column - xOffset, diameter * (7 - bit), diameter, diameter);
            }
          }
        }
      }
    }
  }

  private Color color;
  private boolean roundCorners = false;
  private boolean cutLeft = false;
  private boolean cutRight = true;
  
  private Path shape;
  private Font legendFont;
  private boolean printValue;
  
  private short punches[] = new short[COLUMNS]; 
  
  private ByteBuffer byteBuffer = ByteBuffer.allocate(2);
  private CharBuffer charBuffer = CharBuffer.allocate(2);

  public PunchedCard(Color color, boolean roundCorners, boolean cutLeft, boolean cutRight, boolean printValue) {
    super(new Size(WIDTH + 2 * MARGIN, HEIGHT + 2 * MARGIN));
    
    this.color = color;
    this.roundCorners = roundCorners;
    this.cutLeft = cutLeft;
    this.cutRight = cutRight;
    
    this.printValue = printValue;
  }
  
  private Path makeOutline(double outset) {
    double angle = 180;
    double quarter = 90;
    
    Path p = new Path();
    double r = ROUND_CORNER_RADIUS + outset;
    double d = 2 * r;
    
    // bottom
    if (roundCorners) {
      p.moveTo(-outset, HEIGHT - ROUND_CORNER_RADIUS);
      p.addArc(-outset, HEIGHT - ROUND_CORNER_DIAMETER - outset, d, d, angle, quarter);
      angle = (angle + quarter) % 360;
      p.lineTo(WIDTH - ROUND_CORNER_RADIUS, HEIGHT + outset);
      p.addArc(WIDTH - ROUND_CORNER_DIAMETER - outset, HEIGHT - ROUND_CORNER_DIAMETER - outset, d, d, angle, quarter);
      angle = (angle + quarter) % 360;
    } else {
      p.moveTo(-outset, HEIGHT + outset);
      p.lineTo(WIDTH + outset, HEIGHT + outset);
    }
    
    // right top
    if (cutRight) {
      p.lineTo(WIDTH + outset, CORNER_CUT_HEIGHT - outset * CORNER_CUT_OUTSET_DY);
      p.lineTo(WIDTH - CORNER_CUT_WIDTH + outset * CORNER_CUT_OUTSET_DX, -outset);
    } else if (roundCorners) {
      p.lineTo(WIDTH + outset, ROUND_CORNER_RADIUS);
      p.addArc(WIDTH - ROUND_CORNER_DIAMETER - outset, -outset, d, d, angle, quarter);
    } else {
      p.lineTo(WIDTH + outset, -outset);
    }
    angle = (angle + quarter) % 360;

    // left top
    if (cutLeft) {
      p.lineTo(CORNER_CUT_WIDTH - outset * CORNER_CUT_OUTSET_DX, -outset);
      p.lineTo(-outset, CORNER_CUT_HEIGHT - outset * CORNER_CUT_OUTSET_DY);
    } else if (roundCorners) {
      p.lineTo(ROUND_CORNER_RADIUS, -outset);
      p.addArc(-outset, -outset, d, d, angle, quarter);
    } else {
      p.lineTo(-outset, -outset);
    }
    
    p.closePath();
    
    return p.withCubics();
  }
  
  public Path makeShape(double outset) {
    Path p = makeOutline(outset);
    
    for (int column = 0 ; column < COLUMNS; ++column) {
      double x = COLUMN_HOLE_OFFSET + column * COLUMN_WIDTH;
      for (int row = 0; row < ROWS; ++row) {
        double y = HEIGHT - (ROW_HOLE_OFFSET + row * ROW_HEIGHT);
        if (isPunched(column, row)) {
          p.moveTo(x + outset, y + outset);
          p.lineTo(x + HOLE_WIDTH - outset, y + outset);
          p.lineTo(x + HOLE_WIDTH - outset, y + HOLE_HEIGHT - outset);
          p.lineTo(x + outset, y + HOLE_HEIGHT - outset);
          p.closePath();
        }
      }
    }
      
    return p.withCubics();
  }
    
  private Path shape() {
    if (this.shape == null) {
      this.shape = makeShape(0);
    }
    return this.shape;
  }
  
  private void drawDigit(Surface s, double x, double y, double scale, int digit) {
    s.save();
    s.translate(x, y);
    s.scale(scale, -scale);
    s.fill(DIGIT_PATHS[digit]);
    s.restore();
  }

  @Override
  public void draw(Surface s) {
    super.draw(s);
    
    s.save();
    s.translate(MARGIN, MARGIN);
    
    s.save();
    s.setColor(Color.LIGHT_GRAY);
    s.setStrokeStyle(new StrokeStyle(0.01));
    s.stroke(makeShape(0));
    s.restore();

    s.save();
    s.translate(0.01, 0.01);
    s.setColor(Color.DARK_GRAY);
    s.fill(makeShape(0));
    s.restore();

    s.setColor(color);
    s.fill(shape());
    
    s.setColor(INK);
    for (int column = 0; column < COLUMNS; ++column) {
      double x = LEGEND_X_OFFSET + column * COLUMN_WIDTH;
      for (int row = 0; row < 10; ++row) {
        double y = HEIGHT - (LEGEND_Y_OFFSET + row * ROW_HEIGHT);
        if (!isPunched(column, row)) {
          drawDigit(s, x, y, LEGEND_DIGIT_SCALE, 9 - row);
        }
      }
    }
    
    double y9 = HEIGHT - (ROW_HEIGHT - LEGEND_SMALL_DIGIT_HEIGHT) / 2.0;
    double y01 = HEIGHT - (19 * ROW_HEIGHT - LEGEND_SMALL_DIGIT_HEIGHT) / 2.0;
    
    for (int column = 0; column < 9; column++) {
      double x = LEGEND_SMALL_SINGLE_DIGIT_X_OFFSET + column * COLUMN_WIDTH;
      drawDigit(s, x, y9, LEGEND_SMALL_DIGIT_SCALE, column + 1);
      drawDigit(s, x, y01, LEGEND_SMALL_DIGIT_SCALE, column + 1);
    }

    for (int column = 9; column < COLUMNS; column++) {
      double x0 = LEGEND_SMALL_DOUBLE_DIGIT_X_OFFSET + column * COLUMN_WIDTH;
      double x1 = x0 + (1 + LEGEND_SMALL_DIGIT_SPACING) * LEGEND_SMALL_DIGIT_WIDTH;
      int d0 = (column + 1) / 10;
      int d1 = (column + 1) % 10;
      drawDigit(s, x0, y9, LEGEND_SMALL_DIGIT_SCALE, d0);
      drawDigit(s, x1, y9, LEGEND_SMALL_DIGIT_SCALE, d1);
      drawDigit(s, x0, y01, LEGEND_SMALL_DIGIT_SCALE, d0);
      drawDigit(s, x1, y01, LEGEND_SMALL_DIGIT_SCALE, d1);
    }

    if (printValue) {
      s.save();
      s.clip(makeOutline(0));
      s.setColor(PRINT);
      for (int column = 0; column < COLUMNS; ++column) {
        double x = COLUMN_HOLE_OFFSET + column * COLUMN_WIDTH + HOLE_WIDTH / 2;
        Character c = CHARACTERS_BY_PUNCH.get(punches[column]);
        if (c != null) {
          printChar(s, c, x);
        }
      }
      s.restore();
    }
    
    s.restore();
  }
  
  private void printChar(Surface s, char c, double x) {
    charBuffer.clear();
    charBuffer.put(c); 
    charBuffer.flip();
    
    byteBuffer.clear();
    LATIN_1_ENCODER.encode(charBuffer, byteBuffer, false);
    byteBuffer.flip();
    
    byte b = byteBuffer.get();
    int i = (int) b & 0xff;

    s.save();
    s.translate(x, 0);
    for (Ellipse e : PRINT_ELLIPSES[i]) {
      s.fill(e);
    }
    s.restore();
  }
  
  public void setPunched(int column, int row, boolean punched) {
    if (punched) {
      punches[column] |= (1 << row);
    } else {
      punches[column] &= ~(1 << row);
    }
    shape = null;
    repaint();
  }
  
  private boolean isPunched(int column, int row) {
    return (punches[column] & (1 << row)) != 0;
  }

  public void setPunch(int column, int value) {
    punches[column] = (short) value;
    shape = null;
    repaint();
  }

  public void setByte(int column, byte value) {
    punches[column] = ECMA_44_PUNCHES[value];
    shape = null;
    repaint();
  }
  
  public void setChar(int column, char c) {
    setPunch(column, PUNCHES_BY_CHARACTER.containsKey(c) ? PUNCHES_BY_CHARACTER.get(c) : 0);
  }
  
  public void setString(String s) {
    int n = Math.min(s.length(), COLUMNS);
    for (int i = 0; i < n; i++) {
      setChar(i, s.charAt(i));
    }
    for (int i = n; i < COLUMNS; i++) {
      setPunch(i, 0);
    }
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
    repaint();
  }

  public boolean isRoundCorners() {
    return roundCorners;
  }

  public void setRoundCorners(boolean roundCorners) {
    this.roundCorners = roundCorners;
    shape = null;
    repaint();
  }

  public boolean isCutLeft() {
    return cutLeft;
  }

  public void setCutLeft(boolean cutLeft) {
    this.cutLeft = cutLeft;
    shape = null;
    repaint();
  }

  public boolean isCutRight() {
    return cutRight;
  }

  public void setCutRight(boolean cutRight) {
    this.cutRight = cutRight;
    shape = null;
    repaint();
  }

  public Font getLegendFont() {
    return legendFont;
  }

  public boolean isPrintableChar(Character c) {
    Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
    return (!Character.isISOControl(c)) &&
            block != null &&
            block != Character.UnicodeBlock.SPECIALS;
  }
  
  public void punchSample() {
    for (int digit = 0; digit < 10; digit++) {
      setPunch(3 + digit, 1 << (9 - digit));
    }
    
    for (int i = 0; i < 9; i++) {
      setPunch(19 + i, 0x800 | (1 << (8 - i)));
    }
    for (int i = 0; i < 9; i++) {
      setPunch(28 + i, 0x400 | (1 << (8 - i)));
    }
    for (int i = 0; i < 8; i++) {
      setPunch(37 + i, 0x200 | (1 << (7 - i)));
    }
    
    setPunch(48, 0x800);
    for (int i = 0; i < 6; i++) {
      setPunch(49 + i, 0x802 | (1 << (7 - i)));
    }
    setPunch(55, 0x400);
    for (int i = 0; i < 6; i++) {
      setPunch(56 + i, 0x402 | (1 << (7 - i)));
    }
    setPunch(62, 0x300);
    for (int i = 0; i < 6; i++) {
      setPunch(63 + i, 0x202 | (1 << (7 - i)));
    }    
    for (int i = 0; i < 6; i++) {
      setPunch(69 + i, 0x002 | (1 << (7 - i)));
    }
  }
  
  public void punchRandom() {
    Random random = new Random();
    for (int row = 0; row < 12; row++) {
      for (int column = 0; column < 80; column++) {
        setPunched(column, row, random.nextInt(8) == 0);
      }
    }
  }
  
  public void punchLace() {
    for (int column = 0; column < 80; column++) {
      setPunch(column, 0xfff);
    }
  }
  
  private static int hammingWeight(int i) {
    i = i - ((i >> 1) & 0x55555555);
    i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
    return (((i + (i >> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
  }
}

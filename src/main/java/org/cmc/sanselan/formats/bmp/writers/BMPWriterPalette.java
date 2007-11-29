/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cmc.sanselan.formats.bmp.writers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.cmc.sanselan.common.BinaryOutputStream;
import org.cmc.sanselan.palette.SimplePalette;

public class BMPWriterPalette extends BMPWriter
{
	private final SimplePalette palette;
	//	private final int palette[];
	private final int bits_per_sample;

	public BMPWriterPalette(SimplePalette palette)
	//	public BMPWriterPalette(int palette[])
	{
		this.palette = palette;

		if (palette.length() <= 2)
			bits_per_sample = 1;
		else if (palette.length() <= 16)
			bits_per_sample = 4;
		else
			bits_per_sample = 8;

		System.out.println("bits_per_sample: " + bits_per_sample);
	}

	public int getPaletteSize()
	{
		return palette.length();
	}

	public int getBitsPerPixel()
	{
		return bits_per_sample;
	}

	public void writePalette(BinaryOutputStream bos) throws IOException
	{
		for (int i = 0; i < palette.length(); i++)
		{
			int rgb = palette.getEntry(i);

			int red = 0xff & (rgb >> 16);
			int green = 0xff & (rgb >> 8);
			int blue = 0xff & (rgb >> 0);

			bos.write(blue);
			bos.write(green);
			bos.write(red);
			bos.write(0);
		}
	}

	public byte[] getImageData(BufferedImage src)
	{
		int width = src.getWidth();
		int height = src.getHeight();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//			BinaryOutputStream bos = new BinaryOutputStream(baos, BYTE_ORDER_Network);

		int bit_cache = 0;
		int bits_in_cache = 0;

		int bytecount = 0;
		for (int y = height - 1; y >= 0; y--)
		{
			//			for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				int argb = src.getRGB(x, y);
				int rgb = 0xffffff & argb;

				int index = palette.getPaletteIndex(rgb);
				//				int index = getPaletteIndex(rgb);

				if (bits_per_sample == 8)
				{
					baos.write(0xff & index);
					bytecount++;
				}
				else
				// 4 or 1
				{
					bit_cache = (bit_cache << bits_per_sample) | index;
					bits_in_cache += bits_per_sample;
					if (bits_in_cache >= 8)
					{
						baos.write(0xff & bit_cache);
						bytecount++;
						bit_cache = 0;
						bits_in_cache = 0;
					}

				}
			}
			while ((bytecount % 4) != 0)
			{
				baos.write(0);
				bytecount++;
			}
		}

		return baos.toByteArray();
	}
}
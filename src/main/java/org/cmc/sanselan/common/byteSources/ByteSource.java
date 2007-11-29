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
package org.cmc.sanselan.common.byteSources;

import java.io.IOException;
import java.io.InputStream;

import org.cmc.sanselan.common.BinaryFileFunctions;

public abstract class ByteSource extends BinaryFileFunctions
{
	protected final String filename;

	public ByteSource(final String filename)
	{
		this.filename = filename;
	}

	public final InputStream getInputStream(int start) throws IOException
	{
		InputStream is = getInputStream();

		skipBytes(is, start);

		return is;
	}

	public abstract InputStream getInputStream() throws IOException;

	public abstract byte[] getBlock(int start, int length) throws IOException;

	public abstract byte[] getAll() throws IOException;

//	public abstract long getLength();
//
//	public byte[] getAll() throws IOException
//	{
//		return getBlock(0, (int) getLength());
//	}

	public abstract String getDescription();

	public final String getFilename()
	{
		return filename;
	}
}
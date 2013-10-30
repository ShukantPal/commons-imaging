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
package org.apache.commons.imaging.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.imaging.util.IoUtils;

public class ZLibUtils extends BinaryFunctions {
    public final byte[] inflate(final byte bytes[]) throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        final InflaterInputStream zIn = new InflaterInputStream(in);
        return getStreamBytes(zIn);
    }

    public final byte[] deflate(final byte bytes[]) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DeflaterOutputStream dos = new DeflaterOutputStream(baos);
        boolean canThrow = false;
        try {
            dos.write(bytes);
            canThrow = true;
        } finally {
            IoUtils.closeQuietly(canThrow, dos);
        }
        return baos.toByteArray();
    }

}
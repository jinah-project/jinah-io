/* 
 * JINAH Project - Java Is Not A Hammer
 * http://obadaro.com/jinah
 * 
 * Copyright (C) 2010-2012 Roberto Badaro 
 * and individual contributors by the @authors tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.obadaro.jinah.io.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Roberto Badaro
 */
public class BOMFilterInputstreamTest {

    @Test
    public void tBomBytes() throws IOException {

        final String text = "A text starting with BOM bytes.";
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos.write(BOMFilterInputstream.BOM_BYTE);
        baos.write(text.getBytes());

        final BOMFilterInputstream bomFilterIn = new BOMFilterInputstream(new ByteArrayInputStream(baos.toByteArray()));
        final byte[] buff = new byte[4096];

        final int len = bomFilterIn.read(buff);
        final String result = new String(buff, 0, len);

        Assert.assertEquals(text, result);
    }

    @Test
    public void tBomChars() throws IOException {

        final String text = "A text starting with BOM chars.";
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos.write(BOMFilterInputstream.BOM_STR.getBytes());
        baos.write(text.getBytes());

        final BOMFilterInputstream bomFilterIn = new BOMFilterInputstream(new ByteArrayInputStream(baos.toByteArray()));
        final byte[] buff = new byte[4096];

        final int len = bomFilterIn.read(buff);
        final String result = new String(buff, 0, len);

        Assert.assertEquals(text, result);
    }

    @Test
    public void tNoBom() throws IOException {

        final String text = "A text with no starting BOM bytes or chars.";
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(text.getBytes());

        final BOMFilterInputstream bomFilterIn = new BOMFilterInputstream(new ByteArrayInputStream(baos.toByteArray()));
        final byte[] buff = new byte[4096];

        final int len = bomFilterIn.read(buff);
        final String result = new String(buff, 0, len);

        Assert.assertEquals(text, result);
    }
}

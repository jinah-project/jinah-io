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
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A FilterInputStream for streams that can contain BOM bytes at the start. If BOM found, they'll be
 * removed from stream.
 * <p>
 * To learn more about BOM (Byte order mark): http://en.wikipedia.org/wiki/Byte_order_mark
 * </p>
 * 
 * @author Roberto Badaro
 */
public class BOMFilterInputstream extends FilterInputStream {

    static final String BOM_STR = new String("\u00EF\u00BB\u00BF");
    static final byte[] BOM_BYTE = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };

    protected boolean bomChecked = false;

    /**
     * Returns next position after BOM bytes. If {@code buff} not contains BOM, return 0.
     * 
     * @param buff
     * @return Next position after BOM bytes, or 0 (zero) if no BOM found.
     * @throws IOException
     */
    public static int containsBOM(final byte[] buff) throws IOException {

        int good = 0;
        if (buff.length > 2) {
            if ((buff[0] == BOM_BYTE[0]) && (buff[1] == BOM_BYTE[1]) && (buff[2] == BOM_BYTE[2])) {
                good = 3;
            } else if (buff.length >= 6) {
                final String testBOM = new String(buff, 0, 6);
                if (BOM_STR.equals(testBOM)) {
                    good = 6;
                }
            }
        }
        return good;
    }

    /**
     * @param in
     */
    public BOMFilterInputstream(final InputStream in) throws IOException {

        super(null);
        checkAndRemoveBOM(in);
    }

    protected void checkAndRemoveBOM(final InputStream input) throws IOException {

        if (bomChecked) {
            return;
        }

        bomChecked = true;
        byte[] buff = new byte[6];
        int len = input.read(buff);

        final int goodStart = containsBOM(buff);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(buff, goodStart, len - goodStart);

        buff = new byte[4096];
        while ((len = input.read(buff)) > 0) {
            baos.write(buff, 0, len);
        }

        in = new ByteArrayInputStream(baos.toByteArray());
    }

}

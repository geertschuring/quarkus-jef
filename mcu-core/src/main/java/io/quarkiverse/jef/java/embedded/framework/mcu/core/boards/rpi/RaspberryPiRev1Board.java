/*
 * Copyright (c) 2021, IOT-Hub.RU and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is dual-licensed: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License Version 3 as
 * published by the Free Software Foundation. For the terms of this
 * license, see <http://www.gnu.org/licenses/>.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public
 * License Version 3 for more details (a copy is included in the LICENSE
 * file that accompanied this code).
 *
 * You should have received a copy of the GNU Affero General Public License
 * version 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact support@iot-hub.ru or visit www.iot-hub.ru if you need
 * additional information or have any questions.
 *
 * You can be released from the requirements of the license by purchasing
 * a Java Embedded Framework Commercial License. Buying such a license is
 * mandatory as soon as you develop commercial activities involving the
 * Java Embedded Framework software without disclosing the source code of
 * your own applications.
 *
 * Please contact sales@iot-hub.ru if you have any question.
 */

package io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.rpi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.quarkiverse.jef.java.embedded.framework.linux.i2c.I2CBus;
import io.quarkiverse.jef.java.embedded.framework.linux.i2c.I2CBusImpl;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBusImpl;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiMode;
import io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.BoardPin;

class RaspberryPiRev1Board extends RaspberryPiAbstractBoard {
    public RaspberryPiRev1Board() throws IOException {
        super();
    }

    @Override
    protected List<I2CBus> initI2C() throws IOException {
        final List<I2CBus> i2cs = new ArrayList<>();
        if (new File("/dev/i2c-1").exists()) {
            i2cs.add(
                    new I2CBusImpl("/dev/i2c-1"));
        }
        return Collections.unmodifiableList(i2cs);
    }

    @Override
    protected List<SpiBus> initSPI() throws IOException {
        List<SpiBus> ss = new ArrayList<>();
        if (new File("/dev/spidev0.0").exists()) {
            ss.add(
                    new SpiBusImpl("/dev/spidev0.0", 500000, SpiMode.SPI_MODE_3, 8, 0));
        }
        return Collections.unmodifiableList(ss);
    }

    @Override
    protected List<BoardPin> initGPIO() throws IOException {
        return RaspberryPi26Rev1Pins.createPins();
    }
}

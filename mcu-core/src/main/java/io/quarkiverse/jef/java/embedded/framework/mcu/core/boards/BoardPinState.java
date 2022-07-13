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

package io.quarkiverse.jef.java.embedded.framework.mcu.core.boards;

import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioPin;

public enum BoardPinState {
    NOT_SET(-1),
    LOW(0),
    HIGH(1);

    int value;

    BoardPinState(int value) {
        this.value = value;
    }

    public static BoardPinState valueOf(GpioPin.State read) {
        switch (read) {
            case LOW:
                return BoardPinState.LOW;
            case HIGH:
                return BoardPinState.HIGH;
            default:
                return BoardPinState.NOT_SET;
        }
    }

    int getValue() {
        return value;
    }
}

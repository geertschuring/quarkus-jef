
package io.quarkiverse.jef.java.embedded.framework.linux.core;

import java.util.concurrent.atomic.AtomicBoolean;

import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.IntReference;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.LongReference;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.SmbusIoctlData;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.SpiIocTransfer;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioChipInfo;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioHandleData;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioHandleRequest;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioLineInfo;

@SuppressWarnings("unused")
public abstract class Ioctl extends IoctlBase implements FeatureSupport {

    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    public static int TIOCMGET = 0x5415;
    public static int TIOCMSET = 0x5418;
    public static final long FIONREAD = 0x541B;
    private static Ioctl instance;

    public static Ioctl getInstance() {
        if (instance == null && !initialized.get()) {
            synchronized (Ioctl.class) {
                if (instance == null && !initialized.get()) {
                    instance = NativeBeanLoader.createContent(Ioctl.class);
                    instance.initVariables();
                    initialized.set(true);
                }
            }
        }
        return instance;
    }

    public abstract int ioctl(FileHandle fd, long command, long arg) throws NativeIOException;

    public abstract int ioctl(FileHandle fd, long command, LongReference arg) throws NativeIOException;

    public abstract int ioctl(FileHandle fd, long command, IntReference arg) throws NativeIOException;

    public abstract int ioctl(FileHandle fd, long command, SmbusIoctlData ptr) throws NativeIOException;

    public abstract int ioctl(FileHandle fd, SpiIocTransfer ptr) throws NativeIOException;

    public abstract int ioctl(FileHandle handle, long command, GpioHandleRequest request) throws NativeIOException;

    public abstract int ioctl(FileHandle handle, long command, GpioChipInfo info) throws NativeIOException;

    public abstract int ioctl(FileHandle handle, long command, GpioLineInfo line) throws NativeIOException;

    public abstract int ioctl(int fd, long command, GpioHandleData data) throws NativeIOException;
}


package io.quarkiverse.jef.java.embedded.framework.linux.serial;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.IOFlags.*;
import static io.quarkiverse.jef.java.embedded.framework.linux.core.Termios.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Ioctl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Termios;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.IntReference;

@SuppressWarnings("unused")
public class SerialPort implements SerialBus {
    private final Fcntl fcntl;
    private final Ioctl ioctl;
    private final Termios termios;
    private final FileHandle handle;
    private final SerialInputStream is;
    private final SerialOutputStream os;

    public SerialPort(String path, SerialBaudRate rate) throws NativeIOException {
        fcntl = Fcntl.getInstance();
        ioctl = Ioctl.getInstance();
        termios = Termios.getInstance();
        handle = fcntl.open(path, EnumSet.of(O_RDWR, O_NOCTTY, O_NONBLOCK));
        fcntl.fcntl(handle, Fcntl.F_SETFL, EnumSet.of(O_RDWR));

        //https://blog.mbedded.ninja/programming/operating-systems/linux/linux-serial-ports-using-c-cpp/
        TermiosStructure tty = termios.tcgetattr(handle);

        // Control Modes
        tty.c_cflag &= ~PARENB; // Clear parity bit, disabling parity
        tty.c_cflag &= ~CSTOPB; // Clear stop field, only one stop bit used in communication
        tty.c_cflag &= ~CSIZE; // Clear all the size bits, then use one of the statements
        tty.c_cflag |= CS8; // 8 bits per byte
        tty.c_cflag &= ~CRTSCTS; // Disable RTS/CTS hardware flow control
        tty.c_cflag |= CREAD | CLOCAL; // Turn on READ & ignore ctrl lines (CLOCAL = 1)

        // Local Modes
        tty.c_lflag &= ~(ICANON | IEXTEN | ISIG | ECHO);
        //tty.c_lflag &= ~ICANON; // Canonical mode is disabled
        //tty.c_lflag &= ~ECHO; // Disable echo
        //tty.c_lflag &= ~ISIG; // Disable interpretation of INTR, QUIT and SUSP
        //tty.c_lflag |= ICANON; // Canonical mode is enabled

        // Input Modes
        tty.c_iflag &= ~(ICRNL | INPCK | ISTRIP | IXON | BRKINT);
        //tty.c_iflag &= ~(IXON | IXOFF | IXANY); // Turn off s/w flow ctrl
        //tty.c_iflag &= ~(IGNBRK|BRKINT|PARMRK|ISTRIP|INLCR|IGNCR|ICRNL);

        // Output Modes
        tty.c_oflag &= ~OPOST; // Prevent special interpretation of output bytes (e.g. newline chars)
        //tty.c_oflag &= ~ONLCR; // Prevent conversion of newline to carriage return/line feed

        tty.c_cc[VMIN] = 0;
        tty.c_cc[VTIME] = 50;

        termios.tcflush(handle, TCIFLUSH);
        termios.tcsetattr(handle, TCSANOW, tty);

        /*
         * TermiosStructure tty = termios.tcgetattr(handle);
         * 
         * termios.cfmakeraw(tty);
         * int speed = rate.value;
         * 
         * termios.cfsetispeed(tty, speed);
         * termios.cfsetospeed(tty, speed);
         * 
         * tty.c_cflag |= (CLOCAL | CREAD) ;
         * tty.c_cflag &= ~PARENB ;
         * tty.c_cflag &= ~CSTOPB ;
         * tty.c_cflag &= ~CSIZE ;
         * tty.c_cflag |= CS8 ;
         * tty.c_lflag &= ~(ICANON | ECHO |
         *//* ECHOE | *//*
                         * ISIG | IEXTEN) ;
                         * tty.c_oflag &= ~OPOST ; // ++
                         * 
                         * tty.c_cc [VMIN] = 1 ;
                         * tty.c_cc [VTIME] = 5 ; // Ten seconds (100 deciseconds)
                         * 
                         * termios.tcflush(handle, TCIFLUSH);
                         * termios.tcsetattr(handle, TCSANOW, tty);
                         * 
                         * IntReference statusRef = new IntReference();
                         * 
                         * try {
                         * ioctl.ioctl(handle, Ioctl.TIOCMGET, statusRef);
                         * 
                         * int status = statusRef.getValue();
                         * 
                         * status |= Termios.TIOCM_DTR;
                         * status |= Termios.TIOCM_RTS;
                         * 
                         * statusRef.setValue(status);
                         * 
                         * ioctl.ioctl(handle, Ioctl.TIOCMSET, statusRef);
                         * } catch (NativeIOException e) {
                         * e.printStackTrace();
                         * }
                         */

        is = new SerialInputStream();
        os = new SerialOutputStream();
    }

    @Override
    public void close() {
        handle.close();
    }

    @Override
    public InputStream getInputStream() {
        return is;
    }

    @Override
    public FileHandle getHandle() {
        return handle;
    }

    @Override
    public OutputStream getOutputStream() {
        return os;
    }

    private final class SerialOutputStream extends OutputStream {
        /*
         * @Override
         * public void write(byte[] b) throws IOException {
         * fcntl.write(handle, b, b.length);
         * }
         * 
         * @Override
         * public void write(byte[] b, int off, int len) throws IOException {
         * byte[] slice = Arrays.copyOfRange(b, off, off+len);
         * System.out.println("write: " + Arrays.toString(slice));
         * fcntl.write(handle, slice, slice.length);
         * }
         */

        @Override
        public void write(int b) throws IOException {
            fcntl.write(handle, new byte[] { (byte) b }, 1);
        }

        @Override
        public void flush() {
            termios.tcflush(handle, Termios.TCIOFLUSH);
        }

        @Override
        public void close() throws IOException {
            handle.close();
        }
    }

    private final class SerialInputStream extends InputStream {
        /*
         * @Override
         * public int read(byte[] b) throws IOException {
         * return fcntl.read(handle, b, b.length);
         * }
         * 
         * @Override
         * public int read(byte[] b, int off, int len) throws IOException {
         * byte[] bb = new byte[len];
         * int result = fcntl.read(handle, bb, bb.length);
         * System.arraycopy(bb, 0, b, off, len);
         * return result;
         * }
         */

        @Override
        public long skip(long n) throws IOException {
            return fcntl.lseek(handle, n, Fcntl.Whence.SEEK_CUR);
        }

        @Override
        public int read() throws IOException {
            byte[] b = new byte[1];
            if (fcntl.read(handle, b, 1) != 1) {
                return -1;
            }
            return b[0] & 0xFF;
        }

        @Override
        public int available() throws IOException {
            IntReference ref = new IntReference();
            if (ioctl.ioctl(handle, Ioctl.FIONREAD, ref) == -1) {
                return -1;
            }
            return ref.getValue();
        }

        @Override
        public void close() throws IOException {
            handle.close();
        }
    }
}

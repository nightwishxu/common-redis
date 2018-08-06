package core.common.redis;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DefaultCodec {
    public DefaultCodec() {
    }

    protected byte[] encode(Object obj) throws IOException {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        byte[] bytesJava = (byte[])null;

        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            bytesJava = bos.toByteArray();
        } catch (IOException var9) {
            throw new IOException(var9);
        } finally {
            if (bos != null) {
                bos.close();
            }

            if (oos != null) {
                oos.close();
            }

        }

        return bytesJava;
    }

    protected Object decode(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        Object ret = null;

        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            ret = ois.readObject();
        } catch (IOException var10) {
            throw new IOException(var10);
        } catch (ClassNotFoundException var11) {
            throw new ClassNotFoundException("", var11);
        } finally {
            if (ois != null) {
                ois.close();
            }

        }

        return ret;
    }
}

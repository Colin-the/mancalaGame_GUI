package mancala;

import java.io.*;

public class Saver implements Serializable{
    private static final long serialVersionUID = 13L;
    private static final String ASSETS_FOLDER = "assets/";

    public void saveObject(final Serializable toSave, final String fileName) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(ASSETS_FOLDER + fileName))) {
            outputStream.writeObject(toSave);
        }
    }

    public Serializable loadObject(final String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(ASSETS_FOLDER + fileName))) {
            final Serializable loadedObject = (Serializable) inputStream.readObject();
            return loadedObject;
        }
    }
}

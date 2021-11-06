package Util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

public class DataHandler<T extends Object> {
 
    /**
     * @param object Object to be jsonified
     * @param path relative path under iteration_1
     * @param fileName should end with .json extension
     */
    public void writeToJson(T object, String path, String fileName) {
        try {
            checkExtension(fileName, ".json");

            Writer writer = new FileWriter("./iteration_1/" + path + "/" + fileName);
            new GsonBuilder().setPrettyPrinting().create().toJson(object, writer);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path relative path under iteration_1
     * @param fileName should end with .json extension
     * @return LinkedTreeMap<String, Object>
     */
    @SuppressWarnings("unchecked")
    public LinkedTreeMap<String, Object> parseFromJson(String path, String fileName) {
        try {
            checkExtension(fileName, ".json");

            Reader reader = new FileReader("./iteration_1/" + path + "/" + fileName);
            Type typeOfT = new TypeToken<T>(){}.getType();
            T returnableObj = new GsonBuilder().create().fromJson(reader, typeOfT);
            reader.close();
            LinkedTreeMap<String, Object> treeMap = (LinkedTreeMap<String, Object>)returnableObj;
            return treeMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param fileName file name to check
     * @param extension intented extension
     * @throws Error prints @param extension
     */
    private void checkExtension(String fileName, String extension) throws Error {
        if (!fileName.endsWith(extension)) throw new Error("File name should end with " + extension);
    }

}
package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;


public class Test {
    public final static void main(String[] args) throws IOException {

        System.out.println("User=" + System.getProperty("user.name")); //platform independent

        File testFile = new File("/home/app/api/src/main/resources/schema/requests/signupRequest.schema.json");
        System.out.println("File "+testFile+" exists=" + testFile.exists());

        InputStream is = new FileInputStream(testFile);
        System.out.println("Opened file "+is);


        Path parentDirectory = new File(args[0]).toPath();
        String file = args[1];
        System.out.println("Resolving " + args[1] + " from " + args[0]);

        Path pathToUse = parentDirectory.resolve(args[1]).normalize();

        System.out.println("Resolved " + pathToUse);
        String result = readAll(pathToUse);

        if (result == null) {
            System.out.println("Failed to read results, trying classpath");

            InputStream inputStream = Test.class.getResourceAsStream(file);

            if (inputStream == null) {
                inputStream = Test.class.getClassLoader().getResourceAsStream(file);
            }

            if (inputStream == null) {
                inputStream = ClassLoader.getSystemResourceAsStream(file);
            }

            if (inputStream != null) {
                try {
                    result = readAll(inputStream);

                } catch (IOException e) {
                    throw new RuntimeException("Could not read " + file + " from the classpath", e);
                }
            }

            if (result == null) {
                throw new RuntimeException("Could not find " + file + " on the classpath");
            } else {
                System.out.println("!!FOUND!!");
            }

        }else {
            System.out.println("!!FOUND!!");
        }

    }

    private static String readAll(Path path) throws IOException {
        try (InputStream inputStream = new FileInputStream(path.toFile())) {
            return readAll(inputStream);
        }
    }

    private static String readAll(InputStream inputStream) throws IOException {
        byte[] data = new byte[inputStream.available()];
        inputStream.read(data);
        return new String(data);
    }
}
package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public abstract class FileDao<T> implements Dao<T> {

    private final String directory;
    private static final Logger logger = LogManager.getLogger(FileDao.class);

    public FileDao(String directory) {
        this.directory = directory;
        try {
            Files.createDirectories(Paths.get(directory));
        } catch (IOException e) {
            logger.error("Could not create directory: " + directory, e);
            throw new UncheckedIOException("Failed to create directory: " + directory, e);
        }
    }

    @SuppressWarnings("unchecked")
    public T read(String name) {
        Path filePath = Paths.get(this.directory, name);
        try (FileInputStream fileInputStream = new FileInputStream(filePath.toFile());
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            logger.error("Class not found while reading the file: " + filePath, e);
            throw new RuntimeException("Class not found during deserialization: " + filePath, e);
        } catch (StreamCorruptedException e) {
            logger.error("Stream corruption while reading the file: " + filePath, e);
            throw new RuntimeException("Stream corrupted during deserialization: " + filePath, e);
        } catch (FileNotFoundException e) {
            logger.error("File not found: " + filePath, e);
            throw new RuntimeException("File not found: " + filePath, e);
        } catch (IOException e) {
            logger.error("I/O error while reading the file: " + filePath, e);
            throw new RuntimeException("I/O error while reading the file: " + filePath, e);
        }
    }

    public void write(String name, T obj) {
        Path filePath = Paths.get(this.directory, name);
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(obj);
        } catch (FileNotFoundException e) {
            logger.error("File not found: " + filePath, e);
            throw new RuntimeException("Failed to create or open the file: " + filePath, e);
        } catch (IOException e) {
            logger.error("I/O error while writing to file: " + filePath, e);
            throw new RuntimeException("I/O error when writing to file: " + filePath, e);
        }
    }

    public List<String> names() {
        try (Stream<Path> files = Files.walk(Paths.get(this.directory)).filter(Files::isRegularFile)) {
            return files.map(Path::getFileName).map(Path::toString).toList();
        } catch (IOException e) {
            logger.error("Failed to list files in directory: " + directory, e);
            throw new RuntimeException("Failed to access files in directory: " + directory, e);
        }
    }

    public void close() throws Exception {
    }
}

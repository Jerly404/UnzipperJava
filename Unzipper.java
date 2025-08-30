package descomprimidor;

import java.io.*;
import java.util.zip.*;

/**
 * Class responsible for extracting ZIP files.
 */
public class Unzipper {

    public static void main(String[] args) {
        String zipFilePath = "D:\\Trash\\Practice\\appointmentapp-backend.zip";   
        String destinationDir = "D:\\Trash\\Practice\\"; 

        Unzipper unzipper = new Unzipper();
        try {
            unzipper.unzip(zipFilePath, destinationDir);
            System.out.println("✅ Unzipping completed successfully!");
        } catch (IOException e) {
            System.err.println("❌ Error during unzipping: " + e.getMessage());
        }
    }

    /**
     * Unzips a ZIP file into the specified destination folder.
     * @param zipFilePath Path to the ZIP file
     * @param destinationDir Folder where files will be extracted
     * @throws IOException If a read/write error occurs
     */
    public void unzip(String zipFilePath, String destinationDir) throws IOException {
        createDirectoryIfNotExists(destinationDir);

        // Open the ZIP file using try-with-resources to auto-close the stream
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                String fullPath = destinationDir + File.separator + entry.getName();

                if (entry.isDirectory()) {
                    createDirectoryIfNotExists(fullPath); // Create folders
                } else {
                    extractFile(zipIn, fullPath); // Extract files
                }

                zipIn.closeEntry(); // Close current entry
            }
        }
    }

    /**
     * Extracts a single file from the ZIP input stream to disk.
     * @param zipIn ZIP input stream
     * @param filePath Path where the file will be saved
     * @throws IOException If an error occurs while writing the file
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        File file = new File(filePath);
        createDirectoryIfNotExists(file.getParent()); // Ensure parent folder exists

        // Write the file using a buffer and try-with-resources
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = zipIn.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * Creates a directory if it doesn't exist.
     * @param dirPath Path of the directory
     * @throws IOException If the directory cannot be created
     */
    private void createDirectoryIfNotExists(String dirPath) throws IOException {
        if (dirPath == null) return;
        File dir = new File(dirPath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create directory: " + dirPath);
        }
    }
}

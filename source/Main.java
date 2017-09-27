/**
 * Created by EE on 26.09.2017.
 */

import javax.xml.stream.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.stream.*;
import java.util.zip.*;
import java.util.*;

public class Main {
    public static final Charset CP866 = Charset.forName("CP866");

    public static final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

    public static void main(String[] args) {
        String dir = null;

        if (dir == null) {
            Scanner scanner = new Scanner(System.in);
            dir = scanner.nextLine();
        }

        readDirectory(new File(dir));
    }

    public static void readDirectory(File file) {
        for (File entry : file.listFiles()) {
            if (entry.isDirectory()) {
                Main.readDirectory(entry);
            }
            else {
                try {
                    Main.readZip(entry);
                }
                catch (IOException e) {
                    // is not zip
                }
            }
        }
    }

    public static void readZip(File file) throws IOException {
        ZipFile zipFile = new ZipFile(file, Main.CP866);

        String zipAbsolutePath = file.getAbsolutePath();

        Stream<? extends ZipEntry> entries = zipFile.stream();

        Iterator<? extends ZipEntry> iterator = entries.iterator();

        while (iterator.hasNext()) {
            ZipEntry entry = iterator.next();

            InputStream inputStream = zipFile.getInputStream(entry);

            try {
                Main.readXml(entry, inputStream, zipAbsolutePath);
            }
            catch (XMLStreamException e) {
                // is not xml
            }

            inputStream.close();
        }

        zipFile.close();
    }

    public static void readXml(ZipEntry entry, InputStream inputStream, String zipAbsolutePath) throws XMLStreamException {
        if (!entry.isDirectory()) {
            String entryName = entry.getName();
            XMLStreamReader reader = Main.xmlInputFactory.createXMLStreamReader(inputStream);

            while (reader.hasNext()) {
                int type = reader.next();

                if (reader.isCharacters()) {
                    try {
                        String characters = reader.getText();

                        int lineNumber = reader.getLocation().getLineNumber();

                        Main.printURL(characters, lineNumber, entryName, zipAbsolutePath);
                    }
                    catch (MalformedURLException e) {
                        // is not url
                    }
                }
            }

            reader.close();
        }
    }

    public static void printURL(String string, int lineNumber, String entryName, String zipAbsolutePath) throws MalformedURLException {
        URL url = new URL(string);

        System.out.println("zip \t" + zipAbsolutePath);
        System.out.println("xml \t" + entryName);
        System.out.println("line\t" + lineNumber);
        System.out.println("url \t" + url.toString());
        System.out.println();
    }
}


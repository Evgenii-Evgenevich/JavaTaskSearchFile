/**
 * Created by EE on 26.09.2017.
 */

import javax.xml.parsers.*;
import javax.xml.stream.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.stream.*;
import java.util.zip.*;
import java.util.*;

public class Main {
    public static final Charset CP866 = Charset.forName("CP866");

    private static String zipAbsolutePath = null;

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
        ZipFile zipFile = new ZipFile(file, CP866);

        Stream<? extends ZipEntry> entries = zipFile.stream();

        Iterator<? extends ZipEntry> iterator = entries.iterator();

        while (iterator.hasNext()) {
            ZipEntry entry = iterator.next();

            InputStream inputStream = zipFile.getInputStream(entry);

            try {
                Main.zipAbsolutePath = file.getAbsolutePath();

                Main.readXml(entry, inputStream);
            }
            catch (XMLStreamException e) {
                // is not xml
            }

            inputStream.close();
        }

        zipFile.close();
    }

    public static void readXml(ZipEntry entry, InputStream inputStream) throws XMLStreamException {
        if (!entry.isDirectory()) {
            boolean isxml = entry.getName().endsWith(".xml");

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream);

            while (reader.hasNext()) {
                int type = reader.next();

                if (reader.isCharacters())
                {
                    String string = reader.getText();

                    try {
                        int lineNumber = reader.getLocation().getLineNumber();
                        printURL(string, lineNumber, entry.getName());
                    }
                    catch (MalformedURLException e) {
                        // is not url
                    }
                }
            }

            reader.close();
        }
    }

    public static void printURL(String string, int lineNumber, String entryName) throws MalformedURLException {
        URL url = new URL(string);

        System.out.println("zip \t" + Main.zipAbsolutePath);
        System.out.println("xml \t" + entryName);
        System.out.println("line\t" + lineNumber);
        System.out.println("url \t" + url.toString());
        System.out.println();
    }
}

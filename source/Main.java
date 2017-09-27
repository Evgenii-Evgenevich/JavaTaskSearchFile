/**
 * Created by EE on 26.09.2017.
 */

import javax.xml.parsers.*;
import javax.xml.stream.*;
import java.io.*;
import java.net.*;
import java.util.stream.*;
import java.util.zip.*;
import java.util.*;

public class Main {

    private static String zipAbsolutePath = null;

    public static void main(String[] args) {
        System.out.println("Search for");

        Scanner scanner = new Scanner(System.in);

        File dir = new File(scanner.nextLine());

        readDirectory(dir);
    }

    private static void readDirectory(File file) {

        for (File entry : file.listFiles()) {
            if (entry.isDirectory()) {
                Main.readDirectory(entry);
            }
            else {
                try {
                    Main.readZip(entry);
                }
                catch (IOException e) {}
            }
        }
    }

    private static void readZip(File file) throws IOException {
        ZipFile zipFile = new ZipFile(file);

        Stream<? extends ZipEntry> entries = zipFile.stream();

        Iterator<? extends ZipEntry> iterator = entries.iterator();

        while (iterator.hasNext()) {
            ZipEntry entry = iterator.next();

            InputStream inputStream = zipFile.getInputStream(entry);

            try {
                zipAbsolutePath = file.getAbsolutePath();

                Main.readXml(entry, inputStream);
            }
            catch (XMLStreamException e) {}
        }

        zipFile.close();
    }

    public static void readXml(ZipEntry entry, InputStream inputStream) throws IOException, XMLStreamException {
        if (!entry.isDirectory()) {
            boolean isxml = entry.getName().endsWith(".xml");

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream);

            while (reader.hasText()) {
                String string = reader.getText();

                try {
                    URL url = new URL(string);
                    int lineNumber = reader.getLocation().getLineNumber();

                    System.out.println("zip\t" + Main.zipAbsolutePath);
                    System.out.println("xml\t" + entry.getName());
                    System.out.println("line\t" + lineNumber);
                    System.out.println("url\t" + url.toString());
                } catch (MalformedURLException e) {
                    // is not url
                }
            }
        }
    }
}

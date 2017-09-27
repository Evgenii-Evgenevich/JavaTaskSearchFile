/**
 * Created by EE on 26.09.2017.
 */

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.stream.*;
import java.util.zip.*;
import java.util.*;

public class Main {

    private static String zipName = null;

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        System.out.println("Search for");

        Scanner scanner = new Scanner(System.in);

        zipName = scanner.nextLine();

        if (true) {
            ZipInputStream zipStream = new ZipInputStream(new FileInputStream(zipName));

            ZipEntry entry = null;

            while ((entry = zipStream.getNextEntry()) != null) {
                Main.readEntry(entry, zipStream);
            }

            zipStream.close();
        }
        else {
            ZipFile zipFile = new ZipFile(zipName);

            Stream<? extends ZipEntry> entries = zipFile.stream();

            Iterator<? extends ZipEntry> iterator = entries.iterator();

            while (iterator.hasNext()) {
                ZipEntry entry = iterator.next();

                InputStream in = zipFile.getInputStream(entry);

                Main.readEntry(entry, in);
            }

            zipFile.close();
        }
    }

    public static void readEntry(ZipEntry entry, InputStream in) throws IOException, ParserConfigurationException, SAXException {
        boolean isxml = entry.getName().endsWith(".xml");

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

        Document document = docBuilder.parse(in);

        //...
        //...
    }
}

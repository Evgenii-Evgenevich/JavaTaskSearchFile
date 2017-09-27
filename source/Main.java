/**
 * Created by EE on 26.09.2017.
 */

import java.io.*;
import java.util.stream.*;
import java.util.zip.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Search for");

        Scanner scanner = new Scanner(System.in);

        String zipName = scanner.nextLine();

        if (true) {
            ZipInputStream zipStream = new ZipInputStream(new FileInputStream(zipName));

            ZipEntry entry = null;

            while ((entry = zipStream.getNextEntry()) != null) {
                Main.readEntry(entry);
            }
        }
        else {
            ZipFile zipFile = new ZipFile(zipName);

            Stream<? extends ZipEntry> entries = zipFile.stream();

            Iterator<? extends ZipEntry> iterator = entries.iterator();

            while (iterator.hasNext()) {
                Main.readEntry(iterator.next());
            }
        }
    }


    public static void readEntry(ZipEntry entry) {

    }
}

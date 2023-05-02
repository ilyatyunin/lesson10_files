package ru.betboom.parsing;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.betboom.Human;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class FileParsingTest {
    private ClassLoader cl = FileParsingTest.class.getClassLoader();
    @Test
    void pdfParseTest()  throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File download = $("a[href*='junit-user-guide-5.9.3.pdf']").download();
        PDF pdf = new PDF(download);
        Assertions.assertEquals("Stefan Bechtold, Sam Brannen, Johannes Link, Matthias Merdes, Marc Philipp, Juliette de Rancourt, Christian Stein", pdf.author);
    }

    @Test
    void xlsParseTest() throws Exception {
        open("https://excelvba.ru/programmes/Teachers");
        File download = $("a[href*='teachers.xls']").download();
        XLS xls = new XLS(download);
        Assertions.assertTrue(
                xls.excel.getSheetAt(0).getRow(3).getCell(2).getStringCellValue()
                        .startsWith("1. Суммарное количество часов планируемое на штатную по всем разделам плана  должно \n" +
                                "составлять примерно 1500 час в год.  ")
        );
    }

    @Test
    void csvParseTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("expectedfiles/qaguru.csv");
             InputStreamReader isr = new InputStreamReader(is)) {
            CSVReader csvReader = new CSVReader(isr);
            List<String[]> content = csvReader.readAll(); // вернет 4 элемента из 2х элементов каждый
            Assertions.assertArrayEquals(new String[] {"Тучс", "JUnit5"}, content.get(1));
        }
    }

    @Test
    void filesEquilsTest() throws Exception {
        open("https://github.com/ilyatyunin/lesson10_files/blob/main/src/test/resources/expectedfiles/qaguru.csv");
        File download = $("a[href*='/ilyatyunin/lesson10_files/raw/main/src/test/resources/expectedfiles/qaguru.csv']").download();
        try (InputStream isExpected = cl.getResourceAsStream("expectedfiles/qaguru.csv");
        InputStream downloaded = new FileInputStream(download)
        ) {
            Assertions.assertArrayEquals(isExpected.readAllBytes(), downloaded.readAllBytes());
        }
    }

    @Test
    void zipTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("reqq.zip");
             ZipInputStream zs = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zs.getNextEntry()) != null) { // возвращает файлы по очереди
                Assertions.assertTrue(entry.getName().contains("reqq.txt"));
            }
        }
    }

    @Test
    void jsonCleverTest() throws Exception {
        Gson gson = new Gson();
        try (InputStream is = cl.getResourceAsStream("human.json");
             InputStreamReader isr = new InputStreamReader(is)) {
            Human human = gson.fromJson(isr, Human.class);
            Assertions.assertTrue(human.isClever);
            Assertions.assertEquals(33, human.age);
        }


    }

}

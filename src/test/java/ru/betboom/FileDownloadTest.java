package ru.betboom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class FileDownloadTest {
    @Test
    void downloadTest() throws Exception {
        open("https://github.com/qa-guru/niffler/blob/master/README.md");
        File download = $("a[href='/qa-guru/niffler/raw/master/README.md']").download();

        try (InputStream is = new FileInputStream(download)) {
            byte[] bytes = is.readAllBytes(); // прочитать все содержимое файла в виде массива байтс
            String fileAsString = new String(bytes, StandardCharsets.UTF_8); // создание строки из массива байтов
            Assertions.assertTrue(fileAsString.contains("Технологии, использованные в Niffler"));
        }
    }


    @Test
    void uploadTest() throws Exception {
        Selenide.open("https://tus.io/demo.html");
        $("input[type='file']").uploadFromClasspath("2023-02-20 09.51.20.jpg");
        $("#js-upload-container").shouldHave(text("The upload is complete!"));

    }
}

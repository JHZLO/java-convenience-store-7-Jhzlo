package store.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {
    public static List<String> readFileData(String fileName) {
        Path path = Paths.get("src/main/resources", fileName); // 파일 경로를 설정

        try {
            return Files.lines(path).skip(1).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
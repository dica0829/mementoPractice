package fileSystem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class File implements FileSystemComponent {
    private String name;
    private long size;
    private int depth;

    public File(String name, long size, int depth) {
        this.name = name;
        this.size = size;
        this.depth = depth;
    }
    public File() { // 기본 생성자
        this("", 0, 0);
    }

    @Override // 현재 파일의 이름과 사이즈를 출력
    public void display() { // 파일 이름과 사이즈를 출력, 깊이에 따른 들여쓰기
        System.out.printf(" ".repeat(depth)+"%s (%d B)\n", name, size);
    }

    @Override // 파일 사이즈를 반환
    public long getSize() { // 사이즈 반환
        return size;
    }

    // 과제2
    @Override
    public String serialize() {//File:name (size B) depth: depth
        return "File:" + name + " (" + size + " B) depth:" + depth + "\n";
    }

    @Override
    public void deserialize(String opaque) {
        Pattern pattern = Pattern.compile("(.+)/File:(.+) \\((\\d+) B\\) depth:(\\d+)");
        Matcher matcher = pattern.matcher(opaque);

        if (matcher.matches()) {
            this.name = matcher.group(2).trim();           // 파일 이름 (공백 포함)
            this.size = Long.parseLong(matcher.group(3));  // 파일 크기
            this.depth = Integer.parseInt(matcher.group(4)); // 깊이
        } else {
            throw new IllegalArgumentException("Invalid format: " + opaque);
        }
    }
}
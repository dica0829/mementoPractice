package fileSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Directory implements FileSystemComponent {
    private String name; // 디렉토리 이름
    private int depth; // 디렉토리 깊이
    public ArrayList<FileSystemComponent> files = new ArrayList<>(); // 파일 목록을 읽기 위한 리스트

    public Directory() { //기본 최상위 생성자
        this(".", 0);
    }

    public Directory(String name, int depth) { // 디렉토리 지정 생성자
        this.name = name;
        this.depth = depth;
    }

    public void add(FileSystemComponent component) { // component에 파일 추가
        files.add(component); // 컴포넌트 추가
    }

    @Override
    public void display() { // 현재 있는 디렉토리의 파일들을 보여준다.
        System.out.printf(" ".repeat(depth)+ "%s/ (total: %d B)\n", name, this.getSize());
        for (FileSystemComponent file : files) {
            file.display();
        }
    }

    @Override
    public long getSize() { // 디렉토리 사이즈는 하위 파일의 사이즈를 재귀적으로 다 더한 뒤 출력
        long sum = 0; // 사이즈 합계
        for (FileSystemComponent file : files) {
            sum += file.getSize();
        }
        return sum;
    }

    // 과제2
    @Override
    public String serialize() { //Dir:name/ (total: size) depth: depth
        String result = "Dir:" + name + "/ (total: " + this.getSize() + " B) depth:" + depth + "\n";
        for (FileSystemComponent file : files) {
            result += name + "/" + file.serialize();
        }
        return result;
    }

    @Override
    public void deserialize(String opaque) {
        String[] lines = opaque.split("\n"); //한줄마다 잘라넣기
        lines = Arrays.copyOfRange(lines, 1, lines.length); //최상위 디렉토리를 시작으로 넣을 것이니 index 0을 제외
        deserializeRecursive(this, lines, 0);
    }

    public int deserializeRecursive(Directory current, String[] lines, int index) {
        //정규식
        // (.+) : 공백을 포함한 하나 이상의 문자
        // (\\d+) : 숫자 1개 이상
        Pattern dirPattern = Pattern.compile("(.+)/Dir:(.+)/ \\(total: (\\d+) B\\) depth:(\\d+)");
        while (index < lines.length) { //읽은 라인만큼 확인
            String line = lines[index];
            Matcher matcher = dirPattern.matcher(line); // 디렉토리의 정규식이 line과 맞는지 검사
            int depth = Integer.parseInt(line.replaceAll(".*depth:", ""));

            // 현재 디렉토리의 depth보다 이 파일의 depth가 작거나 같으면 디렉토리에서 빠져나옴
            // (크다면 디렉토리 안 파일임)
            if (depth <= current.depth) {
                break;
            }

            if (matcher.matches()) {// 디렉토리인 경우 현 디렉토리에 추가하고 재귀 시작
                String dirName = matcher.group(2); //정규식의 이름을 갖고옴 (.+)
                Directory dir = new Directory(dirName, depth);//현재 depth와 이름을 새로운 디렉토리로 만듦
                current.add(dir);
                index = deserializeRecursive(dir, lines, index + 1);// 재귀로 돌고 난 뒤 다음 인덱스를 반환
            } else {
                // 파일인 경우 파일 복원 후 현 디렉토리에 파일 추가
                File file = new File();
                file.deserialize(line);
                current.add(file);
                index++;
            }
        }
        return index;
    }
}

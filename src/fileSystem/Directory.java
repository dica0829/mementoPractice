package fileSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Directory implements FileSystemComponent {
    private String name; // 폴더 이름
    private int depth; // 폴더 깊이
    public ArrayList<FileSystemComponent> files = new ArrayList<>(); // 파일 목록을 읽기 위한 리스트

    public Directory() { //기본 최상위 생성자
        this.name = ".";
        this.depth = 0;
    }

    public Directory(String name, int depth) { // 폴더 지정 생성자
        this.name = name;
        this.depth = depth;
    }

    public void add(FileSystemComponent component) { // component에 파일 추가
        files.add(component); // 컴포넌트 추가
    }

    @Override
    public void display() { // 현재 있는 폴더의 파일들을 보여준다.
        System.out.printf(" ".repeat(depth)+ "%s/ (total: %d B)\n", name, this.getSize());
        for (FileSystemComponent file : files) {
            file.display();
        }
    }

    @Override
    public long getSize() { // 폴더 사이즈 구하기
        long sum = 0; // 사이즈 합계
        for (FileSystemComponent file : files) {
            sum += file.getSize(); // 하위 파일의 사이즈를 구한다.
        }
        return sum;
    }

    // 과제2

    @Override
    public String serialize() { //폴더제목/ (total: 사이즈) depth: 깊이
        String result = name + "/ (total: " + this.getSize() + " B) depth: " + depth + "\n";
        for (FileSystemComponent file : files) {
            result += file.serialize(); // 문자열 결합
        }
        return result;
    }

    @Override
    public void deserialize(String opaque) {
        String[] lines = opaque.split("\n"); //한줄마다 잘라넣기
        lines = Arrays.copyOfRange(lines, 1, lines.length); //최상위 디렉토리를 stack에 넣었으니 index 0을 제외
        Stack<Directory> stack = new Stack<>(); // 부모 디렉토리를 저장하는 스택
        stack.push(this); // 현재 Directory가 최상위 디렉토리
        for (String line : lines) {
            //현재 파일의 깊이 파악
            int depth = Integer.parseInt(line.replaceAll(".*depth: ", ""));

            // 현재 파일의 깊이가 이전 폴더랑 같거나 더 얕다면 이전 폴더를 스택에서 삭제하여 부모를 최신화
            while (stack.size() > 1 && stack.peek().depth >= depth) {
                stack.pop();
            }
            if (line.contains("/ (total:")) {
                // 디렉토리인 경우
                String dirName = line.split("/")[0];
                Directory dir = new Directory(dirName, depth);
                stack.peek().add(dir);  // 부모 디렉토리에 자식 디렉토리 추가
                stack.push(dir);  // 새 디렉토리를 스택 최상위에 추가
            } else {
                // 파일인 경우
                File file = new File();
                file.deserialize(line); // 파일 복원
                stack.peek().add(file);  // 부모 디렉토리에 파일 추가
            }
        }

        /*
        1. depth 비교
        2. 파일이면 부모 디렉토리에 파일 추가
        3. 폴더면 디렉토리 추가 하고 그 디렉토리를 재귀로 판단
         */
    }
}

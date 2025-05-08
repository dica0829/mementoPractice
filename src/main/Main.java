package main;

import fileSystem.Directory;
import fileSystem.File;
import fileSystem.FileSystemComponent;

public class Main {
    public static void main(String[] args) {
        int depth = 1; // 가독성을 위한 들여쓰기 갯수 처음은 무조건 최상위 디렉토리 안 파일들이므로 기본은 1
        FileSystemComponent directory = new Directory();
        loadDirectory(new java.io.File("."), directory, depth);
        // 이제 디렉토리의 모든 파일과 디렉토리를 읽었으므로 출력. 재귀적으로 동작할 것이다.
        directory.display();

        //2번 과제
        System.out.println("--------------2번 과제 memento--------------------");
        String opaque = directory.serialize();
        FileSystemComponent newDirectory = new Directory();
        newDirectory.deserialize(opaque);
        newDirectory.display();
    }

    public static void loadDirectory(java.io.File root, FileSystemComponent directory, int depth) {
        java.io.File[] files = root.listFiles(); // 루트 디렉토리의 파일 리스트를 받아옴
        for (java.io.File file : files) {
            if (file.isFile()) { // 파일일 떄는 현재 디렉토리에 파일 추가
                FileSystemComponent f = new File(file.getName(), file.length(), depth);
                ((Directory) directory).add(f);
            } else if (file.isDirectory()) { // 디렉토리라면 재귀적으로 조사
                FileSystemComponent d = new Directory(file.getName(), depth);
                ((Directory) directory).add(d);
                depth++; // 하위 디렉토리 진입하면 깊이 1개 추가
                loadDirectory(file, d, depth);
                depth--; // 하위 디렉토리에서 빠져나오면 깊이 1개 빼기
            }
        }
    }
}
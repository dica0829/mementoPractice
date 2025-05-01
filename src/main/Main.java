package main;

import fileSystem.Directory;
import fileSystem.FileSystemComponent;
import fileSystem.File;

public class Main {
    public static void main(String[] args) {
        int depth = 1; // 가독성을 위한 들여쓰기 갯수 처음은 무조건 최상위 폴더 안 파일들이므로 기본은 1
        FileSystemComponent directory = new Directory(); // 최상위이므로 depth 0
        //시작 디렉토리는 최상위 폴더
        loadDirectory(new java.io.File("."), directory, depth); // 최상위 폴더, 시작 디렉토리, 깊이를 집어넣는다.
        // 이제 디렉터리의 모든 파일과 디렉터리를 읽었으므로 출력. 재귀적으로 동작할 것이다.
        directory.display();

        //2번 과제
        System.out.println("--------------2번 과제 memento--------------------");
        String opaque = directory.serialize();
        System.out.println(opaque);
        FileSystemComponent newDirectory = new Directory();
        newDirectory.deserialize(opaque);
        newDirectory.display();
    }

    public static void loadDirectory(java.io.File root, FileSystemComponent directory, int depth) {
        java.io.File[] files = root.listFiles(); // 루트 폴더의 파일 리스트를 받아옴
        for (java.io.File file : files) { // 파일 리스트 하나하나 확인
            if (file.isFile()) { // 파일일 떄
                FileSystemComponent f = new File(file.getName(), file.length(), depth); // 파일 이름, 사이즈 받아오기
                ((Directory) directory).add(f); //디렉토리에 추가
            } else if (file.isDirectory()) { // 폴더일 때
                FileSystemComponent d = new Directory(file.getName(), depth); // 폴더의 이름 받아오기
                ((Directory) directory).add(d); // 디렉토리에 추가
                depth++; // 하위 폴더 진입하면 깊이 1개 추가
                loadDirectory(file, d, depth); // 루트 폴더에 지금 탐색하는 폴더를 넣고 하위 파일에 대해 다시 조사
                depth--; // 하위 폴더에서 빠져나오면 깊이 1개 빼기
            }
        }
    }
}
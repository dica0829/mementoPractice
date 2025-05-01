package fileSystem;

public interface FileSystemComponent {

    // 과제1 내용
    public void display(); //파일의 이름과 사이즈를 출력
    public long getSize(); // 파일의 사이즈를 반환한다. 하위 파일의 총 사이즈를 위한 함수

    // 과제2 내용
    public String serialize();
    public void deserialize(String opaque);
}
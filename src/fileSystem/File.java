package fileSystem;

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

    @Override
    public void display() { // 파일 이름과 사이즈를 출력, 깊이에 따른 들여쓰기
        System.out.printf(" ".repeat(depth)+"%s (%d B)\n", name, size);
    }

    @Override
    public long getSize() { // 사이즈 반환
        return size;
    }

    // 과제2
    @Override
    public String serialize() {//name[0] (size[1] B)[2] 깊이:[3] depth[4]
        return name + " (" + size + " B) depth: " + depth + "\n";
    }

    @Override
    public void deserialize(String opaque) {
        String line = opaque.trim();
        String[] parts = line.split(" "); // 공백을 기준으로 슬라이싱
        this.name = parts[0]; // 이름은 항상 처음에 있음
        //(사이즈 B)에서 사이즈만 빼오기
        this.size = Long.parseLong(parts[1].replace("(", "").replace("B)", ""));
        this.depth = Integer.parseInt(parts[4]); //깊이는 항상 4번에 위치
    }
}
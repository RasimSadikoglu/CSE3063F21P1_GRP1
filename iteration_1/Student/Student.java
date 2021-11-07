package Student;

public class Student {
    private String id;
    private Transcript transcript;
    public Student(String id, Transcript transcript) {
        this.id = id;
        this.transcript = transcript;
    }

    public String getId() {
        return id;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }
}
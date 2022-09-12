package notes;

public class Note {
    private String title, content;

    public Note(String title, String content) {
        if (title.equals(""))
            this.setTitle("(untitled)");
        else
            this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        return this.getTitle() + " - " + this.getContent();
    }
}

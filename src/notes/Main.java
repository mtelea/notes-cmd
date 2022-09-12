package notes;

import java.io.*;
import java.util.ArrayList;

public class Main {

    private static final File f = new File("notes.txt");
    private static final boolean dbg = false;
    private static boolean loaded = false;
    private static boolean firstRun = true;

    private static ArrayList<Note> noteArray = new ArrayList<Note>();

    public static void main(String args[]) {

        if(args.length == 0 || args.length > 3) {
            System.out.println("Incorrect arguments provided");
            return;
        }

        if (args[0].equalsIgnoreCase("-list") && args.length == 2) {	// list note titles
            listNote(args[1]);
        } else if (args[0].equalsIgnoreCase("-list")) {    // list specified note
            listNoteTitles();
        } else if (args[0].equalsIgnoreCase("-add") && args.length == 3) {	// add
            addNote(args[1], args[2]);
        }

        if (!(usableFile(f, dbg))) {
            System.err.println("File not usable");
        } else {
            loadNotesFile();
            firstRun = false;
        }
    }

    public static void loadNotesFile() {
        if (!(loaded)) {
            for (Note n :readFile(f)) {
                noteArray.add(n);
            }
            loaded = true;
        }
    }

    public static void listNoteTitles() {
        if (!(usableFile(f, dbg))) {
            System.err.println("File not usable");
        } else {
            loadNotesFile();
            if (noteArray.size() == 0) {
                System.out.println("There are no notes to show.");
                return;
            }
            for (Note n: noteArray) {
                int separatorIndex = n.toString().indexOf(" - ");
                System.out.println(noteArray.indexOf(n) + 1 + " - " +n.toString().substring(0, separatorIndex));
            }
        }
    }

    public static void listNote(String noteTitle) {
        if (!(usableFile(f, dbg))) {
            System.err.println("File not usable");
        } else {
            loadNotesFile();
            if (noteArray.size() == 0) {
                System.out.println("No notes to show");

                return;
            }
            for (Note n: noteArray) {
                int separatorIndex = n.toString().indexOf(" - ");
                if(noteTitle.equals(n.toString().substring(0, separatorIndex))) {
                    System.out.println(n);
                }
            }
        }
    }

    public static void addNote(String noteTitle, String noteContent) {
        if (!(usableFile(f, dbg))) {
            System.err.println("File not usable");
        } else {
            loadNotesFile();
            Note addition = new Note(noteTitle, noteContent);
            printToFile(f, true, addition);
            noteArray.add(addition);
            listNote(noteTitle);
        }
    }

    public static void printToFile(File out, boolean append, Note input) {
        if (!(usableFile(out, dbg))) {
            System.err.println("File not usable");
            return;
        }

        FileWriter outStream = null;
        try {
            outStream = new FileWriter(out, append);

            outStream.write(input.getTitle() + " - " + input.getContent() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean usableFile(File in, boolean debug) {
        if (!(in.exists())) {
            if (debug)
                System.out.println("File does not exist");
            try {
                if (in.createNewFile()) {
                    if (debug || firstRun){}
                        System.out.println("File created");
                } else {
                    System.err.println("Error creating file");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (!(in.canWrite()) || !(in.canRead())) {
            System.out.println("File permissions not set");
            try {
                if (in.setReadable(true, true)) {
                    if (debug || firstRun){}
                        System.out.println("Setting read permissions");
                } else {
                    System.err.println("Error setting read permissions");
                }

                if (in.setWritable(true, true)) {
                    if (debug || firstRun){}
                        System.out.println("Setting write permissions");
                } else {
                    System.err.println("Error setting write permissions");
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<Note> readFile(File in) {
        ArrayList<Note> out = new ArrayList<Note>();
        BufferedReader inStream = null;
        if (!(usableFile(in, dbg))) {
            System.err.println("File not usable");
            return null;
        }
        try {
            inStream = new BufferedReader(new FileReader(in));

            String str;
            while ((str = inStream.readLine()) != null) {
                for (String splitNewline: str.split("\n")) { // separates by line
                    String splitContent = splitNewline.substring(splitNewline.indexOf(" - ") + 3);
                    String splitTitle = splitNewline.substring(0, splitNewline.indexOf(" - "));
                    out.add(new Note(splitTitle, splitContent));
                }
            }
        } catch (IOException e) {
            System.out.println("File did not exist and it was created.");
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return out;
    }
}
import java.io.FileNotFoundException;

public class Main {

    private static final String FILE_PATH_FIRST = "src/main/java/samplecode/ReversePolishNotation.java";
    private static final String FILE_PATH_SECOND = "src/main/java/samplecode/ReversePolishNotationCopy.java";

    public static void main(String[] args) throws FileNotFoundException {

        System.out.println(CodeComparator.getFilesSimilarity(FILE_PATH_FIRST, FILE_PATH_SECOND));

    }

}

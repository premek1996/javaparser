import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.List;

public class CodeComparator {

    public static double getFilesSimilarity(String filePathFirst, String filePathSecond) {
        List<MethodDeclaration> methodsFileFirst = AbstractSyntaxTreeReader.getAllMethods(filePathFirst);
        List<MethodDeclaration> methodsFileSecond = AbstractSyntaxTreeReader.getAllMethods(filePathSecond);
        return methodsFileFirst.stream()
                .mapToDouble(method ->getMaxSimilarity(method, methodsFileSecond))
                .sum()/methodsFileFirst.size();
    }

    private static double getMaxSimilarity(MethodDeclaration methodToCompare, List<MethodDeclaration> methods) {
        return methods.stream()
                .mapToDouble(method->getMethodsSimilarity(method.toString(), methodToCompare.toString()))
                .max()
                .orElse(-1);
    }

    private static double getMethodsSimilarity(String methodFirst, String methodSecond) {
        double longestCommonSubsequenceLength = LongestCommonSubsequenceAlgorithm.getLongestCommonSubsequenceLength(methodFirst.toCharArray(), methodSecond.toCharArray());
        double methodFirstLength = methodFirst.length();
        double methodSecondLength = methodSecond.length();
        return 2 * longestCommonSubsequenceLength / (methodFirstLength + methodSecondLength);
    }

}

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractSyntaxTreeReader {

    private static final String DIRECTORY_PATH = "src/main/java";

    public static List<MethodDeclaration> getAllMethods(String filePath) {
        CompilationUnit compilationUnit = getCompilationUnit(filePath);
        modifyMethodDeclarations(compilationUnit);
        modifyVariableDeclaratorNames(compilationUnit);
        return compilationUnit.findAll(MethodDeclaration.class);
    }

    private static CompilationUnit getCompilationUnit(String filePath) {
        StaticJavaParser.setConfiguration(getParserConfiguration());

        CompilationUnit compilationUnit = null;
        try {
            compilationUnit = StaticJavaParser.parse(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return compilationUnit;
    }

    private static ParserConfiguration getParserConfiguration() {
        TypeSolver javaParserTypeSolver = new JavaParserTypeSolver(new File(DIRECTORY_PATH));
        JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(javaParserTypeSolver);
        return new ParserConfiguration().setAttributeComments(false)
                .setSymbolResolver(javaSymbolSolver);
    }

    private static void modifyMethodDeclarations(CompilationUnit compilationUnit) {
        ModifierVisitor<Void> methodDeclarationNameModifier = new MethodDeclarationModifier();
        methodDeclarationNameModifier.visit(compilationUnit, null);
    }

    private static void modifyVariableDeclaratorNames(CompilationUnit compilationUnit) {
        List<String> namesOfVariables = compilationUnit.findAll(VariableDeclarator.class)
                                                        .stream()
                                                        .map(NodeWithSimpleName::getNameAsString)
                                                        .collect(Collectors.toList());

        ModifierVisitor<List<String>> variableDeclaratorNameModifier = new VariableNameModifier();
        variableDeclaratorNameModifier.visit(compilationUnit, namesOfVariables);
    }

}

package nl.grum.grummifier;

import nl.grum.grummifier.antlr.JavaBaseListener;
import nl.grum.grummifier.antlr.JavaLexer;
import nl.grum.grummifier.antlr.JavaParser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;

public class Grummifier {
    public static void main(String[] args) throws IOException {
        final String fileName = "src/main/java/nl/grum/grummifier/Grummifier.java";
        final JavaLexer lexer = new JavaLexer(new ANTLRFileStream(fileName));
        final JavaParser parser = new JavaParser(new CommonTokenStream(lexer));

        final ParseTree tree = parser.compilationUnit();

        final String[] ruleNames = parser.getRuleNames();
        final String[] tokenNames = parser.getTokenNames();

        ParseTreeListener listener = new JavaBaseListener() {
            private int level = 0;

            @Override
            public void visitTerminal(@NotNull final TerminalNode node) {
                indent(level);
                System.out.println("DATA: " + node.getText());
            }

            @Override
            public void visitErrorNode(@NotNull final ErrorNode node) {
                System.out.println("Errornode!: " + node);
            }

            @Override
            public void enterEveryRule(@NotNull final ParserRuleContext ctx) {
                indent(level++);
                System.out.println("> " + ruleNames[ctx.getRuleIndex()]);
            }

            @Override
            public void exitEveryRule(@NotNull final ParserRuleContext ctx) {
                indent(--level);
                System.out.println("< " + ruleNames[ctx.getRuleIndex()]);
            }

            private void indent(final int level) {
                for (int i = 0; i < level; i++) {
                    System.out.print(" ");
                }
            }
        };

        ParseTreeWalker.DEFAULT.walk(listener, tree);
    }
}

import java.io.File
import java.nio.charset.Charset
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size > 1) {
        println("Usage: ktlox [script]");
        exitProcess(64);
    } else if (args.size == 1) {
        Lox.runFile(args[0]);
    } else {
        Lox.runPrompt();
    }
}

class Lox {
    companion object {
        var hadError: Boolean = false;

        fun runFile(path: String) {
            val bytes = File(path).readBytes();
            run(bytes.toString(Charset.defaultCharset()));

            // Indicate an error in the exit code.
            if (hadError) exitProcess(65);
        }

        fun runPrompt() {
            while (true) {
                print("> ");
                val line = readln();
                if (line == "") break;
                run(line);
                Lox.hadError = false;
            }
        }

        private fun run(source: String) {
            val scanner: Scanner = Scanner(source);
            val tokens: List<Token> = scanner.scanTokens();

            for (token in tokens) {
                println(token);
            }
        }

        fun error(line: Int, message: String) {
            report(line, "", message);
        }

        private fun report(line: Int, where: String, message: String) {
            // Kotlin does not a ubiquitous way to write to stderr.
            System.err.println("[line $line] Error $where: $message");
            Lox.hadError = true;
        }
    }
}
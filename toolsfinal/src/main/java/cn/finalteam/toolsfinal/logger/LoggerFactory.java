package cn.finalteam.toolsfinal.logger;

/**
 * Desction:日志工厂类
 * Author:pengjianbo
 * Date:2016/1/29 0029 17:30
 */
public class LoggerFactory {

    public static LoggerPrinter getFactory(String tag, boolean debug) {
        final LoggerPrinter printer = new LoggerPrinter();
        printer.init(tag);
        LogLevel level = LogLevel.NONE;
        if (debug) {
            level = LogLevel.FULL;
        }
        printer.getSettings().methodCount(3).logLevel(level);

        return printer;
    }

}

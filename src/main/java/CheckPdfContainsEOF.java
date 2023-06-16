
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class CheckPdfContainsEOF {

    static final byte[] pdf = {0x25, 0x50, 0x44, 0x46}; // %PDF
    static final byte[] eof = {0x25, 0x25, 0x45, 0x4F, 0x46}; // %%EOF

    static final String[] msgUsage = {"usage: checkPdfContainsEOF file [retry [delay]]",
                                      "If file not starts with %PDF or not has %%EOF at end of file",
                                      "retrys to check file \"retry\" times delaying by \"delay\" secunds",
                                      "If found %PDF and %%EOF exiting 0 otherwise 1"};
    static final String msgInput = "try to check input pdf file";
    static final String msgRetry = "retry check input pdf file";
    static final String msgFound = "found %%EOF in pdf file";
    static final String msgFails = "can't find %%EOF in pdf file";

    public static void main(String[] args) throws InterruptedException, IOException {

        if (args.length < 1
            || args.length > 3) {
            for (var msg : msgUsage)
                System.out.println(msg);
            System.exit(-1);
        }

        var delay = args.length != 3 ? 2 * 1000
                    : Integer.parseInt(args[2]) * 1000;

        var retry = args.length == 1 ? 0
                    : Integer.parseInt(args[1]);

        System.out.println(msgInput);

        var lead = new byte[pdf.length];
        var tail = new byte[eof.length + 42];
        for (int i = 0; i <= retry; i++) try (
                var raf = new RandomAccessFile(args[0], "r")) {
            if (raf.length() > lead.length + tail.length) {
                raf.read(lead, 0, lead.length);
                if (Arrays.equals(lead, pdf)) {
                    raf.seek(raf.length() - tail.length);
                    raf.read(tail, 0, tail.length);
                    if (foundBytes(tail, eof)) {
                        System.out.println(msgFound);
                        System.exit(0);
                    }
                }
            }
            if (i > 0) {
                System.out.println(msgRetry);
                Thread.sleep(delay);
            }
        }

        System.out.println(msgFails);
        System.exit(1);

    }

    static boolean foundBytes(byte[] buffer, byte[] toFind) {
        var found = false;
        for (int i = (buffer.length - toFind.length); i >= 0; i--) {
            if (buffer[i] == toFind[0]) {
                found = true;
                for (int j = 0; j < toFind.length; j++)
                    if (buffer[i + j] != toFind[j]) {
                        found = false;
                        break;
                    }
            }
            if (found)
                return found;
        }
        return found;
    }

}

package io.github.dsheirer.source.tuner;
import io.github.dsheirer.channel.state.StateMachine;
import io.github.dsheirer.preference.TimestampFormat;
import io.github.dsheirer.preference.UserPreferences;
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TunerIdleEventFile {
    public static void create() {

        Logger mLog = LoggerFactory.getLogger(TunerIdleEventFile.class);

        StringBuilder sb = new StringBuilder();
        sb.append(TimestampFormat.TIMESTAMP_COMPACT.getFormatter().format(new Date(System.currentTimeMillis())));
        sb.append("-"+UUID.randomUUID()+".event");

        Path recordingDirectory = Paths.get(System.getProperty("user.home"), "SDRTrunk","recordings");
        Path filePath = recordingDirectory.resolve(sb.toString());

        try {
            FileWriter myWriter = new FileWriter(filePath.toString());
            myWriter.write("Control Channel lost");
            myWriter.close();
            mLog.debug("Created control channel lost file");
        } catch (IOException e) {
            mLog.debug("Error creating control channel lost file", e);
        }
    }
}

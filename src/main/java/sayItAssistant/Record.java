package sayItAssistant;
import javax.sound.sampled.*;
import java.lang.Exception;
import java.io.File;


public class Record {
    // the file that will contain the audio data
    private static String recordingFileName = "newQuestion.wav";

    //thread so no crash
    private Thread t;

    /* 
     * sampleRate = # samples of audio per second.
     * sampleSizeInBits = # bits in each sample of a sound that has been digitized.
     * audio channels: mono = 1, stereo = 2.
     * the data is signed.
     * stored in big endian order. 
     */
    private float sampleRate = 44100;
    private int sampleSizeInBits = 16;
    private int channels = 1;
    private boolean signed = true;
    private boolean bigEndian = false;
    private AudioFormat audioFormat = new AudioFormat(
        sampleRate,
        sampleSizeInBits,
        channels,
        signed,
        bigEndian
    );
    private TargetDataLine targetDataLine;

    /*
     * get the recording file name
     */
    public static String getRecordingFileName() {
        return recordingFileName;
    }

    /*
    * Starts recording the new question
    */
    public void startRecording() {
        t = new Thread(
            () -> {
                try {
                    // the format of the TargetDataLine
                    DataLine.Info dataLineInfo = new DataLine.Info(
                    TargetDataLine.class,
                    audioFormat
                    );
                    // the TargetDataLine used to capture audio data from the microphone
                    targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                    targetDataLine.open(audioFormat);
                    targetDataLine.start();

                    // the AudioInputStream that will be used to write the audio data to a file
                    AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);

                    File audioFile = new File(recordingFileName);
                    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        );
        t.start();
    }

    /*
     * Stops the recording and saves to file
     */
    public void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }

}
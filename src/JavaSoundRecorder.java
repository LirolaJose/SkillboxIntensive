import com.dropbox.core.v2.DbxClientV2;

import javax.sound.sampled.*;
import java.io.*;

public class JavaSoundRecorder
{
    private AudioFileFormat.Type fileType;
    private TargetDataLine line;
    private DataLine.Info info;
    private AudioFormat format;

    private DbxClientV2 client;

    public JavaSoundRecorder(DbxClientV2 client, String folder)
    {
        this.client = client;
        fileType = AudioFileFormat.Type.WAVE;
        format = getAudioFormat();
        info = new DataLine.Info(TargetDataLine.class, format);
    }

    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    public void recordAudio(int milliseconds)
    {
        //TODO: using SimpleDateFormat create name
        // in format: 20200724_201906.wav
        String fileName = "";
        start(fileName);
        finish(milliseconds);
    }

    private void start(String fileName)
    {
        File wavFile = new File(fileName);
        Thread thread = new Thread(() -> {
            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();
                AudioInputStream ais = new AudioInputStream(line);
                AudioSystem.write(ais, fileType, wavFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    private void finish(int milliseconds)
    {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            line.stop();
            line.close();
            //TODO: upload file to Dropbox
            //  and then remove the file from disk
        });
        thread.start();
    }
}
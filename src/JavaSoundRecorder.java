import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;

import javax.sound.sampled.*;
import javax.xml.crypto.Data;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class JavaSoundRecorder
{
    private AudioFileFormat.Type fileType;
    private TargetDataLine line;
    private DataLine.Info info;
    private AudioFormat format;

    LocalDateTime now = LocalDateTime.now();
    private String path;
    private String fileName;
    File wavFile;

    private DbxClientV2 client;

    public JavaSoundRecorder(DbxClientV2 client, String folder)
    {
        this.client = client;
        fileType = AudioFileFormat.Type.WAVE;
        format = getAudioFormat();
        info = new DataLine.Info(TargetDataLine.class, format);
        fileName = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        path = folder + fileName + ".wav";
        wavFile = new File(path);
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
        return new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
    }

    public void recordAudio(int milliseconds)
    {
        start(path);
        finish(milliseconds);
    }

    private void start(String fileName)
    {
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
            uploadFileInDbx();
            //TODO: upload file to Dropbox
            //  and then remove the file from disk
        });
        thread.start();
       try{
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        delete();
    }

    private void delete() {
        if(wavFile.delete()){
            System.out.println("File deleted!");
        }
    }

    private void uploadFileInDbx() {
        try (InputStream in = new FileInputStream(path)) {
            client.files().uploadBuilder("/" + fileName + ".wav").uploadAndFinish(in);
            if(in != null) in.close();
        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }
    }
}
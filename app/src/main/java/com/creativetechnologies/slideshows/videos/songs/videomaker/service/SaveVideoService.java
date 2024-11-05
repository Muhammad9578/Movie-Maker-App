package com.creativetechnologies.slideshows.videos.songs.videomaker.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.LogCallback;
import com.arthenica.mobileffmpeg.LogMessage;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.OnProgressReceiver;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.Util;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.SharedPrefs;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.ScalingUtilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveVideoService extends IntentService {
    MyApplication application;
    private File audioFile;
    private File audioIp;
    int last;
    String timeRe;
    private float toatalSecond;
    String[] inputCode;
    boolean temp_flag = false;
    boolean temp2 = false;
    float val = 0.0f;

    public SaveVideoService() {
        this(SaveVideoService.class.getName());
    }

    public SaveVideoService(String name) {
        super(name);
        this.timeRe = "\\btime=\\b\\d\\d:\\d\\d:\\d\\d.\\d\\d";
        this.last = 0;
    }

    protected void onHandleIntent(Intent intent) {
        this.application = MyApplication.getInstance();

        createVideo();
    }

    private void createVideo() {

        if (Share.twoD_Effect_Flag) {
            toatalSecond = (this.application.getSecond() * ((float) this.application.getSelectedImages().size()));
        } else {
            toatalSecond = (this.application.getSecond() * ((float) this.application.getSelectedImages().size())) - 1.0f;
        }

//        joinAudio();
        audioIp = new File(FileUtils.TEMP_DIRECTORY, "audio.txt");
        this.audioFile = new File(FileUtils.APP_DIRECTORY, "audio.mp3");
        this.audioFile.delete();
        this.audioIp.delete();
        int d = 0;
        while (true) {
            try {
                appendAudioLog(String.format("file '%s'", new Object[]{this.application.getMusicData().track_data}));
                Log.e("TAG", new StringBuilder(String.valueOf(d)).append(" is D  ").append(this.toatalSecond * 1000.0f).append("___").append(this.application.getMusicData().track_duration * ((long) d)).toString());
                if (this.toatalSecond * 1000.0f <= ((float) (this.application.getMusicData().track_duration * ((long) d)))) {
                    break;
                }
                d++;
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        String[] command = new String[]{"-f", "concat", "-safe", "0", "-i", audioIp.getAbsolutePath(), "-c", "copy", "-preset", "ultrafast", "-ac", "2", audioFile.getAbsolutePath()};

        FFmpeg.executeAsync(command, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int returnCode) {
                if (returnCode == 0) {
                    Log.e("TAG", " ***************** Audio  onSuccess ******************* " + returnCode);

                    temp_flag = true;
                }
            }
        });

        Config.enableLogCallback(new LogCallback() {
            @Override
            public void apply(LogMessage message) {
                String line = message.getText();
                Log.e("TAG", " ***************** Audio onProgress ******************* " + message);
                Log.e("TAG", "line   => " + line);
                if (line != null) {
                    val = ((int) ((((float) durationToProgress(line)) * 75.0f) / 100.0f)) + 25;
                    Log.e("process", "val   => " + val);

                    if (MyApplication.Save_Service_On_Off_Flag) {
                        MyApplication.Save_Service_On_Off_Flag = false;
                        //bufferedReader.close();
                        //break;
                    }
                    if (val == 100.0) {
                        //bufferedReader.close();
                        //break;
                    }
                } else {
                    MyApplication.ThreeDServiceisBreak = true;
                    MyApplication.TwoDServiceisBreak = false;
                    MyApplication.MoreServiceisBreak = true;

                    MyApplication.error_in_save_video = true;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            Toast.makeText(application, "Something wrong please try again!!!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        while (true) {
            Log.e("TAG", " ***************** temp_flag ******************* " + temp_flag);

            if (temp_flag) {
                if (Share.threeD_Effect_Flag) {
                    Log.e("TAG", " ***************** ThreeDPreviewCreatorService.threeD_Effect_Flag  if ******************* " + Share.threeD_Effect_Flag);
                    while (true) {
                        Log.e("TAG", "MorePreviewCreatorService.isImageComplate" + ThreeDPreviewCreatorService.isImageComplate + "///" + Share.buffer_progress);
                        if (ThreeDPreviewCreatorService.isImageComplate) {
                            Log.e("TAG", " ***************** ThreeDPreviewCreatorService.isImageComplate  if ******************* ");
                            break;
                        } else {
                            if (Share.buffer_progress == 100.0f) {
                                Log.e("TAG", " ***************** ThreeDPreviewCreatorService.isImageComplate  else ******************* ");
                                break;
                            }
                        }
                    }
                } else if (Share.twoD_Effect_Flag) {
                    Log.e("TAG", " ***************** ThreeDPreviewCreatorService.twoD_Effect_Flag  if ******************* " + Share.twoD_Effect_Flag);
                    while (true) {
                        Log.e("TAG", "MorePreviewCreatorService.isImageComplate" + TwoDPreviewCreatorService.isImageComplate + "///" + Share.buffer_progress);
                        if (TwoDPreviewCreatorService.isImageComplate) {
                            Log.e("TAG", " ***************** TwoDPreviewCreatorService.isImageComplate if ******************* ");
                            break;
                        } else {
                            if (Share.buffer_progress == 100.0f) {
                                Log.e("TAG", " ***************** TwoDPreviewCreatorService.isImageComplate  else ******************* ");
                                break;
                            }
                        }
                    }
                } else if (Share.more_Effect_Flag) {
                    Log.e("TAG", " ***************** ThreeDPreviewCreatorService.more_Effect_Flag  if ******************* " + Share.more_Effect_Flag);
                    while (true) {
                        Log.e("TAG", "MorePreviewCreatorService.isImageComplate" + MorePreviewCreatorService.isImageComplate + "///" + Share.buffer_progress);
                        if (MorePreviewCreatorService.isImageComplate) {
                            Log.e("TAG", " ***************** MorePreviewCreatorService.isImageComplate  if *******************");
                            break;
                        } else {
                            if (Share.buffer_progress == 100.0f) {
                                Log.e("TAG", " ***************** MorePreviewCreatorService.isImageComplate  else ******************* ");
                                break;
                            }
                        }
                    }
                }
                break;
            }
        }

        Log.e("TAG", " ***************** create Video  *******************");
        new File(FileUtils.TEMP_DIRECTORY, "video.txt").delete();
        for (int i = 0; i < application.videoImages.size(); i++) {
            appendVideoLog(String.format("file '%s'", new Object[]{application.videoImages.get(i)}));
        }

        if (!FileUtils.SAVE_DIRECTORY.exists()) {
            FileUtils.SAVE_DIRECTORY.mkdirs();
        }

        String videoPath = new File(FileUtils.SAVE_DIRECTORY, getVideoName()).getAbsolutePath();
        Share.video_path = videoPath;

        if (application.getMusicData() == null) {
//            inputCode = new String[]{"-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-f", "concat", "-i", new File(FileUtils.TEMP_DIRECTORY, "video.txt").getAbsolutePath(), "-r", "30", "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", videoPath};
            MyApplication.no_music_command = true;
        } else if (application.getFrame() != -1) {
            if (!FileUtils.frameFile.exists()) {
                try {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), application.getFrame());
                    if (!(bm.getWidth() == MyApplication.VIDEO_WIDTH && bm.getHeight() == MyApplication.VIDEO_HEIGHT)) {
                        try {
                            bm = ScalingUtilities.scaleCenterCrop(bm, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                    FileOutputStream outStream = new FileOutputStream(FileUtils.frameFile);
                    bm.compress(CompressFormat.PNG, 40, outStream);
                    outStream.flush();
                    outStream.close();
                    bm.recycle();
                    System.gc();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            MyApplication.frame_command = true;
        } else {
//            inputCode = new String[]{"-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-f", "concat", "-safe", "0", "-i", new File(FileUtils.TEMP_DIRECTORY, "video.txt").getAbsolutePath(), "-i", audioFile.getAbsolutePath(), "-strict", "experimental", "-r", "30", "-t", String.valueOf(toatalSecond), "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", "-ac", "2", videoPath};
        }
        System.gc();
        //Process process = null;

        SharedPrefs.save(application, SharedPrefs.video_created_or_not, "true");
        SharedPrefs.save(application, SharedPrefs.video_path, Share.video_path);


        temp2 = false;
        if (MyApplication.no_music_command) {
            MyApplication.no_music_command = false;
            FFmpeg.executeAsync(new String[]{"-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-f", "concat", "-i", new File(FileUtils.TEMP_DIRECTORY, "video.txt").getAbsolutePath(), "-r", "30", "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", videoPath}, new ExecuteCallback() {
                @Override
                public void apply(long executionId, int returnCode) {
                    if (returnCode == 0) {
                        temp2 = true;
                    }
                }
            });
            //process = Runtime.getRuntime().exec(new String[]{FileUtils.getFFmpeg(getApplicationContext()), "-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-f", "concat", "-i", new File(FileUtils.TEMP_DIRECTORY, "video.txt").getAbsolutePath(), "-r", "30", "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", videoPath});
        } else if (MyApplication.frame_command) {
            MyApplication.frame_command = false;
            //process = Runtime.getRuntime().exec(new String[]{FileUtils.getFFmpeg(getApplicationContext()), "-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-f", "concat", "-safe", "0", "-i", new File(FileUtils.TEMP_DIRECTORY, "video.txt").getAbsolutePath(), "-i", FileUtils.frameFile.getAbsolutePath(), "-i", audioFile.getAbsolutePath(), "-filter_complex", "overlay= 0:0", "-strict", "experimental", "-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-t", String.valueOf(toatalSecond), "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", "-ac", "2", videoPath});
            FFmpeg.executeAsync(new String[]{"-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-f", "concat", "-safe", "0", "-i", new File(FileUtils.TEMP_DIRECTORY, "video.txt").getAbsolutePath(), "-i", FileUtils.frameFile.getAbsolutePath(), "-i", audioFile.getAbsolutePath(), "-filter_complex", "overlay= 0:0", "-strict", "experimental", "-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-t", String.valueOf(toatalSecond), "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", "-ac", "2", videoPath}, new ExecuteCallback() {
                @Override
                public void apply(long executionId, int returnCode) {
                    if (returnCode == 0) {
                        temp2 = true;
                    }
                }
            });
        } else {
            //process = Runtime.getRuntime().exec(new String[]{FileUtils.getFFmpeg(getApplicationContext()), "-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-f", "concat", "-safe", "0", "-i", new File(FileUtils.TEMP_DIRECTORY, "video.txt").getAbsolutePath(), "-i", audioFile.getAbsolutePath(), "-strict", "experimental", "-r", "30", "-t", String.valueOf(toatalSecond), "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", "-ac", "2", videoPath});
            FFmpeg.executeAsync(new String[]{"-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-f", "concat", "-safe", "0", "-i", new File(FileUtils.TEMP_DIRECTORY, "video.txt").getAbsolutePath(), "-i", audioFile.getAbsolutePath(), "-strict", "experimental", "-r", "30", "-t", String.valueOf(toatalSecond), "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", "-ac", "2", videoPath}, new ExecuteCallback() {
                @Override
                public void apply(long executionId, int returnCode) {
                    if (returnCode == 0) {
                        temp2 = true;
                    }
                }
            });
        }

        while (true) {
            if (temp2) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        OnProgressReceiver receiver = SaveVideoService.this.application.getOnProgressReceiver();
                        if (receiver != null) {
                            if (!MyApplication.Save_Service_On_Off_Flag) {
                                receiver.onVideoProgressFrameUpdate(100.0f);
                                receiver.onProgressFinish("");
                            } else {
                                MyApplication.Save_Service_On_Off_Flag = false;
                                stopSelf();
                            }
                        }
                    }
                });

                FileUtils.deleteTempDir();
                stopSelf();
                break;
            }
            //Log.e("TAG", "while first");
            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            /*while (true) {
                Log.e("TAG", "while second");
                String line = bufferedReader.readLine();
                Log.e("TAG", "line   => " + line);
                if (line != null) {
                    val = ((int) ((((float) durationToprogtess(line)) * 75.0f) / 100.0f)) + 25;
                    Log.e("process", "val   => " + val);

                    if (MyApplication.Save_Service_On_Off_Flag) {
                        MyApplication.Save_Service_On_Off_Flag = false;
                        bufferedReader.close();
                        break;
                    }
                    if (val == 100.0) {
                        bufferedReader.close();
                        break;
                    }
                } else {
                    MyApplication.ThreeDServiceisBreak = true;
                    MyApplication.TwoDServiceisBreak = false;
                    MyApplication.MoreServiceisBreak = true;

                    MyApplication.error_in_save_video = true;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            Toast.makeText(application, "Something wrong please try again!!!", Toast.LENGTH_LONG).show();
                        }
                    });

                    break;

                }
                                 *//*if (MyApplication.Save_Service_On_Off_Flag) {
                    Util.destroyProcess(process);
                    break;
                }
                if (val == 100.0) {
                    Util.destroyProcess(process);
                    break;
                }*//*
            }*/
        }
    }

    private void joinAudio() {
        audioIp = new File(FileUtils.TEMP_DIRECTORY, "audio.txt");
        audioFile = new File(FileUtils.APP_DIRECTORY, "audio.mp3");
        audioFile.delete();
        audioIp.delete();
        int d = 0;
        while (true) {
            appendAudioLog(String.format("file '%s'", new Object[]{application.getMusicData().track_data}));
            Log.e("audio", new StringBuilder(String.valueOf(d)).append(" is D  ").append(this.toatalSecond * 1000.0f).append("___").append(application.getMusicData().track_duration * ((long) d)).toString());
            if (toatalSecond * 1000.0f <= ((float) (application.getMusicData().track_duration * ((long) d)))) {
                break;
            }
            d++;
        }
        Process process = null;
        try {

            process = Runtime.getRuntime().exec(new String[]{FileUtils.getFFmpeg(getApplicationContext()), "-f", "concat", "-safe", "0", "-i", audioIp.getAbsolutePath(), "-c", "copy", "-preset", "ultrafast", "-ac", "2", audioFile.getAbsolutePath()});
            while (!Util.isProcessCompleted(process)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while (true) {
                    String line = reader.readLine();
                    if (line != null) {
                        Log.e("audio", line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("audio", "io", e);
        } finally {
            Util.destroyProcess(process);
        }
    }

    private int durationToProgress(String input) {
        int progress = 0;
        try {
//            if (!input.equalsIgnoreCase("") && input != null) {
            Matcher matcher = Pattern.compile(this.timeRe).matcher(input);
            int MINUTE = 1 * 60;
            int HOUR = MINUTE * 60;
            if (TextUtils.isEmpty(input) || !input.contains("time=")) {
                Log.e("time", "not contain time " + input);
                return this.last;
            }
            while (matcher.find()) {
                String time = matcher.group();
                String[] splitTime = time.substring(time.lastIndexOf(61) + 1).split(":");
                float hour = ((Float.valueOf(splitTime[0]).floatValue() * ((float) HOUR)) + (Float.valueOf(splitTime[1]).floatValue() * ((float) MINUTE))) + Float.valueOf(splitTime[2]).floatValue();
                Log.e("time", "totalSecond:" + hour);
                progress = (int) ((100.0f * hour) / this.toatalSecond);
                updateInMili(hour);
            }
            try {
                this.last = progress;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
//            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return progress;
    }

    private void updateInMili(float time) {
        final double progress = (((double) time) * 100.0d) / ((double) this.toatalSecond);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                OnProgressReceiver receiver = SaveVideoService.this.application.getOnProgressReceiver();
                try {
                    if (receiver != null) {
                        Log.e("timeTotal", "progress __" + progress);
                        receiver.onVideoProgressFrameUpdate((float) progress);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getVideoName() {
        return "video_" + new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss", Locale.ENGLISH).format(new Date()) + ".mp4";
    }

    public static File appendVideoLog(String text) {
        if (!FileUtils.TEMP_DIRECTORY.exists()) {
            FileUtils.TEMP_DIRECTORY.mkdirs();
        }
        File logFile = new File(FileUtils.TEMP_DIRECTORY, "video.txt");
        Log.e("TAG", "File append " + text);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return logFile;
    }

    public static void appendAudioLog(String text) {
        if (!FileUtils.TEMP_DIRECTORY.exists()) {
            FileUtils.TEMP_DIRECTORY.mkdirs();
        }
        File logFile = new File(FileUtils.TEMP_DIRECTORY, "audio.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.Save_Service_On_Off_Flag = false;
    }

    /*private void joinAudio() {
        audioIp = new File(FileUtils.TEMP_DIRECTORY, "audio.txt");
        audioFile = new File(FileUtils.APP_DIRECTORY, "audio.mp3");
        try {
            audioFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioFile.delete();
        audioIp.delete();
        int d = 0;
        while (true) {
            appendAudioLog(String.format("file '%s'", new Object[]{application.getMusicData().track_data}));
//            Log.e("audio", new StringBuilder(String.valueOf(d)).append(" is D  ").append(toatalSecond * 1000.0f).append("___").append(this.application.getMusicData().track_duration * ((long) d)).toString());
            if (toatalSecond * 1000.0f <= ((float) (this.application.getMusicData().track_duration * ((long) d)))) {
                break;
            }
            d++;
        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{FileUtils.getFFmpeg(this), "-f", "concat", "-safe", "0", "-i", this.audioIp.getAbsolutePath(), "-c", "copy", "-preset", "ultrafast", "-ac", "2", this.audioFile.getAbsolutePath()});
            while (!Util.isProcessCompleted(process)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while (true) {
                    String line = reader.readLine();
                    if (line != null) {
                        Log.i("audio", line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("audio", "io", e);
        } finally {
            Util.destroyProcess(process);
        }

        while (true) {
            if (ThreeDPreviewCreatorService.isImageComplate) {
                break;
            }
        }

        Log.e("createVideo", "video create start");

        String[] inputCode;
        new File(FileUtils.TEMP_DIRECTORY, "video.txt").delete();
        for (int i = 0; i < application.videoImages.size(); i++) {
            SaveVideoService.appendVideoLog(String.format("file '%s'", new Object[]{application.videoImages.get(i)}));
        }

        if (!FileUtils.SAVE_DIRECTORY.exists()) {
            FileUtils.SAVE_DIRECTORY.mkdirs();
        }
        String videoPath = new File(FileUtils.SAVE_DIRECTORY, getVideoName()).getAbsolutePath();
        Share.video_path = videoPath;

        if (application.getMusicData() == null) {
            inputCode = new String[]{"-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-f", "concat", "-i", new File(FileUtils.TEMP_DIRECTORY, "video.txt").getAbsolutePath(), "-r", "30", "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", videoPath};
        } else if (application.getFrame() != -1) {
            if (!FileUtils.frameFile.exists()) {
                try {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), application.getFrame());
                    if (!(bm.getWidth() == MyApplication.VIDEO_WIDTH && bm.getHeight() == MyApplication.VIDEO_HEIGHT)) {
                        bm = ScalingUtilities.scaleCenterCrop(bm, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                    }
                    FileOutputStream outStream = new FileOutputStream(FileUtils.frameFile);
                    bm.compress(CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                    bm.recycle();
                    System.gc();
                } catch (Exception e) {
                }
            }
            inputCode = new String[]{"-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-f", "concat", "-safe", "0", "-i", new File(FileUtils.TEMP_DIRECTORY, "video.txt").getAbsolutePath(), "-i", FileUtils.frameFile.getAbsolutePath(), "-i", audioFile.getAbsolutePath(), "-filter_complex", "overlay= 0:0", "-strict", "experimental", "-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-t", String.valueOf(toatalSecond), "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", "-ac", "2", videoPath};
        } else {
            inputCode = new String[]{"-r", String.valueOf(BitmapDescriptorFactory.HUE_ORANGE / application.getSecond()), "-f", "concat", "-safe", "0", "-i", new File(FileUtils.TEMP_DIRECTORY, "video.txt").getAbsolutePath(), "-i", audioFile.getAbsolutePath(), "-strict", "experimental", "-r", "30", "-t", String.valueOf(toatalSecond), "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", "-ac", "2", videoPath};
        }
        System.gc();
        Process process1 = null;

        // if user remove app from recent
        SharedPrefs.save(application, SharedPrefs.video_created_or_not, "true");
        SharedPrefs.save(application, SharedPrefs.video_path, Share.video_path);
        Log.e("TAG", "video_path :=====> ");
        System.gc();
        try {
            process1 = Runtime.getRuntime().exec(inputCode);
            while (!Util.isProcessCompleted(process1)) {
                Log.e("TAG", "while :=====> ");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process1.getErrorStream()));
                bufferedReader.readLine();
                while (true) {
                    Log.e("TAG", "while true :=====> ");
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        Log.e("TAG", "readLine != null :=====> ");
                        if (readLine != null) {
                            Log.e("TAG", "readLine :=====> " + readLine);
                            durationToprogtess(readLine);
//                                            f1935f.setProgress(100, ((int) ((((float) durationToprogtess(readLine)) * 75.0f) / 100.0f)) + 25, false);
                        }
                    }
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.e("TAG", "LOI: " + e2);
        } finally {
            Log.e("TAG", "finally :=====> ");
            Util.destroyProcess(process1);
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                OnProgressReceiver receiver = SaveVideoService.this.application.getOnProgressReceiver();
                if (receiver != null) {
                    receiver.onVideoProgressFrameUpdate(100.0f);
                    receiver.onProgressFinish("");
                }
            }
        });

        *//*String[] command = new String[]{"-f", "concat", "-safe", "0", "-i", this.audioIp.getAbsolutePath(), "-c", "copy", "-preset", "ultrafast", "-ac", "2", this.audioFile.getAbsolutePath()};
        try {
            if (SplashHomeActivity.ffmpeg != null) {
                SplashHomeActivity.ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {

                    @Override
                    public void onSuccess(String message) throws FFmpegCommandAlreadyRunningException {
                        super.onSuccess(message);


                        *//**//*try {
                            SplashHomeActivity.ffmpeg.execute(inputCode, new ExecuteBinaryResponseHandler() {

                                @Override
                                public void onFailure(String message) {
                                }

                                @Override
                                public void onSuccess(String message) throws FFmpegCommandAlreadyRunningException {

                                    FinalPreviewActivity.flLoader.setVisibility(View.GONE);

                                    if (FinalPreviewActivity.activity != null) {
                                        FinalPreviewActivity.activity.finish();
                                    }

                                    Share.from_my_video = false;
                                    MyApplication.video_created_or_not = false;
                                    SharedPrefs.save(application, SharedPrefs.video_created_or_not, "false");
                                    if (!MyApplication.getInstance().requestNewInterstitial()) {
                                        Intent intent = new Intent(getApplicationContext(), SaveVideoActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    } else {

                                        MyApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                                            @Override
                                            public void onAdClosed() {
                                                super.onAdClosed();
                                                MyApplication.getInstance().mInterstitialAd.setAdListener(null);
                                                MyApplication.getInstance().mInterstitialAd = null;
                                                MyApplication.getInstance().ins_adRequest = null;
                                                MyApplication.getInstance().LoadAds();

                                                Intent intent = new Intent(getApplicationContext(), SaveVideoActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onAdFailedToLoad(int i) {
                                                super.onAdFailedToLoad(i);
                                            }

                                            @Override
                                            public void onAdLoaded() {
                                                super.onAdLoaded();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    super.onFinish();

                                }
                            });

                        } catch (FFmpegCommandAlreadyRunningException e) {
                            e.printStackTrace();
                        }*//**//*
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onFailure(String message) {
                        super.onFailure(message);
                    }
                });
            } else {
                Toast.makeText(application, "Please try new video , something want to wrong", Toast.LENGTH_SHORT);
            }
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }*//*
    }*/
}

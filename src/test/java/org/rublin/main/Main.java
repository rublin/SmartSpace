package org.rublin.main;

import org.rublin.controller.*;
import org.rublin.util.Resources;


import javax.sound.sampled.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class Main {
    private static final int ZONE_ID = 10;
    static ModemController modemController;
    private static final String TEXT_TO_SPEECH_SERVICE =
            "http://translate.google.com/translate_tts";
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) " +
                    "Gecko/20100101 Firefox/11.0";

    public void go(Language language, String text) throws Exception {
        // Create url based on input params
        String strUrl = TEXT_TO_SPEECH_SERVICE + "?" +
                "tl=" + language + "&q=" + text + "&client=tw-ob";
        URL url = new URL(strUrl);

        // Etablish connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Get method
        connection.setRequestMethod("GET");
        // Set User-Agent to "mimic" the behavior of a web browser. In this
        // example, I used my browser's info
        connection.addRequestProperty("User-Agent", USER_AGENT);
        connection.connect();

        // Get content
        BufferedInputStream bufIn =
                new BufferedInputStream(connection.getInputStream());
        byte[] buffer = new byte[1024];
        int n;
        ByteArrayOutputStream bufOut = new ByteArrayOutputStream();
        while ((n = bufIn.read(buffer)) > 0) {
            bufOut.write(buffer, 0, n);
        }

        // Done, save data
        File output = new File("output.mp3");
        BufferedOutputStream out =
                new BufferedOutputStream(new FileOutputStream(output));
        out.write(bufOut.toByteArray());
        out.flush();
        out.close();
        System.out.println("Done");
    }

    public enum Language {
        UK("ukraine"),
        EN("english");

        private final String language;
        private Language(String language) {
            this.language = language;
        }

        public String getFullName() {
            return language;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        /*
        InMemoryObjectRepository objectRepository = new InMemoryObjectRepository();
        InMemoryTriggerRepository triggerRepository = new InMemoryTriggerRepository();
        InMemoryEventRepository stateRepository = new InMemoryEventRepository();
        Zone obj = objectRepository.get(ZONE_ID);
        //stateRepository.get(triggerRepository.get(1)).forEach(System.out::println);
        stateRepository.save(triggerRepository.get(1), new DigitEvent(triggerRepository.get(1),false));
        stateRepository.get(triggerRepository.get(1)).forEach(System.out::println);
        triggerRepository.save(new Trigger("org.rublin.main Move 1 floor"), obj);
        triggerRepository.save(new Trigger("org.rublin.main Door 1 floor"), obj);
        triggerRepository.getAll().forEach(System.out::println);

        stateRepository.save(triggerRepository.get(1), new DigitEvent(triggerRepository.get(1),true));
        //hread.sleep(1000);
        stateRepository.save(triggerRepository.get(2), new DigitEvent(triggerRepository.get(2),false));
        Thread.sleep(1000);
        stateRepository.save(triggerRepository.get(2), new DigitEvent(triggerRepository.get(2),true));
        //Thread.sleep(5000);
        stateRepository.save(triggerRepository.get(1), new DigitEvent(triggerRepository.get(1),false));
        stateRepository.get(triggerRepository.get(1)).forEach(System.out::println);

        Trigger analog = triggerRepository.save(new Trigger("FAT"), obj);
        stateRepository.save(triggerRepository.get(analog.getId()), new AnalogEvent(analog, 12.0));
        stateRepository.get(triggerRepository.get(analog.getId())).forEach(System.out::println);
        */
//        ModemController modem = new ModemController();
//        modem.start();
//        modem.sendSms("+380950724287", "Zone Zone1 notification:\n" +
//                "Trigger: <b>Trigger1</b>; Status: <b>BAD</b>".replaceAll("\\<.*\\>", ""));
//        modem.stop();
//        System.out.println("Zone Zone1 notification:\nTrigger: <b>Trigger1</b>; Status: <b>BAD</b>".replaceAll("<[^>]*>", ""));

        Language language = Language.valueOf("UK");
        WeatherController weatherController = new WeatherController();
        String weather = weatherController.getForecast(Resources.WEATHER_CITY, Resources.WEATHER_LANG);
        System.out.println(weather);

//        try {
//            text = URLEncoder.encode(text, "utf-8");
//            new Main().go(language, text);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        TTSController ttsController = new TTSController();
        ttsController.say(weather, "uk");

//        Thread.sleep(15000);
//        weather = weatherController.getCondition(Resources.WEATHER_CITY, Resources.WEATHER_LANG);
//        System.out.println(weather);
//        ttsController.say(weather, "uk");
//        audio notification

       /* AudioInputStream din = null;
        URLConnection urlConnection;
        try {
            urlConnection = new URL("https://translate.google.com/translate_tts?ie=UTF-8&q=%D0%BF%D1%80%D0%B8%D0%B2%D1%96%D1%82&tl=uk&client=tw-ob").openConnection();
            urlConnection.addRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            urlConnection.connect();
//        AudioInputStream in = AudioSystem.getAudioInputStream(new BufferedInputStream(urlConnection.getInputStream()));
            AudioInputStream in = AudioSystem.getAudioInputStream(new URL("https://translate.google.com/translate_tts?ie=UTF-8&q=%D0%BF%D1%80%D0%B8%D0%B2%D1%96%D1%82&tl=uk&client=tw-ob"));
        AudioFormat baseFormat = in.getFormat();
        AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
                baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
                false);
        din = AudioSystem.getAudioInputStream(decodedFormat, in);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
        SourceDataLine line = null;
            line = (SourceDataLine) AudioSystem.getLine(info);

        if(line != null) {
            try {
                line.open(decodedFormat);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            byte[] data = new byte[4096];
            // Start
            line.start();

            int nBytesRead;
            while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
                line.write(data, 0, nBytesRead);
            }
            // Stop
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        }*/

//        String file = "D:\\tmp\\Air_Horn_in_Close_Hall_Series_audio-cutter.wav";
//        String cop = "D:\\tmp\\Police_Siren_audio-cutter.wav";
//        SoundController soundController = new SoundController();
//        soundController.play(new File(cop));
//        soundController.play(new File(file), 20);

//        SMS notification

        /*
        List<String> phones = Arrays.asList("+380950724288", "+380950724287");

        modemController = new ModemController();
        modemController.start();
//        modemController.call("+380950724287");
        phones.forEach(p -> sendCall(p, 10000));

        */
        /*List<String> sms = modemController.readSms();
        StringBuffer sb = new StringBuffer();
        sms.forEach(System.out::println);
        sms.forEach(s -> sb.append("http://www.smspdu.com/?action=ppdu&pdu=").append(s).append("\r"));
        System.out.println(sb);*/
    }
    public static void sendCall(String mobile, int i) {
        System.out.println(mobile);
        System.out.println(modemController.call(mobile, i));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

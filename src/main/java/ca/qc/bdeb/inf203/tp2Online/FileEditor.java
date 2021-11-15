package ca.qc.bdeb.inf203.tp2Online;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class FileEditor {
    private String fileName;

    public FileEditor(String fileName) {
        this.fileName = fileName;
    }

    public void addScore(int score, String name) throws IOException{
        if(name.trim().equals("")) {
            name = "Sans nom";
        }
        name = name.replaceAll(" ", "wioujehrbg");

        System.out.println(name);
        FileWriter writer = new FileWriter(fileName, true);
        String line = score + " " + name;
        writer.write(line + "\n");
        writer.close();
    }

    public ArrayList<String> getScores() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String lecture;
        while((lecture = reader.readLine()) != null) {
            String[] lectureArr = lecture.split(" ");
            String score = lectureArr[1].replaceAll("wioujehrbg", " ") + " -- " + lectureArr[0] + "px";
            list.add(score);
        }
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                int i1 = extractScore(o1);
                int i2 = extractScore(o2);
                if(i1 > i2) {
                    return -1;
                } else if (i1 < i2) {
                    return 1;
                } else return 0;
            }
        };

        list.sort(comparator);

        for(int i = 0; i < list.size(); i++) {
            list.set(i, "#" + (i + 1) + " -- " + list.get(i));
        }
        
        return list;
    }

    private int extractScore(Object o1) {
        String str1 = o1.toString().split(" -- ")[1];
        int i1 = Integer.parseInt(str1.substring(0, str1.length() - 2));
        return i1;
    }
}

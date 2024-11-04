package org.example.Service;

import com.domain.VinData;
import org.example.mybatismapper.CatalogMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.mybatismapper.VinDataMapper;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VinDataService {

    @Autowired
    private VinDataMapper vinDataMapper;

    @Autowired
    private CatalogMapper2 catalogMapper;

    //todo 设置字段
    public void insertVinData() throws IOException {

//        File file=new File("c://aa.txt");
//        FileInputStream is=new FileInputStream(file);

//        InputStreamReader reader=new InputStreamReader(is,"GBK");
//        BufferedReader br=new BufferedReader(reader);
//        String line=null;
//        while ((line=br.readLine())!=null) {
//            System.out.println(line);
//        }

        //再导一部分
        String path = "E:\\options\\vinDataNew.txt";
        BufferedReader fileBufferedReader = new BufferedReader(new FileReader(path));

//        LineIterator fileContents = FileUtils.lineIterator(new File(path));
//        BufferedReader bufferedReader = readTextFile(path);

//        File file = new File(path);
//        FileInputStream is = new FileInputStream(file);
//        InputStreamReader reader = new InputStreamReader(is, "GBK");
//        BufferedReader bufferedReader = new BufferedReader(reader);

//        String line = bufferedReader.readLine();
        List<VinData> vinList = new ArrayList<>();

        String line = fileBufferedReader.readLine();

        //catalog得在123个catalog中
        List<String> catalogcodeList = catalogMapper.selectCatalogCode();
        Set catalogCodeHashSet = catalogcodeList.stream().collect(Collectors.toSet());

        while (line != null) {
            String[] properties = line.split(",");
            VinData vinData = VinData.convert(properties);
            if (catalogCodeHashSet.contains(vinData.getCatalog())) {
                vinList.add(vinData);
            }
            //判断是否为最后一行

            line = fileBufferedReader.readLine();
            if (vinList.size() == 1000 || line == null) {
                vinDataMapper.batchInsert(vinList);
                vinList = new ArrayList<>();
            }
        }
        System.out.println("Finished");
    }

//    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
////        getVinOptions_ColumnCount(null);
////        List<String> dataFiles = new ArrayList<>();
////        File directory = new File("C:\\Users\\admin\\Desktop\\options\\");
////
////        // 递归遍历目录及子目录
////        searchForDataFiles(directory, dataFiles);
////        convertFile(null);
//        filterVinData();
//    }

    //将其他字段关联进去
    //先处理，再关联
    //一会儿重新运行，

    //一会儿还要开个会
    public void filterVinData() {
        try {
            List<String> catalogCodes = catalogMapper.selectCatalogCode();
            Set<String> catalogCodesSet = catalogCodes.stream().collect(Collectors.toSet());
            FileReader fd = new FileReader("E:\\options\\vinData.txt");
            BufferedReader bufferedReader = new BufferedReader(fd);
            FileWriter fw = new FileWriter("E:\\options\\vinDataNew2.txt");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
                String[] splitLines = line.split(",");
//                System.out.println(splitLines[5]);
//                System.out.println(catalogCodesSet.iterator().next());
                if (catalogCodesSet.contains(splitLines[5])) {
                    fw.write(line + '\n');
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //先合并到文件里面
    public static void convertFile(String[] args)
            throws SQLException, ClassNotFoundException, IOException {
        // TODO Auto-generated method stub

        try {
            FileWriter fw= new FileWriter("E:\\options\\vinData.txt");
            List<String> dataFiles = new ArrayList<>();
            File directory = new File("E:\\options\\");

            // 递归遍历目录及子目录
            searchForDataFiles(directory, dataFiles);

            String line = null;
            BufferedReader reader = null;
            for(int i=0;i<dataFiles.size();i++) {
                reader = readTextFile(dataFiles.get(i));
                line = reader.readLine(); // first row is the count number
                line = reader.readLine();
                while (line != null) {
                    fw.write(line + "\r\n");
                    line = reader.readLine();
                }
//                String[] values = line.split(",");
//                fw.write(dataFiles.get(i).toString() + " : " + values.length + "\r\n");
                //System.out.println(dataFiles.get(i).toString() + " : " + values.length);
            }
            fw.flush();
            fw.close();
            System.out.println("Finished");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedReader readTextFile(String dataFile) throws FileNotFoundException {
        FileReader fileReader = new FileReader(dataFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader;
    }

    private static void searchForDataFiles(File directory, List<String> dataFiles) {
        // 获取目录中的所有文件和文件夹
        File[] files = directory.listFiles();

//        int datCount = 0;
//        int rarCount = 0;

        HashSet set = new HashSet<>();
//        set.add("AH94");
//        set.add("AH93");
//        set.add("AH92");
//        set.add("AH91");
//        set.add("AH90");
//        set.add("AH89");
//        set.add("AH88");
//        set.add("AH87");
//        set.add("AH86");
//        set.add("AH85");
//        set.add("AH84");
//        set.add("AH83");

        //确认一下
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归遍历子目录
                    searchForDataFiles(file, dataFiles);
                } else if (file.isFile() && file.getName().endsWith(".dat")) {
                    // 添加满足条件的文件到列表中
                    dataFiles.add(file.getAbsolutePath());
//                    file.delete();
//                    datCount++;
                }
//                else if (file.isFile() && file.getName().endsWith(".rar")) {
//                    rarCount++;
//                }
            }
        }
//        System.out.println("dat");
//        System.out.println(datCount);
//        System.out.println("rar");
//        System.out.println(rarCount);
    }

}

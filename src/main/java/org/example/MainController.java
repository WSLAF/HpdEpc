package org.example;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.XML;
import cn.hutool.json.JSONUtil;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.domain.*;
import com.domain.Catalog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import org.example.mybatismapper.ModelPartOptionNewTemp1Mapper;
import org.example.mybatismapper.ModelSectionPartsTemp1Mapper;
import org.example.mapper.VinPartOptionNewMapper;
import com.mysql.cj.util.StringUtils;
import org.dom4j.Document;
import org.example.Service.ElasticSearchService;
import org.example.Service.VinDataService;
import org.example.mybatismapper.VehicleInfoMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.example.model.*;
import org.example.model.SectionOptionApplicability;
import org.example.mybatismapper.VinSectionOption2Mapper;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
//import org.example.mapper.CatalogMapper;
//import org.example.mapper.FlatSectionMapper;
//import org.example.model.Catalog;
import org.example.mapper.VinMapper;
import org.example.mybatismapper.*;
//import org.example.model.Catalog;
//import org.example.model.FlatSection;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.example.utils.FileWriterWithLimit.*;
//import java.io.FileReader;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
@RestController
public class MainController {

    //improve your efficiency can also benefits company
    //wish you find a suitable candidate
    //reward repay
    private static ArrayList<String> filelist = new ArrayList<>();

    private Map<String, Boolean> chooseMap = new HashMap<>();
    @Resource
    private VinDataService vinDataService;

    @Resource
    private VinDataMapper vinDataMapper;

    @Resource
    private VinDecodeNewestMapper vinDecodeNewestMapper;

    @Resource
    private CatalogMapper2 catalogMapper;

    @Resource
    private VehicleInfoMapper vehicleInfoMapper;

    @Resource
    private VinPartOptionNewMapper vinPartOptionNewMapper;

    @Resource
    private ModelSectionPartsTemp1Mapper modelSectionPartsTemp1Mapper;

    @Resource
    private VinPartOptionNewestCopy2Mapper vinPartOptionNewestCopy2Mapper;

//    @Resource
//    private ModelPartOptionTempSqlserverMapper modelPartOptionTempSqlserverMapper;

//    @Resource
//    private SectionPartsTempSqlserverMapper sectionPartsTempSqlserverMapper;

//    List<Element> result = new ArrayList<>();

//    @Autowired
//    private FlatSectionMapper flatSectionMapper;
//
//    @Autowired
//    private CatalogMapper catalogMapper;

    @Resource
    private EpcSectionMapper epcSectionMapper;

    @Resource
    private SectionOptionApplicabilityMapper sectionOptionApplicabilityMapper;

    @Resource
    private SectionPartsMapper sectionPartsMapper;

    @Resource
    private ModelPartOptionMapper modelPartOptionMapper;

    @Resource
    private ModelPartOptionNewTemp1Mapper modelPartOptionNewTemp1Mapper;

    @Resource
    private VinMapper vinMapper;

    @Resource
    private ModelfileinfoMapper modelfileinfoMapper;

    @Resource
    private FlatSectionsMapper flatSectionsMapper;

    @Resource
    private VinPartOptionMapper vinPartOptionMapper;

    @Resource
    private VinSectionOptionMapper vinSectionOptionMapper;

    @Resource
    private VinSectionOption2Mapper vinSectionOptionMapper2;

    @Resource
    private SectionPartsByVin2Mapper sectionPartsByVin2Mapper;

    @Resource
    private SectionPartsByVin2AggregateOptionIdMapper sectionPartsByVin2AggregateOptionIdMapper;

    @Resource
    private ElasticSearchService elasticSearchService;

    List<Element> rawElementList = new ArrayList<>();
    List<Element> elementList = new ArrayList<>();

    static List<ModelSectionPartsTemp1> sectionParts1 = new ArrayList<>();
    static List<ModelPartOptionNewTemp1> modelPartOptions1 = new ArrayList<>();
    static List<ModelSectionPartsTemp1> sectionParts2 = new ArrayList<>();
    static List<ModelPartOptionNewTemp1> modelPartOptions2 = new ArrayList<>();

    private AtomicLong id = new AtomicLong(1150567);

    private AtomicLong id2 = new AtomicLong(1);

    @PostMapping("/aggregate-parts-by-vin")
    public void aggregatePartsByVin() {
        //读取数据
        //memory不够
        Map<String, SectionPartsByVin2AggregateOptionId> map;

        Long start;

        List<String> catalogList = new ArrayList<>();
        catalogList.add("HMA2B0PA06");

        for (String catalog : catalogList) {
            start = 0L;
//            List<SectionPartDto> sectionParts = sectionPartsMapper.selectPartByModelNew(catalog, start);
            List<SectionPartsByVin2> sectionPartsByVin2s = sectionPartsByVin2Mapper.selectByCatalogCode(catalog, start);
            System.out.println("ssss" + sectionPartsByVin2s.size());

            map = new HashMap<>();
            while (!sectionPartsByVin2s.isEmpty()) {
                //遍历零件
                for (int i = 0; i < sectionPartsByVin2s.size(); i++) {
                    //试一下
                    //为什么会有optionId

                    //找两个一样的，比较一下是否一致
                    String str = sectionPartsByVin2s.get(i).toString();
//                    System.out.println("ssss" + str);

                    //todo 打印
//                    if (sectionParts.get(i).getId() == 126297324) {
//                        System.out.println("aaaa");
//                    }
                    if (map.containsKey(str)) {
//                    System.out.println("mmmm");
                        SectionPartsByVin2AggregateOptionId value = map.get(str);
                        value.getOptionIdArr().add(sectionPartsByVin2s.get(i).getOptionId());
                        map.put(str, value);
//                        if (sectionParts.get(i).getId() == 126297324) {
//                            specificStr = str;
//                            System.out.println("bbbb" + value.getOptionIdsArr());
//                            System.out.println(value.toString());
//                        }
//                        if (str.equals(specificStr)) {
//                            System.out.println("gggg");
//                            System.out.println(sectionParts.get(i).toString());
//                            System.out.println(value.toString());
//                        }
                    } else {
                        //optionId
                        SectionPartsByVin2AggregateOptionId sectionPartsByVin2AggregateOptionId = SectionPartsByVin2AggregateOptionId.convert(sectionPartsByVin2s.get(i));
//                        sectionPartsByModelUltimate.getOptionIdsArr().add(sectionParts.get(i).getOptionId());
                        map.put(str, sectionPartsByVin2AggregateOptionId);
//                        if (sectionParts.get(i).getId() == 126297324) {
//                            System.out.println("cccc" + sectionPartsByModelUltimate.getOptionIdsArr());
//                        }
                    }
                }
                start = sectionPartsByVin2s.get(sectionPartsByVin2s.size() - 1).getId();
                //多维数组
                //两个对象，比如两个string

                sectionPartsByVin2s = sectionPartsByVin2Mapper.selectByCatalogCode(catalog, start);
//            System.out.println("kkkk" + sectionParts.size());
//            sectionParts = new ArrayList<>();
            }

            //转换，插到数据库
            //批量插入
            //插进去是json字段
            List<SectionPartsByVin2AggregateOptionId> sectionPartsByVin2AggregateOptionIds = new ArrayList();
            for (Map.Entry<String, SectionPartsByVin2AggregateOptionId> entry : map.entrySet()) {
//            System.out.println("kkkk");
                SectionPartsByVin2AggregateOptionId sectionPartsByVin2AggregateOptionId = entry.getValue();
//                if (entry.getKey().equals(specificStr)) {
//                    System.out.println("uuuu");
//                    System.out.println(sectionPartsByModelUltimate.toString());
//                }
                sectionPartsByVin2AggregateOptionId.setOptionIds(sectionPartsByVin2AggregateOptionId.getOptionIdArr().toString());
                sectionPartsByVin2AggregateOptionIds.add(sectionPartsByVin2AggregateOptionId);
            }

            List<SectionPartsByVin2AggregateOptionId> subList;
            int insertStart = 0;
            int insertEnd = 2000;
            while (insertStart < sectionPartsByVin2AggregateOptionIds.size()) {
                if (insertEnd > sectionPartsByVin2AggregateOptionIds.size()) {
                    insertEnd = sectionPartsByVin2AggregateOptionIds.size();
                }
                subList = sectionPartsByVin2AggregateOptionIds.subList(insertStart, insertEnd);
//                sectionPartsMapper.batchInsertUltimate(subList);
                sectionPartsByVin2AggregateOptionIdMapper.batchInsert(subList);
                insertStart += 2000;
                insertEnd += 2000;
                //会不会边界没写进去
            }
            System.out.println("finished " + catalog + "time " + LocalDateTime.now());
        }
        //todo 结束注释
    }

    @PostMapping("/insertHpdEpcPartData")
    public void insertHpdEpcPartData() throws IOException {
        elasticSearchService.insert();
    }

    @PostMapping("/insertHpdEpcPartDataByVin")
    public void insertHpdEpcPartDataByVin() throws IOException {
//        System.out.println("aaaaa");
        elasticSearchService.insertPartByVin();
    }

    @PostMapping("/insertHpdEpcOptionByVin")
    public void insertHpdEpcOptionByVin() throws IOException {
//        System.out.println("aaaaa");
        elasticSearchService.insertVinOption();
    }

    @PostMapping("/handlePartsByVin")
    public void handlePartsByVin() {
        //todo 1.验证参数对不对（问题：builddate, modelCode, optionCode, majorAttributes, engineMipCode, transmissionMipCode）
        //2.插到数据库（数据格式不一样）
        //3.聚合
        //4.插到es
        //5.测试

        //再生成一遍参数文件，对比。再生成的参数文件会有问题吗？
        //验证第一行参数
        //vin解析后的信息存到一张表里，vin和解析信息的对应的关系（直接从原epc获取是最稳妥的）。

        //再生成一遍
        //写一个接口，逆向查找。
        //先把文件全部都下载下来（至少是批量）
        //先把参数文件下下来

    }




    @PostMapping("/insertVinData")
    public void insertVinDataService() throws IOException {
        vinDataService.insertVinData();
    }

    //生成数据文件
    public static void searchCatalogWithSectionsParam() throws IOException {

        //读取文件
        FileReader vin = new FileReader("C:\\Users\\admin\\IdeaProjects\\HpdEpc\\vin.txt");
        List<String> lines = vin.readLines();
        //0 2HMBF22F0NB056688  100 false false true
        FileWriter fileWriter = new FileWriter("vinParams", true);

        for (String line : lines) {
            StringBuilder sb = new StringBuilder();
            sb.append(0).append(" ").append(line).append("  ").append(100).append(" ").append(0).append(" ").append(0).append(" ").append(1);
            fileWriter.println(sb.toString());
        }

        fileWriter.close();

    }

    //四行一存储
    @PostMapping("/check")
    public void check() throws FileNotFoundException {
//        JsonObject jsonObject = new JsonObject();
        //调用那个接口，查一百个
        //比较个数是否相等

        //1.取vin
        //2.根据catalog获取
        //3.过滤掉可选项不匹配的
        //4.子可选项不匹配的

        //从文件中读取
        //根据vin码查catalogCode
        //有一个partition方法，while （left < right） {
        // int mid = left + (right - left) >> 2;
        //}

        //lcs，动态规划/二分

//        List<String> catalogCodes = catalogMapper.selectCatalogcode();
//        FileReader fileReader = new FileReader("VinCatalog");
//        List<String> lines = fileReader.readLines();

//        for (int i = 0; i < lines.size(); i++) {
//            if (i % 4 == 0) {
//
//            }
//        }
        //分多行
//        for (String line : lines) {
//            String catalogCode = "KHMAP0A20";
//            String[] splitLines = line.split("\\s+");
//            String vin = splitLines[0];
//            String catalogCode = splitLines[1];
        String catalogCode = "KHMAPF219";
        String applicable = "1";
//            int unapplicableCount = 0;
        List<EpcSection> epcSections = epcSectionMapper.selectByCatalogcodeAndApplicableAndParentuniqueid(catalogCode, applicable, null);
        int applicableCount = epcSections.size();
        System.out.println("aaaa" + applicableCount);
        int bbbb = 0;
        int cccc = 0;

        //把uccCode拼接上去

//            int unappliable = epcSections.stream().filter(e -> !"[ \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\" ]".equals(e.getU) && !);
//            epcSections = epcSections.stream().filter(e -> !"[]".equals(e.getChildrenindex())).collect(Collectors.toList());

        for (EpcSection epcSection : epcSections) {
            List<String> uccCodes = new ArrayList<>();

            List<EpcSection> children = epcSectionMapper.selectByCatalogcodeAndApplicableAndParentuniqueid(catalogCode, applicable, epcSection.getUniqueid());
            //把可选项
            //查找可选项

            //从返回结果获取uccCodes
            uccCodes.add("[ \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\" ]");
            uccCodes.add("[ \"\", \"D2\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\" ]");
            uccCodes.add("[ \"\", \"\", \"\", \"\", \"7\", \"\", \"\", \"\", \"\", \"\" ]");
            uccCodes.add("[ \"\", \"D2\", \"\", \"\", \"7\", \"\", \"\", \"\", \"\", \"\" ]");
            List<VinSectionOption2> list1 = vinSectionOptionMapper2.selectByUccCodesAndCatalogCodeAndSectionCode(uccCodes, catalogCode, epcSection.getUniqueid());
//                System.out.println("bbbb"+list1.size());
            if (list1.isEmpty()) {
                applicableCount--;
                bbbb++;
            } else if (!"[]".equals(epcSection.getChildrenindex())) {
                int count = 0;
                for (EpcSection child : children) {
                    List<VinSectionOption2> list = vinSectionOptionMapper2.selectByUccCodesAndCatalogCodeAndSectionCode(uccCodes, catalogCode, child.getUniqueid());
                    if (!CollectionUtils.isEmpty(list)) {
                        count++;
                    }
                }
                if (count == 0) {
//                System.out.println("bbbb");
//                System.out.println(epcSection.getUniqueid());
                    applicableCount--;
                    cccc++;
                }
            }
        }
//        }
        //把结果存到表中，比对一下
        System.out.println("bbbb" + bbbb);
        System.out.println("cccc" + cccc);
        System.out.println("aaaaa");
        System.out.println(applicableCount);
    }

    //不要把该方法删掉
    @PostMapping("/store-sections")
    public void storeSections() throws JsonProcessingException {
        JsonObject jsonObject = new JsonObject();

        //从flat_section表中读取数据，插到一张section表中
        //mapper select超时
        List<String> catalogCodes = flatSectionsMapper.selectCatalogCode();
//        List<FlatSections> flatSections = flatSectionsMapper.selectAllByCatalogCode("KHMAPF219");
        Set<String> existCatalogCodes = epcSectionMapper.selectCatalogcode();
//        System.out.println("zzzz" + flatSections.size());
        for (String catalogCode : catalogCodes) {
            if (!existCatalogCodes.contains(catalogCode)) {
                List<FlatSections> flatSections = flatSectionsMapper.selectAllByCatalogCode(catalogCode);
                String clientSectionStr = flatSections.get(0).getClientSectionList();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                JsonNode node = mapper.readTree(clientSectionStr);
                JsonNode sectionNodes = node.get("sections");
                List<EpcSection> epcSections = new ArrayList<>();
                //需不需要调整结构
                //先原封不动存进去
                if (sectionNodes.isArray()) {
                    for (int i = 0; i < sectionNodes.size(); i++) {
                        JsonNode sectionNode = sectionNodes.get(i);
                        //将json转为对象
                        EpcSectionDto epcSectionDto = mapper.convertValue(sectionNode, EpcSectionDto.class);
                        EpcSection epcSection = EpcSection.convert(epcSectionDto);
                        epcSections.add(epcSection);
//                System.out.println(sectionNode.toPrettyString());
//                System.out.println(678);
                    }
                }
//        System.out.println("ssss" + epcSections.size());
                epcSectionMapper.batchInsert(epcSections);
            }
        }

        //代码粒粒面
        //那些嵌套字段要展开
//        System.out.println("6666");
    }

    @PostMapping("/check-vin-part-new")
    public void checkVinPartNew() {

        //取100个vin，获取原来的查询结果

        //取100个vin，查到对应的可选项，查询相应的零件，再根据builddate和uccCode过滤

        //比较结果是否相同

    }

    @PostMapping("/insert-part-with-option")
    public void storePartWithOption() throws IOException {
//        FileReader reader = new FileReader("Z:\\MCHYW\\Live\\PartListWithOptions.txt");
//        String path = "D:\\\\epc新\\\\PartListWithOptions.txt";
        String path = "E:\\新建文件夹\\partsAndImages.txt";
//        FileInputStream inputStream = null;
//        Scanner sc = null;
//        List<String> lines = reader.readLines();
        List<VinPartOption> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
//        boolean start = false;
//        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        //以{开头说明是返回值，不以{开头说明是参数
        try {
//            inputStream = new FileInputStream(path);
//            sc = new Scanner(inputStream);
            LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
//            String line;
//            Set<String> paramSet = new HashSet<>();
//            List<String> duplicateList = new ArrayList<>();
            while (fileContents.hasNext()) {
                String line = fileContents.nextLine();
//                if (!(line.charAt(0) == '{')) {

//                }
                //todo
                if (line.charAt(0) == '{') {
//                    System.out.println(line);
                    JsonNode node = mapper.readTree(line);

//                    if (!start) {
//                        if ("[\"8441A5\", \"8446A1\", \"846JA1\", \"8811F2\", \"9556A1\", \"9618A1\", \"961QA1\", \"961RA1\", \"\"]".equals(node.get("options").toPrettyString()) &&
//                                ";;||||K|||||;8441A5|8446A1|846JA1|8811F2|9556A1|9618A1|961QA1|961RA1|;;S|;;;G9|17|K7||||CH8460||084||P|||;8;".equals(node.get("interpretationCodes").asText()) &&
//                                "97040A97040G9000".equals(node.get("partId").asText())) {
//                            start = true;
//                            System.out.println("sssss");
//                        }
//                        System.out.println(node.get("options").toPrettyString());
//                        System.out.println(node.get("interpretationCodes").asText());
//                        System.out.println(node.get("partId").asText());
//                    }
//                    if (!start) {
//                        continue;
//                    }
                    JsonNode parts = node.get("parts");
                    if (parts.isArray()) {
                        Iterator<JsonNode> elements = parts.iterator();
                        while (elements.hasNext()) {
                            JsonNode element = elements.next();
//                            System.out.println(6667 + element.toPrettyString());
                            VinPartOptionDto vinPartOptionDto = mapper.convertValue(element, VinPartOptionDto.class);
                            VinPartOption vinPartOption = VinPartOption.convert(vinPartOptionDto);
                            //将sectionParts转为sectionPart,
//                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
//                            sectionParts.add(sectionPart);
                            list.add(vinPartOption);
                            if (list.size() == 1000 || !fileContents.hasNext()) {
                                vinPartOptionMapper.batchInsert(list);
                                list = new ArrayList<>();
                            }
                        }
                    }

//                    VinPartOption vinPartOption = VinPartOption.convert(vinPartOptionDto);
//                    System.out.println("ppppppp:" + vinPartOption.getPartId());
//                    list.add(vinPartOption);
                    //最后一次读取数据不满50行
                } else {
//                    if (!paramSet.contains(line)) {
//                        paramSet.add(line);
//                    } else {
//                        duplicateList.add(line);
//                    }
//                    if ("KHMAPJJ20 8888011 <interpretation catalog=\"KHMAPJJ20\" type=\"EBOM\" level=\"2\"/> false true ".equals(line)) {
//                        System.out.println("kkkkkk");
//                        break;
//                    }
                }
                //结束
            }
            System.out.println("finished");
//            System.out.println("ssss" + paramSet.size());
//            System.out.println("llll" + duplicateList.size());
//            for (String item : duplicateList) {
//                System.out.println(item);
//            }
        } catch (IOException e) {
            System.out.println(e);
        }
//        finally {
//            if (inputStream != null) {
//                inputStream.close();
//            }
//            if (sc != null) {
//                sc.close();
//            }
    }
//        ObjectMapper mapper = new ObjectMapper();
//        List<VinPartOption> list = new ArrayList<>();
//        for (int i = 0; i < lines.size(); i++) {
//            String line = lines.get(i);
//            JsonNode node = mapper.readTree(line);
//            VinPartOption vinPartOption = mapper.convertValue(node, VinPartOption.class);
//            list.add(vinPartOption);
////                    new VinPartOption();
//            //将jsonNode转为对象
//
//        }
//        vinPartOptionMapper.batchInsert(list);

//    }

//    @PostMapping("")
//    public void storeInterpretationData() throws FileNotFoundException {
//        FileReader reader = new FileReader("C:\\Users\\admin\\IdeaProjects\\HpdEpc\\sectionOption.txt");
//        List<String> lines = reader.readLines();
//        ObjectMapper mapper = new ObjectMapper();
//
//
//    }

    @PostMapping("/insert-vin-section-option")
    public void storeSectionOption() throws FileNotFoundException, JsonProcessingException {
        FileReader reader = new FileReader("C:\\Users\\admin\\IdeaProjects\\HpdEpc\\sectionOption.txt");
        List<String> lines = reader.readLines();
//        System.out.println("sssss" + lines.size());
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            JsonNode node = mapper.readTree(line);
//            String catalogCode = node.get("catalogCode").toString();
//            catalogCode = catalogCode.substring(1, catalogCode.length() - 1);
//            System.out.println(catalogCode);
//            String sectionWithOption = node.toString();
//            VinSectionOption vinSectionOption = new VinSectionOption();
//            vinSectionOption.setCatalogcode(catalogCode);
//            vinSectionOption.setSectionwithoption(sectionWithOption);


//            VinSectionOption2 vinSectionOption2 = mapper.convertValue(node, VinSectionOption2.class);
            VinSectionOption2 vinSectionOption2 = new VinSectionOption2();
            vinSectionOption2.setDriveType(node.get("driveType").toString().substring(1, node.get("driveType").toString().length() - 1));
            vinSectionOption2.setWeatherType(node.get("weatherType").toString().substring(1, node.get("weatherType").toString().length() - 1));
            //数组
            vinSectionOption2.setUccCodes(node.get("uccCodes").toPrettyString());

//            JsonNode uccNodes = node.get("uccCodes");
//            System.out.println(uccNodes.toPrettyString());
            //2、3、5的倍数。2、3、5 4、6、9、10, 放到队列里面，一个元素小于乘以2一定大于任何一个比它小的元素乘以2，但不一定大于比它小的元素乘以3，所以，也不一定大于比它小的元素乘以5，
            //所以，需要把最新乘以2、3、5的元素比较一下大小
            //为了不遗漏，每个元素都需要乘以2、3、5,从小的元素开始乘
            //这个数组是怎么定义的？,以i、j结尾的最长公共子串吗？还是前i,j个元素最长公共子串。如果是前i，j个元素的最长公共子串。
            //最长有效子串f[i][j] = f[i][j] + 1;
            //正则.*, f[i][j] = f[i - 1][j - 1] + 1;
            //f[i][j] = f[i - 1][j] + f[i][j - 1];

            vinSectionOption2.setOptionCodes(node.get("optionCodes").toPrettyString());
            vinSectionOption2.setExcludedOptionCodes(node.get("excludedOptionCodes").toPrettyString());

            vinSectionOption2.setCatalogCode(node.get("catalogCode").toString().substring(1, node.get("catalogCode").toString().length() - 1));
//            vinSectionOption2.setSectionNumberDisplay(node.get(""));
//            vinSectionOption2.setPageDisplay("");
//            vinSectionOption2.setIndexLevel("");
            vinSectionOption2.setSectionCode(node.get("sectionCode").toString().substring(1, node.get("sectionCode").toString().length() - 1));
            if (node.get("sectionId") != null) {
                vinSectionOption2.setSectionId(node.get("sectionId").toString());
            }

            vinSectionOption2.setExcludedNationCodes(node.get("excludedNationCodes").toPrettyString());

            if (node.get("nationCodes") != null) {
                vinSectionOption2.setNationCodes(node.get("nationCodes").toPrettyString());
            }

            if (node.get("beginDate") != null) {
                vinSectionOption2.setBeginDate(node.get("beginDate").toString());
            }

            if (node.get("endDate") != null) {
                vinSectionOption2.setEndDate(node.get("endDate").toString());
            }

            if (node.get("topInterpretationStream") != null) {
                vinSectionOption2.setTopInterpretationStream(node.get("topInterpretationStream").toPrettyString());
            }

            if (node.get("parentInterpretationStream") != null) {
                vinSectionOption2.setParentInterpretationStream(node.get("parentInterpretationStream").toPrettyString());
            }
            //转为数组
//            vinSectionOption2.setParentInterpretationStream();
//            vinSectionOption2.setTopInterpretationStream();
            //结

//            vinSectionOption2.setIllustrations();
//            vinSectionOption2.setAnyParts();
//            vinSectionOption2.set
            vinSectionOptionMapper2.insert(vinSectionOption2);

            //将node转为json
        }

        //将读到的行转为对象，存到表里，或者存字符串
    }

    public String convertArrToStr(String[] arr) {
        StringBuilder builder = new StringBuilder();
        for (String item : arr) {
            builder.append(item).append(",");
        }
        return builder.substring(0, builder.length() - 1);
    }

//    public void storeSectionOption() {
//
//    }

//    select vin, [Column 5] from (
//            SELECT vin, [Column 5], Row_Number() OVER(partition by [Column 5] order by [Column 5])  as  rank
//    FROM VINData
//) temp
//    where rank = 1


//    public static void readFile() throws IOException {
//        // 1.读取json文件
//        this.refreshFileList("D:\\section\\SectionData");
//
//        FileReader interpretation = new FileReader("C:\\Users\\admin\\Desktop\\每日\\2024-01\\InterpretationData.txt");
//        List<String> lines = interpretation.readLines();
//        for (int i = 0; i < filelist.size(); i++) {
//            FileReader reader = new FileReader(filelist.get(i));
//            // System.out.println("kkk:" + filelist.get(i));
//            String content = reader.readAll();
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode node = mapper.readTree(content);
//            JsonNode catalogNode = node.get("clientSectionList").get("data");
//            JsonNode sectionNode = node.get("clientSectionList").get("sections");
//            sectionNode.get("childrenIndex");
//            String catalogStr = catalogNode.toString().substring(1, catalogNode.toString().length() - 1);
//            Set<String> sectionSet = new HashSet<>();
//
//            if (sectionNode.isArray()) {
//                for (JsonNode objnode : sectionNode) {
//                    if (objnode.get("childrenIndex").size() == 0) {
//                        sectionSet.add(objnode.get("code").toString().substring(1, objnode.get("code").toString().length() - 1));
//                    }
//                }
//            }
//            String interpretationStr = "";
//
//            for (String line : lines) {
//                if (line.contains(catalogNode.toString())) {
//                    interpretationStr = line;
//                }
//            }
//            List<String> rowList = new ArrayList<>();
//
//            FileWriter fileWriter = new FileWriter("SectionPartParams", true);
//            String row;
//            for (String section : sectionSet) {
//                row = catalogStr + " " + section + " " + interpretationStr + " " + 0 + " " + 1;
//                rowList.add(row);
//                fileWriter.println(row);
//            }
//            fileWriter.close();
//        }
//
////        Set<String> catalogSet = new HashSet<>();
////        catalogSet.add(catalogNode.toString());
//
////        StringBuilder sb = new StringBuilder();
//
//        //1.setbody
//        //2.edit,在调用某个接口的地方，前后加入参数
//        //做一下试验吧
//
////        System.out.println(rowList.size());
//
////        for () {
////
////        }
//
////        System.out.println(sectionSet.size());
////        System.out.println(sectionNode);
//
////        sectionNode.get
//        //
////        for () {
////
////        }
//
////        JsonNode
//        //        System.out.println(666);
////        Object obj = mapper.writeValueAsString(content);
////        System.out.println(obj);
//
////        System.out.println(content);
//
//        // 2.获取到对应的catalog和section,以及对应的interpretation,后面加上false和true
//
//        // 3.存到另一个文件中
//    }

//    FileReader catalogSections = new FileReader("D:\\section\\CatalogSections.txt");
//    String content = catalogSections.readAll();
//    ObjectMapper mapper = new ObjectMapper();
//    JsonNode node = mapper.readTree(content);


    @PostMapping("/insert-flat-section")
    public void readFlatSection() throws IOException {
        refreshFileList("D:\\section\\SectionData");
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < filelist.size(); i++) {
            FileReader reader = new FileReader(filelist.get(i));
            // System.out.println("kkk:" + filelist.get(i));
            String content = reader.readAll();
            JsonNode node = mapper.readTree(content);
//            JsonNode clientCatalogAttributesNode = node.get("clientCatalogInformation");
//            String clientCatalogAttributesStr = mapper.writeValueAsString(clientCatalogAttributesNode);
//            JSONObject clientCatalogAttributes = new JSONObject(clientCatalogAttributesStr);
            ObjectNode clientCatalogAttributes = node.with("clientCatalogInformation");
//            JsonNode clientGlossariesNode = node.get("clientGlossaries");
//            String clientGlossariesStr = mapper.writeValueAsString(clientGlossariesNode);
//            JSONObject clientGlossaries = new JSONObject(clientGlossariesStr);
            ObjectNode clientGlossaries = node.with("clientGlossaries");
//            JsonNode clientSectionList = node.get("clientSectionList");
//            String clientSectionStr = mapper.writeValueAsString(clientSectionList);
            ObjectNode clientSection = node.with("clientSectionList");
//            JSONObject clientSection = new JSONObject(clientSectionStr);

            //获取catalogCode
//            String catalogCode = node.get("clientSectionList").get("data").toString();
//            FlatSection flatSection = new FlatSection(catalogCode, clientCatalogAttributes, clientGlossaries, clientSection);
//            flatSectionMapper.insert(flatSection);

        }
    }

    //再创建下一个catalog的参数，注意不要有错。跑一次需要很久。
    //开虚拟机跑
//    public static void main(String[] args) throws DocumentException, IOException {
//        SAXReader r = new SAXReader();
    //获取xml文件
//        File f = new File("C:\\Users\\admin\\IdeaProjects\\HpdEpc\\test.txt");
//        FileReader fileReader = new FileReader("C:\\Users\\admin\\IdeaProjects\\HpdEpc\\test.txt");
//        List<String> fine = fileReader.readLines();
//        //将xml文件解析成一个docment对象
//        List<String> lines = fileReader.readLines();
//        String firstLine = lines.get(0);
//        int index = firstLine.indexOf(" ");
//        System.out.println(firstLine.substring(0, index) + "aaa" + firstLine.substring(index + 1, firstLine.length()));

//        Document doc = r.read(f);
//        Element root = doc.getRootElement();
//        searchCatalogWithSectionsParam();
//        FileReader fileReader = new FileReader("C:\\Users\\admin\\IdeaProjects\\HpdEpc\\vin");
//        List<String> lines = fileReader.readLines();
//        for (String line : lines) {
//            System.out.println(line);
//        }
//        Object obj = new Object();
//        String a = obj.toString();
//        System.out.println(a);
//        storeSectionOption();
//    }

    //应该保存一下id和deleteflag,方便统计数据
    @PostMapping("/store-vin")
    public void storeVin() throws JsonProcessingException, FileNotFoundException {
        Scanner scanner = new Scanner(new java.io.FileReader("C:\\Users\\admin\\Desktop\\新建文件夹 (4)\\vin2.idx"));
//        scanner.nextLine();
        ObjectMapper mapper = new ObjectMapper();
        List<SectionPart> sectionParts = new ArrayList<>();
        List<Catalog> catalogs = catalogMapper.selectAll();
        Set<String> catalogCodes = catalogs.stream().map(Catalog::getCatalogCode).collect(Collectors.toSet());
        List<Vin> vinList = new ArrayList<>();
        int count = 0;
//        int totalCount = 0;

        boolean start = false;
        while (scanner.hasNextLine()) {

            //取所有catalogcode,取8位或9位，看是否匹配
            String row = scanner.nextLine();
            if ("KM8SRDHFXHU248092KHMAPB816 B8WCJ5A1G   GK7 L320170301".equals(row)) {
                start = true;
            }
            if (!start) {
                continue;
            }

            String[] strs = row.split("\\s+");
            String vinCode = strs[0].substring(0, 17);


            System.out.println("vin:" + vinCode);
            int catalogEndIndex = catalogCodes.contains(strs[0].substring(17, 26)) || strs[0].length() == 26 ? 26 : 27;
            String catalogCode = strs[0].substring(17, catalogEndIndex);
//            System.out.println("catalog:" + catalogCode);
            String modelCode = "";
            String plantCode = "";
            String date = "";
            if (strs.length == 3) {
                modelCode = strs[0].substring(catalogEndIndex, strs[0].length());
                plantCode = strs[1];
                date = strs[2];
                if (!(date.charAt(0) > '0' && date.charAt(0) < '9')) {
                    date = date.substring(2);
                }
            } else if (strs.length == 4) {
                modelCode = strs[1];
                plantCode = strs[2];
                date = strs[3];
                if (!(date.charAt(0) > '0' && date.charAt(0) < '9')) {
                    date = date.substring(2);
                }
            } else if (strs.length == 2) {
                //那些不太清楚的字段先不管了
                modelCode = strs[0].substring(catalogEndIndex, strs[0].length());
                date = strs[1];
                if (!(date.charAt(0) > '0' && date.charAt(0) < '9')) {
                    date = date.substring(2);
                }
            }

//            System.out.println("modelCode:" + modelCode);
//            String plantCode = strs[1];
//            System.out.println("plantCode:" + plantCode);
//            String date = strs[2];
//            System.out.println("date:" + date);
            if (start) {

                Vin vin = new Vin(vinCode, catalogCode, modelCode, plantCode, date);
                vinList.add(vin);
                count++;
            }

            if (count == 1000 || !scanner.hasNext()) {
                vinMapper.batchInsert(vinList);
                vinList = new ArrayList<>();
                count = 0;
            }

            //年份前面可能还有一个字段
            //catalog和modelcode间可能存在空格

//            System.out.println(row);
//        JsonNode node = mapper.readTree(row);
//        JsonNode catalogNode = node.get("catalogs");
            //数组用json字段，
//        String catalogCode = catalogNode.get(0).get("data").toString();
//        JsonNode parts = catalogNode.get(0).get("parts");
//        if (parts.isArray()) {
//            Iterator<JsonNode> elements = parts.iterator();
//            while (elements.hasNext()) {
//                JsonNode element = elements.next();
//
//                SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                //将sectionParts转为sectionPart,
//                SectionPart sectionPart = SectionPart.convert(sectionPartDto);
//                sectionParts.add(sectionPart);
//
//            }
        }

        // 先插一条
//        sectionPartsMapper.batchInsert(sectionParts);
        scanner.close();

    }

    //先取一个catalog的数据
    @PostMapping("/store-parts")
    public void storeParts() throws FileNotFoundException, JsonProcessingException {
        //字段不能转化成jsonobject
        //读取文件
//        Scanner scanner = new Scanner(new java.io.FileReader("D:\\section\\sectionOptionApplicability.txt"));
        Scanner scanner = new Scanner(new java.io.FileReader("E:\\新建文件夹\\partsAndImages.txt"));
//        scanner.nextLine();
        ObjectMapper mapper = new ObjectMapper();
        List<SectionPart> sectionParts = new ArrayList<>();
        int totalCount = 0;
        //BufferedReader
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            if (!row.startsWith("{")) {
                continue;
            } else {
                JsonNode node = mapper.readTree(row);
                JsonNode parts = node.get("parts");

                //数组用json字段，
//            String catalogCode = catalogNode.get(0).get("data").toString();
//            JsonNode parts = catalogNode.get(0).get("parts");

                if (parts.isArray()) {
                    Iterator<JsonNode> elements = parts.iterator();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
//                        System.out.println(6667 + element.toPrettyString());

                        SectionPart sectionPart = mapper.convertValue(element, SectionPart.class);
                        //将sectionParts转为sectionPart,
//                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                        sectionParts.add(sectionPart);
                    }
                }
                if (sectionParts.size() == 1000 || !scanner.hasNextLine()) {
                    sectionPartsMapper.batchInsert(sectionParts);
                    sectionParts = new ArrayList<>();
                }

//        System.out.println("bbbb" + sectionParts.size());
//        System.out.println("kkkk");
//            totalCount += Integer.parseInt(catalogNode.get(0).get("total").toString());
//        }
//        System.out.println("aaaa" + totalCount);
//        System.out.println(totalCount);
//        System.out.println(node.toPrettyString());
//        System.out.println(catalogNode.get(0).get("total"));
//        }
//         System.out.println(row);

//            String line = scanner.nextLine();
////            line
//
//            System.out.println(line);
//            System.out.println(scanner.nextLine());
//            row++;
//            line = scanner.nextLine();
//            int firstBlankIndex = line.indexOf(" ");
//            int lastBlankIndex = line.lastIndexOf(" ");
//            String catalogCode = line.substring(0, firstBlankIndex - 1);
//            String interpretation = line.substring(firstBlankIndex, lastBlankIndex);

//            System.out.println(catalogCode);
//            System.out.println(interpretation);

                //是xml
//            System.out.println();
//            JSONObject interpretationJSONObj = XML.toJSONObject(interpretation);


//            System.out.println("aaaa" + interpretationJSONObj.toJSONString(4));

//            Document document = DocumentHelper.parseText(option);
                //先读一行
//            Element rootElement = document.getRootElement();

//            System.out.println();

                //是json
//            String applicability = splitLine[2];

            }
        }
//        System.out.println("rrrrr:" + row);
        scanner.close();

    }


    //把可选项转换为多个字段，一共有几种类型
    //先把零件本身存起来
    @PostMapping("/store-option-section-relation")
    public void storeOptionSectionRelation() throws FileNotFoundException, DocumentException, JsonProcessingException {
        //读取文件
        Scanner scanner = new Scanner(new java.io.FileReader("D:\\section\\sectionOptionApplicability.txt"));
        ObjectMapper objectMapper = new ObjectMapper();
        int count = 0;
        List<SectionOptionApplicability> list = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
//            System.out.println(line);
//            System.out.println(scanner.nextLine());
//            line = scanner.nextLine();

            int firstBlankIndex = line.indexOf(" ");
            int lastBlankIndex = line.lastIndexOf(" ");
            //catalogcode错误
            String catalogCode = line.substring(0, firstBlankIndex);
            String interpretation = line.substring(firstBlankIndex, lastBlankIndex);

//            System.out.println(catalogCode);
//            System.out.println(interpretation);

            JSONObject interpretationJSONObj = XML.toJSONObject(interpretation);

//            System.out.println("aaaa" + interpretationJSONObj.toJSONString(4));

            String applicability = line.substring(lastBlankIndex + 1, line.length());
            JsonNode applicabilityNode = objectMapper.readTree(applicability);
//            System.out.println(applicabilityNode.get("sectionApplicability"));

            JSONObject jsonObj = interpretationJSONObj.getJSONObject("interpretation");

            SectionOptionApplicability sectionOptionApplicability = new SectionOptionApplicability();
            sectionOptionApplicability.setCatalogCode(catalogCode);
            String date = jsonObj.getStr("builddate");
//            .getStr("builddate")
            if (date != null && !"".equals(date)) {
                sectionOptionApplicability.setYear(Integer.parseInt(date.substring(0, 4)));
            }

            Object avs = jsonObj.get("avs");
            if (avs != null) {
                if (avs instanceof JSONArray) {
                    JSONArray arr = new JSONArray(avs);
                    for (int i = 0; i < arr.size(); i++) {
                        switch (arr.getJSONObject(i).getStr("type")) {
                            case "01":
                                sectionOptionApplicability.setType01(arr.getJSONObject(i).getStr("data"));
                                break;
                            case "02":
                                sectionOptionApplicability.setType02(arr.getJSONObject(i).getStr("data"));
                                break;
                            case "03":
                                sectionOptionApplicability.setType03(arr.getJSONObject(i).getStr("data"));
                                break;
                            case "04":
                                sectionOptionApplicability.setType04(arr.getJSONObject(i).getStr("data"));
                                break;
                            case "05":
                                sectionOptionApplicability.setType05(arr.getJSONObject(i).getStr("data"));
                                break;
                            case "06":
                                sectionOptionApplicability.setType06(arr.getJSONObject(i).getStr("data"));
                                break;
                            case "WT":
                                sectionOptionApplicability.setTypewt(arr.getJSONObject(i).getStr("data"));
                                break;
                            case "DT":
                                sectionOptionApplicability.setTypedt(arr.getJSONObject(i).getStr("data"));
                                break;
                        }
                    }
                } else if (avs instanceof JSONObject) {
                    JSONObject obj = new JSONObject(avs);
                    switch (obj.getStr("type")) {
                        case "01":
                            sectionOptionApplicability.setType01(obj.getStr("data"));
                            break;
                        case "02":
                            sectionOptionApplicability.setType02(obj.getStr("data"));
                            break;
                        case "03":
                            sectionOptionApplicability.setType03(obj.getStr("data"));
                            break;
                        case "04":
                            sectionOptionApplicability.setType04(obj.getStr("data"));
                            break;
                        case "05":
                            sectionOptionApplicability.setType05(obj.getStr("data"));
                            break;
                        case "06":
                            sectionOptionApplicability.setType06(obj.getStr("data"));
                            break;
                        case "WT":
                            sectionOptionApplicability.setTypewt(obj.getStr("data"));
                            break;
                        case "DT":
                            sectionOptionApplicability.setTypedt(obj.getStr("data"));
                            break;
                    }
                }
            }
            sectionOptionApplicability.setIndex(applicabilityNode.get("sectionApplicability").toString());
            list.add(sectionOptionApplicability);
            count++;
            if (count == 1000 || !scanner.hasNext()) {
//                System.out.println("996" + sectionOptionApplicabilityMapper);
                sectionOptionApplicabilityMapper.batchInsert(list);
                list = new ArrayList<>();
                count = 0;
            }
//        System.out.println(applicability);
//        System.out.println("rrrrr:" + row);
        }
        scanner.close();
//        }
    }

//    @PostMapping("/store-get-vin-param")
//    public void getVinParam() throws IOException {
////        List<Catalog> catalogList = catalogMapper.selectAll();
////        List<String> catalogCodes = catalogList.stream().map(Catalog::getCatalogCode).collect(Collectors.toList());
//        //用in vinMapper.select()
//
//        FileWriter fileWriter = new FileWriter("SearchCatalogWithSectionsParams.txt", true);
//        int start = 1;
//        int size = 100000;
//
//        long maxId = vinMapper.selectMaxId();
//
//        while (start <= maxId) {
//            List<String> vinList = vinMapper.selectByCatalogcodein(start, start + size);
//            for (Vin vin : vinList) {
//                fileWriter.println(vin.getVincode());
//            }
//            start += size;
//        }
//
//        fileWriter.close();
//        //生成vin参数文件
//
//    }

//    public static void main(String[] args) {
////        String a = "1 2";
////        a.split("\\s+");
////        JsonObject jsonObject = new JsonObject();
////        jsonObject.getAsString();
////        JsonElement jsonElement = jsonObject.get("aaa");
////        JsonArray arr = jsonElement.getAsJsonArray();
////        System.out.println(arr.toString());
////        String a = "WT  3|0101A9|2111A2|2841G3|2843A6|2844B1|2861B3|2865A2|2891C1|3111C4|3281A1|371CA1|3711B3|3921A1|4371B1|4503A2|4504A2|4509A1|4510A1|4911A2|4951D9|529AA2|5291T1|5293HK|5294B4|5297G3|5411A1|5412A2|5416A1|5511B2|5512A2|5611B2|5631A6|569BB6|569DB6|5691A2|5692A2|5693A3|5695A1|5711A2|5811A6|5812A9|5891A2|5894A1|5911B4|5971A1|6501A1|811AA1|8112A2|812AA1|8122A2|8124A1|8125A2|813AA1|813BA1|813MA1|8131A2|8132A1|8135A2|8152A4|819AA1|819BA1|8195A2|82A5A1|8221A1|8231B2|8232A4|8233B1|8236A3|8241A3|8261A2|8262A9|834CA2|834DB2|8341A1|8347A1|8421E1|846BA1|846LA3|846NA1|8491A1|8492A1|85ACA1|85APA1|8511B1|852AA1|852BA1|852DA1|852EA1|852GA1|852HA1|852KA1|8521A3|8522A1|8523A3|8531A3|8561A2|8571C1|8572A3|8573A2|8578A1|858AA5|858RB1|8611B3|8612A1|863CA2|8631AB|8641A1|8642A1|8648A1|8649B5|871BA1|8711A1|8712B3|8722A3|8762C2|8763C1|8765A2|8772B1|8811C1|8821A3|8823A1|8824A2|8832A1|8836A3|885RA1|8854A3|886BA3|8866A1|888AA2|888DA1|888EA1|888FA1|888KA3|888NA1|888RA3|8881B1|8884D1|8885A2|8888A1|889MA1|8912L3|892DA1|892GA1|8981A5|921JA1|9211F1|9213A3|923DA1|9234A1|9235A1|9241A6|928HA2|928WA2|9286A1|9287A1|9297A1|9298A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E6A1|94G0A1|94H6A1|94H9A1|94K3A1|94M0A3|94N0A2|940AA3|940BA1|940CA1|940DA1|9401C1|9402A1|941AA1|941CA3|941GA1|9451A1|9491A1|9541A1|9542A1|9545A1|9574A2|961AA1|9611P6|9614B1|9615A2|9621E1|9624L1|963CA1|963GA1|963KA1|965AA1|965BA1|965CA1|965KA1|9651A1|9661B1|967KA1|9681A1|9684A1|9714A1|972DA1|9721C1|9761A1|979EA1|981AA1|9811A2|9817A1|9819A1|9909A1";
////        System.out.println(a.contains(""));
//
//        // 一个个比对
//        String a1 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a2 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a3 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a4 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a5 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a6 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a7 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a8 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a9 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a10 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a11 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a12 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a13 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//        String a14 = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|3711C2|371CA1|371FA1|3731B4|4371B1|4503A2|4504A2|4509A1|450AA2|5291VA|5293J7|5294A3|5297C1|529AA2|529TA2|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|5691A2|5692A2|5693A3|5695A1|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5711B1|5811A6|5812A9|5891A2|5894A1|5898A1|589AA1|589GA1|589HA1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|8111A1|8112A1|811AA1|8131A2|8132A1|8135A3|8136A1|813BA1|813RA1|8151A2|8152A5|8153A1|8171A1|8172A1|8173A1|8175A2|8177A1|817AB3|817BA1|8191A2|8192C3|8194B3|8198A1|819AA1|819BA1|8221A4|8223A2|8225A3|8231B1|8232A1|8233A1|8235A1|8236A3|823UC1|8241A3|8245A2|824AA3|824BA1|824CA1|824HA5|8261A2|8262A3|82A5A1|82A7A1|82A8A1|82CAA1|8421E1|8451A1|8452A2|846BA1|8491A1|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8511B1|8521A1|8522A1|8523A1|852BA1|852EA2|852GA1|852HA1|852LA2|8531A3|8532A2|853AA2|853JA1|8571B1|8573A3|8575A3|8578A1|857FA1|857KA1|858AA1|858BA1|858CA1|858RA1|858SA1|8611B4|8612A1|8631A9|863AAK|863CA0|8641A1|8642A1|8648A1|8649B5|864HA1|8651B1|8652B3|8653A3|8656A2|865EA2|865HA1|865LA1|8692A1|869AA1|869BA3|86A2A1|8712B4|871BA1|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|8772B2|8775A6|877BA1|877FA1|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|8854A1|885EA2|885KA1|886BA1|8881B1|8884B1|8885A2|8889A1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8912C1|8917B1|8918B1|8919B1|8922A2|892DA1|892GA1|8981A5|8982A5|89C1A1|89E4A1|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|9211P7|9212C1|9213B4|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|9241B2|9242B1|9248A2|9249A2|924AA1|924CA1|9251A2|9252C1|9271A4|9272B1|9281C1|9285B1|9287C1|928CC2|9295B1|9401D5|9402A1|940BA1|940CA1|940DA1|940EA1|9451A1|9491A1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|9611ZG|9615A2|9618A4|961LA2|961NR4|961PA1|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|9811A2|9819A1|981AA1|9851A1|9852A1|9871A1|3734A1|961UA1";
//
//
//
////        String a15 = "WT  3 2111A22536A22539A42841D42843B22844B22921A43111B43113A23711C2371CA1371FA13731B44371B14503A24504A24509A1450AA25291VA5293J75294A35297C1529AA2529TA25411A15412A75511B15512A75611C25631A45691A25692A25693A35695A1569BB6569CA1569HA1569MA1569NA1569PA15711B15811A65812A95891A25894A15898A1589AA1589GA1589HA15911A65971A46501A16921A27931A17932A18111A18112A1811AA18131A28132A18135A38136A1813BA1813RA18151A28152A58153A18171A18172A18173A18175A28177A1817AB3817BA18191A28192C38194B38198A1819AA1819BA18221A48223A28225A38231B18232A18233A18235A18236A3823UC18241A38245A2824AA3824BA1824CA1824HA58261A28262A382A5A182A7A182A8A182CAA18421E18451A18452A2846BA18491A184C3A484GBA184K9A184M2A184M5A18511B18521A18522A18523A1852BA1852EA2852GA1852HA1852LA28531A38532A2853AA2853JA18571B18573A38575A38578A1857FA1857KA1858AA1858BA1858CA1858RA1858SA18611B48612A18631A9863AAK863CA08641A18642A18648A18649B5864HA18651B18652B38653A38656A2865EA2865HA1865LA18692A1869AA1869BA386A2A18712B4871BA18721B18723A28725A18731B18762C28763C18765A28772B28775A6877BA1877FA18791B18811C18821A18824A28825A48836A18854A1885EA2885KA1886BA18881B18884B18885A28889A1888AA2888DA1888FA1888HA1888JA1888NA1888SA18912C18917B18918B18919B18922A2892DA1892GA18981A58982A589C1A189E4A19119A2915AA1915BA1915CA1915EA1915NA1915VA1915WA19211P79212C19213B4921AC2921NA2921PA2921QB1921RA2921WA19231C29234B29235B19236B19237A29238A19241B29242B19248A29249A2924AA1924CA19251A29252C19271A49272B19281C19285B19287C1928CC29295B19401D59402A1940BA1940CA1940DA1940EA19451A19491A194C1A194C6A194C7A194C8A194C9A194D0A194D1A194D2A194D4A194D7A194D9A194E3A194E5A194E6A194F3A194G0A194H7A194H9A194J0A194J1A194K3A194K7A194M0A394N0A294X3A194X4A19511A19541A19545A19549A19552E1956GA1956MA19574A1958AA1958FD1958GA1958JB3958VB2958XA1959AA1959EA1959NA39611ZG9615A29618A4961LA2961NR4961PA19620E2963CA1963MA1963ZA1965AA1965BA1965CA1965DA1965EA1965QA1965TA19661B2967AA1967EA1967FA29681A19684A19686A19714A1972SA19741A19761A19763A19768A29769A1979EA49811A29819A1981AA19851A19852A19871A13734A1961UA1";
//        String a = "WT  3|2111A2|2536A2|2539A4|2841D4|2843B2|2844B2|2921A4|3111B4|3113A2|371CA1|371FA1|3711C2|3731B4|3734A1|4371B1|450AA2|4503A2|4504A2|4509A1|529AA2|529TA2|5291VA|5293J7|5294A3|5297C1|5411A1|5412A7|5511B1|5512A7|5611C2|5631A4|569BB6|569CA1|569HA1|569MA1|569NA1|569PA1|5691A2|5692A2|5693A3|5695A1|5711B1|5811A6|5812A9|589AA1|589GA1|589HA1|5891A2|5894A1|5898A1|5911A6|5971A4|6501A1|6921A2|7931A1|7932A1|811AA1|8111A1|8112A1|813BA1|813RA1|8131A2|8132A1|8135A3|8136A1|8151A2|8152A5|8153A1|817AB3|817BA1|8171A1|8172A1|8173A1|8175A2|8177A1|819AA1|819BA1|8191A2|8192C3|8194B3|8198A1|82A5A1|82A7A1|82A8A1|82CAA1|8221A4|8223A2|8225A3|823UC1|8231B1|8232A1|8233A1|8235A1|8236A3|824AA3|824BA1|824CA1|824HA5|8241A3|8245A2|8261A2|8262A3|84C3A4|84GBA1|84K9A1|84M2A1|84M5A1|8421E1|8451A1|8452A2|846BA1|8491A1|8511B1|852BA1|852EA2|852GA1|852HA1|852LA2|8521A1|8522A1|8523A1|853AA2|853JA1|8531A3|8532A2|857FA1|857KA1|8571B1|8573A3|8575A3|8578A1|858AA1|858BA1|858CA1|858RA1|858SA1|86A2A1|8611B4|8612A1|863AAK|863CA0|8631A9|864HA1|8641A1|8642A1|8648A1|8649B5|865EA2|865HA1|865LA1|8651B1|8652B3|8653A3|8656A2|869AA1|869BA3|8692A1|871BA1|8712B4|8721B1|8723A2|8725A1|8731B1|8762C2|8763C1|8765A2|877BA1|877FA1|8772B2|8775A6|8791B1|8811C1|8821A1|8824A2|8825A4|8836A1|885EA2|885KA1|8854A1|886BA1|888AA2|888DA1|888FA1|888HA1|888JA1|888NA1|888SA1|8881B1|8884B1|8885A2|8889A1|89C1A1|89E4A1|8912C1|8917B1|8918B1|8919B1|892DA1|892GA1|8922A2|8981A5|8982A5|9119A2|915AA1|915BA1|915CA1|915EA1|915NA1|915VA1|915WA1|921AC2|921NA2|921PA2|921QB1|921RA2|921WA1|9211P7|9212C1|9213B4|9231C2|9234B2|9235B1|9236B1|9237A2|9238A1|924AA1|924CA1|9241B2|9242B1|9248A2|9249A2|9251A2|9252C1|9271A4|9272B1|928CC2|9281C1|9285B1|9287C1|9295B1|94C1A1|94C6A1|94C7A1|94C8A1|94C9A1|94D0A1|94D1A1|94D2A1|94D4A1|94D7A1|94D9A1|94E3A1|94E5A1|94E6A1|94F3A1|94G0A1|94H7A1|94H9A1|94J0A1|94J1A1|94K3A1|94K7A1|94M0A3|94N0A2|94X3A1|94X4A1|940BA1|940CA1|940DA1|940EA1|9401D5|9402A1|9451A1|9491A1|9511A1|9541A1|9545A1|9549A1|9552E1|956GA1|956MA1|9574A1|958AA1|958FD1|958GA1|958JB3|958VB2|958XA1|959AA1|959EA1|959NA3|961LA2|961NR4|961PA1|961UA1|9611ZG|9615A2|9618A4|9620E2|963CA1|963MA1|963ZA1|965AA1|965BA1|965CA1|965DA1|965EA1|965QA1|965TA1|9661B2|967AA1|967EA1|967FA2|9681A1|9684A1|9686A1|9714A1|972SA1|9741A1|9761A1|9763A1|9768A2|9769A1|979EA4|981AA1|9811A2|9819A1|9851A1|9852A1|9871A1";
////        System.out.println(sortOptionCode(convertOptionCode(a15)));
//        String sortedA = sortOptionCode(a);
////        System.out.println(sortedA);
//        //        String[] strs = a.split("\\|");
////        List<String> sortedStrs = Arrays.asList(strs).stream().sorted().collect(Collectors.toList());
////        System.out.println(996);
////        StringBuilder sb = new StringBuilder();
////        sb.append(sortedStrs.get(sortedStrs.size() - 1));
////        for (int i = 0; i < sortedStrs.size() - 1; i++) {
////            sb.append("|").append(sortedStrs.get(i));
////        }
////        System.out.println(sb.toString());
//
//        System.out.println(sortedA.equals(sortOptionCode(a1)));
//        System.out.println(sortedA.equals(sortOptionCode(a2)));
//        System.out.println(sortedA.equals(sortOptionCode(a3)));
//        System.out.println(sortedA.equals(sortOptionCode(a4)));
//        System.out.println(sortedA.equals(sortOptionCode(a5)));
//        System.out.println(sortedA.equals(sortOptionCode(a6)));
//        System.out.println(sortedA.equals(sortOptionCode(a7)));
//        System.out.println(sortedA.equals(sortOptionCode(a8)));
//        System.out.println(sortedA.equals(sortOptionCode(a9)));
//        System.out.println(sortedA.equals(sortOptionCode(a10)));
//        System.out.println(sortedA.equals(sortOptionCode(a11)));
//        System.out.println(sortedA.equals(sortOptionCode(a12)));
//        System.out.println(sortedA.equals(sortOptionCode(a13)));
//        System.out.println(sortedA.equals(sortOptionCode(a14)));
////        System.out.println(sortedA.equals(sortOptionCode(convertOptionCode(a15))));
//    }
//    public static void main(String[] args) {
//        System.out.println(generateOptionCode("HMA390PA99", "09102831292037144372451152745276528A529X5462546456135614561556915692569A589159105971650181338134813581368161819181958234824383418343842784708476847784798513852485278531857B857D8586862686398651871787188756875D8784880088058810881688728888888B888D888F8898889B889D889F890189828987898C92159232924192509255926292759281928292859341940194159495951095449572957895859607960996139654966396GX96HB96S896SA96SE970B971397689862ALL0W30 Y090", "5277"));
//    }

    public static String convertOptionCode(String optionCode) {
//        if (!optioncode.isEmpty()) {
        StringBuilder optionStringBuilder = new StringBuilder();
        int index = 5;
        System.out.println();
        optionStringBuilder.append(optionCode.substring(0, 5));
        index++;
        while (index + 6 <= optionCode.length()) {
            optionStringBuilder.append("|").append(optionCode.substring(index, index + 6));
            index += 6;
        }
        String optionStr = optionStringBuilder.toString();
        return optionStr;
    }

    public static String sortOptionCode(String optionCode) {
        String[] strs = optionCode.split("\\|");
        List<String> sortedStrs = Arrays.asList(strs).stream().sorted().collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        sb.append(sortedStrs.get(sortedStrs.size() - 1));
        for (int i = 0; i < sortedStrs.size() - 1; i++) {
            sb.append("|").append(sortedStrs.get(i));
        }
        return sb.toString();
    }

//    public void getVin() throws IOExceptionList

    //5g
    @PostMapping("/store-vehicle-info")
    public void storeVehicleInfo() throws IOException {
        String path = "D:\\\\epc新\\\\searchCatalogWithSectionsNew.txt";
        java.io.FileReader fileReader = new java.io.FileReader(path);
        Scanner sc = new Scanner(fileReader);
        ObjectMapper mapper = new ObjectMapper();
        List<VehicleInfo> vehicleInfos = new ArrayList<>();
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.charAt(0) == '{') {
                JsonNode node = mapper.readTree(line);
                JsonNode catalogNode = node.get("catalog");
                if (catalogNode != null) {
                    JsonNode vehicleNode = catalogNode.get("vehicle");
                    String catalogCode = node.get("sectionHierarchyApplicability").get("catalogCode").asText();

                    //将vehicle对象转换为实体
                    VehicleInfoDto vehicleInfoDto = mapper.convertValue(vehicleNode, VehicleInfoDto.class);
                    VehicleInfo vehicleInfo = VehicleInfo.convert(vehicleInfoDto);
                    vehicleInfo.setCatalogCode(node.get("sectionHierarchyApplicability").get("catalogCode").asText());
                    //可以先不插
                    vehicleInfo.setType(node.get("catalog").get("type").asText());
                    vehicleInfos.add(vehicleInfo);

                    if (vehicleInfos.size() == 50 || !sc.hasNext()) {
                        vehicleInfoMapper.batchInsert(vehicleInfos);
                        vehicleInfos = new ArrayList<>();
                    }

//                    System.out.println(vehicleInfo.toString());

//                    System.out.println(vehicleNode.toPrettyString());
//                    Element rootElement = DocumentHelper.createElement("interpretation");
//                    rootElement.addAttribute("modelcode", vehicleNode.get("modelCode").asText())
//                            .addAttribute("type", catalogNode.get("type").asText())
//                            .addAttribute("catalog", catalogCode)
//                            .addAttribute("modelcode", vehicleNode.get("modelCode").asText())
//                            .addAttribute("builddate", vehicleNode.get("buildDate").asText())
//                            .addAttribute("vin", vehicleNode.get("vin").asText())
//                            .addAttribute("displaybuilddate", vehicleNode.get("displayBuildDate").asText())
//                            .addAttribute("modelyear", vehicleNode.get("modelYear").asText())
//                            .addAttribute("modelid", vehicleNode.get("modelID").asText())
//                            .addAttribute("area", vehicleNode.get("area").asText())
//                            .addAttribute("extcolourdown", vehicleNode.get("exteriorKeyColorCode").asText())
//                            .addAttribute("extcolourmain", vehicleNode.get("exteriorKeyColorCode").asText())
//                            .addAttribute("extcolourup", vehicleNode.get("exteriorKeyColorCode").asText())
//                            .addAttribute("exteriorkeycolorcode", vehicleNode.get("exteriorKeyColorCode").asText())
//                            .addAttribute("intcolour", vehicleNode.get("interiorKeyColorCode").asText())
//                            .addAttribute("interiorkeycolorcode", vehicleNode.get("interiorKeyColorCode").asText())
//                            .addAttribute("nation", vehicleNode.get("nation").asText())
//                            .addAttribute("plant", vehicleNode.get("plant").asText())
//                            .addAttribute("fullSpecificationCode", vehicleNode.get("fullSpecificationCode").asText())
//                            .addAttribute("vintag", vehicleNode.get("vinTag").asText())
//                            .addAttribute("level", "2");
                    //color参数可能设置有问题

//                    JsonNode avsNodes = vehicleNode.get("interpretationAVSData");
//                    if (avsNodes.isArray()) {
//                        for (JsonNode avsNode : avsNodes) {
//                            Element avsElement = rootElement.addElement("avs");
//                            avsElement.addAttribute("data", avsNode.get("data").asText());
//                            avsElement.addAttribute("type", avsNode.get("type").asText());
//                        }
//                    }
//                    System.out.println(catalogCode);
//                    vinSectionOptionMapper2.selectByUccCodesAndCatalogCodeAndSectionCode()
//                    List<EpcSection> epcSections = epcSectionMapper.selectByCatalogcodeAndApplicableAndParentuniqueid(catalogCode, "1", null);

//                    System.out.println("tttt" + catalogNode.get("type").asText());
                }
            }


        }
    }

    //把返回结果存进去
    @PostMapping("/generate-get-section-part-param")
    public void storeSearchCatalogWithSection() throws IOException {
        String path = "D:\\\\epc新\\\\searchCatalogWithSectionsNew.txt";
        java.io.FileReader fileReader = new java.io.FileReader(path);
        FileWriter fileWriter = new FileWriter("searchCatalogWithSectionParams");
        Scanner sc = new Scanner(fileReader);
        ObjectMapper mapper = new ObjectMapper();
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.charAt(0) == '{') {
                JsonNode node = mapper.readTree(line);
                JsonNode catalogNode = node.get("catalog");
                if (catalogNode != null) {
                    JsonNode vehicleNode = catalogNode.get("vehicle");
                    String catalogCode = node.get("sectionHierarchyApplicability").get("catalogCode").asText();
//                    System.out.println(vehicleNode.toPrettyString());
                    Element rootElement = DocumentHelper.createElement("interpretation");
                    rootElement.addAttribute("modelcode", vehicleNode.get("modelCode").asText())
                            .addAttribute("type", catalogNode.get("type").asText())
                            .addAttribute("catalog", catalogCode)
                            .addAttribute("modelcode", vehicleNode.get("modelCode").asText())
                            .addAttribute("builddate", vehicleNode.get("buildDate").asText())
                            .addAttribute("vin", vehicleNode.get("vin").asText())
                            .addAttribute("displaybuilddate", vehicleNode.get("displayBuildDate").asText())
                            .addAttribute("modelyear", vehicleNode.get("modelYear").asText())
                            .addAttribute("modelid", vehicleNode.get("modelID").asText())
                            .addAttribute("area", vehicleNode.get("area").asText())
                            .addAttribute("extcolourdown", vehicleNode.get("exteriorKeyColorCode").asText())
                            .addAttribute("extcolourmain", vehicleNode.get("exteriorKeyColorCode").asText())
                            .addAttribute("extcolourup", vehicleNode.get("exteriorKeyColorCode").asText())
                            .addAttribute("exteriorkeycolorcode", vehicleNode.get("exteriorKeyColorCode").asText())
                            .addAttribute("intcolour", vehicleNode.get("interiorKeyColorCode").asText())
                            .addAttribute("interiorkeycolorcode", vehicleNode.get("interiorKeyColorCode").asText())
                            .addAttribute("nation", vehicleNode.get("nation").asText())
                            .addAttribute("plant", vehicleNode.get("plant").asText())
                            .addAttribute("fullSpecificationCode", vehicleNode.get("fullSpecificationCode").asText())
                            .addAttribute("vintag", vehicleNode.get("vinTag").asText())
                            .addAttribute("level", "2");

                    if (vehicleNode.get("modelYearCode") != null && !vehicleNode.get("modelYearCode").asText().isEmpty()) {
                        rootElement.addAttribute("modelyearcode", vehicleNode.get("modelYearCode").asText());
                    }
                    if (vehicleNode.get("optionCodes") != null && !vehicleNode.get("optionCodes").asText().isEmpty()) {
                        rootElement.addAttribute("optioncodes", vehicleNode.get("optionCodes").asText());
                    }
                    if (vehicleNode.get("engineMipCode") != null && !vehicleNode.get("engineMipCode").asText().isEmpty()) {
                        rootElement.addAttribute("engineMipCode", vehicleNode.get("engineMipCode").asText());
                    }
                    if (vehicleNode.get("transmissionMipCode") != null && !vehicleNode.get("transmissionMipCode").asText().isEmpty()) {
                        rootElement.addAttribute("transmissionMipCode", vehicleNode.get("transmissionMipCode").asText());
                    }

                    //vehicle里面没有weatherType
//                    if (vehicleNode.get("weatherType") != null && !vehicleNode.get("weatherType").asText().isEmpty()) {
//                        rootElement.addAttribute("weathertype", vehicleNode.get("weatherType").asText());
//                    }
                    //color参数可能设置有问题

                    JsonNode avsNodes = vehicleNode.get("interpretationAVSData");
                    if (avsNodes.isArray()) {
                        for (JsonNode avsNode : avsNodes) {
                            Element avsElement = rootElement.addElement("avs");
                            avsElement.addAttribute("data", avsNode.get("data").asText());
                            avsElement.addAttribute("type", avsNode.get("type").asText());
                            if (avsNode.get("type").asText().equals("WT") && !avsNode.get("data").asText().isEmpty()) {
                                rootElement.addAttribute("weathertype", avsNode.get("data").asText());
                            }
                        }
                    }
                    System.out.println(catalogCode);
//                    vinSectionOptionMapper2.selectByUccCodesAndCatalogCodeAndSectionCode()
                    List<EpcSection> epcSections = epcSectionMapper.selectByCatalogcodeAndApplicableAndParentuniqueid(catalogCode, "1", null);
                    fileWriter.println(catalogCode + " " + epcSections.get(10).getUniqueid() + " " + rootElement.asXML());
//                    System.out.println("tttt" + catalogNode.get("type").asText());
                }
            }
        }
    }

//    @PostMapping("/generate-param")
//    public void generateParam() throws IOException {
//        //catalogCode
//        //sectionCode
//        //interpretationData
//        String path = "";
//        //读取参数文件
//        java.io.FileReader fileReader = new java.io.FileReader(path);
//        Scanner sc = new Scanner(fileReader);
//        while (sc.hasNext()) {
//            String vinCode = sc.nextLine();
//            Vin vin = vinMapper.selectByVincode(vinCode);
//            String catalogCode = vin.getCatalogcode();
//            //List<EpcSection> epcSections = epcSectionMapper.selectByCatalogcodeAndApplicableAndParentuniqueid(catalogCode, "", "");
//            EpcSection epcSection = epcSections.get(0);
//            Element rootElement = DocumentHelper.createElement("interpretation");
//            rootElement.addAttribute("catalog", catalogCode)
//                    .addAttribute("modelcode", epcSection.getMo)
//                    .;
//
//        }
//
//    }

    //如何合并可选项？
    //将多的可选项保留下来,有反例吗？
    @PostMapping("/check-vin-part")
    public void checkVinPart() throws IOException {
        //1.过滤逻辑
        //2.先把数据获取到，正在执行
        //3.获得原来的结果，存到表里（等2执行完了再做）
        //4.实现逻辑，将结果存到另一张表里

        //根据vin获取catalogCode
        //catalogCode下取一sectionCode

        //读取文件
        //最小原型
        String path = "D:\\\\epc新\\\\getSectionPartsNew.txt";
        java.io.FileReader fileReader = new java.io.FileReader(path);
        Scanner sc = new Scanner(fileReader);
        String vinCode = "";
        String catalogCode = "";
        String sectionCode = "";
        String interpretationData = "";
        int sameCount = 0;
        int ebomCount = 0;
        int rkitCount = 0;
        int otherCount = 0;
        int unMatchCount = 0;

        //主要是过于主观，没理解别人的意思
        //双向筛选
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.charAt(0) == '{') {
//                System.out.println("vvvv" + vinCode);
//                Vin vin = vinMapper.selectByVincode(vinCode);
//                System.out.println("v1");
                List<VehicleInfo> vehicleInfos = vehicleInfoMapper.selectByVin(vinCode);
//                System.out.println("v2");
                VehicleInfo vehicleInfo = vehicleInfos.get(0);
//                System.out.println("bbbbb" + vehicleInfo.getBuildDate());
                List<Catalog> catalogs = catalogMapper.selectByCatalogCode(catalogCode);
                Catalog catalog = catalogs.get(0);
//                System.out.println("v3");
                String catalogDataTypeDescription = catalog.getCatalogDataTypeDescription();
//                System.out.println("v4");
                //获取可选项
                //vin-part-option
                List<VinPartOption> vinPartOptions = vinPartOptionMapper.selectByCatalogCodeAndSectionId(catalogCode, sectionCode);
                //for (int i = 0; i < vinPartOptions.size(); i++) {
//                    vinPartOptions.get(i).setBomPartSections(null);
//                    System.out.println("begindate: " + vinPartOptions.get(i).getBeginDate());
//                    System.out.println("enddate: " + vinPartOptions.get(i).getEndDate());
//                    System.out.println(vinPartOptions.toString());
//                }
//                System.out.println("ccc" + catalogCode + "sss" + sectionCode);
//                System.out.println("ooooo" + vinPartOptions.size());
                //过滤掉isApplicable为0的数据

//                if (!catalogDataTypeDescription.equals("RKIT")) {

                //includeCountry、excludeCountry

                //比较日期
                //获取buildDate
                String buildDate = vehicleInfo.getBuildDate();
                //buildDate可能是输入的参数
//                    e.getEndDate().isEmpty()
                vinPartOptions = vinPartOptions.stream().distinct().filter(e -> ((e.getBeginDate().isEmpty() || e.getBeginDate().compareTo(buildDate) <= 0) && (e.getEndDate().compareTo(buildDate) >= 0 || e.getEndDate().isEmpty()))).collect(Collectors.toList());
//                    System.out.println("6666c" + vinPartOptions.size());
                //System.out.println("ccccc" + vinPartOptions.size());

                String vehicleInterpretationAVSData = vehicleInfo.getInterpretationAVSData();
                JSONArray jsonArr2 = JSONUtil.parseArray(vehicleInterpretationAVSData);
//                    JSONObject jsonObj1 = (JSONObject) jsonArr2.get(0);
//                    JSONObject jsonObj2 = (JSONObject) jsonArr2.get(1);
//                    JSONObject jsonObj3 = (JSONObject) jsonArr2.get(2);
//                    JSONObject jsonObj4 = (JSONObject) jsonArr2.get(3);
//                    JSONObject jsonObj5 = (JSONObject) jsonArr2.get(4);
//                    JSONObject jsonObj6 = (JSONObject) jsonArr2.get(5);
//                    JSONObject jsonObj7 = (JSONObject) jsonArr2.get(6);
                if (!catalogDataTypeDescription.equals("RKIT")) {
                    if (catalogDataTypeDescription.equals("EBOM")) {
                        ebomCount++;
                        //比较weatherType
//                        vinPartOptions = vinPartOptions.stream().filter(e -> ((e.getWeatherType().isEmpty() || e.getWeatherType().equals()))).collect(Collectors.toList());
//                        //比较driveType
//                        vinPartOptions = vinPartOptions.stream().filter(e -> ((e.getDriveType().isEmpty() || e.getDriveType().equals()))).collect(Collectors.toList());
                        //比较uccCodes
                        //先把uccCodes拆分，转化成json
//                        List<VinPartOption> filteredVinPartOption = new ArrayList<>();
                        for (VinPartOption vinPartOption : vinPartOptions) {
//                            System.out.println(vinPartOption.toString());
                            String uccCodes = vinPartOption.getUccCodes();
                            JSONArray jsonArr = JSONUtil.parseArray(uccCodes);
//                            System.out.println("spspsp" + jsonArr.toStringPretty());

//                            List<String> uccCodeList = jsonArr.stream().map(e -> String.valueOf(e)).collect(Collectors.toList());
                            //零件的uccCodeList

//                            JSONObject jsonObj8 = (JSONObject) jsonArr2.get(7);
                            //todo ModelCodeInterpretationEnabled
                            if (vinPartOption.getDriveType().isEmpty() || vinPartOption.getDriveType().equals(((JSONObject) jsonArr2.get(5)).getStr("data"))) {
                                vinPartOption.setApplicable2(true);
                            } else {
                                vinPartOption.setApplicable2(false);
                            }

                            if (vinPartOption.getWeatherType().isEmpty() || vinPartOption.getWeatherType().equals(((JSONObject) jsonArr2.get(6)).getStr("data"))) {
                                vinPartOption.setApplicable2(true);
                            } else {
                                vinPartOption.setApplicable2(false);
                            }

//                                System.out.println(jsonArr.get(0));
//                                System.out.println(jsonArr.get(1));
//                                System.out.println(jsonArr.get(2));
//                                System.out.println(jsonArr.get(3));
//                                System.out.println(jsonArr.get(4));
//                                System.out.println(jsonObj1.getStr("data"));
//                                System.out.println(jsonObj2.getStr("data"));
//                                System.out.println(jsonObj3.getStr("data"));
//                                System.out.println(jsonObj4.getStr("data"));
//                                System.out.println(jsonObj5.getStr("data"));

                            //todo 看一下可选项过滤是否正确
                            String vinTag = vehicleInfo.getVinTag();
                            if (vinPartOption.getPartCode().equalsIgnoreCase("36500-3D920")) {
//                                    System.out.println("ccccc");
                                System.out.println(vinTag);
//                                    System.out.println("ddddd");
                            }

                            //如果vin
                            if (vinTag.equals("") || vinTag.equals("1") || vinTag.equals("2") || vinTag.equals("6") || vinTag.equals("4") || vinTag.equals("C")) {
                                if (vinPartOption.getPartCode().equalsIgnoreCase("36500-3D920")) {
                                    System.out.println("66666");
                                }
                                //todo uccCodes 不是uccCode问题
                                for (int i = 0; i < 5; i++) {
                                    if (vinPartOption.getPartCode().equalsIgnoreCase("36500-3D920")) {
                                        System.out.println("yyy");
                                        System.out.println(String.valueOf(((JSONObject) jsonArr2.get(i)).get("data")));
                                        System.out.println("zzz");
                                        System.out.println(String.valueOf(jsonArr.get(i)));
                                        System.out.println(!String.valueOf(jsonArr2.get(i)).isEmpty() && !String.valueOf(jsonArr.get(i)).isEmpty() && !String.valueOf(((JSONObject) jsonArr2.get(i)).get("data")).equalsIgnoreCase(String.valueOf(jsonArr.get(i))));
                                    }
                                    if (!String.valueOf(jsonArr2.get(i)).isEmpty() && !String.valueOf(jsonArr.get(i)).isEmpty() && !String.valueOf(((JSONObject) jsonArr2.get(i)).get("data")).equalsIgnoreCase(String.valueOf(jsonArr.get(i)))) {
                                        vinPartOption.setApplicable2(false);
                                        System.out.println("qqq" + vinPartOption.getPartCode() + " " + String.valueOf(((JSONObject) jsonArr2.get(i)).get("data")) + " " + String.valueOf(jsonArr.get(i)));
                                        break;
//                                            if (vinPartOption.getPartCode().equalsIgnoreCase("36500-3D920")) {
//                                                System.out.println("qqq");
//                                                break;
//                                            }
                                    }
                                }
//                                    if ((!String.valueOf(jsonArr.get(0)).isEmpty() || String.valueOf(jsonArr.get(0)).equalsIgnoreCase(jsonObj1.getStr("data")))
//                                            && (String.valueOf(jsonArr.get(1)).isEmpty() || String.valueOf(jsonArr.get(1)).equalsIgnoreCase(jsonObj2.getStr("data")))
//                                            && (String.valueOf(jsonArr.get(2)).isEmpty() || String.valueOf(jsonArr.get(2)).equalsIgnoreCase(jsonObj3.getStr("data")))
//                                            && (String.valueOf(jsonArr.get(3)).isEmpty() || String.valueOf(jsonArr.get(3)).equalsIgnoreCase(jsonObj4.getStr("data")))
//                                            && (String.valueOf(jsonArr.get(4)).isEmpty() || String.valueOf(jsonArr.get(4)).equalsIgnoreCase(jsonObj5.getStr("data")))) {
////                            && (String.valueOf(jsonArr.get(5)).isEmpty() || String.valueOf(jsonArr.get(5)).equals(jsonObj6.getStr("data")))) {
//                                        vinPartOption.setApplicable2(true);
//                                    } else {
//
//                                        vinPartOption.setApplicable2(false);
//                                    }
                            }

                            //todo 还有optionCombineColor过滤条件

                            //todo vinTag
                            if (vinPartOption.getApplicable2()) {
                                if (vinPartOption.getVinTag().isEmpty() || vinPartOption.getVinTag().equals(vehicleInfo.getVinTag())) {
                                    vinPartOption.setApplicable2(true);
                                } else {
                                    vinPartOption.setApplicable2(false);
                                }
                            }

                            //todo 补充isOptionCodeInterpretationEnabled()
                            if (vinPartOption.getApplicable2() && vehicleInfo.getVinTag() != null && (vehicleInfo.getVinTag().equals("1") || vehicleInfo.getVinTag().equalsIgnoreCase("B"))) {

                                //todo options
                                if (vinPartOption.getOptions() != null) {
                                    if (("1".equals(vehicleInfo.getVinTag()) || "2".equals(vehicleInfo.getVinTag()) || "6".equals(vehicleInfo.getVinTag()) && vehicleInfo.getOptionCodes().length() >= 20)) {
                                        JSONArray options = JSONUtil.parseArray(vinPartOption.getOptions());
                                        if ("36500-3D920".equals(vinPartOption.getPartCode())) {
                                            System.out.println("uuuuuu");
                                        }
//                                            System.out.println("oooo"+vehicleInfo.getOptionCodes());
                                        for (Object item : options) {
                                            String itemStr = String.valueOf(item);
//                                                System.out.println("iiii" + itemStr);
//                                                System.out.println(vehicleInfo.getOptionCodes());
                                            if (!itemStr.isEmpty() && vehicleInfo.getOptionCodes().length() > 0 && !vehicleInfo.getOptionCodes().contains(itemStr)) {
                                                if ("36500-3D920".equals(vinPartOption.getPartCode())) {
                                                    System.out.println("vvvvvv");
                                                }
                                                vinPartOption.setApplicable2(false);
//                                                    System.out.println("dqdqdq" + itemStr);
//                                                    System.out.println(vehicleInfo.getOptionCodes());
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            //todo countries
                            if (!StringUtils.isNullOrEmpty(vinPartOption.getLocalPart())) {

                            }

//                                if (vinPartOption.isApplicable2() && vehicleInfo.getVinTag() != null && (vehicleInfo.getVinTag().equals("1") || vehicleInfo.getVinTag().equalsIgnoreCase("B"))) {
//
//                                    //todo options
//                                    if (vinPartOption.getOptions() != null) {
//                                        if (("1".equals(vehicleInfo.getVinTag()) || "2".equals(vehicleInfo.getVinTag()) || "6".equals(vehicleInfo.getVinTag()) && vehicleInfo.getOptionCodes().length() >= 20)) {
//                                            JSONArray options = JSONUtil.parseArray(vinPartOption.getOptions());
//                                            for (Object item : options) {
//                                                String itemStr = String.valueOf(item);
//                                                if (!itemStr.isEmpty() && vehicleInfo.getOptionCodes().length() > 0 && !vehicleInfo.getOptionCodes().contains(itemStr)) {
//                                                    vinPartOption.setApplicable2(false);
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//
                            if (vinPartOption.getExcludedOptions() != null) {
                                if ("1".equals(vehicleInfo.getVinTag()) || "2".equals(vehicleInfo.getVinTag()) || "6".equals(vehicleInfo.getVinTag())) {
                                    JSONArray excludedOptions = JSONUtil.parseArray(vinPartOption.getExcludedOptions());
                                    for (Object item : excludedOptions) {
                                        String itemStr = String.valueOf(item);
                                        if (!itemStr.isEmpty() && vehicleInfo.getOptionCodes().contains(itemStr)) {
                                            vinPartOption.setApplicable2(false);
                                            break;
                                        }
                                    }
                                }
                            }
//
//                                    String nation = vinPartOption.getNations();
//                                    String vinTag = vehicleInfo.getVinTag();
//
//                                    //todo nations
//                                    if (nation != null && nation.length() > 0 && !nation.isEmpty()) {
//                                        JSONArray nationArr = JSONUtil.parseArray(nation);
//                                        List<String> arr = nationArr.stream().filter(e -> String.valueOf(e).trim().isEmpty()).map(e -> String.valueOf(e)).collect(Collectors.toList());
////                                vinPartOption.setApplicable2(true);
//                                        if ("1".equals(vinTag) || "2".equals(vinTag) || "6".equals(vinTag)) {
//                                            if (!arr.isEmpty() && !vehicleInfo.getNation().isEmpty()) {
//                                                for (String item : arr) {
//                                                    if (!vehicleInfo.getNation().equalsIgnoreCase(item)) {
//                                                        vinPartOption.setApplicable2(false);
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    String excludedNations = vinPartOption.getExcludedNations();
//                                    if (excludedNations != null && excludedNations.length() > 0 && !excludedNations.isEmpty()) {
//                                        JSONArray excludedNationArr = JSONUtil.parseArray(excludedNations);
////                                List<String> arr = nationArr.stream().filter(e -> String.valueOf(e).trim().isEmpty()).map(e -> String.valueOf(e)).collect(Collectors.toList())
////                                vinPartOption.setApplicable2(true);
//                                        if (("1".equals(vinTag) || "2".equals(vinTag) || "6".equals(vinTag)) && nation != null && !nation.isEmpty()) {
//                                            for (Object item : excludedNationArr) {
//                                                if (vehicleInfo.getNation().equalsIgnoreCase(String.valueOf(item))) {
//                                                    vinPartOption.setApplicable2(false);
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                    }
//                                }

                            //todo 根据color过滤,外面写if

                            String partColor = vinPartOption.getPartColor();
                            System.out.println("pppp" + vinPartOption.getApplicable2());
                            System.out.println(partColor);
                            if (vinPartOption.getApplicable2() && !StringUtils.isNullOrEmpty(partColor)) {
                                JSONObject partColorObj = JSONUtil.parseObj(partColor);
                                String splyCode = partColorObj.getStr("splyCode");
                                JSONArray keyColors = partColorObj.getJSONArray("keyColours");
                                System.out.println(1111);
                                if ("UNSPECIFIED".equals(splyCode)) {
                                    String exteriorKeyColorCode = vehicleInfo.getExteriorKeyColorCode();
                                    //todo extColorUp和extColorDown
                                    if (!exteriorKeyColorCode.isEmpty() && !keyColors.isEmpty()) {
                                        List<String> keyColorList = keyColors.stream().map(e -> String.valueOf(e)).collect(Collectors.toList());
                                        if (!keyColorList.contains(exteriorKeyColorCode)) {
                                            vinPartOption.setApplicable2(false);
                                        }
                                    }
                                } else if ("INTERNAL".equals(splyCode)) {
//                                        if ("KMHL54LJXLA000205".equals(vehicleInfo.getVin())) {
//                                            System.out.println();
//                                        }
                                    System.out.println(2222);
                                    String interiorKeyColorCode = vehicleInfo.getInteriorKeyColorCode();
                                    if (!interiorKeyColorCode.isEmpty() && !keyColors.isEmpty()) {
                                        List<String> keyColorList = keyColors.stream().map(e -> String.valueOf(e)).collect(Collectors.toList());
                                        System.out.println(interiorKeyColorCode);
                                        if (!keyColorList.contains(interiorKeyColorCode)) {
                                            System.out.println(3333);
                                            vinPartOption.setApplicable2(false);
                                        }
                                    }
                                } else if ("EXTERNAL".equals(splyCode)) {

                                }
                            }

                        }
                        //todo 添加代码

                        //非ebom、非rkit
                    } else {
                        //
                        otherCount++;
                        for (VinPartOption vinPartOption : vinPartOptions) {
                            String uccCodes = vinPartOption.getUccCodes();
                            JSONArray jsonArr = JSONUtil.parseArray(uccCodes);
                            //todo isModelCodeInterpretationEnabled
                            String vinTag = vehicleInfo.getVinTag();
                            if (vinTag == null || "2".equals(vinTag) || "4".equals(vinTag) || "5".equals(vinTag) || "6".equals(vinTag) || "C".equals(vinTag) || "8".equals(vinTag) || "".equals(vinTag)) {

                                if (vinPartOption.getDriveType().isEmpty() || vinPartOption.getDriveType().equals(((JSONObject) jsonArr2.get(5)).getStr("data"))) {
//                                        vinPartOption.setApplicable2(true);
                                } else {
                                    vinPartOption.setApplicable2(false);
                                }

                                if (vinPartOption.getWeatherType().isEmpty() || vinPartOption.getWeatherType().equals(((JSONObject) jsonArr2.get(6)).getStr("data"))) {
//                                        vinPartOption.setApplicable2(true);
                                } else {
                                    vinPartOption.setApplicable2(false);
                                }


                                //todo uccCodes一样吗 有问题
                                if (vinTag.equals("") || vinTag.equals("1") || vinTag.equals("2") || vinTag.equals("4") || vinTag.equals("6") || vinTag.equals("C")) {
//                                        if (vinPartOption.getPartCode().equalsIgnoreCase("36500-3D920")) {
//                                            System.out.println("66666");
//                                        }
                                    //todo uccCodes 不是uccCode问题
                                    for (int i = 0; i < 5; i++) {
//                                            if (vinPartOption.getPartCode().equalsIgnoreCase("36500-3D920")) {
//                                                System.out.println("yyy");
//                                                System.out.println(String.valueOf(((JSONObject)jsonArr2.get(i)).get("data")));
//                                                System.out.println("zzz");
//                                                System.out.println(String.valueOf(jsonArr.get(i)));
//                                                System.out.println(!String.valueOf(jsonArr2.get(i)).isEmpty() && !String.valueOf(jsonArr.get(i)).isEmpty() && !String.valueOf(((JSONObject)jsonArr2.get(i)).get("data")).equalsIgnoreCase(String.valueOf(jsonArr.get(i))));
//                                            }
                                        if (!String.valueOf(jsonArr2.get(i)).isEmpty() && !String.valueOf(jsonArr.get(i)).isEmpty() && !String.valueOf(((JSONObject) jsonArr2.get(i)).get("data")).equalsIgnoreCase(String.valueOf(jsonArr.get(i)))) {
                                            vinPartOption.setApplicable2(false);
                                            System.out.println("qqq" + vinPartOption.getPartCode() + " " + String.valueOf(((JSONObject) jsonArr2.get(i)).get("data")) + " " + String.valueOf(jsonArr.get(i)));
                                            break;
//                                            if (vinPartOption.getPartCode().equalsIgnoreCase("36500-3D920")) {
//                                                System.out.println("qqq");
//                                                break;
//                                            }
                                        }
                                    }
//                                    if ((!String.valueOf(jsonArr.get(0)).isEmpty() || String.valueOf(jsonArr.get(0)).equalsIgnoreCase(jsonObj1.getStr("data")))
//                                            && (String.valueOf(jsonArr.get(1)).isEmpty() || String.valueOf(jsonArr.get(1)).equalsIgnoreCase(jsonObj2.getStr("data")))
//                                            && (String.valueOf(jsonArr.get(2)).isEmpty() || String.valueOf(jsonArr.get(2)).equalsIgnoreCase(jsonObj3.getStr("data")))
//                                            && (String.valueOf(jsonArr.get(3)).isEmpty() || String.valueOf(jsonArr.get(3)).equalsIgnoreCase(jsonObj4.getStr("data")))
//                                            && (String.valueOf(jsonArr.get(4)).isEmpty() || String.valueOf(jsonArr.get(4)).equalsIgnoreCase(jsonObj5.getStr("data")))) {
////                            && (String.valueOf(jsonArr.get(5)).isEmpty() || String.valueOf(jsonArr.get(5)).equals(jsonObj6.getStr("data")))) {
//                                        vinPartOption.setApplicable2(true);
//                                    } else {
//
//                                        vinPartOption.setApplicable2(false);
//                                    }
                                }
//                                    if ((String.valueOf(jsonArr.get(0)).isEmpty() || String.valueOf(jsonArr.get(0)).equals(jsonObj1.getStr("data")))
//                                            && (String.valueOf(jsonArr.get(1)).isEmpty() || String.valueOf(jsonArr.get(1)).equals(jsonObj2.getStr("data")))
//                                            && (String.valueOf(jsonArr.get(2)).isEmpty() || String.valueOf(jsonArr.get(2)).equals(jsonObj3.getStr("data")))
//                                            && (String.valueOf(jsonArr.get(3)).isEmpty() || String.valueOf(jsonArr.get(3)).equals(jsonObj4.getStr("data")))
//                                            && (String.valueOf(jsonArr.get(4)).isEmpty() || String.valueOf(jsonArr.get(4)).equals(jsonObj5.getStr("data")))) {
////                            && (String.valueOf(jsonArr.get(5)).isEmpty() || String.valueOf(jsonArr.get(5)).equals(jsonObj6.getStr("data")))) {
//                                        vinPartOption.setApplicable2(true);
//                                    } else {
//                                        vinPartOption.setApplicable2(false);
//                                    }

                                //todo color的比较逻辑不一样
                                //todo 获取splycode
                                if (vinPartOption.getPartColor() != null && vinPartOption.getApplicable2() && !vinTag.isEmpty()) {
                                    switch (vinTag) {
                                        case "2":
                                        case "6":
                                            //46
                                            if (vinPartOption.getPartColor() == null) {
                                                vinPartOption.setApplicable2(true);
                                            } else {
                                                String partColor = vinPartOption.getPartColor();
                                                JSONObject partColorObj = JSONUtil.parseObj(partColor);
                                                JSONArray jsonArray = partColorObj.getJSONArray("keyColours");
                                                if (jsonArray.isEmpty()) {
                                                    vinPartOption.setApplicable2(true);
                                                } else {
                                                    String intColor = vehicleInfo.getInteriorKeyColorCode();
                                                    String extColor = vehicleInfo.getExteriorKeyColorCode();

                                                    Iterator it = partColorObj.getJSONArray("keyColours").stream().iterator();
                                                    String keyColor = "";
                                                    do {
                                                        if (!it.hasNext()) {
                                                            vinPartOption.setApplicable2(false);
                                                        }
                                                        //todo AssertionError
                                                        keyColor = (String) it.next();
                                                    } while (!intColor.equals(keyColor) && !extColor.equals(keyColor));
                                                    vinPartOption.setApplicable2(true);
                                                }
                                            }
                                        case "8":
                                            String partColor = vinPartOption.getPartColor();
                                            JSONObject partColorObj = JSONUtil.parseObj(partColor);
                                            String splyCode = String.valueOf(partColorObj.get("splyCode"));
                                            if (splyCode.equals("INTERNAL")) {
                                                String intColor = vehicleInfo.getInteriorKeyColorCode();
                                                JSONArray keyColours = partColorObj.getJSONArray("keyColours");
                                                String color;
                                                if (!keyColours.isEmpty()) {
                                                    Iterator keyColorIt = keyColours.stream().iterator();
                                                    do {
                                                        if (!keyColorIt.hasNext()) {
                                                            vinPartOption.setApplicable2(false);
                                                        }
                                                        color = String.valueOf(keyColorIt.next());
                                                    } while (!intColor.equals(color));
                                                } else {
                                                    if (intColor.isEmpty()) {
                                                        vinPartOption.setApplicable2(true);
                                                    } else {
                                                        vinPartOption.setApplicable2(false);
                                                    }
                                                }
                                            } else {
                                                vinPartOption.setApplicable2(true);
                                            }
                                            //可选项
                                    }
                                }
                            }

                            //todo isOptionCodeInterpretationEnabled
                            if (vinPartOption.getApplicable2() && vinTag != null && ("2".equals(vinTag) || "6".equals(vinTag))) {
                                JSONArray options = JSONUtil.parseArray(vinPartOption.getOptions());
                                if (options != null && options.size() > 0) {
                                    for (Object item : options) {
                                        String itemStr = String.valueOf(item);
                                        if (!itemStr.isEmpty() && vehicleInfo.getOptionCodes().length() > 0 && !vehicleInfo.getOptionCodes().contains(itemStr)) {
                                            vinPartOption.setApplicable2(false);
                                            break;
                                        }
                                    }
                                }
                                JSONArray excludedOptions = JSONUtil.parseArray(vinPartOption.getExcludedOptions());
                                if (excludedOptions != null && excludedOptions.size() > 0) {
                                    for (Object item : excludedOptions) {
                                        String itemStr = String.valueOf(item);
                                        if (!itemStr.isEmpty() && vehicleInfo.getOptionCodes().contains(itemStr)) {
                                            vinPartOption.setApplicable2(false);
                                            break;
                                        }
                                    }
                                }
                                JSONArray nations = JSONUtil.parseArray(vinPartOption.getNations());
                                if (nations != null && nations.size() > 0 && !vehicleInfo.getNation().isEmpty()) {
                                    if ("1".equals(vinTag) || "2".equals(vinTag) || "6".equals(vinTag)) {
                                        if (!nations.isEmpty() && !vehicleInfo.getNation().isEmpty()) {
                                            for (Object item : nations) {
                                                if (!vehicleInfo.getNation().equalsIgnoreCase(String.valueOf(item))) {
                                                    vinPartOption.setApplicable2(false);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                JSONArray excludedNations = JSONUtil.parseArray(vinPartOption.getExcludedNations());
                                if (nations != null && nations.size() > 0 && !vehicleInfo.getNation().isEmpty()) {
                                    JSONArray excludedNationArr = JSONUtil.parseArray(excludedNations);
//                                List<String> arr = nationArr.stream().filter(e -> String.valueOf(e).trim().isEmpty()).map(e -> String.valueOf(e)).collect(Collectors.toList())
//                                vinPartOption.setApplicable2(true);
                                    if (("1".equals(vinTag) || "2".equals(vinTag) || "6".equals(vinTag)) && vehicleInfo.getNation() != null && !vehicleInfo.getNation().isEmpty()) {
                                        for (Object item : excludedNationArr) {
                                            if (vehicleInfo.getNation().equalsIgnoreCase(String.valueOf(item))) {
                                                vinPartOption.setApplicable2(false);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //RKIT
                } else {
                    rkitCount++;
                    for (VinPartOption vinPartOption : vinPartOptions) {
                        String uccCodes = vinPartOption.getUccCodes();
                        JSONArray jsonArr = JSONUtil.parseArray(uccCodes);
                        //todo isModelCodeInterpretationEnabled
                        String vinTag = vehicleInfo.getVinTag();
                        if (vinTag == null || "2".equals(vinTag) || "4".equals(vinTag) || "5".equals(vinTag) || "6".equals(vinTag) || "C".equals(vinTag) || "8".equals(vinTag) || "".equals(vinTag)) {

                            if (vinPartOption.getDriveType().isEmpty() || vinPartOption.getDriveType().equals(((JSONObject) jsonArr2.get(5)).getStr("data"))) {
                                vinPartOption.setApplicable2(true);
                            } else {
                                vinPartOption.setApplicable2(false);
                            }

                            if (vinPartOption.getWeatherType().isEmpty() || vinPartOption.getWeatherType().equals(((JSONObject) jsonArr2.get(6)).getStr("data"))) {
                                vinPartOption.setApplicable2(true);
                            } else {
                                vinPartOption.setApplicable2(false);
                            }

                            if (!(vinTag.equals("") || vinTag.equals("1") || vinTag.equals("2") || vinTag.equals("6") || vinTag.equals("4") || vinTag.equals("C"))) {

                                //写一个循环
                                for (int i = 0; i < 10; i++) {
                                    if (!String.valueOf(jsonArr2.get(i)).isEmpty() && !String.valueOf(jsonArr.get(i)).isEmpty() && !String.valueOf(jsonArr2.get(i)).equalsIgnoreCase(String.valueOf(jsonArr.get(i)))) {
                                        vinPartOption.setApplicable2(false);
                                        break;
                                    }
                                }
                            }

                            //结果更少
                            //todo uccCodes一样吗
//                                if ((String.valueOf(jsonArr.get(0)).isEmpty() || String.valueOf(jsonArr.get(0)).equals(jsonObj1.getStr("data")))
//                                        && (String.valueOf(jsonArr.get(1)).isEmpty() || String.valueOf(jsonArr.get(1)).equals(jsonObj2.getStr("data")))
//                                        && (String.valueOf(jsonArr.get(2)).isEmpty() || String.valueOf(jsonArr.get(2)).equals(jsonObj3.getStr("data")))
//                                        && (String.valueOf(jsonArr.get(3)).isEmpty() || String.valueOf(jsonArr.get(3)).equals(jsonObj4.getStr("data")))
//                                        && (String.valueOf(jsonArr.get(4)).isEmpty() || String.valueOf(jsonArr.get(4)).equals(jsonObj5.getStr("data")))) {
////                            && (String.valueOf(jsonArr.get(5)).isEmpty() || String.valueOf(jsonArr.get(5)).equals(jsonObj6.getStr("data")))) {
//                                    vinPartOption.setApplicable2(true);
//                                } else {
//                                    vinPartOption.setApplicable2(false);
//                                }

//                                if (vinPartOption.getPartColor() == null || vinPartOption.getPartColor().equals(vehicleInfo.getInteriorKeyColorCode())) {
//                                    vinPartOption.setApplicable2(true);
//                                } else {
//                                    vinPartOption.setApplicable2(false);
//                                }
                            //todo color的比较逻辑不一样
                            //todo 获取splycode
//                                if (vinPartOption.getPartColor() != null && vinPartOption.getApplicable2() && !vinTag.isEmpty()) {
//                                    switch (vinTag) {
//                                        case "2":
//                                        case "6":
//                                            //467行
//                                            if (vinPartOption.getPartColor() == null) {
//                                                vinPartOption.setApplicable2(true);
//                                            } else {
//                                                String intColor = vehicleInfo.getInteriorKeyColorCode();
//                                                String extColor = vehicleInfo.getExteriorKeyColorCode();
//
//                                                String partColor = vinPartOption.getPartColor();
//                                                JSONObject partColorObj = JSONUtil.parseObj(partColor);
//                                                Iterator it = partColorObj.getJSONArray("keyColours").stream().iterator();
//                                                String keyColor;
//                                                do {
//                                                    if (!it.hasNext()) {
//                                                        vinPartOption.setApplicable2(false);
//                                                    }
//                                                    //todo AssertionError
//                                                    keyColor = (String) it.next();
//                                                } while (!intColor.equals(keyColor) && !extColor.equals(keyColor));
//                                                vinPartOption.setApplicable2(true);
//                                            }
//                                        case "8":
//                                            String partColor = vinPartOption.getPartColor();
//                                            JSONObject partColorObj = JSONUtil.parseObj(partColor);
//                                            String splyCode = String.valueOf(partColorObj.get("splyCode"));
//                                            if (splyCode.equals("INTERNAL")) {
//                                                String intColor = vehicleInfo.getInteriorKeyColorCode();
//                                                JSONArray keyColours = partColorObj.getJSONArray("keyColours");
//                                                String color;
//                                                if (!keyColours.isEmpty()) {
//                                                    Iterator keyColorIt = keyColours.stream().iterator();
//                                                    do {
//                                                        if (!keyColorIt.hasNext()) {
//                                                            vinPartOption.setApplicable2(false);
//                                                        }
//                                                        color = String.valueOf(keyColorIt.next());
//                                                    } while (intColor.equals(color));
//                                                } else {
//                                                    if (intColor.isEmpty()) {
//                                                        vinPartOption.setApplicable2(true);
//                                                    } else {
//                                                        vinPartOption.setApplicable2(false);
//                                                    }
//                                                }
//                                            } else {
//                                                vinPartOption.setApplicable2(true);
//                                            }
//                                            //492行
//                                    }
//                                }
                        }
                    }

                    //todo 还有optionCombineColor过滤条件

                    //todo vinTag
                }
                vinPartOptions = vinPartOptions.stream().filter(e -> e.getApplicable2()).collect(Collectors.toList());


//                }
                //todo 和实际返回的结果对比
                //1.个数是否相等
                //2.字段是否相同
//                JSONObject jsonObj3 = new JSONObject();
                JSONObject result = JSONUtil.parseObj(line);
                JSONArray catalogsArr = result.getJSONArray("catalogs");
                JSONObject catalogOne = (JSONObject) catalogsArr.get(0);
                int totalCount = catalogOne.getInt("total");
//                int totalCount = result.get("catalogs").getInt("total");
                //为什么多过滤了
                System.out.println("aaa" + vinPartOptions.size());
                System.out.println("bbb" + totalCount);
                System.out.println("ccc" + catalogOne.getInt("totalapplicable"));
                System.out.println("ddd" + catalogOne.getInt("totalnonapplicable"));
                if (vinPartOptions.size() == totalCount) {
                    sameCount++;
                } else {
                    unMatchCount++;
                    System.out.println("unmatch data" + catalogCode + " " + sectionCode + " " + interpretationData + " " + catalogDataTypeDescription);
                    System.out.println("unmatch2" + catalogOne.toJSONString(2));
                    for (VinPartOption item : vinPartOptions) {
                        System.out.println(item.toString());
                    }
                }
            } else {
                int firstBlankIndex = line.indexOf(" ");
//                System.out.println("FFFFF" + firstBlankIndex);
                int secondBlankIndex = line.indexOf(" ", firstBlankIndex + 1);
//                System.out.println("SSSSS" + secondBlankIndex);
                catalogCode = line.substring(0, firstBlankIndex);
                sectionCode = line.substring(firstBlankIndex + 1, secondBlankIndex);
                interpretationData = line.substring(secondBlankIndex + 1, line.length());
                System.out.println("kkkkkk" + catalogCode + " " + sectionCode + " " + interpretationData);
                JSONObject jsonObject = XML.toJSONObject(interpretationData);
                JSONObject interpretation = jsonObject.getJSONObject("interpretation");
                vinCode = interpretation.getStr("vin");

            }
        }
        System.out.println("samecount" + sameCount);
        System.out.println("unmatchCount" + unMatchCount);
        System.out.println("ebomCount" + ebomCount);
        System.out.println("rkitCount" + rkitCount);
        System.out.println("otherCount" + otherCount);

//陈旧、封建、固执
//开放、包容、闲->变卷
//        List<String> catalogCodes = vinPartOptions.stream().map(VinPartOption::getCatalogCode).collect(Collectors.toList());
//        List<Catalog> catalogList = catalogMapper.selectbycatalogcode();

//        String catalogDataType = ""

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

//    public static void main(String[] args) throws DocumentException {
//        //        JSONObject jb = XML.toJSONObject(interpretationData);
////        System.out.println(jb.toStringPretty());
////        String param = "KHMAPTMF12 8181911 <interpretation catalog=\"KHMAPTMF12\" type=\"EBOM\" level=\"2\"><avs data=\"W5\" type=\"01\"/></interpretation> false true ";
//        String interpretationData = "<interpretation catalog=\"KHMAPTMF12\" type=\"EBOM\" level=\"2\"><avs data=\"W5\" type=\"01\"/></interpretation>";
//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//        for (Element element : elements) {
//            System.out.println(element.attribute("data").getData());
//        }
////        System.out.println(rootElement.asXML());
////        int lastBlankIndex = param.lastIndexOf(" ");
////
////        int lastSecondBlankIndex = param.lastIndexOf(" ", lastBlankIndex - 1);
//
////        System.out.println(catalog);
////        System.out.println(section);
////        System.out.println(interpretationData);
//    }

    //    VinDataService
    @PostMapping("/filter-vin-data")
    public void filter() {
        vinDataService.filterVinData();
    }

    @PostMapping("/store-section-parts-to-sqlserver")
    public void insertGetSectionPartsToSqlServer() throws IOException, DocumentException {

//        File directory = new File("E:\\two\\getSectionPartsAHMAPS218.txt");
//        File[] files = directory.listFiles();
        List<SectionPart> sectionParts = new ArrayList<>();
        List<ModelPartOption> modelPartOptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        String param = "";
        String parts = "";

//        for (File file : files) {
        File file = new File("E:\\hpdepc\\getSectionPartsHMA260PA00.txt");
        LineIterator fileContents = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name());
//            System.out.println(file.getName() + " start");
        while (fileContents.hasNext()) {
            String line = fileContents.nextLine();
            if (!line.startsWith("{")) {
//                System.out.println("llll");
                param = line;
            } else {
//                System.out.println("oooo");
                parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);
                int firstBlankIndex = param.indexOf(" ");

                int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                String catalog = param.substring(0, firstBlankIndex);

                String section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
                Document text = DocumentHelper.parseText(interpretationData);
                Element rootElement = text.getRootElement();
                List<Element> avsElements = rootElement.elements("avs");

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                JsonNode partNode = catalogNode.get(0).get("parts");

                if (partNode.isArray()) {
                    Iterator<JsonNode> elements = partNode.iterator();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
                        SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                        //将sectionParts转为sectionPart,
                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                        sectionPart.setCatalogCode(catalog);
                        sectionPart.setSectionCode(section);
                        sectionParts.add(sectionPart);

                        ModelPartOption modelPartOption = new ModelPartOption();
                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                        modelPartOption.setCatalogCode(catalog);
                        modelPartOption.setSectionCode(section);

                        //根据type设置
                        for (Element avsElement : avsElements) {
                            if (avsElement.attributeValue("type").equals("01")) {
                                modelPartOption.setType01(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("02")) {
                                modelPartOption.setType02(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("03")) {
                                modelPartOption.setType03(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("04")) {
                                modelPartOption.setType04(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("05")) {
                                modelPartOption.setType05(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("06")) {
                                modelPartOption.setType06(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("WT")) {
                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("DT")) {
                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                            }
                        }
//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                        modelPartOptions.add(modelPartOption);

//                            try {
                        // elements.hasNext()
                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
                            sectionPartsMapper.batchInsert(sectionParts);
//                                    sectionPartsTempSqlserverMapper.batchInsert(sectionParts);
                            sectionParts = new ArrayList<>();
                        }

                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
                            modelPartOptionMapper.batchInsert(modelPartOptions);
                            modelPartOptions = new ArrayList<>();
                        }
//                            } catch (Exception e) {
//
//                            }
                    }
                }
            }
        }
        System.out.println(file.getName() + " end");
//        }
    }

//    @PostMapping("/store-section-parts")
//    public void insertGetSectionParts() throws IOException, DocumentException {
////        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
//        //保存sectionParts到数据库
//
//        String path = "E:\\two\\getSectionPartsKHMAPTG05.txt";
////        File file = new File(path);
////        System.out.println("kkl");
//        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
//        String param = "";
//        String parts = "";
//
//        List<SectionPart> sectionParts = new ArrayList<>();
//        List<ModelPartOption> modelPartOptions = new ArrayList<>();
//        ObjectMapper mapper = new ObjectMapper();
//
////        String catalogCode = ;
////        BufferedReader buf = new BufferedReader(new java.io.FileReader(path));
////        String line;
//        //先插一条看看
//        while (fileContents.hasNext()) {
//            String line = fileContents.nextLine();
//            if (!line.startsWith("{")) {
////                System.out.println("llll");
//                param = line;
//            } else {
////                System.out.println("oooo");
//                parts = line;
////                JSONObject jsonObject = JSONUtil.parseObj(parts);
////                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
//                JsonNode node = mapper.readTree(parts);
////                System.out.println(node.toPrettyString());
//
//                JsonNode catalogNode = node.get("catalogs");
//
////                System.out.println("pppp" + param);
//                int firstBlankIndex = param.indexOf(" ");
//
//                int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);
//
//                String catalog = param.substring(0, firstBlankIndex);
//
//                String section = param.substring(firstBlankIndex + 1, secondBlankIndex);
//
//                String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);
//
////                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
//                Document text = DocumentHelper.parseText(interpretationData);
//                Element rootElement = text.getRootElement();
//                List<Element> avsElements = rootElement.elements("avs");
//
////        Document text = DocumentHelper.parseText(interpretationData);
////        Element rootElement = text.getRootElement();
////        List<Element> elements = rootElement.elements("avs");
////                for (Element element : avsElements) {
////                    System.out.println(element.asXML());
////                }
//
//                JsonNode partNode = catalogNode.get(0).get("parts");
//
//                if (partNode.isArray()) {
//                    Iterator<JsonNode> elements = partNode.iterator();
//                    while (elements.hasNext()) {
//                        JsonNode element = elements.next();
//                        SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
////                        System.out.println("sssss:" + sectionPart.toString());
//                        //将sectionParts转为sectionPart,
//                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
//                        sectionPart.setCatalogCode(catalog);
//                        sectionPart.setSectionCode(section);
//                        sectionParts.add(sectionPart);
//
//                        ModelPartOption modelPartOption = new ModelPartOption();
//                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
//                        modelPartOption.setCatalogCode(catalog);
//                        modelPartOption.setSectionCode(section);
//
//                        //根据type设置
//                        for (Element avsElement : avsElements) {
//                            if (avsElement.attributeValue("type").equals("01")) {
//                                modelPartOption.setType01(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("02")) {
//                                modelPartOption.setType02(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("03")) {
//                                modelPartOption.setType03(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("04")) {
//                                modelPartOption.setType04(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("05")) {
//                                modelPartOption.setType05(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("06")) {
//                                modelPartOption.setType06(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("WT")) {
//                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("DT")) {
//                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
//                            }
//                        }
////                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
////                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
////                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
////                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
////                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
////                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
////                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
//                        modelPartOptions.add(modelPartOption);
//
//                        // elements.hasNext()
//                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
//                            sectionPartsMapper.batchInsert(sectionParts);
//                            sectionParts = new ArrayList<>();
//                        }
//
//                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert(modelPartOptions);
//                            modelPartOptions = new ArrayList<>();
//                        }
//                    }
//                }
//            }
//        }
//        System.out.println("Finished");
////        int result = sectionPartsMapper.batchInsert(sectionParts);
//    }

    //nas2809100.txt
//    @PostMapping("/store-section-parts-second")
//    public void insertGetSectionPartsSecond() throws IOException, DocumentException {
////        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
//        //保存sectionParts到数据库
//        String path = "E:\\hpdepc\\AHMAPCMA06\\新建文件夹";
////        System.out.println("kkl");
////       LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
//
//        //String str1 = "KHMAPDV19 ZA000058ZZ <interpretation catalog=\"KHMAPDV19\" type=\"EBOM\" level=\"2\"><avs data=\"D5\" type=\"02\"/><avs data=\"A\" type=\"03\"/><avs data=\"L\" type=\"DT\"/><avs data=\"3\" type=\"WT\"/></interpretation> false true";
//        //String str2 = "{\"data\":\"ZA000058ZZ\",\"hasJPIOSection\":false,\"catalogs\":[{\"data\":\"KHMAPDV19\",\"label\":\"SONATA 19 (2021-)\",\"total\":0,\"totalapplicable\":0,\"totalnonapplicable\":0,\"parts\":[]}],\"illustrations\":[{\"data\":\"ZA000058ZZ\",\"folder\":\"HYW/cat/KHMAPDV19\",\"name\":\"AHMAPDW20ECMirror.png\",\"rects\":[],\"imagePath\":\"HYW/cat/KHMAPDV19/AHMAPDW20ECMirror.png\",\"origin\":0}],\"linkedSectionCodes\":[],\"linkedAjSectionCodes\":[],\"fitmentInstructions\":[],\"tyreCertificates\":[]}";
//
//        //把part3重新导一遍
//
//        //执行到
//        //AHMAPCMA06 8888111 <interpretation catalog="AHMAPCMA06" type="EBOM" level="2"><avs data="W7" type="01"/><avs data="6" type="03"/><avs data="1" type="04"/><avs data="D" type="05"/><avs data="L" type="DT"/><avs data="2" type="WT"/></interpretation> false true
////        看看最后一行
//
//        File directory = new File(path);
//        File[] files = directory.listFiles();
//
//        for (File file : files) {
//            System.out.println("ffff" + file.getName());
//            LineIterator fileContents = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name());
//
//            String param = "";
//            String parts = "";
//
//            List<SectionPart> sectionParts = new ArrayList<>();
//            List<ModelPartOption> modelPartOptions = new ArrayList<>();
//            ObjectMapper mapper = new ObjectMapper();
//
////        String catalogCode = ;
//
//            //elasticsearch
//            //key-value形式
//            //根据聚合
//
//            //先插一条看看
//            while (fileContents.hasNext()) {
//                String line = fileContents.nextLine();
//                if (!line.startsWith("{")) {
////                System.out.println("llll");
//                    param = line;
//                } else {
//                    try {
////                System.out.println("oooo");
//                        parts = line;
////                JSONObject jsonObject = JSONUtil.parseObj(parts);
////                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
//                        JsonNode node = mapper.readTree(parts);
////                System.out.println(node.toPrettyString());
//
//                        JsonNode catalogNode = node.get("catalogs");
//
////                System.out.println("pppp" + param);
//                        int firstBlankIndex = param.indexOf(" ");
//
//                        int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);
//
//                        String catalog = param.substring(0, firstBlankIndex);
//
//                        String section = param.substring(firstBlankIndex + 1, secondBlankIndex);
//
//                        String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);
//
////                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
//                        Document text = DocumentHelper.parseText(interpretationData);
//                        Element rootElement = text.getRootElement();
//                        List<Element> avsElements = rootElement.elements("avs");
//
////        Document text = DocumentHelper.parseText(interpretationData);
////        Element rootElement = text.getRootElement();
////        List<Element> elements = rootElement.elements("avs");
////                for (Element element : avsElements) {
////                    System.out.println(element.asXML());
////                }
//
//                        JsonNode partNode = catalogNode.get(0).get("parts");
//
//                        if (partNode.isArray()) {
//                            Iterator<JsonNode> elements = partNode.iterator();
//                            while (elements.hasNext()) {
//                                JsonNode element = elements.next();
//                                SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
////                        System.out.println("sssss:" + sectionPart.toString());
//                                //将sectionParts转为sectionPart,
//                                SectionPart sectionPart = SectionPart.convert(sectionPartDto);
//                                sectionPart.setCatalogCode(catalog);
//                                sectionPart.setSectionCode(section);
//                                sectionParts.add(sectionPart);
//
//                                ModelPartOption modelPartOption = new ModelPartOption();
//                                modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
//                                modelPartOption.setCatalogCode(catalog);
//                                modelPartOption.setSectionCode(section);
//
//                                //根据type设置
//                                for (Element avsElement : avsElements) {
//                                    if (avsElement.attributeValue("type").equals("01")) {
//                                        modelPartOption.setType01(avsElement.attributeValue("data"));
//                                    } else if (avsElement.attributeValue("type").equals("02")) {
//                                        modelPartOption.setType02(avsElement.attributeValue("data"));
//                                    } else if (avsElement.attributeValue("type").equals("03")) {
//                                        modelPartOption.setType03(avsElement.attributeValue("data"));
//                                    } else if (avsElement.attributeValue("type").equals("04")) {
//                                        modelPartOption.setType04(avsElement.attributeValue("data"));
//                                    } else if (avsElement.attributeValue("type").equals("05")) {
//                                        modelPartOption.setType05(avsElement.attributeValue("data"));
//                                    } else if (avsElement.attributeValue("type").equals("06")) {
//                                        modelPartOption.setType06(avsElement.attributeValue("data"));
//                                    } else if (avsElement.attributeValue("type").equals("WT")) {
//                                        modelPartOption.setTypeWT(avsElement.attributeValue("data"));
//                                    } else if (avsElement.attributeValue("type").equals("DT")) {
//                                        modelPartOption.setTypeDT(avsElement.attributeValue("data"));
//                                    }
//                                }
////                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
////                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
////                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
////                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
////                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
////                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
////                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
//                                modelPartOptions.add(modelPartOption);
//
////                        if (sectionParts.size() == 1000 || !elements.hasNext()) {
////                            sectionPartsMapper.batchInsert2(sectionParts);
////                            sectionParts = new ArrayList<>();
////                        }
////
////                        if (modelPartOptions.size() == 1000 || !elements.hasNext()) {
////                            modelPartOptionMapper.batchInsert2(modelPartOptions);
////                            modelPartOptions = new ArrayList<>();
////                        }
//                                if (sectionParts.size() == 2000 || !elements.hasNext()) {
//                                    sectionPartsMapper.batchInsertPartByModelNew(sectionParts);
//                                    sectionParts = new ArrayList<>();
//                                }
//
//                                if (modelPartOptions.size() == 2000 || !elements.hasNext()) {
//                                    modelPartOptionNewTemp1Mapper.batchInsertModelPartOptionNewestTemp2(modelPartOptions);
//                                    modelPartOptions = new ArrayList<>();
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println(param);
//                        return;
//                    }
//                }
//            }
//            System.out.println("Finished" + file.getName());
//        }
//        System.out.println("Finished2");
////        int result = sectionPartsMapper.batchInsert(sectionParts);
//    }

    @PostMapping("/store-section-parts-third")
    public void insertGetSectionPartsThird() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
        String path = "E:\\hpdepc\\getSectionPartsNAS2409200(2).txt";
//        System.out.println("kkl");
        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
        String param = "";
        String parts = "";

        List<SectionPart> sectionParts = new ArrayList<>();
        List<ModelPartOption> modelPartOptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

//        String catalogCode = ;
        //先插一条看看
        while (fileContents.hasNext()) {
            String line = fileContents.nextLine();
            if (!line.startsWith("{")) {
//                System.out.println("llll");
                param = line;
            } else {
                try {
//                System.out.println("oooo");
                    parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                    JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                    JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);
                    int firstBlankIndex = param.indexOf(" ");

                    int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                    String catalog = param.substring(0, firstBlankIndex);

                    String section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                    String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
                    Document text = DocumentHelper.parseText(interpretationData);
                    Element rootElement = text.getRootElement();
                    List<Element> avsElements = rootElement.elements("avs");

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                    JsonNode partNode = catalogNode.get(0).get("parts");

                    if (partNode.isArray()) {
                        Iterator<JsonNode> elements = partNode.iterator();
                        while (elements.hasNext()) {
                            JsonNode element = elements.next();
                            SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                            //将sectionParts转为sectionPart,
                            SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                            sectionPart.setCatalogCode(catalog);
                            sectionPart.setSectionCode(section);
                            sectionParts.add(sectionPart);

                            ModelPartOption modelPartOption = new ModelPartOption();
                            modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                            modelPartOption.setCatalogCode(catalog);
                            modelPartOption.setSectionCode(section);

                            //根据type设置
                            for (Element avsElement : avsElements) {
                                if (avsElement.attributeValue("type").equals("01")) {
                                    modelPartOption.setType01(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("02")) {
                                    modelPartOption.setType02(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("03")) {
                                    modelPartOption.setType03(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("04")) {
                                    modelPartOption.setType04(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("05")) {
                                    modelPartOption.setType05(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("06")) {
                                    modelPartOption.setType06(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("WT")) {
                                    modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("DT")) {
                                    modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                                }
                            }
                            //todo

//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                            modelPartOptions.add(modelPartOption);

//                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
//                            sectionPartsMapper.batchInsert3(sectionParts);
//                            sectionParts = new ArrayList<>();
//                        }
//
//                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert3(modelPartOptions);
//                            modelPartOptions = new ArrayList<>();
//                        }
                            if (sectionParts.size() == 5000 || !elements.hasNext()) {
                                sectionPartsMapper.batchInsert3(sectionParts);
                                sectionParts = new ArrayList<>();
                            }

                            if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
                                modelPartOptionMapper.batchInsert3(modelPartOptions);
                                modelPartOptions = new ArrayList<>();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(param);
                    return;
                }
            }
        }
        System.out.println("Finished3");
//        int result = sectionPartsMapper.batchInsert(sectionParts);
    }

    @PostMapping("/store-section-parts-Fourth")
    public void insertGetSectionPartsFourth() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
        String path = "E:\\fault\\getSectionPartsKHMAPDV19.txt";
//        System.out.println("kkl");
        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
        String param = "";
        String parts = "";

        List<SectionPart> sectionParts = new ArrayList<>();
        List<ModelPartOption> modelPartOptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

//        String catalogCode = ;

        //先插一条看看
        while (fileContents.hasNext()) {
            String line = fileContents.nextLine();
            if (!line.startsWith("{")) {
//                System.out.println("llll");
                param = line;
            } else {
                try {
//                System.out.println("oooo");
                    parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                    JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                    JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);
                    int firstBlankIndex = param.indexOf(" ");

                    int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                    String catalog = param.substring(0, firstBlankIndex);

                    String section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                    String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
                    Document text = DocumentHelper.parseText(interpretationData);
                    Element rootElement = text.getRootElement();
                    List<Element> avsElements = rootElement.elements("avs");

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                    JsonNode partNode = catalogNode.get(0).get("parts");

                    if (partNode.isArray()) {
                        Iterator<JsonNode> elements = partNode.iterator();
                        while (elements.hasNext()) {
                            JsonNode element = elements.next();
                            SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                            //将sectionParts转为sectionPart,
                            SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                            sectionPart.setCatalogCode(catalog);
                            sectionPart.setSectionCode(section);
                            sectionParts.add(sectionPart);

                            ModelPartOption modelPartOption = new ModelPartOption();
                            modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                            modelPartOption.setCatalogCode(catalog);
                            modelPartOption.setSectionCode(section);

                            //根据type设置
                            for (Element avsElement : avsElements) {
                                if (avsElement.attributeValue("type").equals("01")) {
                                    modelPartOption.setType01(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("02")) {
                                    modelPartOption.setType02(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("03")) {
                                    modelPartOption.setType03(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("04")) {
                                    modelPartOption.setType04(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("05")) {
                                    modelPartOption.setType05(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("06")) {
                                    modelPartOption.setType06(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("WT")) {
                                    modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("DT")) {
                                    modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                                }
                            }
//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                            modelPartOptions.add(modelPartOption);

//                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
//                            sectionPartsMapper.batchInsert4(sectionParts);
//                            sectionParts = new ArrayList<>();
//                        }
//
//                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert4(modelPartOptions);
//                            modelPartOptions = new ArrayList<>();
//                        }
                            if (sectionParts.size() == 2000 || !elements.hasNext()) {
                                sectionPartsMapper.batchInsert4(sectionParts);
                                sectionParts = new ArrayList<>();
                            }

                            if (modelPartOptions.size() == 2000 || !elements.hasNext()) {
                                modelPartOptionMapper.batchInsert4(modelPartOptions);
                                modelPartOptions = new ArrayList<>();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(param);
                    return;
                }
            }
        }
        System.out.println("Finished4");
//        int result = sectionPartsMapper.batchInsert(sectionParts);
    }

    //处理by-vin数据
    //是怎么处理，怎么验证的
//    @PostMapping("/handle-by-vin-data")
//    public void handleByVinData() {
//        //by-vin，怎么处理的
//        //partnumber
//        for () {
//
//        }
//    }

    @PostMapping("/store-section-parts-fifth")
    public void insertGetSectionPartsFifth() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
        String path = "E:\\hpdepc\\getSectionPartsKHMAPDV19-new.txt";
//        System.out.println("kkl");
        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
        String param = "";
        String parts = "";

        List<SectionPart> sectionParts = new ArrayList<>();
        List<ModelPartOption> modelPartOptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

//        String catalogCode = ;

        //先插一条看看
        while (fileContents.hasNext()) {
            String line = fileContents.nextLine();
            if (!line.startsWith("{")) {
//                System.out.println("llll");
                param = line;
            } else {
                try {
//                System.out.println("oooo");
                    parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                    JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                    JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);
                    int firstBlankIndex = param.indexOf(" ");

                    int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                    String catalog = param.substring(0, firstBlankIndex);

                    String section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                    String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
                    Document text = DocumentHelper.parseText(interpretationData);
                    Element rootElement = text.getRootElement();
                    List<Element> avsElements = rootElement.elements("avs");

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                    JsonNode partNode = catalogNode.get(0).get("parts");

                    if (partNode.isArray()) {
                        Iterator<JsonNode> elements = partNode.iterator();
                        while (elements.hasNext()) {
                            JsonNode element = elements.next();
                            SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                            //将sectionParts转为sectionPart,
                            SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                            sectionPart.setCatalogCode(catalog);
                            sectionPart.setSectionCode(section);
                            sectionParts.add(sectionPart);

                            ModelPartOption modelPartOption = new ModelPartOption();
                            modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                            modelPartOption.setCatalogCode(catalog);
                            modelPartOption.setSectionCode(section);

                            //根据type设置
                            for (Element avsElement : avsElements) {
                                if (avsElement.attributeValue("type").equals("01")) {
                                    modelPartOption.setType01(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("02")) {
                                    modelPartOption.setType02(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("03")) {
                                    modelPartOption.setType03(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("04")) {
                                    modelPartOption.setType04(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("05")) {
                                    modelPartOption.setType05(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("06")) {
                                    modelPartOption.setType06(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("WT")) {
                                    modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                                } else if (avsElement.attributeValue("type").equals("DT")) {
                                    modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                                }
                            }
//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                            modelPartOptions.add(modelPartOption);

//                        if (sectionParts.size() == 1000 || !elements.hasNext()) {
////                            sectionPartsMapper.batchInsert5(sectionParts);
//                            sectionParts = new ArrayList<>();
//                        }
//
//                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert5(modelPartOptions);
//                            modelPartOptions = new ArrayList<>();
//                        }
                            if (sectionParts.size() == 2000 || !elements.hasNext()) {
                                sectionPartsMapper.batchInsert5(sectionParts);
                                sectionParts = new ArrayList<>();
                            }

                            if (modelPartOptions.size() == 2000 || !elements.hasNext()) {
                                modelPartOptionMapper.batchInsert5(modelPartOptions);
                                modelPartOptions = new ArrayList<>();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(param);
                    return;
                }
            }
        }
        System.out.println("Finished5");
//        int result = sectionPartsMapper.batchInsert(sectionParts);
    }

//    @PostMapping("/store-section-parts-Sixth")
//    public void insertGetSectionPartsSixth() throws IOException, DocumentException {
//
////        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
//        //保存sectionParts到数据库
//        String path = "E:\\hpdepc\\getSectionPartsAHMAPS221-Part2.txt";
////        System.out.println("kkl");
//        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
//        String param = "";
//        String parts = "";
//
//        List<SectionPart> sectionParts = new ArrayList<>();
//        List<ModelPartOption> modelPartOptions = new ArrayList<>();
//        ObjectMapper mapper = new ObjectMapper();
//
////        String catalogCode = ;
//
//        //先插一条看看
//        while (fileContents.hasNext()) {
//            String line = fileContents.nextLine();
//            if (!line.startsWith("{")) {
////                System.out.println("llll");
//                param = line;
//            } else {
////                System.out.println("oooo");
//                parts = line;
////                JSONObject jsonObject = JSONUtil.parseObj(parts);
////                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
//                JsonNode node = mapper.readTree(parts);
////                System.out.println(node.toPrettyString());
//
//                JsonNode catalogNode = node.get("catalogs");
//
////                System.out.println("pppp" + param);
//                int firstBlankIndex = param.indexOf(" ");
//
//                int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);
//
//                String catalog = param.substring(0, firstBlankIndex);
//
//                String section = param.substring(firstBlankIndex + 1, secondBlankIndex);
//
//                String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);
//
////                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
//                Document text = DocumentHelper.parseText(interpretationData);
//                Element rootElement = text.getRootElement();
//                List<Element> avsElements = rootElement.elements("avs");
//
////        Document text = DocumentHelper.parseText(interpretationData);
////        Element rootElement = text.getRootElement();
////        List<Element> elements = rootElement.elements("avs");
////                for (Element element : avsElements) {
////                    System.out.println(element.asXML());
////                }
//
//                JsonNode partNode = catalogNode.get(0).get("parts");
//
//                if (partNode.isArray()) {
//                    Iterator<JsonNode> elements = partNode.iterator();
//                    while (elements.hasNext()) {
//                        JsonNode element = elements.next();
//                        SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
////                        System.out.println("sssss:" + sectionPart.toString());
//                        //将sectionParts转为sectionPart,
//                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
//                        sectionPart.setCatalogCode(catalog);
//                        sectionPart.setSectionCode(section);
//                        sectionParts.add(sectionPart);
//
//                        ModelPartOption modelPartOption = new ModelPartOption();
//                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
//                        modelPartOption.setCatalogCode(catalog);
//                        modelPartOption.setSectionCode(section);
//
//                        //根据type设置
//                        for (Element avsElement : avsElements) {
//                            if (avsElement.attributeValue("type").equals("01")) {
//                                modelPartOption.setType01(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("02")) {
//                                modelPartOption.setType02(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("03")) {
//                                modelPartOption.setType03(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("04")) {
//                                modelPartOption.setType04(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("05")) {
//                                modelPartOption.setType05(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("06")) {
//                                modelPartOption.setType06(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("WT")) {
//                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("DT")) {
//                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
//                            }
//                        }
////                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
////                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
////                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
////                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
////                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
////                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
////                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
//                        modelPartOptions.add(modelPartOption);
//
////                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
////                            sectionPartsMapper.batchInsert6(sectionParts);
////                            sectionParts = new ArrayList<>();
////                        }
////
////                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
////                            modelPartOptionMapper.batchInsert6(modelPartOptions);
////                            modelPartOptions = new ArrayList<>();
////                        }
//                        if (sectionParts.size() == 2000 || !elements.hasNext()) {
//                            sectionPartsMapper.batchInsert6(sectionParts);
//                            sectionParts = new ArrayList<>();
//                        }
//
//                        if (modelPartOptions.size() == 2000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert6(modelPartOptions);
//                            modelPartOptions = new ArrayList<>();
//                        }
//                    }
//                }
//            }
//        }
//        System.out.println("Finished6");
////        int result = sectionPartsMapper.batchInsert(sectionParts);
//    }

//    @PostMapping("/store-section-parts7")
//    public void insertGetSectionParts7() throws IOException, DocumentException {
////        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
//        //保存sectionParts到数据库
//        String path = "E:\\two\\getSectionPartsNAS2409200.txt";
////        System.out.println("kkl");
//        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
//        String param = "";
//        String parts = "";
//
//        List<SectionPart> sectionParts = new ArrayList<>();
//        List<ModelPartOption> modelPartOptions = new ArrayList<>();
//        ObjectMapper mapper = new ObjectMapper();
//
////        String catalogCode = ;
//
//        //先插一条看看
//        while (fileContents.hasNext()) {
//            String line = fileContents.nextLine();
//            if (!line.startsWith("{")) {
////                System.out.println("llll");
//                param = line;
//            } else {
////                System.out.println("oooo");
//                parts = line;
////                JSONObject jsonObject = JSONUtil.parseObj(parts);
////                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
//                JsonNode node = mapper.readTree(parts);
////                System.out.println(node.toPrettyString());
//
//                JsonNode catalogNode = node.get("catalogs");
//
////                System.out.println("pppp" + param);
//                int firstBlankIndex = param.indexOf(" ");
//
//                int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);
//
//                String catalog = param.substring(0, firstBlankIndex);
//
//                String section = param.substring(firstBlankIndex + 1, secondBlankIndex);
//
//                String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);
//
////                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
//                Document text = DocumentHelper.parseText(interpretationData);
//                Element rootElement = text.getRootElement();
//                List<Element> avsElements = rootElement.elements("avs");
//
////        Document text = DocumentHelper.parseText(interpretationData);
////        Element rootElement = text.getRootElement();
////        List<Element> elements = rootElement.elements("avs");
////                for (Element element : avsElements) {
////                    System.out.println(element.asXML());
////                }
//
//                JsonNode partNode = catalogNode.get(0).get("parts");
//
//                if (partNode.isArray()) {
//                    Iterator<JsonNode> elements = partNode.iterator();
//                    while (elements.hasNext()) {
//                        JsonNode element = elements.next();
//                        SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
////                        System.out.println("sssss:" + sectionPart.toString());
//                        //将sectionParts转为sectionPart,
//                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
//                        sectionPart.setCatalogCode(catalog);
//                        sectionPart.setSectionCode(section);
//                        sectionParts.add(sectionPart);
//
//                        ModelPartOption modelPartOption = new ModelPartOption();
//                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
//                        modelPartOption.setCatalogCode(catalog);
//                        modelPartOption.setSectionCode(section);
//
//                        //根据type设置
//                        for (Element avsElement : avsElements) {
//                            if (avsElement.attributeValue("type").equals("01")) {
//                                modelPartOption.setType01(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("02")) {
//                                modelPartOption.setType02(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("03")) {
//                                modelPartOption.setType03(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("04")) {
//                                modelPartOption.setType04(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("05")) {
//                                modelPartOption.setType05(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("06")) {
//                                modelPartOption.setType06(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("WT")) {
//                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
//                            } else if (avsElement.attributeValue("type").equals("DT")) {
//                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
//                            }
//                        }
////                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
////                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
////                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
////                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
////                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
////                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
////                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
//                        modelPartOptions.add(modelPartOption);
//
////                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
////                            sectionPartsMapper.batchInsert7(sectionParts);
////                            sectionParts = new ArrayList<>();
////                        }
////
////                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
////                            modelPartOptionMapper.batchInsert7(modelPartOptions);
////                            modelPartOptions = new ArrayList<>();
////                        }
//                        if (sectionParts.size() == 2000 || !elements.hasNext()) {
//                            sectionPartsMapper.batchInsert7(sectionParts);
//                            sectionParts = new ArrayList<>();
//                        }
//
//                        if (modelPartOptions.size() == 2000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert7(modelPartOptions);
//                            modelPartOptions = new ArrayList<>();
//                        }
//                    }
//                }
//            }
//        }
//        System.out.println("Finished7");
////        int result = sectionPartsMapper.batchInsert(sectionParts);
//    }

    @PostMapping("/store-section-parts8")
    public void insertGetSectionParts8() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
        String path = "E:\\three\\partsAndImagesAHMAPHU21.txt";
//        System.out.println("kkl");
        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
        String param = "";
        String parts = "";

        List<SectionPart> sectionParts = new ArrayList<>();
        List<ModelPartOption> modelPartOptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        //先插一条看看
        while (fileContents.hasNext()) {
            String line = fileContents.nextLine();
            if (!line.startsWith("{")) {
//                System.out.println("llll");
                param = line;
            } else {
//                System.out.println("oooo");
                parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);
                int firstBlankIndex = param.indexOf(" ");

                int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                String catalog = param.substring(0, firstBlankIndex);

                String section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
                Document text = DocumentHelper.parseText(interpretationData);
                Element rootElement = text.getRootElement();
                List<Element> avsElements = rootElement.elements("avs");

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }
                JsonNode partNode = null;
                try {
                    partNode = catalogNode.get(0).get("parts");
                } catch (Exception e) {
                    System.out.println(line);
                }

                if (partNode.isArray()) {
                    Iterator<JsonNode> elements = partNode.iterator();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
                        SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                        //将sectionParts转为sectionPart,
                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                        sectionPart.setCatalogCode(catalog);
                        sectionPart.setSectionCode(section);
                        sectionParts.add(sectionPart);

                        ModelPartOption modelPartOption = new ModelPartOption();
                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                        modelPartOption.setCatalogCode(catalog);
                        modelPartOption.setSectionCode(section);

                        //根据type设置
                        for (Element avsElement : avsElements) {
                            if (avsElement.attributeValue("type").equals("01")) {
                                modelPartOption.setType01(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("02")) {
                                modelPartOption.setType02(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("03")) {
                                modelPartOption.setType03(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("04")) {
                                modelPartOption.setType04(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("05")) {
                                modelPartOption.setType05(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("06")) {
                                modelPartOption.setType06(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("WT")) {
                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("DT")) {
                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                            }
                        }
//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                        modelPartOptions.add(modelPartOption);

//                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
//                            sectionPartsMapper.batchInsert8(sectionParts);
//                            sectionParts = new ArrayList<>();
//                        }
//
//                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert8(modelPartOptions);
//                            modelPartOptions = new ArrayList<>();
//                        }

                        if (sectionParts.size() == 2000 || !elements.hasNext()) {
                            sectionPartsMapper.batchInsert8(sectionParts);
                            sectionParts = new ArrayList<>();
                        }

                        if (modelPartOptions.size() == 2000 || !elements.hasNext()) {
                            modelPartOptionMapper.batchInsert8(modelPartOptions);
                            modelPartOptions = new ArrayList<>();
                        }
                    }
                }
            }
        }
        System.out.println("Finished8");
//        int result = sectionPartsMapper.batchInsert(sectionParts);
    }

    @PostMapping("/store-section-parts9")
    public void insertGetSectionParts9() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
//        String path = "E:\\five\\getSectionPartsKHMAPHG15.txt";
//        System.out.println("kkl");
//        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
//        String param = "";
//        String parts = "";

        String param = "KHMAPDV19 ZA000058ZZ <interpretation catalog=\"KHMAPDV19\" type=\"EBOM\" level=\"2\"><avs data=\"D5\" type=\"02\"/><avs data=\"A\" type=\"03\"/><avs data=\"L\" type=\"DT\"/><avs data=\"3\" type=\"WT\"/></interpretation> false true";
        String parts = "{\"data\":\"ZA000058ZZ\",\"hasJPIOSection\":false,\"catalogs\":[{\"data\":\"KHMAPDV19\",\"label\":\"SONATA 19 (2021-)\",\"total\":0,\"totalapplicable\":0,\"totalnonapplicable\":0,\"parts\":[]}],\"illustrations\":[{\"data\":\"ZA000058ZZ\",\"folder\":\"HYW/cat/KHMAPDV19\",\"name\":\"AHMAPDW20ECMirror.png\",\"rects\":[],\"imagePath\":\"HYW/cat/KHMAPDV19/AHMAPDW20ECMirror.png\",\"origin\":0}],\"linkedSectionCodes\":[],\"linkedAjSectionCodes\":[],\"fitmentInstructions\":[],\"tyreCertificates\":[]}";

        List<SectionPart> sectionParts = new ArrayList<>();
        List<ModelPartOption> modelPartOptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

//        String catalogCode = ;

        //先插一条看看
//        while (fileContents.hasNext()) {
//            String line = fileContents.nextLine();
//            if (!line.startsWith("{")) {
////                System.out.println("llll");
//                param = line;
//            } else {
////                System.out.println("oooo");
//                parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
        JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

        JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);
        int firstBlankIndex = param.indexOf(" ");

        int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

        String catalog = param.substring(0, firstBlankIndex);

        String section = param.substring(firstBlankIndex + 1, secondBlankIndex);

        String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
        Document text = DocumentHelper.parseText(interpretationData);
        Element rootElement = text.getRootElement();
        List<Element> avsElements = rootElement.elements("avs");

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

        JsonNode partNode = catalogNode.get(0).get("parts");

        if (partNode.isArray()) {
            Iterator<JsonNode> elements = partNode.iterator();
            while (elements.hasNext()) {
                JsonNode element = elements.next();
                SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                //将sectionParts转为sectionPart,
                SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                sectionPart.setCatalogCode(catalog);
                sectionPart.setSectionCode(section);
                sectionParts.add(sectionPart);

                ModelPartOption modelPartOption = new ModelPartOption();
                modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                modelPartOption.setCatalogCode(catalog);
                modelPartOption.setSectionCode(section);

                //根据type设置
                for (Element avsElement : avsElements) {
                    if (avsElement.attributeValue("type").equals("01")) {
                        modelPartOption.setType01(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("02")) {
                        modelPartOption.setType02(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("03")) {
                        modelPartOption.setType03(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("04")) {
                        modelPartOption.setType04(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("05")) {
                        modelPartOption.setType05(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("06")) {
                        modelPartOption.setType06(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("WT")) {
                        modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("DT")) {
                        modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                    }
                }
//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                modelPartOptions.add(modelPartOption);

//                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
//                            sectionPartsMapper.batchInsert9(sectionParts);
//                            sectionParts = new ArrayList<>();
//                        }
//
//                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert9(modelPartOptions);
//                            modelPartOptions = new ArrayList<>();
//                        }
//                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
//                            sectionPartsMapper.batchInsert9(sectionParts);
//                            sectionParts = new ArrayList<>();
//                        }
//
//                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert9(modelPartOptions);
//                            modelPartOptions = new ArrayList<>();
//                        }
            }
        }
    }
//        }
//        System.out.println("Finished9");
//        int result = sectionPartsMapper.batchInsert(sectionParts);
//    }

    @PostMapping("/store-section-parts10")
    public void insertGetSectionParts10() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库

        String path = "E:\\hpdepc\\getSectionPartsKHMAPDV19.txt";
//        System.out.println("kkl");
        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
        String param = "";
        String parts = "";

        List<SectionPart> sectionParts = new ArrayList<>();
        List<ModelPartOption> modelPartOptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

//        String catalogCode = ;

        //ymm
        //partnumber+

        //先插一条看看
        while (fileContents.hasNext()) {
            String line = fileContents.nextLine();
            if (!line.startsWith("{")) {
//                System.out.println("llll");
                param = line;
            } else {
//                System.out.println("oooo");
                parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);
                int firstBlankIndex = param.indexOf(" ");

                int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                String catalog = param.substring(0, firstBlankIndex);

                String section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
                Document text = DocumentHelper.parseText(interpretationData);
                Element rootElement = text.getRootElement();
                List<Element> avsElements = rootElement.elements("avs");

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                JsonNode partNode = catalogNode.get(0).get("parts");

                if (partNode.isArray()) {
                    Iterator<JsonNode> elements = partNode.iterator();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
                        SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                        //将sectionParts转为sectionPart,
                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                        sectionPart.setCatalogCode(catalog);
                        sectionPart.setSectionCode(section);
                        sectionParts.add(sectionPart);

                        ModelPartOption modelPartOption = new ModelPartOption();
                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                        modelPartOption.setCatalogCode(catalog);
                        modelPartOption.setSectionCode(section);

                        //根据type设置
                        for (Element avsElement : avsElements) {
                            if (avsElement.attributeValue("type").equals("01")) {
                                modelPartOption.setType01(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("02")) {
                                modelPartOption.setType02(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("03")) {
                                modelPartOption.setType03(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("04")) {
                                modelPartOption.setType04(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("05")) {
                                modelPartOption.setType05(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("06")) {
                                modelPartOption.setType06(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("WT")) {
                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("DT")) {
                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                            }
                        }
//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                        modelPartOptions.add(modelPartOption);

//                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
//                            sectionPartsMapper.batchInsert10(sectionParts);
//                            sectionParts = new ArrayList<>();
//                        }
//
//                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert10(modelPartOptions);
//                            modelPartOptions = new ArrayList<>();
//                        }
                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
                            sectionPartsMapper.batchInsert10(sectionParts);
                            sectionParts = new ArrayList<>();
                        }

                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
                            modelPartOptionMapper.batchInsert10(modelPartOptions);
                            modelPartOptions = new ArrayList<>();
                        }
                    }
                }
            }
        }
        System.out.println("Finished10");
//        int result = sectionPartsMapper.batchInsert(sectionParts);
    }

    @PostMapping("/store-section-parts11")
    public void insertGetSectionParts11() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
        String path = "E:\\hpdepc\\getSectionPartsAHMAPCMA06-Part1.txt";
//        System.out.println("kkl");
        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
        String param = "";
        String parts = "";

        List<SectionPart> sectionParts = new ArrayList<>();
        List<ModelPartOption> modelPartOptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

//        String catalogCode = ;

        //ymm
        //partnumber+

        //先插一条看看
        while (fileContents.hasNext()) {
            String line = fileContents.nextLine();
            if (!line.startsWith("{")) {
//                System.out.println("llll");
                param = line;
            } else {
//                System.out.println("oooo");
                parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);
                int firstBlankIndex = param.indexOf(" ");

                int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                String catalog = param.substring(0, firstBlankIndex);

                String section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
                Document text = DocumentHelper.parseText(interpretationData);
                Element rootElement = text.getRootElement();
                List<Element> avsElements = rootElement.elements("avs");

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                JsonNode partNode = catalogNode.get(0).get("parts");

                if (partNode.isArray()) {
                    Iterator<JsonNode> elements = partNode.iterator();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
                        SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                        //将sectionParts转为sectionPart,
                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                        sectionPart.setCatalogCode(catalog);
                        sectionPart.setSectionCode(section);
                        sectionParts.add(sectionPart);

                        ModelPartOption modelPartOption = new ModelPartOption();
                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                        modelPartOption.setCatalogCode(catalog);
                        modelPartOption.setSectionCode(section);

                        //根据type设置
                        for (Element avsElement : avsElements) {
                            if (avsElement.attributeValue("type").equals("01")) {
                                modelPartOption.setType01(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("02")) {
                                modelPartOption.setType02(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("03")) {
                                modelPartOption.setType03(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("04")) {
                                modelPartOption.setType04(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("05")) {
                                modelPartOption.setType05(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("06")) {
                                modelPartOption.setType06(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("WT")) {
                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("DT")) {
                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                            }
                        }
//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                        modelPartOptions.add(modelPartOption);

//                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
//                            sectionPartsMapper.batchInsert11(sectionParts);
//                            sectionParts = new ArrayList<>();
//                        }
//
//                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert11(modelPartOptions);
//                            modelPartOptions = new ArrayList<>();
//                        }
                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
                            sectionPartsMapper.batchInsert11(sectionParts);
                            sectionParts = new ArrayList<>();
                        }

                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
                            modelPartOptionMapper.batchInsert11(modelPartOptions);
                            modelPartOptions = new ArrayList<>();
                        }
                    }
                }
            }
        }
        System.out.println("Finished11");
//        int result = sectionPartsMapper.batchInsert(sectionParts);
    }

    @PostMapping("/store-section-parts12")
    public void insertGetSectionParts12() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
        String path = "E:\\hpdepc\\getSectionPartsKHMAPG317.txt";
//        System.out.println("kkl");
        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
        String param = "";
        String parts = "";

        List<SectionPart> sectionParts = new ArrayList<>();
        List<ModelPartOption> modelPartOptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

//        String catalogCode = ;

        //ymm
        //partnumber+

        //先插一条看看
        while (fileContents.hasNext()) {
            String line = fileContents.nextLine();
            if (!line.startsWith("{")) {
//                System.out.println("llll");
                param = line;
            } else {
//                System.out.println("oooo");
                parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);
                int firstBlankIndex = param.indexOf(" ");

                int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                String catalog = param.substring(0, firstBlankIndex);

                String section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
                Document text = DocumentHelper.parseText(interpretationData);
                Element rootElement = text.getRootElement();
                List<Element> avsElements = rootElement.elements("avs");

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                JsonNode partNode = catalogNode.get(0).get("parts");

                if (partNode.isArray()) {
                    Iterator<JsonNode> elements = partNode.iterator();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
                        SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                        //将sectionParts转为sectionPart,
                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                        sectionPart.setCatalogCode(catalog);
                        sectionPart.setSectionCode(section);
                        sectionParts.add(sectionPart);

                        ModelPartOption modelPartOption = new ModelPartOption();
                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                        modelPartOption.setCatalogCode(catalog);
                        modelPartOption.setSectionCode(section);

                        //根据type设置
                        for (Element avsElement : avsElements) {
                            if (avsElement.attributeValue("type").equals("01")) {
                                modelPartOption.setType01(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("02")) {
                                modelPartOption.setType02(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("03")) {
                                modelPartOption.setType03(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("04")) {
                                modelPartOption.setType04(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("05")) {
                                modelPartOption.setType05(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("06")) {
                                modelPartOption.setType06(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("WT")) {
                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("DT")) {
                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                            }
                        }
//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                        modelPartOptions.add(modelPartOption);

//                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
//                            sectionPartsMapper.batchInsert12(sectionParts);
//                            sectionParts = new ArrayList<>();
//                        }
//
//                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert12(modelPartOptions);
//                            modelPartOptions = new ArrayList<>();
//                        }
                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
                            sectionPartsMapper.batchInsert12(sectionParts);
                            sectionParts = new ArrayList<>();
                        }

                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
                            modelPartOptionMapper.batchInsert12(modelPartOptions);
                            modelPartOptions = new ArrayList<>();
                        }
                    }
                }
            }
        }
        System.out.println("Finished12");
//        int result = sectionPartsMapper.batchInsert(sectionParts);
    }

    @PostMapping("/store-section-parts13")
    public void insertGetSectionParts13() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
        String path = "E:\\hpdepc\\";
//        System.out.println("kkl");
        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
        String param = "";
        String parts = "";

        List<SectionPart> sectionParts = new ArrayList<>();
        List<ModelPartOption> modelPartOptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

//        String catalogCode = ;

        //ymm
        //partnumber+

        //先插一条看看
        while (fileContents.hasNext()) {
            String line = fileContents.nextLine();
            if (!line.startsWith("{")) {
//                System.out.println("llll");
                param = line;
            } else {
//                System.out.println("oooo");
                parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);
                int firstBlankIndex = param.indexOf(" ");

                int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                String catalog = param.substring(0, firstBlankIndex);

                String section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
                Document text = DocumentHelper.parseText(interpretationData);
                Element rootElement = text.getRootElement();
                List<Element> avsElements = rootElement.elements("avs");

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                JsonNode partNode = catalogNode.get(0).get("parts");

                if (partNode.isArray()) {
                    Iterator<JsonNode> elements = partNode.iterator();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
                        SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                        //将sectionParts转为sectionPart,
                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                        sectionPart.setCatalogCode(catalog);
                        sectionPart.setSectionCode(section);
                        sectionParts.add(sectionPart);

                        ModelPartOption modelPartOption = new ModelPartOption();
                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                        modelPartOption.setCatalogCode(catalog);
                        modelPartOption.setSectionCode(section);

                        //根据type设置
                        for (Element avsElement : avsElements) {
                            if (avsElement.attributeValue("type").equals("01")) {
                                modelPartOption.setType01(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("02")) {
                                modelPartOption.setType02(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("03")) {
                                modelPartOption.setType03(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("04")) {
                                modelPartOption.setType04(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("05")) {
                                modelPartOption.setType05(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("06")) {
                                modelPartOption.setType06(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("WT")) {
                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("DT")) {
                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                            }
                        }
//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                        modelPartOptions.add(modelPartOption);

                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
                            sectionPartsMapper.batchInsert13(sectionParts);
                            sectionParts = new ArrayList<>();
                        }

                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
                            modelPartOptionMapper.batchInsert13(modelPartOptions);
                            modelPartOptions = new ArrayList<>();
                        }
                    }
                }
            }
        }
        System.out.println("Finished13");
//        int result = sectionPartsMapper.batchInsert(sectionParts);
    }

    @PostMapping("/store-section-parts14")
    public void insertGetSectionParts14() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
        String path = "E:\\hpdepc\\getSectionPartsKHMAPD318.txt";
//        System.out.println("kkl");
        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
        String param = "";
        String parts = "";

        List<SectionPart> sectionParts = new ArrayList<>();
        List<ModelPartOption> modelPartOptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

//        String catalogCode = ;

        //ymm
        //partnumber+

        //先插一条看看
        while (fileContents.hasNext()) {
            String line = fileContents.nextLine();
            if (!line.startsWith("{")) {
//                System.out.println("llll");
                param = line;
            } else {
//                System.out.println("oooo");
                parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);
                int firstBlankIndex = param.indexOf(" ");

                int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                String catalog = param.substring(0, firstBlankIndex);

                String section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
                Document text = DocumentHelper.parseText(interpretationData);
                Element rootElement = text.getRootElement();
                List<Element> avsElements = rootElement.elements("avs");

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                JsonNode partNode = catalogNode.get(0).get("parts");

                if (partNode.isArray()) {
                    Iterator<JsonNode> elements = partNode.iterator();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
                        SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                        //将sectionParts转为sectionPart,
                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                        sectionPart.setCatalogCode(catalog);
                        sectionPart.setSectionCode(section);
                        sectionParts.add(sectionPart);

                        ModelPartOption modelPartOption = new ModelPartOption();
                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                        modelPartOption.setCatalogCode(catalog);
                        modelPartOption.setSectionCode(section);

                        //根据type设置
                        for (Element avsElement : avsElements) {
                            if (avsElement.attributeValue("type").equals("01")) {
                                modelPartOption.setType01(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("02")) {
                                modelPartOption.setType02(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("03")) {
                                modelPartOption.setType03(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("04")) {
                                modelPartOption.setType04(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("05")) {
                                modelPartOption.setType05(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("06")) {
                                modelPartOption.setType06(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("WT")) {
                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("DT")) {
                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                            }
                        }
//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                        modelPartOptions.add(modelPartOption);

                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
                            sectionPartsMapper.batchInsert14(sectionParts);
                            sectionParts = new ArrayList<>();
                        }

                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
                            modelPartOptionMapper.batchInsert14(modelPartOptions);
                            modelPartOptions = new ArrayList<>();
                        }
                    }
                }
            }
        }
        System.out.println("Finished14");
//        int result = sectionPartsMapper.batchInsert(sectionParts);
    }

    @PostMapping("/store-section-parts15")
    public void insertGetSectionParts15() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
        String path = "E:\\hpdepc\\getSectionPartsKHMAPDK12.txt";
//        System.out.println("kkl");
        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
        String param = "";
        String parts = "";

        List<SectionPart> sectionParts = new ArrayList<>();
        List<ModelPartOption> modelPartOptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

//        String catalogCode = ;

        //ymm
        //partnumber+

        //先插一条看看
        while (fileContents.hasNext()) {
            String line = fileContents.nextLine();
            if (!line.startsWith("{")) {
//                System.out.println("llll");
                param = line;
            } else {
//                System.out.println("oooo");
                parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);
                int firstBlankIndex = param.indexOf(" ");

                int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                String catalog = param.substring(0, firstBlankIndex);

                String section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
                Document text = DocumentHelper.parseText(interpretationData);
                Element rootElement = text.getRootElement();
                List<Element> avsElements = rootElement.elements("avs");

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                JsonNode partNode = catalogNode.get(0).get("parts");

                if (partNode.isArray()) {
                    Iterator<JsonNode> elements = partNode.iterator();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
                        SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                        //将sectionParts转为sectionPart,
                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                        sectionPart.setCatalogCode(catalog);
                        sectionPart.setSectionCode(section);
                        sectionParts.add(sectionPart);

                        ModelPartOption modelPartOption = new ModelPartOption();
                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                        modelPartOption.setCatalogCode(catalog);
                        modelPartOption.setSectionCode(section);

                        //根据type设置
                        for (Element avsElement : avsElements) {
                            if (avsElement.attributeValue("type").equals("01")) {
                                modelPartOption.setType01(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("02")) {
                                modelPartOption.setType02(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("03")) {
                                modelPartOption.setType03(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("04")) {
                                modelPartOption.setType04(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("05")) {
                                modelPartOption.setType05(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("06")) {
                                modelPartOption.setType06(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("WT")) {
                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("DT")) {
                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                            }
                        }
//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                        modelPartOptions.add(modelPartOption);

                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
                            sectionPartsMapper.batchInsert15(sectionParts);
                            sectionParts = new ArrayList<>();
                        }

                        if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
                            modelPartOptionMapper.batchInsert15(modelPartOptions);
                            modelPartOptions = new ArrayList<>();
                        }
                    }
                }
            }
        }
        System.out.println("Finished15");
//        int result = sectionPartsMapper.batchInsert(sectionParts);
    }

    //todo
    @PostMapping("/store-section-parts16")
    public void insertGetSectionParts16() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
//        getSectionPartsAHMAPCMA06-Part2.txt
        String path = "E:\\eighteen\\";

        //有一份新的文件
//        String path = "E:\\hpdepc\\getSectionPartsKHMAPDV19-new.txt";
//        System.out.println("kkl");

        File directory = new File(path);
        File[] files = directory.listFiles();

        for (File file : files) {
            LineIterator fileContents = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name());
            String param = "";
            String parts = "";

            List<ModelSectionPartsTemp1> sectionParts = new ArrayList<>();
            List<ModelPartOptionNewTemp1> modelPartOptions = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            String catalog = "";
            String section = "";

            //ymm
            //partnumber+

            //先插一条看看
            while (fileContents.hasNext()) {
                String line = fileContents.nextLine();
                if (!line.startsWith("{")) {
//                System.out.println("llll");
                    ModelPartOptionNewTemp1 modelPartOption = new ModelPartOptionNewTemp1();
                    param = line;
                    int firstBlankIndex = param.indexOf(" ");

                    int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                    catalog = param.substring(0, firstBlankIndex);

                    section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                    String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);
                    Document text = DocumentHelper.parseText(interpretationData);
                    Element rootElement = text.getRootElement();
                    modelPartOption.setCatalogCode(catalog);
                    modelPartOption.setSectionCode(section);
                    modelPartOption.setId(id.incrementAndGet());
                    List<Element> avsElements = rootElement.elements("avs");
                    for (Element avsElement : avsElements) {
                        if (avsElement.attributeValue("type").equals("01")) {
                            modelPartOption.setType01(avsElement.attributeValue("data"));
                        } else if (avsElement.attributeValue("type").equals("02")) {
                            modelPartOption.setType02(avsElement.attributeValue("data"));
                        } else if (avsElement.attributeValue("type").equals("03")) {
                            modelPartOption.setType03(avsElement.attributeValue("data"));
                        } else if (avsElement.attributeValue("type").equals("04")) {
                            modelPartOption.setType04(avsElement.attributeValue("data"));
                        } else if (avsElement.attributeValue("type").equals("05")) {
                            modelPartOption.setType05(avsElement.attributeValue("data"));
                        } else if (avsElement.attributeValue("type").equals("06")) {
                            modelPartOption.setType06(avsElement.attributeValue("data"));
                        } else if (avsElement.attributeValue("type").equals("WT")) {
                            modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                        } else if (avsElement.attributeValue("type").equals("DT")) {
                            modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                        }
                    }
                    modelPartOptions.add(modelPartOption);
                } else {
//                System.out.println("oooo");

                    parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                    JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                    JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                    JsonNode partNode = catalogNode.get(0).get("parts");

                    if (partNode.isArray()) {
                        Iterator<JsonNode> elements = partNode.iterator();
                        while (elements.hasNext()) {
                            JsonNode element = elements.next();
                            SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                            //将sectionParts转为sectionPart,
//                        ModelSectionPartsTemp1 modelSectionPartsTemp1 = new ModelSectionPartsTemp1();
//                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
//                        sectionPart.setCatalogCode(catalog);
//                        sectionPart.setSectionCode(section);
                            ModelSectionPartsTemp1 modelSectionPartsTemp1 = ModelSectionPartsTemp1.convert(sectionPartDto);
                            modelSectionPartsTemp1.setCatalogCode(catalog);
                            modelSectionPartsTemp1.setSectionCode(section);
//                        id.get()
                            modelSectionPartsTemp1.setOptionId(id.get());
                            sectionParts.add(modelSectionPartsTemp1);

//                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());

                            //根据type设置

//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");

                            if (sectionParts.size() == 5000 || !elements.hasNext()) {
                                modelSectionPartsTemp1Mapper.batchInsert(sectionParts);
//                            sectionPartsMapper.batchInsert15(sectionParts);
                                sectionParts = new ArrayList<>();
                            }

                            if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
//                            modelPartOptionMapper.batchInsert15(modelPartOptions);
                                modelPartOptionNewTemp1Mapper.batchInsert(modelPartOptions);
                                modelPartOptions = new ArrayList<>();
                            }
                        }
                    }
                }
            }
            System.out.println("Finished" + file.getName());
//        int result = sectionPartsMapper.batchInsert(sectionParts);
        }
    }

    @PostMapping("/store-section-parts17")
    public void insertGetSectionParts17() throws IOException, DocumentException {
//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
//        getSectionPartsAHMAPCMA06-Part2.txt
//        String path = "H:\\HPDEPC4";
//        String path = "E:\\fifty";

//        String path = "I:\\hpdepc3";
        String path = "E:\\eighty-two";
        //有一份新的文件
//        String path = "E:\\hpdepc\\getSectionPartsKHMAPDV19-new.txt";
//        System.out.println("kkl");

        File directory = new File(path);
        File[] files = directory.listFiles();
//        Long maxId = modelPartOptionMapper.selectMaxId();
//        System.out.println("oooo" + maxId);
        Long maxId = modelPartOptionMapper.selectMaxId();
        id2 = new AtomicLong(maxId);
//        System.out.println("iiii" + id2);

        for (File file : files) {
            LineIterator fileContents = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name());
            String param = "";
            String parts = "";

//            List<ModelSectionPartsTemp1> sectionParts = new ArrayList<>();
//            List<ModelPartOptionNewTemp1> modelPartOptions = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            String catalog = "";
            String section = "";

            //ymm
            //partnumber+

            //先插一条看看
            while (fileContents.hasNext()) {
                String line = fileContents.nextLine();
                try {
                    if (!line.startsWith("{")) {
//                System.out.println("llll");
                        ModelPartOptionNewTemp1 modelPartOption = new ModelPartOptionNewTemp1();
                        param = line;
                        int firstBlankIndex = param.indexOf(" ");

                        int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                        catalog = param.substring(0, firstBlankIndex);

                        section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                        String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);
                        Document text = DocumentHelper.parseText(interpretationData);
                        Element rootElement = text.getRootElement();
                        modelPartOption.setCatalogCode(catalog);
                        modelPartOption.setSectionCode(section);
                        modelPartOption.setId(id2.incrementAndGet());
                        List<Element> avsElements = rootElement.elements("avs");
                        for (Element avsElement : avsElements) {
                            if (avsElement.attributeValue("type").equals("01")) {
                                modelPartOption.setType01(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("02")) {
                                modelPartOption.setType02(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("03")) {
                                modelPartOption.setType03(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("04")) {
                                modelPartOption.setType04(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("05")) {
                                modelPartOption.setType05(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("06")) {
                                modelPartOption.setType06(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("WT")) {
                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("DT")) {
                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                            }
                        }
                        modelPartOptions1.add(modelPartOption);
                    } else {
//                System.out.println("oooo");

                        parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                        JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                        JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                        JsonNode partNode = catalogNode.get(0).get("parts");

                        if (partNode.isArray()) {
                            Iterator<JsonNode> elements = partNode.iterator();
                            while (elements.hasNext()) {
                                JsonNode element = elements.next();
                                SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                                //将sectionParts转为sectionPart,
//                        ModelSectionPartsTemp1 modelSectionPartsTemp1 = new ModelSectionPartsTemp1();
//                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
//                        sectionPart.setCatalogCode(catalog);
//                        sectionPart.setSectionCode(section);
                                ModelSectionPartsTemp1 modelSectionPartsTemp1 = ModelSectionPartsTemp1.convert(sectionPartDto);
                                modelSectionPartsTemp1.setCatalogCode(catalog);
                                modelSectionPartsTemp1.setSectionCode(section);
//                        id.get()
                                modelSectionPartsTemp1.setOptionId(id2.get());
                                sectionParts1.add(modelSectionPartsTemp1);

//                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());

                                //根据type设置

//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");

                                if (sectionParts1.size() == 5000) {
//                                modelSectionPartsTemp1Mapper.batchInsert(sectionParts);
                                    modelSectionPartsTemp1Mapper.batchInsertPartByModelNew(sectionParts1);
//                            sectionPartsMapper.batchInsert15(sectionParts);
                                    sectionParts1 = new ArrayList<>();
                                }

                                if (modelPartOptions1.size() >= 1000) {
//                            modelPartOptionMapper.batchInsert15(modelPartOptions);
                                    modelPartOptionNewTemp1Mapper.batchInsertModelPartOptionNewestTemp2(modelPartOptions1);
                                    modelPartOptions1 = new ArrayList<>();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("llll" + param);
                }
            }
            modelSectionPartsTemp1Mapper.batchInsertPartByModelNew(sectionParts1);
            modelPartOptionNewTemp1Mapper.batchInsertModelPartOptionNewestTemp2(modelPartOptions1);
//        int result = sectionPartsMapper.batchInsert(sectionParts);
            System.out.println("Finished" + file.getName());
        }
    }

    @PostMapping("/store-section-parts18")
    public void insertGetSectionParts18() throws IOException, DocumentException {
        //        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库
//        getSectionPartsAHMAPCMA06-Part2.txt
        String path = "H:\\hpdepc5";
        //有一份新的文件
//        String path = "E:\\hpdepc\\getSectionPartsKHMAPDV19-new.txt";
//        System.out.println("kkl");
        File directory = new File(path);
        File[] files = directory.listFiles();
        Long maxId = modelPartOptionMapper.selectMaxId2();
        id2 = new AtomicLong(maxId);

        for (File file : files) {
            LineIterator fileContents = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name());
            String param = "";
            String parts = "";
//            List<ModelSectionPartsTemp1> sectionParts = new ArrayList<>();
//            List<ModelPartOptionNewTemp1> modelPartOptions = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            String catalog = "";
            String section = "";
            //ymm
            //partnumber+
            //先插一条看看
            while (fileContents.hasNext()) {
                String line = fileContents.nextLine();
                try {
                    if (!line.startsWith("{")) {
//                System.out.println("llll");
                        ModelPartOptionNewTemp1 modelPartOption = new ModelPartOptionNewTemp1();
                        param = line;
                        int firstBlankIndex = param.indexOf(" ");

                        int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                        catalog = param.substring(0, firstBlankIndex);

                        section = param.substring(firstBlankIndex + 1, secondBlankIndex);

                        String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);
                        Document text = DocumentHelper.parseText(interpretationData);
                        Element rootElement = text.getRootElement();
                        modelPartOption.setCatalogCode(catalog);
                        modelPartOption.setSectionCode(section);
                        modelPartOption.setId(id2.incrementAndGet());
                        //插入排序可以二分。二段性。
                        //如何最快找到bug所在？根据现象推测，有几个组件，可能是哪个组件有问题。再到代码，优先在相关度高的地方打断点。
                        //先用深度优先(逻辑推理)，不行就广度优先。
                        List<Element> avsElements = rootElement.elements("avs");
                        for (Element avsElement : avsElements) {
                            if (avsElement.attributeValue("type").equals("01")) {
                                modelPartOption.setType01(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("02")) {
                                modelPartOption.setType02(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("03")) {
                                modelPartOption.setType03(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("04")) {
                                modelPartOption.setType04(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("05")) {
                                modelPartOption.setType05(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("06")) {
                                modelPartOption.setType06(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("WT")) {
                                modelPartOption.setTypeWT(avsElement.attributeValue("data"));
                            } else if (avsElement.attributeValue("type").equals("DT")) {
                                modelPartOption.setTypeDT(avsElement.attributeValue("data"));
                            }
                        }
                        modelPartOptions2.add(modelPartOption);
                    } else {
//                System.out.println("oooo");

                        parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                        JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                        JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);

//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                        JsonNode partNode = catalogNode.get(0).get("parts");

                        if (partNode.isArray()) {
                            Iterator<JsonNode> elements = partNode.iterator();
                            while (elements.hasNext()) {
                                JsonNode element = elements.next();
                                SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                                //将sectionParts转为sectionPart,
//                        ModelSectionPartsTemp1 modelSectionPartsTemp1 = new ModelSectionPartsTemp1();
//                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
//                        sectionPart.setCatalogCode(catalog);
//                        sectionPart.setSectionCode(section);
                                ModelSectionPartsTemp1 modelSectionPartsTemp1 = ModelSectionPartsTemp1.convert(sectionPartDto);
                                modelSectionPartsTemp1.setCatalogCode(catalog);
                                modelSectionPartsTemp1.setSectionCode(section);
//                        id.get()
                                modelSectionPartsTemp1.setOptionId(id2.get());
                                sectionParts2.add(modelSectionPartsTemp1);

//                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());

                                //根据type设置

//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");

                                if (sectionParts2.size() == 5000) {
//                                    modelSectionPartsTemp1Mapper.batchInsert(sectionParts);
                                    modelSectionPartsTemp1Mapper.batchInsertPartByModelNew2(sectionParts2);
//                            sectionPartsMapper.batchInsert15(sectionParts);
                                    sectionParts2 = new ArrayList<>();
                                }
//                                System.out.println("mmmm" + modelPartOptions.size());
                                if (modelPartOptions2.size() >= 1000) {
//                                    modelPartOptionMapper.batchInsert15(modelPartOptions);
//                                    System.out.println("kkkk");
                                    modelPartOptionNewTemp1Mapper.batchInsertModelPartOptionNewestTemp3(modelPartOptions2);
                                    modelPartOptions2 = new ArrayList<>();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("llll" + param);
                }
            }
            modelSectionPartsTemp1Mapper.batchInsertPartByModelNew2(sectionParts2);
            modelPartOptionNewTemp1Mapper.batchInsertModelPartOptionNewestTemp3(modelPartOptions2);
            System.out.println("Finished" + file.getName());
//        int result = sectionPartsMapper.batchInsert(sectionParts);
        }
    }

    @PostMapping("/store-duplicate-vin-decode-data")
    public void insertDuplicateVinDecodeData() throws IOException {
        //读取目录
        String path = "C:\\Users\\admin\\Desktop";
        File directory = new File(path);
        File[] files = directory.listFiles();

        List<VinDecodeNewest> vinDecodeNewestList = new ArrayList<>();
        for (File file : files) {
            int count = 1;
            LineIterator fileContents = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name());

//            VinDecodeNewest vinDecodeNewest = new VinDecodeNewest();
            while (fileContents.hasNext()) {
                String line = fileContents.nextLine();
                System.out.println(line);
                if (count % 2 == 1) {
//                    String vin = fileContents.nextLine();
//                    vinDecodeNewest.setVin(line);
                    count++;
                    continue;
                } else if (count % 2 == 0) {
                    //解析
                    JSONObject jsonLine = JSONUtil.parseObj(line);
                    JSONArray jsonArray = jsonLine.getJSONArray("catalogs");
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = (JSONObject) object;
                        JSONArray vehicleJsonArray = jsonObject.getJSONArray("vehicles");
                        JSONObject vehicleJsonObject = (JSONObject) vehicleJsonArray.get(0);
                        VinDecodeNewest vinDecodeNewest = new VinDecodeNewest();
//                        JSONArray interpretationAVSData = jsonObject.getJSONArray("interpretationAVSData");
                        vinDecodeNewest.setVin(String.valueOf(vehicleJsonObject.get("vin")));
                        //todo 设置sectionApplicability
                        System.out.println("vvvvv"+ vinDecodeNewest.getVin());
                        vinDecodeNewest.setInterior_key_colour_code(String.valueOf(vehicleJsonObject.get("interiorKeyColorCode")));
                        vinDecodeNewest.setExterior_key_colour_code(String.valueOf(vehicleJsonObject.get("exteriorKeyColorCode")));
                        JSONArray vehicleInfoList = vehicleJsonObject.getJSONArray("vehicleInfoList");
                        JSONArray clientVehicleAttributes = ((JSONObject) vehicleInfoList.get(0)).getJSONArray("clientVehicleAttributes");
                        System.out.println("lllll" + vehicleInfoList.size());
                        for (Object vehicleInfo : vehicleInfoList) {
                            if (((JSONObject)vehicleInfo).get("data").equals("general")) {
                                for (Object clientVehicleAttribute : clientVehicleAttributes) {
                                    JSONObject jsonVehicleInfo = (JSONObject) clientVehicleAttribute;
                                    System.out.println("jjjjj" + jsonVehicleInfo.toJSONString(0));
                                    if (jsonVehicleInfo.getStr("label").equals("Catalog Code")) {
                                        vinDecodeNewest.setCatalog_code(jsonVehicleInfo.getStr("data"));
                                    } else if (jsonVehicleInfo.getStr("label").equals("Production Date")) {
                                        vinDecodeNewest.setProduction_date(jsonVehicleInfo.getStr("data"));
                                    } else if (jsonVehicleInfo.getStr("label").equals("Country")) {
                                        vinDecodeNewest.setCountry(jsonVehicleInfo.getStr("data"));
                                    } else if (jsonVehicleInfo.getStr("label").equals("Region")) {
                                        vinDecodeNewest.setRegion(jsonVehicleInfo.getStr("data"));
                                    } else if (jsonVehicleInfo.getStr("label").equals("Plant")) {
                                        vinDecodeNewest.setPlant(jsonVehicleInfo.getStr("data"));
                                    }
                                }
                            } else if (((JSONObject)vehicleInfo).getStr("data").equals("exteriorcolor")) {
                                JSONArray exteriorColorAttributes = ((JSONObject)vehicleInfo).getJSONArray("clientVehicleAttributes");
                                vinDecodeNewest.setExterior_colour_desc(((JSONObject) exteriorColorAttributes.get(0)).getStr("label"));
                                vinDecodeNewest.setExterior_colour_sub_attributes(((JSONObject) exteriorColorAttributes.get(0)).getJSONArray("subAttributes").toJSONString(0));
                            } else if (((JSONObject)vehicleInfo).getStr("data").equals("interiorcolor")) {
                                JSONArray interiorColorAttributes = ((JSONObject)vehicleInfo).getJSONArray("clientVehicleAttributes");
                                vinDecodeNewest.setInterior_colour_desc(((JSONObject) interiorColorAttributes.get(0)).getStr("label"));
                                if (((JSONObject) interiorColorAttributes.get(0)).getJSONArray("subAttributes") != null) {
                                    vinDecodeNewest.setInterior_colour_sub_attributes(((JSONObject) interiorColorAttributes.get(0)).getJSONArray("subAttributes").toJSONString(0));
                                }
                            }
                        }

                        vinDecodeNewest.setModel_year(vehicleJsonObject.getStr("modelYear"));
                        vinDecodeNewest.setModel(vehicleJsonObject.getStr("modelCode"));
//                      todo 设置sectionApplicability
//                        vinDecodeNewest.setSection_applicability();

//                        if (jsonObject.get("label").equals("Catalog Code")) {
//                            vinDecodeNewest.setCatalog_code(String.valueOf(jsonObject.get("data")));
//                        } else if (jsonObject.get("label").equals("Production Date")) {
//                            vinDecodeNewest.setProduction_date(String.valueOf(jsonObject.get("data")));
//                        } else if (jsonObject.get("label").equals("Model Year")) {
//                            vinDecodeNewest.setModel_year(String.valueOf(jsonObject.get("data")));
//                        } else if (jsonObject.get("label").equals("Model")) {
//                            vinDecodeNewest.setModel(String.valueOf(jsonObject.get("data")));
//                        } else if (jsonObject.get("label").equals("Country")) {
//                            vinDecodeNewest.setCountry(String.valueOf(jsonObject.get("data")));
//                        } else if (jsonObject.get("label").equals("Region")) {
//                            vinDecodeNewest.setRegion(String.valueOf(jsonObject.get("data")));
//                        } else if (jsonObject.get("label").equals("Plant")) {
//                            vinDecodeNewest.setPlant(String.valueOf(jsonObject.get("data")));
//                        }
                        vinDecodeNewestList.add(vinDecodeNewest);
                    }
//                    if (vinDecodeNewestList.size() >= 1000) {
//                        vinDecodeNewestMapper.batchInsert(vinDecodeNewestList);
//                        vinDecodeNewestList = new ArrayList<>();
//                    }
                }
//                else if (count % 5 == 3) {
//                    JSONArray exteriorColorJsonArray = JSONUtil.parseArray(line);
////                    for (Object object : interiorColorJsonArray) {
//                    vinDecodeNewest.setExterior_key_colour_code(String.valueOf(((JSONObject)exteriorColorJsonArray.get(0)).get("data")));
//                    vinDecodeNewest.setExterior_colour_desc(((JSONObject)exteriorColorJsonArray.get(0)).getStr("label"));
//                    JSONArray jsonArray = ((JSONObject)exteriorColorJsonArray.get(0)).getJSONArray("subAttributes");
//                    if (jsonArray != null) {
//                        String subAttributesArr = jsonArray.toJSONString(0);
//                        vinDecodeNewest.setExterior_colour_sub_attributes(subAttributesArr);
//                    }
////                                subAttributes
////                        vinDecodeNewest.
////                    }
//                } else if (count % 5 == 4) {
//                    JSONArray interiorColorJsonArray = JSONUtil.parseArray(line);
////                    for (Object object : interiorColorJsonArray) {
//                    vinDecodeNewest.setInterior_key_colour_code(String.valueOf(((JSONObject)interiorColorJsonArray.get(0)).get("data")));
//                    vinDecodeNewest.setInterior_colour_desc(((JSONObject)interiorColorJsonArray.get(0)).getStr("label"));
//                    JSONArray jsonArray = ((JSONObject)interiorColorJsonArray.get(0)).getJSONArray("subAttributes");
//                    if (jsonArray != null) {
//                        String subAttributesArr = jsonArray.toJSONString(0);
//                        vinDecodeNewest.setInterior_colour_sub_attributes(subAttributesArr);
//                    }
////                    vinDecodeNewest.setEx
//                } else if (count % 5 == 0) {
//                    vinDecodeNewest.setSection_applicability(line);
//                    vinDecodeNewestList.add(vinDecodeNewest);
//                    vinDecodeNewest =  new VinDecodeNewest();
//                }
//                if (count % 5 == 0) {
//                    System.out.println(vinDecodeNewestList.get(0).toString());
//                }
                count++;
                if (vinDecodeNewestList.size() == 1000) {
                    vinDecodeNewestMapper.batchInsert(vinDecodeNewestList);
                    vinDecodeNewestList = new ArrayList<>();
                }
            }
            System.out.println();
            vinDecodeNewestMapper.batchInsert(vinDecodeNewestList);
            vinDecodeNewestList = new ArrayList<>();
        }


        //读取vin
        //读取general
        //读取color
        //读取适用
        //存表
    }

    @PostMapping("/store-vin-decode-data")
    public void insertVinDecodeData() throws IOException {
        //读取目录
        String path = "C:\\Users\\admin\\Desktop\\新建文件夹 (8)";
        File directory = new File(path);
        File[] files = directory.listFiles();

        List<VinDecodeNewest> vinDecodeNewestList = new ArrayList<>();
        for (File file : files) {
            int count = 1;
            LineIterator fileContents = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name());

            VinDecodeNewest vinDecodeNewest = new VinDecodeNewest();
            while (fileContents.hasNext()) {
                String line = fileContents.nextLine();
                System.out.println(line);
                if (count % 5 == 1) {
                    System.out.println("1111" + line);
//                    String vin = fileContents.nextLine();
                    vinDecodeNewest.setVin(line);
                } else if (count % 5 == 2) {
                    System.out.println("2222" + line);
                    //解析
                    if (line.length() == 17) {
                        System.out.println("bbbb");
                        vinDecodeNewest.setVin(line);
                        continue;
                    }
                    JSONArray jsonArray = JSONUtil.parseArray(line);
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = (JSONObject) object;
                        if (jsonObject.get("label").equals("Catalog Code")) {
                            vinDecodeNewest.setCatalog_code(String.valueOf(jsonObject.get("data")));
                        } else if (jsonObject.get("label").equals("Production Date")) {
                            vinDecodeNewest.setProduction_date(String.valueOf(jsonObject.get("data")));
                        } else if (jsonObject.get("label").equals("Model Year")) {
                            vinDecodeNewest.setModel_year(String.valueOf(jsonObject.get("data")));
                        } else if (jsonObject.get("label").equals("Model")) {
                            vinDecodeNewest.setModel(String.valueOf(jsonObject.get("data")));
                        } else if (jsonObject.get("label").equals("Country")) {
                            vinDecodeNewest.setCountry(String.valueOf(jsonObject.get("data")));
                        } else if (jsonObject.get("label").equals("Region")) {
                            vinDecodeNewest.setRegion(String.valueOf(jsonObject.get("data")));
                        } else if (jsonObject.get("label").equals("Plant")) {
                            vinDecodeNewest.setPlant(String.valueOf(jsonObject.get("data")));
                        }
                    }
                } else if (count % 5 == 3) {
                    System.out.println("3333" + line);
                    JSONArray exteriorColorJsonArray = JSONUtil.parseArray(line);
//                    for (Object object : interiorColorJsonArray) {
                        vinDecodeNewest.setExterior_key_colour_code(String.valueOf(((JSONObject)exteriorColorJsonArray.get(0)).get("data")));
                        vinDecodeNewest.setExterior_colour_desc(((JSONObject)exteriorColorJsonArray.get(0)).getStr("label"));
                        JSONArray jsonArray = ((JSONObject)exteriorColorJsonArray.get(0)).getJSONArray("subAttributes");
                        if (jsonArray != null) {
                            String subAttributesArr = jsonArray.toJSONString(0);
                            vinDecodeNewest.setExterior_colour_sub_attributes(subAttributesArr);
                        }
//                                subAttributes
//                        vinDecodeNewest.
//                    }
                } else if (count % 5 == 4) {
                    System.out.println("4444" + line);
                    JSONArray interiorColorJsonArray = JSONUtil.parseArray(line);
//                    for (Object object : interiorColorJsonArray) {
                    vinDecodeNewest.setInterior_key_colour_code(String.valueOf(((JSONObject)interiorColorJsonArray.get(0)).get("data")));
                    vinDecodeNewest.setInterior_colour_desc(((JSONObject)interiorColorJsonArray.get(0)).getStr("label"));
                    JSONArray jsonArray = ((JSONObject)interiorColorJsonArray.get(0)).getJSONArray("subAttributes");
                    if (jsonArray != null) {
                        String subAttributesArr = jsonArray.toJSONString(0);
                        vinDecodeNewest.setInterior_colour_sub_attributes(subAttributesArr);
                    }
//                    vinDecodeNewest.setEx
                } else if (count % 5 == 0) {
                    System.out.println("5555" + line);
                    vinDecodeNewest.setSection_applicability(line);
                    vinDecodeNewestList.add(vinDecodeNewest);
                    vinDecodeNewest =  new VinDecodeNewest();
                }
//                if (count % 5 == 0) {
//                    System.out.println(vinDecodeNewestList.get(0).toString());
//                }
                count++;
                if (vinDecodeNewestList.size() == 1000) {
                    vinDecodeNewestMapper.batchInsert(vinDecodeNewestList);
                    vinDecodeNewestList = new ArrayList<>();
                }
            }

            if (vinDecodeNewestList.size() > 0) {
                vinDecodeNewestMapper.batchInsert(vinDecodeNewestList);
                vinDecodeNewestList = new ArrayList<>();
            }
            System.out.println("finished" + file.getName());
        }


        //读取vin
        //读取general
        //读取color
        //读取适用
        //存表
    }


//    @PostMapping("/store-section-parts18")
//    public void insertGetSectionParts18() throws IOException, DocumentException {
////        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
//        //保存sectionParts到数据库
////        getSectionPartsAHMAPCMA06-Part2.txt
////        String path = "E:\\eighteen\\";
//
//        //有一份新的文件
////        String path = "E:\\hpdepc\\getSectionPartsKHMAPDV19-new.txt";
////        System.out.println("kkl");
//
////        File directory = new File(path);
////        File[] files = directory.listFiles();
//
//        // 从原表查询数据
////        for (File file : files) {
////            LineIterator fileContents = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name());
////            String param = "";
////            String parts = "";
////
////            List<ModelSectionPartsTemp1> sectionParts = new ArrayList<>();
////            List<ModelPartOptionNewTemp1> modelPartOptions = new ArrayList<>();
////            ObjectMapper mapper = new ObjectMapper();
////
////            String catalog = "";
////            String section = "";
////
////            //ymm
////            //partnumber+
////
////            //先插一条看看
////            while (fileContents.hasNext()) {
////                String line = fileContents.nextLine();
////                if (!line.startsWith("{")) {
//////                System.out.println("llll");
////                    ModelPartOptionNewTemp1 modelPartOption = new ModelPartOptionNewTemp1();
////                    param = line;
////                    int firstBlankIndex = param.indexOf(" ");
////
////                    int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);
////
////                    catalog = param.substring(0, firstBlankIndex);
////
////                    section = param.substring(firstBlankIndex + 1, secondBlankIndex);
////
////                    String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);
////                    Document text = DocumentHelper.parseText(interpretationData);
////                    Element rootElement = text.getRootElement();
////                    modelPartOption.setCatalogCode(catalog);
////                    modelPartOption.setSectionCode(section);
////                    modelPartOption.setId(id2.incrementAndGet());
////                    List<Element> avsElements = rootElement.elements("avs");
////                    for (Element avsElement : avsElements) {
////                        if (avsElement.attributeValue("type").equals("01")) {
////                            modelPartOption.setType01(avsElement.attributeValue("data"));
////                        } else if (avsElement.attributeValue("type").equals("02")) {
////                            modelPartOption.setType02(avsElement.attributeValue("data"));
////                        } else if (avsElement.attributeValue("type").equals("03")) {
////                            modelPartOption.setType03(avsElement.attributeValue("data"));
////                        } else if (avsElement.attributeValue("type").equals("04")) {
////                            modelPartOption.setType04(avsElement.attributeValue("data"));
////                        } else if (avsElement.attributeValue("type").equals("05")) {
////                            modelPartOption.setType05(avsElement.attributeValue("data"));
////                        } else if (avsElement.attributeValue("type").equals("06")) {
////                            modelPartOption.setType06(avsElement.attributeValue("data"));
////                        } else if (avsElement.attributeValue("type").equals("WT")) {
////                            modelPartOption.setTypeWT(avsElement.attributeValue("data"));
////                        } else if (avsElement.attributeValue("type").equals("DT")) {
////                            modelPartOption.setTypeDT(avsElement.attributeValue("data"));
////                        }
////                    }
////                    modelPartOptions.add(modelPartOption);
////                } else {
//////                System.out.println("oooo");
////
////                    parts = line;
//////                JSONObject jsonObject = JSONUtil.parseObj(parts);
//////                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
////                    JsonNode node = mapper.readTree(parts);
//////                System.out.println(node.toPrettyString());
////
////                    JsonNode catalogNode = node.get("catalogs");
//
////                System.out.println("pppp" + param);
//
////                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);
//
////        Document text = DocumentHelper.parseText(interpretationData);
////        Element rootElement = text.getRootElement();
////        List<Element> elements = rootElement.elements("avs");
////                for (Element element : avsElements) {
////                    System.out.println(element.asXML());
////                }
//
////                    JsonNode partNode = catalogNode.get(0).get("parts");
//
////                    if (partNode.isArray()) {
////                        Iterator<JsonNode> elements = partNode.iterator();
////                        while (elements.hasNext()) {
////                            JsonNode element = elements.next();
////                            SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//////                        System.out.println("sssss:" + sectionPart.toString());
////                            //将sectionParts转为sectionPart,
//////                        ModelSectionPartsTemp1 modelSectionPartsTemp1 = new ModelSectionPartsTemp1();
//////                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
//////                        sectionPart.setCatalogCode(catalog);
//////                        sectionPart.setSectionCode(section);
////                            ModelSectionPartsTemp1 modelSectionPartsTemp1 = ModelSectionPartsTemp1.convert(sectionPartDto);
////                            modelSectionPartsTemp1.setCatalogCode(catalog);
////                            modelSectionPartsTemp1.setSectionCode(section);
//////                        id.get()
////                            modelSectionPartsTemp1.setOptionId(id2.get());
////                            sectionParts.add(modelSectionPartsTemp1);
//
////                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
//
//        //根据type设置
//
////                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
////                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
////                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
////                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
////                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
////                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
////                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
//        int from = 0;
//        int limit = 5000;
//        String catalog = "AHMAPCMA06";
//        System.out.println("sss");
//        List<SectionPartNewDto> sectionPartNewDtos = modelSectionPartsTemp1Mapper.selectAggregateOptionId(catalog, from, limit);
//        System.out.println("eee" + sectionPartNewDtos.size());
//        while (!CollectionUtils.isEmpty(sectionPartNewDtos)) {
//
//            List<ModelSectionPartsTemp1Newest> modelSectionPartsTemp1Newest2s = sectionPartNewDtos.stream().map(e -> ModelSectionPartsTemp1Newest.convert(e)).collect(Collectors.toList());
//
////                            if (sectionParts.size() == 5000 || !elements.hasNext()) {
//            modelSectionPartsTemp1Mapper.batchInsertNewest(modelSectionPartsTemp1Newest2s);
////                            sectionPartsMapper.batchInsert15(sectionParts);
////                            }
//            from += limit;
//            sectionPartNewDtos = modelSectionPartsTemp1Mapper.selectAggregateOptionId(catalog, from, limit);
////                            if (modelPartOptions.size() == 5000 || !elements.hasNext()) {
////                            modelPartOptionMapper.batchInsert15(modelPartOptions);
//            // 即便如此，数据量还是很大
//            // 后续还是要插
////            modelPartOptionNewTemp1Mapper.batchInsert2(modelPartOptions);
//
////                                modelPartOptions = new ArrayList<>();
//        }
//        System.out.println("finished");
//
////                        }
////                    }
////                }
////            }
////            System.out.println("Finished" + file.getName());
////        int result = sectionPartsMapper.batchInsert(sectionParts);
////        }
//    }

    //可以不经过二次处理
    //现在已经在表里的可以处理一下，看看
    //先试一个catalogCode，后面再循环列表
    @PostMapping("/handle-section-parts")
    public void handleSectionPartsByModel1() {

//        Map<SectionPartDto, JsonArray> map = new HashMap<>();
        Map<String, SectionPartsByModelUltimate> map;

//        SectionPartDto
        //需要gc
        Long start;
//        List<SectionPartDto> list1 = sectionPartsMapper.selectPartById(1768);
//        List<SectionPartDto> list2 = sectionPartsMapper.selectPartById(87705);
//        System.out.println(list1.get(0).toString());
//        System.out.println(list1.get(0).hashCode());
//        System.out.println(list2.get(0).toString());
//        System.out.println(list2.get(0).hashCode());
//        System.out.println(list1.get(0).hashCode() == list2.get(0).hashCode());

        //todo 查的时候不要查id, equals的时候不要equals optionId，没有catalog字段
//        catalog = "AHMAPCMA06";

        //TODO 开始注释
        List<String> catalogList = new ArrayList<>();
        //ms的问题
        //kmp算法不用回头找
        //并查集有哪些方法：union、isConnected、findParent。

        //试着去创造好的交流体验
        //新鲜感
        //这段里面没有
        //abcab
        //abcabdddabc
        //察觉到有负面想法，马上停止

        //先执行一个，看执行了多久
//        catalogList.add("HMA250PA00");
//        catalogList.add("KHMAPCA23");
//        catalogList.add("KHMAPCM06");
//        catalogList.add("KHMAPEP06");
//        catalogList.add("KHMAPG216");
//        catalogList.add("KHMAPG921");
//        catalogList.add("KHMAPHG11");
//        catalogList.add("KHMAPJD07");
//        catalogList.add("KHMAPJP22");

//        AHMAPDA22
//                AHMAPNFA04
//        KHMAPA515
//                KHMAPHT20
//        MHMAPJ018
//                NAS2309200
//        catalogList.add("AHMAPDA22");
//        catalogList.add("AHMAPNFA04");
//        catalogList.add("KHMAPA515");
//        catalogList.add("KHMAPHT20");
//        catalogList.add("MHMAPJ018");
//        catalogList.add("NAS2309200");
//        catalogList.add("AHMAPS221");
//        catalogList.add("HMA1E0PA06");
//        catalogList.add("HMA2B0PA06");
//        catalogList.add("HMA2C0PA01");
//        catalogList.add("HMA2D0PA00");
//        catalogList.add("HMA3L0PA05");
//        catalogList.add();
//        catalogList.add("KHMAPBHL13");

//        catalogList.add("KHMAPCA23");
//        catalogList.add("KHMAP1V21");
//        catalogList.add("KHMAPF216");
//        catalogList.add("KHMAPJ921");
//        catalogList.add("KHMAPM518")
//        catalogList.add("KHMAPS819");
//        catalogList.add("KHMAP0A20");
//        catalogList.add("KHMAPBHL08");
//        catalogList.add("KHMAPE617");
//        catalogList.add("KHMAPHG15");
//        catalogList.add("KHMAPTG05");

//        catalogList.add("KHMAPJ918");
//        catalogList.add("KHMAPS822");

//        catalogList.add("KHMAPS819");
//        catalogList.add("AHMAPS218");
//        catalogList.add("AHMAPSDA14");
//        catalogList.add("KHMAPA515");
//        catalogList.add("KHMAPJ317");
//        catalogList.add("NAS2409200");
//        catalogList.add("KHMAPJ317");
//        catalogList.add("KHMAPSD11");
//        catalogList.add("KHMAPB813");
//        catalogList.add("KHMAPTMF12");
//        catalogList.add("NAS2709700");

//        KHMAPBHL13
//        KHMAPGI21
//        KHMAPHD06
//        KHMAPSN19
//        catalogList.add("KHMAPBHL13");
//        catalogList.add("KHMAPGI21");
//        catalogList.add("KHMAPHD06");
//        catalogList.add("KHMAPSN19");
//        KHMAPJ918
//        KHMAPTG05
//        catalogList.add("KHMAPJ918");
//        catalogList.add("KHMAPTG05");
//        catalogList.add("KHMAPD219");
//        catalogList.add("NAS2909600");
//        catalogList.add("KHMAPB114");
//        catalogList.add("KHMAP1V21");

//        catalogList.add("AHMAPC217");
//        catalogList.add("HMA390PA99");
//        KHMAPGK07
//        KHMAPOSE21
        catalogList.add("AHMAPC217");

        for (String catalog : catalogList) {
            start = 0L;
            List<SectionPartDto> sectionParts = sectionPartsMapper.selectPartByModelNew(catalog, start);
            System.out.println("ssss" + sectionParts.size());
//            System.out.println("tttt" + sectionParts.get(0).toString());
            //查询optionId
            //Integer maxId = modelPartOptionNewTemp1Mapper.selectMaxId();
            //AtomicLong id = new AtomicLong(maxId + 1);

            map = new HashMap<>();
//            String specificStr = new String();
            while (!sectionParts.isEmpty()) {
                for (int i = 0; i < sectionParts.size(); i++) {
                    //试一下
                    //为什么会有optionId
                    String str = sectionParts.get(i).toString();

                    //todo 打印
//                    if (sectionParts.get(i).getId() == 126297324) {
//                        System.out.println("aaaa");
//                    }
                    if (map.containsKey(str)) {
//                    System.out.println("mmmm");
                        SectionPartsByModelUltimate value = map.get(str);
                        value.getOptionIdsArr().add(sectionParts.get(i).getOptionId());
                        map.put(str, value);
//                        if (sectionParts.get(i).getId() == 126297324) {
//                            specificStr = str;
//                            System.out.println("bbbb" + value.getOptionIdsArr());
//                            System.out.println(value.toString());
//                        }
//                        if (str.equals(specificStr)) {
//                            System.out.println("gggg");
//                            System.out.println(sectionParts.get(i).toString());
//                            System.out.println(value.toString());
//                        }
                    } else {
                        //optionId
                        SectionPartsByModelUltimate sectionPartsByModelUltimate = SectionPartsByModelUltimate.convert(sectionParts.get(i));
//                        sectionPartsByModelUltimate.getOptionIdsArr().add(sectionParts.get(i).getOptionId());
                        map.put(str, sectionPartsByModelUltimate);
//                        if (sectionParts.get(i).getId() == 126297324) {
//                            System.out.println("cccc" + sectionPartsByModelUltimate.getOptionIdsArr());
//                        }
                    }
                }
                start = sectionParts.get(sectionParts.size() - 1).getId();
                //多维数组
                //两个对象，比如两个string

                sectionParts = sectionPartsMapper.selectPartByModelNew(catalog, start);
//            System.out.println("kkkk" + sectionParts.size());
//            sectionParts = new ArrayList<>();
            }

            //转换，插到数据库
            //批量插入
            //插进去是json字段
            List<SectionPartsByModelUltimate> sectionPartsByModelUltimates = new ArrayList();
            for (Map.Entry<String, SectionPartsByModelUltimate> entry : map.entrySet()) {
//            System.out.println("kkkk");
                SectionPartsByModelUltimate sectionPartsByModelUltimate = entry.getValue();
//                if (entry.getKey().equals(specificStr)) {
//                    System.out.println("uuuu");
//                    System.out.println(sectionPartsByModelUltimate.toString());
//                }
                sectionPartsByModelUltimate.setOptionIds(sectionPartsByModelUltimate.getOptionIdsArr().toString());
                sectionPartsByModelUltimates.add(sectionPartsByModelUltimate);
            }
//        System.out.println(sectionPartsByModelUltimates.get(0).toString());

            //分多次插
            //size正好2000
            //insertStart 2000,不用插

            //size 2001
            //insertStart 2000
            //insertEnd 2001

            //size 4000
            //insertStart 2000
            //insertEnd 4000
            //todo insertStart insertEnd有没有问题
            List<SectionPartsByModelUltimate> subList;
            int insertStart = 0;
            int insertEnd = 2000;
//            System.out.println("size:" + sectionPartsByModelUltimates.size());
            while (insertStart < sectionPartsByModelUltimates.size()) {
//                System.out.println("insertStart:" + insertStart);
                if (insertEnd > sectionPartsByModelUltimates.size()) {
                    insertEnd = sectionPartsByModelUltimates.size();
                }
//                System.out.println("insertEnd:" + insertEnd);
                subList = sectionPartsByModelUltimates.subList(insertStart, insertEnd);
                sectionPartsMapper.batchInsertUltimate(subList);
                insertStart += 2000;
                insertEnd += 2000;
                //会不会边界没写进去
            }
            System.out.println("finished " + catalog + "time " + LocalDateTime.now());

        }
        //todo 结束注释
    }

    @PostMapping("/handle-section-parts2")
    public void handleSectionPartsByModel2() {

//        Map<SectionPartDto, JsonArray> map = new HashMap<>();
        Map<String, SectionPartsByModelUltimate> map;

//        SectionPartDto
        //需要gc
        Long start;
//        List<SectionPartDto> list1 = sectionPartsMapper.selectPartById(1768);
//        List<SectionPartDto> list2 = sectionPartsMapper.selectPartById(87705);
//        System.out.println(list1.get(0).toString());
//        System.out.println(list1.get(0).hashCode());
//        System.out.println(list2.get(0).toString());
//        System.out.println(list2.get(0).hashCode());
//        System.out.println(list1.get(0).hashCode() == list2.get(0).hashCode());

        //todo 查的时候不要查id, equals的时候不要equals optionId，没有catalog字段
//        catalog = "AHMAPCMA06";

        //TODO 开始注释
        List<String> catalogList = new ArrayList<>();

//        catalogList.add("HMA250PA00");
//        catalogList.add("KHMAPCA23");
//        catalogList.add("KHMAPCM06");
//        catalogList.add("KHMAPEP06");
//        catalogList.add("KHMAPG216");
//        catalogList.add("KHMAPG921");
//        catalogList.add("KHMAPHG11");
//        catalogList.add("KHMAPJD07");
//        catalogList.add("KHMAPJP22");

//        catalogList.add("AHMAPHU21");
//        catalogList.add("AHMAPNFA08");
//        catalogList.add("HMA2108700");
//        catalogList.add("KHMAPBB21");
//        catalogList.add("KHMAPG317");
//        catalogList.add("KHMAPG716");
//        catalogList.add("KHMAPG719");
//        catalogList.add("KHMAPGX21");
//        catalogList.add("NAS2809100");

//        catalogList.add("HMA3K0PA04");
//        catalogList.add("HMA4J0PA06");
//        catalogList.add("KHMAPD315");
//        catalogList.add("KHMAPOSE21");

//        catalogList.add("AHMAPIT23");
//        catalogList.add("AHMAPSDA11");
//        catalogList.add("HMA0A0PA04");
//        catalogList.add("KHMAPCA23");
//        catalogList.add("KHMAPJM04");

//        catalogList.add("HMA2H0PA06");
//        catalogList.add("KHMAPGI21");
//        catalogList.add("NAS2909600");

//        catalogList.add("KHMAPA515");

//        catalogList.add("KHMAPCA23");
//        catalogList.add("KHMAPB813");
//        catalogList.add("KHMAPBHL08");
//        catalogList.add("KHMAPD219");
//        catalogList.add("KHMAPG917");
//        catalogList.add("KHMAPTG05");
//        catalogList.add("KHMAPGS11");
//        catalogList.add("AHMAPF319");
//        catalogList.add("KHMAPD219");
//        catalogList.add("KHMAPF219");
//        catalogList.add("AHMAPS218");
//        catalogList.add("HMA0W0PA06");
//        catalogList.add("AHMAPS221");

//        KHMAPB116
//                KHMAPG917
//        catalogList.add("KHMAPB116");
//        catalogList.add("KHMAPG917");
//        catalogList.add("KHMAPB816");
//        catalogList.add("KHMAPGS11");
        catalogList.add("AHMAPC217");

        for (String catalog : catalogList) {
            start = 0L;
            List<SectionPartDto> sectionParts = sectionPartsMapper.selectPartByModelNew2(catalog, start);
            System.out.println("ssss" + sectionParts.size());
//            System.out.println("tttt" + sectionParts.get(0).toString());
            //查询optionId
            //Integer maxId = modelPartOptionNewTemp1Mapper.selectMaxId();
            //AtomicLong id = new AtomicLong(maxId + 1);

            map = new HashMap<>();
            while (!sectionParts.isEmpty()) {
                for (int i = 0; i < sectionParts.size(); i++) {
                    //试一下
                    //为什么会有optionId
                    String str = sectionParts.get(i).toString();
                    if (map.containsKey(str)) {
//                    System.out.println("mmmm");
                        SectionPartsByModelUltimate value = map.get(str);
                        value.getOptionIdsArr().add(sectionParts.get(i).getOptionId());
                        map.put(str, value);
                    } else {
                        //optionId
                        SectionPartsByModelUltimate sectionPartsByModelUltimate = SectionPartsByModelUltimate.convert(sectionParts.get(i));
                        sectionPartsByModelUltimate.getOptionIdsArr().add(sectionParts.get(i).getOptionId());
                        map.put(str, sectionPartsByModelUltimate);
                    }
                }
                start = sectionParts.get(sectionParts.size() - 1).getId();
                sectionParts = sectionPartsMapper.selectPartByModelNew2(catalog, start);
//            System.out.println("kkkk" + sectionParts.size());
//            sectionParts = new ArrayList<>();
            }

            //转换，插到数据库
            //批量插入
            //插进去是json字段
            List<SectionPartsByModelUltimate> sectionPartsByModelUltimates = new ArrayList();
            for (Map.Entry<String, SectionPartsByModelUltimate> entry : map.entrySet()) {
//            System.out.println("kkkk");
                SectionPartsByModelUltimate sectionPartsByModelUltimate = entry.getValue();
                sectionPartsByModelUltimate.setOptionIds(sectionPartsByModelUltimate.getOptionIdsArr().toString());
                sectionPartsByModelUltimates.add(sectionPartsByModelUltimate);
            }
//        System.out.println(sectionPartsByModelUltimates.get(0).toString());
            //分多次插
            int insertStart = 0;
            int insertEnd = 2000;
            //size正好2000
            //insertStart 2000,不用插

            //size 2001
            //insertStart 2000
            //insertEnd 2001

            //size 4000
            //insertStart 2000
            //insertEnd 4000
            List<SectionPartsByModelUltimate> subList;
            while (insertStart < sectionPartsByModelUltimates.size()) {
                if (insertEnd > sectionPartsByModelUltimates.size()) {
                    insertEnd = sectionPartsByModelUltimates.size();
                }
                subList = sectionPartsByModelUltimates.subList(insertStart, insertEnd);
                sectionPartsMapper.batchInsertUltimate(subList);
                insertStart += 2000;
                insertEnd += 2000;
            }
            System.out.println("finished " + catalog + "time " + LocalDateTime.now());

        }
        //todo 结束注释
    }

//    private static File currentFile;
//    private static BufferedWriter writer;
//    private static int fileCount = 1;

    //找一个catalog处理数据
    //再插一遍
    @PostMapping("/store-section-parts-by-vin")
    public void storeSectionPartsByVin() throws IOException, DocumentException {
        //写code

        //读取数据文件
//        String path = "E:\\hpdepc\\partsAndImagesAHMAPDW20-newest-Part1.txt";\
        //报了一个错误
        String path = "E:\\partsAndImagesKHMAPJI21-newest.txt";
        //哪里没插
        //存库

        //范式，可选项组合

        //放可选项

        //找一个小的

        //FileWriter fileWriter = new FileWriter("E:\\hpdepc\\getSectionPartsKHMAPTMF12.txt");
        //保存sectionParts到数据库

        LineIterator fileContents = FileUtils.lineIterator(new File(path), StandardCharsets.UTF_8.name());
        String param = "";
        String parts = "";

        List<SectionPart> sectionParts = new ArrayList<>();
//        List<ModelPartOption> modelPartOptions = new ArrayList<>();

        List<VinPartOptionNewestCopy2> vinPartOptionNewList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        //ymm
        //partnumber+

        //先插一条看看
        String catalog="";
        String section="";
        while (fileContents.hasNext()) {
            String line = fileContents.nextLine();
            if (!line.startsWith("{")) {
//                System.out.println("llll");
                param = line;
                int firstBlankIndex = param.indexOf(" ");

                int secondBlankIndex = param.indexOf(" ", firstBlankIndex + 1);

                catalog = param.substring(0, firstBlankIndex);
                section = param.substring(firstBlankIndex + 1, secondBlankIndex);
                VinPartOptionNewestCopy2 vinPartOptionNew = new VinPartOptionNewestCopy2();
                vinPartOptionNew.setId(id.incrementAndGet());
//                        vinPartOptionNew.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
                vinPartOptionNew.setCatalog(catalog);
                vinPartOptionNew.setSectionCode(section);

                String interpretationData = param.substring(secondBlankIndex + 1, param.length() - 12);
                Document text = DocumentHelper.parseText(interpretationData);
                Element rootElement = text.getRootElement();
                List<Element> avsElements = rootElement.elements("avs");

//                        ModelPartOption modelPartOption = new ModelPartOption();
//                        modelPartOption.setPartNumber(sectionPartDto.getClientIdentificationData().get("partnumber").toString());
//                        modelPartOption.setCatalogCode(catalog);
//                        modelPartOption.setSectionCode(section);

                vinPartOptionNew.setOptionCodes(rootElement.attributeValue("optioncodes"));

                vinPartOptionNew.setExteriorKeyColorCode(rootElement.attributeValue("exteriorkeycolorcode"));

                vinPartOptionNew.setInteriorKeyColorCode(rootElement.attributeValue("interiorkeycolorcode"));

                vinPartOptionNew.setPlant(rootElement.attributeValue("plant"));

                vinPartOptionNew.setModelCode(rootElement.attributeValue("modelcode"));

                vinPartOptionNew.setBuildDate(rootElement.attributeValue("builddate"));

                vinPartOptionNew.setArea(rootElement.attributeValue("area"));

                vinPartOptionNew.setFullSpecificationCode(rootElement.attributeValue("fullSpecificationCode"));

                vinPartOptionNew.setVinTag(rootElement.attributeValue("vintag"));

                vinPartOptionNew.setEngineMipCode(rootElement.attributeValue("enginemipcode"));

                vinPartOptionNew.setTransmissionMipCode(rootElement.attributeValue("transmissionMipCode"));
//                try with resource finally 什么时候异常会中断，什么时候不会
//                中规中矩就好了

//                        vinPartOptionNew.setType(rootElement.attributeValue("type"));

//                        vinPartOptionNew.setLevel(rootElement.attributeValue("level"));

                //根据type设置参数。
                for (Element avsElement : avsElements) {
                    if (avsElement.attributeValue("type").equals("01")) {
                        vinPartOptionNew.setType01(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("02")) {
                        vinPartOptionNew.setType02(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("03")) {
                        vinPartOptionNew.setType03(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("04")) {
                        vinPartOptionNew.setType04(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("05")) {
                        vinPartOptionNew.setType05(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("06")) {
                        vinPartOptionNew.setType06(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("WT")) {
                        vinPartOptionNew.setTypeWT(avsElement.attributeValue("data"));
                    } else if (avsElement.attributeValue("type").equals("DT")) {
                        vinPartOptionNew.setTypeDT(avsElement.attributeValue("data"));
                    }
                }
//                        System.out.println(vinPartOptionNew);
//                        modelPartOption.setType01(avsElements.size() >= 1 ? avsElements.get(0).attributeValue("data") : "");
//                        modelPartOption.setType02(avsElements.size() >= 2 ? avsElements.get(1).attributeValue("data") : "");
//                        modelPartOption.setType03(avsElements.size() >= 3 ? avsElements.get(2).attributeValue("data") : "");
//                        modelPartOption.setType04(avsElements.size() >= 4 ? avsElements.get(3).attributeValue("data") : "");
//                        modelPartOption.setType05(avsElements.size() >= 5 ? avsElements.get(4).attributeValue("data") : "");
//                        modelPartOption.setTypeWT(avsElements.size() >= 6 ? avsElements.get(5).attributeValue("data") : "");
//                        modelPartOption.setTypeDT(avsElements.size() >= 7 ? avsElements.get(6).attributeValue("data") : "");
                vinPartOptionNewList.add(vinPartOptionNew);
//                id.getAndIncrement();
            } else {
//                System.out.println("oooo");
                parts = line;
//                JSONObject jsonObject = JSONUtil.parseObj(parts);
//                SectionPart sectionPart = JSONObject.(jsonObject, SectionPart.class);
                JsonNode node = mapper.readTree(parts);
//                System.out.println(node.toPrettyString());

                JsonNode catalogNode = node.get("catalogs");

//                System.out.println("pppp" + param);


//                JSONObject interpretationDataJSONObject = XML.toJSONObject(interpretationData);

//        Document text = DocumentHelper.parseText(interpretationData);
//        Element rootElement = text.getRootElement();
//        List<Element> elements = rootElement.elements("avs");
//                for (Element element : avsElements) {
//                    System.out.println(element.asXML());
//                }

                JsonNode partNode = catalogNode.get(0).get("parts");

                if (partNode.isArray()) {
                    Iterator<JsonNode> elements = partNode.iterator();
                    while (elements.hasNext()) {
                        JsonNode element = elements.next();
                        SectionPartDto sectionPartDto = mapper.convertValue(element, SectionPartDto.class);
//                        System.out.println("sssss:" + sectionPart.toString());
                        //将sectionParts转为sectionPart,
                        SectionPart sectionPart = SectionPart.convert(sectionPartDto);
                        sectionPart.setCatalogCode(catalog);
                        sectionPart.setSectionCode(section);
//                        sectionPart.setOptionId(id.get());

//                        System.out.println(sectionParts);
                        //根据某些字段聚合
                        sectionParts.add(sectionPart);

                        //最后几组没插进去

                        //不要自增，程序里面设置一个id
                        if (sectionParts.size() == 5000 || !elements.hasNext()) {
                            sectionPartsMapper.batchInsert8(sectionParts);
                            sectionParts = new ArrayList<>();
                        }

//                        VinPartOptionNewestCopy2
                        if (vinPartOptionNewList.size() == 5000 || !elements.hasNext()) {
                            vinPartOptionNewestCopy2Mapper.batchInsert3(vinPartOptionNewList);
                            vinPartOptionNewList = new ArrayList<>();
                        }
                    }
                }
            }
        }

        System.out.println("FinishedNew");
        //插到数据库里面，然后导入到es，怎么导，之前怎么导的？有一个java接口。
//        int result = sectionPartsMapper.batchInsert(sectionParts);
    }

    //todo 生成一下新的by-vin参数
//    @PostMapping("/store-get-section-parts-param2")
//    public void storeGetSectionPartsParam2() throws IOException {
//
////        JsonParser parser = JsonParser.parseString();
////        获取catalog fromyear toyear
////        QueryWrapper catalogQuery = Wrappers.query();
////        catalogQuery.select("catalogCode, catalogDataTypeDescription, fromYear, toYear");
////        ObjectMapper mapper = new ObjectMapper();
//        List<Catalog> catalogList = catalogMapper.selectAll();
//        Map<String, String> catalogTypeMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogCode, Catalog::getCatalogDataTypeDescription));
//
////        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\VinSectionPartParamsNewest", true);
////        catalog开始年份
////        改为lambda后不再npe
////        Map<String, String> beginYearMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogcode, e -> e.getFromyear()));
////
////        catalog结束年份
////        Map<String, String> endYearMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogcode, e -> e.getToyear()));
////
////        QueryWrapper sectionQuery = Wrappers.query();
//
////        选一条报错
////        直接从文件读取
////        sectionQuery.select("catalog_code, client_catalog_information");
////        sectionQuery.ge("id", 1);
////        sectionQuery.le("id", 41);
////        sectionQuery.eq("catalog_code", "AHMAP0B21");
////        sectionQuery.ne("catalog_code", "HREPAIRKIT");
//
////        List<FlatSections> sectionList = flatSectionsMapper.selectAllByCatalogCode(null);
////        System.out.println("hhhh" + sectionList.size());
////        参数
////        List<String> parameters = new ArrayList<>();
//
//        //oom
////        List<VinData> vinDatas = vinDataMapper.selectByCatalog("AHMAPHU21");
////        System.out.println(9967);
//
//        //catalog
//        //category
//        //subcategory
//        //part
//        //option
//
////        KHMAPTMF12
//
////        AHMAPGFA10
////        KHMAPB116
////        KHMAPB816
//
////        KHMAPBB21
////        KHMAPD318
//        List<String> catalogCodes = new ArrayList<>();
////        -- KHMAPCM06
////                -- KHMAPD216
////                -- KHMAPD219
////                -- KHMAPD315
////                -- KHMAPD318
////                -- KHMAPDK08
////                -- KHMAPDK12
////                -- KHMAPDV19
////                -- KHMAPE616
////                -- KHMAPE617
////                -- KHMAPEN07
////                -- KHMAPEP06
////                -- KHMAPF216
////                -- KHMAPF219
////                -- KHMAPG317
////                -- KHMAPG716
////                -- KHMAPG719
////                -- KHMAPG917
////                -- KHMAPG921
////                -- KHMAPGFE11
////                -- KHMAPGI21
////                -- KHMAPGK07
////                -- KHMAPGS11
////                -- KHMAPGS15
////                -- KHMAPGW21
////                -- KHMAPHD06
////                -- KHMAPHG11
////                -- KHMAPHG15
////                -- KHMAPI321
////                -- KHMAPJ317
////                -- KHMAPJ918
////                -- KHMAPJ921
////                -- KHMAPJD07
////                -- KHMAPJI21
////                -- KHMAPJJ20
////                -- KHMAPJM04
////                -- KHMAPJP22
////                -- KHMAPJR20
////                -- KHMAPM518
////                -- KHMAPMC06
////                -- KHMAPOSE19
////                -- KHMAPOSE21
////        catalogCodes.add("KHMAPCM06");
////        catalogCodes.add("KHMAPD216");
////        catalogCodes.add("KHMAPD219");
////        catalogCodes.add("KHMAPD315");
////        catalogCodes.add("KHMAPD318");
////        catalogCodes.add("KHMAPDK08");
////        catalogCodes.add("KHMAPDK12");
////        catalogCodes.add("KHMAPDV19");
////        catalogCodes.add("KHMAPE616");
////        catalogCodes.add("KHMAPE617");
////        catalogCodes.add("KHMAPEN07");
////        catalogCodes.add("KHMAPEP06");
////        catalogCodes.add("KHMAPF216");
////        catalogCodes.add("KHMAPF219");
////        catalogCodes.add("KHMAPG317");
////        catalogCodes.add("KHMAPG716");
////        catalogCodes.add("KHMAPG719");
////        catalogCodes.add("KHMAPG917");
////        catalogCodes.add("KHMAPG921");
////        catalogCodes.add("KHMAPGFE11");
////        catalogCodes.add("KHMAPGI21");
////        catalogCodes.add("KHMAPGK07");
////        catalogCodes.add("KHMAPGS11");
////        catalogCodes.add("KHMAPGS15");
////        catalogCodes.add("KHMAPGW21");
////        catalogCodes.add("KHMAPHD06");
////        catalogCodes.add("KHMAPHG11");
////        catalogCodes.add("KHMAPHG15");
////        catalogCodes.add("KHMAPI321");
////        catalogCodes.add("KHMAPJ317");
////        catalogCodes.add("KHMAPJ918");
////        catalogCodes.add("KHMAPJ921");
////        catalogCodes.add("KHMAPJD07");
////        catalogCodes.add("KHMAPJI21");
////        catalogCodes.add("KHMAPJJ20");
////        catalogCodes.add("KHMAPJM04");
////        catalogCodes.add("KHMAPJP22");
////        catalogCodes.add("KHMAPJR20");
////        catalogCodes.add("KHMAPM518");
////        catalogCodes.add("KHMAPMC06");
////        catalogCodes.add("KHMAPOSE19");
////        catalogCodes.add("KHMAPOSE21");
//
////        catalogCodes.add("KHMAPS819");
////        catalogCodes.add("KHMAPS822");
////        catalogCodes.add("KHMAPSB11");
////        catalogCodes.add("KHMAPSB15");
////        catalogCodes.add("KHMAPSD11");
////        catalogCodes.add("KHMAPSD14");
////        catalogCodes.add("KHMAPSN19");
////        catalogCodes.add("KHMAPTG05");
////        catalogCodes.add("KHMAPTM10");
////        catalogCodes.add("KHMAPTM14");
////        catalogCodes.add("KHMAPTMF12");
////        catalogCodes.add("MHMAPJ018");
//
////        catalogCodes.add("NAS2209500");
////        catalogCodes.add("NAS2709700");
////        catalogCodes.add("NAS2909600");
////        catalogCodes.add("NAS3409500");
////        catalogCodes.add("NAS3809900");
////        http://localhost:8088/store-get-section-parts-param2
////        catalogCodes.add("KHMAPHG11");
////        catalogCodes.add("KHMAPSC20");
////        HMA2B0PA06
////        KHMAPBHL08
//
////        KHMAPGFE11
//
////        catalogCodes.add("KHMAPGFE11");
//
////        catalogCodes.add("HMA2B0PA06");
//
//        //发送方的带宽和接收方的带宽
//        //形成一份模型，
//
//        //找一份数据量比较少的跑跑
//
//        for (String catalogCode : catalogCodes) {
//            List<EpcSection> epcSections = epcSectionMapper.selectByCatalogcodeAndApplicableAndParentuniqueid(catalogCode, "1", null);
//
//            StringBuilder sb;
//
////        StringBuilder optionStringBuilder;
//            //包含sectionCode、catalogCode和interpretation
//            //获取section
//
//            int start = 0;
//            //所以就第一个partandimages是好的
//            StopWatch stopWatch = new StopWatch();
//            List<VinDataDto> vinDataDtos = vinDataMapper.selectByCatalog2(catalogCode, start);
//
//            System.out.println("search:" + stopWatch.getTotalTimeSeconds());
////        System.out.println("ssss" + vinDataDtos.size());
////        System.out.println(catalogCode);
////        System.out.println("sssss" + vinDataDtos.get(0).getFullSpecificationCode());
//            //todo 从这里开始注释
//            initializeNewFile(catalogCode);
//            //todo 取消注释
//                //怎样使查出来的结果少，搞一个阈值，但要防止把正确结果过滤掉
//            //循环请求
//            while (vinDataDtos.size() > 0) {
////            initializeNewFile();
//                for (VinDataDto vinDataDto : vinDataDtos) {
//                    //正确的多，错误的少，过于模糊，自圆其说
//                    //错误
//
////            System.out.println("vvv" + vinDataDto.toString());
////            System.out.println("vvvv" + vinData.getVin());
////            获取catalogcode
////            FlatSections item = sectionList.get(0);
////            rawElementList.clear();
////            elementList.clear();
////            chooseMap.clear();
//
////            System.out.println("pppp" + catalogCode);
//
////            ArrayNode clientCatalogInformation = item.getClientCatalogInformation().withArray("clientCatalogAttributes");
////            String clientCatalogInformation = item.getClientCatalogInformation();
////            String clientSectionList = item.getClientSectionList();
////            JsonNode sectionNode = mapper.readTree(clientSectionList);
////            JsonNode sections = sectionNode.get("sections");
////            JsonNode childrenIndex = sections.get("childrenIndex");
////            Set<String> sectionSet = new HashSet<>();
////            if (sections.isArray()) {
////                for (JsonNode objnode : sections) {
////                    if (objnode.get("childrenIndex").size() == 0) {
////                        sectionSet.add(objnode.get("code").toString().substring(1, objnode.get("code").toString().length() - 1));
////                    }
////                }
////            }
//
////            JsonNode sectionNode = item.get("clientSectionList").get("sections");
////            sectionNode.get("childrenIndex");
//
////        JsonNode clientCatalogInformationNode = mapper.readTree(clientCatalogInformation);
////        ArrayNode arrNode = clientCatalogInformationNode.withArray("clientCatalogAttributes");
////        List<OptionBrief> optionAll = new ArrayList<>();
//
////        if (arrNode.isArray()) {
////                Map<String, List<String>> optionMap = new HashMap<>();
////
////            Iterator<JsonNode> it = arrNode.iterator();
//
////            遍历所有可选项类型
////            先获取第一个
////            while (it.hasNext()) {
////                JsonNode optionInfoNode = it.next();
////                String optionType = optionInfoNode.get("data").toString();
////                JsonNode options = optionInfoNode.get("options");
////                Iterator<JsonNode> optionIt = options.iterator();
////                List<OptionBrief> optionData = new ArrayList<>();
////                //遍历当前类型的option，存到数组里
////                while (optionIt.hasNext()) {
////                    JsonNode optionNode = optionIt.next();
////                    String data = optionNode.get("data").toString();
////                    OptionBrief optionBrief = new OptionBrief(optionType.replace("\"", ""), data.replace("\"", ""));
////                    optionData.add(optionBrief);
////                }
//////                    optionMap.put(optionType, optionData);
////
////                optionAll.addAll(optionData);
//
//
////                List<Element> element = new ArrayList<>();
//
////            attribute顺序有么有影响
////
////            从集合A中取一个元素(可不取)，集合B中取一个元素，...，返回所有结果
////            每次循环之前都要将当前集合复制一份
////            组合，所有子集，回溯，第一个取/不取，比组合更复杂，因为取的话还涉及到取哪个。
////            所有子集有个index
////
////            拆出去
////            可以
////                    if (optionMap.get(catalogCode) != null) {
////                        List<Element> curElement = new ArrayList<>(element);
//////                        curElement.add();
////                        for (Map.Entry<String, List<String>> entry : optionMap.entrySet()) {
//////                            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
////                            List<String> values = entry.getValue();
////                            for () {
////
////                            }
////                        }
////                    }
////
////
////                    public void recurse(List<Element> result, int index1, int index2, List<List<OptionBrief>> option)
////                    recurse(new ArrayList<>(), 0, 0, optionBriefListAll);
////                }
////            }
//                    List<Modelfileinfo> modelFileInfos = modelfileinfoMapper.selectAllByCataloguecode(catalogCode);
//
//                    String modelCode = generateModelCode(catalogCode, vinDataDto.getFullSpecificationCode());
//                    for (Modelfileinfo modelfileinfo : modelFileInfos) {
//                        if (vinDataDto.getFullSpecificationCode().contains(modelfileinfo.getModelCode())) {
//                            modelCode = modelfileinfo.getModelCode();
//                        }
//                    }
//
//                    //todo 开始
//                    Element rootElement = DocumentHelper.createElement("interpretation");
//                    rootElement.addAttribute("catalog", catalogCode);
//                    //todo 不同catalog不一样
////                    rootElement.addAttribute("modelcode", vinDataDto.getFullSpecificationCode().substring(4, 13));
////                    rootElement.addAttribute("modelcode", vinDataDto.getFullSpecificationCode().substring(5, 11));
//                    rootElement.addAttribute("modelcode", modelCode);
//                    rootElement.addAttribute("builddate", vinDataDto.getProductionDate());
//                    rootElement.addAttribute("area", vinDataDto.getArea());
//                    //todo 不传vin试试
////            rootElement.addAttribute("vin", );
////            rootElement.addAttribute("displaybuilddate", vinData.getProductionDate());
//                    rootElement.addAttribute("fullSpecificationCode", vinDataDto.getFullSpecificationCode());
//
//                    String optioncode1 = vinDataDto.getOptionCode1();
////                System.out.println("oooccc1" + optioncode1);
//                    String optioncode2 = vinDataDto.getOptionCode2();
////                System.out.println("oooccc2" + optioncode2);
////                if (!optioncode1.isEmpty()) {
////                    optionStringBuilder = new StringBuilder();
////                    int index = 5;
////                    optioncode1 += optioncode2;
////                    optionStringBuilder.append(optioncode1.substring(0, 5));
////                    index++;
////                    while (index + 6 <= optioncode1.length()) {
////                        optionStringBuilder.append("|").append(optioncode1.substring(index, index + 6));
////                        index += 6;
////                    }
////                    String optionStr = optionStringBuilder.toString();
////                    //optioncodes排列也不一样
////                    rootElement.addAttribute("optioncodes", optionStr);
////                }
//                    String optionStr = generateOptionCode(catalogCode, optioncode1, optioncode2);
//                    rootElement.addAttribute("optioncodes", optionStr);
//                    rootElement.addAttribute("builddate", vinDataDto.getProductionDate());
//                    rootElement.addAttribute("exteriorkeycolorcode", vinDataDto.getExtColor());
//                    rootElement.addAttribute("interiorkeycolorcode", vinDataDto.getIntColor());
//                    rootElement.addAttribute("plant", vinDataDto.getArea());
//                    rootElement.addAttribute("vintag", vinDataDto.getVinTag());
//                    rootElement.addAttribute("enginemipcode", vinDataDto.getEngineMipCode());
//                    rootElement.addAttribute("transmissionMipCode", vinDataDto.getTransmissionMipCode());
//                    rootElement.addAttribute("weathertype", vinDataDto.getWeatherType());
////            rootElement.addAttribute("enginenumber", vinData.getEngineNumber());
//                    rootElement.addAttribute("type", catalogTypeMap.get(catalogCode));
////            rootElement.addAttribute();
//
//                    //vin、modelid、color
//                    rootElement.addAttribute("level", "2");
//
//                    //todo 结束
//                    //正确的生成方式是调用vin
//                    //不同catalog,可选项的截取规则不同
//                    //vin对应的可选项
//                    //根据catalogCode、fullSpecificationCode可以找到对应的对应的可选项
//                    //下面这一块要改改
//                    //抽一个生成可选项的方法generateOption
//
//                    List<Modelfileinfo> modelfileinfos = modelfileinfoMapper.selectAllByCataloguecode(catalogCode);
//                    Modelfileinfo modelfileinfo = null;
//
//                    for (Modelfileinfo item : modelfileinfos) {
//                        if (item.getM1().equals(vinDataDto.getPlant()) && item.getModelCode().equals(modelCode)) {
//                            modelfileinfo = item;
//                        }
//                    }
//
////                    String catalogCode, String weatherType, String driveType, String fullSpecificationCode, Modelfileinfo modelfileinfo
//                    MajorAttributes majorAttributes = generateMajorAttributes(catalogCode, vinDataDto.getFullSpecificationCode(), vinDataDto.getWeatherType(), vinDataDto.getDriveType(), modelfileinfo);
//
//                    Element avsElement = rootElement.addElement("avs");
//                    avsElement.addAttribute("data", majorAttributes.getType01());
//                    avsElement.addAttribute("type", "01");
//
//                    Element avsElement2 = rootElement.addElement("avs");
//                    avsElement2.addAttribute("data", majorAttributes.getType02());
//                    avsElement2.addAttribute("type", "02");
//
//                    Element avsElement3 = rootElement.addElement("avs");
//                    avsElement3.addAttribute("data", majorAttributes.getType03());
//                    avsElement3.addAttribute("type", "03");
//
//                    Element avsElement4 = rootElement.addElement("avs");
//                    avsElement4.addAttribute("data", majorAttributes.getType04());
//                    avsElement4.addAttribute("type", "04");
//
//                    Element avsElement5 = rootElement.addElement("avs");
//                    avsElement5.addAttribute("data", majorAttributes.getType05());
//                    avsElement5.addAttribute("type", "05");
//
//                    Element avsElement6 = rootElement.addElement("avs");
//                    avsElement6.addAttribute("data", majorAttributes.getType06());
//                    avsElement6.addAttribute("type", "06");
//
////              todo 参数文件记得带上type06
//                    Element avsElement7 = rootElement.addElement("avs");
//                    avsElement7.addAttribute("data", majorAttributes.getTypeDT());
//                    avsElement7.addAttribute("type", "DT");
//
//                    Element avsElement8 = rootElement.addElement("avs");
//                    avsElement8.addAttribute("data", majorAttributes.getTypeWT());
//                    avsElement8.addAttribute("type", "WT");
//
////                List<String> sectionList = new ArrayList<>();
//                    sb = new StringBuilder();
//                    JSONArray arr = new JSONArray();
//                    for (EpcSection epcSection : epcSections) {
////                System.out.println(epcSection.getCode());
//
//                        //todo 开始注释
////                    sectionList.add(epcSection.getCode());
//                        arr.add(epcSection.getCode());
//                        // writeToFile(sb.toString(), catalogCode);
//                        //todo 取消注释
//
//                    }
//
//                    sb.append(catalogCode).append(" ").append(arr).append(" ").append(rootElement.asXML()).append("\n");
//                    stopWatch.start("写入耗时");
//                    writeToFile(sb.toString(), catalogCode);
//                    stopWatch.stop();
////                rootElement.addAttribute("isDefaultApplicability", "0");
////                rootElement.addAttribute("attributeValidationRequired", "0");
////                elementList.add(rootElement);
////            rawElementList.add(rootElement);
//
////            String a = "";
//
//
////                if (beginYearMap.containsKey(catalogCode)) {
////                    Integer endYear = "".equals(endYearMap.get(catalogCode)) ? 2025 : Integer.valueOf(endYearMap.get(catalogCode));
////                    List<String> dateList = generateDateParam(Integer.parseInt(beginYearMap.get(catalogCode)), endYear);
////                    //添加element
////                    for (String date : dateList) {
////                        Element element = rootElement.createCopy();
////                        element.addAttribute("builddate", date);
////                        element.addAttribute("displaybuilddate", date);
////                        rawElementList.add(element);
////                    }
////                }
//
////            某个时刻elementlist为空
////            for (Element element : rawElementList) {
////                recurse(element, 0, optionAll);
////            }
////            elementList.add(rootElement);
//
//                    //缺少sectioncode
//
////            for (Element element : elementList) {
////                for (String sectionCode : sectionSet) {
////                    String row = catalogCode + " " + sectionCode + " " + element.asXML() + " " + 0 + " " + 1;
////                    fileWriter.println(row);
////                }
////            }
//
//                }
//                start = vinDataDtos.get(vinDataDtos.size() - 1).getRowNum();
////            System.out.println("aaa" + start);
//                stopWatch.start("查询耗时");
//                vinDataDtos = vinDataMapper.selectByCatalog2(catalogCode, start);
//                stopWatch.stop();
////            System.out.println("bbb" + vinDataDtos.size());
////            if (vinDataDtos.size() > 0) {
////                System.out.println("kkk" + vinDataDtos.get(vinDataDtos.size() - 1).toString());
////            }
//            }
//
//            closeWriter();
//            System.out.println(stopWatch.prettyPrint());
//            //todo 结束注释
//            //允许有一定的延迟
//            System.out.println("Finished:" + catalogCode);
//        }
//    }

    @GetMapping("/store-get-section-parts-param2")
    public void storeGetSectionPartsParam2(String catalog) throws IOException {

//        JsonParser parser = JsonParser.parseString();
//        获取catalog fromyear toyear
//        QueryWrapper catalogQuery = Wrappers.query();
//        catalogQuery.select("catalogCode, catalogDataTypeDescription, fromYear, toYear");
//        ObjectMapper mapper = new ObjectMapper();
        List<Catalog> catalogList = catalogMapper.selectAll();
        Map<String, String> catalogTypeMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogCode, Catalog::getCatalogDataTypeDescription));

//        FileWriter fileWriter = new FileWriter("E:\\hpdepc\\VinSectionPartParamsNewest", true);
//        catalog开始年份
//        改为lambda后不再npe
//        Map<String, String> beginYearMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogcode, e -> e.getFromyear()));
//
//        catalog结束年份
//        Map<String, String> endYearMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogcode, e -> e.getToyear()));
//
//        QueryWrapper sectionQuery = Wrappers.query();

//        选一条报错
//        直接从文件读取
//        sectionQuery.select("catalog_code, client_catalog_information");
//        sectionQuery.ge("id", 1);
//        sectionQuery.le("id", 41);
//        sectionQuery.eq("catalog_code", "AHMAP0B21");
//        sectionQuery.ne("catalog_code", "HREPAIRKIT");

//        List<FlatSections> sectionList = flatSectionsMapper.selectAllByCatalogCode(null);
//        System.out.println("hhhh" + sectionList.size());
//        参数
//        List<String> parameters = new ArrayList<>();

        //oom
//        List<VinData> vinDatas = vinDataMapper.selectByCatalog("AHMAPHU21");
//        System.out.println(9967);

        //catalog
        //category
        //subcategory
        //part
        //option

//        KHMAPTMF12

//        AHMAPGFA10
//        KHMAPB116
//        KHMAPB816

//        KHMAPBB21
//        KHMAPD318
        List<String> catalogCodes = new ArrayList<>();
//        -- KHMAPCM06
//                -- KHMAPD216
//                -- KHMAPD219
//                -- KHMAPD315
//                -- KHMAPD318
//                -- KHMAPDK08
//                -- KHMAPDK12
//                -- KHMAPDV19
//                -- KHMAPE616
//                -- KHMAPE617
//                -- KHMAPEN07
//                -- KHMAPEP06
//                -- KHMAPF216
//                -- KHMAPF219
//                -- KHMAPG317
//                -- KHMAPG716
//                -- KHMAPG719
//                -- KHMAPG917
//                -- KHMAPG921
//                -- KHMAPGFE11
//                -- KHMAPGI21
//                -- KHMAPGK07
//                -- KHMAPGS11
//                -- KHMAPGS15
//                -- KHMAPGW21
//                -- KHMAPHD06
//                -- KHMAPHG11
//                -- KHMAPHG15
//                -- KHMAPI321
//                -- KHMAPJ317
//                -- KHMAPJ918
//                -- KHMAPJ921
//                -- KHMAPJD07
//                -- KHMAPJI21
//                -- KHMAPJJ20
//                -- KHMAPJM04
//                -- KHMAPJP22
//                -- KHMAPJR20
//                -- KHMAPM518
//                -- KHMAPMC06
//                -- KHMAPOSE19
//                -- KHMAPOSE21
//        catalogCodes.add("KHMAPCM06");
//        catalogCodes.add("KHMAPD216");
//        catalogCodes.add("KHMAPD219");
//        catalogCodes.add("KHMAPD315");
//        catalogCodes.add("KHMAPD318");
//        catalogCodes.add("KHMAPDK08");
//        catalogCodes.add("KHMAPDK12");
//        catalogCodes.add("KHMAPDV19");
//        catalogCodes.add("KHMAPE616");
//        catalogCodes.add("KHMAPE617");
//        catalogCodes.add("KHMAPEN07");
//        catalogCodes.add("KHMAPEP06");
//        catalogCodes.add("KHMAPF216");
//        catalogCodes.add("KHMAPF219");
//        catalogCodes.add("KHMAPG317");
//        catalogCodes.add("KHMAPG716");
//        catalogCodes.add("KHMAPG719");
//        catalogCodes.add("KHMAPG917");
//        catalogCodes.add("KHMAPG921");
//        catalogCodes.add("KHMAPGFE11");
//        catalogCodes.add("KHMAPGI21");
//        catalogCodes.add("KHMAPGK07");
//        catalogCodes.add("KHMAPGS11");
//        catalogCodes.add("KHMAPGS15");
//        catalogCodes.add("KHMAPGW21");
//        catalogCodes.add("KHMAPHD06");
//        catalogCodes.add("KHMAPHG11");
//        catalogCodes.add("KHMAPHG15");
//        catalogCodes.add("KHMAPI321");
//        catalogCodes.add("KHMAPJ317");
//        catalogCodes.add("KHMAPJ918");
//        catalogCodes.add("KHMAPJ921");
//        catalogCodes.add("KHMAPJD07");
//        catalogCodes.add("KHMAPJI21");
//        catalogCodes.add("KHMAPJJ20");
//        catalogCodes.add("KHMAPJM04");
//        catalogCodes.add("KHMAPJP22");
//        catalogCodes.add("KHMAPJR20");
//        catalogCodes.add("KHMAPM518");
//        catalogCodes.add("KHMAPMC06");
//        catalogCodes.add("KHMAPOSE19");
//        catalogCodes.add("KHMAPOSE21");

//        catalogCodes.add("KHMAPS819");
//        catalogCodes.add("KHMAPS822");
//        catalogCodes.add("KHMAPSB11");
//        catalogCodes.add("KHMAPSB15");
//        catalogCodes.add("KHMAPSD11");
//        catalogCodes.add("KHMAPSD14");
//        catalogCodes.add("KHMAPSN19");
//        catalogCodes.add("KHMAPTG05");
//        catalogCodes.add("KHMAPTM10");
//        catalogCodes.add("KHMAPTM14");
//        catalogCodes.add("KHMAPTMF12");
//        catalogCodes.add("MHMAPJ018");

//        catalogCodes.add("NAS2209500");
//        catalogCodes.add("NAS2709700");
//        catalogCodes.add("NAS2909600");
//        catalogCodes.add("NAS3409500");
//        catalogCodes.add("NAS3809900");
//        http://localhost:8088/store-get-section-parts-param2
//        catalogCodes.add("KHMAPHG11");
//        catalogCodes.add("KHMAPSC20");
//        HMA2B0PA06
//        KHMAPBHL08

//        KHMAPGFE11

//        catalogCodes.add("KHMAPGFE11");

//        catalogCodes.add("HMA2B0PA06");
//        catalogCodes.addAll(catalogCodes);

        //发送方的带宽和接收方的带宽
        //形成一份模型，

        //找一份数据量比较少的跑跑
        catalogCodes.add(catalog);

        for (String catalogCode : catalogCodes) {
            System.out.println(catalogCode);
            List<EpcSection> epcSections = epcSectionMapper.selectByCatalogcodeAndApplicableAndParentuniqueid(catalogCode, "1", null);

            StringBuilder sb;

//        StringBuilder optionStringBuilder;
            //包含sectionCode、catalogCode和interpretation
            //获取section

            int start = 0;
            //所以就第一个partandimages是好的
            StopWatch stopWatch = new StopWatch();
            List<VinDataDto> vinDataDtos = vinDataMapper.selectByCatalog2(catalogCode, start);

            System.out.println("search:" + stopWatch.getTotalTimeSeconds());
//        System.out.println("ssss" + vinDataDtos.size());
//        System.out.println(catalogCode);
//        System.out.println("sssss" + vinDataDtos.get(0).getFullSpecificationCode());
            //todo 从这里开始注释
            initializeNewFile(catalogCode);
            //todo 取消注释
            //怎样使查出来的结果少，搞一个阈值，但要防止把正确结果过滤掉
            //循环请求
            while (vinDataDtos.size() > 0) {
//            initializeNewFile();
                for (VinDataDto vinDataDto : vinDataDtos) {
//                    if (vinDataDto.getFullSpecificationCode().equals("") && vinDataDto.getBuildDate()) {
//
//                    }
                    //正确的多，错误的少，过于模糊，自圆其说
                    //错误

//            System.out.println("vvv" + vinDataDto.toString());
//            System.out.println("vvvv" + vinData.getVin());
//            获取catalogcode
//            FlatSections item = sectionList.get(0);
//            rawElementList.clear();
//            elementList.clear();
//            chooseMap.clear();

//            System.out.println("pppp" + catalogCode);

//            ArrayNode clientCatalogInformation = item.getClientCatalogInformation().withArray("clientCatalogAttributes");
//            String clientCatalogInformation = item.getClientCatalogInformation();
//            String clientSectionList = item.getClientSectionList();
//            JsonNode sectionNode = mapper.readTree(clientSectionList);
//            JsonNode sections = sectionNode.get("sections");
//            JsonNode childrenIndex = sections.get("childrenIndex");
//            Set<String> sectionSet = new HashSet<>();
//            if (sections.isArray()) {
//                for (JsonNode objnode : sections) {
//                    if (objnode.get("childrenIndex").size() == 0) {
//                        sectionSet.add(objnode.get("code").toString().substring(1, objnode.get("code").toString().length() - 1));
//                    }
//                }
//            }

//            JsonNode sectionNode = item.get("clientSectionList").get("sections");
//            sectionNode.get("childrenIndex");

//        JsonNode clientCatalogInformationNode = mapper.readTree(clientCatalogInformation);
//        ArrayNode arrNode = clientCatalogInformationNode.withArray("clientCatalogAttributes");
//        List<OptionBrief> optionAll = new ArrayList<>();

//        if (arrNode.isArray()) {
//                Map<String, List<String>> optionMap = new HashMap<>();
//
//            Iterator<JsonNode> it = arrNode.iterator();

//            遍历所有可选项类型
//            先获取第一个
//            while (it.hasNext()) {
//                JsonNode optionInfoNode = it.next();
//                String optionType = optionInfoNode.get("data").toString();
//                JsonNode options = optionInfoNode.get("options");
//                Iterator<JsonNode> optionIt = options.iterator();
//                List<OptionBrief> optionData = new ArrayList<>();
//                //遍历当前类型的option，存到数组里
//                while (optionIt.hasNext()) {
//                    JsonNode optionNode = optionIt.next();
//                    String data = optionNode.get("data").toString();
//                    OptionBrief optionBrief = new OptionBrief(optionType.replace("\"", ""), data.replace("\"", ""));
//                    optionData.add(optionBrief);
//                }
////                    optionMap.put(optionType, optionData);
//
//                optionAll.addAll(optionData);


//                List<Element> element = new ArrayList<>();

//            attribute顺序有么有影响
//
//            从集合A中取一个元素(可不取)，集合B中取一个元素，...，返回所有结果
//            每次循环之前都要将当前集合复制一份
//            组合，所有子集，回溯，第一个取/不取，比组合更复杂，因为取的话还涉及到取哪个。
//            所有子集有个index
//
//            拆出去
//            可以
//                    if (optionMap.get(catalogCode) != null) {
//                        List<Element> curElement = new ArrayList<>(element);
////                        curElement.add();
//                        for (Map.Entry<String, List<String>> entry : optionMap.entrySet()) {
////                            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
//                            List<String> values = entry.getValue();
//                            for () {
//
//                            }
//                        }
//                    }
//
//
//                    public void recurse(List<Element> result, int index1, int index2, List<List<OptionBrief>> option)
//                    recurse(new ArrayList<>(), 0, 0, optionBriefListAll);
//                }
//            }
//                    List<Modelfileinfo> modelFileInfos = modelfileinfoMapper.selectAllByCataloguecode(catalogCode);

//                    String modelCode = generateModelCode(catalogCode, vinDataDto.getFullSpecificationCode());
//                    for (Modelfileinfo modelfileinfo : modelFileInfos) {
//                        if (vinDataDto.getFullSpecificationCode().contains(modelfileinfo.getModelCode())) {
//                            modelCode = modelfileinfo.getModelCode();
//                        }
//                    }
                    List<Modelfileinfo> modelfileinfos = modelfileinfoMapper.selectAllByCataloguecode(catalogCode);
                    Modelfileinfo modelfileinfo = null;

                    modelfileinfos = modelfileinfos.stream().sorted(Comparator.comparing(e -> e.getModelCode().length())).collect(Collectors.toList());
                    for (Modelfileinfo item : modelfileinfos) {
                        if (item.getM1().equals(vinDataDto.getPlant()) && vinDataDto.getFullSpecificationCode().contains(item.getModelCode())) {
                            modelfileinfo = item;
                        }
                    }

                    //todo 开始
                    Element rootElement = DocumentHelper.createElement("interpretation");
                    rootElement.addAttribute("catalog", catalogCode);
                    //todo 不同catalog不一样
//                    rootElement.addAttribute("modelcode", vinDataDto.getFullSpecificationCode().substring(4, 13));
//                    rootElement.addAttribute("modelcode", vinDataDto.getFullSpecificationCode().substring(5, 11));
//                    rootElement.addAttribute("modelcode", modelCode);
                    rootElement.addAttribute("modelcode", modelfileinfo.getModelCode());
                    rootElement.addAttribute("builddate", vinDataDto.getBuildDate());
                    rootElement.addAttribute("area", vinDataDto.getArea());
                    //todo 不传vin试试
//            rootElement.addAttribute("vin", );
//            rootElement.addAttribute("displaybuilddate", vinData.getProductionDate());
                    rootElement.addAttribute("fullSpecificationCode", vinDataDto.getFullSpecificationCode());

                    String optioncode1 = vinDataDto.getOptionCode1();
//                System.out.println("oooccc1" + optioncode1);
                    String optioncode2 = vinDataDto.getOptionCode2();
//                System.out.println("oooccc2" + optioncode2);
//                if (!optioncode1.isEmpty()) {
//                    optionStringBuilder = new StringBuilder();
//                    int index = 5;
//                    optioncode1 += optioncode2;
//                    optionStringBuilder.append(optioncode1.substring(0, 5));
//                    index++;
//                    while (index + 6 <= optioncode1.length()) {
//                        optionStringBuilder.append("|").append(optioncode1.substring(index, index + 6));
//                        index += 6;
//                    }
//                    String optionStr = optionStringBuilder.toString();
//                    //optioncodes排列也不一样
//                    rootElement.addAttribute("optioncodes", optionStr);
//                }

                    String optionStr = generateOptionCode(catalogCode, optioncode1, optioncode2);
                    rootElement.addAttribute("optioncodes", optionStr);
                    rootElement.addAttribute("builddate", vinDataDto.getBuildDate());
                    rootElement.addAttribute("exteriorkeycolorcode", vinDataDto.getExtColor());
                    rootElement.addAttribute("interiorkeycolorcode", vinDataDto.getIntColor());
                    rootElement.addAttribute("plant", vinDataDto.getArea());
                    rootElement.addAttribute("vintag", vinDataDto.getVinTag());
                    rootElement.addAttribute("enginemipcode", vinDataDto.getEngineMipCode());
                    rootElement.addAttribute("transmissionMipCode", vinDataDto.getTransmissionMipCode());
                    rootElement.addAttribute("weathertype", vinDataDto.getWeatherType());
//            rootElement.addAttribute("enginenumber", vinData.getEngineNumber());
                    rootElement.addAttribute("type", catalogTypeMap.get(catalogCode));
//            rootElement.addAttribute();

                    //vin、modelid、color
                    rootElement.addAttribute("level", "2");

                    //todo 结束
                    //正确的生成方式是调用vin
                    //不同catalog,可选项的截取规则不同
                    //vin对应的可选项
                    //根据catalogCode、fullSpecificationCode可以找到对应的对应的可选项
                    //下面这一块要改改
                    //抽一个生成可选项的方法generateOption

                    List<FlatSections> flatSections = flatSectionsMapper.selectAllByCatalogCode(catalogCode);
                    String clientCatalogInformation = flatSections.get(0).getClientCatalogInformation();
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(clientCatalogInformation);
                    com.alibaba.fastjson.JSONArray catalogAttributes = jsonObject.getJSONArray("clientCatalogAttributes");
//                    String catalogCode, String weatherType, String driveType, String fullSpecificationCode, Modelfileinfo modelfileinfo
                    List<ClientCatalogAttributes> clientCatalogAttributes = com.alibaba.fastjson.JSONArray.parseArray(catalogAttributes.toString(), ClientCatalogAttributes.class);
                    //todo 从modelfile中获取majorAttributes

//                    Element avsElement7 = rootElement.addElement("avs");
//                    avsElement7.addAttribute("data", majorAttributes.getTypeDT());
//                    avsElement7.addAttribute("type", "DT");
                    for (ClientCatalogAttributes attribute : clientCatalogAttributes) {
                        if (attribute.getData().equals("01")) {
////                System.out.println(6667);
////                System.out.println(vinDataVo.getMajorAttributes().getType01());
////                System.out.println(vinDataVo.getMajorAttributes().toString());
                            if (modelfileinfo.getAttribute1().equals(".")) {
                                if (attribute.getOptions().size() == 1) {
////                                    jsonObject2.put(attribute.getLabel(), attribute.getOptions().get(0).getLabel());
                                    Element avsElement01 = rootElement.addElement("avs");
                                    avsElement01.addAttribute("data", attribute.getOptions().get(0).getData());
                                    avsElement01.addAttribute("type", "01");
//                                } else {
//                                    jsonObject2.put(attribute.getLabel(), "Unknown");
                                }
                            } else {
//                                jsonObject2.put(attribute.getLabel(), attribute.getOptions().stream().filter(e -> e.getData().equals(vinDataVo.getMajorAttributes().getType01())).map(Option::getLabel).findFirst().get());
                                Element avsElement01 = rootElement.addElement("avs");
                                avsElement01.addAttribute("data", modelfileinfo.getAttribute1());
                                avsElement01.addAttribute("type", "01");
                            }
                        } else if (attribute.getData().equals("02")) {
                            if (modelfileinfo.getAttribute2().equals(".")) {
                                if (attribute.getOptions().size() == 1) {
                                    Element avsElement02 = rootElement.addElement("avs");
                                    avsElement02.addAttribute("data", attribute.getOptions().get(0).getData());
                                    avsElement02.addAttribute("type", "02");
//                                    System.out.println("pppp");
//                                    jsonObject2.put("type02", attribute.getOptions().get(0).getLabel());
//                                } else {
//                                    jsonObject2.put(attribute.getLabel(), "Unknown");
                                }
                            } else {
//                                jsonObject2.put(attribute.getLabel(), attribute.getOptions().stream().filter(e -> e.getData().equals(vinDataVo.getMajorAttributes().getType02())).map(Option::getLabel).findFirst().get());
//                                jsonObject2.put("type02", vinDataVo.getMajorAttributes().getType02());
                                Element avsElement02 = rootElement.addElement("avs");
                                avsElement02.addAttribute("data", modelfileinfo.getAttribute2());
                                avsElement02.addAttribute("type", "02");
                            }
                        } else if (attribute.getData().equals("03")) {
                            if (modelfileinfo.getAttribute3().equals(".")) {
                                if (attribute.getOptions().size() == 1) {
//                                    jsonObject2.put(attribute.getLabel(), attribute.getOptions().get(0).getLabel());
                                    Element avsElement03 = rootElement.addElement("avs");
                                    avsElement03.addAttribute("data", attribute.getOptions().get(0).getData());
                                    avsElement03.addAttribute("type", "03");
//                                } else {
//                                    jsonObject2.put(attribute.getLabel(), "Unknown");
                                }
                            } else {
//                                jsonObject2.put(attribute.getLabel(), attribute.getOptions().stream().filter(e -> e.getData().equals(vinDataVo.getMajorAttributes().getType03())).map(Option::getLabel).findFirst().get());
//                                jsonObject2.put("type03", vinDataVo.getMajorAttributes().getType03());
                                Element avsElement03 = rootElement.addElement("avs");
                                avsElement03.addAttribute("data", modelfileinfo.getAttribute3());
                                avsElement03.addAttribute("type", "03");
                            }
                        } else if (attribute.getData().equals("04")) {
                            if (modelfileinfo.getAttribute4().equals(".")) {
                                if (attribute.getOptions().size() == 1) {
//                                    jsonObject2.put(attribute.getLabel(), attribute.getOptions().get(0).getLabel());
//                                    rootElement.addAttribute("type04", attribute.getOptions().get(0).getData());
//                                } else {
//                                    jsonObject2.put(attribute.getLabel(), "Unknown");
                                    Element avsElement04 = rootElement.addElement("avs");
                                    avsElement04.addAttribute("data", attribute.getOptions().get(0).getData());
                                    avsElement04.addAttribute("type", "04");
                                }
                            } else {
//                                jsonObject2.put(attribute.getLabel(), attribute.getOptions().stream().filter(e -> e.getData().equals(vinDataVo.getMajorAttributes().getType04())).map(Option::getLabel).findFirst().get());
                                Element avsElement04 = rootElement.addElement("avs");
                                avsElement04.addAttribute("data", modelfileinfo.getAttribute4());
                                avsElement04.addAttribute("type", "04");
                            }
                        } else if (attribute.getData().equals("05")) {
                            if (modelfileinfo.getAttribute5().equals(".")) {
                                if (attribute.getOptions().size() == 1) {
//                                    jsonObject2.put(attribute.getLabel(), attribute.getOptions().get(0).getLabel());
                                    Element avsElement05 = rootElement.addElement("avs");
                                    avsElement05.addAttribute("data", attribute.getOptions().get(0).getData());
                                    avsElement05.addAttribute("type", "05");
//                                } else {
//                                    jsonObject2.put(attribute.getLabel(), "Unknown");
                                }
                            } else {
//                                jsonObject2.put(attribute.getLabel(), attribute.getOptions().stream().filter(e -> e.getData().equals(vinDataVo.getMajorAttributes().getType05())).map(Option::getLabel).findFirst().get());
                                Element avsElement05 = rootElement.addElement("avs");
                                avsElement05.addAttribute("data", modelfileinfo.getAttribute5());
                                avsElement05.addAttribute("type", "05");
                            }
                        } else if (attribute.getData().equals("06")) {
                            if (modelfileinfo.getAttribute6().equals(".")) {
                                if (attribute.getOptions().size() == 1) {
//                                    jsonObject2.put(attribute.getLabel(), attribute.getOptions().get(0).getLabel());
                                    Element avsElement06 = rootElement.addElement("avs");
                                    avsElement06.addAttribute("data", attribute.getOptions().get(0).getData());
                                    avsElement06.addAttribute("type", "06");
//                                } else {
//                                    jsonObject2.put(attribute.getLabel(), "Unknown");
                                }
                            } else {
//                                jsonObject2.put(attribute.getLabel(), attribute.getOptions().stream().filter(e -> e.getData().equals(vinDataVo.getMajorAttributes().getType06())).map(Option::getLabel).findFirst().get());
//                                jsonObject2.put("type06", vinDataVo.getMajorAttributes().getType06());
                                Element avsElement06 = rootElement.addElement("avs");
                                avsElement06.addAttribute("data", modelfileinfo.getAttribute6());
                                avsElement06.addAttribute("type", "06");
                            }
                        }
//                        } else if (attribute.getData().equals("WT")) {
////                if (vinDataVo.getMajorAttributes().getTypeWT().equals(".")) {
////                    if (attribute.getOptions().size() == 1) {
////                        jsonObject.put(attribute.getLabel(), attribute.getOptions().get(0).getLabel());
////                    } else {
////                        jsonObject.put(attribute.getLabel(), "Unknown");
////                    }
////                } else {
////                    jsonObject2.put(attribute.getLabel(), attribute.getOptions().stream().filter(e -> e.getData().equals(vinDataVo.getMajorAttributes().getTypeWT())).map(Option::getLabel).findFirst().get());
////                }
//                            jsonObject2.put(attribute.getLabel(), vinDataVo.getWeatherType().isEmpty() ? "Unknown" : vinDataVo.getWeatherType());
//                        } else if (attribute.getData().equals("DT")) {
////                if (vinDataVo.getMajorAttributes().getTypeDT().equals(".")) {
////                    if (attribute.getOptions().size() == 1) {
////                        jsonObject.put(attribute.getLabel(), attribute.getOptions().get(0).getLabel());
////                    } else {
////                        jsonObject.put(attribute.getLabel(), "Unknown");
////                    }
////                } else {
////                    attribute.getOptions().stream().filter(e -> e.getData().equals(vinDataVo.getMajorAttributes().getTypeDT())).map(Option::getLabel).findFirst().get()
//                            jsonObject2.put(attribute.getLabel(), vinDataVo.getDriveType().isEmpty() ? "Unknown" : vinDataVo.getDriveType());
////                }
                    }
                    Element avsElementDT = rootElement.addElement("avs");
                    avsElementDT.addAttribute("data", vinDataDto.getDriveType());
                    avsElementDT.addAttribute("type", "DT");

                    Element avsElementWT = rootElement.addElement("avs");
                    avsElementWT.addAttribute("data", vinDataDto.getWeatherType());
                    avsElementWT.addAttribute("type", "WT");

//            jsonObject2.put("type02", vinDataVo.getMajorAttributes().getType02());
//            jsonObject2.put("type03", vinDataVo.getMajorAttributes().getType03());
//            jsonObject2.put("type04", vinDataVo.getMajorAttributes().getType04());
//            jsonObject2.put("type05", vinDataVo.getMajorAttributes().getType05());
//            jsonObject2.put("type06", vinDataVo.getMajorAttributes().getType06());
//            jsonObject2.put("typeWT", vinDataVo.getMajorAttributes().getTypeWT());
//            jsonObject2.put("typeDT", vinDataVo.getMajorAttributes().getTypeDT());

//                        vinDataVo.setMajorAttributes2(jsonObject2);
//                    }
//                    MajorAttributes majorAttributes = generateMajorAttributes(catalogCode, vinDataDto.getFullSpecificationCode(), vinDataDto.getWeatherType(), vinDataDto.getDriveType(), modelfileinfo);
//
//                    Element avsElement = rootElement.addElement("avs");
//                    avsElement.addAttribute("data", majorAttributes.getType01());
//                    avsElement.addAttribute("type", "01");
//
//                    Element avsElement2 = rootElement.addElement("avs");
//                    avsElement2.addAttribute("data", majorAttributes.getType02());
//                    avsElement2.addAttribute("type", "02");
//
//                    Element avsElement3 = rootElement.addElement("avs");
//                    avsElement3.addAttribute("data", majorAttributes.getType03());
//                    avsElement3.addAttribute("type", "03");
//
//                    Element avsElement4 = rootElement.addElement("avs");
//                    avsElement4.addAttribute("data", majorAttributes.getType04());
//                    avsElement4.addAttribute("type", "04");
//
//                    Element avsElement5 = rootElement.addElement("avs");
//                    avsElement5.addAttribute("data", majorAttributes.getType05());
//                    avsElement5.addAttribute("type", "05");
//
//                    Element avsElement6 = rootElement.addElement("avs");
//                    avsElement6.addAttribute("data", majorAttributes.getType06());
//                    avsElement6.addAttribute("type", "06");
//
////              todo 参数文件记得带上type06
//                    Element avsElement7 = rootElement.addElement("avs");
//                    avsElement7.addAttribute("data", majorAttributes.getTypeDT());
//                    avsElement7.addAttribute("type", "DT");
//
//                    Element avsElement8 = rootElement.addElement("avs");
//                    avsElement8.addAttribute("data", majorAttributes.getTypeWT());
//                    avsElement8.addAttribute("type", "WT");

//                List<String> sectionList = new ArrayList<>();
                    sb = new StringBuilder();
                    JSONArray arr = new JSONArray();
                    for (EpcSection epcSection : epcSections) {
//                System.out.println(epcSection.getCode());

                        //todo 开始注释
//                    sectionList.add(epcSection.getCode());
                        arr.add(epcSection.getCode());
                        // writeToFile(sb.toString(), catalogCode);
                        //todo 取消注释

                    }

                    sb.append(catalogCode).append(" ").append(arr).append(" ").append(rootElement.asXML()).append("\n");
                    stopWatch.start("写入耗时");
                    writeToFile(sb.toString(), catalogCode);
                    stopWatch.stop();
//                rootElement.addAttribute("isDefaultApplicability", "0");
//                rootElement.addAttribute("attributeValidationRequired", "0");
//                elementList.add(rootElement);
//            rawElementList.add(rootElement);

//            String a = "";


//                if (beginYearMap.containsKey(catalogCode)) {
//                    Integer endYear = "".equals(endYearMap.get(catalogCode)) ? 2025 : Integer.valueOf(endYearMap.get(catalogCode));
//                    List<String> dateList = generateDateParam(Integer.parseInt(beginYearMap.get(catalogCode)), endYear);
//                    //添加element
//                    for (String date : dateList) {
//                        Element element = rootElement.createCopy();
//                        element.addAttribute("builddate", date);
//                        element.addAttribute("displaybuilddate", date);
//                        rawElementList.add(element);
//                    }
//                }

//            某个时刻elementlist为空
//            for (Element element : rawElementList) {
//                recurse(element, 0, optionAll);
//            }
//            elementList.add(rootElement);

                    //缺少sectioncode

//            for (Element element : elementList) {
//                for (String sectionCode : sectionSet) {
//                    String row = catalogCode + " " + sectionCode + " " + element.asXML() + " " + 0 + " " + 1;
//                    fileWriter.println(row);
//                }
//            }

                }
                start = vinDataDtos.get(vinDataDtos.size() - 1).getRowNum();
//            System.out.println("aaa" + start);
                stopWatch.start("查询耗时");
                vinDataDtos = vinDataMapper.selectByCatalog2(catalogCode, start);
                stopWatch.stop();
//            System.out.println("bbb" + vinDataDtos.size());
//            if (vinDataDtos.size() > 0) {
//                System.out.println("kkk" + vinDataDtos.get(vinDataDtos.size() - 1).toString());
//            }
            }

            closeWriter();
            System.out.println(stopWatch.prettyPrint());
            //todo 结束注释
            //允许有一定的延迟
            System.out.println("Finished:" + catalogCode);
        }
    }

    public static String generateModelCode(String catalogCode, String fullSpecificationCode) {
        String modelCode;
//        HMA2B0PA06
        if (catalogCode.startsWith("NAS")) {
            //如果是NAS3409500
            if (catalogCode.equals("NAS3409500")) {
                modelCode = fullSpecificationCode.substring(5, 11);
            } else if (catalogCode.equals("NAS2809100")) {
                modelCode = fullSpecificationCode.substring(5, 10);
            } else {
                modelCode = fullSpecificationCode.substring(5, 12);
            }
        } else if (catalogCode.startsWith("HMA")) {
            //todo fullSpecificationCode的长度大于12
            if (catalogCode.equals("HMA3309100")) {
                modelCode = fullSpecificationCode.substring(5, 10);
                System.out.println(modelCode);
//                KM8SG13D16U022689

            } else if (catalogCode.equals("HMA4J0PA06") || catalogCode.equals("HMA0W0PA06")
                    || catalogCode.equals("HMA390PA99") || catalogCode.equals("HMA2B0PA06")
                    || catalogCode.equals("HMA2C0PA01") || catalogCode.equals("HMA250PA00")
                    || catalogCode.equals("HMA3L0PA05") || catalogCode.equals("HMA3K0PA04")
                    || catalogCode.equals("HMA1E0PA06") || catalogCode.equals("HMA2H0PA06")) {
//                || catalogCode.equals("HMA380PA02")
                modelCode = fullSpecificationCode.substring(6, 13);
            } else if (catalogCode.equals("HMA380PA02")) {
                modelCode = fullSpecificationCode.substring(5, 12);
            } else {
                modelCode = fullSpecificationCode.substring(6, 12);
            }
        } else {
            if (catalogCode.equals("KHMAPE617") || catalogCode.equals("KHMAPG216")) {
                modelCode = fullSpecificationCode.substring(4, 16);
            } else {
                modelCode = fullSpecificationCode.substring(4, 13);
            }
        }
        return modelCode;
    }

//    public void setMajorAttributes(String catalogCode, String fullSpecificationCode, String weatherType, String driveType, Modelfileinfo modelfileinfo) {
////        this.setBodyType(fullSpecificationCode.substring(6, 8));
//        MajorAttributes majorAttributes;
//        //todo 不同catalog可选项的设置方式不一样
//
//        if (modelfileinfo == null) {
//            if (catalogCode.startsWith("NAS")) {
//                majorAttributes = new MajorAttributes(fullSpecificationCode.substring(7, 8),
//                        fullSpecificationCode.substring(8, 9), fullSpecificationCode.substring(9, 10),
//                        fullSpecificationCode.substring(5, 6),
//                        fullSpecificationCode.substring(11, 12),
//                        "U", weatherType, driveType);
//                this.setMajorAttributes(majorAttributes);
//            } else if (catalogCode.startsWith("HMA")) {
////                if (catalogCode.equals("HMA3309100")) {
////                    majorAttributes = new MajorAttributes(fullSpecificationCode.substring());
////                } else {
//                majorAttributes = new MajorAttributes(fullSpecificationCode.substring(8, 9),
//                        fullSpecificationCode.substring(9, 10), fullSpecificationCode.substring(10, 11),
//                        fullSpecificationCode.substring(11, 12), fullSpecificationCode.substring(12, 13),
//                        null, weatherType, driveType);
////                }
//                this.setMajorAttributes(majorAttributes);
//            } else {
////                if (catalogCode.startsWith("KHMA") || catalogCode.startsWith("MHM"))
//                majorAttributes = new MajorAttributes(fullSpecificationCode.substring(6, 8),
//                        fullSpecificationCode.substring(8, 10), fullSpecificationCode.substring(10, 11),
//                        fullSpecificationCode.substring(11, 12), fullSpecificationCode.substring(12, 13),
//                        null, weatherType, driveType);
//                this.setMajorAttributes(majorAttributes);
//            }
//        } else {
//            System.out.println("0000" + modelfileinfo.toString());
//            majorAttributes = new MajorAttributes(modelfileinfo.getAttribute1(), modelfileinfo.getAttribute2(),
//                    modelfileinfo.getAttribute3(), modelfileinfo.getAttribute4(), modelfileinfo.getAttribute5(),
//                    modelfileinfo.getAttribute6(), weatherType, driveType);
//            this.setMajorAttributes(majorAttributes);
//        }

//        this.setType01(fullSpecificationCode.substring(6, 8));
////        this.setGrade(fullSpecificationCode.substring(8, 10));
//        this.setType02(fullSpecificationCode.substring(8, 10));
////        this.setEngineCapacity(fullSpecificationCode.substring(10, 11));
//        this.setType03(fullSpecificationCode.substring(10, 11));
////        this.setFuelType(fullSpecificationCode.substring(11, 12));
//        this.setType04(fullSpecificationCode.substring(11, 12));
////        this.setTransmission(fullSpecificationCode.substring(12, 13));
//        this.setType05(fullSpecificationCode.substring(12, 13));
//        this.setTypeDT(this.getDriveType());
//        this.setTypeWT(this.getWeatherType());
//    }

    public MajorAttributes generateMajorAttributes(String catalogCode, String fullSpecificationCode, String weatherType, String driveType, Modelfileinfo modelfileinfo) {
        MajorAttributes majorAttributes;
        if (modelfileinfo == null) {
            if (catalogCode.startsWith("NAS")) {
                majorAttributes = new MajorAttributes(fullSpecificationCode.substring(7, 8),
                        fullSpecificationCode.substring(8, 9), fullSpecificationCode.substring(9, 10),
                        fullSpecificationCode.substring(5, 6),
                        fullSpecificationCode.substring(11, 12),
                        "U", weatherType, driveType);
                return majorAttributes;
            } else if (catalogCode.startsWith("HMA")) {
//                if (catalogCode.equals("HMA3309100")) {
//                    majorAttributes = new MajorAttributes(fullSpecificationCode.substring());
//                } else {
                majorAttributes = new MajorAttributes(fullSpecificationCode.substring(8, 9),
                        fullSpecificationCode.substring(9, 10), fullSpecificationCode.substring(10, 11),
                        fullSpecificationCode.substring(11, 12), fullSpecificationCode.substring(12, 13),
                        null, weatherType, driveType);
//                }
                return majorAttributes;
            } else {
//                if (catalogCode.startsWith("KHMA") || catalogCode.startsWith("MHM"))
                majorAttributes = new MajorAttributes(fullSpecificationCode.substring(6, 8),
                        fullSpecificationCode.substring(8, 10), fullSpecificationCode.substring(10, 11),
                        fullSpecificationCode.substring(11, 12), fullSpecificationCode.substring(12, 13),
                        null, weatherType, driveType);
                return majorAttributes;
            }
        } else {
//            System.out.println("0000" + modelfileinfo.toString());
            majorAttributes = new MajorAttributes(modelfileinfo.getAttribute1(), modelfileinfo.getAttribute2(),
                    modelfileinfo.getAttribute3(), modelfileinfo.getAttribute4(), modelfileinfo.getAttribute5(),
                    modelfileinfo.getAttribute6(), weatherType, driveType);
//            this.setMajorAttributes(majorAttributes);
            return majorAttributes;
        }
    }

    public static void main(String[] args) {
//        String catalogCode = "HMA0W0PA06";
//        String option1 = "2DA 2GB 3AB 3AC 3GZ 3LA 5AY 5AZ 5DE 5EG 5FB 5HK 5NA 5NC 5XT 6CA 8AP 8BH 8DE 8DG 8DV 8ED 8EE 8FJ 8FP 8FS 8GB 8GH 8GS 8HA 8HD 8HR 8JP 8KD 8KN 8LA 8MH 8NA 8PP 8QA 8RB 8RL 8RM 8SA 8SF 8SG 8TA 8TB 8UC 8UP 8US 8WC 8WD 8WE 8WK 8YC 9AA 9AQ 9BC 9BD 9CE 9EF 9FP 9GA 9HB 9JC 9KB 9LA 9LN 9MA 9PC 9QA 9RM 9RP 9UA 9VP 9VT 9XP 9YB 9ZD W30";
//        String option2 = "2SB 3JE 5JK FED";
//        String optionCode = generateOptionCode(catalogCode, option1, option2);
//        System.out.println(optionCode.equals("FED|W30|2DA|2GB|2SB|3AB|3AC|3GZ|3JE|3LA|5AY|5AZ|5DE|5EG|5FB|5HK|5JK|5NA|5NC|5XT|6CA|8AP|8BH|8DE|8DG|8DV|8ED|8EE|8FJ|8FP|8FS|8GB|8GH|8GS|8HA|8HD|8HR|8JP|8KD|8KN|8LA|8MH|8NA|8PP|8QA|8RB|8RL|8RM|8SA|8SF|8SG|8TA|8TB|8UC|8UP|8US|8WC|8WD|8WE|8WK|8YC|9AA|9AQ|9BC|9BD|9CE|9EF|9FP|9GA|9HB|9JC|9KB|9LA|9LN|9MA|9PC|9QA|9RM|9RP|9UA|9VP|9VT|9XP|9YB|9ZD"));
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        List<String> subList = list.subList(0, 2);
        System.out.println(subList.get(1));

    }



    //将字母设置得比数字小
    public static String generateOptionCode(String catalog, String option1, String option2) {
        if (StringUtils.isNullOrEmpty(option1) && StringUtils.isNullOrEmpty(option2)) {
            return "";
        }

        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        if (catalog.startsWith("HMA")) {
            //option1四位一切，最后的剩几位，保留几位
            //1234
            for (int i = 0; i < option1.length(); i = i + 4) {
//                0123
                if (i + 4 <= option1.length()) {
                    result.add(option1.substring(i, i + 4));
                } else {
                    result.add(option1.substring(i, option1.length()));
                }
            }
            //option2四位一切
            for (int i = 0; i < option2.length(); i = i + 4) {
                if (i + 4 <= option2.length())  {
                    result.add(option2.substring(i, i + 4));
                } else {
                    result.add(option2.substring(i, option2.length()));
                }
            }
            //去掉空格
            //放数组
            //排序
            result = result.stream().sorted(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    for (int i = 0; i < o1.length(); i++) {
                        if ((o1.charAt(i) >= '0' && o1.charAt(i) <= '9') && (o2.charAt(i) >= '0' && o2.charAt(i) <= '9')) {
                            if (o1.charAt(i) > o2.charAt(i)) {
                                return 1;
                            } else if (o1.charAt(i) == o2.charAt(i)) {
                                continue;
                            } else {
                                return -1;
                            }
                        } else if (o1.charAt(i) >= '0' && o1.charAt(i) <= '9') {
                            return 1;
                        } else if (o2.charAt(i) >= '0' && o2.charAt(i) <= '9') {
                            return -1;
                        } else {
                            if (o1.charAt(i) > o2.charAt(i)) {
                                return 1;
                            } else if (o1.charAt(i) == o2.charAt(i)) {
                                continue;
                            } else {
                                return -1;
                            }
                        }
                    }
                    return 0;
                }
            }).map(e -> e.trim()).collect(Collectors.toList());
        } else if (catalog.startsWith("NAS")) {
            String[] optionCodes1 = option1.split(" ");
            String[] optionCodes2 = option2.split(" ");
            result.addAll(Arrays.asList(optionCodes1));
            result.addAll(Arrays.asList(optionCodes2));
            result = result.stream().sorted(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    for (int i = 0; i < o1.length(); i++) {
                        if ((o1.charAt(i) >= '0' && o1.charAt(i) <= '9') && (o2.charAt(i) >= '0' && o2.charAt(i) <= '9')) {
                            if (o1.charAt(i) > o2.charAt(i)) {
                                return 1;
                            } else if (o1.charAt(i) == o2.charAt(i)) {
                                continue;
                            } else {
                                return -1;
                            }
                        } else if (o1.charAt(i) >= '0' && o1.charAt(i) <= '9') {
                            return 1;
                        } else if (o2.charAt(i) >= '0' && o2.charAt(i) <= '9') {
                            return -1;
                        } else {
                            if (o1.charAt(i) > o2.charAt(i)) {
                                return 1;
                            } else if (o1.charAt(i) == o2.charAt(i)) {
                                continue;
                            } else {
                                return -1;
                            }
                        }
                    }
                    return 0;
                }
            }).collect(Collectors.toList());
        } else {
            for (int i = 0; i < option1.length(); i = i + 6) {
//                0123
                if (i + 6 <= option1.length()) {
                    result.add(option1.substring(i, i + 6));
                } else {
                    result.add(option1.substring(i, option1.length()));
                }
            }
            //option2四位一切
            for (int i = 0; i < option2.length(); i = i + 6) {
                if (i + 6 <= option2.length())  {
                    result.add(option2.substring(i, i + 6));
                } else {
                    result.add(option2.substring(i, option2.length()));
                }
            }
            result = result.stream().sorted(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    for (int i = 0; i < o1.length(); i++) {
                        if ((o1.charAt(i) >= '0' && o1.charAt(i) <= '9') && (o2.charAt(i) >= '0' && o2.charAt(i) <= '9')) {
                            if (o1.charAt(i) > o2.charAt(i)) {
                                return 1;
                            } else if (o1.charAt(i) == o2.charAt(i)) {
                                continue;
                            } else {
                                return -1;
                            }
                        } else if (o1.charAt(i) >= '0' && o1.charAt(i) <= '9') {
                            return 1;
                        } else if (o2.charAt(i) >= '0' && o2.charAt(i) <= '9') {
                            return -1;
                        } else {
                            if (o1.charAt(i) > o2.charAt(i)) {
                                return 1;
                            } else if (o1.charAt(i) == o2.charAt(i)) {
                                continue;
                            } else {
                                return -1;
                            }
                        }
                    }
                    return 0;
                }
            }).map(e -> e.trim()).collect(Collectors.toList());
        }
        for (int i = 0; i < result.size(); i++) {
            sb.append(result.get(i)).append("|");
        }
        return sb.substring(0, sb.length() - 1);
    }

    @PostMapping("/generate-param")
    public void generateParam() throws IOException {
        String path = "D:\\\\epc新\\\\getSectionPartsNew.txt";
        java.io.FileReader fileReader = new java.io.FileReader(path);
        Scanner sc = new Scanner(fileReader);
        FileWriter fileWriter = new FileWriter("getSectionPartsParamNewest.txt");
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.startsWith("{")) {
                continue;
            } else {
                fileWriter.println(line);
            }
        }

    }

    //先比对一下
    @PostMapping("/insert-vin-data")
    public void insertVinData() throws IOException {
        //读取数据


        //插入到数据库

    }

//从三个角度出发
//process-monitor、jar包、数据

    //加日志，打印，没结果。
//1.没调用
//2.调用了但是因为某些特殊原因没有打印
//查看FileMonitor，显示了一些jar包，但是不能看到调用了哪些类
//直接看代码，判断是否被调用了，主要是比较抽象，难理解
//光看数据没有用，还是需要了解逻辑
    @PostMapping("/store-get-section-parts-param")
    public void storeGetSectionPartsParam() throws IOException {

//        JsonParser parser = JsonParser.parseString();
        //获取catalog fromyear toyear
//        QueryWrapper catalogQuery = Wrappers.query();
//        catalogQuery.select("catalogCode, catalogDataTypeDescription, fromYear, toYear");
        ObjectMapper mapper = new ObjectMapper();
        List<Catalog> catalogList = catalogMapper.selectAll();
        //catalog类型
        Map<String, String> catalogTypeMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogCode, Catalog::getCatalogDataTypeDescription));

        //catalog开始年份
        //改为lambda后不再npe
//        Map<String, String> beginYearMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogcode, e -> e.getFromyear()));

        //catalog结束年份
//        Map<String, String> endYearMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogcode, e -> e.getToyear()));

//        QueryWrapper sectionQuery = Wrappers.query();

        //选一条报错
        //直接从文件读取
//        sectionQuery.select("catalog_code, client_catalog_information");
//        sectionQuery.ge("id", 1);
//        sectionQuery.le("id", 41);
//        sectionQuery.eq("catalog_code", "AHMAP0B21");
//        sectionQuery.ne("catalog_code", "HREPAIRKIT");

        //可能原始数据有问题
        List<FlatSections> sectionList = flatSectionsMapper.selectAllByCatalogCode("NAS2909600");


        //参数
//        List<String> parameters = new ArrayList<>();

        //包含sectionCode、catalogCode和interpretation
        //获取section
//        for (FlatSection item : sectionList) {
        //获取catalogcode
        FlatSections item = sectionList.get(0);
//            rawElementList.clear();
        elementList.clear();
        chooseMap.clear();
        String catalogCode = item.getCatalogCode();
//            System.out.println("pppp" + catalogCode);

        //ArrayNode clientCatalogInformation = item.getClientCatalogInformation().withArray("clientCatalogAttributes");
        String clientCatalogInformation = item.getClientCatalogInformation();
        String clientSectionList = item.getClientSectionList();
        JsonNode sectionNode = mapper.readTree(clientSectionList);
        JsonNode sections = sectionNode.get("sections");
//            JsonNode childrenIndex = sections.get("childrenIndex");
        Set<String> sectionSet = new HashSet<>();
        if (sections.isArray()) {
            for (JsonNode objnode : sections) {
                if (objnode.get("childrenIndex").size() == 0) {
                    sectionSet.add(objnode.get("code").toString().substring(1, objnode.get("code").toString().length() - 1));
                }
            }
        }

//            JsonNode sectionNode = item.get("clientSectionList").get("sections");
//            sectionNode.get("childrenIndex");

        JsonNode clientCatalogInformationNode = mapper.readTree(clientCatalogInformation);
        ArrayNode arrNode = clientCatalogInformationNode.withArray("clientCatalogAttributes");
        List<OptionBrief> optionAll = new ArrayList<>();

        if (arrNode.isArray()) {
//                Map<String, List<String>> optionMap = new HashMap<>();

            Iterator<JsonNode> it = arrNode.iterator();

            //遍历所有可选项类型
            //先获取第一个
            while (it.hasNext()) {
                JsonNode optionInfoNode = it.next();
                String optionType = optionInfoNode.get("data").toString();
                JsonNode options = optionInfoNode.get("options");
                Iterator<JsonNode> optionIt = options.iterator();
                List<OptionBrief> optionData = new ArrayList<>();
                //遍历当前类型的option，存到数组里
                while (optionIt.hasNext()) {
                    JsonNode optionNode = optionIt.next();
                    String data = optionNode.get("data").toString();
                    OptionBrief optionBrief = new OptionBrief(optionType.replace("\"", ""), data.replace("\"", ""));
                    optionData.add(optionBrief);
                }
//                    optionMap.put(optionType, optionData);
                optionAll.addAll(optionData);

//              List<Element> element = new ArrayList<>();

                //attribute顺序有么有影响

                //从集合A中取一个元素(可不取)，集合B中取一个元素，...，返回所有结果
                //每次循环之前都要将当前集合复制一份
                //组合，所有子集，回溯，第一个取/不取，比组合更复杂，因为取的话还涉及到取哪个。
                //所有子集有个index

                //拆出去
                //可以
//                    if (optionMap.get(catalogCode) != null) {
//                        List<Element> curElement = new ArrayList<>(element);
////                        curElement.add();
//                        for (Map.Entry<String, List<String>> entry : optionMap.entrySet()) {
////                            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
//                            List<String> values = entry.getValue();
//                            for () {
//
//                            }
//                        }
//                    }


//                    public void recurse(List<Element> result, int index1, int index2, List<List<OptionBrief>> option)
//                    recurse(new ArrayList<>(), 0, 0, optionBriefListAll);
//                }
            }


            Element rootElement = DocumentHelper.createElement("interpretation");
            rootElement.addAttribute("catalog", catalogCode);
            rootElement.addAttribute("type", catalogTypeMap.get(catalogCode));
            rootElement.addAttribute("level", "2");
//                rootElement.addAttribute("isDefaultApplicability", "0");
//                rootElement.addAttribute("attributeValidationRequired", "0");
//                elementList.add(rootElement);
            rawElementList.add(rootElement);


//                if (beginYearMap.containsKey(catalogCode)) {
//                    Integer endYear = "".equals(endYearMap.get(catalogCode)) ? 2025 : Integer.valueOf(endYearMap.get(catalogCode));
//                    List<String> dateList = generateDateParam(Integer.parseInt(beginYearMap.get(catalogCode)), endYear);
//                    //添加element

//                    for (String date : dateList) {
//                        Element element = rootElement.createCopy();
//                        element.addAttribute("builddate", date);
//                        element.addAttribute("displaybuilddate", date);
//                        rawElementList.add(element);
//                    }
//                }

            //某个时刻elementlist为空
            for (Element element : rawElementList) {
                recurse(element, 0, optionAll);
            }
            elementList.add(rootElement);

            //缺少sectioncode
            FileWriter fileWriter = new FileWriter("SectionPartParams-NAS2909600-newest", true);
            for (Element element : elementList) {
                for (String sectionCode : sectionSet) {
                    String row = catalogCode + " " + sectionCode + " " + element.asXML() + " " + 0 + " " + 1;
                    fileWriter.println(row);
                }
            }
            //允许有一定的延迟
            fileWriter.close();
        }
        System.out.println("finished");
    }
//    }
//
//    //看一下section和日期有关吗？
//    //各种数据混杂在一起
//    @PostMapping("/store-option-param")
//    public void storeOptionParam() throws IOException {
//        //获取catalog fromyear toyear
//        QueryWrapper catalogQuery = Wrappers.query();
//        catalogQuery.select("catalogCode, catalogDataTypeDescription, fromYear, toYear");
//        List<Catalog> catalogList = catalogMapper.selectList(catalogQuery);
//        //catalog类型
//        Map<String, String> catalogTypeMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogCode, Catalog::getCatalogDataTypeDescription));
//
//        //catalog开始年份
//        //改为lambda后不再npe
//        Map<String, String> beginYearMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogCode, e -> e.getFromYear()));
//
//        //catalog结束年份
//        Map<String, String> endYearMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogCode, e -> e.getToYear()));
//
//        QueryWrapper sectionQuery = Wrappers.query();
//
//        //选一条报错
//        //直接从文件读取
//        sectionQuery.select("catalog_code, client_catalog_information");
////        sectionQuery.ge("id", 1);
////        sectionQuery.le("id", 41);
//        sectionQuery.eq("catalog_code", "HREPAIRKIT");
//        List<FlatSection> sectionList = flatSectionMapper.selectList(sectionQuery);
//
//        //参数
////        List<String> parameters = new ArrayList<>();
//
//        //获取section
//        //从191到300
//        for (FlatSection item : sectionList) {
//            //获取catalogcode
//            rawElementList.clear();
//            elementList.clear();
//            chooseMap.clear();
//            String catalogCode = item.getCatalogCode();
//            System.out.println("pppp" + catalogCode);
//            ArrayNode clientCatalogInformation = item.getClientCatalogInformation().withArray("clientCatalogAttributes");
//            List<OptionBrief> optionAll = new ArrayList<>();
//
//            if (clientCatalogInformation.isArray()) {
////                Map<String, List<String>> optionMap = new HashMap<>();
//
//                Iterator<JsonNode> it = clientCatalogInformation.iterator();
//
//
//                //遍历所有可选项类型
//                //先获取第一个
//                while (it.hasNext()) {
//                    JsonNode optionInfoNode = it.next();
//                    String optionType = optionInfoNode.get("data").toString();
//                    JsonNode options = optionInfoNode.get("options");
//                    Iterator<JsonNode> optionIt = options.iterator();
//                    List<OptionBrief> optionData = new ArrayList<>();
//                    //遍历当前类型的option，存到数组里
//                    while (optionIt.hasNext()) {
//                        JsonNode optionNode = optionIt.next();
//                        String data = optionNode.get("data").toString();
//                        OptionBrief optionBrief = new OptionBrief(optionType.replace("\"", ""), data.replace("\"", ""));
//                        optionData.add(optionBrief);
//                    }
////                    optionMap.put(optionType, optionData);
//
//                    optionAll.addAll(optionData);
//
//
////                    List<Element> element = new ArrayList<>();
//
//                    //attribute顺序有么有影响
//
//                    //从集合A中取一个元素(可不取)，集合B中取一个元素，...，返回所有结果
//                    //每次循环之前都要将当前集合复制一份
//                    //组合，所有子集，回溯，第一个取/不取，比组合更复杂，因为取的话还涉及到取哪个。
//                    //所有子集有个index
//
//                    //拆出去
//                    //可以
////                    if (optionMap.get(catalogCode) != null) {
////                        List<Element> curElement = new ArrayList<>(element);
//////                        curElement.add();
////                        for (Map.Entry<String, List<String>> entry : optionMap.entrySet()) {
//////                            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
////                            List<String> values = entry.getValue();
////                            for () {
////
////                            }
////                        }
////                    }
//
//
////                    public void recurse(List<Element> result, int index1, int index2, List<List<OptionBrief>> option)
////                    recurse(new ArrayList<>(), 0, 0, optionBriefListAll);
////                }
//                }
//
//                Element rootElement = DocumentHelper.createElement("interpretation");
//                rootElement.addAttribute("catalog", catalogCode);
//                rootElement.addAttribute("type", catalogTypeMap.get(catalogCode));
//                rootElement.addAttribute("isDefaultApplicability", "0");
//                rootElement.addAttribute("attributeValidationRequired", "0");
//                rawElementList.add(rootElement);
//                elementList.add(rootElement);
//
//
//                //怎样读取文件
//                if (beginYearMap.containsKey(catalogCode)) {
//                    Integer endYear = "".equals(endYearMap.get(catalogCode)) ? 2025 : Integer.valueOf(endYearMap.get(catalogCode));
//                    List<String> dateList = generateDateParam(Integer.parseInt(beginYearMap.get(catalogCode)), endYear);
//                    //添加element
//                    for (String date : dateList) {
//                        Element element = rootElement.createCopy();
//                        element.addAttribute("builddate", date);
//                        element.addAttribute("displaybuilddate", date);
//                        rawElementList.add(element);
//                        elementList.add(element);
//                    }
//                }
//
//                for (Element element : rawElementList) {
//                    recurse(element, 0, optionAll);
//                }
//
//                FileWriter fileWriter = new FileWriter("searchCatalogWithSections", true);
//                for (Element element : elementList) {
//                    String row = catalogCode + " " + element.asXML();
////                    fileWriter.println(row);
//                }
//                fileWriter.close();
//            }
//        }
//    }


// 多加几层
// 结果会不会很多
// 可以不放到list里面吗？

// 什么容器类可以一边做，一边改
// impl IOC容器
// chatgpt干不了复杂事情

// 递归。设置成全局变量。

// 拆分,先看一下目前写的对不对，先取一条数据

//todo 测一下该方法对不对
//一个element是一个可选项组合
//result 之前选过的可选项组合
//index1 可选项类型
//index2 该类型下的可选项

//从n个集合中，每个集合取0个或1个元素，组合起来，返回所有组合
//递归，传List<List<>>
//从第一个list

//    public class Solution {
//        public ArrayList<ArrayList<Integer>> subsets(int[] S) {
//            ArrayList<ArrayList<Integer>> res=new ArrayList<ArrayList<Integer>>();
//            res.add(new ArrayList<Integer>());
//            ArrayList<Integer> list=new ArrayList<Integer>();
//            //由于需要有顺序，所以先对数组进行排序
//            Arrays.sort(S);
//            dfs(0,S,res,list);
//            return res;
//        }
//
//        public void dfs(int start, int[] S, ArrayList<ArrayList<Integer>> res, ArrayList<Integer> list){
//            if(start>=S.length){
//                return;
//            }
//
//            for(int i=start;i<S.length;i++){
//                //不需要剪枝，直接存结果集
//                list.add(S[i]);
//                res.add(new ArrayList<Integer>(list));
//                dfs(i+1,S,res,list);
//                //退回一步
//                list.remove(list.size()-1);
//            }
//        }
//    }

    public static List<String> generateDateParam(Integer fromYear, Integer toYear) {
        //builddate="20211212" displaybuilddate="20211212"
        //获取fromyear的年末 + toyear的年初

        //若toYear为空，取2025
        int endYear = toYear != null ? toYear : 2025;
        List<String> result = new ArrayList<>();
        String date = "";
        for (int i = fromYear; i <= endYear - 1; i++) {
            date = i + "1231";
            result.add(date);
        }
        result.add(endYear + "0101");
//        calendar.get();
        return result;

    }

    public void recurse(Element curElement, int index1, List<OptionBrief> optionAll) {
//        System.out.println("bbq" + optionAll.size());
        //什么时候返回result
        if (index1 == optionAll.size()) {
            return;
        }

        for (int i = index1; i < optionAll.size(); i++) {
            OptionBrief option = optionAll.get(i);
            if (chooseMap.getOrDefault(option.getType(), false)) {
            } else {
                Element avsElement = curElement.addElement("avs");
                avsElement.addAttribute("data", optionAll.get(i).getData());
                avsElement.addAttribute("type", optionAll.get(i).getType());
                Element elementCopy = curElement.createCopy();
                elementList.add(elementCopy);
                chooseMap.put(option.getType(), true);
                recurse(curElement, i + 1, optionAll);
                chooseMap.put(option.getType(), false);
                curElement.remove(avsElement);
            }
        }

//        List<OptionBrief> ob = option.get(index1);

        // 遍历完所有可选项
        // 什么时候需要添加元素
//        if (index2 == ob.size() - 1) {
//            if (index1 == option.size() - 1) {
//                elementList.add(curElement);
//                return;
//            }
//        }

        //创建一个新的，避免后面的递归修改前面的。
//        List<Element> curResult = new ArrayList<>(result);

        //如果是一维组合
        //能不能简化
//        for (Element element : result) {

        //能不能分解成简单问题
//            for (int i = index1; i < option.size(); i++) {


        //可以不取可选项，这边逻辑选了一个才能继续往下选
        //加一个空可选项

//        public void dfs(int start, int[] S, ArrayList<ArrayList<Integer>> res, ArrayList<Integer> list){
//            if(start>=S.length){
//                return;
//            }
        //
//            for(int i=start;i<S.length;i++){
//                //不需要剪枝，直接存结果集
//                list.add(S[i]);
//                res.add(new ArrayList<Integer>(list));
//                dfs(i+1,S,res,list);
//                //退回一步
//                list.remove(list.size()-1);
//            }


        //无重复元素，但是少了不选元素的情况
        //为什么没有从第二个元素开始的
        //不选的情况下往第二层递归

        //二维合并成一维，选或不选，选了同类型可选项不能重复选，需要创建一个map，表示该类型的是否选过了。没选过才能选。
//                for (int j = 0; j <= ob.size(); j++) {
//                    if (j >= 0 && j < ob.size()) {
////                    if (StringUtils.isBlank(ob.get(j).getData())) {
////                        elementList.add(curElement.createCopy());
////                        if (index1 <= option.size() - 1) {
////                            recurse(curElement, index1 + 1, 0, option);
////                        }
////                    } else {
//                        Element avsElement = curElement.addElement("avs");
//                        avsElement.addAttribute("data", ob.get(j).getData());
//                        avsElement.addAttribute("type", ob.get(j).getType());
//                        Element elementCopy = curElement.createCopy();
//                        curElement.add(elementCopy);
//                        //添加当前元素,选下一种类型的可选项
//
//                        if (index1 <= option.size() - 1) {
//                            recurse(curElement, index1 + 1, 0, option);
//                        }
//
////                    else {
////                        recurse(elementCopy, index1, index2 + 1, option);
////                    }
//                        //还原
//                        //这个东西是不是有问题
//                        //是不是要复制一份
//                        curElement.remove(avsElement);
//                    } else {
//                        recurse(curElement, index1 + 1, 0, option);
//                    }

    }
//                }
//            }
//        }
//todo 设置年份
//    }



//    public static void main(String[] args) {
////        Element root = DocumentHelper.createElement("interpretation");
////        root.addAttribute("catalog", "MHMAPJ018");
////        root.addAttribute("type", "EBOM");
////        root.addElement("avs");
////        root.addElement("avs");
////        System.out.println("MHMAPJ018" + " " + root.asXML());
//        storeOptionParam();
//    }


// 存数据库的话，关联以及查询比较方便。存bigdata，性能比较好，后面还是要聚合起来的
// todo 组合成不同参数,写到参数文件。
//  数据保存到hive表、还是数据库表、catalog和可选项

//先获取各个section的index，获取section和catagory的对应关系
//todo 该方法获取参数
//    @PostMapping("/store-part-search-param")
//    public void generatePartSearchElements() throws IOException {
//        refreshFileList("D:\\section\\SectionData");
//
////        FileReader interpretation = new FileReader("C:\\Users\\admin\\Desktop\\每日\\2024-01\\InterpretationData.txt");
////        List<String> lines = interpretation.readLines();
//
////        for (int i = 0; i < filelist.size(); i++) {
//        FileReader reader = new FileReader(filelist.get(0));
//        // System.out.println("kkk:" + filelist.get(i));
//        String content = reader.readAll();
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode node = mapper.readTree(content);
//        JsonNode clientCatalogAttributesNode = node.get("clientCatalogInformation").get("clientCatalogAttributes");
//
//        //获取catalogCode
//        String catalogCode = node.get("clientSectionList").get("data").toString();
//        catalogCode = catalogCode.substring(1, catalogCode.length() - 1);
//        QueryWrapper catalogQuery = Wrappers.query();
//        catalogQuery.select("catalogCode, catalogDataTypeDescription, fromYear, toYear");
//        catalogQuery.eq("catalogCode","AHMAP0B21");
//        List<Catalog> catalogList = catalogMapper.selectList(catalogQuery);
//        //catalog类型
//        Map<String, String> catalogTypeMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogCode, Catalog::getCatalogDataTypeDescription));
//
//        //catalog开始年份
//        //改为lambda后不再npe
//        Map<String, String> beginYearMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogCode, e -> e.getFromYear()));
//
//        //catalog结束年份
//        Map<String, String> endYearMap = catalogList.stream().collect(Collectors.toMap(Catalog::getCatalogCode, e -> e.getToYear()));
//
//        //根据index获取是否适用，把适用的option存到key里面，value存section信息，K用的是这种
//
//        //还是把适用的option存到子对象里
//
//        //适用关系可以通过某接口获取到
//
//        //catalog下面的section、diagram最好先存到库里，处理起来比较方便。
//        //这样的话怎么存，catalog，下面section，包含index
//
//        JsonNode sectionNode = node.get("clientSectionList").get("sections");
//        sectionNode.get("childrenIndex");
//
//        //todo 开始
//        List<OptionBrief> optionAll = new ArrayList<>();
//
//        if (clientCatalogAttributesNode.isArray()) {
////                Map<String, List<String>> optionMap = new HashMap<>();
//
//            Iterator<JsonNode> it = clientCatalogAttributesNode.iterator();
//
//            //遍历所有可选项类型
//            //先获取第一个
//            while (it.hasNext()) {
//                JsonNode optionInfoNode = it.next();
//                String optionType = optionInfoNode.get("data").toString();
//                JsonNode options = optionInfoNode.get("options");
//                Iterator<JsonNode> optionIt = options.iterator();
//                List<OptionBrief> optionData = new ArrayList<>();
//                //遍历当前类型的option，存到数组里
//                while (optionIt.hasNext()) {
//                    JsonNode optionNode = optionIt.next();
//                    String data = optionNode.get("data").toString();
//                    OptionBrief optionBrief = new OptionBrief(optionType.replace("\"", ""), data.replace("\"", ""));
//                    optionData.add(optionBrief);
//                }
////                    optionMap.put(optionType, optionData);
//
//                optionAll.addAll(optionData);
//            }
//            //todo 结束
//
//            //输出格式：参数格式
//            //可选项参数可以不传
//            //以展开的方式存到mysql
//            //clientsectionlist里面有catalogCode
//
//            //为什么不能整个返回？
//            //因为返回的结果也不是能直接拿来用的。把完整的存下来。
//
//            //把完整的catalog存下来。一共一百多条。
//            //还是非常乱，很难找到有用的信息。
//
//            //生成catalog
//            Set<String> sectionSet = new HashSet<>();
//
//            if (sectionNode.isArray()) {
//                for (JsonNode objnode : sectionNode) {
//                    if (objnode.get("childrenIndex").size() == 0) {
//                        sectionSet.add(objnode.get("code").toString().substring(1, objnode.get("code").toString().length() - 1));
//                    }
//                }
//            }
//
//
////            String interpretationStr = "";
//
////            for (String line : lines) {
////                if (line.contains(catalogCode.toString())) {
////                    interpretationStr = line;
////                }
////            }
//
//            Element rootElement = DocumentHelper.createElement("interpretation");
//            rootElement.addAttribute("catalog", catalogCode);
//            rootElement.addAttribute("type", catalogTypeMap.get(catalogCode));
//            rootElement.addAttribute("level", "2");
//            rawElementList.add(rootElement);
//
//            //怎样读取文件，
//
//            if (beginYearMap.containsKey(catalogCode)) {
//                Integer endYear = "".equals(endYearMap.get(catalogCode)) ? 2025 : Integer.valueOf(endYearMap.get(catalogCode));
//                List<String> dateList = generateDateParam(Integer.parseInt(beginYearMap.get(catalogCode)), endYear);
//                //添加element
//                for (String date : dateList) {
//                    Element element = rootElement.createCopy();
//                    element.addAttribute("builddate", date);
//                    element.addAttribute("displaybuilddate", date);
//                    rawElementList.add(element);
//                }
//            }
//
//            for (Element element : rawElementList) {
//                recurse(element, 0, optionAll);
//            }
//            List<String> rowList = new ArrayList<>();
//
//            FileWriter sectionPartParamsFileWriter = new FileWriter("SectionPartParamsNew", true);
//            String row;
//
//            //code -> section -> catalog,两层循环
//            for (String section : sectionSet) {
//                for (Element element : elementList) {
//                    row = catalogCode + " " + section + " " + element.asXML() + " " + 0 + " " + 1;
//                    rowList.add(row);
//                    sectionPartParamsFileWriter.println(row);
//                }
//            }
//            sectionPartParamsFileWriter.close();
//        }
//
//        //之前是一个个文件处理的，现在合并处理会不会有问题，现在看一下格式有没有问题
////        JsonNode catalogNode = node.get("clientSectionList").get("data");
////        JsonNode sectionNode = node.get("clientSectionList").get("sections");
////        sectionNode.get("childrenIndex");
//    }

    public void refreshFileList(String strPath) {
        File dir = new File(strPath);
        File[] files = dir.listFiles();

        if (files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                refreshFileList(files[i].getAbsolutePath());
            } else {
                String strFileName = files[i].getAbsolutePath();
                filelist.add(strFileName);
            }
        }
    }

}